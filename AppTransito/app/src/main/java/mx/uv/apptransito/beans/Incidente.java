package mx.uv.apptransito.beans;

import java.util.List;

public class Incidente {

    public static final int ESTADO_PENDIENTE = 1;

    private Integer idIncidente;
    private Float latitud;
    private Float longitud;
    private String fecha;
    private String hora;
    private Integer estado;
    private List<Foto> fotos;

    public Incidente() {
    }

    public Integer getIdIncidente() {
        return idIncidente;
    }

    public void setIdIncidente(Integer idIncidente) {
        this.idIncidente = idIncidente;
    }

    public Float getLatitud() {
        return latitud;
    }

    public void setLatitud(Float latitud) {
        this.latitud = latitud;
    }

    public Float getLongitud() {
        return longitud;
    }

    public void setLongitud(Float longitud) {
        this.longitud = longitud;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public List<Foto> getFotos() {
        return fotos;
    }

    public void setFotos(List<Foto> fotos) {
        this.fotos = fotos;
    }

    @Override
    public String toString() {
        return "Incidente cerca (ID " + idIncidente + ")";
    }
}
