package mx.uv.apptransito;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import mx.uv.apptransito.beans.RespuestaWS;
import mx.uv.apptransito.tasks.ProgressDialogTask;
import mx.uv.apptransito.tasks.RespuestaHandler;
import mx.uv.apptransito.ws.HttpClient;
import mx.uv.apptransito.ws.Respuesta;

public class FotosActivity extends AppCompatActivity {

    private static final String HOST = "http://10.0.2.2:8084";

    public static final int REQUEST_CAPTURE = 1;
    public static final int PICK_IMAGE = 100;
    private Bitmap foto;
    private Uri photoURI;

    private ImageView img_foto;
    private Button btn_enviar;
    private Button btn_terminar;

    private Integer idReporte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fotos);

        Intent intent = getIntent();
        idReporte = intent.getIntExtra("idReporte", 0);

        img_foto = findViewById(R.id.img_foto);
        btn_enviar = findViewById(R.id.btn_enviar);
        btn_terminar = findViewById(R.id.btn_terminar);

        btn_enviar.setEnabled(false);

        if(!validarCamara()) {
            Toast.makeText(this, "El dispositivo no cuenta con camara", Toast.LENGTH_LONG).show();
        }
        validarPermisosAlmacenados();
    }

    private boolean validarCamara() {
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
    }

    private void validarPermisosAlmacenados() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Sin acceso al almacenamiento interno", Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(this, new String[] {
                    Manifest.permission.WRITE_EXTERNAL_STORAGE } , 1);
        }
    }

    public void tomarForo(View v) {
        btn_enviar.setEnabled(false);
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Crear archivo temporal de imagen
        File tmp = null;
        try {
            tmp = crearArchivoTemporal();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        if (tmp != null) {
            // Acceder a la ruta completa del archivo
            photoURI = FileProvider.getUriForFile(this,
                    getApplicationContext().getPackageName(), tmp);
            i.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            // Lanzar camara
            startActivityForResult(i, REQUEST_CAPTURE);
        }
    }

    private File crearArchivoTemporal() throws IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String nombre = sdf.format(new Date()) + ".jpg";
        File path = getExternalFilesDir(
                Environment.DIRECTORY_PICTURES + "/tmps");
        File archivo = File.createTempFile(nombre,".jpg",path);
        return archivo;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Desde camara
        if (requestCode == REQUEST_CAPTURE) {
            if (resultCode == RESULT_OK) {
                img_foto.setImageURI(this.photoURI);
                foto = ((BitmapDrawable)img_foto.getDrawable()).getBitmap();
                btn_enviar.setEnabled(true);
            }
        }
    }

    public void enviarFoto(View v) {
        new SubirFotoTask(this).execute();
    }

    private RespuestaHandler subirFotoHandler = new RespuestaHandler() {
        @Override
        public void handle(RespuestaWS respuestaWS) {
            img_foto.setImageResource(R.drawable.crash);
            btn_enviar.setEnabled(false);
        }
    };

    private class SubirFotoTask extends ProgressDialogTask {

        public SubirFotoTask(FotosActivity context) {
            super(context, context.subirFotoHandler);
        }

        @Override
        protected Respuesta doInBackground(String... strings) {
            return HttpClient.subirFoto(HOST + "/WSTransito/ws/siniestro/subirfoto/" + idReporte, foto);
        }
    }
}
