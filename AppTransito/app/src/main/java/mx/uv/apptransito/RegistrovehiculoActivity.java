package mx.uv.apptransito;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.Objects;

import mx.uv.apptransito.beans.Conductor;
import mx.uv.apptransito.beans.Vehiculo;
import mx.uv.apptransito.ws.HttpUtils;
import mx.uv.apptransito.ws.Respuesta;

public class RegistrovehiculoActivity extends AppCompatActivity {
    private EditText edtMarca;
    private EditText edtModelo;
    private EditText edtColor;
    private EditText edtAseguradora;
    private EditText edtPlaca;
    private EditText edtAnio;
    private EditText edtPoliza;
    private Conductor c;
    private String json;
    private String conductor;
    private ProgressDialog pd_espera;
    private Vehiculo ve;
    private RadioButton rbDuenio;
    private String[] params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrovehiculo);
        edtMarca = (EditText) findViewById(R.id.edtMarca);
        edtModelo = (EditText) findViewById(R.id.edtModelo);
        edtAnio = (EditText) findViewById(R.id.edtAnio);
        edtAseguradora = (EditText) findViewById(R.id.edtAseguradora);
        edtPlaca = (EditText) findViewById(R.id.edtPlaca);
        edtPoliza = (EditText) findViewById(R.id.edtPoliza);
        edtColor = (EditText) findViewById(R.id.edtColor);
        rbDuenio = (RadioButton) findViewById(R.id.rbDuenio);
        params = new String[8];
        leerPreferencias();
    }

    private void leerPreferencias() {
        SharedPreferences sp = getSharedPreferences("TRANSITO", Context.MODE_PRIVATE);
        conductor = sp.getString("conductor", "");
        Gson gson = new Gson();
        c = gson.fromJson(conductor, Conductor.class);
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
        if (Objects.equals(edtMarca.getText().toString().trim(), "")) {
            edtMarca.setError("Llene este campo");
            return false;
        }
        if (Objects.equals(edtModelo.getText().toString().trim(), "")) {
            edtModelo.setError("Llene este campo");
            return false;
        }
        if (Objects.equals(edtAnio.getText().toString().trim(), "")) {
            edtAnio.setError("Llene este campo");
            return false;
        }
        if (Objects.equals(edtColor.getText().toString().trim(), "")) {
            edtColor.setError("Llene este campo");
            return false;
        }
        if (Objects.equals(edtPlaca.getText().toString().trim(), "")) {
            edtPlaca.setError("Llene este campo");
            return false;
        }
        return true;
    }

    public void registrarNuevo(View v) {
        if (validar()) {
            params[0] = edtMarca.getText().toString().trim();
            String marca = params[0];
            params[1] = edtModelo.getText().toString().trim();
            String modelo =params[1];
            params[2] = edtAnio.getText().toString().trim();
            String anio = params[2];
            params[3] = edtAseguradora.getText().toString().trim();
            String aseguradora = params[3];
            params[4] = edtPoliza.getText().toString().trim();
            String poliza = params[4];
            params[5] = edtColor.getText().toString().trim();
            String color = params[5];
            params[6] = edtPlaca.getText().toString().trim();
            String placa = params[6];
            if (rbDuenio.isChecked()) {
                params[7] = "S";
            } else {
                params[7] = "N";
            }
            String propietario = params[7];
            WSPOSTRegistrarTask task = new WSPOSTRegistrarTask();
            task.execute(marca, modelo, anio, color, aseguradora, poliza, placa, propietario, c.getIdConductor().toString());
        }
    }

    class WSPOSTRegistrarTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... strings) {
            return HttpUtils.registrarVehiculo(params[0], params[1], params[2], params[5], params[3], params[4], params[6], params[7], c.getIdConductor());
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
                Toast.makeText(this, "Vehiculo registrado exitosamente", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            Toast.makeText(this, "No se pudo registrar el vehiculo...", Toast.LENGTH_SHORT).show();
        }
    }
}
