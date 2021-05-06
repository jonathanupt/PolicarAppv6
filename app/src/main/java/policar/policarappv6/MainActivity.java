package policar.policarappv6;

import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;


import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    /**
     * IDENTIFICADOR EQUIPO
     */
    private String Identificador = "";

    /*
    * DATOS CONDUCTOR
     */
    private String ConductorId = "";
    private String ConductorNumeroDocumento = "";
    private String ConductorNombre = "";
    private String ConductorEmail = "";
    private String ConductorCelular = "";
    private String ConductorEstado = "";
    private String ConductorContrasena = "";
    private String ConductorFoto = "";

    private Integer ConductorOcupado = 2;
    private Integer ConductorCobertura = 0;

    /*
          * DATOS VEHICULO
           */
    private String VehiculoUnidad = "";
    private String VehiculoPlaca = "";
    private String VehiculoColor = "";
    private String VehiculoModelo = "";
    private String VehiculoTipo = "";
    private String VehiculoDireccionActual = "";

    private String VehiculoCoordenadaX = "0.00";
    private String VehiculoCoordenadaY = "0.00";
   /*
    * EMRESA
     */

    private String RegionId = "";
    private String RegionNombre = "";

    private String EmpresaId = "";
    private String EmpresaNombre = "";
    private String EmpresaFoto = "";

    /*
    * CONDUCTOR DOCUMENTOS
     */
    private String ConductorLicenciaFechaFin = "";
    private String ConductorLicenciaFechaFinNivel = "";
    private String ConductorSOATFechaFin = "";
    private String ConductorSOATFechaFinNivel = "";
    private String ConductorCredencialFechaFin = "";
    private String ConductorCredencialFechaFinNivel = "";
    private String ConductorCredencialConductorFechaFin = "";
    private String ConductorCredencialConductorFechaFinNivel = "";
    private String ConductorRevisionTecnicaFechaFin = "";
    private String ConductorRevisionTecnicaFechaFinNivel = "";

    /**
     * SESION
     */
    private boolean SesionIniciada = false;
    private boolean PrimeraVez = false;

/*
    PERMISOS
     */

    private Context context;
    private Activity activity;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private View view;

