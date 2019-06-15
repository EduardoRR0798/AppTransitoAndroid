package mx.uv.apptransito;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import mx.uv.apptransito.ws.HttpUtils;
import mx.uv.apptransito.ws.Respuesta;

import static android.Manifest.permission.READ_SMS;
import static android.Manifest.permission.RECEIVE_SMS;

public class ValidacionActivity extends AppCompatActivity {
    private EditText edtCodigo;
    private Button btnValidar;
    private ProgressDialog pd_espera;
    private String[] params;
    private String json;
    private String telefono;
    public static final Integer PETICION_PERMISO_MENSAJE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validacion);
        edtCodigo = (EditText) findViewById(R.id.edtCodigo);
        btnValidar = (Button) findViewById(R.id.btnValidar);
        params = new String[2];
        leerParametros();
        iniciarBroadcast();
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

    public void confirmar(View v) {
        if (validar()) {
            Toast.makeText(this, telefono, Toast.LENGTH_SHORT).show();
            params[0] = telefono;
            params[1] = edtCodigo.getText().toString();
            WSPOSTConfirmarTask task = new WSPOSTConfirmarTask();
            task.execute(params[1], telefono);
        } else {
            Toast.makeText(this, "Llene todos los campos...", Toast.LENGTH_SHORT).show();
        }
    }

    class WSPOSTConfirmarTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... strings) {
            return HttpUtils.validaregistro(params[1], params[0]);
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
                Toast.makeText(this, "Conductor validado.", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            Toast.makeText(this, "No se puede validar." + telefono, Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validar() {
        if (edtCodigo.getText().toString().isEmpty()) {
            Toast.makeText(this, "Ingrese el codigo.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void leerParametros() {
        Intent intent = getIntent();
        telefono = intent.getStringExtra("telefono");
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            unregisterReceiver(broadCastSMS);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void iniciarBroadcast() {
        if (tienePermisosMensaje()) {
            IntentFilter mIntentFilter = new IntentFilter();
            mIntentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
            registerReceiver(broadCastSMS, mIntentFilter);
        }
    }

    public boolean tienePermisosMensaje() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(READ_SMS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        requestPermissions(new String[]{RECEIVE_SMS, READ_SMS}, PETICION_PERMISO_MENSAJE);
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PETICION_PERMISO_MENSAJE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                iniciarBroadcast();
        }
    }

    private final BroadcastReceiver broadCastSMS = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            final Bundle bundle = intent.getExtras();
            try {
                if (bundle != null) {
                    final Object[] pdusObj = (Object[]) bundle.get("pdus");
                    for (int i = 0; i < pdusObj.length; i++) {
                        SmsMessage sms = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                        String texto = sms.getDisplayMessageBody();
                        Log.v("SMS", texto);

                        try {
                            int index = texto.indexOf(":");
                            String token = texto.substring(index+2, index+6);
                            edtCodigo.setText(token);
                        }catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (Exception e) {
                Log.v("SmsReceiver", "Exception smsReceiver" +e);
            }
        }
    };


}
