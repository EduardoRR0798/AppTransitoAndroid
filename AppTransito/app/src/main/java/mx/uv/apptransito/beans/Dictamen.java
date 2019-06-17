package mx.uv.apptransito.beans;

public class Dictamen {

    private Integer idDictamen;
    private String folio;
    private String descripcion;
    private String fechahora;

    public Dictamen() {
    }

    public Integer getIdDictamen() {
        return idDictamen;
    }

    public void setIdDictamen(Integer idDictamen) {
        this.idDictamen = idDictamen;
    }

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFechahora() {
        return fechahora;
    }

    public void setFechahora(String fechahora) {
        this.fechahora = fechahora;
    }
}
