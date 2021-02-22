package policar.policarappv6;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import android.Manifest;
import android.app.Activity;
import android.app.KeyguardManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.PowerManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class NavegacionActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener {

    /**
     * IDENTIFICADOR EQUIPO
     */
    private String Identificador = "";

    /*
    * Variables Pedido
    */
    private String PedidoId = "";
    private String PedidoFecha = "";
    private String PedidoHora = "";

    private String ClienteId = "";
    private String ClienteNombre = "";
    private String ClienteCelular = "";
    private String ClienteTelefono = "";
    private String ClienteFoto = "";
    private String ClienteVerificado = "";

    private String PedidoTipo = "";
    private String PedidoTipoUnidad = "";
    private String PedidoTipoAccion = "";
    private String PedidoDistancia = "";
    private String PedidoTiempo = "";


    private String PedidoDireccion = "";
    private String PedidoReferencia = "";
    private String PedidoDetalle = "";
    private String PedidoLugarCompra = "";
    private String PedidoEstado = "";

    private String PedidoCoordenadaX = "0.00";
    private String PedidoCoordenadaY = "0.00";

    private String PedidoMensaje = "";


    /*
    * DATOS CONDUCTOR
     */

    private String EmpresaId = "";
    private String EmpresaNombre = "";
    private String EmpresaFoto = "";

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

    private String ConductorIdDestino = "";
    private String ConductorVehiculoUnidadDestino = "";


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


    /**
     * DATOS TIMER
     */

    Timer timerNavegacion1;
    Timer timerNavegacion2;
    Timer timerNavegacion3;
    Timer timerNavegacion4;

    Timer timerNavegacion5;
    Timer timerNavegacion6;

    /*
    * RADIO
     */
    private String RadioCanal = "";
    private String ConductorCanal = "";

    private boolean RadioActivada = false;
    private boolean RadioIniciar = false;


    ProgressDialog dialog;

    private MediaRecorder mediaRecorder = null;
    private MediaPlayer mediaPlayer = null;

    private static String audioFilePath;
    private static ImageButton stopButton;
    private static ImageButton playButton;
    private static ImageButton recordButton;
    private static ImageButton recibirButton;

    private boolean isRecording = false;
    private boolean isPlaying = false;
    private boolean isPlayingAnterior = false;

    private boolean RadioMensajeReproducir = false;
    ArrayList RadioMensajeRecibidos = new ArrayList();

    private String RadioAccion = "";

    TextView txtCanalActual;

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
    * REGION
     */

    private String RegionId = "";
    private String RegionNombre = "";

    private String SectorId = "";
    private String SectorNombre = "";



    /*
    * COORDENADAS
     */
    private AlarmManager alarmManager;
    private Intent gpsTrackerIntent;
    private PendingIntent pendingIntent;

    private boolean currentlyTracking;
    private int intervalInMinutes = 1;


    /*
    * FLOTANTES
     */
    private Boolean isFabOpen = true;
    private FloatingActionButton fab, fab1, fab2, fab3, fab4, fab5, fab6, fab41, fab42, fab43, fab44, fab45, fabRadio;
    private Animation fab_open, fab_close, rotate_forward, rotate_backward;


    ArrayList PedidoRechazados = new ArrayList();

    private final static int MAX_VOLUME = 100;





    /*
    *RADIO
     */

    private AudioRecordHelper mAudioRecordHelper;
    private AudioPlayHelper mAudioPlayHelper;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    private Boolean MonitoreoSonido;
    private Boolean MonitoreoEncendido;
    private String TipoRadio;

/*
* SONIDOS
 */
    MediaPlayer mediaPlayerEmergencia;
    MediaPlayer mediaPlayerPedido;
    MediaPlayer mediaPlayerMensaje;

    MediaPlayer mediaPlayerRadioA;
    MediaPlayer mediaPlayerRadioB;

    MediaPlayer mediaPlayerEmergenciaEnviar;




    /*
* CONDUCTOR ALERTA
 */
    private String ConductorAlertaCoordenadaX = "";
    private String ConductorAlertaCoordenadaY = "";


    private String Cobertura = "0";

    private String PedidoEsNoche = "2";
    private String PedidoEsFestivo = "2";
    //ONSTART -> ON CREATE -> ONRESTART
    @Override
    protected void onStart() {
        super.onStart();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Log.e("Navegacion20", "Start");
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Navegacion Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://policar.policarappv6/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);


    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("Navegacion20", "Resume");


   /*     //GUARDAR MEMORIA
        SharedPreferences.Editor editor2 = sharedPreferences2.edit();
        editor2.putInt("ConductorOcupado", 2);
        editor2.apply();*/

        //RECUPERANDO SESION
        SharedPreferences sharedPreferences = getSharedPreferences("policar.policarappv6", Context.MODE_PRIVATE);

        //boolean ConductorTienePedidoRecepcionar = sharedPreferences.getBoolean("ConductorTienePedidoRecepcionar", false);
        boolean valConductorTienePedido = sharedPreferences.getBoolean("ConductorTienePedido", false);
        Integer valConductorOcupado = sharedPreferences.getInt("ConductorOcupado", 2);

        if (valConductorTienePedido) {

            //cosmosgps.cosmosgpsappv
            Intent intentPedidoActual = new Intent(NavegacionActivity.this, PedidoActualActivity.class);
            Bundle bundlePedidoActual = new Bundle();

            bundlePedidoActual.putString("PedidoId", sharedPreferences.getString("PedidoId", ""));

            bundlePedidoActual.putString("ClienteId", sharedPreferences.getString("ClienteId", ""));
            bundlePedidoActual.putString("ClienteNombre", sharedPreferences.getString("ClienteNombre", ""));
            bundlePedidoActual.putString("ClienteCelular", sharedPreferences.getString("ClienteCelular", ""));
            bundlePedidoActual.putString("ClienteTelefono", sharedPreferences.getString("ClienteTelefono", ""));

            bundlePedidoActual.putString("PedidoDireccion", sharedPreferences.getString("PedidoDireccion", ""));
            bundlePedidoActual.putString("PedidoReferencia", sharedPreferences.getString("PedidoReferencia", ""));
            bundlePedidoActual.putString("PedidoDetalle", sharedPreferences.getString("PedidoDetalle", ""));
            bundlePedidoActual.putString("PedidoLugarCompra", sharedPreferences.getString("PedidoLugarCompra", ""));

            bundlePedidoActual.putString("PedidoEstado", sharedPreferences.getString("PedidoEstado", ""));
            bundlePedidoActual.putString("PedidoTipo", sharedPreferences.getString("PedidoTipo", ""));
            bundlePedidoActual.putString("PedidoTipoUnidad", sharedPreferences.getString("PedidoTipoUnidad", ""));
            bundlePedidoActual.putString("PedidoTipoAccion", sharedPreferences.getString("PedidoTipoAccion", ""));

            bundlePedidoActual.putString("PedidoTiempo", sharedPreferences.getString("PedidoTiempo", ""));
            bundlePedidoActual.putString("PedidoDistancia", sharedPreferences.getString("PedidoDistancia", ""));

            bundlePedidoActual.putString("PedidoCoordenadaX", sharedPreferences.getString("PedidoCoordenadaX", "0.00"));
            bundlePedidoActual.putString("PedidoCoordenadaY", sharedPreferences.getString("PedidoCoordenadaY", "0.00"));

            bundlePedidoActual.putString("VehiculoCoordenadaX", sharedPreferences.getString("VehiculoCoordenadaX", "0.00"));
            bundlePedidoActual.putString("VehiculoCoordenadaY", sharedPreferences.getString("VehiculoCoordenadaY", "0.00"));

            bundlePedidoActual.putString("MonedaSimbolo", sharedPreferences.getString("MonedaSimbolo", ""));
            bundlePedidoActual.putString("TarifaMinima", sharedPreferences.getString("TarifaMinima", "0.00"));
            bundlePedidoActual.putString("TarifaKilometro", sharedPreferences.getString("TarifaKilometro", "0.00"));
            bundlePedidoActual.putString("TarifaAdicionalNoche", sharedPreferences.getString("TarifaAdicionalNoche", "0.00"));
            bundlePedidoActual.putString("TarifaAdicionalFestivo", sharedPreferences.getString("TarifaAdicionalFestivo", "0.00"));
            bundlePedidoActual.putString("PedidoEsNoche", sharedPreferences.getString("PedidoEsNoche", "2"));
            bundlePedidoActual.putString("PedidoEsFestivo", sharedPreferences.getString("PedidoEsFestivo", "2"));


            intentPedidoActual.putExtras(bundlePedidoActual);//Put your id to your next Intent
            startActivity(intentPedidoActual);
            finish();

        } else if (valConductorOcupado == 1 || valConductorOcupado == 11) {

            Intent intentConductorDesconectado1 = new Intent(NavegacionActivity.this, ConductorDesconectadoActivity.class);

            Bundle bundleConductorDesconectado1 = new Bundle();
            bundleConductorDesconectado1.putString("ConductorId", ConductorId);
            bundleConductorDesconectado1.putInt("ConductorOcupado", valConductorOcupado);

            intentConductorDesconectado1.putExtras(bundleConductorDesconectado1); //Put your id to your next Intent

            startActivity(intentConductorDesconectado1);
            finish();

            // Intent intentConductorOcupado = new Intent(NavegacionActivity.this, ConductorDesconectadoActivity.class);
            // startActivity(intentConductorOcupado);
            // finish();

        } else {

            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                //Toast.makeText(this, "GPS is Enabled in your devide", Toast.LENGTH_SHORT).show();
            } else {
                //showGPSDisabledAlertToUser();
                Toast.makeText(this, "Su GPS no esta activo", Toast.LENGTH_SHORT).show();
            }


        }


        //RECUPERANDO VARIABLES v2
        displayUserSettings();




        /*
        DESMINIMIZAR
        RESTART
        START
        RESUME

        MINIMIZAR
        PAUSE
        SAVE INSTANCE
        STOP

        PASAR ACTIVITY
        PAUSE
        STOP
        DESTROY

        INICIAR ACTIVITY
        ONCREATE
        START
        RESUME
        */

    }

    //ONPAUSE -> ONRESUME -> ONSTOP
    @Override
    protected void onPause() {
        super.onPause();
        Log.e("Navegacion20", "Pause");


    }

    @Override
    protected void onStop() {
        super.onStop();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Navegacion Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://policar.policarappv6/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        Log.e("Navegacion20", "Stop");
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.disconnect();
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("Navegacion20", "Restart");

        Log.e("Navegacion500", "MINIMIZADO FALSE");
//MINIMIZADO
        sharedPreferences2 = getSharedPreferences("policar.policarappv6", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor2 = sharedPreferences2.edit();
        editor2.putBoolean("Minimizado", false);
        editor2.apply();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("Navegacion20", "Destroy");

        if (timerNavegacion1 != null) {
            timerNavegacion1.cancel();
        }

        if (timerNavegacion2 != null) {
            timerNavegacion2.cancel();
        }

        if (timerNavegacion3 != null) {
            timerNavegacion3.cancel();
        }

        if (timerNavegacion4 != null) {
            timerNavegacion4.cancel();
        }


        if (timerNavegacion5 != null) {
            timerNavegacion5.cancel();
        }
        if (timerNavegacion6 != null) {
            timerNavegacion6.cancel();
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e("Navegacion20", "SaveInstance");

        Log.e("Navegacion500", "MINIMIZADO TRUE");
        //MINIMIZADO
        sharedPreferences2 = getSharedPreferences("policar.policarappv6", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor2 = sharedPreferences2.edit();
        editor2.putBoolean("Minimizado", true);
        editor2.apply();

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.e("Navegacion20", "RestoreInstance");
    }

/*
    int resultACCESS_FINE_LOCATION;
    int resultREAD_PHONE_STATE;
    int resultRECORD_AUDIO;
    int resultACCESS_COARSE_LOCATION;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navegacion);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Log.e("Navegacion20", "onCreate");

       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
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
        //actionBar.setIcon(R.mipmap.ic_launcher);             //Establecer icono
        //actionBar.setTitle("Monitoreo");        //Establecer titulo
        actionBar.setTitle(getString(R.string.title_activity_navegacion));     //Establecer Subtitulo

        //RECUPERAR VARIABLES
        displayUserSettings();

        //PERMISOS
        context = getApplicationContext();
        activity = this;

        //CAB MENU

        View header = navigationView.getHeaderView(0);
        TextView txtUsuario = (TextView) header.findViewById(R.id.CmpUsuario);
        txtUsuario.setText(ConductorNombre);


        //MOSTRANDO VARIABLES
        TextView TxtVehiculoUnidad = (TextView) findViewById(R.id.CmpVehiculoUnidad);
        TxtVehiculoUnidad.setText(VehiculoUnidad);

        TextView TxtVehiculoPlaca = (TextView) findViewById(R.id.CmpVehiculoPlaca);
        TxtVehiculoPlaca.setText(VehiculoPlaca);

        TextView TxtVehiculoColor = (TextView) findViewById(R.id.CmpVehiculoColor);
        TxtVehiculoColor.setText(VehiculoColor);


        txtCanalActual = (TextView) findViewById(R.id.CmpCanalActual);


        mediaPlayerEmergencia = MediaPlayer.create(context, R.raw.alerta);
        mediaPlayerPedido = MediaPlayer.create(context, R.raw.sou_pedidov7);
        mediaPlayerMensaje = MediaPlayer.create(context, R.raw.sou_mensaje);

        mediaPlayerRadioA = MediaPlayer.create(context, R.raw.sou_radio11a);
        mediaPlayerRadioB = MediaPlayer.create(context, R.raw.sou_radio11b);

        mediaPlayerRadioB = MediaPlayer.create(context, R.raw.sou_radio11b);

        mediaPlayerEmergenciaEnviar = MediaPlayer.create(context, R.raw.sou_alerta);

        //mediaPlayerRadioB.start();
        //PREFERENCIAS
        sharedPreferences2 = getSharedPreferences("policar.policarappv6", Context.MODE_PRIVATE);

        //OBTENER COORDENADAS
        if (checkPermission(2)) {
            MtdObtenerCoordenadas();
        }

        //CREANDO MAPA
        if (checkPermission(1)) {
            createMapView();
        }

        //CREANDO CARPETA
        if (checkPermission(4)) {
            MtdCrearCarpetaRadio();
        }

        //RADIO
        ConductorCanal = sharedPreferences2.getString("ConductorCanal", "0");
        ConductorIdDestino = sharedPreferences2.getString("ConductorIdDestino", "");
        ConductorVehiculoUnidadDestino = sharedPreferences2.getString("ConductorVehiculoUnidadDestino", "");
        Cobertura = sharedPreferences2.getString("Cobertura", "0");

        RadioAccion = "1";

        //REGION Y SECTOR
        RegionId = sharedPreferences2.getString("RegionId", "");
        SectorId = sharedPreferences2.getString("SectorId", "");

        //PREFERENCIAS
        MonitoreoSonido = sharedPreferences2.getBoolean("MonitoreoSonido",true);
        MonitoreoEncendido = sharedPreferences2.getBoolean("MonitoreoEncendido",true);
        TipoRadio = sharedPreferences2.getString("TipoRadio","T3GP");

        Log.e("TipoRadio",TipoRadio);

        //RECUPERANDO VARIABLES
        Intent intentExtras = getIntent();
        Bundle extrasBundle = intentExtras.getExtras();

        PedidoMensaje = intentExtras.getStringExtra("PedidoMensaje");

        if (!PedidoMensaje.equals("")) {

            //FncMostrarMensaje(PedidoMensaje, false);



            AlertDialog.Builder MsgNavegacion1 = new AlertDialog.Builder(this);
            MsgNavegacion1.setTitle(getString(R.string.app_titulo));
            MsgNavegacion1.setMessage(PedidoMensaje);
            MsgNavegacion1.setCancelable(false);
            /*MsgNavegacion1.setPositiveButton("Aceptar",
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            // Do nothing but close the dialog

                        }
                    });*/

            // Remember, create doesn't show the dialog
            final AlertDialog msgNavegacion1 = MsgNavegacion1.create();
            msgNavegacion1.show();


            final Timer timer1 = new Timer();
            timer1.schedule(new TimerTask() {
                public void run() {

                    if(msgNavegacion1!=null && msgNavegacion1.isShowing()){
                        msgNavegacion1.dismiss();
                    }

                    timer1.cancel(); //this will cancel the timer of the system


                }
            }, 1500); // the timer will count 5 seconds....

        }

      //  AudioManager audioManager = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
        //audioManager.setMode(AudioManager.MODE_NORMAL);

        /*AudioManager amanager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        int maxVolume = amanager.getStreamMaxVolume(AudioManager.STREAM_ALARM);
        amanager.setStreamVolume(AudioManager.STREAM_ALARM, maxVolume, 0);
*/



        AudioManager am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        int yourVolume = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        am.setStreamVolume(AudioManager.STREAM_MUSIC, yourVolume, 0);

        if (timerNavegacion1 != null) {
            timerNavegacion1.cancel();
        }

        timerNavegacion1 = new Timer();
        timerNavegacion1.schedule(new TimerTask() {
            @Override
            public void run() {

               // Log.e("MsgMonitoreo30", "Cargando pedidos: iniciando time");

                //SharedPreferences sharedPreferences = getSharedPreferences("policar.policarappv6", Context.MODE_PRIVATE);
                sharedPreferences2 = getSharedPreferences("policar.policarappv6", Context.MODE_PRIVATE);

                Integer conductorOcupado = sharedPreferences2.getInt("ConductorOcupado", 2);
                MonitoreoEncendido = sharedPreferences2.getBoolean("MonitoreoEncendido", true);
                MonitoreoSonido = sharedPreferences2.getBoolean("MonitoreoSonido", true);

                final boolean Minimizado = sharedPreferences2.getBoolean("Minimizado", false);

                LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

                String GpsActivado = "0";

                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    GpsActivado = "1";
                } else {
                    GpsActivado = "2";
                }

                if (conductorOcupado == 2) {

                    try {

                       /// Log.e("MsgMonitoreo30", "Cargando pedidos: iniciando post");

                        final ArrayList<NavegacionResults> navegacionResults = new ArrayList<NavegacionResults>();

                        final String resObtenerPedidoPendientes;
                        resObtenerPedidoPendientes = MtdObtenerPedidoPendientes(
                                ConductorId,
                                GpsActivado,
                                RegionId,
                                SectorId,
                                EmpresaId,
                                Cobertura,
                                VehiculoCoordenadaX,
                                VehiculoCoordenadaY);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                //Log.e("MsgMonitoreo30", "Cargando pedidos: parseando datos del post");
//
                                String JsRespuesta = "";
                                JSONArray JsDatos;

                                try {

                                    //Log.e("MsgMonitoreo3030", resObtenerPedidoPendientes);

                                    JSONObject jsonObject = new JSONObject(resObtenerPedidoPendientes);
                                    JsRespuesta = jsonObject.getString("Respuesta");
                                    JsDatos = jsonObject.getJSONArray("Datos");

                                  //  Log.e("MsgMonitoreo30", "Cargando pedidos: datos parseados");

                                    for (int i = 0; i < JsDatos.length(); i++) {

                                        JSONObject jsonObj = JsDatos.getJSONObject(i);

                                        String JsPedidoId = jsonObj.getString("PedidoId");
                                        String JsPedidoFecha = jsonObj.getString("PedidoFecha");
                                        String JsPedidoHora = jsonObj.getString("PedidoHora");
                                        String JsPedidoDireccion = jsonObj.getString("PedidoDireccion");
                                        String JsPedidoReferencia = jsonObj.getString("PedidoReferencia");
                                        String JsPedidoDetalle = jsonObj.getString("PedidoDetalle");
                                        String JsPedidoLugarCompra = jsonObj.getString("PedidoLugarCompra");


                                        String JsPedidoCoordenadaX = jsonObj.getString("PedidoCoordenadaX");
                                        String JsPedidoCoordenadaY = jsonObj.getString("PedidoCoordenadaY");
                                        String JsPedidoTipo = jsonObj.getString("PedidoTipo");
                                        String JsPedidoTipoUnidad = jsonObj.getString("PedidoTipoUnidad");
                                        String JsPedidoTipoAccion = jsonObj.getString("PedidoTipoAccion");
                                        String JsPedidoEstado = jsonObj.getString("PedidoEstado");

                                        String JsClienteId = jsonObj.getString("ClienteId");
                                        String JsClienteNombre = jsonObj.getString("ClienteNombre");
                                        String JsClienteCelular = jsonObj.getString("ClienteCelular");
                                        String JsClienteTelefono = jsonObj.getString("ClienteTelefono");
                                        String JsClienteVerificado = jsonObj.getString("ClienteVerificado");

                                        String JsPedidoEsFestivo = jsonObj.getString("PedidoEsFestivo");
                                        String JsPedidoEsNoche = jsonObj.getString("PedidoEsNoche");


                                        NavegacionResults sr1 = new NavegacionResults();

                                        sr1.setPedidoId(JsPedidoId);

                                        sr1.setPedidoFecha(JsPedidoFecha);
                                        sr1.setPedidoHora(JsPedidoHora);

                                        sr1.setPedidoDireccion(JsPedidoDireccion);
                                        sr1.setPedidoReferencia(JsPedidoReferencia);
                                        sr1.setPedidoDetalle(JsPedidoDetalle);
                                        sr1.setPedidoLugarCompra(JsPedidoLugarCompra);

                                        sr1.setPedidoCoordenadaX(JsPedidoCoordenadaX);
                                        sr1.setPedidoCoordenadaY(JsPedidoCoordenadaY);
                                        sr1.setPedidoTipo(JsPedidoTipo);
                                        sr1.setPedidoTipoUnidad(JsPedidoTipoUnidad);
                                        sr1.setPedidoTipoAccion(JsPedidoTipoAccion);
                                        sr1.setPedidoEstado(JsPedidoEstado);

                                        sr1.setClienteId(JsClienteId);
                                        sr1.setClienteNombre(JsClienteNombre);
                                        sr1.setClienteCelular(JsClienteCelular);
                                        sr1.setClienteTelefono(JsClienteTelefono);
                                        sr1.setClienteVerificado(JsClienteVerificado);

                                        sr1.setPedidoEsFestivo(JsPedidoEsFestivo);
                                        sr1.setPedidoEsNoche(JsPedidoEsNoche);

                                        boolean AgregarPedido = false;

                                        if (PedidoRechazados.size() > 0) {
                                            for (int f = 0; f < PedidoRechazados.size(); f++) {
                                                if (PedidoRechazados.get(f).equals(JsPedidoId)) {
                                                    AgregarPedido = false;
                                                    //break outerloop;
                                                    break;
                                                } else {
                                                    AgregarPedido = true;
                                                }
                                            }
                                        } else {
                                            AgregarPedido = true;
                                        }

                                        if (AgregarPedido) {
                                            navegacionResults.add(sr1);
                                        }


                                        PedidoId = jsonObj.getString("PedidoId");
                                        PedidoFecha = jsonObj.getString("PedidoFecha");
                                        PedidoHora = jsonObj.getString("PedidoHora");
                                        PedidoDireccion = jsonObj.getString("PedidoDireccion");
                                        PedidoReferencia = jsonObj.getString("PedidoReferencia");
                                        PedidoDetalle = jsonObj.getString("PedidoDetalle");
                                        PedidoLugarCompra = jsonObj.getString("PedidoLugarCompra");

                                        PedidoCoordenadaX = jsonObj.getString("PedidoCoordenadaX");
                                        PedidoCoordenadaY = jsonObj.getString("PedidoCoordenadaY");
                                        PedidoTipo = jsonObj.getString("PedidoTipo");
                                        PedidoTipoUnidad = jsonObj.getString("PedidoTipoUnidad");
                                        PedidoTipoAccion= jsonObj.getString("PedidoTipoAccion");
                                        PedidoEstado = jsonObj.getString("PedidoEstado");

                                        ClienteId = jsonObj.getString("ClienteId");
                                        ClienteNombre = jsonObj.getString("ClienteNombre");
                                        ClienteCelular = jsonObj.getString("ClienteCelular");
                                        ClienteTelefono = jsonObj.getString("ClienteTelefono");
                                        ClienteVerificado = jsonObj.getString("ClienteVerificado");

                                        PedidoEsFestivo = jsonObj.getString("PedidoEsFestivo");
                                        PedidoEsNoche = jsonObj.getString("PedidoEsNoche");

                                        /*if(!PedidoCoordenadaX.equals("") && !PedidoCoordenadaY.equals("") && !PedidoCoordenadaX.equals("0.00") && !PedidoCoordenadaY.equals("0.00") && PedidoCoordenadaX != null  && PedidoCoordenadaY != null) {

                                            if(!VehiculoCoordenadaX.equals("") && !VehiculoCoordenadaY.equals("") && !VehiculoCoordenadaX.equals("0.00") && !VehiculoCoordenadaY.equals("0.00") && VehiculoCoordenadaX != null  && VehiculoCoordenadaY != null) {

                                                Double PedidoDistancia2 = redondear(calculateDistance(Double.parseDouble(VehiculoCoordenadaX),Double.parseDouble(VehiculoCoordenadaY),Double.parseDouble(PedidoCoordenadaX),Double.parseDouble(PedidoCoordenadaY)),2);
                                                Double PedidoTiempo2 = 0.00;

                                                PedidoTiempo2 = redondear((((PedidoDistancia2)/50)*60),2);

                                                PedidoDistancia = PedidoDistancia2.toString()+" km";
                                                PedidoTiempo = PedidoTiempo2.toString() + " min";


                                            }

                                        }
*/

                                    }

                                    // TextView TxtPedidoDisponible = (TextView) findViewById(R.id.CmpPedidoDisponible);
                                    //TxtPedidoDisponible.setText("" + navegacionResults.size() + "");

                                    final ListView lstPedidoPendientes = (ListView) findViewById(R.id.LstPedidoPendientes);
                                    lstPedidoPendientes.setAdapter(new NavegacionAdapter(NavegacionActivity.this, navegacionResults));

                                    lstPedidoPendientes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> a, View v, int position, long id) {

                                            Object o = lstPedidoPendientes.getItemAtPosition(position);
                                            final NavegacionResults fullObject = (NavegacionResults) o;


                                            //timerNavegacion1.cancel();

                                        /*    android.app.AlertDialog.Builder MsgMonitoreo = new android.app.AlertDialog.Builder(NavegacionActivity.this);
                                            MsgMonitoreo.setTitle(getString(R.string.alert_title));
                                            MsgMonitoreo.setMessage("¿Realmente deseas aceptar el servicio?");
                                            MsgMonitoreo.setCancelable(false);
                                            MsgMonitoreo.setPositiveButton("Si",
                                                    new DialogInterface.OnClickListener() {

                                                        public void onClick(DialogInterface dialog, int which) {
                                                            // Do nothing but close the dialog
                                                            FncMostrarToast("¡Servicio aceptado!");*/

                                            Intent intentPedidoDetalle = new Intent(NavegacionActivity.this, PedidoDetalleActivity.class);
                                            Bundle bundle = new Bundle();

                                            bundle.putString("PedidoId", PedidoId);

                                            bundle.putString("PedidoFecha", PedidoFecha);
                                            bundle.putString("PedidoHora", PedidoHora);
                                            bundle.putString("PedidoDireccion", PedidoDireccion);
                                            bundle.putString("PedidoReferencia", PedidoReferencia);
                                            bundle.putString("PedidoDetalle", PedidoDetalle);
                                            bundle.putString("PedidoLugarCompra", PedidoLugarCompra);

                                            bundle.putString("PedidoCoordenadaX", PedidoCoordenadaX);
                                            bundle.putString("PedidoCoordenadaY", PedidoCoordenadaY);
                                            bundle.putString("PedidoTipo", PedidoTipo);
                                            bundle.putString("PedidoTipoUnidad", PedidoTipoUnidad);
                                            bundle.putString("PedidoTipoAccion", PedidoTipoAccion);
                                            bundle.putString("PedidoEstado", PedidoEstado);

                                            bundle.putString("ClienteId", ClienteId);
                                            bundle.putString("ClienteNombre", ClienteNombre);
                                            bundle.putString("ClienteCelular", ClienteCelular);
                                            bundle.putString("ClienteTelefono", ClienteTelefono);
                                            bundle.putString("ClienteVerificado", ClienteVerificado);

                                            bundle.putString("VehiculoCoordenadaX", VehiculoCoordenadaX);
                                            bundle.putString("VehiculoCoordenadaY", VehiculoCoordenadaY);
                                            bundle.putString("Sonar", "1");

                                            bundle.putString("PedidoEsFestivo", PedidoEsFestivo);
                                            bundle.putString("PedidoEsNoche", PedidoEsNoche);

                                            intentPedidoDetalle.putExtras(bundle);//Put your id to your next Intent
                                            startActivity(intentPedidoDetalle);
                                            finish();//AGREGADO 19-11-15
/*
                                                        }
                                                    });

                                            MsgMonitoreo.setNegativeButton("No",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {


                                                            PedidoRechazados.add(fullObject.getPedidoId());

                                                            FncMostrarToast("¡Servicio rechazado!");


                                                        }
                                                    });
                                            // Remember, create doesn't show the dialog
                                            android.app.AlertDialog msgMonitoreo = MsgMonitoreo.create();
                                            msgMonitoreo.show();
*/


                                        }
                                    });

                                } catch (JSONException e) {
                                    Log.e("NavegacionMsg10", e.toString());
                                    e.printStackTrace();
                                }

                                switch (JsRespuesta) {
                                    case "P007":

                                        if (navegacionResults.size() > 0) {

                                            if (MonitoreoEncendido) {

                                                PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
                                                PowerManager.WakeLock wakeLock = pm.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "TAG");
                                                wakeLock.acquire();

                                                KeyguardManager keyguardManager = (KeyguardManager) getApplicationContext().getSystemService(Context.KEYGUARD_SERVICE);
                                                KeyguardManager.KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("TAG");
                                                keyguardLock.disableKeyguard();

                                            }

                                            if (MonitoreoSonido) {

                                                if (!isPlaying) {
                                                    // MediaPlayer player = MediaPlayer.create(NavegacionActivity.this, R.raw.sou_pedido3);
                                                    //player = MediaPlayer.create(NavegacionActivity.this, R.raw.sou_pedido3);
                                                    //player.start();

                                                    mediaPlayerPedido.start();
                                                }

                                            }

                                            //if(Minimizado){
                                            Log.e("Navegacion500", "MINIMIZADO SI");

                                            //Intent intentNavegacion = new Intent(NavegacionActivity.this, NavegacionActivity.class);
                                            //startActivity(intentNavegacion);
                                            //finish();

//                                                Intent intent = new Intent(context, NavegacionActivity.class);
                                            //                                              intent.setAction("android.intent.actiton.MAIN");
                                            //                                            intent.addCategory("android.intent.category.LAUNCHER");
                                            //                                          intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            //                                        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                            //                                      context.startActivity(intent);
//                                                finish();


                                         /*   Intent intentPedidoDetalle = new Intent(NavegacionActivity.this, PedidoDetalleActivity.class);
                                            Bundle bundle = new Bundle();

                                            bundle.putString("PedidoId", PedidoId);

                                            bundle.putString("PedidoFecha", PedidoFecha);
                                            bundle.putString("PedidoHora", PedidoHora);
                                            bundle.putString("PedidoDireccion", PedidoDireccion);
                                            bundle.putString("PedidoReferencia", PedidoReferencia);
                                            bundle.putString("PedidoDetalle", PedidoDetalle);
                                            bundle.putString("PedidoLugarCompra", PedidoLugarCompra);

                                            bundle.putString("PedidoCoordenadaX", PedidoCoordenadaX);
                                            bundle.putString("PedidoCoordenadaY", PedidoCoordenadaY);
                                            bundle.putString("PedidoTipo", PedidoTipo);
                                            bundle.putString("PedidoTipoUnidad", PedidoTipoUnidad);
                                            bundle.putString("PedidoTipoAccion", PedidoTipoAccion);
                                            bundle.putString("PedidoEstado", PedidoEstado);

                                            bundle.putString("ClienteId", ClienteId);
                                            bundle.putString("ClienteNombre", ClienteNombre);
                                            bundle.putString("ClienteCelular", ClienteCelular);
                                            bundle.putString("ClienteTelefono", ClienteTelefono);
                                            bundle.putString("ClienteVerificado", ClienteVerificado);

                                            bundle.putString("VehiculoCoordenadaX", VehiculoCoordenadaX);
                                            bundle.putString("VehiculoCoordenadaY", VehiculoCoordenadaY);
                                            bundle.putString("Sonar", "1");

                                            intentPedidoDetalle.putExtras(bundle);//Put your id to your next Intent
                                            startActivity(intentPedidoDetalle);
                                            finish();//AGREGADO 19-11-15


                                            sharedPreferences2 = getSharedPreferences("policar.policarappv6", Context.MODE_PRIVATE);
                                            SharedPreferences.Editor editor2 = sharedPreferences2.edit();
                                            editor2.putBoolean("Minimizado", false);
                                            editor2.apply();*/

                                            //FncMostrarMensaje("Pedido entrante",false);

                                            // }else{

                                            //   Log.e("Navegacion500", "MINIMIZADO NO");

//                                            }

                                        }

                                        break;

                                    case "P008":
                                        break;

                                    case "P053":
                                        break;


                                    default:
                                        break;
                                }


                            }
                        });


                    } catch (Exception e) {
                        // TODO: handle exception
                        Log.e("MsgMonitoreo9", e.toString());
                    }

                }


            }
            //}, 1000, AplicacionMonitoreoAlertaContador);
        }, 1000, 3000);





        if(timerNavegacion2!=null){
            timerNavegacion2.cancel();
        }

        //TAREA ENVIAR COORDENADAS - INICIO

        //final Timer timer3 = new Timer();
        timerNavegacion2 = new Timer();
        timerNavegacion2.schedule(new TimerTask(){
            @Override
            public void run() {

                SharedPreferences sharedPreferences = getSharedPreferences("policar.policarappv6", Context.MODE_PRIVATE);
                Integer conductorOcupado = sharedPreferences.getInt("ConductorOcupado", 2);

                if(conductorOcupado == 2){

                    // if(estaConectado(false)){

                    try {
                        //private String VehiculoDireccionActual = "";
                        final String resActualizarConductorCoordenada;
                        resActualizarConductorCoordenada = MtdActualizarConductorCoordenada("",ConductorId,ConductorNombre,ConductorNumeroDocumento, VehiculoCoordenadaX, VehiculoCoordenadaY,VehiculoUnidad,VehiculoPlaca);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                String JsRespuesta = "";
                                String JsSectorNumero = "";
                                String JsClienteId = "";
                                String JsRegionNombre = "";
                                String JsRegionId = "";
                                String JsSectorNombre = "";
                                String JsSectorId = "";

                                try {

                                    JSONObject jsonObject = new JSONObject(resActualizarConductorCoordenada);
                                    JsRespuesta = jsonObject.getString("Respuesta");
                                    JsSectorNumero = jsonObject.getString("SectorNumero");

                                    JsRegionNombre = jsonObject.getString("RegionNombre");
                                    JsRegionId = jsonObject.getString("RegionId");

                                    JsSectorNombre = jsonObject.getString("SectorNombre");
                                    JsSectorId = jsonObject.getString("SectorId");

                                } catch (JSONException e) {
                                    Log.e("Navegacion7", e.toString());
                                    e.printStackTrace();
                                }

                                switch(JsRespuesta){

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

                                        //editor.putString("RegionId", JsRegionId);
//                                        editor.putString("RegionNombre", JsRegionNombre);

  //                                      editor.putString("SectorId", JsSectorId);
    //                                    editor.putString("SectorNombre", JsSectorNombre);
      //                                  editor.apply();

                                   /*     PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
                                        PowerManager.WakeLock wakeLock = pm.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "TAG");
                                        wakeLock.acquire();

                                        KeyguardManager keyguardManager = (KeyguardManager) getApplicationContext().getSystemService(Context.KEYGUARD_SERVICE);
                                        KeyguardManager.KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("TAG");
                                        keyguardLock.disableKeyguard();*/

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



                    } catch (Exception e) {
                        // TODO: handle exception
                        Log.e("Navegacion8", e.toString());
                    }

                    // }



                }



            }
        }, 1000, 3500);



        //}, 3000, 30000);


        if (timerNavegacion3 != null) {
            timerNavegacion3.cancel();
        }
        //final Timer timer3 = new Timer();
        timerNavegacion3 = new Timer();
        timerNavegacion3.schedule(new TimerTask() {
            @Override
            public void run() {

                SharedPreferences sharedPreferences = getSharedPreferences("policar.policarappv6", Context.MODE_PRIVATE);
                Integer conductorOcupado = sharedPreferences.getInt("ConductorOcupado", 2);

                // if(conductorOcupado == 2){

                // if(estaConectado(false)){

                try {

                    final String resObtenerPedidoAsignado;
                    resObtenerPedidoAsignado = MtdObtenerPedidoAsignado(ConductorId, VehiculoCoordenadaX, VehiculoCoordenadaY);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            String JsRespuesta = "";
                            String JsPedidoId = "";
                            String JsPedidoFecha = "";
                            String JsPedidoHora = "";
                            String JsPedidoDireccion = "";
                            String JsPedidoReferencia = "";
                            String JsPedidoDetalle = "";
                            String JsPedidoLugarCompra = "";

                            String JsPedidoCoordenadaX = "";
                            String JsPedidoCoordenadaY = "";
                            String JsPedidoTipo = "";
                            String JsPedidoTipoUnidad = "";
                            String JsPedidoTipoAccion = "";
                            String JsPedidoEstado = "";

                            String JsClienteNombre = "";
                            String JsClienteCelular = "";
                            String JsClienteTelefono = "";
                            String JsClienteVerificado = "";

                            try {
                                JSONObject jsonObject = new JSONObject(resObtenerPedidoAsignado);
                                JsRespuesta = jsonObject.getString("Respuesta");

                                JsPedidoId = jsonObject.getString("PedidoId");
                                JsPedidoFecha = jsonObject.getString("PedidoFecha");
                                JsPedidoHora = jsonObject.getString("PedidoHora");
                                JsPedidoDireccion = jsonObject.getString("PedidoDireccion");
                                JsPedidoReferencia = jsonObject.getString("PedidoReferencia");
                                JsPedidoDetalle = jsonObject.getString("PedidoDetalle");
                                JsPedidoLugarCompra = jsonObject.getString("PedidoLugarCompra");

                                JsPedidoCoordenadaX = jsonObject.getString("PedidoCoordenadaX");
                                JsPedidoCoordenadaY = jsonObject.getString("PedidoCoordenadaY");
                                JsPedidoTipo = jsonObject.getString("PedidoTipo");
                                JsPedidoTipoUnidad = jsonObject.getString("PedidoTipoUnidad");
                                JsPedidoTipoAccion = jsonObject.getString("PedidoTipoAccion");
                                JsPedidoEstado = jsonObject.getString("PedidoEstado");

                                JsClienteNombre = jsonObject.getString("ClienteNombre");
                                JsClienteCelular = jsonObject.getString("ClienteCelular");
                                JsClienteTelefono = jsonObject.getString("ClienteTelefono");
                                JsClienteVerificado = jsonObject.getString("ClienteVerificado");

                            } catch (JSONException e) {
                                Log.e("Navegacion30", e.toString());
                                e.printStackTrace();
                            }

                            switch (JsRespuesta) {

                                case "P074":


                                    if(!JsPedidoCoordenadaX.equals("") && !JsPedidoCoordenadaY.equals("") && !JsPedidoCoordenadaX.equals("0.00") && !JsPedidoCoordenadaY.equals("0.00") && JsPedidoCoordenadaX != null  && JsPedidoCoordenadaY != null) {

                                        if(!VehiculoCoordenadaX.equals("") && !VehiculoCoordenadaY.equals("") && !VehiculoCoordenadaX.equals("0.00") && !VehiculoCoordenadaY.equals("0.00") && VehiculoCoordenadaX != null  && VehiculoCoordenadaY != null) {

                                            Double PedidoDistancia2 = redondear(calculateDistance(Double.parseDouble(VehiculoCoordenadaX),Double.parseDouble(VehiculoCoordenadaY),Double.parseDouble(JsPedidoCoordenadaX),Double.parseDouble(JsPedidoCoordenadaY)),2);
                                            Double PedidoTiempo2 = 0.00;

                                            PedidoTiempo2 = redondear((((PedidoDistancia2)/50)*60),2);

                                            PedidoDistancia = PedidoDistancia2.toString()+" km";
                                            PedidoTiempo = PedidoTiempo2.toString() + " min";

                                        }

                                    }


/*
                                        Intent intentPedidoDetalle = new Intent(NavegacionActivity.this, PedidoDetalleActivity.class);
                                        Bundle bundle = new Bundle();

                                        bundle.putString("PedidoId", JsPedidoId);

                                        bundle.putString("PedidoFecha", JsPedidoFecha);
                                        bundle.putString("PedidoHora", JsPedidoHora);
                                        bundle.putString("PedidoDireccion", JsPedidoDireccion);
                                        bundle.putString("PedidoReferencia", JsPedidoReferencia);
                                        bundle.putString("PedidoCoordenadaX", JsPedidoCoordenadaX);
                                        bundle.putString("PedidoCoordenadaY", JsPedidoCoordenadaY);
                                        bundle.putString("PedidoTipo", JsPedidoTipo);
                                        bundle.putString("PedidoTipoUnidad", JsPedidoTipoUnidad);
                                        bundle.putString("PedidoEstado", JsPedidoEstado);

                                        bundle.putString("ClienteNombre", JsClienteNombre);
                                        bundle.putString("ClienteCelular", JsClienteCelular);
                                        bundle.putString("ClienteTelefono", JsClienteTelefono);
                                        bundle.putString("ClienteVerificado", JsClienteVerificado);

                                        bundle.putString("VehiculoCoordenadaX", VehiculoCoordenadaX);
                                        bundle.putString("VehiculoCoordenadaY", VehiculoCoordenadaY);

                                        intentPedidoDetalle.putExtras(bundle);//Put your id to your next Intent
                                        startActivity(intentPedidoDetalle);
                                        finish();//AG
*/

                                    SharedPreferences sharedPreferences = getSharedPreferences("policar.policarappv6", Context.MODE_PRIVATE);

                                    SharedPreferences.Editor editor = sharedPreferences.edit();

                                    editor.putString("PedidoId", JsPedidoId);

                                    editor.putString("ClienteNombre", JsClienteNombre);
                                    editor.putString("ClienteCelular", JsClienteCelular);
                                    editor.putString("ClienteTelefono", JsClienteTelefono);

                                    editor.putString("PedidoDireccion", JsPedidoDireccion);
                                    editor.putString("PedidoReferencia", JsPedidoReferencia);
                                    editor.putString("PedidoDetalle", JsPedidoDetalle);
                                    editor.putString("PedidoLugarCompra", JsPedidoLugarCompra);

                                    editor.putString("PedidoEstado", JsPedidoEstado);
                                    editor.putString("PedidoTipo", JsPedidoTipo);
                                    editor.putString("PedidoTipoUnidad", JsPedidoTipoUnidad);
                                    editor.putString("PedidoTipoAccion", JsPedidoTipoAccion);
                                    editor.putString("PedidoTiempo", PedidoTiempo);
                                    editor.putString("PedidoDistancia", PedidoDistancia);

                                    editor.putString("PedidoCoordenadaX", JsPedidoCoordenadaX);
                                    editor.putString("PedidoCoordenadaY", JsPedidoCoordenadaY);

                                    editor.putString("VehiculoCoordenadaX", VehiculoCoordenadaX.trim());
                                    editor.putString("VehiculoCoordenadaY", VehiculoCoordenadaY.trim());

                                    editor.putInt("ConductorOcupado", 111);
                                    editor.putBoolean("ConductorTienePedido", true);
                                    editor.apply();


                                    Intent intentPedidoActual = new Intent(NavegacionActivity.this, PedidoActualActivity.class);
                                    Bundle bundlePedidoActual = new Bundle();

                                    bundlePedidoActual.putString("PedidoId", JsPedidoId);

                                    bundlePedidoActual.putString("ClienteNombre", JsClienteNombre);
                                    bundlePedidoActual.putString("ClienteCelular", JsClienteCelular);
                                    bundlePedidoActual.putString("ClienteTelefono", JsClienteTelefono);

                                    bundlePedidoActual.putString("PedidoDireccion", JsPedidoDireccion);
                                    bundlePedidoActual.putString("PedidoReferencia", JsPedidoReferencia);
                                    bundlePedidoActual.putString("PedidoDetalle", JsPedidoDetalle);
                                    bundlePedidoActual.putString("PedidoLugarCompra", JsPedidoLugarCompra);
                                    bundlePedidoActual.putString("PedidoEstado", JsPedidoEstado);

                                    bundlePedidoActual.putString("PedidoTipo", JsPedidoTipo);
                                    bundlePedidoActual.putString("PedidoTipoUnidad", JsPedidoTipoUnidad);
                                    bundlePedidoActual.putString("PedidoTipoAccion", JsPedidoTipoAccion);

                                    bundlePedidoActual.putString("PedidoTiempo", PedidoTiempo);
                                    bundlePedidoActual.putString("PedidoDistancia", PedidoDistancia);

                                    bundlePedidoActual.putString("PedidoCoordenadaX", JsPedidoCoordenadaX);
                                    bundlePedidoActual.putString("PedidoCoordenadaY", JsPedidoCoordenadaY);

                                    bundlePedidoActual.putString("VehiculoCoordenadaX", VehiculoCoordenadaX);
                                    bundlePedidoActual.putString("VehiculoCoordenadaY", VehiculoCoordenadaY);

                                    intentPedidoActual.putExtras(bundlePedidoActual);//Put your id to your next Intent
                                    startActivity(intentPedidoActual);
                                    NavegacionActivity.this.overridePendingTransition(R.anim.anim_slide_in_left,
                                            R.anim.anim_slide_out_left);
                                    finish();


                                    break;

                                case "P075":
                                    break;

                                case "P076":
                                    break;

                                case "P104":
                                    break;

                                default:
                                    break;

                            }

                        }
                    });


                } catch (Exception e) {
                    // TODO: handle exception
                    Log.e("Navegacion8", e.toString());
                }

                // }


                //}


            }
        }, 1000, 7000);
        //}, 3000, 30000);







       // final AlertDialog.Builder MsgNavegacion = new AlertDialog.Builder(NavegacionActivity.this);


        if (timerNavegacion5 != null) {
            timerNavegacion5.cancel();
        }
        //final Timer timer3 = new Timer();
        timerNavegacion5 = new Timer();
        timerNavegacion5.schedule(new TimerTask() {
            @Override
            public void run() {

                try {

                    final String resMtdObtenerConductorMensajeUltimo;
                    resMtdObtenerConductorMensajeUltimo = MtdObtenerConductorMensajeUltimo(ConductorId,"1","1");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            String JsRespuesta = "";
                            String JsConductorMensajeId = "";
                            String JsConductorMensajeTitulo = "";
                            String JsConductorMensajeDescripcion= "";

                            try {
                                JSONObject jsonObject = new JSONObject(resMtdObtenerConductorMensajeUltimo);
                                JsRespuesta = jsonObject.getString("Respuesta");

                                JsConductorMensajeId = jsonObject.getString("ConductorMensajeId");
                                JsConductorMensajeTitulo = jsonObject.getString("ConductorMensajeTitulo");
                                JsConductorMensajeDescripcion = jsonObject.getString("ConductorMensajeDescripcion");


                            } catch (JSONException e) {
                                Log.e("Navegacion50", e.toString());
                                e.printStackTrace();
                            }

                            switch (JsRespuesta) {

                                case "A001":

                                    //HABILITAR AUDIO MAS ADELANTE
                                    //MediaPlayer player = MediaPlayer.create(NavegacionActivity.this, R.raw.sou_mensaje);
                                    //player = MediaPlayer.create(NavegacionActivity.this, R.raw.sou_mensaje);
                                   // player.start();

                                    mediaPlayerMensaje.start();

                                    AlertDialog.Builder MsgNavegacion = new AlertDialog.Builder(NavegacionActivity.this);
                                    MsgNavegacion.setTitle(JsConductorMensajeTitulo);
                                    MsgNavegacion.setMessage(JsConductorMensajeDescripcion);
                                    MsgNavegacion.setCancelable(false);
                                    MsgNavegacion.setPositiveButton("Aceptar",
                                            new DialogInterface.OnClickListener() {

                                                public void onClick(DialogInterface dialog, int which) {
                                                    // Do nothing but close the dialog

                                                }
                                            });

                                    // Remember, create doesn't show the dialog
                                    AlertDialog msgNavegacion = MsgNavegacion.create();
                                    msgNavegacion.show();

                                    break;

                                case "A002":


                                    break;

                                default:
                                    break;

                            }

                        }
                    });


                } catch (Exception e) {
                    // TODO: handle exception
                    Log.e("Navegacion8", e.toString());
                }

                // }


                //}


            }
        }, 1000, 9000);
        //}, 3000, 30000);









        if(timerNavegacion4!=null){
            timerNavegacion4.cancel();
        }

        timerNavegacion4 = new Timer();
        timerNavegacion4.schedule(new TimerTask(){
            @Override
            public void run() {

                //if(!isRecording){

                    if(!isPlaying){


                    Thread nt = new Thread() {
                        @Override
                        public void run() {

                            //if(!mediaPlayer.isPlaying()){
                            try {

                                final String resObtenerRadioMensajes;
                                resObtenerRadioMensajes = MtdObtenerRadioMensajes(ConductorId,ConductorCanal);

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        String JsRespuesta = "";
                                        JSONArray JsDatos;

                                        try {

                                            JSONObject jsonObject = new JSONObject(resObtenerRadioMensajes);
                                            JsRespuesta = jsonObject.getString("Respuesta");
                                            JsDatos = jsonObject.getJSONArray("Datos");

                                            for (int i = 0; i < JsDatos.length(); i++) {

                                                JSONObject jsonObj = JsDatos.getJSONObject(i);
                                                String JsRadioMensajeId = jsonObj.getString("RadioMensajeId");
                                                String JsRadioMensajeArchivo = jsonObj.getString("RadioMensajeArchivo");
                                                String JsConductorVehiculoUnidadOrigen = jsonObj.getString("ConductorVehiculoUnidadOrigen");

                                                switch(JsRespuesta){

                                                    case "R006":
                                                        // Log.e("Navegacion70","AAA")
                                                        if(RadioMensajeRecibidos.size()>0){
                                                            for (int f = 0; f < RadioMensajeRecibidos.size(); f++) {
                                                                if(RadioMensajeRecibidos.get(f).equals(JsRadioMensajeId)){

                                                                    RadioMensajeReproducir = false;
                                                                    //break outerloop;
                                                                    break;
                                                                }else{
                                                                    RadioMensajeReproducir = true;
                                                                }
                                                            }
                                                        }else{
                                                            RadioMensajeReproducir = true;
                                                        }


                                                        if(RadioMensajeReproducir){


                                                            FncMostrarToast(JsConductorVehiculoUnidadOrigen);
                                                            Log.e("Navegacion70","Reproducir SI...");

                                                            RadioMensajeRecibidos.add(JsRadioMensajeId);

                                                            fab1.setVisibility(View.GONE);
                                                            fab2.setVisibility(View.GONE);
                                                            fab3.setVisibility(View.VISIBLE);

                                                            isPlaying = true;
                                                            //mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                                                            try {

                                                                // int maxVolume = 50;
                                                                //float log1=(float)(Math.log(maxVolume-currVolume)/Math.log(maxVolume));
                                                                // yourMediaPlayer.setVolume(1-log1);

                                                                //  recordButton = (ImageButton) findViewById(R.id.BtnNavegacionIniciarRadio);
                                                                // recordButton.setBackgroundResource(R.drawable.dra_boton_radio3);




                                                                //AudioManager amanager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                                                                //int maxVolume = amanager.getStreamMaxVolume(AudioManager.STREAM_ALARM);
                                                                //amanager.setStreamVolume(AudioManager.STREAM_ALARM, maxVolume, 0);



                                                                //AudioManager mAudioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
                                                                //mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume * mLastProgress / 10, 0);
                                                               // int maxVolume = 50;
                                                                //float log1=(float)(Math.log(maxVolume-2)/Math.log(maxVolume));
                                                                //yourMediaPlayer.setVolume(1-log1);

                                                                mediaPlayer = new MediaPlayer();
                                                                mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM); // this is important.
                                                                mediaPlayer.setDataSource(JsRadioMensajeArchivo);
                                                                //mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);


                                                                // mediaPlayer.setVolume(30,00);
                                                                mediaPlayer.prepare();
                                                                mediaPlayer.setLooping(false);
                                                                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                                                                    @Override
                                                                    public void onCompletion(MediaPlayer mp) {

                                                                        //recordButton = (ImageButton) findViewById(R.id.BtnNavegacionIniciarRadio);
                                                                        //recordButton.setBackgroundResource(R.drawable.dra_boton_radio);
                                                                        fab1.setVisibility(View.VISIBLE);
                                                                        fab2.setVisibility(View.GONE);
                                                                        fab3.setVisibility(View.GONE);

                                                                        isPlaying = false;
                                                                    }

                                                                });
                                                                mediaPlayer.setVolume(1.0f, 1.0f);
                                                                //mediaPlayer.setVolume(1000.99f, 1000.99f);
                                                                //mediaPlayer.setVolume(1-log1,1-log1);

                                                                mediaPlayer.start();

                                                                //MonitoreoSonar = true;
                                                                PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
                                                                PowerManager.WakeLock wakeLock = pm.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "TAG");
                                                                wakeLock.acquire();

                                                                // Here, thisActivity is the current activity
                                                                KeyguardManager keyguardManager = (KeyguardManager) getApplicationContext().getSystemService(Context.KEYGUARD_SERVICE);
                                                                KeyguardManager.KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("TAG");
                                                                keyguardLock.disableKeyguard();

                                                            } catch (IOException e) {
                                                                e.printStackTrace();

                                                                fab1.setVisibility(View.VISIBLE);
                                                                fab2.setVisibility(View.GONE);
                                                                fab3.setVisibility(View.GONE);

                                                                isPlaying = false;
                                                            }

                                                            //String in = new java.net.URL(JsRadioMensajeArchivo).toString();
                                                            //Log.e("Navegacion70",JsRadioMensajeArchivo);


                                                        }else{
                                                            Log.e("Navegacion70","Reproducir NO...");

                                                        }

                                                        break;

                                                    case "R007":
                                                        break;

                                                    default:
                                                        break;

                                                }

                                            }

                                        } catch (JSONException e) {
                                            Log.e("Navegacion66", e.toString());
                                            e.printStackTrace();
                                        }

                                    }
                                });



                            } catch (Exception e) {
                                // TODO: handle exception
                                Log.e("Navegacion67", e.toString());
                            }
                            //}else{
                            //     Log.e("Navegacion69","Reproduccion ya en curso");

                            //  }



                        }

                    };
                    nt.start();


                     }


                //}



            }

        }, 1000, 2200);




        //TAREA OBTENER  ALERTAS
        if(timerNavegacion6!=null){
            timerNavegacion6.cancel();
        }

        //final Timer timer2 = new Timer();
        timerNavegacion6 = new Timer();
        timerNavegacion6.schedule(new TimerTask(){
            @Override
            public void run() {

                SharedPreferences sharedPreferences = getSharedPreferences("policar.policarappv6", Context.MODE_PRIVATE);
                Integer conductorOcupado = sharedPreferences.getInt("ConductorOcupado", 2);

                if(conductorOcupado == 2){

                    if(estaConectado(false)){


                        try {

                            final String resConductorAlertas;
                            resConductorAlertas = MtdObtenerConductorAlertas();

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    String JsRespuesta = "";
                                    JSONArray JsDatos;

                                    //if(googleMap != null){

                                    try {

                                        JSONObject jsonObject = new JSONObject(resConductorAlertas);
                                        JsRespuesta = jsonObject.getString("Respuesta");
                                        JsDatos = jsonObject.getJSONArray("Datos");

                                        for (int i = 0; i < JsDatos.length(); i++) {

                                            JSONObject jsonObj = JsDatos.getJSONObject(i);

                                            String JsConductorAlertaTiempoTranscurrido = jsonObj.getString("ConductorAlertaTiempoTranscurrido");
                                            String JsConductorAlertaTipo = jsonObj.getString("ConductorAlertaTipo");
                                            String JsConductorAlertaCoordenadaX = jsonObj.getString("ConductorAlertaCoordenadaX");
                                            String JsConductorAlertaCoordenadaY = jsonObj.getString("ConductorAlertaCoordenadaY");


                                            if(JsConductorAlertaTiempoTranscurrido.equals("0")){

                                                ConductorAlertaCoordenadaX = JsConductorAlertaCoordenadaX;
                                                ConductorAlertaCoordenadaY = JsConductorAlertaCoordenadaY;

                                                menuAlerta.setVisible(false);
                                                menuVerAlerta.setVisible(true);

                                                mediaPlayerEmergencia.start();

                                            }else{

                                                menuAlerta.setVisible(true);
                                                menuVerAlerta.setVisible(false);

                                                ConductorAlertaCoordenadaX = "";
                                                ConductorAlertaCoordenadaY = "";

                                            }



                                        }


                                    } catch (JSONException e) {
                                        Log.e("MsgRadar4", e.toString());
                                        e.printStackTrace();
                                    }

                                    switch(JsRespuesta){

                                        case "A001":
                                            break;

                                        case "A002":

                                            menuAlerta.setVisible(true);
                                            menuVerAlerta.setVisible(false);

                                            ConductorAlertaCoordenadaX = "";
                                            ConductorAlertaCoordenadaY = "";

                                            break;

                                        default:
                                            break;

                                    }


                                    // }



                                }
                            });

                        } catch (Exception e) {
                            // TODO: handle exception
                            Log.e("MsgRadar3", e.toString());
                        }

                    }




                }

            }
        }, 5000, 7000);
        //}, 2000, 59000);


