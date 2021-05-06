package policar.policarappv6;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
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
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

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


public class ConductorDesconectadoActivity extends AppCompatActivity
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



    /**
     * SESION
     */
    private boolean SesionIniciada = false;
    private boolean PrimeraVez = false;

    /**
     * DATOS GOOGLE MAP
     */

    private GoogleMap googleMap = null;
    private Marker vehiculoMarker;

    /*
    * dDATOS GPS
     */
    final String gpsLocationProvider = LocationManager.GPS_PROVIDER;
    final String networkLocationProvider = LocationManager.NETWORK_PROVIDER;

    /*
    PERMISOS
     */
    private Context context;
    private Activity activity;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private View view;
    /*
       * PREFERENCIAS
       * */
    SharedPreferences sharedPreferences2;
    /*
    * COORDENADAS
     */
    private AlarmManager alarmManager;
    private Intent gpsTrackerIntent;
    private PendingIntent pendingIntent;

    private boolean currentlyTracking;
    private int intervalInMinutes = 1;


    //ONSTART -> ON CREATE -> ONRESTART
    @Override
    protected void onStart() {
        super.onStart();
        Log.e("ConductorDesconectado20", "Start");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("ConductorDesconectado20", "Resume");
    }

    //ONPAUSE -> ONRESUME -> ONSTOP
    @Override
    protected void onPause() {
        super.onPause();
        Log.e("ConductorDesconectado20", "Pause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("ConductorDesconectado20", "Stop");
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("ConductorDesconectado20", "Restart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("ConductorDesconectado20", "Destroy");



    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e("ConductorDesconectado20", "SaveInstance");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.e("ConductorDesconectado20", "RestoreInstance");
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conductor_desconectado);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

      /*  FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

       /* DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
*/
        Log.e("ConductorDesconectado20", "onCreate");



        //CREANDO BARRA
        ActionBar actionBar = getSupportActionBar();

        //actionBar.setDisplayShowHomeEnabled(true);
        //actionBar.setIcon(R.mipmap.ic_launcher);             //Establecer icono
        //actionBar.setTitle("Desconectado");        //Establecer titulo
       actionBar.setTitle(getString(R.string.title_activity_conductor_desconectado));     //Establecer Subtitulo

        //RECUPERANDO VARIABLES v2
        displayUserSettings();

        //PERMISOS
        context = getApplicationContext();
        activity = this;

        //CAB MENU
        /*View header = navigationView.getHeaderView(0);
        TextView txtUsuario = (TextView) header.findViewById(R.id.CmpUsuario);
        txtUsuario.setText(ConductorNombre);
*/

        //RECUPERANDO VARIABLES
        Intent intentExtras = getIntent();
        Bundle extrasBundle = intentExtras.getExtras();

        ConductorId = intentExtras.getStringExtra("ConductorId");
        ConductorOcupado = intentExtras.getIntExtra("ConductorOcupado",0);

        //PREFERENCIAS
        sharedPreferences2 = getSharedPreferences("policar.policarappv6", Context.MODE_PRIVATE);




        final ProgressDialog PrgConductorDesconectado = new ProgressDialog(this);
        PrgConductorDesconectado.setIcon(R.mipmap.ic_launcher);
        PrgConductorDesconectado.setMessage("Cargando...");
        PrgConductorDesconectado.setCancelable(false);
        PrgConductorDesconectado.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        PrgConductorDesconectado.show();

        Thread nt = new Thread() {
            @Override
            public void run() {

                try {

                    final String resActualizarConductorOcupado;
                    resActualizarConductorOcupado = MtdActualizarConductorOcupado(ConductorId,ConductorOcupado, VehiculoCoordenadaX, VehiculoCoordenadaY);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            String JsRespuesta = "";

                            try {

                                JSONObject jsonObject = new JSONObject(resActualizarConductorOcupado);
                                JsRespuesta = jsonObject.getString("Respuesta");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            PrgConductorDesconectado.cancel();

                            switch(JsRespuesta){
                                case "C026":
                                    break;

                                case "C027":
                                    break;

                                case "C028":
                                    break;

                                default:
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





        //OBTENER COORDENADAS
        if(checkPermission(2)){
            MtdObtenerCoordenadas();
        }

        //CREANDO MAPA
        if(checkPermission(1)){
            createMapView();
        }






        currentlyTracking = sharedPreferences2.getBoolean("currentlyTracking", false);


        if (checkIfGooglePlayEnabled()) {
            // currentlyTracking = true;
            SharedPreferences.Editor editor = sharedPreferences2.edit();
            editor.putBoolean("currentlyTracking", false);
            //editor.putFloat("totalDistanceInMeters", 0f);
            //editor.putBoolean("firstTimeGettingPosition", true);
            // editor.putString("sessionID",  UUID.randomUUID().toString());
            editor.apply();

            Log.e("Main10","Google Play Activado");

            if(currentlyTracking) {
                cancelAlarmManager();
            }

            //startAlarmManager();

        }




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
    private void startAlarmManager() {
//        Log.d(TAG, "startAlarmManager");
        Log.e("Main10","startAlarmManager");

        Context context = getBaseContext();
        alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        gpsTrackerIntent = new Intent(context, GpsTrackerAlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(context, 0, gpsTrackerIntent, 0);

        SharedPreferences sharedPreferences = this.getSharedPreferences("policar.policarappv6", Context.MODE_PRIVATE);
        intervalInMinutes = sharedPreferences.getInt("intervalInMinutes", 30000);

        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime(),
                intervalInMinutes, // 60000 = 1 minute
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


    private boolean checkPermission(int permiso) {

        Log.e("ConductorDesconectado10","VERIFICAR PERMISO");

        boolean respuesta = false;

        int MyVersion = Build.VERSION.SDK_INT;

        if (MyVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {

            switch(permiso){
                case 1:

                    int result1 = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);

                    if (result1 == PackageManager.PERMISSION_GRANTED) {
                        Log.e("ConductorDesconectado10","AAA");
                        respuesta = true;
                    }else {
                        respuesta = false;
                        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, permiso);
                        Log.e("ConductorDesconectado10", "BBB");
                    }

                    break;

                case 2:

                    int result2 = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);

                    if (result2 == PackageManager.PERMISSION_GRANTED) {
                        Log.e("ConductorDesconectado10","AAA");
                        respuesta = true;
                    }else {
                        respuesta = false;
                        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, permiso);
                        Log.e("ConductorDesconectado10", "BBB");
                    }

                    break;
            }

        }else{
            respuesta = true;
        }

        return respuesta;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            Log.e("ConductorDesconectado10","PERMISO ACEPTADO");

            switch (requestCode) {
                case 1:
                    createMapView();
                    break;

                case 2:
                    MtdObtenerCoordenadas();
                    break;
            }

        } else {
            Log.e("ConductorDesconectado10","PERMISO NEGADO");
            FncMostrarToast("Permiso denegado, es posible que la aplicacion no funcione  correctamente.");
        }

    }



    public void MtdObtenerCoordenadas() {

        //OBTENIENDO UBICACION
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //Toast.makeText(this, "GPS is Enabled in your devide", Toast.LENGTH_SHORT).show();

            ConductorDesconectadoListener mlocListener = new ConductorDesconectadoListener();
            mlocListener.setConductorDesconectadoActivity(this);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,mlocListener);

            Location lastKnownLocation_byGps =
                    locationManager.getLastKnownLocation(gpsLocationProvider);
            Location lastKnownLocation_byNetwork =
                    locationManager.getLastKnownLocation(networkLocationProvider);

            if(lastKnownLocation_byGps==null){
                if(lastKnownLocation_byNetwork==null){

                }else{
                    VehiculoCoordenadaX = Double.toString(lastKnownLocation_byNetwork.getLatitude());
                    VehiculoCoordenadaY = Double.toString(lastKnownLocation_byNetwork.getLongitude());
                }

            }else{
                if(lastKnownLocation_byNetwork==null){
                    VehiculoCoordenadaX = Double.toString(lastKnownLocation_byGps.getLatitude());
                    VehiculoCoordenadaY = Double.toString(lastKnownLocation_byGps.getLongitude());

                }else{
                    if(lastKnownLocation_byGps.getAccuracy() <= lastKnownLocation_byNetwork.getAccuracy()){
                        VehiculoCoordenadaX = Double.toString(lastKnownLocation_byGps.getLatitude());
                        VehiculoCoordenadaY = Double.toString(lastKnownLocation_byGps.getLongitude());
                    }else{
                        VehiculoCoordenadaX = Double.toString(lastKnownLocation_byNetwork.getLatitude());
                        VehiculoCoordenadaY = Double.toString(lastKnownLocation_byNetwork.getLongitude());
                    }
                }
            }

        } else {
            Toast.makeText(this, "Su GPS no esta activo", Toast.LENGTH_SHORT).show();
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

   /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.conductor_desconectado, menu);
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
/*
            Intent intenNavegacion = new Intent(ConductorDesconectadoActivity.this, NavegacionActivity.class);
            startActivity(intenNavegacion);
            finish();
*/
            Intent intentNavegacion = new Intent(ConductorDesconectadoActivity.this, NavegacionActivity.class);

            Bundle bundleNavegacion = new Bundle();
            bundleNavegacion.putString("PedidoMensaje", "");
            intentNavegacion.putExtras(bundleNavegacion); //Put your id to your next Intent

            startActivity(intentNavegacion);
            finish();



        } else if (id == R.id.nav_salir_cuenta) {

            android.app.AlertDialog.Builder MsgMonitoreo = new android.app.AlertDialog.Builder(this);
            MsgMonitoreo.setTitle(getString(R.string.alert_title));
            MsgMonitoreo.setMessage("¿Realmente deseas cerrar sesión?");
            MsgMonitoreo.setCancelable(false);
            MsgMonitoreo.setPositiveButton("Si",
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            // Do nothing but close the dialog
                            ConductorId = "";
                            ConductorNombre = "";
                            ConductorEstado = "";
                            ConductorNumeroDocumento = "";
                            ConductorCelular = "";
                            ConductorFoto = "";
                            ConductorContrasena = "";

                            VehiculoUnidad = "";
                            VehiculoPlaca = "";
                            VehiculoColor = "";
                            VehiculoModelo = "";
                            VehiculoTipo = "";

                            saveUserSettings();

                            //CERRAR SESION
                            SharedPreferences sharedPreferences = getSharedPreferences("policar.policarappv6", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("SesionIniciada", false);
                            editor.apply();

                            Intent intentMainSesion = new Intent(ConductorDesconectadoActivity.this, MainActivity.class);
                            startActivity(intentMainSesion);
                            finish();

                        }
                    });


            MsgMonitoreo.setNegativeButton("No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            // Remember, create doesn't show the dialog
            android.app.AlertDialog msgMonitoreo = MsgMonitoreo.create();
            msgMonitoreo.show();

        } else if (id == R.id.nav_acerca_de) {

            FncMostrarAcercaDe();

        } else if (id == R.id.nav_salir) {

            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finish();
            startActivity(intent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void createMapView(){

        /**
         * Catch the null pointer exception that
         * may be thrown when initialising the map
         */
        try {

            if(null == googleMap){

                googleMap  = ((MapFragment) getFragmentManager().findFragmentById(R.id.map1)).getMap();
                googleMap.setMyLocationEnabled(true);
                googleMap.getUiSettings().setZoomControlsEnabled(false);
                googleMap.setPadding(0, 0, 0, 0);
                googleMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
                    @Override
                    public void onMyLocationChange(Location location) {

                        VehiculoCoordenadaX = Double.toString(location.getLatitude());
                        VehiculoCoordenadaY = Double.toString(location.getLongitude());

                    }
                });

                if(!VehiculoCoordenadaX.equals("") && !VehiculoCoordenadaY.equals("")  &&  VehiculoCoordenadaX !=null && VehiculoCoordenadaY != null){

                    if(vehiculoMarker!=null){
                        vehiculoMarker.remove();
                    }

                    vehiculoMarker = googleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(Double.parseDouble(VehiculoCoordenadaX), Double.parseDouble(VehiculoCoordenadaY)))
                            .title("¡Aquì estoy!")
                            .draggable(true)
                            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_taxi150)));

                    // if( (VehiculoCoordenadaX.equals("") && VehiculoCoordenadaY.equals("")) || (VehiculoCoordenadaX.equals("0.00") && VehiculoCoordenadaY.equals("0.00")) || (VehiculoCoordenadaX == null && VehiculoCoordenadaY == null)){

                    LatLng latLng = new LatLng(Double.parseDouble(VehiculoCoordenadaX),Double.parseDouble(VehiculoCoordenadaY));
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(latLng)      // Sets the center of the map to Mountain View
                            .zoom(19)                   // Sets the zoom
                            .bearing(20)  //era 90              // Sets the orientation of the camera to east
                            .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                            .build();                   // Creates a CameraPosition from the builder
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                    //  }

                }






                /**
                 * If the map is still null after attempted initialisation,
                 * show an error to the user
                 */
                if(null == googleMap) {
                    FncMostrarToast("Ha ocurrido un error cargando el mapa.");
                }

            }
        } catch (Exception exception){
            // Toast.makeText(getApplicationContext(),
            //   exception.toString(),Toast.LENGTH_SHORT).show();
            FncMostrarToast("Ha ocurrido un error cargando el mapa: "+exception.toString());
        }
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
                        if (oCerrar) {
                            finish();
                        }
                    }
                });

        // Remember, create doesn't show the dialog
        AlertDialog msgGeneral = MsgGeneral.create();
        msgGeneral.show();

    }


    private void FncMostrarToast(String oMensaje){

        Toast.makeText(ConductorDesconectadoActivity.this, oMensaje,
                Toast.LENGTH_LONG).show();
    }

