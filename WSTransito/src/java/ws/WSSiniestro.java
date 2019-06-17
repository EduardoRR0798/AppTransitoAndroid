/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws;

import java.util.List;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import modelo.dao.ConductorDAO;
import modelo.dao.FotoDAO;
import modelo.dao.IncidenteDAO;
import modelo.dao.ReporteDAO;
import modelo.dao.VehiculoDAO;
import modelo.pojos.Incidente;
import modelo.pojos.Reporte;
import modelo.pojos.Respuesta;
import modelo.pojos.Vehiculo;

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
        } else {
            Integer idGenerado = FotoDAO.guardar(idReporte, fotoBytes);
            if (idGenerado == null) {
                respuesta.setMensaje("No se puede guardar foto");
            } else {
                respuesta.setError(false);
                respuesta.setMensaje("Foto guardada con éxito");
                respuesta.setIdGenerado(idGenerado);
            }
        }
        return respuesta;
    }

    @POST
    @Path("registrarIncidente")
    @Produces(MediaType.APPLICATION_JSON)
    public Respuesta registrarIncidente(
            @FormParam("latitud") Float latitud,
            @FormParam("longitud") Float longitud
    ) {
        Respuesta respuesta = new Respuesta();
        respuesta.setError(true);
        if (latitud == null) {
            respuesta.setMensaje("Latitud no válida");
        } else if (longitud == null) {
            respuesta.setMensaje("Longitud no válida");
        } else {
            Integer idGenerado = IncidenteDAO.registrar(latitud, longitud);
            if (idGenerado == null) {
                respuesta.setMensaje("No se puede registrar el incidente");
            } else {
                respuesta.setError(false);
                respuesta.setMensaje("Incidente registrado con éxito");
                respuesta.setIdGenerado(idGenerado);
            }
        }
        return respuesta;
    }

    @POST
    @Path("levantarReporte")
    @Produces(MediaType.APPLICATION_JSON)
    public Respuesta levantarReporte(
            @FormParam("latitud") Float latitud,
            @FormParam("longitud") Float longitud,
            @FormParam("nombreConductor") String nombreConductor,
            @FormParam("nombreAseguradora") String nombreAseguradora,
            @FormParam("numPoliza") String numPoliza,
            @FormParam("marca") String marca,
            @FormParam("modelo") String modelo,
            @FormParam("color") String color,
            @FormParam("placa") String placa,
            @FormParam("idConductor") Integer idConductor,
            @FormParam("idVehiculo") Integer idVehiculo,
            @FormParam("idIncidente") Integer idIncidente
    ) {
        Respuesta respuesta = new Respuesta();
        respuesta.setError(true);
        if (latitud == null) {
            respuesta.setMensaje("Latitud no válida");
        } else if (longitud == null) {
            respuesta.setMensaje("Longitud no válida");
        } else if (nombreConductor == null || nombreConductor.isEmpty()) {
            respuesta.setMensaje("Nombre de conductor no válido");
        } else if (nombreAseguradora == null || nombreAseguradora.isEmpty()) {
            respuesta.setMensaje("Nombre de aseguradora no válido");
        } else if (numPoliza == null || numPoliza.isEmpty()) {
            respuesta.setMensaje("Número de póliza no válido");
        } else if (marca == null || marca.isEmpty()) {
            respuesta.setMensaje("Marca válida");
        } else if (modelo == null || modelo.isEmpty()) {
            respuesta.setMensaje("Modelo no válido");
        } else if (color == null || color.isEmpty()) {
            respuesta.setMensaje("Color no válido");
        } else if (placa == null || placa.isEmpty()) {
            respuesta.setMensaje("Placa no válida");
        } else if (idConductor == null || ConductorDAO.registrarConductor(idConductor) == null) {
            respuesta.setMensaje("Conductor no válido o no existe");
        } else if (idIncidente == null || !IncidenteDAO.existeIncidente(idIncidente)) {
            respuesta.setMensaje("Incidente no válido o no existe");
        } else {
            boolean encontrado = false;
            if (idVehiculo != null) {
                List<Vehiculo> vehiculos = VehiculoDAO.findByIdConductor(idConductor);
                for (Vehiculo vehiculo : vehiculos) {
                    if (vehiculo.getIdVehiculo().equals(idVehiculo)) {
                        encontrado = true;
                        break;
                    }
                }
            }
            if (!encontrado) {
                respuesta.setMensaje("Vehículo no válido o no existe");
            } else {
                Integer idGenerado = ReporteDAO.registrar(
                        latitud,
                        longitud,
                        nombreConductor,
                        nombreAseguradora,
                        numPoliza,
                        marca,
                        modelo,
                        color,
                        placa,
                        idConductor,
                        idVehiculo,
                        idIncidente
                );
                if (idGenerado == null) {
                    respuesta.setMensaje("No se puede levantar el reporte");
                } else {
                    respuesta.setError(false);
                    respuesta.setMensaje("Reporte levantado con éxito");
                    respuesta.setIdGenerado(idGenerado);
                }
            } 
        }
        return respuesta;
    }

    @GET
    @Path("incidentesCenrca/{latitud}/{longitud}")
    @Produces(MediaType.APPLICATION_JSON)
    public Respuesta obtenerIncidentesCerca(
            @PathParam("latitud") Float latitud,
            @PathParam("longitud") Float longitud
    ) {
        Respuesta respuesta = new Respuesta();
        respuesta.setError(true);
        if (latitud == null) {
            respuesta.setMensaje("Latitud no válida");
        } else if (longitud == null) {
            respuesta.setMensaje("Longitud no válida");
        } else {
            List<Incidente> incidentes = IncidenteDAO.incidentesCerca(latitud, longitud);
            if (incidentes == null || incidentes.isEmpty()) {
                respuesta.setMensaje("No se encontraron incidentes");
            } else {
                respuesta.setError(false);
                respuesta.setMensaje(incidentes.size() + " incidente(s) encontrados");
                respuesta.setIncidentes(incidentes);
            }
        }
        return respuesta;
    }

    @GET
    @Path("historial/{idConductor}")
    @Produces(MediaType.APPLICATION_JSON)
    public Respuesta obtenerHistorial(
            @PathParam("idConductor") Integer idConductor
    ) {
        Respuesta respuesta = new Respuesta();
        respuesta.setError(true);
        if (idConductor == null || ConductorDAO.registrarConductor(idConductor) == null) {
            respuesta.setMensaje("Conductor no válido o no existe");
        } else {
            List<Reporte> reportes = ReporteDAO.historial(idConductor);
            if (reportes == null || reportes.isEmpty()) {
                respuesta.setMensaje("No se encontraron reportes");
            } else {
                respuesta.setError(false);
                respuesta.setMensaje(reportes.size() + " reporte(s) encontrados");
                respuesta.setReportes(reportes);
            }
        }
        return respuesta;
    }

}