/*
        final ArrayList<RadioMensaje> radioMensaje = new ArrayList<RadioMensaje>();


        if (timerNavegacion4 != null) {
            timerNavegacion4.cancel();
        }

        timerNavegacion4 = new Timer();
        timerNavegacion4.schedule(new TimerTask() {
            @Override
            public void run() {

                if (!isRecording) {

                    //if(!isPlaying){


                    Thread nt = new Thread() {
                        @Override
                        public void run() {

                            //if(!mediaPlayer.isPlaying()){
                            try {

                                final String resObtenerRadioMensajes;
                                resObtenerRadioMensajes = MtdObtenerRadioMensajes(ConductorId, ConductorCanal);

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        String JsRespuesta = "";
                                        JSONArray JsDatos;

                                        try {

                                            JSONObject jsonObject = new JSONObject(resObtenerRadioMensajes);
                                            JsRespuesta = jsonObject.getString("Respuesta");
                                            JsDatos = jsonObject.getJSONArray("Datos");

                                            for (int i = 0; i < JsDatos.length(); i++) {

                                                JSONObject jsonObj = JsDatos.getJSONObject(i);
                                                String JsRadioMensajeId = jsonObj.getString("RadioMensajeId");
                                                String JsRadioMensajeArchivo = jsonObj.getString("RadioMensajeArchivo");
                                                String JsConductorVehiculoUnidadOrigen = jsonObj.getString("ConductorVehiculoUnidadOrigen");

                                                switch (JsRespuesta) {

                                                    case "R006":

                                                        RadioMensaje radioMensaje1 = new RadioMensaje();

                                                        radioMensaje1.setRmeId(JsRadioMensajeId);
                                                        radioMensaje1.setRmeArchivo(JsRadioMensajeArchivo);
                                                        radioMensaje.add(radioMensaje1);

                                                        Log.e("JsRadioMensajeId", JsRadioMensajeId);
                                                        Log.e("JsRadioMensajeArchivo", JsRadioMensajeArchivo);

                                                        break;

                                                    case "R007":
                                                        break;

                                                    default:
                                                        break;

                                                }

                                            }

                                        } catch (JSONException e) {
                                            Log.e("Navegacion66", e.toString());
                                            e.printStackTrace();
                                        }

                                    }
                                });


                            } catch (Exception e) {
                                // TODO: handle exception
                                Log.e("Navegacion67", e.toString());
                            }
                            //}else{
                            //     Log.e("Navegacion69","Reproduccion ya en curso");

                            //  }


                        }

                    };
                    nt.start();


                    // }


                }


            }

        }, 1000, 2000);


*/



