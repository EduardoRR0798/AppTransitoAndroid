/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import modelo.dao.FotoDAO;
import modelo.dao.ReporteDAO;
import modelo.pojos.Respuesta;

/**
 * REST Web Service
 *
 * @author Razer
 */
@Path("siniestro")
public class WSSiniestro {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of WSSiniestro
     */
    public WSSiniestro() {
    }

    @POST
    @Path("subirfoto/{idReporte}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    public Respuesta subirFoto(
            @PathParam("idReporte") Integer idReporte,
            byte[] fotoBytes
    ) {
        Respuesta respuesta = new Respuesta();
        respuesta.setError(true);
        if (idReporte == null || !ReporteDAO.existeReporte(idReporte)) {
            respuesta.setMensaje("Reporte no válido o no existe");
        } else if (fotoBytes == null) {
            respuesta.setMensaje("Foto no válida");
        } else if (!FotoDAO.guardar(idReporte, fotoBytes)) {
            respuesta.setMensaje("No se puede guardar foto");
        } else {
            respuesta.setError(false);
            respuesta.setMensaje("Foto guardada");
        }
        return respuesta;
    }
    
}
