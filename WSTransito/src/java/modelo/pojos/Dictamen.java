/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.pojos;

/**
 *
 * @author Razer
 */
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