/*
for(int i=0;i<=radioMensaje.size();i++){
    radioMensaje[i].
}        ;
*/




/*

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setDataSource("");
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        // mediaPlayer.setVolume(30,00);
        mediaPlayer.prepare();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {


            }

        });

        mediaPlayer.setVolume(1.0f, 1.0f);
        mediaPlayer.start();
*/


        fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab3 = (FloatingActionButton) findViewById(R.id.fab3);
        fab4 = (FloatingActionButton) findViewById(R.id.fab4);

        fab41 = (FloatingActionButton) findViewById(R.id.fab41);
        fab42 = (FloatingActionButton) findViewById(R.id.fab42);
        fab43 = (FloatingActionButton) findViewById(R.id.fab43);
        fab44 = (FloatingActionButton) findViewById(R.id.fab44);
        fab45 = (FloatingActionButton) findViewById(R.id.fab45);

        fab6 = (FloatingActionButton) findViewById(R.id.fab6);
        fabRadio = (FloatingActionButton) findViewById(R.id.FabRadio);

        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        // rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_forward);
        //  rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_backward);

        fab1.setOnClickListener(this);
        fab2.setOnClickListener(this);
        fab3.setOnClickListener(this);
        fab4.setOnClickListener(this);

        fab41.setOnClickListener(this);
        fab42.setOnClickListener(this);
        fab43.setOnClickListener(this);
        fab44.setOnClickListener(this);
        fab45.setOnClickListener(this);

        fab6.setOnClickListener(this);
        fabRadio.setOnClickListener(this);

        switch (ConductorCanal) {
            case "1":
                fab4.setVisibility(View.GONE);
                fab41.setVisibility(View.VISIBLE);
                fab42.setVisibility(View.GONE);
                fab43.setVisibility(View.GONE);
                fab44.setVisibility(View.GONE);
                fab45.setVisibility(View.GONE);

                break;

            case "2":
                fab4.setVisibility(View.GONE);
                fab41.setVisibility(View.GONE);
                fab42.setVisibility(View.VISIBLE);
                fab43.setVisibility(View.GONE);
                fab44.setVisibility(View.GONE);
                fab45.setVisibility(View.GONE);
                break;

            case "3":
                fab4.setVisibility(View.GONE);
                fab41.setVisibility(View.GONE);
                fab42.setVisibility(View.GONE);
                fab43.setVisibility(View.VISIBLE);
                fab44.setVisibility(View.GONE);
                fab45.setVisibility(View.GONE);
                break;

            case "4":
                fab4.setVisibility(View.GONE);
                fab41.setVisibility(View.GONE);
                fab42.setVisibility(View.GONE);
                fab43.setVisibility(View.GONE);
                fab44.setVisibility(View.VISIBLE);
                fab45.setVisibility(View.GONE);
                break;

            case "5":
                fab4.setVisibility(View.GONE);
                fab41.setVisibility(View.GONE);
                fab42.setVisibility(View.GONE);
                fab43.setVisibility(View.GONE);
                fab44.setVisibility(View.GONE);
                fab45.setVisibility(View.VISIBLE);
                break;

            default:
                fab4.setVisibility(View.VISIBLE);
                fab41.setVisibility(View.GONE);
                fab42.setVisibility(View.GONE);
                fab43.setVisibility(View.GONE);
                fab44.setVisibility(View.GONE);
                fab45.setVisibility(View.GONE);
                break;
        }


        //  File tmp = new File( Environment.getExternalStorageDirectory().getAbsolutePath()+"/radio/", "lol.mp3");

        mAudioPlayHelper = new AudioPlayHelper<>(new AudioPlayHelper.RecordPlayListener<Object>() {
            @Override
            public void onPlayStart(Object o) {
                Log.e("mAudioPlayHelper", "onPlayStart");
            }

            @Override
            public void onPlayStop(Object o) {
                Log.e("mAudioPlayHelper", "onPlayStop");
            }

            @Override
            public void onPlayError(Object o) {
                ///showStatus("Play error!!!");
                Log.e("mAudioPlayHelper", "onPlayError");
            }
        });

       /* mAudioRecordHelper = new AudioRecordHelper(tmp, new AudioRecordHelper.RecordCallback() {
            @Override
            public void onRecordStart() {
                //showStatus("Recording, release to play!");
            }

            @Override
            public void onProgress(long time) {

            }

            @SuppressWarnings("unchecked")
            @Override
            public void onRecordDone(final File file, final long time) {
                //showStatus("Record done!\n\rFile size:" + file.length() + "B Time:" + time + "ms");
                //mAudioPlayHelper.trigger(NavegacionActivity.this, file.getAbsolutePath());
            }
        });
*/

        // SharedPreferences sharedPreferences2 = this.getSharedPreferences("com.websmithing.gpstracker.prefs", Context.MODE_PRIVATE);
        currentlyTracking = sharedPreferences2.getBoolean("currentlyTracking", false);

        // boolean firstTimeLoadingApp = sharedPreferences2.getBoolean("firstTimeLoadingApp", true);

        //  if (firstTimeLoadingApp) {
        //      SharedPreferences.Editor editor = sharedPreferences2.edit();
        //       editor.putBoolean("firstTimeLoadingApp", false);
        //        editor.putString("appID",  UUID.randomUUID().toString());
        //          editor.apply();
