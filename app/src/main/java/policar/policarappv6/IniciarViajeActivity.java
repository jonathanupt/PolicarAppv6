package policar.policarappv6;

import android.app.KeyguardManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PowerManager;
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
import android.app.Notification;
import android.app.PendingIntent;
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
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class IniciarViajeActivity extends AppCompatActivity
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
      * Variables Pedido
      */
    private String PedidoId = "";

    private String ClienteNombre = "";
    private String ClienteCelular = "";
    private String ClienteTelefono = "";
    private String ClienteFoto = "";

    private String PedidoDireccion = "";
    private String PedidoReferencia = "";
    private String PedidoEstado = "";

    private String PedidoCoordenadaX = "0.00";
    private String PedidoCoordenadaY = "0.00";

    /**
     * SESION
     */
    private boolean SesionIniciada = false;
    private boolean PrimeraVez = false;


    /**
     * DATOS GOOGLE MAP
     */

    private GoogleMap googleMap = null;

    private Marker pedidoMarker;
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
    * KILOMETRAJE
    * */
    private Double KilometrajeRecorrido = 0.00;
    private Double PrecioReferencial = 0.00;

    TextView txtKilometrajeRecorrido;
    TextView txtPrecio;
    TextView txtMonedaSimbolo;



    /*
       * DATOS TIMER
        */
    Timer timerIniciarViaje1;
    Timer timerIniciarViaje2;


    /*
    * CONTADOR
     */

    Boolean IniciarViajeFinalizar = false;
    TextView txtCronometroEspera;
    CountDownTimer countTimerRecepcionar2;



    private String RegionId = "";
    private String RegionNombre = "";


    private String SectorId = "";
    private String SectorNombre = "";

    private String MonedaSimbolo = "";
    private String TarifaMinima = "0.00";
    private String TarifaKilometro = "0.00";
    private String TarifaAdicionalNoche = "0.00";
    private String TarifaAdicionalFestivo = "0.00";

    private String PedidoEsNoche = "2";
    private String PedidoEsFestivo = "2";


    //ONSTART -> ON CREATE -> ONRESTART
    @Override
    protected void onStart() {
        super.onStart();
        Log.e("IniciarViaje20", "Start");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("IniciarViaje20", "Resume");

    }

    //ONPAUSE -> ONRESUME -> ONSTOP
    @Override
    protected void onPause() {
        super.onPause();
        Log.e("IniciarViaje20", "Pause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("IniciarViaje20", "Stop");
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("IniciarViaje20", "Restart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("IniciarViaje20", "Destroy");


        if (timerIniciarViaje1 != null) {
            timerIniciarViaje1.cancel();
        }


        if (timerIniciarViaje2 != null) {
            timerIniciarViaje2.cancel();
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e("IniciarViaje20", "SaveInstance");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.e("IniciarViaje20", "RestoreInstance");
    }
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_viaje);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Log.e("IniciarViaje20", "onCreate");

/*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
*/
     /*   DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
*/
        //CREANDO BARRA
        ActionBar actionBar = getSupportActionBar();

        //actionBar.setDisplayShowHomeEnabled(true);
       // actionBar.setIcon(R.mipmap.ic_launcher);             //Establecer icono
        //actionBar.setTitle("Viaje Iniciado");        //Establecer titulo
        actionBar.setTitle(getString(R.string.title_activity_iniciar_viaje));     //Establecer Subtitulo


//PERMISOS
        context = getApplicationContext();
        activity = this;

        //PREFERENCIAS
        sharedPreferences2 = getSharedPreferences("policar.policarappv6", Context.MODE_PRIVATE);
        KilometrajeRecorrido = Double.valueOf(sharedPreferences2.getFloat("KilometrajeRecorrido",0));

        //RECUPERANDO VARIABLES
        Intent intentExtras = getIntent();
        Bundle extrasBundle = intentExtras.getExtras();

        PedidoId = intentExtras.getStringExtra("PedidoId");
        VehiculoCoordenadaX = intentExtras.getStringExtra("VehiculoCoordenadaX");
        VehiculoCoordenadaY = intentExtras.getStringExtra("VehiculoCoordenadaY");

        MonedaSimbolo = intentExtras.getStringExtra("MonedaSimbolo");
        TarifaMinima = intentExtras.getStringExtra("TarifaMinima");
        TarifaKilometro = intentExtras.getStringExtra("TarifaKilometro");
        TarifaAdicionalNoche = intentExtras.getStringExtra("TarifaAdicionalNoche");
        TarifaAdicionalFestivo = intentExtras.getStringExtra("TarifaAdicionalFestivo");

        PedidoEsNoche = intentExtras.getStringExtra("PedidoEsNoche");
        PedidoEsFestivo = intentExtras.getStringExtra("PedidoEsFestivo");

        IniciarViajeFinalizar = false;

        //RECUPERANDO VARIABLES v2
        displayUserSettings();

        //CAJAS
        txtKilometrajeRecorrido = (TextView) findViewById(R.id.CmpIniciarViajeKilometrajeRecorrido);
        txtCronometroEspera = (TextView) findViewById(R.id.CmpIniciarViajeCronometroEspera);
        txtPrecio = (TextView) findViewById(R.id.CmpIniciarViajePrecio);
        txtMonedaSimbolo = (TextView) findViewById(R.id.CmpIniciarViajeMonedaSimbolo);



        //MOSTRANDO VARIABLES
        txtKilometrajeRecorrido.setText(Double.toString(KilometrajeRecorrido)+" km");
        txtCronometroEspera.setText("");
        txtPrecio.setText("0.0");
        txtMonedaSimbolo.setText(MonedaSimbolo);

        //OBTENER COORDENADAS
        if(checkPermission(2)){
            MtdObtenerCoordenadas();
        }

        //CREANDO MAPA
        if(checkPermission(1)){
            createMapView();
        }


        if (timerIniciarViaje1 != null) {
            timerIniciarViaje1.cancel();
        }

        timerIniciarViaje1 = new Timer();
        timerIniciarViaje1.schedule(new TimerTask() {
            @Override
            public void run() {

                try {

                    final String resActualizarConductorCoordenada;
                    resActualizarConductorCoordenada = MtdActualizarConductorCoordenada(PedidoId, ConductorId, ConductorNombre,ConductorNumeroDocumento,VehiculoCoordenadaX, VehiculoCoordenadaY,VehiculoUnidad,VehiculoPlaca);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            String JsRespuesta = "";
                            String JsRegionNombre = "";
                            String JsRegionId = "";
                            String JsSectorNombre = "";
                            String JsSectorId = "";

                            try {

                                JSONObject jsonObject = new JSONObject(resActualizarConductorCoordenada);
                                JsRespuesta = jsonObject.getString("Respuesta");

                                JsRegionNombre = jsonObject.getString("RegionNombre");
                                JsRegionId = jsonObject.getString("RegionId");

                                JsSectorNombre = jsonObject.getString("SectorNombre");
                                JsSectorId = jsonObject.getString("SectorId");

                            } catch (JSONException e) {
                                Log.e("IniciarViaje9", e.toString());
                                e.printStackTrace();
                            }

                            switch (JsRespuesta) {

                                case "U001":

                                    SharedPreferences.Editor editor = sharedPreferences2.edit();
                                    editor.putString("RegionNombre", JsRegionNombre.trim());
                                    editor.putString("RegionId", JsRegionId.trim());
                                    editor.putString("SectorNombre", JsSectorNombre.trim());
                                    editor.putString("SectorId", JsSectorId.trim());
                                    editor.apply();

                                    if(!JsSectorId.equals(SectorId)) {
                                        FncMostrarToast("Bienvenid@ a Sector "+JsSectorNombre);
                                    }

                                    RegionId = JsRegionId;
                                    RegionNombre = JsRegionNombre;

                                    SectorId = JsSectorId;
                                    SectorNombre = JsSectorNombre;

                                    PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
                                    PowerManager.WakeLock wakeLock = pm.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "TAG");
                                    wakeLock.acquire();

                                    KeyguardManager keyguardManager = (KeyguardManager) getApplicationContext().getSystemService(Context.KEYGUARD_SERVICE);
                                    KeyguardManager.KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("TAG");
                                    keyguardLock.disableKeyguard();


                                    break;

                                case "U002":
                                    break;

                                case "U003":
                                    break;

                                default:
                                    break;

                            }

                        }
                    });
                } catch (final Exception e) {
                    // TODO: handle exception
                    Log.e("MsgIniciarViaje11", e.toString());
                }


            }
        }, 1000, 3500);
        //TAREA ENVIAR COORDENADA PEDIDO





        if(timerIniciarViaje2!=null) {
            timerIniciarViaje2.cancel();
        }
        //TAREA ESPERAR PEDIDO

        timerIniciarViaje2 = new Timer();
        timerIniciarViaje2.schedule(new TimerTask(){
            @Override
            public void run() {

                // if( estaConectado(false)){

                try {

                    final String resMtdObtenerMapa;
                    resMtdObtenerMapa = MtdObtenerMapa(ConductorId);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            String JsRespuesta = "";
                            JSONArray JsDatos;

                            if (googleMap != null) {
                                googleMap.clear();
                            }

                            if(!VehiculoCoordenadaX.equals("") && !VehiculoCoordenadaY.equals("")   && VehiculoCoordenadaX != null && VehiculoCoordenadaY != null){

                                if(vehiculoMarker!=null){
                                    vehiculoMarker.remove();
                                }

                                vehiculoMarker = googleMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(Double.parseDouble(VehiculoCoordenadaX), Double.parseDouble(VehiculoCoordenadaY)))
                                        .title("¡Aquí estoy!")
                                        .draggable(false)
                                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_taxi150)));

                            }

                            try {

                                JSONObject jsonObject = new JSONObject(resMtdObtenerMapa);
                                JsRespuesta = jsonObject.getString("Respuesta");
                                JsDatos = jsonObject.getJSONArray("Datos");

                                for (int i = 0; i < JsDatos.length(); i++) {

                                    JSONObject jsonObj = JsDatos.getJSONObject(i);

                                    String JsDestinoId = jsonObj.getString("DestinoId");
                                    String JsDestinoNombre = jsonObj.getString("DestinoNombre");
                                    String JsDestinoTipo = jsonObj.getString("DestinoTipo");
                                    String JsDestinoActividad = jsonObj.getString("DestinoActividad");
                                    String JsDestinoCoordenadaX = jsonObj.getString("DestinoCoordenadaX");
                                    String JsDestinoCoordenadaY = jsonObj.getString("DestinoCoordenadaY");
                                    String JsDestinoIconoUrl = jsonObj.getString("DestinoIconoUrl");

                                    if(!JsDestinoCoordenadaX.equals("") && !JsDestinoCoordenadaY.equals("")   && JsDestinoCoordenadaX != null && JsDestinoCoordenadaY != null){

                                        if(!ConductorId.equals(JsDestinoId)){

                                            switch(JsDestinoTipo){
                                                case "1":

                                                    googleMap.addMarker(new MarkerOptions()
                                                            .position(new LatLng(Double.parseDouble(JsDestinoCoordenadaX), Double.parseDouble(JsDestinoCoordenadaY)))
                                                            .title(JsDestinoNombre)
                                                            .draggable(false)
                                                            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_paradero150)));
                                                    break;

                                                case "2":

                                                    googleMap.addMarker(new MarkerOptions()
                                                            .position(new LatLng(Double.parseDouble(JsDestinoCoordenadaX), Double.parseDouble(JsDestinoCoordenadaY)))
                                                            .title(JsDestinoNombre)
                                                            .draggable(false)
                                                            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_turismo150)));
                                                    break;

                                                case "3":

                                                    googleMap.addMarker(new MarkerOptions()
                                                            .position(new LatLng(Double.parseDouble(JsDestinoCoordenadaX), Double.parseDouble(JsDestinoCoordenadaY)))
                                                            .title(JsDestinoNombre)
                                                            .draggable(false)
                                                            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_restaurante150)));
                                                    break;

                                                case "4":

                                                    googleMap.addMarker(new MarkerOptions()
                                                            .position(new LatLng(Double.parseDouble(JsDestinoCoordenadaX), Double.parseDouble(JsDestinoCoordenadaY)))
                                                            .title(JsDestinoNombre)
                                                            .draggable(false)
                                                            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_hospedaje150)));
                                                    break;

                                                case "6":

                                                    googleMap.addMarker(new MarkerOptions()
                                                            .position(new LatLng(Double.parseDouble(JsDestinoCoordenadaX), Double.parseDouble(JsDestinoCoordenadaY)))
                                                            .title(JsDestinoNombre)
                                                            .draggable(false)
                                                            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_taxi_otro150)));

                                                    break;

                                                default:

                                                    break;



                                            }


                                        }



                                    }

                                }

                                switch(JsRespuesta){

                                    case "M001":
                                        break;

                                    case "M002":
                                        break;

                                    case "M003":
                                        break;

                                    default:
                                        break;

                                }

                            } catch (JSONException e) {
                                Log.e("AcpetarMedido2", e.toString());
                                e.printStackTrace();
                            }


                        }
                    });

                } catch (Exception e) {
                    // TODO: handle exception
                    Log.e("AceptarPedido1", e.toString());
                }

                //  }

            }
        }, 1000, 3000);


