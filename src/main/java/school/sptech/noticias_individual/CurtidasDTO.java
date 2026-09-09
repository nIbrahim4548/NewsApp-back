package school.sptech.noticias_individual;

import java.util.List;

public class CurtidasDTO {
    private List<Integer> noticiasCurtidas;
    private List<Integer> comentariosCurtidos;

    public CurtidasDTO(List<Integer> noticiasCurtidas, List<Integer> comentariosCurtidos) {
        this.noticiasCurtidas = noticiasCurtidas;
        this.comentariosCurtidos = comentariosCurtidos;
    }

    // getters e setters
    public List<Integer> getNoticiasCurtidas() {
        return noticiasCurtidas; }
    public void setNoticiasCurtidas(List<Integer> noticiasCurtidas) {
        this.noticiasCurtidas = noticiasCurtidas; }
    public List<Integer> getComentariosCurtidos() {
        return comentariosCurtidos; }
    public void setComentariosCurtidos(List<Integer> comentariosCurtidos) {
        this.comentariosCurtidos = comentariosCurtidos; }
}