//        }

        if (checkIfGooglePlayEnabled()) {
            // currentlyTracking = true;
            SharedPreferences.Editor editor = sharedPreferences2.edit();
            editor.putBoolean("currentlyTracking", true);
            //editor.putFloat("totalDistanceInMeters", 0f);
            //editor.putBoolean("firstTimeGettingPosition", true);
            // editor.putString("sessionID",  UUID.randomUUID().toString());
            editor.apply();

            Log.e("Main10", "Google Play Activado");

            if (currentlyTracking) {
                cancelAlarmManager();
            }

            startAlarmManager();

        }

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    private void onStartRecord() {
        mAudioPlayHelper.stop();
        mAudioRecordHelper.recordAsync();
    }

    private void onStopRecord() {
        mAudioRecordHelper.stop(false);
    }

    private void onCancelRecord() {
        mAudioRecordHelper.stop(true);
    }

    private boolean checkIfGooglePlayEnabled() {
        if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(this) == ConnectionResult.SUCCESS) {
            return true;
        } else {
            //Log.e(TAG, "unable to connect to google play services.");
            Log.e("Main10", "unable to connect to google play services.");
            Toast.makeText(getApplicationContext(), "No se encontro Google Play Services instalado", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    private void startAlarmManager() {
//        Log.d(TAG, "startAlarmManager");
        Log.e("Main10", "startAlarmManager");

        Context context = getBaseContext();
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        gpsTrackerIntent = new Intent(context, GpsTrackerAlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(context, 0, gpsTrackerIntent, 0);

        SharedPreferences sharedPreferences = this.getSharedPreferences("policar.policarappv6", Context.MODE_PRIVATE);
        intervalInMinutes = sharedPreferences.getInt("intervalInMinutes", 3000);

        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime(),
                intervalInMinutes, // 60000 = 1 minute
                pendingIntent);
    }

    private void cancelAlarmManager() {
//        Log.d(TAG, "cancelAlarmManager");
        Log.e("Main10", "cancelAlarmManager");

        Context context = getBaseContext();
        Intent gpsTrackerIntent = new Intent(context, GpsTrackerAlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, gpsTrackerIntent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);

    }

    private boolean checkPermission(int permiso) {
        Log.e("Navegacion10", "VERIFICAR PERMISO");
        boolean respuesta = false;

        int MyVersion = Build.VERSION.SDK_INT;

        if (MyVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {

            switch (permiso) {
                case 1:

                    int result1 = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);

                    if (result1 == PackageManager.PERMISSION_GRANTED) {
                        Log.e("Navegacion10", "1AAA");
                        respuesta = true;
                    } else {
                        respuesta = false;
                        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, permiso);
                        Log.e("Navegacion10", "1BBB");
                    }

                    break;

                case 2:

                    int result2 = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION);

                    if (result2 == PackageManager.PERMISSION_GRANTED) {
                        Log.e("Navegacion10", "2AAA");
                        respuesta = true;
                    } else {
                        respuesta = false;
                        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, permiso);
                        Log.e("Navegacion10", "2BBB");
                    }

                    break;

                case 3:

                    int result3 = ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO);

                    if (result3 == PackageManager.PERMISSION_GRANTED) {
                        Log.e("Navegacion10", "3AAA");
                        respuesta = true;
                    } else {
                        respuesta = false;
                        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.RECORD_AUDIO}, permiso);
                        Log.e("Navegacion10", "3BBB");
                    }

                    break;

                case 4:

                    int result4 = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);

                    if (result4 == PackageManager.PERMISSION_GRANTED) {
                        Log.e("Navegacion10", "4AAA");
                        respuesta = true;
                    } else {
                        respuesta = false;
                        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, permiso);
                        Log.e("Navegacion10", "4BBB");
                    }

                    break;

                case 5:

                    int result5 = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE);

                    if (result5 == PackageManager.PERMISSION_GRANTED) {
                        Log.e("Navegacion10", "5AAA");
                        respuesta = true;
                    } else {
                        respuesta = false;
                        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, permiso);
                        Log.e("Navegacion10", "5BBB");
                    }

                    break;

                case 6:

                    int result6 = ContextCompat.checkSelfPermission(context, Manifest.permission.VIBRATE);

                    if (result6 == PackageManager.PERMISSION_GRANTED) {
                        Log.e("Navegacion10", "5AAA");
                        respuesta = true;
                    } else {
                        respuesta = false;
                        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.VIBRATE}, permiso);
                        Log.e("Navegacion10", "5BBB");
                    }

                    break;
            }

        } else {
            respuesta = true;
        }


        return respuesta;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            Log.e("Navegacion10", "PERMISO ACEPTADO");

            switch (requestCode) {
                case 1:
                    createMapView();
                    break;

                case 2:
                    MtdObtenerCoordenadas();
                    break;

                case 3:
                    MtdIniciarGrabarAudio();
                    break;

                case 4:
                    MtdCrearCarpetaRadio();
                    break;

                case 5:
                    MtdDetenerGrabarAudio();
                    break;

            }

        } else {
            Log.e("Navegacion10", "PERMISO NEGADO");
            FncMostrarToast("Permiso denegado, es posible que la aplicacion no funcione  correctamente.");
        }

    }


    @Override
    public void onClick(View v) {

        Log.d("RADIO", "onClick");

        int id = v.getId();
        switch (id) {



           /* case R.id.fab5:

                if(RadioAccion.equals("1")){

                    Log.d("RADIO", "GRABAR");

                //   if (!isPlaying){
                        RadioAccion = "2";
                        isRecording = true;
                        RadioMensajeReproducir = false;

                        //Log.e("Navegacion500", "Iniciar Radio");

                        MediaPlayer player_sou_radio11a = MediaPlayer.create(NavegacionActivity.this, R.raw.sou_radio11a);
                        player_sou_radio11a = MediaPlayer.create(NavegacionActivity.this, R.raw.sou_radio11a);
                        player_sou_radio11a.start();

                        Vibrator vibrator = (Vibrator) this.context.getSystemService(Context.VIBRATOR_SERVICE);
                        // Vibrate for 500 milliseconds
                        vibrator.vibrate(250);

                        if(checkPermission(3)){
                            MtdIniciarGrabarAudio();
                        }

                       // Log.d("Raj", "Fab 5");



                }else{

                    Log.d("RADIO", "DETENER");

                    RadioAccion = "1";
                    isRecording = false;
                    RadioMensajeReproducir = true;

                //    Log.e("Navegacion500", "Detener Radio");

                    MediaPlayer player_radio11b = MediaPlayer.create(NavegacionActivity.this, R.raw.sou_radio11b);
                    player_radio11b = MediaPlayer.create(NavegacionActivity.this, R.raw.sou_radio11b);
                    player_radio11b.start();

                    if(checkPermission(5)){
                        MtdDetenerGrabarAudio();
                    }

                    fab5.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00CC33")));//VERDE

                }
                break;

*/

            case R.id.fab1:


                RadioAccion = "2";
                isRecording = true;
                RadioMensajeReproducir = false;

                Log.e("Navegacion500", "Iniciar Radio");

                fab1.setVisibility(View.GONE);
                fab2.setVisibility(View.VISIBLE);
                fab3.setVisibility(View.GONE);


                if(MonitoreoSonido) {

                    //MediaPlayer player_sou_radio11a = MediaPlayer.create(NavegacionActivity.this, R.raw.sou_radio11a);
                   // player_sou_radio11a = MediaPlayer.create(NavegacionActivity.this, R.raw.sou_radio11a);
                   // player_sou_radio11a.start();

                    mediaPlayerRadioA.start();

                }


                Vibrator vibrator = (Vibrator) this.context.getSystemService(Context.VIBRATOR_SERVICE);
                // Vibrate for 500 milliseconds
                vibrator.vibrate(250);

                if (checkPermission(3)) {
                    MtdIniciarGrabarAudio();
                }

                Log.d("Raj", "Fab 1");

                break;

            case R.id.fab2:

                RadioAccion = "1";
                isRecording = false;
                RadioMensajeReproducir = true;

                Log.e("Navegacion500", "Detener Radio");

                fab1.setVisibility(View.VISIBLE);
                fab2.setVisibility(View.GONE);
                fab3.setVisibility(View.GONE);
                // fab1.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF0405")));

                if(MonitoreoSonido) {

                    //MediaPlayer player_radio11b = MediaPlayer.create(NavegacionActivity.this, R.raw.sou_radio11b);
                    //player_radio11b = MediaPlayer.create(NavegacionActivity.this, R.raw.sou_radio11b);
                    //player_radio11b.start();

                    mediaPlayerRadioB.start();
                }

                if (checkPermission(5)) {
                    MtdDetenerGrabarAudio();
                }

                Log.d("Raj", "Fab 2");
                break;


            case R.id.fab3:

                //   fab1.startAnimation(fab_close);
                // fab2.startAnimation(fab_close);
                // fab3.startAnimation(fab_close);


                /*Log.e("Navegacion500", "Radio Accion");

                if(RadioAccion.equals("1")){

                    RadioAccion = "2";
                    isRecording = true;

                    Log.e("Navegacion500", "Iniciar Radio");

                    //recordButton = (ImageButton) findViewById(R.id.BtnNavegacionIniciarRadio);
                    //recordButton.setBackgroundResource(R.drawable.dra_boton_radio2);

                    fab1.startAnimation(fab_close);
                    fab2.startAnimation(fab_close);
                    //fab3.startAnimation(fab_close);

                    MediaPlayer player = MediaPlayer.create(NavegacionActivity.this, R.raw.sou_radio11a);
                    player = MediaPlayer.create(NavegacionActivity.this, R.raw.sou_radio11a);
                    player.start();

                    Vibrator vv = (Vibrator) this.context.getSystemService(Context.VIBRATOR_SERVICE);
                    // Vibrate for 500 milliseconds
                    vv.vibrate(250);

                    if(checkPermission(3)){
                        MtdIniciarGrabarAudio();
                    }

                }else if(RadioAccion.equals("2")){

                    RadioAccion = "1";
                    isRecording = false;

                    Log.e("Navegacion500", "Detener Radio");

                    //recordButton = (ImageButton) findViewById(R.id.BtnNavegacionIniciarRadio);
                    //recordButton.setBackgroundResource(R.drawable.dra_boton_radio);

                    MediaPlayer player = MediaPlayer.create(NavegacionActivity.this, R.raw.sou_radio11b);
                    player = MediaPlayer.create(NavegacionActivity.this, R.raw.sou_radio11b);
                    player.start();

                    if(checkPermission(5)){
                        MtdDetenerGrabarAudio();
                    }

                }else{
                    Log.e("Navegacion500", "ERROR Radio");
                }
*/


                Log.d("Raj", "Fab 3");
                break;

            case R.id.fab4:

                MtdEscogerCanal();

                Log.d("Raj", "Fab 4");
                break;

            case R.id.fab41:

                MtdEscogerCanal();

                Log.d("Raj", "Fab 41");
                break;

            case R.id.fab42:

                MtdEscogerCanal();

                Log.d("Raj", "Fab 42");
                break;

            case R.id.fab43:

                MtdEscogerCanal();

                Log.d("Raj", "Fab 43");
                break;

            case R.id.fab44:

                MtdEscogerCanal();

                Log.d("Raj", "Fab 44");
                break;

            case R.id.fab45:

                MtdEscogerCanal();

                Log.d("Raj", "Fab 45");

                break;

            case R.id.fab6:

                Intent intentListarCanalConductor = new Intent(NavegacionActivity.this, ListarCanalConductorActivity.class);
                Bundle bundleListarCanalConductor = new Bundle();

                Log.e("ConductorCanal", ConductorCanal);

                bundleListarCanalConductor.putString("ConductorCanal", ConductorCanal);

                intentListarCanalConductor.putExtras(bundleListarCanalConductor);//Put your id to your next Intent
                startActivity(intentListarCanalConductor);
                finish();


                Log.d("Raj", "Fab 6");

                break;

            case R.id.FabRadio:

                MtdEscogerCobertura();

                Log.d("Raj", "FabRadio");

                break;

        }
    }

    public void MtdEscogerCanal() {

        //ACCIONES
        final CharSequence[] items = {"Canal 1", "Canal 2", "Canal 3", "Canal 4", "Canal 5"};

        int intConductorCanal = Integer.parseInt(ConductorCanal);
        intConductorCanal = intConductorCanal - 1;

        // Creating and Building the Dialog
        AlertDialog.Builder MsgNavegacion = new AlertDialog.Builder(this);
        MsgNavegacion.setTitle("Escoja un canal");
        //MsgNavegacion.setCancelable(false);
        MsgNavegacion.setSingleChoiceItems(items, intConductorCanal,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {

                        //TextView TxtCanalActual = (TextView) findViewById(R.id.CmpCanalActual);

                        switch (item) {
                            case 0:


                                Thread nt0 = new Thread() {
                                    @Override
                                    public void run() {

                                        try {

                                            final String resMtdActualizarConductorCanal;
                                            resMtdActualizarConductorCanal = MtdActualizarConductorCanal(ConductorId, "1");

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {

                                                    String JsRespuesta = "";

                                                    try {
                                                        JSONObject jsonObject = new JSONObject(resMtdActualizarConductorCanal);
                                                        JsRespuesta = jsonObject.getString("Respuesta");

                                                    } catch (JSONException e) {
                                                        Log.e("Navegacion7", e.toString());
                                                        e.printStackTrace();
                                                    }

                                                    switch (JsRespuesta) {

                                                        case "C032":


                                                            FncMostrarToast("Cambiando a Canal 1");

                                                            break;

                                                        case "C033":
                                                            break;

                                                        case "C034":
                                                            break;

                                                        default:
                                                            break;

                                                    }

                                                }
                                            });
                                        } catch (final Exception e) {
                                            // TODO: handle exception
                                            Log.e("Navegacion11", e.toString());
                                        }

                                    }

                                };
                                nt0.start();


                                RadioCanal = "Canal 1";
                                ConductorCanal = "1";
                                txtCanalActual.setText("Canal " + ConductorCanal + "/" + ConductorVehiculoUnidadDestino);

//                                        fab4.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.icon_1canal150));

                                fab4.setVisibility(View.GONE);
                                fab41.setVisibility(View.VISIBLE);
                                fab42.setVisibility(View.GONE);
                                fab43.setVisibility(View.GONE);
                                fab44.setVisibility(View.GONE);
                                fab45.setVisibility(View.GONE);


                                SharedPreferences sharedPreferences0 = getSharedPreferences("policar.policarappv6", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor0 = sharedPreferences0.edit();
                                editor0.putString("RadioCanal", "Canal 1");
                                editor0.putString("ConductorCanal", "1");
                                editor0.apply();

                                fab1.setVisibility(View.VISIBLE);
                                fab2.setVisibility(View.GONE);
                                fab3.setVisibility(View.GONE);

                                dialog.dismiss();

                                break;


                            case 1:

                                Thread nt1 = new Thread() {
                                    @Override
                                    public void run() {

                                        try {

                                            final String resMtdActualizarConductorCanal;
                                            resMtdActualizarConductorCanal = MtdActualizarConductorCanal(ConductorId, "2");

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {

                                                    String JsRespuesta = "";

                                                    try {
                                                        JSONObject jsonObject = new JSONObject(resMtdActualizarConductorCanal);
                                                        JsRespuesta = jsonObject.getString("Respuesta");

                                                    } catch (JSONException e) {
                                                        Log.e("Navegacion7", e.toString());
                                                        e.printStackTrace();
                                                    }

                                                    switch (JsRespuesta) {

                                                        case "C032":

                                                            FncMostrarToast("Cambiando a Canal 2");

                                                            break;

                                                        case "C033":
                                                            break;

                                                        case "C034":
                                                            break;

                                                        default:
                                                            break;

                                                    }

                                                }
                                            });
                                        } catch (final Exception e) {
                                            // TODO: handle exception
                                            Log.e("Navegacion11", e.toString());
                                        }

                                    }

                                };
                                nt1.start();


                                RadioCanal = "Canal 2";
                                ConductorCanal = "2";
                                txtCanalActual.setText("Canal " + ConductorCanal + "/" + ConductorVehiculoUnidadDestino);

                                //fab4.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.icon_2canal150));

                                fab4.setVisibility(View.GONE);
                                fab41.setVisibility(View.GONE);
                                fab42.setVisibility(View.VISIBLE);
                                fab43.setVisibility(View.GONE);
                                fab44.setVisibility(View.GONE);
                                fab45.setVisibility(View.GONE);


                                SharedPreferences sharedPreferences1 = getSharedPreferences("policar.policarappv6", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor1 = sharedPreferences1.edit();
                                editor1.putString("RadioCanal", "Canal 2");
                                editor1.putString("ConductorCanal", "2");
                                editor1.apply();

                                fab1.setVisibility(View.VISIBLE);
                                fab2.setVisibility(View.GONE);
                                fab3.setVisibility(View.GONE);

                                dialog.dismiss();

                                break;


                            case 2:

                                Thread nt2 = new Thread() {
                                    @Override
                                    public void run() {

                                        try {

                                            final String resMtdActualizarConductorCanal;
                                            resMtdActualizarConductorCanal = MtdActualizarConductorCanal(ConductorId, "3");

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {

                                                    String JsRespuesta = "";

                                                    try {
                                                        JSONObject jsonObject = new JSONObject(resMtdActualizarConductorCanal);
                                                        JsRespuesta = jsonObject.getString("Respuesta");

                                                    } catch (JSONException e) {
                                                        Log.e("Navegacion7", e.toString());
                                                        e.printStackTrace();
                                                    }

                                                    switch (JsRespuesta) {

                                                        case "C032":


                                                            FncMostrarToast("Cambiando a Canal 3");
                                                            break;

                                                        case "C033":
                                                            break;

                                                        case "C034":
                                                            break;

                                                        default:
                                                            break;

                                                    }

                                                }
                                            });
                                        } catch (final Exception e) {
                                            // TODO: handle exception
                                            Log.e("Navegacion11", e.toString());
                                        }

                                    }

                                };
                                nt2.start();


                                RadioCanal = "Canal 3";
                                ConductorCanal = "3";
                                txtCanalActual.setText("Canal " + ConductorCanal + "/" + ConductorVehiculoUnidadDestino);

                                //fab4.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.icon_3canal150));

                                fab4.setVisibility(View.GONE);
                                fab41.setVisibility(View.GONE);
                                fab42.setVisibility(View.GONE);
                                fab43.setVisibility(View.VISIBLE);
                                fab44.setVisibility(View.GONE);
                                fab45.setVisibility(View.GONE);


                                SharedPreferences sharedPreferences3 = getSharedPreferences("policar.policarappv6", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor3 = sharedPreferences3.edit();
                                editor3.putString("RadioCanal", "Canal 3");
                                editor3.putString("ConductorCanal", "3");
                                editor3.apply();

                                fab1.setVisibility(View.VISIBLE);
                                fab2.setVisibility(View.GONE);
                                fab3.setVisibility(View.GONE);

                                dialog.dismiss();

                                break;

                            case 3:

                                Thread nt3 = new Thread() {
                                    @Override
                                    public void run() {

                                        try {

                                            final String resMtdActualizarConductorCanal;
                                            resMtdActualizarConductorCanal = MtdActualizarConductorCanal(ConductorId, "4");

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {

                                                    String JsRespuesta = "";

                                                    try {
                                                        JSONObject jsonObject = new JSONObject(resMtdActualizarConductorCanal);
                                                        JsRespuesta = jsonObject.getString("Respuesta");

                                                    } catch (JSONException e) {
                                                        Log.e("Navegacion7", e.toString());
                                                        e.printStackTrace();
                                                    }

                                                    switch (JsRespuesta) {

                                                        case "C032":

                                                            FncMostrarToast("Cambiando a Canal 4");
                                                            break;

                                                        case "C033":
                                                            break;

                                                        case "C034":
                                                            break;

                                                        default:
                                                            break;

                                                    }

                                                }
                                            });
                                        } catch (final Exception e) {
                                            // TODO: handle exception
                                            Log.e("Navegacion11", e.toString());
                                        }

                                    }

                                };
                                nt3.start();

                                RadioCanal = "Canal 4";
                                ConductorCanal = "4";
                                txtCanalActual.setText("Canal " + ConductorCanal + "/" + ConductorVehiculoUnidadDestino);

                                //fab4.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.icon_4canal150));

                                fab4.setVisibility(View.GONE);
                                fab41.setVisibility(View.GONE);
                                fab42.setVisibility(View.GONE);
                                fab43.setVisibility(View.GONE);
                                fab44.setVisibility(View.VISIBLE);
                                fab45.setVisibility(View.GONE);


                                SharedPreferences sharedPreferences4 = getSharedPreferences("policar.policarappv6", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor4 = sharedPreferences4.edit();
                                editor4.putString("RadioCanal", "Canal 4");
                                editor4.putString("ConductorCanal", "4");
                                editor4.apply();

                                fab1.setVisibility(View.VISIBLE);
                                fab2.setVisibility(View.GONE);
                                fab3.setVisibility(View.GONE);

                                dialog.dismiss();

                                break;

                            case 4:

                                Thread nt4 = new Thread() {
                                    @Override
                                    public void run() {

                                        try {

                                            final String resMtdActualizarConductorCanal;
                                            resMtdActualizarConductorCanal = MtdActualizarConductorCanal(ConductorId, "5");

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {

                                                    String JsRespuesta = "";

                                                    try {
                                                        JSONObject jsonObject = new JSONObject(resMtdActualizarConductorCanal);
                                                        JsRespuesta = jsonObject.getString("Respuesta");

                                                    } catch (JSONException e) {
                                                        Log.e("Navegacion7", e.toString());
                                                        e.printStackTrace();
                                                    }

                                                    switch (JsRespuesta) {

                                                        case "C032":


                                                            FncMostrarToast("Cambiando a Canal 5");
                                                            break;

                                                        case "C033":
                                                            break;

                                                        case "C034":
                                                            break;

                                                        default:
                                                            break;

                                                    }

                                                }
                                            });
                                        } catch (final Exception e) {
                                            // TODO: handle exception
                                            Log.e("Navegacion11", e.toString());
                                        }

                                    }

                                };
                                nt4.start();

                                RadioCanal = "Canal 5";
                                ConductorCanal = "5";
                                txtCanalActual.setText("Canal " + ConductorCanal + "/" + ConductorVehiculoUnidadDestino);

                                //fab4.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.icon_5canal150));
                                fab4.setVisibility(View.GONE);
                                fab41.setVisibility(View.GONE);
                                fab42.setVisibility(View.GONE);
                                fab43.setVisibility(View.GONE);
                                fab44.setVisibility(View.GONE);
                                fab45.setVisibility(View.VISIBLE);


                                SharedPreferences sharedPreferences5 = getSharedPreferences("policar.policarappv6", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor5 = sharedPreferences5.edit();
                                editor5.putString("RadioCanal", "Canal 5");
                                editor5.putString("ConductorCanal", "5");
                                editor5.apply();

                                fab1.setVisibility(View.VISIBLE);
                                fab2.setVisibility(View.GONE);
                                fab3.setVisibility(View.GONE);

                                dialog.dismiss();

                                break;

                        }

                    }
                });
        //levelDialog = MsgNavegacion.create();
        //levelDialog.show();

        AlertDialog msgNavegacion = MsgNavegacion.create();
        msgNavegacion.show();


    }


    public void MtdEscogerCobertura() {

        //ACCIONES
        final CharSequence[] items = {"1 Kilometro", "2 Kilometros", "3 Kilometros", "4 Kilometros", "Libre"};

        int intCobertura = Integer.parseInt(Cobertura);
        intCobertura = intCobertura - 1;

        // Creating and Building the Dialog
        AlertDialog.Builder MsgNavegacion = new AlertDialog.Builder(this);
        MsgNavegacion.setTitle("Escoja una cobertura");
        //MsgNavegacion.setCancelable(false);
        MsgNavegacion.setSingleChoiceItems(items, intCobertura,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {

                        //TextView TxtCanalActual = (TextView) findViewById(R.id.CmpCanalActual);

                        switch (item) {
                            case 0:

                                /*Thread nt0 = new Thread() {
                                    @Override
                                    public void run() {

                                        try {

                                            final String resMtdActualizarConductorCanal;
                                            resMtdActualizarConductorCanal = MtdActualizarConductorCanal(ConductorId, "1");

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {

                                                    String JsRespuesta = "";

                                                    try {
                                                        JSONObject jsonObject = new JSONObject(resMtdActualizarConductorCanal);
                                                        JsRespuesta = jsonObject.getString("Respuesta");

                                                    } catch (JSONException e) {
                                                        Log.e("Navegacion7", e.toString());
                                                        e.printStackTrace();
                                                    }

                                                    switch (JsRespuesta) {

                                                        case "C032":


                                                            FncMostrarToast("Cambiando a Canal 1");

                                                            break;

                                                        case "C033":
                                                            break;

                                                        case "C034":
                                                            break;

                                                        default:
                                                            break;

                                                    }

                                                }
                                            });
                                        } catch (final Exception e) {
                                            // TODO: handle exception
                                            Log.e("Navegacion11", e.toString());
                                        }

                                    }

                                };
                                nt0.start();*/


                                Cobertura = "1";
                                //txtCanalActual.setText("Canal " + ConductorCanal + "/" + ConductorVehiculoUnidadDestino);

                               // SharedPreferences sharedPreferences0 = getSharedPreferences("policar.policarappv6", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor0 = sharedPreferences2.edit();
                                editor0.putString("Cobertura", "1");
                                editor0.apply();

                                dialog.dismiss();

                                break;


                            case 1:

                                /*Thread nt1 = new Thread() {
                                    @Override
                                    public void run() {

                                        try {

                                            final String resMtdActualizarConductorCanal;
                                            resMtdActualizarConductorCanal = MtdActualizarConductorCanal(ConductorId, "2");

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {

                                                    String JsRespuesta = "";

                                                    try {
                                                        JSONObject jsonObject = new JSONObject(resMtdActualizarConductorCanal);
                                                        JsRespuesta = jsonObject.getString("Respuesta");

                                                    } catch (JSONException e) {
                                                        Log.e("Navegacion7", e.toString());
                                                        e.printStackTrace();
                                                    }

                                                    switch (JsRespuesta) {

                                                        case "C032":

                                                            FncMostrarToast("Cambiando a Canal 2");

                                                            break;

                                                        case "C033":
                                                            break;

                                                        case "C034":
                                                            break;

                                                        default:
                                                            break;

                                                    }

                                                }
                                            });
                                        } catch (final Exception e) {
                                            // TODO: handle exception
                                            Log.e("Navegacion11", e.toString());
                                        }

                                    }

                                };
                                nt1.start();*/


                                Cobertura = "2";
                                //txtCanalActual.setText("Canal " + ConductorCanal + "/" + ConductorVehiculoUnidadDestino);

                                //SharedPreferences sharedPreferences1 = getSharedPreferences("policar.policarappv6", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor1 = sharedPreferences2.edit();
                                editor1.putString("Cobertura", "2");
                                editor1.apply();

                                dialog.dismiss();

                                break;


                            case 2:

                                /*Thread nt2 = new Thread() {
                                    @Override
                                    public void run() {

                                        try {

                                            final String resMtdActualizarConductorCanal;
                                            resMtdActualizarConductorCanal = MtdActualizarConductorCanal(ConductorId, "3");

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {

                                                    String JsRespuesta = "";

                                                    try {
                                                        JSONObject jsonObject = new JSONObject(resMtdActualizarConductorCanal);
                                                        JsRespuesta = jsonObject.getString("Respuesta");

                                                    } catch (JSONException e) {
                                                        Log.e("Navegacion7", e.toString());
                                                        e.printStackTrace();
                                                    }

                                                    switch (JsRespuesta) {

                                                        case "C032":


                                                            FncMostrarToast("Cambiando a Canal 3");
                                                            break;

                                                        case "C033":
                                                            break;

                                                        case "C034":
                                                            break;

                                                        default:
                                                            break;

                                                    }

                                                }
                                            });
                                        } catch (final Exception e) {
                                            // TODO: handle exception
                                            Log.e("Navegacion11", e.toString());
                                        }

                                    }

                                };
                                nt2.start();*/


                                Cobertura = "3";
                                //txtCanalActual.setText("Canal " + ConductorCanal + "/" + ConductorVehiculoUnidadDestino);

                                SharedPreferences.Editor editor3 = sharedPreferences2.edit();
                                editor3.putString("Cobertura", "3");
                                editor3.apply();



                                dialog.dismiss();

                                break;

                            case 3:

                                /*Thread nt3 = new Thread() {
                                    @Override
                                    public void run() {

                                        try {

                                            final String resMtdActualizarConductorCanal;
                                            resMtdActualizarConductorCanal = MtdActualizarConductorCanal(ConductorId, "4");

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {

                                                    String JsRespuesta = "";

                                                    try {
                                                        JSONObject jsonObject = new JSONObject(resMtdActualizarConductorCanal);
                                                        JsRespuesta = jsonObject.getString("Respuesta");

                                                    } catch (JSONException e) {
                                                        Log.e("Navegacion7", e.toString());
                                                        e.printStackTrace();
                                                    }

                                                    switch (JsRespuesta) {

                                                        case "C032":

                                                            FncMostrarToast("Cambiando a Canal 4");
                                                            break;

                                                        case "C033":
                                                            break;

                                                        case "C034":
                                                            break;

                                                        default:
                                                            break;

                                                    }

                                                }
                                            });
                                        } catch (final Exception e) {
                                            // TODO: handle exception
                                            Log.e("Navegacion11", e.toString());
                                        }

                                    }

                                };
                                nt3.start();*/

                              //
                                Cobertura = "4";
                               // txtCanalActual.setText("Canal " + ConductorCanal + "/" + ConductorVehiculoUnidadDestino);

                                SharedPreferences.Editor editor4 = sharedPreferences2.edit();
                                editor4.putString("Cobertura", "4");
                                editor4.apply();

                                dialog.dismiss();

                                break;

                            case 4:

                                /*Thread nt4 = new Thread() {
                                    @Override
                                    public void run() {

                                        try {

                                            final String resMtdActualizarConductorCanal;
                                            resMtdActualizarConductorCanal = MtdActualizarConductorCanal(ConductorId, "5");

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {

                                                    String JsRespuesta = "";

                                                    try {
                                                        JSONObject jsonObject = new JSONObject(resMtdActualizarConductorCanal);
                                                        JsRespuesta = jsonObject.getString("Respuesta");

                                                    } catch (JSONException e) {
                                                        Log.e("Navegacion7", e.toString());
                                                        e.printStackTrace();
                                                    }

                                                    switch (JsRespuesta) {

                                                        case "C032":


                                                            FncMostrarToast("Cambiando a Canal 5");
                                                            break;

                                                        case "C033":
                                                            break;

                                                        case "C034":
                                                            break;

                                                        default:
                                                            break;

                                                    }

                                                }
                                            });
                                        } catch (final Exception e) {
                                            // TODO: handle exception
                                            Log.e("Navegacion11", e.toString());
                                        }

                                    }

                                };
                                nt4.start();*/

                                Cobertura = "0";
                                //txtCanalActual.setText("Canal " + ConductorCanal + "/" + ConductorVehiculoUnidadDestino);

                                SharedPreferences.Editor editor5 = sharedPreferences2.edit();
                                editor5.putString("Cobertura", "0");
                                editor5.apply();


                                dialog.dismiss();

                                break;

                        }

                    }
                });
        //levelDialog = MsgNavegacion.create();
        //levelDialog.show();

        AlertDialog msgNavegacion = MsgNavegacion.create();
        msgNavegacion.show();


    }


    public void MtdObtenerCoordenadas() {

        //OBTENIENDO UBICACION
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //Toast.makeText(this, "GPS is Enabled in your devide", Toast.LENGTH_SHORT).show();

            NavegacionListener mlocListener = new NavegacionListener();
            mlocListener.setNavegacionActivity(this);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mlocListener);

            Location lastKnownLocation_byGps =
                    locationManager.getLastKnownLocation(gpsLocationProvider);
            Location lastKnownLocation_byNetwork =
                    locationManager.getLastKnownLocation(networkLocationProvider);

            if (lastKnownLocation_byGps == null) {
                if (lastKnownLocation_byNetwork == null) {

                } else {
                    VehiculoCoordenadaX = Double.toString(lastKnownLocation_byNetwork.getLatitude());
                    VehiculoCoordenadaY = Double.toString(lastKnownLocation_byNetwork.getLongitude());
                }

            } else {
                if (lastKnownLocation_byNetwork == null) {
                    VehiculoCoordenadaX = Double.toString(lastKnownLocation_byGps.getLatitude());
                    VehiculoCoordenadaY = Double.toString(lastKnownLocation_byGps.getLongitude());

                } else {
                    if (lastKnownLocation_byGps.getAccuracy() <= lastKnownLocation_byNetwork.getAccuracy()) {
                        VehiculoCoordenadaX = Double.toString(lastKnownLocation_byGps.getLatitude());
                        VehiculoCoordenadaY = Double.toString(lastKnownLocation_byGps.getLongitude());
                    } else {
                        VehiculoCoordenadaX = Double.toString(lastKnownLocation_byNetwork.getLatitude());
                        VehiculoCoordenadaY = Double.toString(lastKnownLocation_byNetwork.getLongitude());
                    }
                }
            }

        } else {
            Toast.makeText(this, "Su GPS no esta activo", Toast.LENGTH_SHORT).show();
        }

    }


    public void MtdIniciarGrabarAudio(){

        Log.e("MtdIniciarGrabarAudio", TipoRadio);

        if(TipoRadio.equals("TMP3")){



            try {

                Long tsLong = System.currentTimeMillis() / 1000;
                String ts = tsLong.toString();

                //audioFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/radio/myaudio" + ts + "_" + ConductorId + "_" + ConductorCanal + "_" + ConductorIdDestino + ".mp3";
                audioFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/radio/myaudio" + ts + "_" + ConductorId + "_" + ConductorCanal + "_" + ConductorIdDestino + ".mp3";
                //audioFilePath = Environment.getDataDirectory().getAbsolutePath() + "/myaudio" + ts + "_" + ConductorId + "_" + ConductorCanal + "_" + ConductorIdDestino + ".mp3";

                File tmp = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/radio/", "myaudio" + ts + "_" + ConductorId + "_" + ConductorCanal + "_" + ConductorIdDestino + ".mp3");
                //File tmp = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/", "myaudio" + ts + "_" + ConductorId + "_" + ConductorCanal + "_" + ConductorIdDestino + ".mp3");

                mAudioRecordHelper = new AudioRecordHelper(tmp, new AudioRecordHelper.RecordCallback() {
                    @Override
                    public void onRecordStart() {
                        //showStatus("Recording, release to play!");
                        Log.e("mAudioRecordHelper", "onRecordStart");
                    }

                    @Override
                    public void onProgress(long time) {
                        Log.e("mAudioRecordHelper", "onProgress");
                    }

                    @SuppressWarnings("unchecked")
                    @Override
                    public void onRecordDone(final File file, final long time) {
                        //  showStatus("Record done!\n\rFile size:" + file.length() + "B Time:" + time + "ms");
                        // mAudioPlayHelper.trigger(MainActivity.this, file.getAbsolutePath());
                    }
                });

                mAudioPlayHelper.stop();
                mAudioRecordHelper.recordAsync();

               /* mediaRecorder = new MediaRecorder();
                mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                //mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                //mediaRecorder.setAudioEncoder(MediaRecorder.getAudioSourceMax());
                mediaRecorder.setAudioEncodingBitRate(96000);//32
                mediaRecorder.setAudioSamplingRate(44100);//44100
                mediaRecorder.setOutputFile(audioFilePath);
*/
                //isRecording = true;

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                // mediaRecorder.prepare();
                //   mediaRecorder.start();
            } catch (Exception e) {
                e.printStackTrace();
            }

            //  }

        }else{


            //    if(!isPlaying){

            try {

                Long tsLong = System.currentTimeMillis()/1000;
                String ts = tsLong.toString();

                // File folder = new File(Environment.getExternalStorageDirectory() + "/radio");

                boolean success = true;

                //if (!folder.exists()) {
                //     folder.mkdir();
                //}

                audioFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/radio/myaudio"+ts+"_"+ConductorId+"_"+ConductorCanal+"_"+ConductorIdDestino+".3gp";
                mediaRecorder = new MediaRecorder();
                mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                //mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
                //mediaRecorder.setAudioEncoder(MediaRecorder.getAudioSourceMax());
                mediaRecorder.setAudioEncodingBitRate(16);//32
                mediaRecorder.setAudioSamplingRate(44100);//44100
                mediaRecorder.setOutputFile(audioFilePath);

                //isRecording = true;

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                mediaRecorder.prepare();
                mediaRecorder.start();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }



        //  }

    }


    public void MtdDetenerGrabarAudio(){

        Log.e("MtdDetenerGrabarAudio", TipoRadio);

        if(TipoRadio.equals("TMP3")){

            try {

                mAudioRecordHelper.stop(false);

            /*mediaRecorder.stop();
            mediaRecorder.reset();
            mediaRecorder.release();
            mediaRecorder = null;
*/
                //isRecording = false;

            } catch (Exception e) {
                e.printStackTrace();
            }

            // dialog = ProgressDialog.show(NavegacionActivity.this,"","Cargando...",true);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    //creating new thread to handle Http Operations
                    uploadFile(audioFilePath);
                }
            }).start();

        }else{

            try {

                mediaRecorder.stop();
                mediaRecorder.reset();
                mediaRecorder.release();
                mediaRecorder = null;

                //isRecording = false;

            } catch (Exception e) {
                e.printStackTrace();
            }

            // dialog = ProgressDialog.show(NavegacionActivity.this,"","Cargando...",true);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    //creating new thread to handle Http Operations
                    uploadFile(audioFilePath);
                }
            }).start();
        }




    }

    public void MtdCrearCarpetaRadio() {

        File folder = new File(Environment.getExternalStorageDirectory() + "/radio");

        boolean success = true;

        if (!folder.exists()) {
            folder.mkdir();
        }

    }


    public void MtdIniciarGrabarAudio5() {


        //    if(!isPlaying){

        try {

            Long tsLong = System.currentTimeMillis() / 1000;
            String ts = tsLong.toString();



            audioFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/radio/myaudio" + ts + "_" + ConductorId + "_" + ConductorCanal + "_" + ConductorIdDestino + ".mp3";
            //audioFilePath = Environment.getDataDirectory().getAbsolutePath() + "/myaudio" + ts + "_" + ConductorId + "_" + ConductorCanal + "_" + ConductorIdDestino + ".mp3";

            File tmp = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/radio/", "myaudio" + ts + "_" + ConductorId + "_" + ConductorCanal + "_" + ConductorIdDestino + ".mp3");
            //File tmp = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/", "myaudio" + ts + "_" + ConductorId + "_" + ConductorCanal + "_" + ConductorIdDestino + ".mp3");


            mAudioRecordHelper = new AudioRecordHelper(tmp, new AudioRecordHelper.RecordCallback() {
                @Override
                public void onRecordStart() {
                    //showStatus("Recording, release to play!");
                    Log.e("mAudioRecordHelper", "onRecordStart");
                }

                @Override
                public void onProgress(long time) {
                    Log.e("mAudioRecordHelper", "onProgress");
                }

                @SuppressWarnings("unchecked")
                @Override
                public void onRecordDone(final File file, final long time) {
                    //  showStatus("Record done!\n\rFile size:" + file.length() + "B Time:" + time + "ms");
                    // mAudioPlayHelper.trigger(MainActivity.this, file.getAbsolutePath());
                }
            });

            mAudioPlayHelper.stop();
            mAudioRecordHelper.recordAsync();

               /* mediaRecorder = new MediaRecorder();
                mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                //mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                //mediaRecorder.setAudioEncoder(MediaRecorder.getAudioSourceMax());
                mediaRecorder.setAudioEncodingBitRate(96000);//32
                mediaRecorder.setAudioSamplingRate(44100);//44100
                mediaRecorder.setOutputFile(audioFilePath);
*/
            //isRecording = true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            // mediaRecorder.prepare();
            //   mediaRecorder.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //  }

    }


    public void MtdDetenerGrabarAudio5() {

        try {

            mAudioRecordHelper.stop(false);

            /*mediaRecorder.stop();
            mediaRecorder.reset();
            mediaRecorder.release();
            mediaRecorder = null;
*/
            //isRecording = false;

        } catch (Exception e) {
            e.printStackTrace();
        }

        // dialog = ProgressDialog.show(NavegacionActivity.this,"","Cargando...",true);

        new Thread(new Runnable() {
            @Override
            public void run() {
                //creating new thread to handle Http Operations
                uploadFile(audioFilePath);
            }
        }).start();

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


    private MenuItem menuAlerta;
    private MenuItem menuVerAlerta;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navegacion, menu);

        menuAlerta = menu.findItem(R.id.action_emergencia);
        menuVerAlerta = menu.findItem(R.id.action_ver_emergencia);

        menuAlerta.setVisible(true);
        menuVerAlerta.setVisible(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_emergencia) {


            final ProgressDialog prgNavegacion = new ProgressDialog(this);
            prgNavegacion.setIcon(R.mipmap.ic_launcher);
            prgNavegacion.setMessage("Cargando...");
            prgNavegacion.setCancelable(false);
            prgNavegacion.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            prgNavegacion.show();

            Thread nt = new Thread() {
                @Override
                public void run() {

                    try {

                        final String resRegistrarConductorAlerta;
                        resRegistrarConductorAlerta = MtdRegistrarConductorAlerta(ConductorId,ConductorNumeroDocumento,ConductorNombre,VehiculoUnidad,VehiculoPlaca, "1", VehiculoCoordenadaX, VehiculoCoordenadaY, RegionId);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                String JSRespuesta = "";
                                String JSConductorId = "";

                                try {
                                    JSONObject jsonObject = new JSONObject(resRegistrarConductorAlerta);
                                    JSRespuesta = jsonObject.getString("Respuesta");

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                prgNavegacion.cancel();


                                switch (JSRespuesta) {
                                    case "A004":

                                        //MediaPlayer player = MediaPlayer.create(NavegacionActivity.this, R.raw.sou_boton1);
                                        //player = MediaPlayer.create(NavegacionActivity.this, R.raw.sou_boton1);
                                        //player.start();

                                        mediaPlayerEmergenciaEnviar.start();

                                        break;

                                    case "A005":
                                        FncMostrarMensaje("No se ha podido registrar la alerta, intente nuevamente", false);
                                        break;

                                    case "A006":
                                        FncMostrarMensaje("No se ha podido identificarlo, se reiniciará la aplicación", false);
                                        break;

                                    default:
                                        FncMostrarToast(getString(R.string.message_error_interno));
                                        break;
                                }

                            }
                        });
                    } catch (final Exception e) {
                        // TODO: handle exception
                        Log.e("Navegacion11", e.toString());
                    }

                }

            };
            nt.start();

            return true;
        } else if (id == R.id.action_ver_emergencia) {

            Intent intentMapa = new Intent(NavegacionActivity.this, MapaActivity.class);

            Bundle bundleMapa = new Bundle();
            bundleMapa.putString("ConductorId", ConductorId);
            bundleMapa.putString("ConductorAlertaCoordenadaX", ConductorAlertaCoordenadaX);
            bundleMapa.putString("ConductorAlertaCoordenadaY", ConductorAlertaCoordenadaY);
            bundleMapa.putString("ConductorAlerta", "1");

            intentMapa.putExtras(bundleMapa); //Put your id to your next Intent

            startActivity(intentMapa);
            finish();

            return true;
        } else if (id == R.id.action_mensaje) {

            final ProgressDialog prgNavegacion = new ProgressDialog(this);
            prgNavegacion.setIcon(R.mipmap.ic_launcher);
            prgNavegacion.setMessage("Cargando...");
            prgNavegacion.setCancelable(false);
            prgNavegacion.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            prgNavegacion.show();

            Thread nt = new Thread() {
                @Override
                public void run() {

                    try {

                        final String resMtdObtenerConductorMensajeUltimo;
                        resMtdObtenerConductorMensajeUltimo = MtdObtenerConductorMensajeUltimo(ConductorId, "2","");

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                String JsRespuesta = "";
                                String JsConductorMensajeId = "";
                                String JsConductorMensajeTitulo = "";
                                String JsConductorMensajeDescripcion= "";

                                try {
                                    JSONObject jsonObject = new JSONObject(resMtdObtenerConductorMensajeUltimo);
                                    JsRespuesta = jsonObject.getString("Respuesta");

                                    JsConductorMensajeId = jsonObject.getString("ConductorMensajeId");
                                    JsConductorMensajeTitulo = jsonObject.getString("ConductorMensajeTitulo");
                                    JsConductorMensajeDescripcion = jsonObject.getString("ConductorMensajeDescripcion");


                                } catch (JSONException e) {
                                    Log.e("Navegacion50", e.toString());
                                    e.printStackTrace();
                                }

                                prgNavegacion.cancel();

                                switch (JsRespuesta) {

                                    case "A001":

                                        mediaPlayerMensaje.start();

                                        AlertDialog.Builder MsgNavegacion = new AlertDialog.Builder(NavegacionActivity.this);
                                        MsgNavegacion.setTitle(JsConductorMensajeTitulo);
                                        MsgNavegacion.setMessage(JsConductorMensajeDescripcion);
                                        MsgNavegacion.setCancelable(false);
                                        MsgNavegacion.setPositiveButton("Aceptar",
                                                new DialogInterface.OnClickListener() {

                                                    public void onClick(DialogInterface dialog, int which) {
                                                        // Do nothing but close the dialog

                                                    }
                                                });

                                        // Remember, create doesn't show the dialog
                                        AlertDialog msgNavegacion = MsgNavegacion.create();
                                        msgNavegacion.show();

                                        break;

                                    case "A002":

                                        AlertDialog.Builder MsgNavegacion2 = new AlertDialog.Builder(NavegacionActivity.this);
                                        MsgNavegacion2.setTitle(getString(R.string.app_titulo));
                                        MsgNavegacion2.setMessage("¡No se encontraron mensajes!");
                                        MsgNavegacion2.setCancelable(false);
                                        /*sgNavegacion2.setPositiveButton("Aceptar",
                                                new DialogInterface.OnClickListener() {

                                                    public void onClick(DialogInterface dialog, int which) {
                                                        // Do nothing but close the dialog

                                                    }
                                                });*/

                                        // Remember, create doesn't show the dialog
                                        final AlertDialog msgNavegacion2 = MsgNavegacion2.create();
                                        msgNavegacion2.show();

                                        final Timer timer2 = new Timer();
                                        timer2.schedule(new TimerTask() {
                                            public void run() {

                                                if(msgNavegacion2!=null && msgNavegacion2.isShowing()){
                                                    msgNavegacion2.dismiss();
                                                }

                                                timer2.cancel(); //this will cancel the timer of the system

                                            }
                                        }, 1500); // the timer will count 5 seconds....

                                        break;

                                    default:
                                        break;

                                }

                            }
                        });
                    } catch (final Exception e) {
                        // TODO: handle exception
                        Log.e("Navegacion11", e.toString());
                    }

                }

            };
            nt.start();

            return true;
        } else if (id == R.id.action_verificar_gps) {

            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                //Toast.makeText(this, "GPS is Enabled in your devide", Toast.LENGTH_SHORT).show();
                //FncMostrarToast("¡Su GPS esta activado!");
                //FncMostrarMensaje("¡Su GPS esta activado!",false);


                AlertDialog.Builder MsgGeneral = new AlertDialog.Builder(this);
                MsgGeneral.setTitle(getString(R.string.app_titulo));
                MsgGeneral.setMessage("¡Su GPS esta activado!");
                MsgGeneral.setCancelable(false);
                MsgGeneral.setPositiveButton("Aceptar",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                // Do nothing but close the dialog

                            }
                        });

                // Remember, create doesn't show the dialog
                AlertDialog msgGeneral = MsgGeneral.create();
                msgGeneral.show();


            } else {
                //showGPSDisabledAlertToUser();
                //Toast.makeText(this, "Su GPS no esta activo", Toast.LENGTH_SHORT).show();
                //FncMostrarToast("¡Su GPS esta desactivado!");
                //FncMostrarMensaje("¡Su GPS esta desactivado!, activelo desde \"Mis Preferencias\"",false);

                AlertDialog.Builder MsgGeneral = new AlertDialog.Builder(this);
                MsgGeneral.setTitle(getString(R.string.app_titulo));
                MsgGeneral.setMessage("¡Su GPS esta desactivado!, activelo desde Mis Preferencias");
                MsgGeneral.setCancelable(false);
                MsgGeneral.setPositiveButton("Aceptar",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                // Do nothing but close the dialog

                            }
                        });

                // Remember, create doesn't show the dialog
                AlertDialog msgGeneral = MsgGeneral.create();
                msgGeneral.show();

            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_inicio) {
            // Handle the camera action

            Intent intenNavegacion = new Intent(NavegacionActivity.this, NavegacionActivity.class);
            startActivity(intenNavegacion);
            finish();


        } else if (id == R.id.nav_ver_cuenta) {
            // Handle the camera action

            Intent intenConductorDato = new Intent(NavegacionActivity.this, ConductorDatoActivity.class);

            Bundle bundleConductorDato = new Bundle();
            bundleConductorDato.putString("ConductorId", ConductorId);

            intenConductorDato.putExtras(bundleConductorDato); //Put your id to your next Intent

            startActivity(intenConductorDato);
            finish();

        } else if (id == R.id.nav_ranking) {
            // Handle the camera action

            Intent intenRanking = new Intent(NavegacionActivity.this, RankingActivity.class);

            Bundle bundleRanking = new Bundle();
            bundleRanking.putString("ConductorId", ConductorId);

            intenRanking.putExtras(bundleRanking); //Put your id to your next Intent

            startActivity(intenRanking);
            finish();

        } else if (id == R.id.nav_mis_preferencias) {

            Intent intentMisPreferencias = new Intent(NavegacionActivity.this, MisPreferenciasActivity.class);

            Bundle bundleMisPreferencias = new Bundle();
            bundleMisPreferencias.putString("ConductorId", ConductorId);

            intentMisPreferencias.putExtras(bundleMisPreferencias); //Put your id to your next Intent

            startActivity(intentMisPreferencias);
            finish();

        } else if (id == R.id.nav_mapa) {

            Intent intentMapa = new Intent(NavegacionActivity.this, MapaActivity.class);

            Bundle bundleMapa = new Bundle();
            bundleMapa.putString("ConductorId", ConductorId);
            bundleMapa.putString("ConductorAlertaCoordenadaX", "");
            bundleMapa.putString("ConductorAlertaCoordenadaY", "");
            bundleMapa.putString("ConductorAlerta", "2");

            intentMapa.putExtras(bundleMapa); //Put your id to your next Intent

            startActivity(intentMapa);
            finish();


        } else if (id == R.id.nav_historial) {

            Intent intentHistorial = new Intent(NavegacionActivity.this, HistorialActivity.class);

            Bundle bundleHistorial = new Bundle();
            bundleHistorial.putString("ConductorId", ConductorId);

            intentHistorial.putExtras(bundleHistorial); //Put your id to your next Intent

            startActivity(intentHistorial);
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

                            Intent intentMainSesion = new Intent(NavegacionActivity.this, MainActivity.class);
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
                        if (oCerrar) {
                            finish();
                        }
                    }
                });

        // Remember, create doesn't show the dialog
        AlertDialog msgGeneral = MsgGeneral.create();
        msgGeneral.show();

    }


    private void FncMostrarToast(String oMensaje) {

        Toast.makeText(NavegacionActivity.this, oMensaje,
                Toast.LENGTH_LONG).show();
    }


    private void createMapView() {

        /**
         * Catch the null pointer exception that
         * may be thrown when initialising the map
         */
        try {

            if (null == googleMap) {

                googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map1)).getMap();
                googleMap.setMyLocationEnabled(true);
                googleMap.getUiSettings().setZoomControlsEnabled(true);
                googleMap.setPadding(0, 0, 0, 0);
                googleMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
                    @Override
                    public void onMyLocationChange(Location location) {

                        VehiculoCoordenadaX = Double.toString(location.getLatitude());
                        VehiculoCoordenadaY = Double.toString(location.getLongitude());

                        //  if(!VehiculoCoordenadaX.equals("") && !VehiculoCoordenadaY.equals("") && !VehiculoCoordenadaX.equals("0.00") && !VehiculoCoordenadaY.equals("0.00") &&  VehiculoCoordenadaX !=null && VehiculoCoordenadaY != null){

                            /*if(vehiculoMarker!=null){
                                vehiculoMarker.remove();
                            }

                            vehiculoMarker = googleMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(Double.parseDouble(VehiculoCoordenadaX), Double.parseDouble(VehiculoCoordenadaY)))
                                    .title("¡Aquì estoy!")
                                    .draggable(true)
                                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_taxi150)));
*/
                        //if( (VehiculoCoordenadaX.equals("") && VehiculoCoordenadaY.equals("")) || (VehiculoCoordenadaX.equals("0.00") && VehiculoCoordenadaY.equals("0.00")) || (VehiculoCoordenadaX == null && VehiculoCoordenadaY == null)){

                          /*  LatLng latLng = new LatLng(Double.parseDouble(VehiculoCoordenadaX),Double.parseDouble(VehiculoCoordenadaY));
                            CameraPosition cameraPosition = new CameraPosition.Builder()
                                    .target(latLng)      // Sets the center of the map to Mountain View
                                    .zoom(19)                   // Sets the zoom
                                    .bearing(90)  //era 90              // Sets the orientation of the camera to east
                                    .tilt(10)                   // Sets the tilt of the camera to 30 degrees
                                    .build();                   // Creates a CameraPosition from the builder
                            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
*/
                        //}

                               /* try {

                                    Geocoder geocoder = new Geocoder(NavegacionActivity.this, Locale.getDefault());
                                    List<Address> list = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                                    if (!list.isEmpty()) {
                                        Address address = list.get(0);
                                        VehiculoDireccionActual = address.getAddressLine(0);
                                    }

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }*/
//
                        //  }


                    }
                });
