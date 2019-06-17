package mx.uv.apptransito.beans;

import java.util.List;

public class RespuestaWS {

    private Boolean error;
    private Integer errorcode;
    private String mensaje;
    private Integer idGenerado;
    private List<Reporte> reportes;
    private List<Incidente> incidentes;
    private List<Vehiculo> vehiculos;

    public RespuestaWS() {
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public Integer getErrorcode() {
        return errorcode;
    }

    public void setErrorcode(Integer errorcode) {
        this.errorcode = errorcode;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Integer getIdGenerado() {
        return idGenerado;
    }

    public void setIdGenerado(Integer idGenerado) {
        this.idGenerado = idGenerado;
    }

    public List<Reporte> getReportes() {
        return reportes;
    }

    public void setReportes(List<Reporte> reportes) {
        this.reportes = reportes;
    }

    public List<Incidente> getIncidentes() {
        return incidentes;
    }

    public void setIncidentes(List<Incidente> incidentes) {
        this.incidentes = incidentes;
    }

    public List<Vehiculo> getVehiculos() {
        return vehiculos;
    }

    public void setVehiculos(List<Vehiculo> vehiculos) {
        this.vehiculos = vehiculos;
    }
}
