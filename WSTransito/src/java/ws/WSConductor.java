package ws;

import gateway.sms.JaxSms;
import java.util.Objects;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;
import modelo.dao.ConductorDAO;
import modelo.pojos.Conductor;
import modelo.pojos.Respuesta;
import modelo.pojos.RespuestaEmailSMS;

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
        if (!ConductorDAO.buscarPorTelefono(telefono)) {
            r.setError(true);
            r.setErrorcode(1);
            r.setMensaje("Ya existe un usuario con este telefono.");
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
        String token = generarNumeros();
        c.setToken(token);
        int fa = ConductorDAO.registrarConductor(c);
        if (fa != 1) {
            r.setError(true);
            r.setErrorcode(fa);
            r.setMensaje("No se pudo registrar al conductor.");
        } else {
            RespuestaEmailSMS sms = new RespuestaEmailSMS();
            JaxSms jax = new JaxSms();
            String respuesta = jax.enviar(telefono, "Codigo: " + token);
            r.setError(false);
            r.setErrorcode(0);
            r.setMensaje("Usuario registrado exitosamente.");
        }
        
        return r;
    }
    
    public String generarNumeros() {
        Integer numero = (int) (Math.random() * 9000 + 1000);
        return numero.toString();
    }
    
    @POST
    @Path("validarsms")
    @Produces(MediaType.APPLICATION_JSON)
    public Respuesta validarToken(
    @FormParam ("telefono") String telefono,
            @FormParam ("token") String token) {
        Respuesta r = new Respuesta();
        Conductor c = new Conductor();
        c.setToken(token);
        c.setTelefono(telefono);
        if (token.length() < 4 && token.length() > 4) {
            r.setError(true);
            r.setErrorcode(7);
            r.setMensaje("El token debe contener solo 4 caracteres.");
            return r;
        }
        if (!ConductorDAO.verificarToken(c)) {
            r.setError(true);
            r.setErrorcode(7);
            r.setMensaje("Token incorrecto.");
            return r;
        }
        int fa = ConductorDAO.validarConductor(c);
        if (fa != 1) {
            r.setError(true);
            r.setErrorcode(1);
            r.setMensaje("No se pudo validar al conductor");
        } else {
            r.setError(false);
            r.setErrorcode(0);
            r.setMensaje("Conductor validado.");
        }
        return r;
    }
    
    @POST
    @Path("login")
    @Produces(MediaType.APPLICATION_JSON)
    public Conductor login(
    @FormParam ("telefono") String telefono,
            @FormParam ("contrasenia") String contrasenia) {
        Conductor aux = new Conductor();
        aux.setTelefono(telefono);
        aux.setContrasenia(contrasenia);
        Conductor c = ConductorDAO.login(aux);
        return c;
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
