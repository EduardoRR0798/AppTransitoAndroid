package mx.uv.apptransito;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import im.delight.android.location.SimpleLocation;
import mx.uv.apptransito.beans.Incidente;
import mx.uv.apptransito.beans.RespuestaWS;
import mx.uv.apptransito.tasks.ProgressDialogTask;
import mx.uv.apptransito.tasks.RespuestaHandler;
import mx.uv.apptransito.ws.HttpClient;
import mx.uv.apptransito.ws.Respuesta;

public class SeleccionarIncidenteActivity extends AppCompatActivity {

    private static final String HOST = "http://40.74.248.169:8080";

    private static SimpleLocation simpleLocation;
    private ListView lst_incidentes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccionar_incidente);

        lst_incidentes = findViewById(R.id.lst_incidentes);

        simpleLocation = new SimpleLocation(this);
        // if we can't access the location yet
        if (!simpleLocation.hasLocationEnabled()) {
            // ask the user to enable location access
            SimpleLocation.openSettings(this);
        }
        simpleLocation.beginUpdates();

        new CargarIncidentesTask(this).execute();
    }

    public void registrarIncidente(View view) {
        new CrearIncidenteTask(this).execute();
    }

    private void verFotos(final Incidente incidente) {

        Dialog builder = new Dialog(this);
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                //nothing;
            }
        });

        final ImageView imageView = new ImageView(this);

        if (incidente.getFotos().isEmpty()) {
            Toast.makeText(this,"Este incidente no tiene fotos", Toast.LENGTH_LONG).show();
        } else {

            setImage(imageView, incidente.getFotos().get(0).getFotoBytes());

            imageView.setOnClickListener(new View.OnClickListener() {

                int index = 0;

                @Override
                public void onClick(View view) {
                    if (++index == incidente.getFotos().size()) {
                        index = 0;
                    }

                    setImage(imageView, incidente.getFotos().get(index).getFotoBytes());
                }
            });
        }
        builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        builder.show();

    }

    private void setImage(ImageView imageView, String bytes) {
        byte[] foto = Base64.decode(bytes, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(foto,0, foto.length);
        Bitmap bitmapScaled = resize(bitmap, 1000, 1000);
        imageView.setImageBitmap(bitmapScaled);
    }

    private Bitmap resize(Bitmap image, int maxWidth, int maxHeight) {
        if (maxHeight > 0 && maxWidth > 0) {
            int width = image.getWidth();
            int height = image.getHeight();
            float ratioBitmap = (float) width / (float) height;
            float ratioMax = (float) maxWidth / (float) maxHeight;

            int finalWidth = maxWidth;
            int finalHeight = maxHeight;
            if (ratioMax > ratioBitmap) {
                finalWidth = (int) ((float)maxHeight * ratioBitmap);
            } else {
                finalHeight = (int) ((float)maxWidth / ratioBitmap);
            }
            image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
            return image;
        } else {
            return image;
        }
    }

    private RespuestaHandler cargarIncidentesHandler = new RespuestaHandler() {
        @Override
        public void handle(RespuestaWS respuestaWS) {
            Incidente[] incidentes = respuestaWS.getIncidentes().toArray(new Incidente[]{});

            ArrayAdapter<Incidente> adapter = new ArrayAdapter<>(
                    SeleccionarIncidenteActivity.this,
                    android.R.layout.simple_list_item_1,
                    incidentes
            );

            lst_incidentes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    verFotos((Incidente) lst_incidentes.getItemAtPosition(position));
                }
            });

            lst_incidentes.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent();
                    intent.putExtra("idIncidente", ((Incidente) lst_incidentes.getItemAtPosition(position)).getIdIncidente());
                    intent.putExtra("latitud", simpleLocation.getLatitude());
                    intent.putExtra("longitud", simpleLocation.getLongitude());
                    setResult(RESULT_OK, intent);
                    finish();
                    return true;
                }
            });

            lst_incidentes.setAdapter(adapter);
        }
    };

    private RespuestaHandler crearIncidenteHandler = new RespuestaHandler() {
        @Override
        public void handle(RespuestaWS respuestaWS) {
            Intent intent = new Intent();
            intent.putExtra("idIncidente", respuestaWS.getIdGenerado());
            intent.putExtra("latitud", simpleLocation.getLatitude());
            intent.putExtra("longitud", simpleLocation.getLongitude());
            setResult(RESULT_OK, intent);
            finish();
        }
    };


    private static class CargarIncidentesTask extends ProgressDialogTask {

        private CargarIncidentesTask(SeleccionarIncidenteActivity context) {
            super(context, context.cargarIncidentesHandler);
        }

        @Override
        protected Respuesta doInBackground(String... strings) {

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            simpleLocation.endUpdates();

            double latitud = simpleLocation.getLatitude();
            double longitud = simpleLocation.getLongitude();

            Log.v("lat", String.valueOf(latitud));
            Log.v("long", String.valueOf(longitud));

            return HttpClient.getRequest(
                    HOST + "/WSTransito/ws/siniestro/incidentesCenrca/" + latitud + "/" + longitud
            );
        }
    }

    private static class CrearIncidenteTask extends ProgressDialogTask {

        public CrearIncidenteTask(SeleccionarIncidenteActivity context) {
            super(context, context.crearIncidenteHandler);
        }

        @Override
        protected Respuesta doInBackground(String... strings) {

            Map<String,Object> postData = new HashMap<>();
            postData.put("latitud", simpleLocation.getLatitude());
            postData.put("longitud", simpleLocation.getLongitude());

            return HttpClient.postRequest(HOST + "/WSTransito/ws/siniestro/registrarIncidente", postData);
        }
    }
}
