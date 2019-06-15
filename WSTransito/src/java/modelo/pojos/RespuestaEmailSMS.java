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
public class RespuestaEmailSMS {
    private String smscode;
    private boolean emailstatus;
    
    public RespuestaEmailSMS() {}
    
    /**
     * @return the smscode
     */
    public String getSmscode() {
        return smscode;
    }

    /**
     * @param smscode the smscode to set
     */
    public void setSmscode(String smscode) {
        this.smscode = smscode;
    }

    /**
     * @return the emailstatus
     */
    public boolean isEmailstatus() {
        return emailstatus;
    }

    /**
     * @param emailstatus the emailstatus to set
     */
    public void setEmailstatus(boolean emailstatus) {
        this.emailstatus = emailstatus;
    }
    
}
