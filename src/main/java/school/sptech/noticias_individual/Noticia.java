package school.sptech.noticias_individual;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Noticia {

    private Integer id;
    private String titulo;
    private String descricao;
    private LocalDate data;
    private Integer autorId;
    private String categoria;

    public Noticia(Integer id, String descricao, String titulo, LocalDate data, Integer autorId,  String categoria) {
        this.id = id;
        this.descricao = descricao;
        this.titulo = titulo;
        this.data = data;
        this.autorId = autorId;
        this.categoria = categoria;

    }

    public Noticia() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public Integer getAutorId() {
        return autorId;
    }

    public void setAutor(Integer autorId) {
        this.autorId = autorId;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }



}
