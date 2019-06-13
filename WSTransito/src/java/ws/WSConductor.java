package ws;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;
import modelo.dao.ConductorDAO;
import modelo.pojos.Conductor;
import modelo.pojos.Respuesta;

/**
 * REST Web Service
 * 
 * @author Eduardo Rosas Rivera.
 */
@Path("conductor")
public class WSConductor {
    byte[] archivo; 

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of Conductor
     */
    public WSConductor() {
    }

    @PUT
    @Path("registrar")
    @Produces(MediaType.APPLICATION_JSON)
    public Respuesta registrar(
    @FormParam ("nombre") String nombre,
    @FormParam ("fechaNacimiento") String fechaNacimiento,
    @FormParam ("numLicencia") String numLicencia,
    @FormParam ("telefono") String telefono,
    @FormParam ("contrasenia") String contrasenia) {
        Respuesta r = new Respuesta();
        Conductor c = new Conductor();
        if (Objects.equals(nombre, null)) {
            r.setError(true);
            r.setErrorcode(1);
            r.setMensaje("El nombre no puede ser nulo.");
            return r;
        }
        c.setNombre(nombre);
        if (Objects.equals(fechaNacimiento, null)) {
            r.setError(true);
            r.setErrorcode(1);
            r.setMensaje("La fecha de nacimiento no puede estar vacia.");
            return r;
        }
        c.setFechaNacimiento(fechaNacimiento);
        if (Objects.equals(telefono, null)) {
            r.setError(true);
            r.setErrorcode(1);
            r.setMensaje("El telefono no puede ser nulo.");
            return r;
        }
        if (!telefono.matches("^\\D?(\\d{3})\\D?\\D?(\\d{3})\\D?(\\d{4})$")) {
            r.setError(true);
            r.setErrorcode(1);
            r.setMensaje("Ingrese un telefono valido.");
            return r;
        }
        c.setTelefono(telefono);
        if (Objects.equals(numLicencia, null)) {
            r.setError(true);
            r.setErrorcode(1);
            r.setMensaje("El numero de licencia no puede ser nulo.");
            return r;
        }
        c.setNumLicencia(numLicencia);
        if (Objects.equals(contrasenia, null)) {
            r.setError(true);
            r.setErrorcode(1);
            r.setMensaje("La contraseña no puede estar vacia.");
            return r;
        }
        if (contrasenia.length() < 6) {
            r.setError(true);
            r.setErrorcode(1);
            r.setMensaje("La contraseña debe contener minimo 6 caraceres.");
            return r;
        }
        c.setContrasenia(contrasenia);
        int fa = ConductorDAO.registrarConductor(c);
        if (fa != 0) {
            r.setError(true);
            r.setErrorcode(fa);
            r.setMensaje("No se pudo registrar al conductor.");
        } else {
            r.setError(false);
            r.setErrorcode(0);
            r.setMensaje("Usuario registrado exitosamente.");
        }
        
        return r;
    }
    
    /**
     * Retrieves representation of an instance of ws.WSConductor
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(javax.ws.rs.core.MediaType.APPLICATION_XML)
    public String getXml() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

    /**
     * PUT method for updating or creating an instance of WSConductor
     * @param content representation for the resource
     */
    @PUT
    @Consumes(javax.ws.rs.core.MediaType.APPLICATION_XML)
    public void putXml(String content) {
    }
}