/*
* EVENTOS NAVEGACION
 */

    public void onClick_BtnConductorDesconectadoConectar(View v){

        // MediaPlayer player = MediaPlayer.create(ConductorDesconectadoActivity.this, R.raw.sou_boton1);
        //  player = MediaPlayer.create(ConductorDesconectadoActivity.this, R.raw.sou_boton1);
        //  player.start();

        final ProgressDialog PrgConductorDesconectado = new ProgressDialog(this);
        PrgConductorDesconectado.setIcon(R.mipmap.ic_launcher);
        PrgConductorDesconectado.setMessage("Cargando...");
        PrgConductorDesconectado.setCancelable(false);
        PrgConductorDesconectado.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        PrgConductorDesconectado.show();

        Thread nt = new Thread() {
            @Override
            public void run() {

                try {

                    final String resActualizarConductorOcupado;
                    resActualizarConductorOcupado = MtdActualizarConductorOcupado(ConductorId,2, VehiculoCoordenadaX, VehiculoCoordenadaY);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            String JsRespuesta = "";

                            try {

                                JSONObject jsonObject = new JSONObject(resActualizarConductorOcupado);
                                JsRespuesta = jsonObject.getString("Respuesta");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            PrgConductorDesconectado.cancel();
                            SharedPreferences.Editor editor = sharedPreferences2.edit();

                            switch(JsRespuesta){
                                case "C026":

                                    //GUARDAR MEMORIA

                                    editor.putInt("ConductorOcupado", 2);
                                    editor.apply();


                                   /* Intent intentNavegacion26 = new Intent(ConductorDesconectadoActivity.this, NavegacionActivity.class);
                                    startActivity(intentNavegacion26);
                                    finish();
*/
                                    Intent intentNavegacion26 = new Intent(ConductorDesconectadoActivity.this, NavegacionActivity.class);

                                    Bundle bundleNavegacion26 = new Bundle();
                                    bundleNavegacion26.putString("PedidoMensaje", "");
                                    intentNavegacion26.putExtras(bundleNavegacion26); //Put your id to your next Intent

                                    startActivity(intentNavegacion26);
                                    finish();




                                    break;

                                case "C027":

                                    //GUARDAR MEMORIA

                                    editor.putInt("ConductorOcupado", 2);
                                    editor.apply();

/*
                                    Intent intentNavegacion27 = new Intent(ConductorDesconectadoActivity.this, NavegacionActivity.class);
                                    startActivity(intentNavegacion27);
                                    finish();
*/

                                    Intent intentNavegacion27 = new Intent(ConductorDesconectadoActivity.this, NavegacionActivity.class);

                                    Bundle bundleNavegacion27 = new Bundle();
                                    bundleNavegacion27.putString("PedidoMensaje", "");
                                    intentNavegacion27.putExtras(bundleNavegacion27); //Put your id to your next Intent

                                    startActivity(intentNavegacion27);
                                    finish();



                                    break;

                                case "C028":

                                    //GUARDAR MEMORIA

                                    editor.putInt("ConductorOcupado", 2);
                                    editor.apply();

/*
                                    Intent intentNavegacion28 = new Intent(ConductorDesconectadoActivity.this, NavegacionActivity.class);
                                    startActivity(intentNavegacion28);
                                    finish();
                                    */
                                    Intent intentNavegacion28 = new Intent(ConductorDesconectadoActivity.this, NavegacionActivity.class);

                                    Bundle bundleNavegacion28 = new Bundle();
                                    bundleNavegacion28.putString("PedidoMensaje", "");
                                    intentNavegacion28.putExtras(bundleNavegacion28); //Put your id to your next Intent

                                    startActivity(intentNavegacion28);
                                    finish();


                                    break;

                                default:
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

       /*
        Intent intentConductorConectar = new Intent(ConductorDesconectadoActivity.this, NavegacionActivity.class);

        // Bundle bundleConductorDesconectado = new Bundle();
        // bundleConductorDesconectado.putString("ConductorId", ConductorId);

        //intentConductorConectar.putExtras(bundleConductorDesconectado); //Put your id to your next Intent

        startActivity(intentConductorConectar);
        finish();
*/

    }


   /*
    public void onClick_BtnConductorDesconectadoEsconder(View v){

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(intent);


    }
    */

    public void onClick_BtnConductorDesconectadoAjustar(View v){

       /* Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(intent);
*/

        Intent intentMisPreferencias = new Intent(ConductorDesconectadoActivity.this, MisPreferenciasActivity.class);

        Bundle bundleMisPreferencias = new Bundle();
        bundleMisPreferencias.putString("ConductorId", ConductorId);

        intentMisPreferencias.putExtras(bundleMisPreferencias); //Put your id to your next Intent

        startActivity(intentMisPreferencias);
        finish();

    }





    public String MtdActualizarConductorOcupado(String oConductorId,Integer oConductorOcupado,String oVehiculoCoordenadaX,String oVehiculoCoordenadaY) {

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

            HashMap<String, String>postDataParams=new HashMap<>();
            postDataParams.put("ConductorId", oConductorId);
            postDataParams.put("ConductorOcupado", Integer.toString(oConductorOcupado));
            postDataParams.put("VehiculoCoordenadaX", oVehiculoCoordenadaX);
            postDataParams.put("VehiculoCoordenadaY", oVehiculoCoordenadaY);
            postDataParams.put("AppVersion", getString(R.string.app_version));

            postDataParams.put("Identificador", Identificador);
            postDataParams.put("Accion", "ActualizarConductorOcupado");

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

            Log.e("ConductorDesconectado5", response);

        } catch (Exception e) {

            Log.e("ConductorDesconectado6", e.toString());
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
