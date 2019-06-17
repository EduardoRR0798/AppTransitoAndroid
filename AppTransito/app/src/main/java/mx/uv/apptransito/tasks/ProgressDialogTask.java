package mx.uv.apptransito.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.lang.ref.WeakReference;

import mx.uv.apptransito.beans.RespuestaWS;
import mx.uv.apptransito.ws.Respuesta;

public abstract class ProgressDialogTask extends AsyncTask<String, String, Respuesta> {

    private ProgressDialog progressDialog;
    private WeakReference<Context> context;
    private RespuestaHandler respuestaHandler;

    public ProgressDialogTask(Context context, RespuestaHandler respuestaHandler) {
        this.context = new WeakReference<Context>(context);
        this.respuestaHandler = respuestaHandler;
    }

    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog(context.get());
        progressDialog.setMessage("Espera por favor...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected void onPostExecute(Respuesta respuesta) {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

        Log.v("response", respuesta.getResult());

        try {
            RespuestaWS respuestaWS = new Gson().fromJson(respuesta.getResult(), RespuestaWS.class);

            Toast.makeText(
                    context.get(),
                    respuestaWS.getMensaje(),
                    Toast.LENGTH_LONG
            ).show();

            if (!respuestaWS.getError()) {
                respuestaHandler.handle(respuestaWS);
            }
        } catch (JsonSyntaxException e) {
            Toast.makeText(
                    context.get(),
                    "No se puede conectar",
                    Toast.LENGTH_LONG
            ).show();
        }

    }
}