/*
                if(!VehiculoCoordenadaX.equals("") && !VehiculoCoordenadaY.equals("")  &&  VehiculoCoordenadaX !=null && VehiculoCoordenadaY != null){

                    if(vehiculoMarker!=null){
                        vehiculoMarker.remove();
                    }

                    vehiculoMarker = googleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(Double.parseDouble(VehiculoCoordenadaX), Double.parseDouble(VehiculoCoordenadaY)))
                            .title("¡Aquì estoy!")
                            .draggable(true)
                            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_taxi150)));

                    //if( (VehiculoCoordenadaX.equals("") && VehiculoCoordenadaY.equals("")) || (VehiculoCoordenadaX.equals("0.00") && VehiculoCoordenadaY.equals("0.00")) || (VehiculoCoordenadaX == null && VehiculoCoordenadaY == null)){

                    LatLng latLng = new LatLng(Double.parseDouble(VehiculoCoordenadaX),Double.parseDouble(VehiculoCoordenadaY));
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(latLng)      // Sets the center of the map to Mountain View
                            .zoom(19)                   // Sets the zoom
                            .bearing(90)  //era 90              // Sets the orientation of the camera to east
                            .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                            .build();                   // Creates a CameraPosition from the builder
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));



                    //}

                }*/


                /**
                 * If the map is still null after attempted initialisation,
                 * show an error to the user
                 */
                if (null == googleMap) {
                    FncMostrarToast("Ha ocurrido un error cargando el mapa.");
                }

            }
        } catch (Exception exception) {
            // Toast.makeText(getApplicationContext(),
            //   exception.toString(),Toast.LENGTH_SHORT).show();
            FncMostrarToast("Ha ocurrido un error cargando el mapa: " + exception.toString());
        }

    }

    public void onClick_BtnNavegacionDesconectar(View v) {

        //MediaPlayer player = MediaPlayer.create(NavegacionActivity.this, R.raw.sou_boton1);
        // player = MediaPlayer.create(NavegacionActivity.this, R.raw.sou_boton1);
        // player.start();


        //ACCIONES
        final CharSequence[] items = {"No desconectar", "Fuera de servicio", "Cliente abordo"};

        // int intConductorCanal = Integer.parseInt(ConductorCanal);
        //   intConductorCanal = intConductorCanal -1;

        // Creating and Building the Dialog
        AlertDialog.Builder MsgPedidoActual = new AlertDialog.Builder(this);
        MsgPedidoActual.setTitle("Motivo desconexion");
        //MsgPedidoActual.setCancelable(false);
        MsgPedidoActual.setSingleChoiceItems(items, 0,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {

                        // TextView TxtCanalActual = (TextView) findViewById(R.id.CmpCanalActual);

                        switch (item) {
                            case 0:

                                dialog.dismiss();

                                break;
                            case 1:

                                //GUARDAR MEMORIA
                                SharedPreferences.Editor editor1 = sharedPreferences2.edit();
                                editor1.putInt("ConductorOcupado", 1);
                                editor1.apply();

                                Intent intentConductorDesconectado1 = new Intent(NavegacionActivity.this, ConductorDesconectadoActivity.class);

                                Bundle bundleConductorDesconectado1 = new Bundle();
                                bundleConductorDesconectado1.putString("ConductorId", ConductorId);
                                bundleConductorDesconectado1.putInt("ConductorOcupado", 1);

                                intentConductorDesconectado1.putExtras(bundleConductorDesconectado1); //Put your id to your next Intent

                                startActivity(intentConductorDesconectado1);
                                finish();

                                dialog.dismiss();
                                break;


                            case 2:

                                //GUARDAR MEMORIA
                                SharedPreferences.Editor editor2 = sharedPreferences2.edit();
                                editor2.putInt("ConductorOcupado", 11);
                                editor2.apply();

                                Intent intentConductorDesconectado2 = new Intent(NavegacionActivity.this, ConductorDesconectadoActivity.class);

                                Bundle bundleConductorDesconectado2 = new Bundle();
                                bundleConductorDesconectado2.putString("ConductorId", ConductorId);
                                bundleConductorDesconectado2.putInt("ConductorOcupado", 11);

                                intentConductorDesconectado2.putExtras(bundleConductorDesconectado2); //Put your id to your next Intent

                                startActivity(intentConductorDesconectado2);
                                finish();

                                dialog.dismiss();
                                break;

                        }

                    }
                });
        //levelDialog = MsgPedidoActual.create();
        //levelDialog.show();

        AlertDialog msgNavegacion = MsgPedidoActual.create();
        msgNavegacion.show();

