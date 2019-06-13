package ws;

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
import org.apache.ibatis.annotations.Delete;

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
        if (Objects.equals(marca, null)) {
            r.setError(true);
            r.setErrorcode(1);
            r.setMensaje("La marca no puede estar vacia.");
        }
        v.setMarca(marca);
        if (Objects.equals(modelo, null)) {
            r.setError(true);
            r.setErrorcode(1);
            r.setMensaje("El modelo no puede estar vacio.");
        }
        v.setModelo(modelo);
        if (Objects.equals(anio, null)) {
            r.setError(true);
            r.setErrorcode(1);
            r.setMensaje("El año no puede estar vacio.");
        }
        v.setAnio(anio);
        if (Objects.equals(color, null)) {
            r.setError(true);
            r.setErrorcode(1);
            r.setMensaje("El color no puede estar vacio.");
        }
        v.setColor(color);
        if (Objects.equals(placa, null)) {
            r.setError(true);
            r.setErrorcode(1);
            r.setMensaje("La placa no puede estar vacia.");
        }
        v.setPlaca(placa);
        v.setNombreAseguradora(nombreAseguradora);
        v.setNumPoliza(numPoliza);
        if (Objects.equals(idConductor, null)) {
            r.setError(true);
            r.setErrorcode(1);
            r.setMensaje("El id del conductor no puede estar vacio.");
        }
        v.setIdConductor(idConductor);
        v.setPropietario(propietario);
        int fa = VehiculoDAO.registrar(v);
        if (fa != 0) {
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
    @FormParam ("idVehiculoConductor") Integer idVehiculoConductor) {
        Respuesta r = new Respuesta();
        int fa = 0;
        fa = VehiculoDAO.eliminarRelacion(idVehiculoConductor);
        if (fa != 0) {
            r.setError(true);
            r.setErrorcode(fa);
            r.setMensaje("El vehiculo se ha eliminado de la lista.");
        } else {
            r.setError(false);
            r.setErrorcode(0);
            r.setMensaje("No se pudo eliminar.");
        }
        return r;
    }
    
    @POST
    @Path("agregar")
    @Produces(MediaType.APPLICATION_JSON)
    public Respuesta agregarVehiculo(
    @FormParam ("idConductor") Integer idConductor,
    @FormParam ("idVehiculo") Integer idVehiculo,
    @FormParam ("propietario") String propirtario) {
        Respuesta r = new Respuesta();
        
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
