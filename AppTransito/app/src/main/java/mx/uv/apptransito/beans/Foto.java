package mx.uv.apptransito.beans;

public class Foto {

    private Integer idFoto;
    private String fotoBytes;
    private String tiempocreacion;

    public Foto() {
    }

    public Integer getIdFoto() {
        return idFoto;
    }

    public void setIdFoto(Integer idFoto) {
        this.idFoto = idFoto;
    }

    public String getFotoBytes() {
        return fotoBytes;
    }

    public void setFotoBytes(String fotoBytes) {
        this.fotoBytes = fotoBytes;
    }

    public String getTiempocreacion() {
        return tiempocreacion;
    }

    public void setTiempocreacion(String tiempocreacion) {
        this.tiempocreacion = tiempocreacion;
    }
}