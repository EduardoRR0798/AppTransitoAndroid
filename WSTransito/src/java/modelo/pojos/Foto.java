/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.pojos;

import java.time.LocalDateTime;

/**
 *
 * @author Razer
 */
public class Foto {
    
    private Integer idFoto;
    private byte[] fotoBytes;
    private String tiempocreacion;

    public Foto() {
    }

    public Integer getIdFoto() {
        return idFoto;
    }

    public void setIdFoto(Integer idFoto) {
        this.idFoto = idFoto;
    }

    public byte[] getFotoBytes() {
        return fotoBytes;
    }

    public void setFotoBytes(byte[] fotoBytes) {
        this.fotoBytes = fotoBytes;
    }

    public String getTiempocreacion() {
        return tiempocreacion;
    }

    public void setTiempocreacion(String tiempocreacion) {
        this.tiempocreacion = tiempocreacion;
    }
    
}
