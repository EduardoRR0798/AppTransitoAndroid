package mx.uv.apptransito.ws;

public class Respuesta {

    private boolean error;
    private int errorcode;
    private String mensaje;

    public Respuesta(){

    }

    public int getStatus() {
        return errorcode;
    }

    public void setStatus(int status) {
        this.errorcode = status;
    }

    public String getResult() {
        return mensaje;
    }

    public void setResult(String result) {
        this.mensaje = result;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }
}
