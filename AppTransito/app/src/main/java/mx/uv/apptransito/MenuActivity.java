package mx.uv.apptransito;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;

import mx.uv.apptransito.beans.Conductor;

public class MenuActivity extends AppCompatActivity {
    private String conductor;
    private Conductor c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        leerPreferencias();
    }

    private void leerPreferencias() {
        SharedPreferences sp = getSharedPreferences("TRANSITO", Context.MODE_PRIVATE);
        conductor = sp.getString("conductor", "");
        Gson gson = new Gson();
        c = gson.fromJson(conductor, Conductor.class);
    }

    public void abrirMisVehiculos(View v) {
        Intent i = new Intent(this, VehiculoActivity.class);
        startActivity(i);
    }

    public void abrirMisDatos(View v) {
        Intent i = new Intent(this, DatosActivity.class);
        startActivity(i);
    }

    public void abrirSiniestros() {

    }
}
