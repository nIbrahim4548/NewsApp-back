package school.sptech.noticias_individual;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.web.bind.annotation.*;

import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/Noticias")
public class NoticiasController {

    private final JdbcTemplate jdbcTemplate;

    public NoticiasController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // GETTERS
    @GetMapping
    public ResponseEntity<List<Noticia>> listarnoticias() {
        String sql = "SELECT n.*, u.nick AS autorNome " +
                "FROM noticia n LEFT JOIN usuario u ON n.autorid = u.id " +
                "ORDER BY n.data DESC";

        List<Noticia> noticias = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Noticia.class));

        return ResponseEntity.status(200).body(noticias);
    }

    @GetMapping("/minhas") //GET /Noticias/minhas?autorId=5
    public ResponseEntity<List<Map<String, Object>>> listarMinhasNoticias(@RequestParam Integer autorId) {

        String sqlNoticias = "SELECT n.*, u.nick AS autorNome " +
                "FROM noticia n LEFT JOIN usuario u ON n.autorid = u.id " +
                "WHERE n.autorid = ? ORDER BY n.data DESC";

        List<Noticia> noticias = jdbcTemplate.query(
                sqlNoticias, new BeanPropertyRowMapper<>(Noticia.class), autorId);

        List<Map<String, Object>> resposta = new java.util.ArrayList<>();
        for (Noticia n : noticias) {
            Integer curtidas = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM curtida_noticia WHERE idNoticia = ?", Integer.class, n.getId());
            Integer comentarios = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM comentario WHERE idNoticia = ?", Integer.class, n.getId());

            Map<String, Object> item = new HashMap<>();
            item.put("noticia", n);
            item.put("curtidas", curtidas);
            item.put("comentarios", comentarios);
            resposta.add(item);
        }

        return ResponseEntity.ok(resposta);
    }

    @GetMapping("/curtidas") //GET /Noticias/curtidas?idUsuario=5
    public ResponseEntity<CurtidasDTO> listarCurtidas(@RequestParam Integer idUsuario) {

        String sqlNoticias = "SELECT idNoticia FROM curtida_noticia WHERE idUsuario = ?";
        List<Integer> noticiasCurtidas = jdbcTemplate.queryForList(sqlNoticias, Integer.class, idUsuario);

        String sqlComentarios = "SELECT idComentario FROM curtida_comentario WHERE idUsuario = ?";
        List<Integer> comentariosCurtidos = jdbcTemplate.queryForList(sqlComentarios, Integer.class, idUsuario);

        CurtidasDTO resposta = new CurtidasDTO(noticiasCurtidas, comentariosCurtidos);
        return ResponseEntity.ok(resposta);
    }

    @GetMapping("/curtidas/contagem")
    public ResponseEntity<ContagemCurtidasDTO> contarCurtidas() {

        String sqlNoticias = "SELECT idNoticia, COUNT(*) as total FROM curtida_noticia GROUP BY idNoticia";
        Map<Integer, Integer> curtidasPorNoticia = new HashMap<>();
        jdbcTemplate.query(sqlNoticias, rs -> {
            curtidasPorNoticia.put(rs.getInt("idNoticia"), rs.getInt("total"));
        });

        String sqlComentarios = "SELECT idComentario, COUNT(*) as total FROM curtida_comentario GROUP BY idComentario";
        Map<Integer, Integer> curtidasPorComentario = new HashMap<>();
        jdbcTemplate.query(sqlComentarios, rs -> {
            curtidasPorComentario.put(rs.getInt("idComentario"), rs.getInt("total"));
        });

        ContagemCurtidasDTO response = new ContagemCurtidasDTO(curtidasPorNoticia, curtidasPorComentario);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/comentario") //GET /Noticias/comentario?idNoticia=5
    public ResponseEntity<List<Comentario>> listarComentarios(@RequestParam Integer idNoticia) {
        String sql = "SELECT c.*, u.nick AS autorNome " +
                "FROM comentario c LEFT JOIN usuario u ON c.idUsuario = u.id " +
                "WHERE c.idNoticia = ? ORDER BY c.id DESC";
        List<Comentario> comentarios = jdbcTemplate.query(
                sql, new BeanPropertyRowMapper<>(Comentario.class), idNoticia);
        return ResponseEntity.ok(comentarios);
    }

    @GetMapping("/comentario/contagem") //GET /Noticias/comentario/contagem
    public ResponseEntity<Map<Integer, Integer>> contarComentarios() {
        String sql = "SELECT idNoticia, COUNT(*) as total FROM comentario GROUP BY idNoticia";
        Map<Integer, Integer> contagem = new HashMap<>();
        jdbcTemplate.query(sql, rs -> {
            contagem.put(rs.getInt("idNoticia"), rs.getInt("total"));
        });
        return ResponseEntity.ok(contagem);
    }

    // POSTS:
    @PostMapping("/curtidas/noticia/{idNoticia}")
    public ResponseEntity<Map<String, Boolean>> toggleCurtidaNoticia(
            @PathVariable Integer idNoticia,
            @RequestParam Integer idUsuario) {

        String sqlDelete = "DELETE FROM curtida_noticia WHERE idUsuario = ? AND idNoticia = ?";
        int linhasAfetadas = jdbcTemplate.update(sqlDelete, idUsuario, idNoticia);

        boolean curtiu;
        if (linhasAfetadas == 0) {
            String sqlInsert = "INSERT INTO curtida_noticia (idUsuario, idNoticia, data) VALUES (?, ?, NOW())";
            jdbcTemplate.update(sqlInsert, idUsuario, idNoticia);
            curtiu = true;
        } else {
            curtiu = false;
        }

        return ResponseEntity.ok(Map.of("curtiu", curtiu));
    }

    @PostMapping("/login")
    public ResponseEntity<Usuario> login(@RequestBody Usuario usuario) {

        if (usuario.getEmail() == null || usuario.getEmail().isEmpty()) {
            return ResponseEntity.status(400).build();
        }
        if (usuario.getSenha() == null || usuario.getSenha().isEmpty()) {
            return ResponseEntity.status(400).build();
        }

        String sql = "SELECT * FROM usuario WHERE email = ? and senha = ?";

        try {
            Usuario usuarioLogin = jdbcTemplate.queryForObject(
                    sql,
                    new BeanPropertyRowMapper<>(Usuario.class),
                    usuario.getEmail(),
                    usuario.getSenha());

            return ResponseEntity.status(200).body(usuarioLogin);
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.status(401).build();
        }
    }

    @PostMapping("/noticia")
    public ResponseEntity<Noticia> postarNoticia(@RequestBody Noticia noticia) {
        if (noticia.getTitulo() == null || noticia.getTitulo().isEmpty()) {
            return ResponseEntity.status(400).build();
        }
        if (noticia.getDescricao() == null || noticia.getDescricao().isEmpty()) {
            return ResponseEntity.status(400).build();
        }
        if (noticia.getDescricao().length() > 4000) {
            return ResponseEntity.status(400).build();
        }
        if (noticia.getTitulo().length() > 200) {
            return ResponseEntity.status(400).build();
        }
        if (noticia.getAutorId() == null) {
            return ResponseEntity.status(400).build();
        }
        if (noticia.getCategoria() == null || noticia.getCategoria().isEmpty()) {
            return ResponseEntity.status(400).build();
        }

        String sql = "INSERT INTO noticia (titulo, descricao, data, autorid, categoria) VALUES (?, ?, ?, ?, ?)";

        KeyHolder keyholder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, noticia.getTitulo());
            ps.setString(2, noticia.getDescricao());
            ps.setObject(3, noticia.getData());
            ps.setInt(4, noticia.getAutorId());
            ps.setString(5, noticia.getCategoria());
            return ps;
        }, keyholder);

        int idGerado = keyholder.getKeyAs(Integer.class);
        noticia.setId(idGerado);
        return ResponseEntity.status(201).body(noticia);
    }

    @PostMapping("/comentario")
    public ResponseEntity<Comentario> postarComentario(@RequestBody Comentario comentario) {

        if (comentario.getConteudo() == null || comentario.getConteudo().isEmpty()) {
            return ResponseEntity.status(400).build();
        }
        if (comentario.getConteudo().length() > 1000) {
            return ResponseEntity.status(400).build();
        }
        if (comentario.getIdUsuario() == null) {
            return ResponseEntity.status(400).build();
        }
        if (comentario.getIdNoticia() == null) {
            return ResponseEntity.status(400).build();
        }

        String sql = "INSERT INTO comentario (idUsuario, idNoticia, conteudo) VALUES (?, ?, ?)";

        KeyHolder keyholder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setInt(1, comentario.getIdUsuario());
            ps.setInt(2, comentario.getIdNoticia());
            ps.setString(3, comentario.getConteudo());
            return ps;
        }, keyholder);

        int idGerado = keyholder.getKeyAs(Integer.class);
        comentario.setId(idGerado);
        return ResponseEntity.status(201).body(comentario);
    }

    @PostMapping("/usuario")
    public ResponseEntity<Object> postarUsuario(@RequestBody Usuario usuario) {

        if(usuario.getEmail() == null || usuario.getEmail().isEmpty()){
            return ResponseEntity.status(400).build();
        }
        if (usuario.getSenha() == null || usuario.getSenha().isEmpty()) {
            return ResponseEntity.status(400).build();
        }
        if (usuario.getSenha() == null || usuario.getSenha().length() < 4) {
            return ResponseEntity.status(400).build();
        }
        if (usuario.getNick() == null || usuario.getNick().isEmpty()) {
            return ResponseEntity.status(400).build();
        }

        Integer emailExistente = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM usuario WHERE email = ?", Integer.class, usuario.getEmail());
        if (emailExistente != null && emailExistente > 0) {
            return ResponseEntity.status(409).body(Map.of("erro", "Este e-mail já está cadastrado."));
        }

        Integer nickExistente = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM usuario WHERE nick = ?", Integer.class, usuario.getNick());
        if (nickExistente != null && nickExistente > 0) {
            return ResponseEntity.status(409).body(Map.of("erro", "Este nome de usuário já está em uso."));
        }

        String sql = "INSERT INTO usuario (nick, email, senha) VALUES (?, ?, ?)";

        KeyHolder keyholder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, usuario.getNick());
            ps.setString(2, usuario.getEmail());
            ps.setString(3, usuario.getSenha());
            return ps;
        }, keyholder);
        Integer idGerado = keyholder.getKeyAs(Integer.class);
        usuario.setId(idGerado);
        return ResponseEntity.status(201).body(usuario);
    }

    //PUTS
    @PutMapping("/noticia/{id}")
    public ResponseEntity<Noticia> atualizarNoticia(@PathVariable Integer id, @RequestBody Noticia noticia) {
        if (noticia.getTitulo() == null || noticia.getTitulo().isEmpty()) {
            return ResponseEntity.status(400).build();
        }
        if (noticia.getDescricao() == null || noticia.getDescricao().isEmpty()) {
            return ResponseEntity.status(400).build();
        }
        if (noticia.getAutorId() == null) {
            return ResponseEntity.status(400).build();
        }
        if (noticia.getData() == null) {
            return ResponseEntity.status(400).build();
        }
        if (noticia.getCategoria() == null || noticia.getCategoria().isEmpty()) {
            return ResponseEntity.status(400).build();
        }

        String sqlAtualizar = "UPDATE noticia SET titulo = ?, descricao = ?, autorid = ?, data = ?, categoria = ? WHERE id = ?";

        jdbcTemplate.update(sqlAtualizar,
                noticia.getTitulo(),
                noticia.getDescricao(),
                noticia.getAutorId(),
                noticia.getData(),
                noticia.getCategoria(),
                id);

        return ResponseEntity.status(200).body(noticia);
    }

    @PutMapping("/comentario/{id}")
    public ResponseEntity<Comentario> atualizarComentario(@PathVariable Integer id, @RequestBody Comentario comentario) {
        if (comentario.getConteudo() == null || comentario.getConteudo().isEmpty()) {
            return ResponseEntity.status(400).build();
        }
        if (comentario.getIdUsuario() == null) {
            return ResponseEntity.status(400).build();
        }
        if (comentario.getIdNoticia() == null) {
            return ResponseEntity.status(400).build();
        }

        String sqlAtualizar = "UPDATE comentario SET conteudo = ? WHERE id = ?";
        jdbcTemplate.update(sqlAtualizar, comentario.getConteudo(), id);
        return ResponseEntity.status(200).body(comentario);
    }

    //DELETE'S
    @DeleteMapping("/noticia/{id}")
    public ResponseEntity<Noticia> deleteNoticia(@PathVariable Integer id) {
        if (id == null) {
            return ResponseEntity.status(400).build();
        }
        String sql = "DELETE FROM noticia WHERE id = ?";
        jdbcTemplate.update(sql, id);
        return ResponseEntity.status(200).build();
    }

    @DeleteMapping("/comentario/{id}")
    public ResponseEntity<Comentario> deleteComentario(@PathVariable Integer id) {
        if (id == null) {
            return ResponseEntity.status(400).build();
        }
        String sql = "DELETE FROM comentario WHERE id = ?";
        jdbcTemplate.update(sql, id);
        return ResponseEntity.status(200).build();
    }

    @DeleteMapping("/usuario/{id}")
    public ResponseEntity<Usuario> deleteUsuario(@PathVariable Integer id) {
        if (id == null) {
            return ResponseEntity.status(400).build();
        }
        String sql = "DELETE FROM usuario WHERE id = ?";
        jdbcTemplate.update(sql, id);
        return ResponseEntity.status(200).build();
    }
}
