package modelo.pojos;

/**
 *
 * @author Eduardo Rosas Rivera.
 */
public class Conductor {
    private Integer idConductor;
    private String nombre;
    private String fechaNacimiento;
    private String numLicencia;
    private String telefono;
    private String contrasenia;
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * @return the fechaNacimiento
     */
    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    /**
     * @param fechaNacimiento the fechaNacimiento to set
     */
    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    /**
     * @return the numLicencia
     */
    public String getNumLicencia() {
        return numLicencia;
    }

    /**
     * @param numLicencia the numLicencia to set
     */
    public void setNumLicencia(String numLicencia) {
        this.numLicencia = numLicencia;
    }

    /**
     * @return the telefono
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * @param telefono the telefono to set
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
     * @return the contrasenia
     */
    public String getContrasenia() {
        return contrasenia;
    }

    /**
     * @param contrasenia the contrasenia to set
     */
    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }
    
    
}
