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
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mx.uv.apptransito.beans.Conductor;
import mx.uv.apptransito.beans.Vehiculo;
import mx.uv.apptransito.ws.HttpUtils;

public class VehiculoActivity extends AppCompatActivity {
    private String conductor;
    private Conductor c;
    private ListView lstVehiculos;
    private ProgressDialog pd_espera;
    private String json;
    private List<Vehiculo> vehiculos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehiculo);
        lstVehiculos = (ListView) findViewById(R.id.lstVehiculos);
        leerParametros();
    }

    public void registrarNuevo(View v) {
        Intent intent = new Intent(this, RegistrovehiculoActivity.class);
        intent.putExtra("conductor", conductor);
        startActivity(intent);
    }

    private void leerParametros() {
        Intent intent = getIntent();
        conductor = intent.getStringExtra("conductor");
        Gson gson = new Gson();
        c = gson.fromJson(conductor, Conductor.class);
    }

    @Override
    protected void onStart() {
        super.onStart();
        cargarVehiculos();
    }

    private void cargarVehiculos() {
        WSPOSTGetNotasTask task = new WSPOSTGetNotasTask();
        task.execute(c.getIdConductor().toString());
    }

    private void mostrarVehiculos() {
        ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, vehiculos);
        final ListView lstVehiculo = (ListView) findViewById(R.id.lstVehiculos);
        lstVehiculo.setAdapter(adapter);

        lstVehiculo.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                        String placa = lstVehiculo.getItemAtPosition(position).toString();
                        editarVehiculo(placa);
                    }
                }
        );

        lstVehiculo.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String nombrenota = lstVehiculo.getItemAtPosition(position).toString();
                preguntaEliminar(nombrenota);
                return true;
            }
        });
    }

    private void preguntaEliminar(final String placa) {
        AlertDialog dialogo = new AlertDialog.Builder(VehiculoActivity.this).create();
        dialogo.setTitle("Eliminar Nota");
        dialogo.setMessage(String.format("¿Desea eliminar el vehiculo: %s?", placa));
        dialogo.setButton(AlertDialog.BUTTON_POSITIVE, "SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                eliminarNota(placa);
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

    private void eliminarNota(String nombrenota) {
        SharedPreferences sp = getSharedPreferences("NOTAS", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(nombrenota);
        editor.commit();

        cargarVehiculos();
        mostrarVehiculos();
    }

    class WSPOSTGetNotasTask extends AsyncTask<String, String, String> {
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

    public void editarVehiculo(String nombrenota) {
        Intent intent = new Intent (this, RegistrovehiculoActivity.class);
        intent.putExtra("val_modo", "E");
        //N = Nueva nota | E = Editar nota
        intent.putExtra("val_nombrenota", nombrenota);
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
            Toast.makeText(this, json, Toast.LENGTH_SHORT).show();
            try {
                Vehiculo[] vsa = gson.fromJson(json, Vehiculo[].class);
                List<Vehiculo> ms = new ArrayList<>(Arrays.asList(vsa));
                //Type listType = new TypeToken<ArrayList<Vehiculo>>(){}.getType();
                //List<Vehiculo> ms = new Gson().fromJson(json, listType);
                //mostrarVehiculos();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "No se pueden recuperar...", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this, "Error al conectar...", Toast.LENGTH_SHORT).show();
        }
    }
}
