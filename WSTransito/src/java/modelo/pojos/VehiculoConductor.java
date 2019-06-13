package modelo.pojos;

/**
 *
 * @author Eduardo Rosas Rivera.
 */
public class VehiculoConductor {
    private Integer idVehiculoConductor;
    private Integer idVehiculo;
    private Integer idConductor;
    private String propiedad;

    /**
     * @return the idVehiculoConductor
     */
    public Integer getIdVehiculoConductor() {
        return idVehiculoConductor;
    }

    /**
     * @param idVehiculoConductor the idVehiculoConductor to set
     */
    public void setIdVehiculoConductor(Integer idVehiculoConductor) {
        this.idVehiculoConductor = idVehiculoConductor;
    }

    /**
     * @return the idVehiculo
     */
    public Integer getIdVehiculo() {
        return idVehiculo;
    }

    /**
     * @param idVehiculo the idVehiculo to set
     */
    public void setIdVehiculo(Integer idVehiculo) {
        this.idVehiculo = idVehiculo;
    }

    /**
     * @return the idConductor
     */
    public Integer getIdConductor() {
        return idConductor;
    }

    /**
     * @param idConductor the idConductor to set
     */
    public void setIdConductor(Integer idConductor) {
        this.idConductor = idConductor;
    }

    /**
     * @return the propiedad
     */
    public String getPropiedad() {
        return propiedad;
    }

    /**
     * @param propiedad the propiedad to set
     */
    public void setPropiedad(String propiedad) {
        this.propiedad = propiedad;
    }
    
    
}
