package school.sptech.noticias_individual;



public class Usuario {
    private Integer id;
    private String nick;
    private String email;
    private String senha;

    public Usuario(String nick, String email, String senha, Integer id) {
        this.nick = nick;
        this.email = email;
        this.senha = senha;
        this.id = id;
    }

    public Usuario() {

    }


    public Integer getId(){
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