/*

        final ProgressDialog PrgIniciarViaje = new ProgressDialog(this);
        PrgIniciarViaje.setIcon(R.mipmap.ic_launcher);
        PrgIniciarViaje.setMessage("Cargando...");
        PrgIniciarViaje.setCancelable(false);
        PrgIniciarViaje.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        PrgIniciarViaje.show();

        Thread nt = new Thread() {
            @Override
            public void run() {

                try {

                    final String resMtdFinalizarViaje;
                    resMtdFinalizarViaje = MtdFinalizarViaje(PedidoId,ConductorId, VehiculoCoordenadaX, VehiculoCoordenadaY);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            String JsRespuesta = "";

                            try {

                                JSONObject jsonObject = new JSONObject(resMtdFinalizarViaje);
                                JsRespuesta = jsonObject.getString("Respuesta");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            PrgIniciarViaje.cancel();

                            switch(JsRespuesta){
                                case "P099":

                                    SharedPreferences sharedPreferences = getSharedPreferences("policar.policarappv6", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putInt("ConductorOcupado",2);
                                    editor.putBoolean("ConductorTienePedido", false);
                                    editor.apply();

                                    Intent intent = new Intent(IniciarViajeActivity.this, NavegacionActivity.class);
                                    startActivity(intent);
                                    finish();

                                    break;

                                case "P100":
                                    break;

                                case "P101":
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

*/

  /* //CONTADOR
        countTimerRecepcionar2 = new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {

                //TextView TxtTiempoTranscurrido = (TextView) findViewById(R.id.CmpTiempoTranscurrido);
                txtCronometroEspera.setText("Tiempo transcurrido: " + String.valueOf(millisUntilFinished / 1000) + " Segundos");

                IniciarViajeFinalizar = false;

            }

            public void onFinish() {

                //TextView TxtTiempoTranscurrido = (TextView) findViewById(R.id.CmpTiempoTranscurrido);
                txtCronometroEspera.setText("Tiempo concluido");

                IniciarViajeFinalizar = true;


            }
        }.start();
*/
        IniciarViajeFinalizar = true;
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
                googleMap.getUiSettings().setZoomControlsEnabled(true);
                googleMap.setPadding(0, 0, 0, 0);

                googleMap.setOnMyLocationButtonClickListener(
                        new GoogleMap.OnMyLocationButtonClickListener() {

                            @Override
                            public boolean onMyLocationButtonClick() {

                                Location location = googleMap.getMyLocation();

                                if(location!=null){

                                    VehiculoCoordenadaX = Double.toString(location.getLatitude());
                                    VehiculoCoordenadaY = Double.toString(location.getLongitude());

                                    Thread nt = new Thread() {
                                        @Override
                                        public void run() {

                                            //if(estaConectado(false)){

                                            try {

                                                final String resActualizarConductorCoordenada;
                                                resActualizarConductorCoordenada = MtdActualizarConductorCoordenada(PedidoId, ConductorId,ConductorNombre,ConductorNumeroDocumento, VehiculoCoordenadaX, VehiculoCoordenadaY,VehiculoUnidad,VehiculoPlaca);

                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {

                                                        String JsRespuesta = "";

                                                        try {

                                                            JSONObject jsonObject = new JSONObject(resActualizarConductorCoordenada);
                                                            JsRespuesta = jsonObject.getString("Respuesta");

                                                        } catch (JSONException e) {
                                                            // Log.e("MsgMonitoreo7", e.toString());
                                                            e.printStackTrace();
                                                        }

                                                        switch(JsRespuesta){

                                                            case "C007":
                                                                break;

                                                            case "C008":
                                                                break;

                                                            case "C009":
                                                                break;

                                                            default:
                                                                break;

                                                        }

                                                    }
                                                });
                                            } catch (final Exception e) {
                                                // TODO: handle exception
                                                // Log.e("MsgMonitoreo11", e.toString());
                                            }

                                            //}



                                        }

                                    };
                                    nt.start();


                                }else{

                                    FncMostrarToast("No se pudo obtener su ubicación");

                                }

                                return false;
                            }
                        }


                );


                googleMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
                    @Override
                    public void onMyLocationChange(Location location) {


                        if(location!=null){

                            Double DblVehiculoCoordenadaX = location.getLongitude();
                            Double DblVehiculoCoordenadaY = location.getLongitude();

                            Double  UltimoKilometraje = round(calculateDistance(Double.parseDouble(VehiculoCoordenadaX),Double.parseDouble(VehiculoCoordenadaY),location.getLatitude(),location.getLongitude()),2);


                            VehiculoCoordenadaX = Double.toString(location.getLatitude());
                            VehiculoCoordenadaY = Double.toString(location.getLongitude());

                            KilometrajeRecorrido = KilometrajeRecorrido + UltimoKilometraje;

                            KilometrajeRecorrido = round(KilometrajeRecorrido,2);

                            txtKilometrajeRecorrido.setText(Double.toString(KilometrajeRecorrido)+" km");

                            SharedPreferences.Editor editor = sharedPreferences2.edit();
                            editor.putFloat("KilometrajeRecorrido",  KilometrajeRecorrido.floatValue());
                            editor.apply();




                            PrecioReferencial = KilometrajeRecorrido * Float.parseFloat(TarifaKilometro);

                            if(PrecioReferencial<Float.parseFloat(TarifaMinima)){
                                PrecioReferencial = 0.00;
                                PrecioReferencial = PrecioReferencial + Float.parseFloat(TarifaMinima);
                            }

                            if(PedidoEsFestivo.equals("1")){
                                PrecioReferencial = PrecioReferencial + Float.parseFloat(TarifaAdicionalFestivo);
                            }

                            if(PedidoEsNoche.equals("1")){
                                PrecioReferencial = PrecioReferencial + Float.parseFloat(TarifaAdicionalNoche);

                            }

                            PrecioReferencial = redondear(PrecioReferencial,0);

                            txtPrecio.setText(Double.toString(PrecioReferencial)+"");


                            if(!VehiculoCoordenadaX.equals("") && !VehiculoCoordenadaY.equals("")  &&  VehiculoCoordenadaX !=null && VehiculoCoordenadaY != null){

                                if(vehiculoMarker!=null){
                                    vehiculoMarker.remove();
                                }

                                vehiculoMarker = googleMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(Double.parseDouble(VehiculoCoordenadaX), Double.parseDouble(VehiculoCoordenadaY)))
                                        .title("¡Aquì estoy!")
                                        .draggable(false)
                                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_taxi150)));

                                LatLng latLng = new LatLng(Double.parseDouble(VehiculoCoordenadaX),Double.parseDouble(VehiculoCoordenadaY));
                                CameraPosition cameraPosition = new CameraPosition.Builder()
                                        .target(latLng)      // Sets the center of the map to Mountain View
                                        .zoom(16)                   // Sets the zoom
                                        //.bearing(20)  //era 90              // Sets the orientation of the camera to east
                                        .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                                        .build();                   // Creates a CameraPosition from the builder
                                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


                            }
                            
                        }else{
                            
                            
                        }
                       

                    }
                });


                if(!VehiculoCoordenadaX.equals("") && !VehiculoCoordenadaY.equals("")   &&  VehiculoCoordenadaX != null && VehiculoCoordenadaY != null) {

                    if(vehiculoMarker!=null){
                        vehiculoMarker.remove();
                    }

                    vehiculoMarker = googleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(Double.parseDouble(VehiculoCoordenadaX), Double.parseDouble(VehiculoCoordenadaY)))
                            .title("¡Aqui estoy!")
                            .draggable(false)
                            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_taxi150)));

                    LatLng latLng = new LatLng(Double.parseDouble(VehiculoCoordenadaX),Double.parseDouble(VehiculoCoordenadaY));
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(latLng)      // Sets the center of the map to Mountain View
                            .zoom(16)                   // Sets the zoom
                            //.bearing(20)  //era 90              // Sets the orientation of the camera to east
                            .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                            .build();                   // Creates a CameraPosition from the builder
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

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
            //FncMostrarToast(exception.toString());
            FncMostrarToast("Ha ocurrido un error cargando el mapa: "+exception.toString());

            Log.e("PedidoActual90", exception.toString());

        }

    }




    private boolean checkPermission(int permiso) {
        Log.e("Navegacion10","VERIFICAR PERMISO");
        boolean respuesta = false;

        int MyVersion = Build.VERSION.SDK_INT;

        if (MyVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {

            switch(permiso){
                case 1:

                    int result1 = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);

                    if (result1 == PackageManager.PERMISSION_GRANTED) {
                        Log.e("Navegacion10","1AAA");
                        respuesta = true;
                    }else {
                        respuesta = false;
                        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, permiso);
                        Log.e("Navegacion10", "1BBB");
                    }

                    break;

                case 2:

                    int result2 = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION);

                    if (result2 == PackageManager.PERMISSION_GRANTED) {
                        Log.e("Navegacion10","2AAA");
                        respuesta = true;
                    }else {
                        respuesta = false;
                        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, permiso);
                        Log.e("Navegacion10", "2BBB");
                    }

                    break;

                case 3:

                    int result3 = ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO);

                    if (result3 == PackageManager.PERMISSION_GRANTED) {
                        Log.e("Navegacion10","3AAA");
                        respuesta = true;
                    }else {
                        respuesta = false;
                        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.RECORD_AUDIO}, permiso);
                        Log.e("Navegacion10", "3BBB");
                    }

                    break;

                case 4:

                    int result4 = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);

                    if (result4 == PackageManager.PERMISSION_GRANTED) {
                        Log.e("Navegacion10","4AAA");
                        respuesta = true;
                    }else {
                        respuesta = false;
                        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, permiso);
                        Log.e("Navegacion10", "4BBB");
                    }

                    break;

                case 5:

                    int result5 = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE);

                    if (result5 == PackageManager.PERMISSION_GRANTED) {
                        Log.e("Navegacion10","5AAA");
                        respuesta = true;
                    }else {
                        respuesta = false;
                        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, permiso);
                        Log.e("Navegacion10", "5BBB");
                    }

                    break;

                case 6:

                    int result6 = ContextCompat.checkSelfPermission(context, Manifest.permission.VIBRATE);

                    if (result6 == PackageManager.PERMISSION_GRANTED) {
                        Log.e("Navegacion10","5AAA");
                        respuesta = true;
                    }else {
                        respuesta = false;
                        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.VIBRATE}, permiso);
                        Log.e("Navegacion10", "5BBB");
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

            Log.e("Navegacion10","PERMISO ACEPTADO");

            switch (requestCode) {
                case 1:
                    createMapView();
                    break;

                case 2:
                    MtdObtenerCoordenadas();
                    break;

            }

        } else {
            Log.e("Navegacion10","PERMISO NEGADO");
            FncMostrarToast("Permiso denegado, es posible que la aplicacion no funcione  correctamente.");
        }

    }



    public void MtdObtenerCoordenadas() {

        //OBTENIENDO UBICACION
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //Toast.makeText(this, "GPS is Enabled in your devide", Toast.LENGTH_SHORT).show();

            IniciarViajeListener mlocListener = new IniciarViajeListener();
            mlocListener.setIniciarViajeActivity(this);
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

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.iniciar_viaje, menu);
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
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_inicio) {
            // Handle the camera action

  /*      Intent intenNavegacion = new Intent(IniciarViajeActivity.this, NavegacionActivity.class);
            startActivity(intenNavegacion);
            finish();
*/
            Intent intentNavegacion = new Intent(IniciarViajeActivity.this, NavegacionActivity.class);

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

                            Intent intentMainSesion = new Intent(IniciarViajeActivity.this, MainActivity.class);
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

        Toast.makeText(IniciarViajeActivity.this, oMensaje,
                Toast.LENGTH_LONG).show();
    }


    protected void displayNotificationOne(String oTitulo,String oContenido,String oContenidoGrande) {

        int notificationId = 001;

        // Build intent for notification content
        Intent viewIntent = new Intent(IniciarViajeActivity.this, MainActivity.class);
        //viewIntent.putExtra(EXTRA_EVENT_ID, eventId);
        PendingIntent viewPendingIntent =
                PendingIntent.getActivity(IniciarViajeActivity.this, 0, viewIntent, 0);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(IniciarViajeActivity.this)
                        .setSmallIcon(policar.policarappv6.R.mipmap.ic_launcher)
                        .setContentTitle(oTitulo)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(oContenidoGrande))
                        .setContentText(oContenido)
                        //.setVibrate(new long[] { 100, 250 })
                        .setDefaults(Notification.DEFAULT_SOUND).setContentIntent(viewPendingIntent)
                ;

        //.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000}).build();

        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(IniciarViajeActivity.this);

        notificationManager.notify(notificationId, notificationBuilder.build());

    }





    public void onClick_BtnIniciarViajeFinalizar(View v) {


        if(IniciarViajeFinalizar ){

            final ProgressDialog PrgIniciarViaje = new ProgressDialog(this);
            PrgIniciarViaje.setIcon(R.mipmap.ic_launcher);
            PrgIniciarViaje.setMessage("Cargando...");
            PrgIniciarViaje.setCancelable(false);
            PrgIniciarViaje.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            PrgIniciarViaje.show();

            Thread nt = new Thread() {
                @Override
                public void run() {

                    try {

                        final String resMtdFinalizarViaje;
                        resMtdFinalizarViaje = MtdFinalizarViaje(PedidoId,ConductorId, VehiculoCoordenadaX, VehiculoCoordenadaY,KilometrajeRecorrido.floatValue());

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                String JsRespuesta = "";

                                try {

                                    JSONObject jsonObject = new JSONObject(resMtdFinalizarViaje);
                                    JsRespuesta = jsonObject.getString("Respuesta");

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                PrgIniciarViaje.cancel();

                                switch(JsRespuesta){
                                    case "P099":

                                        //SharedPreferences sharedPreferences = getSharedPreferences("policar.policarappv6", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences2.edit();
                                        editor.putInt("ConductorOcupado",2);
                                        editor.putBoolean("ConductorTienePedido", false);

                                        editor.putFloat("KilometrajeRecorrido",  0);
                                        editor.putLong("PedTiempoCreacion",  SystemClock.elapsedRealtime());

                                        editor.apply();
/*
                                    Intent intent = new Intent(IniciarViajeActivity.this, NavegacionActivity.class);
                                    startActivity(intent);
                                    finish();*/

                                        Intent intentNavegacion = new Intent(IniciarViajeActivity.this, NavegacionActivity.class);

                                        Bundle bundleNavegacion = new Bundle();
                                        bundleNavegacion.putString("PedidoMensaje", "");
                                        intentNavegacion.putExtras(bundleNavegacion); //Put your id to your next Intent

                                        startActivity(intentNavegacion);
                                        finish();





                                        break;


                                    case "P100":
                                        break;

                                    case "P101":
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

        }else{

            FncMostrarMensaje("Tienen que transcurrir 60 segundos para finalizar el viaje.",false);

        }





       /* final ProgressDialog PrgIniciarViaje = new ProgressDialog(this);
        PrgIniciarViaje.setIcon(R.mipmap.ic_launcher);
        PrgIniciarViaje.setMessage("Cargando...");
        PrgIniciarViaje.setCancelable(false);
        PrgIniciarViaje.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        PrgIniciarViaje.show();

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

                            PrgIniciarViaje.cancel();

                            switch(JsRespuesta){
                                case "C026":

                                    SharedPreferences sharedPreferences = getSharedPreferences("policar.policarappv6", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putInt("ConductorOcupado",2);
                                    editor.putBoolean("ConductorTienePedido", false);
                                    editor.apply();

                                    Intent intent = new Intent(IniciarViajeActivity.this, NavegacionActivity.class);
                                    startActivity(intent);
                                    finish();
                                    
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

        nt.start();*/

        

    }




    public String MtdObtenerMapa(String oConductorId) {



        URL url;
        String response = "";

        try {

            url = new URL( getString(R.string.app_url2)+"/webservice/"+getString(R.string.webservice_jnmapa));

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            HashMap<String, String> postDataParams=new HashMap<>();
            postDataParams.put("ConductorId", oConductorId);
            postDataParams.put("Cercanos", "1");

            postDataParams.put("Identificador", Identificador);
            postDataParams.put("ConductorAppVersion", getString(policar.policarappv6.R.string.app_version));
            postDataParams.put("Accion", "ObtenerMapa");

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

            Log.e("MsgIniciarViaje11", response);

        } catch (Exception e) {

            Log.e("MsgIniciarViaje2", e.toString());
            e.printStackTrace();
        }

        return response;

    }



    public String MtdActualizarConductorCoordenada(String oPedidoId, String oConductorId,String oConductorNombre,String oConductorNumeroDocumento,String oConductorCoordenadaX,String oConductorCoordenadaY,String oVehiculoUnidad,String oVehiculoPlaca) {

        URL url;
        String response = "";

        try {

            url = new URL(getString(R.string.app_url2) + "/webservice/" + getString(R.string.webservice_jnconductorubicacion));

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);


            HashMap<String, String> postDataParams = new HashMap<>();
            postDataParams.put("PedidoId", oPedidoId);

            postDataParams.put("ConductorId", oConductorId);
            postDataParams.put("ConductorNombre", oConductorNombre);
            postDataParams.put("ConductorNumeroDocumento", oConductorNumeroDocumento);

            postDataParams.put("VehiculoUnidad", oVehiculoUnidad);
            postDataParams.put("VehiculoPlaca", oVehiculoPlaca);
            postDataParams.put("VehiculoCoordenadaX", oConductorCoordenadaX);
            postDataParams.put("VehiculoCoordenadaY", oConductorCoordenadaY);
            postDataParams.put("Formulario","NavegacionActivity");

            postDataParams.put("Identificador", Identificador);
            postDataParams.put("Accion", "ActualizarConductorCoordenada");

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();
            int responseCode = conn.getResponseCode();

            String line;
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while ((line = br.readLine()) != null) {
                response += line;
            }

            Log.e("MSgIniciarViaje11", response);

        } catch (Exception e) {

            Log.e("MSgIniciarViaje12", e.toString());
            e.printStackTrace();
        }

        return response;

    }






   /*
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

    */

    public String MtdFinalizarViaje(String oPedidoId, String oConductorId,String oVehiculoCoordenadaX,String oVehiculoCoordenadaY,Float oKilometrajeRecorrido) {

        URL url;
        String response = "";

        try {

            url = new URL(getString(R.string.app_url)+"/webservice/"+getString(R.string.webservice_jnpedido));

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            HashMap<String, String>postDataParams=new HashMap<>();
            postDataParams.put("ConductorId", oConductorId);
            postDataParams.put("PedidoId", oPedidoId);

            postDataParams.put("VehiculoCoordenadaX", oVehiculoCoordenadaX);
            postDataParams.put("VehiculoCoordenadaY", oVehiculoCoordenadaY);
            postDataParams.put("KilometrajeRecorrido", String.valueOf(oKilometrajeRecorrido));

            postDataParams.put("AppVersion", getString(R.string.app_version));

            postDataParams.put("Identificador", Identificador);
            postDataParams.put("Accion", "FinalizarViaje");

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

            Log.e("IniciarViaje5", response);

        } catch (Exception e) {

            Log.e("IniciarViaje6", e.toString());
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



    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    private float calculateDistance(double fromLong, double fromLat,double toLong, double toLat) {

        LatLng my_latlong = new LatLng((fromLong),(fromLat));
        LatLng frnd_latlong = new LatLng((toLong),(toLat));

        Location l1=new Location("One");
        l1.setLatitude(my_latlong.latitude);
        l1.setLongitude(my_latlong.longitude);

        Location l2=new Location("Two");
        l2.setLatitude(frnd_latlong.latitude);
        l2.setLongitude(frnd_latlong.longitude);

        float distance=l1.distanceTo(l2);

        //if(distance>1000.0f)
        // {
        distance=distance/1000.0f;
        // dist=distance;
        // }
        return distance;

    }

    public static double redondear(double d, int decimalPlace) {

        BigDecimal bd = new BigDecimal(Double.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.doubleValue();

    }


}
