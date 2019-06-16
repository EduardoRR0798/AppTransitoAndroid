package mx.uv.apptransito;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.NameList;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import mx.uv.apptransito.beans.Conductor;
import mx.uv.apptransito.beans.Vehiculo;
import mx.uv.apptransito.ws.HttpUtils;
import mx.uv.apptransito.ws.Respuesta;

public class VehiculoActivity extends AppCompatActivity {
    private String conductor;
    private Conductor c;
    private ListView lstVehiculos;
    private ProgressDialog pd_espera;
    private String json;
    private String json1;
    private List<Vehiculo> vehiculos;
    private Integer idVehiculo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehiculo);
        lstVehiculos = (ListView) findViewById(R.id.lstVehiculos);
        leerPreferencias();
    }

    public void registrarNuevo(View v) {
        Intent intent = new Intent(this, RegistrovehiculoActivity.class);
        intent.putExtra("conductor", conductor);
        startActivity(intent);
    }

    private void leerPreferencias() {
        SharedPreferences sp = getSharedPreferences("TRANSITO", Context.MODE_PRIVATE);
        conductor = sp.getString("conductor", "");
        Gson gson = new Gson();
        c = gson.fromJson(conductor, Conductor.class);
    }

    @Override
    protected void onStart() {
        super.onStart();
        cargarVehiculos();
    }

    private void cargarVehiculos() {
        WSPOSTGetVehiculosTask task = new WSPOSTGetVehiculosTask();
        task.execute(c.getIdConductor().toString());
    }

    private void mostrarVehiculos() {
        ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, vehiculos);
        final ListView lstVehiculos = (ListView) findViewById(R.id.lstVehiculos);
        lstVehiculos.setAdapter(adapter);

        lstVehiculos.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                        Vehiculo vehiculo = (Vehiculo) lstVehiculos.getItemAtPosition(position);
                        editarVehiculo(vehiculo);
                    }
                }
        );

        lstVehiculos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Vehiculo vehiculo = (Vehiculo) lstVehiculos.getItemAtPosition(position);
                preguntaEliminar(vehiculo);
                return true;
            }
        });
    }

    private void preguntaEliminar(final Vehiculo vehiculo) {
        AlertDialog dialogo = new AlertDialog.Builder(VehiculoActivity.this).create();
        dialogo.setTitle("Eliminar Vehiculo");
        dialogo.setMessage(String.format("¿Desea eliminar el vehiculo: %s?", vehiculo.getModelo()));
        dialogo.setButton(AlertDialog.BUTTON_POSITIVE, "SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                eliminarVehiculo(vehiculo);
            }
        });
        dialogo.setButton(AlertDialog.BUTTON_NEGATIVE, "NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialogo.show();
    }

    private void eliminarVehiculo(Vehiculo vehiculo) {
        WSPOSTEliminarVehiculoTask task = new WSPOSTEliminarVehiculoTask();
        idVehiculo = vehiculo.getIdVehiculo();
        task.execute(vehiculo.getIdVehiculo().toString(), c.getIdConductor().toString());
    }

    class WSPOSTEliminarVehiculoTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... strings) {
            return HttpUtils.eliminarVehiculo(idVehiculo, c.getIdConductor());
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            json1 = result;
            resultadoEntrarEliminar();
        }
    }

    class WSPOSTGetVehiculosTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... strings) {
            return HttpUtils.recuperarVehiculos(c.getIdConductor());
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            json = result;
            resultadoEntrar();
        }
    }

    public void editarVehiculo(Vehiculo vehiculo) {
        Intent intent = new Intent (this, ModificarVehiculoActivity.class);
        Gson gson = new Gson();
        String sv = gson.toJson(vehiculo);
        intent.putExtra("vehiculo", sv);
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

    private void resultadoEntrar() {
        hideProgressDialog();
        if (!json.equals("")) {
            Gson gson = new Gson();
            try {
                JSONArray jsonArray = new JSONArray(json);
                vehiculos = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Vehiculo v = new Vehiculo();
                    v.setAnio(jsonObject.getString("anio"));
                    v.setColor(jsonObject.getString("color"));
                    v.setMarca(jsonObject.getString("marca"));
                    v.setPlaca(jsonObject.getString("placa"));
                    v.setModelo(jsonObject.getString("modelo"));
                    v.setNombreAseguradora(jsonObject.getString("nombreAseguradora"));
                    v.setNumPoliza(jsonObject.getString("numPoliza"));
                    v.setIdVehiculo(jsonObject.getInt("idVehiculo"));
                    vehiculos.add(v);
                }
                mostrarVehiculos();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "No se pueden recuperar...", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this, "Error al conectar...", Toast.LENGTH_SHORT).show();
        }
    }

    private void resultadoEntrarEliminar() {
        hideProgressDialog();
        if (!json1.equals("")) {
            Gson gson = new Gson();
            Respuesta r = gson.fromJson(json1, Respuesta.class);
            if (r.isError()) {
                Toast.makeText(this, r.getResult(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, r.getResult(), Toast.LENGTH_SHORT).show();
                cargarVehiculos();
                mostrarVehiculos();
            }
        } else {
            Toast.makeText(this, "Error al conectar...", Toast.LENGTH_SHORT).show();
        }
    }
}
