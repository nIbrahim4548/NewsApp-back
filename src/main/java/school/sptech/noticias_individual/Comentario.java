package school.sptech.noticias_individual;

public class Comentario {

    private Integer id;
    private Integer idUsuario;
    private Integer idNoticia;
    private String conteudo;
    private String autorNome;


    public Comentario(Integer id, String conteudo, String curtidas, Integer idUsuario, Integer idNoticia, String autorNome) {
        this.id = id;
        this.conteudo = conteudo;
        this.idUsuario = idUsuario;
        this.idNoticia = idNoticia;
        this.autorNome = curtidas;
    }

    public Comentario() {
    }

    public String getAutorNome() {
        return autorNome;
    }

    public void setAutorNome(String autorNome) {
        this.autorNome = autorNome;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public Integer getIdNoticia() {
        return idNoticia;
    }

    public void setIdNoticia(Integer idNoticia) {
        this.idNoticia = idNoticia;
    }
}