/*
    private AlarmManager alarmManager;
    private Intent gpsTrackerIntent;
    private PendingIntent pendingIntent;

    private boolean currentlyTracking;
    private int intervalInMinutes = 1;
    */

    //ONSTART -> ON CREATE -> ONRESTART
    @Override
    protected void onStart() {
        super.onStart();
        Log.e("Main20", "Start");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("Main20", "Resume");

        SharedPreferences sharedPreferences = this.getSharedPreferences("policar.policarappv6", Context.MODE_PRIVATE);

        boolean SesionIniciada = sharedPreferences.getBoolean("SesionIniciada", false);

        if (SesionIniciada) {
/*
            Intent intentMonitoreo = new Intent(MainActivity.this, NavegacionActivity.class);
            startActivity(intentMonitoreo);
            finish();*/

            Intent intentNavegacion = new Intent(MainActivity.this, NavegacionActivity.class);

            Bundle bundleNavegacion = new Bundle();
            bundleNavegacion.putString("PedidoMensaje", "");
            intentNavegacion.putExtras(bundleNavegacion); //Put your id to your next Intent

            startActivity(intentNavegacion);
            finish();



        }



    }
    //ONPAUSE -> ONRESUME -> ONSTOP
    @Override
    protected void onPause() {
        super.onPause();
        Log.e("Main20", "Pause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("Main20", "Stop");
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("Main20", "Restart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("Main20", "Destroy");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e("Main20", "SaveInstance");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.e("Main20", "RestoreInstance");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Log.e("Main20", "onCreate");

      /*  FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //CREANDO BARRA
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowHomeEnabled(true);
        //actionBar.setIcon(R.drawable.icon_logo_solo);             //Establecer icono
        //actionBar.setTitle("Acceso a Cuenta");        //Establecer titulo
        actionBar.setTitle(getString(R.string.title_activity_main));     //Establecer Subtitulo

        //RECUPERANDO VARIABLES v2
        displayUserSettings();

        //PERMISOS
        context = getApplicationContext();
        activity = this;
        //int resultACCESS_FINE_LOCATION = ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION);

        // if (resultACCESS_FINE_LOCATION != PackageManager.PERMISSION_GRANTED){
        //    if (ActivityCompat.shouldShowRequestPermissionRationale(activity, android.Manifest.permission.ACCESS_FINE_LOCATION)){
        //       Snackbar.make(view,"Permiso aceptado.",Snackbar.LENGTH_LONG).show();
        //    } else {
        //       ActivityCompat.requestPermissions(activity,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},PERMISSION_REQUEST_CODE);
        //   }
        // /   }

        //int resultREAD_PHONE_STATE = ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_PHONE_STATE);

        // if (resultREAD_PHONE_STATE != PackageManager.PERMISSION_GRANTED){
        //if (ActivityCompat.shouldShowRequestPermissionRationale(activity, android.Manifest.permission.READ_PHONE_STATE)){
        //   Snackbar.make(view,"Permiso aceptado.",Snackbar.LENGTH_LONG).show();
        //} else {
        //     ActivityCompat.requestPermissions(activity,new String[]{android.Manifest.permission.READ_PHONE_STATE},PERMISSION_REQUEST_CODE);
        // }
        // }
        /*if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){
            Log.e("Main10","NO TIENE PERMISO");
            ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.READ_PHONE_STATE},PERMISSION_REQUEST_CODE);
            Log.e("Main10","PEDIR");

        }else{
            Log.e("Main10","SI TIENE PERMISO");
        }*/
     /*   Log.e("Main10","VERIFICAR PERMISO");
        int result = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);

        if (result != PackageManager.PERMISSION_GRANTED){
            Log.e("Main10","NO TIENE PERMISO");

            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,Manifest.permission.READ_PHONE_STATE)){
                Log.e("Main10","AAA");
                Toast.makeText(context,"GPS permission allows us to access location data. Please allow in App Settings for additional functionality.",Toast.LENGTH_LONG).show();

            } else {
                Log.e("Main10","BBB");
                ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.READ_PHONE_STATE},PERMISSION_REQUEST_CODE);
            }

        } else {
            Log.e("Main10","SI TIENE PERMISO");

        }
*/
        if(checkPermission(1)){
            MtdEstablecerIdentificador();
        }

        // if(resultREAD_PHONE_STATE == PackageManager.PERMISSION_GRANTED) {
        //IDENTIFICANDO EQUIPO
            /*final TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
            final String tmDevice, tmSerial, androidId; tmDevice = "" + tm.getDeviceId();
            tmSerial = "" + tm.getSimSerialNumber();
            androidId = "" + android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
            UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
            Identificador = deviceUuid.toString();*/
        //   }


        /*SharedPreferences sharedPreferences = this.getSharedPreferences("com.websmithing.gpstracker.prefs", Context.MODE_PRIVATE);
        currentlyTracking = sharedPreferences.getBoolean("currentlyTracking", false);

        boolean firstTimeLoadingApp = sharedPreferences.getBoolean("firstTimeLoadingApp", true);
        if (firstTimeLoadingApp) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("firstTimeLoadingApp", false);
            editor.putString("appID",  UUID.randomUUID().toString());
          //  editor.apply();
        }*/

       /* if (checkIfGooglePlayEnabled()) {

            currentlyTracking = true;

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("currentlyTracking", true);
            editor.putFloat("totalDistanceInMeters", 0f);
            editor.putBoolean("firstTimeGettingPosition", true);
            editor.putString("sessionID",  UUID.randomUUID().toString());
            editor.apply();

            Log.e("Main10","Google Play Activado");

            if(currentlyTracking) {

                cancelAlarmManager();
            }

            startAlarmManager();

        }else {

         }*/




    }

    private boolean checkIfGooglePlayEnabled() {
        if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(this) == ConnectionResult.SUCCESS) {
            return true;
        } else {
            //Log.e(TAG, "unable to connect to google play services.");
            Log.e("Main10","unable to connect to google play services.");
            Toast.makeText(getApplicationContext(), "No se encontro Google Play Services instalado", Toast.LENGTH_LONG).show();
            return false;
        }
    }
/*
    private void startAlarmManager() {
//        Log.d(TAG, "startAlarmManager");
        Log.e("Main10","startAlarmManager");

        Context context = getBaseContext();
        alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        gpsTrackerIntent = new Intent(context, GpsTrackerAlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(context, 0, gpsTrackerIntent, 0);

        SharedPreferences sharedPreferences = this.getSharedPreferences("com.websmithing.gpstracker.prefs", Context.MODE_PRIVATE);
        intervalInMinutes = sharedPreferences.getInt("intervalInMinutes", 1);

        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime(),
                intervalInMinutes * 60000, // 60000 = 1 minute
                pendingIntent);
    }

    private void cancelAlarmManager() {
//        Log.d(TAG, "cancelAlarmManager");
        Log.e("Main10","cancelAlarmManager");

        Context context = getBaseContext();
        Intent gpsTrackerIntent = new Intent(context, GpsTrackerAlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, gpsTrackerIntent, 0);
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }
*/
    private boolean checkPermission(int permiso) {

        Log.e("Main10","AAA");

        boolean respuesta = false;

        int MyVersion = Build.VERSION.SDK_INT;

        if (MyVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {


            switch(permiso) {
                case 1:

                    int result = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE);

                    if (result == PackageManager.PERMISSION_GRANTED) {
                        Log.e("Main10","BBB");
                        respuesta = true;
                    }else {
                        respuesta = false;
                        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_PHONE_STATE}, permiso);
                        Log.e("Main10", "DDD");
                    }

                    break;

            }


        }else{

            respuesta = true;
        }

        return respuesta;
    }

    /*  private void requestPermission(int permiso){

          switch (permiso) {
              case PERMISSION_REQUEST_CODE:

                  if (ActivityCompat.shouldShowRequestPermissionRationale(activity,Manifest.permission.READ_PHONE_STATE)){
                      //Snackbar.make(view,"Este permiso permit.",Snackbar.LENGTH_LONG).show();
                      Log.e("Main10","YA TIENE PERMISO");
                      //Toast.makeText(context,"GPS permission allows us to access location data. Please allow in App Settings for additional functionality.",Toast.LENGTH_LONG).show();
                      // Log.e("Main10","AAA");
                  } else {
                      // Log.e("Main10","BBB");
                      ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.READ_PHONE_STATE},PERMISSION_REQUEST_CODE);
                  }

                  break;
          }


      }

  */
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            Log.e("Main10","PERMISO ACEPTADO");

            switch (requestCode) {
                case 1:
                    MtdEstablecerIdentificador();
                    break;

                case 2:

                    break;
            }

        } else {

            Log.e("Main10","PERMISO NEGADO");
            //Snackbar.make(MainActivity.this,"Permiso denegado, es posible que la aplicacion no funcione  correctamente.",Snackbar.LENGTH_LONG).show();
            FncMostrarToast("Permiso denegado, es posible que la aplicacion no funcione  correctamente.");

        }

    }



    public void MtdEstablecerIdentificador() {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

            Identificador =  Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

        }else{


            //IDENTIFICANDO EQUIPO
            final TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
            final String tmDevice, tmSerial, androidId; tmDevice = "" + tm.getDeviceId();
            tmSerial = "" + tm.getSimSerialNumber();
            androidId = "" + android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
            UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
            Identificador = deviceUuid.toString();

        }

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
*/
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_inicio) {
            // Handle the camera action

            Intent intenMain = new Intent(MainActivity.this, MainActivity.class);
            startActivity(intenMain);
            finish();


        } else if(id == R.id.nav_crear_cuenta) {

            Intent intenCrearCuenta = new Intent(MainActivity.this, CrearCuentaActivity.class);
            startActivity(intenCrearCuenta);
            finish();

        } else if (id == R.id.nav_acerca_de) {

            FncMostrarAcercaDe();

        } else if (id == R.id.nav_salir) {

            Intent intentSalir = new Intent(Intent.ACTION_MAIN);
            intentSalir.addCategory(Intent.CATEGORY_HOME);
            intentSalir.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intentSalir.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finish();
            startActivity(intentSalir);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void onClick_BtnMainCrearCuenta(View v) {

        Intent intentCrearCuenta = new Intent(MainActivity.this, CrearCuentaActivity.class);
        startActivity(intentCrearCuenta);
        finish();

    }


    public void onClick_BtnMainCancelar(View v){

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(intent);

    }


    public void onClick_BtnMainAcceder(View v){

        //  if(estaConectado(true)){

        final EditText valConductorNumeroDocumento = (EditText) findViewById(R.id.CmpConductorNumeroDocumento);
        final EditText valConductorContrasena = (EditText) findViewById(R.id.CmpConductorContrasena);

        if("".equals(valConductorNumeroDocumento.getText().toString())) {

            FncMostrarMensaje("No ha ingresado su DNI", false);

        }else  if("".equals(valConductorContrasena.getText().toString())){

            FncMostrarMensaje("No ha ingresado su clave", false);

        }else{

            final ProgressDialog prgMainAcceder = new ProgressDialog(this);
            prgMainAcceder.setIcon(R.mipmap.ic_launcher);
            prgMainAcceder.setMessage("Cargando...");
            prgMainAcceder.setCancelable(false);
            prgMainAcceder.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            prgMainAcceder.show();

            Thread nt = new Thread() {
                @Override
                public void run() {

                    try {

                        final String resMainAcceder;

                        resMainAcceder = MtdAccederConductor(
                                valConductorNumeroDocumento.getText().toString(),
                                valConductorContrasena.getText().toString(),
                                VehiculoCoordenadaX,
                                VehiculoCoordenadaY);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                String JsRespuesta = "";

                                String JsIdentificador = "";

                                String JsRegionId = "";
                                String JsRegionNombre = "";

                                String JsEmpresaId = "";
                                String JsEmpresaNombre = "";
                                String JsEmpresaFoto = "";

                                String JsConductorId = "";
                                String JsConductorNombre = "";
                                String JsConductorEstado = "";
                                String JsConductorNumeroDocumento = "";
                                String JsConductorCelular = "";
                                String JsConductorEmail = "";
                                String JsConductorFoto = "";
                                String JsConductorContrasena = "";

                                String JsVehiculoUnidad = "";
                                String JsVehiculoPlaca = "";
                                String JsVehiculoColor = "";
                                String JsVehiculoModelo = "";
                                String JsVehiculoTipo = "";

                                String JsConductorLicenciaFechaFin = "";
                                String JsConductorLicenciaFechaFinNivel = "";

                                String JsConductorSOATFechaFin = "";
                                String JsConductorSOATFechaFinNivel = "";

                                String JsConductorCredencialFechaFin = "";
                                String JsConductorCredencialFechaFinNivel = "";

                                String JsConductorCredencialConductorFechaFin = "";
                                String JsConductorCredencialConductorFechaFinNivel = "";

                                String JsConductorRevisionTecnicaFechaFin = "";
                                String JsConductorRevisionTecnicaFechaFinNivel = "";

                                try {

                                    JSONObject jsonObject = new JSONObject(resMainAcceder);
                                    JsRespuesta = jsonObject.getString("Respuesta");

                                    JsRegionId = jsonObject.getString("RegionId");
                                    JsRegionNombre = jsonObject.getString("RegionNombre");

                                    JsEmpresaId = jsonObject.getString("EmpresaId");
                                    JsEmpresaNombre = jsonObject.getString("EmpresaNombre");
                                    JsEmpresaFoto = jsonObject.getString("EmpresaFoto");

                                    JsConductorId = jsonObject.getString("ConductorId");
                                    JsConductorNombre = jsonObject.getString("ConductorNombre");
                                    JsConductorEstado = jsonObject.getString("ConductorEstado");
                                    JsConductorNumeroDocumento = jsonObject.getString("ConductorNumeroDocumento");
                                    JsConductorCelular = jsonObject.getString("ConductorCelular");
                                    JsConductorEmail = jsonObject.getString("ConductorEmail");
                                    JsConductorFoto = jsonObject.getString("ConductorFoto");
                                    JsConductorContrasena = jsonObject.getString("ConductorContrasena");

                                    JsVehiculoUnidad = jsonObject.getString("VehiculoUnidad");
                                    JsVehiculoPlaca = jsonObject.getString("VehiculoPlaca");
                                    JsVehiculoColor = jsonObject.getString("VehiculoColor");
                                    JsVehiculoModelo = jsonObject.getString("VehiculoModelo");
                                    JsVehiculoTipo = jsonObject.getString("VehiculoTipo");

                                    JsConductorLicenciaFechaFin = jsonObject.getString("ConductorLicenciaFechaFin");
                                    JsConductorLicenciaFechaFinNivel = jsonObject.getString("ConductorLicenciaFechaFinNivel");

                                    JsConductorSOATFechaFin = jsonObject.getString("ConductorSOATFechaFin");
                                    JsConductorSOATFechaFinNivel = jsonObject.getString("ConductorSOATFechaFinNivel");

                                    JsConductorCredencialFechaFin = jsonObject.getString("ConductorCredencialFechaFin");
                                    JsConductorCredencialFechaFinNivel = jsonObject.getString("ConductorCredencialFechaFinNivel");

                                    JsConductorCredencialConductorFechaFin = jsonObject.getString("ConductorCredencialConductorFechaFin");
                                    JsConductorCredencialConductorFechaFinNivel = jsonObject.getString("ConductorCredencialConductorFechaFinNivel");

                                    JsConductorRevisionTecnicaFechaFin = jsonObject.getString("ConductorRevisionTecnicaFechaFin");
                                    JsConductorRevisionTecnicaFechaFinNivel = jsonObject.getString("ConductorRevisionTecnicaFechaFinNivel");


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                prgMainAcceder.cancel();

                                switch(JsRespuesta){
                                    case "C001":

                                        android.app.AlertDialog.Builder MsgMain = new android.app.AlertDialog.Builder(MainActivity.this);
                                        MsgMain.setTitle(getString(R.string.app_titulo));
                                        MsgMain.setCancelable(false);
                                        MsgMain.setMessage("Bienvenid@ a " + getString(R.string.app_titulo) + "  \"" + JsVehiculoUnidad + "\"");

                                        MsgMain.setPositiveButton("Aceptar",
                                                new DialogInterface.OnClickListener() {

                                                    public void onClick(DialogInterface dialog, int which) {

  /*                                                      Intent intent = new Intent(MainActivity.this, NavegacionActivity.class);
                                                        startActivity(intent);
                                                        finish();
*/
                                                        Intent intentNavegacion = new Intent(MainActivity.this, NavegacionActivity.class);

                                                        Bundle bundleNavegacion = new Bundle();
                                                        bundleNavegacion.putString("PedidoMensaje", "");
                                                        intentNavegacion.putExtras(bundleNavegacion); //Put your id to your next Intent

                                                        startActivity(intentNavegacion);
                                                        finish();


                                                    }
                                                });

                                        android.app.AlertDialog msgMain = MsgMain.create();
                                        msgMain.show();

                                        RegionId = JsRegionId;
                                        RegionNombre = JsRegionNombre;

                                        EmpresaId = JsEmpresaId;
                                        EmpresaNombre = JsEmpresaNombre;
                                        EmpresaFoto = JsEmpresaFoto;

                                        ConductorId = JsConductorId;
                                        ConductorNombre = JsConductorNombre;
                                        ConductorEstado = JsConductorEstado;
                                        ConductorNumeroDocumento = JsConductorNumeroDocumento;
                                        ConductorCelular = JsConductorCelular;
                                        ConductorFoto = JsConductorFoto;
                                        ConductorContrasena = JsConductorContrasena;

                                        VehiculoUnidad = JsVehiculoUnidad;
                                        VehiculoPlaca = JsVehiculoPlaca;
                                        VehiculoColor = JsVehiculoColor;
                                        VehiculoModelo = JsVehiculoModelo;
                                        VehiculoTipo = JsVehiculoTipo;

                                        ConductorLicenciaFechaFin = JsConductorLicenciaFechaFin;
                                        ConductorSOATFechaFin = JsConductorSOATFechaFin;
                                        ConductorRevisionTecnicaFechaFin = JsConductorRevisionTecnicaFechaFin;
                                        ConductorCredencialFechaFin = JsConductorCredencialFechaFin;
                                        ConductorCredencialConductorFechaFin = JsConductorCredencialConductorFechaFin;

                                        ConductorLicenciaFechaFinNivel = JsConductorLicenciaFechaFinNivel;
                                        ConductorSOATFechaFinNivel = JsConductorSOATFechaFinNivel;
                                        ConductorRevisionTecnicaFechaFinNivel = JsConductorRevisionTecnicaFechaFinNivel;
                                        ConductorCredencialFechaFinNivel = JsConductorCredencialFechaFinNivel;
                                        ConductorCredencialConductorFechaFinNivel = JsConductorCredencialConductorFechaFinNivel;


                                        saveUserSettings();

                                        SharedPreferences sharedPreferences = getSharedPreferences("policar.policarappv6", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putBoolean("SesionIniciada", true);
                                        editor.apply();

                                        break;

                                    case "C002":
                                        FncMostrarMensaje("Su contraseña no ha podido ser identificada, intente nuevamente.", false);
                                        break;

                                    case "C013":

                                        FncMostrarMensaje("Su cuenta ha sido suspendida \""+JsVehiculoUnidad+"\".",true);

                                        break;

                                    case "C014":

                                        FncMostrarMensaje("Su unidad no esta registrada para este equipo",true);

                                        break;

                                    case "C031":

                                        FncMostrarMensaje("Su cuenta ha sido retirada \""+JsVehiculoUnidad+"\".",true);

                                        break;

                                    default:
                                        //FncMostrarMensaje(getString(R.string.message_error_interno),true);
                                        FncMostrarToast(getString(R.string.message_error_interno));
                                        break;
                                }

                            }
                        });
                    } catch (Exception e) {
                        // TODO: handle exception
                    }

                }
            };



            nt.start();
        }

        //}

    }


