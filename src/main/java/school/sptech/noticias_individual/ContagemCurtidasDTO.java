package school.sptech.noticias_individual;

import java.util.Map;

public class ContagemCurtidasDTO {
    private Map<Integer, Integer> curtidasPorNoticia;
    private Map<Integer, Integer> curtidasPorComentario;

    public ContagemCurtidasDTO(Map<Integer, Integer> curtidasPorNoticia, Map<Integer, Integer> curtidasPorComentario) {
        this.curtidasPorNoticia = curtidasPorNoticia;
        this.curtidasPorComentario = curtidasPorComentario;
    }

    public Map<Integer, Integer> getCurtidasPorNoticia() { return curtidasPorNoticia; }
    public void setCurtidasPorNoticia(Map<Integer, Integer> curtidasPorNoticia) { this.curtidasPorNoticia = curtidasPorNoticia; }
    public Map<Integer, Integer> getCurtidasPorComentario() { return curtidasPorComentario; }
    public void setCurtidasPorComentario(Map<Integer, Integer> curtidasPorComentario) { this.curtidasPorComentario = curtidasPorComentario; }
}
