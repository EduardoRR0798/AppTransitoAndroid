package mx.uv.apptransito;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import mx.uv.apptransito.beans.Dictamen;
import mx.uv.apptransito.beans.Reporte;
import mx.uv.apptransito.beans.RespuestaWS;
import mx.uv.apptransito.tasks.ProgressDialogTask;
import mx.uv.apptransito.tasks.RespuestaHandler;
import mx.uv.apptransito.ws.HttpClient;
import mx.uv.apptransito.ws.Respuesta;

public class VerDictamenActivity extends AppCompatActivity {

    private static final String HOST = "http://10.0.2.2:8084";

    private Integer idConductoru;

    private ListView lst_reportes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_dictamen);

        Intent intent = getIntent();
        idConductoru = intent.getIntExtra("idConductor", 18);

        lst_reportes = findViewById(R.id.lst_reportes);

        new CargarReportesTask(this).execute();

    }

    public void actualizar(View v) {
        new CargarReportesTask(this).execute();
    }

    private RespuestaHandler cargarReportesHandler = new RespuestaHandler() {
        @Override
        public void handle(RespuestaWS respuestaWS) {
            Reporte[] reportes = respuestaWS.getReportes().toArray(new Reporte[]{});

            ArrayAdapter<Reporte> adapter = new ArrayAdapter<>(
                    VerDictamenActivity.this,
                    android.R.layout.simple_list_item_1,
                    reportes
            );

            lst_reportes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Reporte reporte = (Reporte) lst_reportes.getItemAtPosition(position);

                    Dictamen dictamen = reporte.getDictamen();
                    new AlertDialog.Builder(VerDictamenActivity.this)
                            .setTitle("Dictamen")
                            .setMessage((dictamen == null)? "No disponible":
                                    "Folio: " + dictamen.getFolio() + "\nDescripcion: " + dictamen.getDescripcion())

                            // A null listener allows the button to dismiss the dialog and take no further action.
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            });

            lst_reportes.setAdapter(adapter);
        }
    };

    private class CargarReportesTask extends ProgressDialogTask {

        public CargarReportesTask(VerDictamenActivity context) {
            super(context, context.cargarReportesHandler);
        }

        @Override
        protected Respuesta doInBackground(String... strings) {
            return HttpClient.getRequest(HOST + "/WSTransito/ws/siniestro/historial/" + idConductoru);
        }
    }
}