/*
        //GUARDAR MEMORIA
        //SharedPreferences sharedPreferences = getSharedPreferences("policar.policarappv6", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences2.edit();

        editor.putInt("ConductorOcupado", 1);
        editor.apply();


        Intent intentConductorDesconectado = new Intent(NavegacionActivity.this, ConductorDesconectadoActivity.class);

        Bundle bundleConductorDesconectado = new Bundle();
        bundleConductorDesconectado.putString("ConductorId", ConductorId);

        intentConductorDesconectado.putExtras(bundleConductorDesconectado); //Put your id to your next Intent

        startActivity(intentConductorDesconectado);
        finish();
*/


    }

    public void onClick_BtnNavegacionHistorial(View v) {

        Intent intentHistorial = new Intent(NavegacionActivity.this, HistorialActivity.class);

        Bundle bundleHistorial = new Bundle();
        bundleHistorial.putString("ConductorId", ConductorId);

        intentHistorial.putExtras(bundleHistorial); //Put your id to your next Intent

        startActivity(intentHistorial);
        finish();


    }

    public void onClick_BtnNavegacionMapa(View v) {

        Intent intentMapa = new Intent(NavegacionActivity.this, MapaActivity.class);

        Bundle bundleMapa = new Bundle();
        bundleMapa.putString("ConductorId", ConductorId);
        bundleMapa.putString("ConductorAlertaCoordenadaX", "");
        bundleMapa.putString("ConductorAlertaCoordenadaY", "");
        bundleMapa.putString("ConductorAlerta", "2");

        intentMapa.putExtras(bundleMapa); //Put your id to your next Intent

        startActivity(intentMapa);
        finish();



    }


    public void onClick_BtnNavegacionCanales(View v) {

      /*  // MediaPlayer player = MediaPlayer.create(NavegacionActivity.this, R.raw.sou_boton1);
        // player = MediaPlayer.create(NavegacionActivity.this, R.raw.sou_boton1);
        // player.start();

        //ANIMACION
        final Animation animRotate = AnimationUtils.loadAnimation(this,
                R.anim.anim_rotate);
        v.startAnimation(animRotate);


        //ACCIONES
        final CharSequence[] items = { "Canal 1", "Canal 2","Canal 3" };

        int intConductorCanal = Integer.parseInt(ConductorCanal);
        intConductorCanal = intConductorCanal -1;

        // Creating and Building the Dialog
        AlertDialog.Builder MsgNavegacion = new AlertDialog.Builder(this);
        MsgNavegacion.setTitle("Escoja un canal");
        //MsgNavegacion.setCancelable(false);
        MsgNavegacion.setSingleChoiceItems(items,intConductorCanal,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {

                        TextView TxtCanalActual = (TextView) findViewById(R.id.CmpCanalActual);

                        switch (item) {
                            case 0:

                                RadioCanal = "Canal 1";
                                ConductorCanal = "1";
                                TxtCanalActual.setText("Canal "+ConductorCanal+"/"+ConductorVehiculoUnidadDestino);

                                SharedPreferences sharedPreferences0 = getSharedPreferences("policar.policarappv6", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor0 = sharedPreferences0.edit();
                                editor0.putString("RadioCanal", "Canal 1");
                                editor0.putString("ConductorCanal", "1");
                                editor0.apply();

                                dialog.dismiss();

                                break;
                            case 1:
                                RadioCanal = "Canal 2";
                                ConductorCanal = "2";
                                TxtCanalActual.setText("Canal "+ConductorCanal+"/"+ConductorVehiculoUnidadDestino);

                                SharedPreferences sharedPreferences1 = getSharedPreferences("policar.policarappv6", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor1 = sharedPreferences1.edit();
                                editor1.putString("RadioCanal", "Canal 2");
                                editor1.putString("ConductorCanal", "2");
                                editor1.apply();

                                dialog.dismiss();

                                break;


                            case 2:

                                RadioCanal = "Canal 3";
                                ConductorCanal = "3";
                                TxtCanalActual.setText("Canal "+ConductorCanal+"/"+ConductorVehiculoUnidadDestino);

                                SharedPreferences sharedPreferences3 = getSharedPreferences("policar.policarappv6", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor3 = sharedPreferences3.edit();
                                editor3.putString("RadioCanal", "Canal 3");
                                editor3.putString("ConductorCanal", "3");
                                editor3.apply();

                                dialog.dismiss();

                                break;

                        }

                    }
                });
        //levelDialog = MsgNavegacion.create();
        //levelDialog.show();

        AlertDialog msgNavegacion = MsgNavegacion.create();
        msgNavegacion.show();
*/

    }
