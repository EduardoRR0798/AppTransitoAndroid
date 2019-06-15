package mx.uv.apptransito;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.Objects;

import mx.uv.apptransito.beans.Conductor;
import mx.uv.apptransito.ws.HttpUtils;
import mx.uv.apptransito.ws.Respuesta;

public class MainActivity extends AppCompatActivity {
    private ProgressDialog pd_espera;
    private EditText edtTelefono;
    private EditText edtContrasenia;
    private String json;
    private String params[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtTelefono = (EditText) findViewById(R.id.edtTelefono);
        edtContrasenia = (EditText) findViewById(R.id.edtContrasenia);
        params = new String[2];
    }

    public void abrirRegistrar(View v) {
        Intent intent = new Intent(this, RegistroActivity.class);
        startActivity(intent);
    }

    private void showProgressDialog() {
        pd_espera = new ProgressDialog(this);
        pd_espera.setMessage("Espera por favor...");
        pd_espera.setCancelable(false);
        pd_espera.show();
    }

    private void hideProgressDialog() {
        if (pd_espera.isShowing())
            pd_espera.hide();
    }

    public void entrar(View v) {
        if (validar()) {
            String telefono = edtTelefono.getText().toString();
            String password = edtContrasenia.getText().toString();
            params[0] = telefono;
            params[1] = password;
            WSPOSTLoginTask task = new WSPOSTLoginTask();
            task.execute(telefono, password);
        } else {
            Toast.makeText(this, "Llene todos los campos", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean validar() {
        if (Objects.equals(edtTelefono.getText().toString(), "")) {
            return false;
        }
        if (Objects.equals(edtContrasenia.getText().toString(), "")) {
            return false;
        }
        return true;
    }

    class WSPOSTLoginTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... strings) {
            return HttpUtils.login(params[0], params[1]);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            json = result;
            resultadoEntrar();
        }
    }

    private void resultadoEntrar() {
        hideProgressDialog();
        if (!json.equals("")) {
            Gson gson = new Gson();
            Conductor c = gson.fromJson(json, Conductor.class);
            try {
                Intent i = new Intent(this, VehiculoActivity.class);
                i.putExtra("conductor", json);
                startActivity(i);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Usuario o contraseña incorrectos.", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this, "Error al conectar...", Toast.LENGTH_SHORT).show();
        }
    }

}