/*
* MENSAJES
 */



    private void FncMostrarAcercaDe() {

        AlertDialog.Builder helpBuilder = new AlertDialog.Builder(this);
        helpBuilder.setTitle(getString(R.string.app_titulo));
        helpBuilder.setMessage(getString(R.string.app_version));
        helpBuilder.setPositiveButton("Aceptar",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                    }
                });

        // Remember, create doesn't show the dialog
        AlertDialog helpDialog = helpBuilder.create();
        helpDialog.show();

    }


    private void FncMostrarMensaje(String oMensaje, final Boolean oCerrar) {

        AlertDialog.Builder MsgGeneral = new AlertDialog.Builder(this);
        MsgGeneral.setTitle(getString(R.string.app_titulo));
        MsgGeneral.setMessage(oMensaje);
        MsgGeneral.setCancelable(false);
        MsgGeneral.setPositiveButton("Aceptar",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                        if(oCerrar){
                            finish();
                        }
                    }
                });

        // Remember, create doesn't show the dialog
        AlertDialog msgGeneral = MsgGeneral.create();
        msgGeneral.show();

    }


    private void FncMostrarToast(String oMensaje){

        Toast.makeText(MainActivity.this, oMensaje,
                Toast.LENGTH_LONG).show();
    }


/*
ENVIO DE VARIABLES
*/

    public String MtdAccederConductor(String oConductorNumeroDocumento,String oConductorContrasena,String oVehiculoCoordenadaX, String oVehiculoCoordenadaY) {

        URL url;
        String response = "";

        try {

            url = new URL(getString(R.string.app_url)+"/webservice/"+getString(R.string.webservice_jnconductor));

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            HashMap<String, String> postDataParams=new HashMap<>();

            postDataParams.put("ConductorNumeroDocumento", oConductorNumeroDocumento);
            postDataParams.put("ConductorContrasena", oConductorContrasena);

            postDataParams.put("VehiculoCoordenadaX", oVehiculoCoordenadaX);
            postDataParams.put("VehiculoCoordenadaY", oVehiculoCoordenadaY);

            postDataParams.put("Identificador", Identificador);
            postDataParams.put("ConductorAppVersion", getString(R.string.app_version));
            postDataParams.put("Accion", "AccederConductor");

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();
            int responseCode=conn.getResponseCode();

            String line;
            BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while ((line=br.readLine()) != null) {
                response+=line;
            }

            Log.e("Main1", response);

        } catch (Exception e) {

            Log.e("Main2", e.toString());
            e.printStackTrace();
        }
        return response;

    }
     /*
    * PREFERENCIAS
    */

    private boolean saveUserSettings() {

        SharedPreferences sharedPreferences = getSharedPreferences("policar.policarappv6", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("Identificador", Identificador.trim());

        editor.putString("RegionId", RegionId.trim());
        editor.putString("RegionNombre", RegionNombre.trim());

        editor.putString("EmpresaId", EmpresaId.trim());
        editor.putString("EmpresaNombre", EmpresaNombre.trim());
        editor.putString("EmpresaFoto", EmpresaFoto.trim());

        editor.putString("ConductorId", ConductorId.trim());
        editor.putString("ConductorNombre", ConductorNombre.trim());
        editor.putString("ConductorNumeroDocumento", ConductorNumeroDocumento.trim());
        editor.putString("ConductorEmail", ConductorEmail.trim());
        editor.putString("ConductorCelular", ConductorCelular.trim());
        editor.putString("ConductorFoto", ConductorFoto.trim());
        editor.putString("ConductorEstado", ConductorEstado.trim());
        editor.putString("ConductorContrasena", ConductorContrasena.trim());

        editor.putString("VehiculoUnidad", VehiculoUnidad.trim());
        editor.putString("VehiculoPlaca", VehiculoPlaca.trim());
        editor.putString("VehiculoColor", VehiculoColor.trim());
        editor.putString("VehiculoModelo", VehiculoModelo.trim());
        editor.putString("VehiculoTipo", VehiculoTipo.trim());

        editor.putString("ConductorLicenciaFechaFin", ConductorLicenciaFechaFin.trim());
        editor.putString("ConductorSOATFechaFin", ConductorSOATFechaFin.trim());
        editor.putString("ConductorRevisionTecnicaFechaFin", ConductorRevisionTecnicaFechaFin.trim());
        editor.putString("ConductorCredencialFechaFin", ConductorCredencialFechaFin.trim());
        editor.putString("ConductorCredencialConductorFechaFin", ConductorCredencialConductorFechaFin.trim());
        editor.putString("ConductorCredencialConductorFechaFinNivel", ConductorCredencialConductorFechaFinNivel.trim());
        editor.putString("ConductorSOATFechaFinNivel", ConductorSOATFechaFinNivel.trim());
        editor.putString("ConductorRevisionTecnicaFechaFinNivel", ConductorRevisionTecnicaFechaFinNivel.trim());
        editor.putString("ConductorCredencialFechaFinNivel", ConductorCredencialFechaFinNivel.trim());
        editor.putString("ConductorCredencialConductorFechaFinNivel", ConductorCredencialConductorFechaFinNivel.trim());


        editor.apply();

        return true;
    }

    private void displayUserSettings() {

        SharedPreferences sharedPreferences = getSharedPreferences("policar.policarappv6", Context.MODE_PRIVATE);

        Identificador = sharedPreferences.getString("Identificador","");

        ConductorId = sharedPreferences.getString("ConductorId","");
        ConductorNombre = sharedPreferences.getString("ConductorNombre", "");
        ConductorNumeroDocumento = sharedPreferences.getString("ConductorNumeroDocumento","");
        ConductorEmail = sharedPreferences.getString("ConductorEmail", "");
        ConductorCelular = sharedPreferences.getString("ConductorCelular", "");
        ConductorFoto = sharedPreferences.getString("ConductorFoto", "");
        ConductorEstado = sharedPreferences.getString("ConductorEstado", "");
        ConductorContrasena = sharedPreferences.getString("ConductorContrasena", "");

        VehiculoUnidad = sharedPreferences.getString("VehiculoUnidad", "");
        VehiculoPlaca = sharedPreferences.getString("VehiculoPlaca", "");
        VehiculoColor = sharedPreferences.getString("VehiculoColor", "");
        VehiculoModelo = sharedPreferences.getString("VehiculoModelo", "");
        VehiculoTipo = sharedPreferences.getString("VehiculoTipo", "");


        ConductorLicenciaFechaFin = sharedPreferences.getString("ConductorLicenciaFechaFin", "");
        ConductorLicenciaFechaFinNivel = sharedPreferences.getString("ConductorLicenciaFechaFinnNivel", "");
        ConductorSOATFechaFin = sharedPreferences.getString("ConductorSOATFechaFin", "");
        ConductorSOATFechaFinNivel = sharedPreferences.getString("ConductorSOATFechaFinNivel", "");
        ConductorCredencialFechaFin = sharedPreferences.getString("ConductorCredencialFechaFin", "");
        ConductorCredencialFechaFinNivel = sharedPreferences.getString("ConductorCredencialFechaFinNivel", "");
        ConductorCredencialConductorFechaFin = sharedPreferences.getString("ConductorCredencialConductorFechaFin", "");
        ConductorCredencialConductorFechaFinNivel = sharedPreferences.getString("ConductorCredencialConductorFechaFinNivel", "");
        ConductorRevisionTecnicaFechaFin = sharedPreferences.getString("ConductorRevisionTecnicaFechaFin", "");
        ConductorRevisionTecnicaFechaFinNivel = sharedPreferences.getString("ConductorRevisionTecnicaFechaFinNivel", "");


        SesionIniciada = sharedPreferences.getBoolean("SesionIniciada", false);

    }

/*
* FUNCIONES AUXILIARES
 */

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }

    

}
