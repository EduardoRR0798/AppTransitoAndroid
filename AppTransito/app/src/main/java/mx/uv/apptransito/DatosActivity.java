package mx.uv.apptransito;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.Objects;

import mx.uv.apptransito.beans.Conductor;
import mx.uv.apptransito.ws.HttpUtils;
import mx.uv.apptransito.ws.Respuesta;

public class DatosActivity extends AppCompatActivity {
    private String conductor;
    private Conductor c;
    private EditText edtNumLicencia;
    private EditText edtContrasenia;
    private EditText edtNombre;
    private EditText edtFecha;
    private EditText edtTelefono;
    private Switch stchActualizar;
    private Button btnActualizar;
    private ProgressDialog pd_espera;
    private String json;
    private String contrasenia;
    private String numLicencia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos);
        edtContrasenia = (EditText) findViewById(R.id.edtContrasenia);
        edtNumLicencia = (EditText) findViewById(R.id.edtNumLicencia);
        edtTelefono = (EditText) findViewById(R.id.edtTelefono);
        edtNombre = (EditText) findViewById(R.id.edtNombre);
        edtFecha = (EditText) findViewById(R.id.edtFecha);
        stchActualizar = (Switch) findViewById(R.id.stchActualizar);
        btnActualizar = (Button) findViewById(R.id.btnActualizar);
        edtContrasenia.setEnabled(false);
        edtNumLicencia.setEnabled(false);
        btnActualizar.setEnabled(false);
        leerPreferencias();
        cargarDatos();
    }

    private void leerPreferencias() {
        SharedPreferences sp = getSharedPreferences("TRANSITO", Context.MODE_PRIVATE);
        conductor = sp.getString("conductor", "");
        Gson gson = new Gson();
        c = gson.fromJson(conductor, Conductor.class);
    }

    private void cargarDatos() {
        edtTelefono.setText(c.getTelefono());
        edtNumLicencia.setText(c.getNumLicencia());
        edtContrasenia.setText(c.getContrasenia());
        edtFecha.setText(c.getFechaNacimiento());
        edtNombre.setText(c.getNombre());
    }

    public void activar(View v) {
        if (stchActualizar.isChecked()) {
            edtContrasenia.setEnabled(true);
            edtNumLicencia.setEnabled(true);
            btnActualizar.setEnabled(true);
        } else {
            edtContrasenia.setEnabled(false);
            edtNumLicencia.setEnabled(false);
            btnActualizar.setEnabled(false);
            edtNumLicencia.setText(c.getNumLicencia());
            edtContrasenia.setText(c.getContrasenia());
        }
    }

    public void actualizar(View v) {
        if (validar()) {
            contrasenia = edtContrasenia.getText().toString().trim();
            numLicencia = edtNumLicencia.getText().toString().trim();
            WSPOSTModificarTask task = new WSPOSTModificarTask();
            task.execute(c.getIdConductor().toString(), contrasenia, numLicencia);
        }
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

    private boolean validar() {
        if (Objects.equals(edtContrasenia.getText().toString().trim(), "")) {
            edtContrasenia.setError("Este campo no puede estar vacio.");
            return false;
        }
        if (Objects.equals(edtNumLicencia.getText().toString().trim(), "")) {
            edtNumLicencia.setError("Este campo no puede estar vacio.");
            return false;
        }
        return true;
    }

    class WSPOSTModificarTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... strings) {
            return HttpUtils.modificarConductor(c.getIdConductor(), contrasenia, numLicencia);
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
            Respuesta r = gson.fromJson(json, Respuesta.class);
            try {
                SharedPreferences sp = getSharedPreferences("TRANSITO", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                c.setContrasenia(contrasenia);
                c.setNumLicencia(numLicencia);
                String con = gson.toJson(c);
                if (sp != null) {
                    editor.remove("conductor");
                    editor.putString("conductor", con);
                }
                editor.commit();
                finish();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Usuario o contraseña incorrectos.", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this, "Error al conectar...", Toast.LENGTH_SHORT).show();
        }
    }
}