/*
* EVENTOS NAVEGACION
 */




    /*
   ENVIO DE VARIABLES
    */

    public String MtdObtenerPedidoPendientes(String oConductorId, String oGpsActivado, String oRegionId,String oSectorId,String oEmpresaId,String oCobertura,String oCoordenadaX, String oCoordenadaY) {

        URL url;
        String response = "";

        try {

            url = new URL(getString(R.string.app_url) + "/webservice/" + getString(R.string.webservice_jnpedido));

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // conn.setReadTimeout(15000);
            //conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            HashMap<String, String> postDataParams = new HashMap<>();
            postDataParams.put("RegionId", oRegionId);
            postDataParams.put("SectorId", oSectorId);
            postDataParams.put("EmpresaId", oEmpresaId);

            postDataParams.put("Cobertura", oCobertura);
            postDataParams.put("CoordenadaX", oCoordenadaX);
            postDataParams.put("CoordenadaY", oCoordenadaY);

            postDataParams.put("Identificador", Identificador);
            postDataParams.put("ConductorId", oConductorId);
            postDataParams.put("GpsActivado", oGpsActivado);

            postDataParams.put("AppVersion", getString(R.string.app_version));

            postDataParams.put("Accion", "ObtenerPedidoPendientes");

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
            // Log.e("Navegacion1616", String.valueOf(responseCode));
            Log.e("Navegacion18", response);

        } catch (Exception e) {
            Log.e("Navegacion19", e.toString());
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

            Log.e("MsgNavegacion7", response);

        } catch (Exception e) {

            Log.e("MsgNavegacion8", e.toString());
            e.printStackTrace();
        }

        return response;

    }


    public String MtdRegistrarConductorAlerta(String oConductorId, String oConductorNumeroDocumento,String oConductorNombre, String oVehiculoUnidad, String oVehiculoPlaca,String oAlertaTipo, String oAlertaCoordenadaX, String oAlertaCoordenadaY, String oRegionId) {

        URL url;
        String response = "";

        try {

            //url = new URL(getString(R.string.app_url) + "/webservice/JnConductorAlerta.php");
            url = new URL(getString(R.string.app_url) + "/webservice/"+getString(R.string.webservice_jnconductoralerta));

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            HashMap<String, String> postDataParams = new HashMap<>();
            postDataParams.put("ConductorId", oConductorId);
            postDataParams.put("ConductorNombre", oConductorNombre);
            postDataParams.put("ConductorNumeroDocumento", oConductorNumeroDocumento);
            postDataParams.put("VehiculoPlaca", oVehiculoPlaca);
            postDataParams.put("VehiculoUnidad", oVehiculoUnidad);
            postDataParams.put("RegionId", oRegionId);
            postDataParams.put("ConductorAlertaTipo", oAlertaTipo);
            postDataParams.put("ConductorAlertaCoordenadaX", oAlertaCoordenadaX);
            postDataParams.put("ConductorAlertaCoordenadaY", oAlertaCoordenadaY);
            postDataParams.put("Identificador", Identificador);
            postDataParams.put("AppVersion", getString(R.string.app_version));

            postDataParams.put("Accion", "RegistrarConductorAlerta");

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

            Log.e("MsgNavegacion5", response);

        } catch (Exception e) {

            Log.e("MsgNavegacion6", e.toString());
            e.printStackTrace();
        }

        return response;

    }

    public String MtdObtenerRadioMensajes(String oConductorId, String oRadioMensajeCanal) {

        URL url;
        String response = "";

        try {

            url = new URL(getString(R.string.app_url2) + "/webservice/"+getString(R.string.webservice_jnradiomensaje));

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            HashMap<String, String> postDataParams = new HashMap<>();
            postDataParams.put("ConductorId", oConductorId);
            postDataParams.put("RadioMensajeCanal", oRadioMensajeCanal);
            postDataParams.put("Identificador", Identificador);
            postDataParams.put("Accion", "ObtenerRadioMensajeUltimo");

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

            Log.e("MsgNavegacion11", response);

        } catch (Exception e) {

            Log.e("MsgNavegacion12", e.toString());
            e.printStackTrace();
        }

        return response;

    }


    public String MtdObtenerConductorMensajeUltimo(String oConductorId,String oMarcarLeido,String oEstado) {

        URL url;
        String response = "";

        try {

            url = new URL(getString(R.string.app_url2) + "/webservice/"+getString(R.string.webservice_jnconductormensaje));

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            HashMap<String, String> postDataParams = new HashMap<>();
            postDataParams.put("ConductorId", oConductorId);
            postDataParams.put("MarcarLeido", oMarcarLeido);
            postDataParams.put("Estado", oEstado);

            postDataParams.put("Identificador", Identificador);
            postDataParams.put("Accion", "ObtenerConductorMensajeUltimo");

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

            Log.e("MsgNavegacion11", response);

        } catch (Exception e) {

            Log.e("MsgNavegacion12", e.toString());
            e.printStackTrace();
        }

        return response;

    }



    public String MtdObtenerPedidoAsignado(String oConductorId, String oVehiculoCoordenadaX, String oVehiculoCoordenadaY) {

        URL url;
        String response = "";

        try {

            url = new URL(getString(R.string.app_url) + "/webservice/" + getString(R.string.webservice_jnpedido));

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            HashMap<String, String> postDataParams = new HashMap<>();
            postDataParams.put("ConductorId", oConductorId);
            postDataParams.put("VehiculoCoordenadaX", oVehiculoCoordenadaX);
            postDataParams.put("VehiculoCoordenadaY", oVehiculoCoordenadaY);

            postDataParams.put("Identificador", Identificador);
            postDataParams.put("Accion", "ObtenerPedidoAsignado");

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

            Log.e("MsgNavegacion19", response);

        } catch (Exception e) {

            Log.e("MsgNavegacion20", e.toString());
            e.printStackTrace();
        }

        return response;

    }


    public String MtdActualizarConductorCanal(String oConductorId, String oConductorCanal) {

        URL url;
        String response = "";

        try {

            url = new URL(getString(R.string.app_url) + "/webservice/" + getString(R.string.webservice_jnconductor));

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            HashMap<String, String> postDataParams = new HashMap<>();
            postDataParams.put("ConductorId", oConductorId);
            postDataParams.put("ConductorCanal", oConductorCanal);

            postDataParams.put("Identificador", Identificador);
            postDataParams.put("Accion", "ActualizarConductorCanal");

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

            Log.e("Navegacion11", response);

        } catch (Exception e) {

            Log.e("Navegacion12", e.toString());
            e.printStackTrace();
        }

        return response;

    }


    public String MtdObtenerConductorAlertas() {

        URL url;
        String response = "";

        try {

            url = new URL(getString(R.string.app_url)+"/webservice/"+getString(R.string.webservice_jnconductoralerta));

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            HashMap<String, String>postDataParams=new HashMap<>();
            postDataParams.put("ConductorId", ConductorId);
            postDataParams.put("TiempoTranscurrido", "0");
            postDataParams.put("Identificador", Identificador);
            postDataParams.put("Accion", "ObtenerConductorAlertas");

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

            Log.e("Navegacion13", response);

        } catch (Exception e) {

            Log.e("Navegacion14", e.toString());
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

        Identificador = sharedPreferences.getString("Identificador", "");

        EmpresaId = sharedPreferences.getString("EmpresaId", "");
        EmpresaNombre = sharedPreferences.getString("EmpresaNombre", "");
        EmpresaFoto = sharedPreferences.getString("EmpresaFoto", "");

        ConductorId = sharedPreferences.getString("ConductorId", "");
        ConductorNombre = sharedPreferences.getString("ConductorNombre", "");
        ConductorNumeroDocumento = sharedPreferences.getString("ConductorNumeroDocumento", "");
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
        for (Map.Entry<String, String> entry : params.entrySet()) {
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




    //android upload file to server
    public int uploadFile(final String selectedFilePath) {

        int serverResponseCode = 0;

        HttpURLConnection connection;
        DataOutputStream dataOutputStream;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";

        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File selectedFile = new File(selectedFilePath);

        String[] parts = selectedFilePath.split("/");
        final String fileName = parts[parts.length - 1];


        //String response = "";

        try {

            //  String otherParametersUrServiceNeed =  "RadioCanal="+RadioCanal+"&Accion=RegistrarRadioMensaje";


            FileInputStream fileInputStream = new FileInputStream(selectedFile);
            String ruta = "";

            if(TipoRadio.equals("TMP3")){
                Log.e("",TipoRadio);
                ruta = "AccRadioSubirArchivov2.php";
            }else{
                ruta = "AccRadioSubirArchivov3.php";
            }

            URL url = new URL(getString(R.string.app_url2) + "/radio/"+ruta);

            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);//Allow Inputs
            connection.setDoOutput(true);//Allow Outputs
            connection.setUseCaches(false);//Don't use a cached Copy
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("ENCTYPE", "multipart/form-data");
            connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            connection.setRequestProperty("uploaded_file", selectedFilePath);


            //creating new dataoutputstream
            dataOutputStream = new DataOutputStream(connection.getOutputStream());

            //writing bytes to data outputstream
            //dataOutputStream.writeBytes(otherParametersUrServiceNeed);
            dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
            dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                    + selectedFilePath + "\"" + lineEnd);

            dataOutputStream.writeBytes(lineEnd);

            //returns no. of bytes present in fileInputStream
            bytesAvailable = fileInputStream.available();
            //selecting the buffer size as minimum of available bytes or 1 MB
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            //setting the buffer as byte array of size of bufferSize
            buffer = new byte[bufferSize];

            //reads bytes from FileInputStream(from 0th index of buffer to buffersize)
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            //loop repeats till bytesRead = -1, i.e., no bytes are left to read
            while (bytesRead > 0) {
                //write the bytes read from inputstream
                dataOutputStream.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }

            dataOutputStream.writeBytes(lineEnd);
            dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            Log.i("UploadFile2", ":3 ");

               /* HashMap<String, String>postDataParams=new HashMap<>();
                postDataParams.put("RadioCanal", RadioCanal);
                postDataParams.put("ConductorId", ConductorId);

                postDataParams.put("VehiculoCoordenadaX", VehiculoCoordenadaX);
                postDataParams.put("VehiculoCoordenadaY", VehiculoCoordenadaY);
                postDataParams.put("Identificador", Identificador);
                postDataParams.put("AppVersion", getString(R.string.app_version));
                postDataParams.put("Accion", "RegistrarRadioMensaje");
                OutputStream os = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));*/
/*

                String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while ((line=br.readLine()) != null) {
                    response+=line;
                }
                Log.i("UploadFile2", ":3 "+response);*/


            serverResponseCode = connection.getResponseCode();
            String serverResponseMessage = connection.getResponseMessage();

            Log.i("UploadFile", "Server Response is: " + serverResponseMessage + ": " + serverResponseCode);

            //response code of 200 indicates the server status OK
            if (serverResponseCode == 200) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        // Log.e("UPLOAD", serverResponseMessage.toString());

                        // tvFileName.setText("File Upload completed.\n\n You can see the uploaded file here: \n\n" + "http://coderefer.com/extras/uploads/"+ fileName);
                        // FncMostrarToast("Listo");
                    }
                });
            }

            //closing the input and output streams
            fileInputStream.close();
            dataOutputStream.flush();
            dataOutputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(NavegacionActivity.this, "Archivo no encontrado.", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Toast.makeText(NavegacionActivity.this, "Error de URL.", Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(NavegacionActivity.this, "No se pudo leer/escribir archivo.", Toast.LENGTH_SHORT).show();
        }

        // dialog.dismiss();
        return serverResponseCode;

    }

























    public static int getDistance(double lat_a,double lng_a, double lat_b, double lon_b){
        int Radius = 6371000; //Radio de la tierra
        double lat1 = lat_a / 1E6;
        double lat2 = lat_b / 1E6;
        double lon1 = lng_a / 1E6;
        double lon2 = lon_b / 1E6;
        double dLat = Math.toRadians(lat2-lat1);
        double dLon = Math.toRadians(lon2-lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon /2) * Math.sin(dLon/2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return (int) (Radius * c);

    }
    public static double redondear(double d, int decimalPlace) {

        BigDecimal bd = new BigDecimal(Double.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
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




    protected Boolean estaConectado(Boolean oMostrarMensaje){
        if(conectadoWifi()){

            //   Log.e("Navegacion60", "Tiene wifi");
            //showAlertDialog(Main.this, "Conexion a Internet",
            //         "Tu Dispositivo tiene Conexion a Wifi.", true);


               /* try {
                    HttpURLConnection urlc = (HttpURLConnection)
                            (new URL("http://clients3.google.com/generate_204")
                                    .openConnection());
                    urlc.setRequestProperty("User-Agent", "Android");
                    urlc.setRequestProperty("Connection", "close");
                    urlc.setConnectTimeout(1500);
                    urlc.connect();
                    return (urlc.getResponseCode() == 204 &&
                            urlc.getContentLength() == 0);
                } catch (IOException e) {

                    Log.e("Navegacion60", "Error checking internet connection", e);

                }*/

            //   return false;
            return true;
        }else{

            // Log.e("Navegacion60", "No tiene wifi");

            if(conectadoRedMovil()){

                //Log.e("Navegacion60", "Tiene datos");

               /* try {

                    HttpURLConnection urlc = (HttpURLConnection)
                            (new URL("http://clients3.google.com/generate_204")
                                    .openConnection());
                    urlc.setRequestProperty("User-Agent", "Android");
                    urlc.setRequestProperty("Connection", "close");
                    urlc.setConnectTimeout(1500);
                    urlc.connect();
                    return (urlc.getResponseCode() == 204 &&
                            urlc.getContentLength() == 0);

                } catch (IOException e) {

                    Log.e("Navegacion60", "Error checking internet connection", e);

                }*/

                //   showAlertDialog(Main.this, "Conexion a Internet",
                //         "Tu Dispositivo tiene Conexion Movil.", true);
                //return false;
                return true;
            }else{

                // Log.e("Navegacion60", "No tiene datos");
                ///showAlertDialog(Main.this, "Conexion a Internet",
                //         "Tu Dispositivo no tiene Conexion a Internet.", false);
                if(oMostrarMensaje){
                    FncMostrarToast("Verifica tu conexion a internet");
                }

                return false;
            }
        }
    }



    protected Boolean conectadoWifi(){

        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (info != null) {
                if (info.isConnected()) {
                    return true;
                }
            }
        }
        return false;

    }

    protected Boolean conectadoRedMovil(){

        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (info != null) {
                if (info.isConnected()) {
                    return true;
                }
            }
        }
        return false;
    }



}
