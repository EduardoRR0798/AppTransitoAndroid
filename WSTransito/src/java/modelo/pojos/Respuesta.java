/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.pojos;

/**
 *
 * @author Eduar
 */
public class Respuesta {
    private boolean error;
    private Integer errorcode;
    private String mensaje;

    public Respuesta() {}
    /**
     * @return the error
     */
    public boolean isError() {
        return error;
    }

    /**
     * @param error the error to set
     */
    public void setError(boolean error) {
        this.error = error;
    }

    /**
     * @return the errorcode
     */
    public Integer getErrorcode() {
        return errorcode;
    }

    /**
     * @param errorcode the errorcode to set
     */
    public void setErrorcode(Integer errorcode) {
        this.errorcode = errorcode;
    }

    /**
     * @return the mensaje
     */
    public String getMensaje() {
        return mensaje;
    }

    /**
     * @param mensaje the mensaje to set
     */
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
    
}
