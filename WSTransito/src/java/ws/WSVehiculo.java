package ws;

import java.util.List;
import java.util.Objects;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;
import modelo.dao.VehiculoDAO;
import modelo.pojos.Respuesta;
import modelo.pojos.Vehiculo;

/**
 * REST Web Service
 *
 * @author Eduardo Rosas Rivera.
 */
@Path("vehiculos")
public class WSVehiculo {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of Vehiculos
     */
    public WSVehiculo() {
    }

    @PUT
    @Path("registrar")
    @Produces(MediaType.APPLICATION_JSON)
    public Respuesta registrar(
            @FormParam ("marca") String marca,
            @FormParam ("modelo") String modelo,
            @FormParam ("anio") String anio,
            @FormParam ("color") String color,
            @FormParam ("nombreAseguradora") String nombreAseguradora,
            @FormParam ("numPoliza") String numPoliza,
            @FormParam ("placa") String placa,
            @FormParam ("propietario") String propietario,
            @FormParam ("idConductor") Integer idConductor) {
        Respuesta r = new Respuesta();
        Vehiculo v = new Vehiculo();
        v.setMarca(marca);
        v.setModelo(modelo);
        v.setAnio(anio);
        v.setColor(color);
        if (!VehiculoDAO.findByPlacaB(placa)) {
            r.setError(true);
            r.setErrorcode(1);
            r.setMensaje("La placa ya esta registrada");
            return r;
        }
        v.setPlaca(placa);
        v.setNumPoliza(numPoliza);
        if (!Objects.equals(numPoliza, null)) {
            if (!VehiculoDAO.findByPolizaId(v)) {
                r.setError(true);
                r.setErrorcode(1);
                r.setMensaje("El numero de poliza ya se encuentra registrado");
                return r;
            }
        }
        
        v.setNombreAseguradora(nombreAseguradora);
        v.setNumPoliza(numPoliza);
        v.setIdConductor(idConductor);
        v.setPropietario(propietario);
        int fa = VehiculoDAO.registrar(v);
        if (fa != 1) {
            r.setError(true);
            r.setErrorcode(fa);
            r.setMensaje("No se pudo registrar el vehiculo.");
        } else {
            r.setError(false);
            r.setErrorcode(0);
            r.setMensaje("Se ha registrado el vehiculo.");
        }
        return r;
    }
    
    @DELETE
    @Path("eliminarrelacion")
    @Produces(MediaType.APPLICATION_JSON)
    public Respuesta eliminarRelacion(
    @FormParam ("idVehiculo") Integer idVehiculo,
            @FormParam ("idConductor") Integer idConductor) {
        Respuesta r = new Respuesta();
        Vehiculo ve = new Vehiculo();
        ve.setIdConductor(idConductor);
        ve.setIdVehiculo(idVehiculo);
        int fa = 0;
        fa = VehiculoDAO.eliminarRelacion(ve);
        if (fa > 0) {
            r.setError(false);
            r.setErrorcode(0);
            r.setMensaje("El vehiculo se ha eliminado de la lista.");
        } else {
            r.setError(true);
            r.setErrorcode(1);
            r.setMensaje("No se pudo eliminar.");
        }
        return r;
    }
    
    @POST
    @Path("agregarexistente")
    @Produces(MediaType.APPLICATION_JSON)
    public Respuesta agregarVehiculo(
    @FormParam ("idConductor") Integer idConductor,
    @FormParam ("idVehiculo") Integer idVehiculo) {
        Respuesta r = new Respuesta();
        int fa = VehiculoDAO.agregarALista(idConductor, idVehiculo);
        if (fa > 0) {
            r.setError(true);
            r.setErrorcode(0);
            r.setMensaje("Vehiculo registrado exitosamente.");
        } else {
            r.setError(true);
            r.setErrorcode(1);
            r.setMensaje("No se pudo registrar el vehiculo.");
        }
        return r;
    }
    
    @POST
    @Path("buscarporidconductor")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Vehiculo> buscarPorIdConductor(
    @FormParam ("idConductor") Integer idConductor) {
        return VehiculoDAO.findByIdConductor(idConductor);
    }
    
    @POST
    @Path("buscarporplaca")
    @Produces(MediaType.APPLICATION_JSON)
    public Vehiculo buscarPorPlaca (
    @FormParam ("placa") String placa) {
        return VehiculoDAO.findByPlaca(placa);
    }
    
    @POST
    @Path("modificar")
    @Produces(MediaType.APPLICATION_JSON)
    public Respuesta modificar(
    @FormParam ("idVehiculo") Integer idVehiculo,
            @FormParam ("marca") String marca,
            @FormParam ("modelo") String modelo,
            @FormParam ("anio") String anio,
            @FormParam ("color") String color,
            @FormParam ("nombreAseguradora") String nombreAseguradora,
            @FormParam ("numPoliza") String numPoliza,
            @FormParam ("placa") String placa) {
        Respuesta r = new Respuesta();
        Vehiculo ve =  new Vehiculo();
        ve.setAnio(anio);
        ve.setColor(color);
        ve.setIdVehiculo(idVehiculo);
        ve.setModelo(modelo);
        ve.setNombreAseguradora(nombreAseguradora);
        ve.setPlaca(placa);
        ve.setMarca(marca);
        ve.setNumPoliza(numPoliza);
        if (!Objects.equals(numPoliza, null)) {
            if (!VehiculoDAO.findByPolizaId(ve)) {
                r.setError(true);
                r.setErrorcode(1);
                r.setMensaje("El numero de poliza ya se encuentra registrado.");
                return r;
            }
        }
        if (!VehiculoDAO.findByPlacaId(ve)) {
            r.setError(true);
            r.setErrorcode(2);
            r.setMensaje("La placa ya se encuentra registrada.");
            return r;
        }
        int fa = 0;
        fa = VehiculoDAO.modificar(ve);
        if (fa > 0) {
            r.setError(false);
            r.setErrorcode(0);
            r.setMensaje("Datos actualizados exitosamente.");
        } else {
            r.setError(true);
            r.setErrorcode(3);
            r.setMensaje("No se pudo actualizar.");
        }
        return r;
    }
    /**
     * Retrieves representation of an instance of ws.WSVehiculo
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public String getXml() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

    /**
     * PUT method for updating or creating an instance of WSVehiculo
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_XML)
    public void putXml(String content) {
    }
}
