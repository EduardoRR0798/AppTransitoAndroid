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

import mx.uv.apptransito.ws.HttpUtils;
import mx.uv.apptransito.ws.Respuesta;

public class RegistroActivity extends AppCompatActivity {
    private EditText edtNombre;
    private EditText edtTelefono;
    private EditText edtNumLicencia;
    private EditText edtFecha;
    private EditText edtContrasenia;
    private String[] params;
    private ProgressDialog pd_espera;
    private String json;
    private String telefono;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        edtNombre = (EditText) findViewById(R.id.edtNombre);
        edtTelefono = (EditText) findViewById(R.id.edtTelefono);
        edtContrasenia = (EditText) findViewById(R.id.edtContrasenia);
        edtNumLicencia = (EditText) findViewById(R.id.edtNumLicencia);
        edtFecha = (EditText) findViewById(R.id.edtFecha);
        params = new String[5];
    }

    private boolean validar() {
        if (Objects.equals(edtNombre.getText().toString().trim(), "")) {
            edtNombre.setError("Llene este campo");
            return false;
        }
        if (Objects.equals(edtTelefono.getText().toString().trim(), "")) {
            edtTelefono.setError("Llene este campo");
            return false;
        }
        if (Objects.equals(edtContrasenia.getText().toString().trim(), "")) {
            edtContrasenia.setError("Llene este campo");
            return false;
        }
        if (Objects.equals(edtNumLicencia.getText().toString().trim(), "")) {
            edtNumLicencia.setError("Llene este campo");
            return false;
        }
        if (Objects.equals(edtFecha.getText().toString().trim(), "")) {
            edtFecha.setError("Llene este campo");
            return false;
        }
        return true;
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

    public void registrar(View v) {
        if (validar()) {
            params[0] = edtNombre.getText().toString().trim();
            params[1] = edtTelefono.getText().toString().trim();
            telefono = params[1];
            params[2] = edtContrasenia.getText().toString().trim();
            params[3] = edtNumLicencia.getText().toString().trim();
            params[4] = edtFecha.getText().toString().trim();
            WSPOSTRegistrarTask task = new WSPOSTRegistrarTask();
            task.execute(params[0], params[2], params[1], params[3], params[4]);
        }
    }

    class WSPOSTRegistrarTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... strings) {
            return HttpUtils.registrar(params[0], params[2], params[1], params[3], params[4]);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            json = result;
            resultadoRegistrar();
        }
    }

    private void resultadoRegistrar() {
        hideProgressDialog();
        if (!json.equals("")) {
            Gson gson = new Gson();
            Respuesta r = gson.fromJson(json, Respuesta.class);
            if (r.isError()) {
                Toast.makeText(this, r.getResult(), Toast.LENGTH_SHORT).show();
            } else {
                Intent i = new Intent(this, ValidacionActivity.class);
                i.putExtra("telefono", telefono);
                startActivity(i);
            }
        } else {
            Toast.makeText(this, "No se puede registrar al usuario...", Toast.LENGTH_SHORT).show();
        }
    }
}
