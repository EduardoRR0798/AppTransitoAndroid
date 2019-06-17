package mx.uv.apptransito;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mx.uv.apptransito.beans.RespuestaWS;
import mx.uv.apptransito.beans.Vehiculo;
import mx.uv.apptransito.tasks.ProgressDialogTask;
import mx.uv.apptransito.tasks.RespuestaHandler;
import mx.uv.apptransito.ws.HttpClient;
import mx.uv.apptransito.ws.Respuesta;

public class RegistrarSiniestroActivity extends AppCompatActivity {

    private static final String HOST = "http://10.0.2.2:8084";

    private static final int REQUEST_CODE_INCIDENTE = 1;
    private static final int REQUEST_CODE_VEHICULO = 2;

    private TextView lbl_incidente;
    private TextView lbl_vehiculo;
    private Button btn_reporte;

    private Integer idConductor;
    private String nombreConductor;
    private List<Vehiculo> vehiculos;
    private Integer idIncidente;
    private double latitud;
    private double longitud;
    private Vehiculo vehiculo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_siniestro);

        lbl_incidente = findViewById(R.id.lbl_incidente);
        lbl_vehiculo = findViewById(R.id.lbl_vehiculo);
        btn_reporte = findViewById(R.id.btn_reporte);

        Intent intent = getIntent();
        idConductor = intent.getIntExtra("idConductor", 18);
        nombreConductor = intent.getStringExtra("nombreConductor");
        if (nombreConductor == null) {
            nombreConductor = "NO_NAME";
        }

        new CargarVehiculosTask(this).execute();

        actualizarUI();
    }

    public void seleccionarIncidente(View v) {
        startActivityForResult(new Intent(this, SeleccionarIncidenteActivity.class), REQUEST_CODE_INCIDENTE);
    }

    public void seleccionarVehiculo(View v) {

        List<String> nombresVehiculos = new ArrayList<>();
        for (Vehiculo vehiculo : vehiculos) {
            nombresVehiculos.add(vehiculo.getMarca() + "  " + vehiculo.getModelo() + "  (" + vehiculo.getPlaca() + ")");
        }
        String[] vehiculosArr = nombresVehiculos.toArray(new String[]{});

        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Seleccione un vehiculo");

        // add a list
        builder.setItems(vehiculosArr, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                vehiculo = vehiculos.get(which);
                actualizarUI();
            }
        });

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void levantarReporte(View v) {
        new LevantarReporteTask(this).execute();
    }

    private RespuestaHandler levantarReporteHandler = new RespuestaHandler() {
        @Override
        public void handle(RespuestaWS respuestaWS) {
            Intent intent = new Intent(RegistrarSiniestroActivity.this, FotosActivity.class);
            intent.putExtra("idReporte", respuestaWS.getIdGenerado());
            startActivity(intent);
            finish();
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_INCIDENTE) {
            if (resultCode == RESULT_OK) {
                idIncidente = data.getIntExtra("idIncidente", 0);
                if (idIncidente.equals(0)) {
                    idIncidente = null;
                }
                latitud = data.getDoubleExtra("latitud",0);
                longitud = data.getDoubleExtra("longitud", 0);
            }
        } else if (requestCode == REQUEST_CODE_VEHICULO) {
            if (resultCode == RESULT_OK) {
                vehiculo = (Vehiculo) data.getExtras().getSerializable("vehiculo");
            }
        }

        actualizarUI();
    }

    private void actualizarUI() {
        if (idIncidente == null) {
            lbl_incidente.setText("Incidente no seleccionado");
        } else {
            lbl_incidente.setText("Incidente seleccionado (ID " + idIncidente + ")");
        }

        if (vehiculo == null) {
            lbl_vehiculo.setText("Vehículo no seleccionado");
        } else {
            lbl_vehiculo.setText("Vehículo seleccionado (ID " + vehiculo.getIdVehiculo() + ")");
        }

        btn_reporte.setEnabled(idIncidente != null && vehiculo != null);
    }

    private RespuestaHandler cargarVehiculosHandler = new RespuestaHandler() {
        @Override
        public void handle(RespuestaWS respuestaWS) {
            vehiculos = respuestaWS.getVehiculos();
        }
    };

    private class CargarVehiculosTask extends ProgressDialogTask {

        private CargarVehiculosTask(RegistrarSiniestroActivity context) {
            super(context, context.cargarVehiculosHandler);
        }

        @Override
        protected Respuesta doInBackground(String... strings) {
            Map<String, Object> postData = new HashMap<>();
            postData.put("idConductor", idConductor);

            return HttpClient.postRequest(HOST + "/WSTransito/ws/vehiculos/deconductor", postData);
        }
    }

    private class LevantarReporteTask extends ProgressDialogTask {
        public LevantarReporteTask(RegistrarSiniestroActivity context) {
            super(context, context.levantarReporteHandler);
        }

        @Override
        protected Respuesta doInBackground(String... strings) {
            Map<String, Object> postData = new HashMap<>();
            postData.put("latitud", latitud);
            postData.put("longitud", longitud);
            postData.put("nombreConductor", nombreConductor);
            postData.put("nombreAseguradora", vehiculo.getNombreAseguradora());
            postData.put("numPoliza", vehiculo.getNumPoliza());
            postData.put("marca", vehiculo.getMarca());
            postData.put("modelo", vehiculo.getModelo());
            postData.put("color", vehiculo.getColor());
            postData.put("placa", vehiculo.getPlaca());
            postData.put("idConductor", idConductor);
            postData.put("idVehiculo", vehiculo.getIdVehiculo());
            postData.put("idIncidente", idIncidente);

            return HttpClient.postRequest(HOST + "/WSTransito/ws/siniestro/levantarReporte", postData);
        }
    }
}
