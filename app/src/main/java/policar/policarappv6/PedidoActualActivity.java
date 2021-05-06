package policar.policarappv6;

import android.app.KeyguardManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.view.Menu;
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
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.net.ssl.HttpsURLConnection;

public class PedidoActualActivity extends AppCompatActivity
        implements

        View.OnClickListener,
        NavigationView.OnNavigationItemSelectedListener {

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

    private String ClienteId = "";
    private String ClienteNombre = "";
    private String ClienteCelular = "";
    private String ClienteTelefono = "";
    private String ClienteFoto = "";

    private String PedidoDireccion = "";
    private String PedidoReferencia = "";
    private String PedidoDetalle = "";
    private String PedidoLugarCompra = "";


    private String PedidoEstado = "";
    private String PedidoTipo = "";
    private String PedidoTipoAccion = "";
    private String PedidoTipoUnidad = "";

    private String PedidoTiempo = "";
    private String PedidoDistancia = "";

    private String PedidoCoordenadaX = "0.00";
    private String PedidoCoordenadaY = "0.00";

    private String MonedaSimbolo = "";
    private String TarifaMinima = "0.00";
    private String TarifaKilometro = "0.00";
    private String TarifaAdicionalNoche = "0.00";
    private String TarifaAdicionalFestivo = "0.00";

    private String PedidoEsNoche = "2";
    private String PedidoEsFestivo = "2";

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
    * DATOS TIMER
     */
    Timer timerPedidoActual1;
    Timer timerPedidoActual2;
    Timer timerPedidoActual3;
    Timer timerPedidoActual4;

     /*
    * dDATOS GPS
     */


    final String gpsLocationProvider = LocationManager.GPS_PROVIDER;
    final String networkLocationProvider = LocationManager.NETWORK_PROVIDER;



    /*
   * Radio
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

    private boolean RadioMensajeReproducir = false;
    ArrayList RadioMensajeRecibidos = new ArrayList();

    private String RadioAccion = "";

    /*
       PERMISOS
        */
    private Context context;
    private Activity activity;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private View view;

    /*
    * CAMARA
     */
    private int MapaCamara = 2;
    private String CamaraCoordenadaX = "0.00";
    private String CamaraCoordenadaY = "0.00";


    /*
    * PREFERENCIAS
    * */
    SharedPreferences sharedPreferences2;

    private Boolean MonitoreoSonido;
    private Boolean MonitoreoEncendido;


    /*
    * CAJAS
     */
    TextView txtPedidoTiempo;
    TextView txtPedidoDistancia;
    /*
    * CAPAS
     */
    LinearLayout capPedidoDetalle;
    LinearLayout capPedidoReferencia;
    LinearLayout capPedidoLugarCompra;

    LinearLayout capPedidoTiempo;
    LinearLayout capPedidoDistancia;

    LinearLayout capPedidoActualAccion;

/*
* CAJAS
 */
    TextView txtClienteNombre;
    TextView txtPedidoDireccion;
    TextView txtPedidoReferencia;
    TextView txtPedidoDetalle;
    TextView txtPedidoLugarCompra;

    TextView txtClienteCelular;

    ImageView imgPedidoTipoAccion;
/*
* CRONOMETRO
 */
    Chronometer croTardanza;



    /*
* TARDANZA
 */
    private boolean Tardanza = false;


    private String RegionId = "";
    private String RegionNombre = "";


    private String SectorId = "";
    private String SectorNombre = "";

    int MapaAltura = 0;

    /**
     * BOTONES FLOTANTES
     */
    private FloatingActionButton fabAumentar, fabReducir, fabUbicar;

    //ONSTART -> ON CREATE -> ONRESTART
    @Override
    protected void onStart() {
        super.onStart();
        Log.e("PedidoActual20", "Start");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("PedidoActual20", "Resume");

    }

    //ONPAUSE -> ONRESUME -> ONSTOP
    @Override
    protected void onPause() {
        super.onPause();
        Log.e("PedidoActual20", "Pause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("PedidoActual20", "Stop");
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("PedidoActual20", "Restart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("PedidoActual20", "Destroy");

        if (timerPedidoActual1 != null) {
            timerPedidoActual1.cancel();
        }

        if (timerPedidoActual2 != null) {
            timerPedidoActual2.cancel();
        }

        if (timerPedidoActual4 != null) {
            timerPedidoActual4.cancel();
        }

        if(croTardanza!=null){
            croTardanza.stop();
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e("PedidoActual20", "SaveInstance");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.e("PedidoActual20", "RestoreInstance");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido_actual);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Log.e("PedidoActual20", "onCreate");

       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
/*
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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
        //actionBar.setTitle("Servicio Actual");        //Establecer titulo
        actionBar.setTitle(getString(R.string.title_activity_pedido_actual));     //Establecer Subtitulo



//PERMISOS
        context = getApplicationContext();
        activity = this;

        //PREFERENCIAS
        sharedPreferences2 = getSharedPreferences("policar.policarappv6", Context.MODE_PRIVATE);

        //PREFERENCIAS
        MonitoreoSonido = sharedPreferences2.getBoolean("MonitoreoSonido", true);
        MonitoreoEncendido = sharedPreferences2.getBoolean("MonitoreoEncendido", true);

        //RECUPERANDO VARIABLES
        Intent intentExtras = getIntent();
        Bundle extrasBundle = intentExtras.getExtras();

        PedidoId = intentExtras.getStringExtra("PedidoId");

        ClienteId = intentExtras.getStringExtra("ClienteId");
        ClienteNombre = intentExtras.getStringExtra("ClienteNombre");
        ClienteCelular = intentExtras.getStringExtra("ClienteCelular");
        ClienteTelefono = intentExtras.getStringExtra("ClienteTelefono");

        PedidoDireccion = intentExtras.getStringExtra("PedidoDireccion");
        PedidoReferencia = intentExtras.getStringExtra("PedidoReferencia");
        PedidoDetalle = intentExtras.getStringExtra("PedidoDetalle");
        PedidoLugarCompra = intentExtras.getStringExtra("PedidoLugarCompra");

        PedidoEstado = intentExtras.getStringExtra("PedidoEstado");
        PedidoTipo = intentExtras.getStringExtra("PedidoTipo");
        PedidoTipoAccion = intentExtras.getStringExtra("PedidoTipoAccion");
        PedidoTipoUnidad = intentExtras.getStringExtra("PedidoTipoUnidad");

        PedidoTiempo = intentExtras.getStringExtra("PedidoTiempo");
        PedidoDistancia = intentExtras.getStringExtra("PedidoDistancia");

        PedidoCoordenadaX = intentExtras.getStringExtra("PedidoCoordenadaX");
        PedidoCoordenadaY = intentExtras.getStringExtra("PedidoCoordenadaY");

        MonedaSimbolo = intentExtras.getStringExtra("MonedaSimbolo");
        TarifaMinima = intentExtras.getStringExtra("TarifaMinima");
        TarifaKilometro = intentExtras.getStringExtra("TarifaKilometro");
        TarifaAdicionalFestivo = intentExtras.getStringExtra("TarifaAdicionalFestivo");
        TarifaAdicionalNoche = intentExtras.getStringExtra("TarifaAdicionalNoche");
        PedidoEsFestivo = intentExtras.getStringExtra("PedidoEsFestivo");
        PedidoEsNoche = intentExtras.getStringExtra("PedidoEsNoche");

        VehiculoCoordenadaX = sharedPreferences2.getString("VehiculoCoordenadaX", "0.00");
        VehiculoCoordenadaY = sharedPreferences2.getString("VehiculoCoordenadaY", "0.00");



        //RECUPERANDO VARIABLES v2
        displayUserSettings();

        //CAMARA
        MapaCamara = 3;

        //MOSTRANDO VARIABLES
        txtClienteNombre = (TextView) findViewById(R.id.CmpClienteNombre);
        txtPedidoDireccion = (TextView) findViewById(R.id.CmpPedidoDireccion);
        txtPedidoReferencia = (TextView) findViewById(R.id.CmpConductorNombre);
        txtPedidoDetalle = (TextView) findViewById(R.id.CmpPedidoDetalle);
        txtPedidoLugarCompra = (TextView) findViewById(R.id.CmpPedidoActualLugarCompra);
        txtClienteCelular = (TextView) findViewById(R.id.CmpClienteCelular);

        txtPedidoTiempo = (TextView) findViewById(R.id.CmpPedidoTiempo);
        txtPedidoDistancia = (TextView) findViewById(R.id.CmpPedidoDistancia);

        imgPedidoTipoAccion = (ImageView)  findViewById(R.id.ImgPedidoActualTipoAccion);

        croTardanza = (Chronometer) findViewById(R.id.CroTardanza);

        //MOSTRANDO DATOS
        txtClienteNombre.setText(ClienteNombre);
        txtPedidoDireccion.setText(PedidoDireccion);
        txtPedidoReferencia.setText(PedidoReferencia);
        txtPedidoDetalle.setText(PedidoDetalle);
        txtPedidoLugarCompra.setText(PedidoLugarCompra);
        txtClienteCelular.setText(ClienteCelular);

        //CAPAS
        capPedidoDetalle = (LinearLayout) this.findViewById(R.id.CapPedidoDetalle);
        capPedidoReferencia = (LinearLayout) this.findViewById(R.id.CapPedidoReferencia);
        capPedidoLugarCompra = (LinearLayout) this.findViewById(R.id.CapPedidoActualLugarCompra);

        capPedidoTiempo = (LinearLayout) findViewById(R.id.CapPedidoTiempo);
        capPedidoDistancia = (LinearLayout) findViewById(R.id.CapPedidoDistancia);

        capPedidoActualAccion = (LinearLayout) findViewById(R.id.CapPedidoActualAccion);


        fabAumentar = (FloatingActionButton) findViewById(R.id.FabPedidoActualAumentar);
        fabReducir = (FloatingActionButton) findViewById(R.id.FabPedidoActualReducir);
        fabUbicar = (FloatingActionButton) findViewById(R.id.FabPedidoActualUbicar);

        //EVENTOS - 8
        fabAumentar.setOnClickListener(this);
        fabReducir.setOnClickListener(this);
        fabUbicar.setOnClickListener(this);



        //MOSTRANDO CAPAS
        if (PedidoReferencia.equals("") || PedidoReferencia == null) {
            capPedidoReferencia.setVisibility(View.GONE);
        } else {
            capPedidoReferencia.setVisibility(View.VISIBLE);
        }

        if (PedidoDetalle.equals("") || PedidoDetalle == null) {
            capPedidoDetalle.setVisibility(View.GONE);
        } else {
            capPedidoDetalle.setVisibility(View.VISIBLE);
        }

        if (PedidoLugarCompra.equals("") || PedidoLugarCompra == null) {
            capPedidoLugarCompra.setVisibility(View.GONE);
        } else {
            capPedidoLugarCompra.setVisibility(View.VISIBLE);
        }


     /*   if (PedidoTiempo.equals("") || PedidoTiempo.equals("-") || PedidoTiempo == null || PedidoTiempo.equals("null") || PedidoTiempo.equals("0.00") || PedidoTiempo.equals("0")) {
            capPedidoTiempo.setVisibility(View.GONE);
        } else {
            capPedidoTiempo.setVisibility(View.VISIBLE);
        }
        */
/*
        if (PedidoDistancia.equals("") || PedidoDistancia.equals("-") || PedidoDistancia == null || PedidoDistancia.equals("null") || PedidoDistancia.equals("0.00") || PedidoDistancia.equals("0")) {
            capPedidoDistancia.setVisibility(View.GONE);
        } else {
            capPedidoDistancia.setVisibility(View.VISIBLE);
        }
*/
        if ( PedidoTipoAccion.equals("Compra") || PedidoTipoAccion.equals("Gas")) {
            capPedidoActualAccion.setVisibility(View.VISIBLE);
        } else {
            capPedidoActualAccion.setVisibility(View.GONE);
        }

        switch (PedidoTipoAccion){

            case "Normal":
                imgPedidoTipoAccion.setImageResource(R.mipmap.icon_tipo_taxi150);
                break;

            case "Gas":
                imgPedidoTipoAccion.setImageResource(R.mipmap.icon_tipo_gas150);
                break;

            case "Compra":
                imgPedidoTipoAccion.setImageResource(R.mipmap.icon_tipo_compra150);
                break;

            case "Flete":
                imgPedidoTipoAccion.setImageResource(R.mipmap.icon_tipo_flete150);
                break;

        }

        switch (PedidoTipoAccion){

            case "Normal":
                actionBar.setIcon(R.mipmap.icon_tipo_taxi150);
                break;

            case "Gas":
                actionBar.setIcon(R.mipmap.icon_tipo_gas150);
                break;

            case "Compra":
                actionBar.setIcon(R.mipmap.icon_tipo_compra150);
                break;

            case "Flete":
                actionBar.setIcon(R.mipmap.icon_tipo_flete150);
                break;

        }


        //CALCULANDO TIEMPO DISTANCIA
        if (!PedidoCoordenadaX.equals("") && !PedidoCoordenadaY.equals("") && !PedidoCoordenadaX.equals("0.00") && !PedidoCoordenadaY.equals("0.00") && PedidoCoordenadaX != null && PedidoCoordenadaY != null) {

            if (!VehiculoCoordenadaX.equals("") && !VehiculoCoordenadaY.equals("") && !VehiculoCoordenadaX.equals("0.00") && !VehiculoCoordenadaY.equals("0.00") && VehiculoCoordenadaX != null && VehiculoCoordenadaY != null) {

                Double PedidoDistancia2 = redondear(calculateDistance(Double.parseDouble(VehiculoCoordenadaX), Double.parseDouble(VehiculoCoordenadaY), Double.parseDouble(PedidoCoordenadaX), Double.parseDouble(PedidoCoordenadaY)), 2);
                Double PedidoTiempo2 = 0.00;

                PedidoTiempo2 = redondear((((PedidoDistancia2) / 50) * 60), 2);

                PedidoDistancia = PedidoDistancia2.toString() + " km";
                PedidoTiempo = PedidoTiempo2.toString() + " min";

                txtPedidoDistancia.setText(PedidoDistancia);
                txtPedidoTiempo.setText(PedidoTiempo);

                /*
                if (PedidoTiempo.equals("") || PedidoTiempo.equals("-") || PedidoTiempo == null || PedidoTiempo.equals("null") || PedidoTiempo.equals("0.00") || PedidoTiempo.equals("0")) {
                    capPedidoTiempo.setVisibility(View.GONE);
                } else {
                    capPedidoTiempo.setVisibility(View.VISIBLE);
                }

                if (PedidoDistancia.equals("") || PedidoDistancia.equals("-") || PedidoDistancia == null || PedidoDistancia.equals("null") || PedidoDistancia.equals("0.00") || PedidoDistancia.equals("0")) {
                    capPedidoDistancia.setVisibility(View.GONE);
                } else {
                    capPedidoDistancia.setVisibility(View.VISIBLE);
                }
                */

            }

        }

        //CRONOMETRO
        long PedTiempoCreacion = sharedPreferences2.getLong("PedTiempoCreacion",0);

        Log.e("IniciarCronometro", "PedTiempoCreacion: "+String.valueOf(PedTiempoCreacion));

        croTardanza = (Chronometer) findViewById(R.id.CroTardanza);
        //cronometro2.setBase(SystemClock.elapsedRealtime());
        croTardanza.setBase(PedTiempoCreacion);
        croTardanza.setFormat("%H:%m:%s");
        croTardanza.start();

        croTardanza.setOnChronometerTickListener(
                new Chronometer.OnChronometerTickListener(){

                    @Override
                    public void onChronometerTick(Chronometer chronometer) {
                        // TODO Auto-generated method stub

                        //long myElapsedMillis = SystemClock.elapsedRealtime() - cronometro2.getBase();
                        long myElapsedMillis = SystemClock.elapsedRealtime() - croTardanza.getBase();

                        //Log.e("getBase",String.valueOf(myElapsedMillis));

                        SharedPreferences.Editor editor = sharedPreferences2.edit();
                        editor.putLong("myElapsedMillis", myElapsedMillis);
                        editor.apply();

                        if(!Tardanza){
                            if(myElapsedMillis>360000){
                                Tardanza = true;
                                Log.e("getBase","TARDANZA");

                                /*if (Build.VERSION.SDK_INT < 23) {
                                    cronometro2.setTextAppearance(context, R.style.est_aceptar_pedido_cronometro_expiro);
                                } else {
                                    cronometro2.setTextAppearance(R.style.est_aceptar_pedido_cronometro_expiro);
                                }*/

                            }
                        }

                        //long myElapsedMillis = SystemClock.elapsedRealtime() - myChronometer.getBase();
                        // String strElapsedMillis = "Elapsed milliseconds: " + myElapsedMillis;
                        //Toast.makeText(AndroidChronometer.this, strElapsedMillis, Toast.LENGTH_SHORT).show();
                    }}
        );


   /*     if(ClienteCelular.equals("")){
            LinearLayout capPedidoReferencia=(LinearLayout)this.findViewById(R.id.CapClienteCelular);
            capPedidoReferencia.setVisibility(View.GONE);
        }else{
            LinearLayout capPedidoReferencia=(LinearLayout)this.findViewById(R.id.CapClienteCelular);
            capPedidoReferencia.setVisibility(View.VISIBLE);
        }
*/

        //SONIDO
        // MediaPlayer player = MediaPlayer.create(PedidoActualActivity.this, R.raw.sou_aceptar);
        // player = MediaPlayer.create(PedidoActualActivity.this, R.raw.sou_aceptar);
        // player.start();


        RadioAccion = "1";
        ConductorCanal = "1";
        RadioCanal = "1";

        isRecording = false;


        //OBTENER COORDENADAS
        if (checkPermission(2)) {
            MtdObtenerCoordenadas();
        }

        //CREANDO MAPA
        if (checkPermission(1)) {
            createMapView();
        }


        //OBTENIENDO UBICACION
 /*       LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        PedidoActualListener mlocListener = new PedidoActualListener();
        mlocListener.setPedidoActualActivity(this);
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
*/

        String Mensaje = "";
        String MensajeCorto = "";

        if (!ClienteNombre.equals("")) {
            Mensaje += "Cliente: " + ClienteNombre;
        }

        if (!PedidoDireccion.equals("")) {
            Mensaje += " Dirección: " + PedidoDireccion;
            MensajeCorto += " Dirección: " + PedidoDireccion;
        }

        if (!PedidoReferencia.equals("")) {
            Mensaje += " Ref.: " + PedidoReferencia;
            MensajeCorto += " Ref.: " + PedidoReferencia;
        }

        if (!ClienteCelular.equals("")) {
            Mensaje += " Cel.: " + ClienteCelular;
        }

        if (!ClienteTelefono.equals("")) {
            Mensaje += " Tel.: " + ClienteTelefono;
        }

        displayNotificationOne(getString(R.string.alert_title) + " - Servicio Actual", Mensaje, MensajeCorto);


        Thread nt = new Thread() {
            @Override
            public void run() {

                try {

                    final String resMtdEnviarPedidoUbicacion;

                    resMtdEnviarPedidoUbicacion = MtdEnviarPedidoUbicacion(PedidoId,
                            PedidoCoordenadaX,
                            PedidoCoordenadaY,
                            VehiculoCoordenadaX,
                            VehiculoCoordenadaY);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            String JsRespuesta = "";


                            try {

                                JSONObject jsonObject = new JSONObject(resMtdEnviarPedidoUbicacion);
                                JsRespuesta = jsonObject.getString("Respuesta");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            //prgMainAcceder.cancel();

                            switch (JsRespuesta) {
                                case "P102":
                                    FncMostrarToast("Coordenadas transferidas correctamente");
                                    break;
                                case "P103":
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


        if (timerPedidoActual2 != null) {
            timerPedidoActual2.cancel();
        }

        timerPedidoActual2 = new Timer();
        timerPedidoActual2.schedule(new TimerTask() {
            @Override
            public void run() {

                try {

                    final String resActualizarConductorCoordenada;
                    resActualizarConductorCoordenada = MtdActualizarConductorCoordenada(
                            PedidoId,
                            ConductorId,
                            ConductorNombre,
                            ConductorNumeroDocumento,
                            VehiculoCoordenadaX, 
                            VehiculoCoordenadaY,
                            VehiculoUnidad,
                            VehiculoPlaca
                    );

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            String JsRespuesta = "";
                            String JsSectorNombre = "";
                            String JsSectorId = "";

                            try {

                                JSONObject jsonObject = new JSONObject(resActualizarConductorCoordenada);
                                JsRespuesta = jsonObject.getString("Respuesta");
                                JsSectorNombre = jsonObject.getString("SectorNombre");
                                JsSectorId = jsonObject.getString("SectorId");

                            } catch (JSONException e) {
                                Log.e("PedidoActual7", e.toString());
                                e.printStackTrace();
                            }

                            switch (JsRespuesta) {

                                case "U001":

                                    SharedPreferences.Editor editor = sharedPreferences2.edit();
                                    //editor.putString("RegionNombre", JsRegionNombre.trim());
                                    //editor.putString("RegionId", JsRegionId.trim());
                                    editor.putString("SectorNombre", JsSectorNombre.trim());
                                    editor.putString("SectorId", JsSectorId.trim());
                                    editor.apply();

                                    if(!JsSectorId.equals(SectorId)) {
                                        FncMostrarToast("Bienvenid@ a Sector "+JsSectorNombre);
                                    }

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
                    Log.e("PedidoActual11", e.toString());
                }


            }
        }, 1000, 3500);
        //TAREA ENVIAR COORDENADA PEDIDO


        if (timerPedidoActual1 != null) {
            timerPedidoActual1.cancel();
        }

        timerPedidoActual1 = new Timer();
        timerPedidoActual1.schedule(new TimerTask() {
            @Override
            public void run() {

                // final Timer timer = new Timer();
                //timer.schedule(new TimerTask(){
                //     @Override
                //     public void run() {
                try {

                    final String resObtenerPedido;
                    resObtenerPedido = MtdObtenerPedido(PedidoId);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            String JsRespuesta = "";
                            String JsPedidoEstado = "";
                            String JsPedidoCancelarMotivo = "";

                            try {

                                JSONObject jsonObject = new JSONObject(resObtenerPedido);
                                JsRespuesta = jsonObject.getString("Respuesta");
                                JsPedidoEstado = jsonObject.getString("PedidoEstado");
                                JsPedidoCancelarMotivo = jsonObject.getString("PedidoCancelarMotivo");

                            } catch (JSONException e) {
                                //Log.e("MsgPedidoActual", e.toString());
                                e.printStackTrace();
                            }

                            switch (JsRespuesta) {

                                case "P013":

                                    if (JsPedidoEstado.equals("3")) {

                                        if (timerPedidoActual1 != null) {
                                            timerPedidoActual1.cancel();
                                        }

                                     /*   SharedPreferences sharedPreferences = getSharedPreferences("policar.policarappv6", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();

                                        editor.putBoolean("ConductorTienePedido", false);
                                        editor.apply();*/


                                        SharedPreferences sharedPreferences = getSharedPreferences("policar.policarappv6", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putInt("ConductorOcupado", 2);
                                        editor.putBoolean("ConductorTienePedido", false);
                                        editor.apply();


                                        String PedidoActualMensaje1 = "ATENCION: El pedido ha sido cancelado.";

                                        if ((JsPedidoCancelarMotivo != null) && !JsPedidoCancelarMotivo.equals("") && !JsPedidoCancelarMotivo.equals("null")) {

                                            PedidoActualMensaje1 += " MOTIVO:" + JsPedidoCancelarMotivo;

                                        }

                                        displayNotificationOne(getString(R.string.app_name), "ATENCION: El pedido ha sido cancelado.", PedidoActualMensaje1);


                                        //if (!isFinishing()) {
                                        String PedidoActualMensaje2 = "El pedido ha sido cancelado.";

                                        if ((JsPedidoCancelarMotivo != null) && !JsPedidoCancelarMotivo.equals("") && !JsPedidoCancelarMotivo.equals("null")) {

                                            PedidoActualMensaje2 += " MOTIVO:" + JsPedidoCancelarMotivo;

                                        }

                                        android.app.AlertDialog.Builder MsgPedidoActual1 = new android.app.AlertDialog.Builder(PedidoActualActivity.this);
                                        MsgPedidoActual1.setTitle(getString(R.string.alert_title));
                                        MsgPedidoActual1.setMessage(PedidoActualMensaje2);
                                        MsgPedidoActual1.setCancelable(false);
                                        MsgPedidoActual1.setPositiveButton("Aceptar",
                                                new DialogInterface.OnClickListener() {

                                                    public void onClick(DialogInterface dialog, int which) {
                                                        // Do nothing but close the dialog


                                                        //Intent intent = new Intent(PedidoActualActivity.this, NavegacionActivity.class);
                                                        //startActivity(intent);
                                                        //finish();

                                                        Intent intentNavegacion = new Intent(PedidoActualActivity.this, NavegacionActivity.class);

                                                        Bundle bundleNavegacion = new Bundle();
                                                        bundleNavegacion.putString("PedidoMensaje", "");
                                                        intentNavegacion.putExtras(bundleNavegacion); //Put your id to your next Intent

                                                        startActivity(intentNavegacion);
                                                        finish();


                                                    }
                                                });

                                        // Remember, create doesn't show the dialog
                                        android.app.AlertDialog msgPedidoActual1 = MsgPedidoActual1.create();
                                        msgPedidoActual1.show();

                                    }

                                    break;

                                case "P014":
                                    break;

                                default:
                                    break;

                            }


                        }
                    });


                } catch (Exception e) {
                    // TODO: handle exception
                    // Log.e("MsgPedidoCliente", e.toString());
                }


            }
        }, 1000, 10000);
        //TAREA OBTENER PEDIDO ESTADO


        if (timerPedidoActual4 != null) {
            timerPedidoActual4.cancel();
        }

        //TAREA VERIFICAR MENSAJE

        timerPedidoActual4 = new Timer();
        timerPedidoActual4.schedule(new TimerTask() {
            @Override
            public void run() {

                try {

                    final String resVerificarPedidoMensajeConductor;
                    resVerificarPedidoMensajeConductor = MtdVerificarPedidoMensajeConductor(PedidoId);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            String JsRespuesta = "";
                            String JsPedidoMensajeConductor = "";
                            try {

                                JSONObject jsonObject = new JSONObject(resVerificarPedidoMensajeConductor);
                                JsRespuesta = jsonObject.getString("Respuesta");
                                JsPedidoMensajeConductor = jsonObject.getString("PedidoMensajeConductor");

                            } catch (JSONException e) {
                                Log.e("PedidoActual101", e.toString());
                                e.printStackTrace();
                            }

                            switch (JsRespuesta) {

                                case "P094":

                                    FncMostrarMensaje(JsPedidoMensajeConductor, false);

                                    //FncMostrarToast("¡Alerta recibida!");

                                    if (MonitoreoSonido) {
                                        //HABILITAR AUDIO MAS ADELANTE
                                        MediaPlayer player = MediaPlayer.create(PedidoActualActivity.this, R.raw.sou_mensaje);
                                        player = MediaPlayer.create(PedidoActualActivity.this, R.raw.sou_mensaje);
                                        player.start();
                                    }


                                    break;

                                case "P095":
                                    break;

                                case "P096":
                                    break;

                                case "P097":
                                    break;

                                default:
                                    break;
                            }

                        }
                    });

                } catch (Exception e) {
                    // TODO: handle exception
                    Log.e("PedidoActual101", e.toString());
                }

            }
        }, 1000, 5000);
        //TAREA VERIFICAR MENSAJE


    }


    private void createMapView() {

        /**
         * Catch the null pointer exception that
         * may be thrown when initialising the map
         */
        try {

            if (null == googleMap) {

                MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map1);

             //  ViewGroup.LayoutParams params = mapFragment.getView().getLayoutParams();
                // MapaAltura = params.height;
                //mMapFragment.getView().setLayoutParams(params);

                googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map1)).getMap();
                googleMap.setMyLocationEnabled(true);
                googleMap.getUiSettings().setZoomControlsEnabled(false);
                googleMap.getUiSettings().setMyLocationButtonEnabled(false);
                googleMap.setPadding(0, 0, 0, 0);

                googleMap.setOnMyLocationButtonClickListener(
                        new GoogleMap.OnMyLocationButtonClickListener() {

                            @Override
                            public boolean onMyLocationButtonClick() {

                                Location location = googleMap.getMyLocation();

                                if (location != null) {

                                    VehiculoCoordenadaX = Double.toString(location.getLatitude());
                                    VehiculoCoordenadaY = Double.toString(location.getLongitude());


                                    if (!PedidoCoordenadaX.equals("") && !PedidoCoordenadaY.equals("") && !PedidoCoordenadaX.equals("0.00") && !PedidoCoordenadaY.equals("0.00") && PedidoCoordenadaX != null && PedidoCoordenadaY != null) {

                                        if (!VehiculoCoordenadaX.equals("") && !VehiculoCoordenadaY.equals("") && !VehiculoCoordenadaX.equals("0.00") && !VehiculoCoordenadaY.equals("0.00") && VehiculoCoordenadaX != null && VehiculoCoordenadaY != null) {

                                            Double PedidoDistancia2 = redondear(calculateDistance(Double.parseDouble(VehiculoCoordenadaX), Double.parseDouble(VehiculoCoordenadaY), Double.parseDouble(PedidoCoordenadaX), Double.parseDouble(PedidoCoordenadaY)), 2);
                                            Double PedidoTiempo2 = 0.00;

                                            PedidoTiempo2 = redondear((((PedidoDistancia2) / 50) * 60), 2);

                                            PedidoDistancia = PedidoDistancia2.toString() + " km";
                                            PedidoTiempo = PedidoTiempo2.toString() + " min";

                                            txtPedidoDistancia.setText(PedidoDistancia);
                                            txtPedidoTiempo.setText(PedidoTiempo);

                                           /* if (PedidoTiempo.equals("") || PedidoTiempo.equals("-") || PedidoTiempo == null || PedidoTiempo.equals("null") || PedidoTiempo.equals("0.00") || PedidoTiempo.equals("0")) {
                                                capPedidoTiempo.setVisibility(View.GONE);
                                            } else {
                                                capPedidoTiempo.setVisibility(View.VISIBLE);
                                            }

                                            if (PedidoDistancia.equals("") || PedidoDistancia.equals("-") || PedidoDistancia == null || PedidoDistancia.equals("null") || PedidoDistancia.equals("0.00") || PedidoDistancia.equals("0")) {
                                                capPedidoDistancia.setVisibility(View.GONE);
                                            } else {
                                                capPedidoDistancia.setVisibility(View.VISIBLE);
                                            }*/

                                        }

                                    }


                                    Thread nt = new Thread() {
                                        @Override
                                        public void run() {

                                            //if(estaConectado(false)){

                                            try {

                                                final String resActualizarConductorCoordenada;
                                                resActualizarConductorCoordenada = MtdActualizarConductorCoordenada(PedidoId, ConductorId, ConductorNombre,ConductorNumeroDocumento,VehiculoCoordenadaX, VehiculoCoordenadaY,VehiculoUnidad,VehiculoPlaca);

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

                                                        switch (JsRespuesta) {

                                                            case "U001":
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
                                                // Log.e("MsgMonitoreo11", e.toString());
                                            }

                                            //}


                                        }

                                    };
                                    nt.start();

                                } else {
                                    FncMostrarToast("No se pudo obtener su ubicación");
                                }


                                return false;
                            }
                        }


                );


                googleMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
                    @Override
                    public void onMyLocationChange(Location location) {

                        if (location != null) {

                            VehiculoCoordenadaX = Double.toString(location.getLatitude());
                            VehiculoCoordenadaY = Double.toString(location.getLongitude());

                            if (!VehiculoCoordenadaX.equals("") && !VehiculoCoordenadaY.equals("") && VehiculoCoordenadaX != null && VehiculoCoordenadaY != null) {

                                if (vehiculoMarker != null) {
                                    vehiculoMarker.remove();
                                }

                                vehiculoMarker = googleMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(Double.parseDouble(VehiculoCoordenadaX), Double.parseDouble(VehiculoCoordenadaY)))
                                        .title("¡Aquí estoy!")
                                        .draggable(false)
                                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_taxi150)));

                                /*LatLng latLng = new LatLng(Double.parseDouble(VehiculoCoordenadaX),Double.parseDouble(VehiculoCoordenadaY));
                                CameraPosition cameraPosition = new CameraPosition.Builder()
                                        .target(latLng)      // Sets the center of the map to Mountain View
                                        .zoom(19)                   // Sets the zoom
                                        //.bearing(20)  //era 90              // Sets the orientation of the camera to east
                                        .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                                        .build();                   // Creates a CameraPosition from the builder
                                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));*/

                            }

                            if (MapaCamara == 1) {



                            }else if (MapaCamara == 2) {

                                LatLng latLng = new LatLng(Double.parseDouble(PedidoCoordenadaX), Double.parseDouble(PedidoCoordenadaY));

                                CameraPosition cameraPosition = new CameraPosition.Builder()
                                        .target(latLng)      // Sets the center of the map to Mountain View
                                        .zoom(18)                   // Sets the zoom
                                        //.bearing(20)  //era 90              // Sets the orientation of the camera to east
                                        .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                                        .build();                   // Creates a CameraPosition from the builder
                                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


                            }else{

                                LatLng latLng = new LatLng(Double.parseDouble(VehiculoCoordenadaX), Double.parseDouble(VehiculoCoordenadaY));

                                CameraPosition cameraPosition = new CameraPosition.Builder()
                                        .target(latLng)      // Sets the center of the map to Mountain View
                                        .zoom(18)                   // Sets the zoom
                                        //.bearing(20)  //era 90              // Sets the orientation of the camera to east
                                        .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                                        .build();                   // Creates a CameraPosition from the builder
                                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                            }


                            if (!PedidoCoordenadaX.equals("") && !PedidoCoordenadaY.equals("") && !PedidoCoordenadaX.equals("0.00") && !PedidoCoordenadaY.equals("0.00") && PedidoCoordenadaX != null && PedidoCoordenadaY != null) {

                                if (!VehiculoCoordenadaX.equals("") && !VehiculoCoordenadaY.equals("") && !VehiculoCoordenadaX.equals("0.00") && !VehiculoCoordenadaY.equals("0.00") && VehiculoCoordenadaX != null && VehiculoCoordenadaY != null) {

                                    Double PedidoDistancia2 = redondear(calculateDistance(Double.parseDouble(VehiculoCoordenadaX), Double.parseDouble(VehiculoCoordenadaY), Double.parseDouble(PedidoCoordenadaX), Double.parseDouble(PedidoCoordenadaY)), 2);
                                    Double PedidoTiempo2 = 0.00;

                                    PedidoTiempo2 = redondear((((PedidoDistancia2) / 50) * 60), 2);

                                    PedidoDistancia = PedidoDistancia2.toString() + " km";
                                    PedidoTiempo = PedidoTiempo2.toString() + " min";

                                    txtPedidoDistancia.setText(PedidoDistancia);
                                    txtPedidoTiempo.setText(PedidoTiempo);

                                    /*
                                    if (PedidoTiempo.equals("") || PedidoTiempo.equals("-") || PedidoTiempo == null || PedidoTiempo.equals("null") || PedidoTiempo.equals("0.00") || PedidoTiempo.equals("0")) {
                                        capPedidoTiempo.setVisibility(View.GONE);
                                    } else {
                                        capPedidoTiempo.setVisibility(View.VISIBLE);
                                    }

                                    if (PedidoDistancia.equals("") || PedidoDistancia.equals("-") || PedidoDistancia == null || PedidoDistancia.equals("null") || PedidoDistancia.equals("0.00") || PedidoDistancia.equals("0")) {
                                        capPedidoDistancia.setVisibility(View.GONE);
                                    } else {
                                        capPedidoDistancia.setVisibility(View.VISIBLE);
                                    }
*/
                                }

                            }


                        }


                    }
                });


                googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
                    @Override
                    public void onCameraChange(CameraPosition position) {

                        final Double Latitude = position.target.latitude;
                        final Double Longitude = position.target.longitude;

                        CamaraCoordenadaX = Double.toString(Latitude);
                        CamaraCoordenadaY = Double.toString(Longitude);

                    }
                });

                if (!VehiculoCoordenadaX.equals("") && !VehiculoCoordenadaY.equals("") && VehiculoCoordenadaX != null && VehiculoCoordenadaY != null) {

                    if (vehiculoMarker != null) {
                        vehiculoMarker.remove();
                    }

                    vehiculoMarker = googleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(Double.parseDouble(VehiculoCoordenadaX), Double.parseDouble(VehiculoCoordenadaY)))
                            .title("¡Aqui estoy!")
                            .draggable(false)
                            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_taxi150)));

                }

                if (!PedidoCoordenadaX.equals("") && !PedidoCoordenadaY.equals("") && PedidoCoordenadaX != null && PedidoCoordenadaY != null) {

                    if (pedidoMarker != null) {
                        pedidoMarker.remove();
                    }

                    pedidoMarker = googleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(Double.parseDouble(PedidoCoordenadaX), Double.parseDouble(PedidoCoordenadaY)))
                            .title(ClienteNombre)
                            .draggable(false)
                            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_pedido150)));
                }

                Log.e("MapaCamara",String.valueOf(MapaCamara));

                if (MapaCamara == 1) {


                    Log.e("MapaCamara","111");

                    if (!PedidoCoordenadaX.equals("") && !PedidoCoordenadaY.equals("") && PedidoCoordenadaX != null && PedidoCoordenadaY != null) {

                        LatLng latLng = new LatLng(Double.parseDouble(PedidoCoordenadaX), Double.parseDouble(PedidoCoordenadaY));
                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(latLng)      // Sets the center of the map to Mountain View
                                .zoom(18)                   // Sets the zoom
                                //.bearing(20)  //era 90              // Sets the orientation of the camera to east
                                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                                .build();                   // Creates a CameraPosition from the builder
                        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                    }


                } else if (MapaCamara == 2) {

                    Log.e("MapaCamara","222");

                    if (!VehiculoCoordenadaX.equals("") && !VehiculoCoordenadaY.equals("") && VehiculoCoordenadaX != null && VehiculoCoordenadaY != null) {

                        LatLng latLng = new LatLng(Double.parseDouble(VehiculoCoordenadaX), Double.parseDouble(VehiculoCoordenadaY));
                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(latLng)      // Sets the center of the map to Mountain View
                                .zoom(18)                   // Sets the zoom
                                //.bearing(20)  //era 90              // Sets the orientation of the camera to east
                                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                                .build();                   // Creates a CameraPosition from the builder
                        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                    }

                } else {

                    Log.e("MapaCamara","333");

                    if (!PedidoCoordenadaX.equals("") && !PedidoCoordenadaY.equals("") && PedidoCoordenadaX != null && PedidoCoordenadaY != null) {

                        if (!VehiculoCoordenadaX.equals("") && !VehiculoCoordenadaY.equals("") && VehiculoCoordenadaX != null && VehiculoCoordenadaY != null) {

                            LatLngBounds.Builder builder = new LatLngBounds.Builder();

                            builder.include(new LatLng(Double.parseDouble(PedidoCoordenadaX), Double.parseDouble(PedidoCoordenadaY)));
                            builder.include(new LatLng(Double.parseDouble(VehiculoCoordenadaX), Double.parseDouble(VehiculoCoordenadaY)));

                            LatLngBounds bounds = builder.build();

                            int width = getResources().getDisplayMetrics().widthPixels;
                            int height = getResources().getDisplayMetrics().heightPixels;
                            int padding = (int) (width * 0.30); // offset from edges of the map 12% of screen


                            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);

                            googleMap.animateCamera(cu);

                        }
                    }


                }


                if (!PedidoCoordenadaX.equals("") && !PedidoCoordenadaY.equals("") && !PedidoCoordenadaX.equals("0.00") && !PedidoCoordenadaY.equals("0.00") && PedidoCoordenadaX != null && PedidoCoordenadaY != null) {

                    if (!VehiculoCoordenadaX.equals("") && !VehiculoCoordenadaY.equals("") && !VehiculoCoordenadaX.equals("0.00") && !VehiculoCoordenadaY.equals("0.00") && VehiculoCoordenadaX != null && VehiculoCoordenadaY != null) {

                        Double PedidoDistancia2 = redondear(calculateDistance(Double.parseDouble(VehiculoCoordenadaX), Double.parseDouble(VehiculoCoordenadaY), Double.parseDouble(PedidoCoordenadaX), Double.parseDouble(PedidoCoordenadaY)), 2);
                        Double PedidoTiempo2 = 0.00;

                        PedidoTiempo2 = redondear((((PedidoDistancia2) / 50) * 60), 2);

                        PedidoDistancia = PedidoDistancia2.toString() + " km";
                        PedidoTiempo = PedidoTiempo2.toString() + " min";

                        txtPedidoDistancia.setText(PedidoDistancia);
                        txtPedidoTiempo.setText(PedidoTiempo);

                        /*
                        if (PedidoTiempo.equals("") || PedidoTiempo.equals("-") || PedidoTiempo == null || PedidoTiempo.equals("null") || PedidoTiempo.equals("0.00") || PedidoTiempo.equals("0")) {
                            capPedidoTiempo.setVisibility(View.GONE);
                        } else {
                            capPedidoTiempo.setVisibility(View.VISIBLE);
                        }

                        if (PedidoDistancia.equals("") || PedidoDistancia.equals("-") || PedidoDistancia == null || PedidoDistancia.equals("null") || PedidoDistancia.equals("0.00") || PedidoDistancia.equals("0")) {
                            capPedidoDistancia.setVisibility(View.GONE);
                        } else {
                            capPedidoDistancia.setVisibility(View.VISIBLE);
                        }
                        */

                    }

                }

/*
                if(!PedidoCoordenadaX.equals("") && !PedidoCoordenadaY.equals("")  && PedidoCoordenadaX !=null && PedidoCoordenadaY != null){

                    if(!VehiculoCoordenadaX.equals("") && !VehiculoCoordenadaY.equals("")  &&  VehiculoCoordenadaX !=null && VehiculoCoordenadaY != null){

                        LatLngBounds.Builder builder = new LatLngBounds.Builder();

                        builder.include(new LatLng(Double.parseDouble(PedidoCoordenadaX),Double.parseDouble(PedidoCoordenadaY)));
                        builder.include(new LatLng(Double.parseDouble(VehiculoCoordenadaX),Double.parseDouble(VehiculoCoordenadaY)));

                        LatLngBounds bounds = builder.build();

                        int width = getResources().getDisplayMetrics().widthPixels;
                        int height = getResources().getDisplayMetrics().heightPixels;
                        int padding = (int) (width * 0.30); // offset from edges of the map 12% of screen

                        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);

                        googleMap.animateCamera(cu);

                    }else{

                        LatLng latLng = new LatLng(Double.parseDouble(PedidoCoordenadaX),Double.parseDouble(PedidoCoordenadaY));
                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(latLng)      // Sets the center of the map to Mountain View
                                .zoom(12)                   // Sets the zoom
                                //.bearing(20)  //era 90              // Sets the orientation of the camera to east
                                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                                .build();                   // Creates a CameraPosition from the builder
                        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                    }

                }else {

                    if(!VehiculoCoordenadaX.equals("") && !VehiculoCoordenadaY.equals("")  &&  VehiculoCoordenadaX !=null && VehiculoCoordenadaY != null) {

                        LatLng latLng = new LatLng(Double.parseDouble(VehiculoCoordenadaX),Double.parseDouble(VehiculoCoordenadaY));
                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(latLng)      // Sets the center of the map to Mountain View
                                .zoom(12)                   // Sets the zoom
                                //.bearing(20)  //era 90              // Sets the orientation of the camera to east
                                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                                .build();                   // Creates a CameraPosition from the builder
                        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                    }

                }

*/

                if (!VehiculoCoordenadaX.equals("") && !VehiculoCoordenadaY.equals("") && VehiculoCoordenadaX != null && VehiculoCoordenadaY != null && !VehiculoCoordenadaX.equals("0.00") && !VehiculoCoordenadaY.equals("0.00")) {

                    if (!PedidoCoordenadaX.equals("") && !PedidoCoordenadaY.equals("") && PedidoCoordenadaX != null && PedidoCoordenadaY != null && !PedidoCoordenadaX.equals("0.00") && !PedidoCoordenadaY.equals("0.00")) {

                        // Getting URL to the Google Directions API
                        String url = getDirectionsUrl(pedidoMarker.getPosition(), vehiculoMarker.getPosition());

                        DownloadTask downloadTask = new DownloadTask();
                        // Start downloading json data from Google Directions API
                        downloadTask.execute(url);

                    }
                }


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
            //FncMostrarToast(exception.toString());
            FncMostrarToast("Ha ocurrido un error cargando el mapa: " + exception.toString());

            Log.e("PedidoActual90", exception.toString());

        }

    }


    private boolean checkPermission(int permiso) {
        Log.e("PedidoActual10", "VERIFICAR PERMISO");
        boolean respuesta = false;

        int MyVersion = Build.VERSION.SDK_INT;

        if (MyVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {

            switch (permiso) {
                case 1:

                    int result1 = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);

                    if (result1 == PackageManager.PERMISSION_GRANTED) {
                        Log.e("PedidoActual10", "1AAA");
                        respuesta = true;
                    } else {
                        respuesta = false;
                        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, permiso);
                        Log.e("PedidoActual10", "1BBB");
                    }

                    break;

                case 2:

                    int result2 = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);

                    if (result2 == PackageManager.PERMISSION_GRANTED) {
                        Log.e("PedidoActual10", "2AAA");
                        respuesta = true;
                    } else {
                        respuesta = false;
                        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, permiso);
                        Log.e("PedidoActual10", "2BBB");
                    }

                    break;

                case 7:

                    int result7 = ContextCompat.checkSelfPermission(context, android.Manifest.permission.CALL_PHONE);

                    if (result7 == PackageManager.PERMISSION_GRANTED) {
                        Log.e("EsperandoTaxiLlegada10","3AAA");
                        respuesta = true;
                    }else {
                        respuesta = false;
                        ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.CALL_PHONE}, permiso);
                        Log.e("EsperandoTaxiLlegada10", "3BBB");
                    }
                    break;

                case 8:

                    int result8 = ContextCompat.checkSelfPermission(context, android.Manifest.permission.CALL_PHONE);

                    if (result8 == PackageManager.PERMISSION_GRANTED) {
                        Log.e("EsperandoTaxiLlegada10","3AAA");
                        respuesta = true;
                    }else {
                        respuesta = false;
                        ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.CALL_PHONE}, permiso);
                        Log.e("EsperandoTaxiLlegada10", "3BBB");
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

            Log.e("PedidoActual10", "PERMISO ACEPTADO");

            switch (requestCode) {
                case 1:
                    createMapView();
                    break;

                case 2:
                    MtdObtenerCoordenadas();
                    break;

                case 7:

                    FncLlamar();

                    break;


                case 8:

                    FncLlamarTelefono();

                    break;


            }

        } else {
            Log.e("PedidoActual10", "PERMISO NEGADO");
            FncMostrarToast("Permiso denegado, es posible que la aplicacion no funcione  correctamente.");
        }

    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){



            case R.id.FabPedidoActualUbicar:

                //if(!CambioUbicacion) {

                //OBTENER COORDENADAS
                /*if(checkPermission(2)){
                    MtdObtenerCoordenadas();
                }
*/


                if(!VehiculoCoordenadaX.equals("") & !VehiculoCoordenadaY.equals("") & !VehiculoCoordenadaX.equals("0.00") & !VehiculoCoordenadaY.equals("0.00") & VehiculoCoordenadaX!=null & VehiculoCoordenadaY!=null){

                    if(googleMap!=null){

                        LatLng latLng = new LatLng(Double.parseDouble(VehiculoCoordenadaX), Double.parseDouble(VehiculoCoordenadaY));
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 18);
                        googleMap.animateCamera(cameraUpdate);

                        MapaCamara = 3;

                    }else{
                        Log.e("CambioUbicacion", "Google Map Error");
                    }

                }else{
                    FncMostrarToast("No se pudo obtener su ubicación");
                }

                break;

            case R.id.FabPedidoActualReducir:

                if(null != googleMap){
                    googleMap.animateCamera(CameraUpdateFactory.zoomOut());
                }

                break;

            case R.id.FabPedidoActualAumentar:


                if(null != googleMap){
                    googleMap.animateCamera(CameraUpdateFactory.zoomIn());
                }
                break;







        }
    }


    public void MtdObtenerCoordenadas() {

        //OBTENIENDO UBICACION
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //Toast.makeText(this, "GPS is Enabled in your devide", Toast.LENGTH_SHORT).show();

            PedidoActualListener mlocListener = new PedidoActualListener();
            mlocListener.setPedidoActualActivity(this);
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


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.pedido_actual, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_reportar_ubicacion) {

            Log.e("MsgPedidoTipo: ", PedidoTipo);


            if ("7".equals(PedidoTipo)) {


                AlertDialog.Builder MsgGeneral = new AlertDialog.Builder(this);
                MsgGeneral.setTitle(getString(R.string.app_titulo));
                MsgGeneral.setMessage("¿Realmente deseas reportar esta ubicación de cliente?");
                MsgGeneral.setCancelable(false);
                MsgGeneral.setPositiveButton("Aceptar",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                // Do nothing but close the dialog

                                //FncMostrarToast("Usted ha ACEPTADO el pedido");
                                final ProgressDialog PrgPedidoDetalle = new ProgressDialog(PedidoActualActivity.this);
                                PrgPedidoDetalle.setIcon(R.mipmap.ic_launcher);
                                PrgPedidoDetalle.setMessage("Cargando...");
                                PrgPedidoDetalle.setCancelable(false);
                                PrgPedidoDetalle.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                PrgPedidoDetalle.show();

                                Thread nt = new Thread() {
                                    @Override
                                    public void run() {

                                        try {

                                            final String resMtdClienteReportarUbicacion;
                                            resMtdClienteReportarUbicacion = MtdClienteReportarUbicacion(ClienteId, VehiculoCoordenadaX, VehiculoCoordenadaY, VehiculoUnidad);

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {

                                                    String JsRespuesta = "";

                                                    try {

                                                        JSONObject jsonObject = new JSONObject(resMtdClienteReportarUbicacion);
                                                        JsRespuesta = jsonObject.getString("Respuesta");

                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }

                                                    PrgPedidoDetalle.cancel();

                                                    switch (JsRespuesta) {
                                                        case "C037":
                                                            //FncMostrarToast("Ubicacion reportada correctamente.");
                                                            FncMostrarMensaje("Ubicacion reportada correctamente.", false);
                                                            break;

                                                        case "C038":
                                                            FncMostrarToast("Ha ocurrido un error reportando la ubicacion, intente nuevamente.");
                                                            break;

                                                        case "C039":
                                                            FncMostrarToast("Cliente no identificado.");
                                                            break;

                                                        case "C040":
                                                            FncMostrarMensaje("No tienes permisos para realizar esta acción", false);
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

                            }
                        });

                MsgGeneral.setNegativeButton("Cancelar",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                // Do nothing but close the dialog

                            }
                        });

                // Remember, create doesn't show the dialog
                AlertDialog msgGeneral = MsgGeneral.create();
                msgGeneral.show();


            } else {

                FncMostrarMensaje("No se puede registrar ubicación a este tipo de cliente", false);
            }
            return true;

        } else if(id==R.id.action_emergencia){

            final ProgressDialog prgPedidoActual = new ProgressDialog(this);
            prgPedidoActual.setIcon(R.mipmap.ic_launcher);
            prgPedidoActual.setMessage("Cargando...");
            prgPedidoActual.setCancelable(false);
            prgPedidoActual.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            prgPedidoActual.show();

            Thread nt = new Thread() {
                @Override
                public void run() {

                    try {

                        final String resRegistrarConductorAlerta;
                        resRegistrarConductorAlerta = MtdRegistrarConductorAlerta(ConductorId, ConductorNumeroDocumento, ConductorNombre, VehiculoUnidad, VehiculoPlaca, "1", VehiculoCoordenadaX, VehiculoCoordenadaY, RegionId);

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

                                prgPedidoActual.cancel();


                                switch (JSRespuesta) {
                                    case "A004":

                                        MediaPlayer player = MediaPlayer.create(PedidoActualActivity.this, R.raw.sou_boton1);
                                        player = MediaPlayer.create(PedidoActualActivity.this, R.raw.sou_boton1);
                                        player.start();

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
                        Log.e("MsgPedidoActual18", e.toString());
                    }

                }

            };
            nt.start();

        return true;

        }


        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_salir_cuenta) {

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

                            Intent intentMainSesion = new Intent(PedidoActualActivity.this, MainActivity.class);
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

        Toast.makeText(PedidoActualActivity.this, oMensaje,
                Toast.LENGTH_LONG).show();
    }


    protected void displayNotificationOne(String oTitulo,String oContenido,String oContenidoGrande) {

        int notificationId = 001;

        // Build intent for notification content
        Intent viewIntent = new Intent(PedidoActualActivity.this, MainActivity.class);
        //viewIntent.putExtra(EXTRA_EVENT_ID, eventId);
        PendingIntent viewPendingIntent =
                PendingIntent.getActivity(PedidoActualActivity.this, 0, viewIntent, 0);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(PedidoActualActivity.this)
                        .setSmallIcon(policar.policarappv6.R.mipmap.ic_launcher)
                        .setContentTitle(oTitulo)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(oContenidoGrande))
                        .setContentText(oContenido)
                        //.setVibrate(new long[] { 100, 250 })
                        .setDefaults(Notification.DEFAULT_SOUND).setContentIntent(viewPendingIntent)
                ;

        //.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000}).build();

        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(PedidoActualActivity.this);

        notificationManager.notify(notificationId, notificationBuilder.build());

    }



    /*
    * NAVEGACION
     */



    public void onClick_BtnPedidoActualUbicar(View v){

        if(MapaCamara == 1){

            MapaCamara = 2;

            if(!PedidoCoordenadaX.equals("") && !PedidoCoordenadaY.equals("")  && PedidoCoordenadaX !=null && PedidoCoordenadaY != null){

               /* if(pedidoMarker!=null){
                    pedidoMarker.remove();
                }

                pedidoMarker = googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(Double.parseDouble(PedidoCoordenadaX), Double.parseDouble(PedidoCoordenadaY)))
                        .title(ClienteNombre)
                        .draggable(false)
                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_pedido150)));*/

                    LatLng latLng = new LatLng(Double.parseDouble(PedidoCoordenadaX),Double.parseDouble(PedidoCoordenadaY));
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(latLng)      // Sets the center of the map to Mountain View
                            .zoom(18)                   // Sets the zoom
                            //.bearing(90)                // Sets the orientation of the camera to east
                            .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                            .build();                   // Creates a CameraPosition from the builder
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            }


        }else if(MapaCamara==2){

            MapaCamara = 3;

            if(!VehiculoCoordenadaX.equals("") && !VehiculoCoordenadaY.equals("") &&  VehiculoCoordenadaX !=null && VehiculoCoordenadaY != null) {

                LatLng latLng = new LatLng(Double.parseDouble(VehiculoCoordenadaX),Double.parseDouble(VehiculoCoordenadaY));
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(latLng)      // Sets the center of the map to Mountain View
                        .zoom(18)                   // Sets the zoom
                        //.bearing(90)                // Sets the orientation of the camera to east
                        .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                        .build();                   // Creates a CameraPosition from the builder
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            }

        }else {

            MapaCamara = 1;

            if(!PedidoCoordenadaX.equals("") && !PedidoCoordenadaY.equals("") && PedidoCoordenadaX !=null && PedidoCoordenadaY != null){

                   if(!VehiculoCoordenadaX.equals("") && !VehiculoCoordenadaY.equals("") &&  VehiculoCoordenadaX !=null && VehiculoCoordenadaY != null){

                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    builder.include(new LatLng(Double.parseDouble(PedidoCoordenadaX),Double.parseDouble(PedidoCoordenadaY)));
                    builder.include(new LatLng(Double.parseDouble(VehiculoCoordenadaX),Double.parseDouble(VehiculoCoordenadaY)));
                    LatLngBounds bounds = builder.build();

                    // begin new code:
                    int width = getResources().getDisplayMetrics().widthPixels;
                    int height = getResources().getDisplayMetrics().heightPixels;

                    //   height = MapaAltura;
                    //int padding = (int) (width * 0.1); // offset from edges of the map 12% of screen
                       int padding = (int) (height * 0.1); // offset from edges of the map 12% of screen

                    //CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
                       CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
                       googleMap.animateCamera(cu);


                    /*   LatLng latLng = new LatLng(Double.parseDouble(CamaraCoordenadaX),Double.parseDouble(CamaraCoordenadaY));
                       CameraPosition cameraPosition = new CameraPosition.Builder()
                               .target(latLng)      // Sets the center of the map to Mountain View
                               .zoom(19)                   // Sets the zoom
                               //.bearing(90)                // Sets the orientation of the camera to east
                               .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                               .build();                   // Creates a CameraPosition from the builder
                       googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));*/


                }

            }


        }



       /* if(!PedidoCoordenadaX.equals("") && !PedidoCoordenadaY.equals("") && !PedidoCoordenadaX.equals("0.00") && !PedidoCoordenadaY.equals("0.00") && PedidoCoordenadaX !=null && PedidoCoordenadaY != null){

            if(pedidoMarker!=null){
                pedidoMarker.remove();
            }

            pedidoMarker = googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(Double.parseDouble(PedidoCoordenadaX), Double.parseDouble(PedidoCoordenadaY)))
                    .title(ClienteNombre)
                    .draggable(false)
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_pedido150)));


            if(!VehiculoCoordenadaX.equals("") && !VehiculoCoordenadaY.equals("") && !VehiculoCoordenadaX.equals("0.00") && !VehiculoCoordenadaY.equals("0.00") &&  VehiculoCoordenadaX !=null && VehiculoCoordenadaY != null){

                LatLngBounds.Builder builder = new LatLngBounds.Builder();

                builder.include(new LatLng(Double.parseDouble(PedidoCoordenadaX),Double.parseDouble(PedidoCoordenadaY)));

                //if(!VehiculoCoordenadaX.equals("") && !VehiculoCoordenadaY.equals("") && !VehiculoCoordenadaX.equals("0.00") && !VehiculoCoordenadaY.equals("0.00") &&  VehiculoCoordenadaX !=null && VehiculoCoordenadaY != null){
                builder.include(new LatLng(Double.parseDouble(VehiculoCoordenadaX),Double.parseDouble(VehiculoCoordenadaY)));
                //}

                LatLngBounds bounds = builder.build();

// begin new code:
                int width = getResources().getDisplayMetrics().widthPixels;
                int height = getResources().getDisplayMetrics().heightPixels;
                int padding = (int) (width * 0.2); // offset from edges of the map 12% of screen

                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);

                googleMap.animateCamera(cu);

            }else{

                LatLng latLng = new LatLng(Double.parseDouble(PedidoCoordenadaX),Double.parseDouble(PedidoCoordenadaY));
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(latLng)      // Sets the center of the map to Mountain View
                        .zoom(19)                   // Sets the zoom
                        //.bearing(90)                // Sets the orientation of the camera to east
                        .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                        .build();                   // Creates a CameraPosition from the builder
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            }

        }else {

            FncMostrarMensaje("La funcion UBICAR no esta disponible para este pedido",false);

        }*/



    }

    public void onClick_BtnPedidoActualCancelar(View v){

        android.app.AlertDialog.Builder MsgPedidoActual = new android.app.AlertDialog.Builder(this);
        MsgPedidoActual.setTitle(getString(R.string.alert_title));
        MsgPedidoActual.setMessage("¿Realmente desea cancelar el pedido?");
        MsgPedidoActual.setPositiveButton("Si",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                        
                        FncMostrarToast("¡Pedido Cancelado!");

                        if(croTardanza!=null){
                            croTardanza.stop();
                        }

                        SharedPreferences sharedPreferences = getSharedPreferences("policar.policarappv6", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("ConductorOcupado",2);
                        editor.putBoolean("ConductorTienePedido", false);
                        editor.apply();

                        Intent intentPedidoCancelacion = new Intent(PedidoActualActivity.this, PedidoCancelacionActivity.class);
                        Bundle bundlePedidoCancelacion = new Bundle();

                        bundlePedidoCancelacion.putString("PedidoId", PedidoId);

                        intentPedidoCancelacion.putExtras(bundlePedidoCancelacion);//Put your id to your next Intent
                        startActivity(intentPedidoCancelacion);
                        finish();

                    }
                });

        MsgPedidoActual.setNegativeButton("No",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                        
                        FncMostrarToast("¡Pedido aun en Curso!");
                    }
                });
        

        // Remember, create doesn't show the dialog
        android.app.AlertDialog msgPedidoActual = MsgPedidoActual.create();
        msgPedidoActual.show();

    }

    public void onClick_BtnPedidoActualAlertar(View v){


        //      if(!PedidoCoordenadaX.equals("") && !PedidoCoordenadaY.equals("") && !PedidoCoordenadaX.equals("0.00") && !PedidoCoordenadaY.equals("0.00") && PedidoCoordenadaX !=null && PedidoCoordenadaY != null){



        // if(estaConectado(true)){

        final ProgressDialog prgPedidoActual = new ProgressDialog(PedidoActualActivity.this);
        prgPedidoActual.setIcon(R.mipmap.ic_launcher);
        prgPedidoActual.setMessage("Cargando...");
        prgPedidoActual.setCancelable(false);
        prgPedidoActual.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        prgPedidoActual.show();

        Thread nt = new Thread() {
            @Override
            public void run() {

                try {

                    final String resAlertarPedido;

                    resAlertarPedido = MtdAlertarPedido(PedidoId);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            String JsRespuesta = "";

                            try {

                                JSONObject jsonObject = new JSONObject(resAlertarPedido);
                                JsRespuesta = jsonObject.getString("Respuesta");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            prgPedidoActual.cancel();

                            switch(JsRespuesta){

                                case "P021":
FncMostrarToast("¡Alerta Enviada!");
                                    MediaPlayer player = MediaPlayer.create(PedidoActualActivity.this,R.raw.sou_taxi_tono);
                                    player = MediaPlayer.create(PedidoActualActivity.this, R.raw.sou_taxi_tono);
                                    player.start();


                                    break;
                                case "P022":

                                    android.app.AlertDialog.Builder MsgPedidoActual22 = new android.app.AlertDialog.Builder(PedidoActualActivity.this);
                                    MsgPedidoActual22.setTitle(getString(R.string.alert_title));
                                    MsgPedidoActual22.setMessage("No se ha podido enviar la alerta, comuniquese con la central. Codigo de error P022");
                                    MsgPedidoActual22.setPositiveButton("Aceptar",
                                            new DialogInterface.OnClickListener() {

                                                public void onClick(DialogInterface dialog, int which) {
                                                    // Do nothing but close the dialog

                                                }
                                            });

                                    // Remember, create doesn't show the dialog
                                    android.app.AlertDialog msgPedidoActual22 = MsgPedidoActual22.create();
                                    msgPedidoActual22.show();

                                    break;

                                case "P023":

                                    android.app.AlertDialog.Builder MsgPedidoActual23 = new android.app.AlertDialog.Builder(PedidoActualActivity.this);
                                    MsgPedidoActual23.setTitle(getString(R.string.alert_title));
                                    MsgPedidoActual23.setMessage("No se ha podido enviar la alerta el estado del pedido ha cambiado, comuniquese con la central. Codigo de error P023");
                                    MsgPedidoActual23.setPositiveButton("Aceptar",
                                            new DialogInterface.OnClickListener() {

                                                public void onClick(DialogInterface dialog, int which) {
                                                    // Do nothing but close the dialog

                                                }
                                            });

                                    // Remember, create doesn't show the dialog
                                    android.app.AlertDialog msgPedidoActual23 = MsgPedidoActual23.create();
                                    msgPedidoActual23.show();

                                    break;

                                default://NO ES CRITICO
                                    FncMostrarToast(getString(R.string.message_error_interno));
                                    // FncMostrarMensaje(getString(R.string.message_error_interno),false);
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


        // }

/*
        }else{

            FncMostrarMensaje("La funcion ALERTAR no esta disponible para este pedido",false);
        }*/



    }

    public void onClick_BtnPedidoActualFinalizar(View v){

        //if(estaConectado(true)){

        android.app.AlertDialog.Builder MsgPedidoActual = new android.app.AlertDialog.Builder(this);
        MsgPedidoActual.setTitle(getString(R.string.alert_title));
        MsgPedidoActual.setMessage("¿El cliente se encuentra dentro de la unidad?");
        MsgPedidoActual.setPositiveButton("Si",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        final ProgressDialog prgPedidoActual = new ProgressDialog(PedidoActualActivity.this);
                        prgPedidoActual.setIcon(R.mipmap.ic_launcher);
                        prgPedidoActual.setMessage("Cargando...");
                        prgPedidoActual.setCancelable(false);
                        prgPedidoActual.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        prgPedidoActual.show();

                        Thread nt = new Thread() {
                            @Override
                            public void run() {

                                try {

                                    final String resAbordoPedido;
                                    resAbordoPedido = MtdAbordoPedido(PedidoId,ConductorId,VehiculoCoordenadaX,VehiculoCoordenadaY);

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {

                                            String JsRespuesta = "";

                                            try {

                                                JSONObject jsonObject = new JSONObject(resAbordoPedido);
                                                JsRespuesta = jsonObject.getString("Respuesta");

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                            prgPedidoActual.cancel();

                                            switch(JsRespuesta){

                                                case "P016":

                                                    FncMostrarToast("¡Pedido Finalizado!");

                                                    if(croTardanza!=null){
                                                        croTardanza.stop();
                                                    }

                                                    Intent intentIniciarViaje = new Intent(PedidoActualActivity.this, IniciarViajeActivity.class);
                                                    Bundle bundleIniciarViaje = new Bundle();

                                                    bundleIniciarViaje.putString("PedidoId", PedidoId);
                                                    bundleIniciarViaje.putString("VehiculoCoordenadaX", VehiculoCoordenadaX);
                                                    bundleIniciarViaje.putString("VehiculoCoordenadaY", VehiculoCoordenadaY);

                                                    bundleIniciarViaje.putString("MonedaSimbolo", MonedaSimbolo);
                                                    bundleIniciarViaje.putString("TarifaMinima", TarifaMinima);
                                                    bundleIniciarViaje.putString("TarifaKilometro", TarifaKilometro);
                                                    bundleIniciarViaje.putString("TarifaAdicionalNoche", TarifaAdicionalNoche);
                                                    bundleIniciarViaje.putString("TarifaAdicionalFestivo", TarifaAdicionalFestivo);
                                                    bundleIniciarViaje.putString("PedidoEsFestivo", PedidoEsFestivo);
                                                    bundleIniciarViaje.putString("PedidoEsNoche", PedidoEsNoche);

                                                    intentIniciarViaje.putExtras(bundleIniciarViaje);//Put your id to your next Intent
                                                    startActivity(intentIniciarViaje);
                                                    finish();

                                                    break;

                                                case "P017":

                                                    android.app.AlertDialog.Builder MsgPedidoActual17 = new android.app.AlertDialog.Builder(PedidoActualActivity.this);
                                                    MsgPedidoActual17.setTitle(getString(R.string.alert_title));
                                                    MsgPedidoActual17.setMessage("No se ha podido cambiar a abordo, comuniquese con la central. Codigo de error P017");
                                                    MsgPedidoActual17.setPositiveButton("Aceptar",
                                                            new DialogInterface.OnClickListener() {

                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    // Do nothing but close the dialog
                                                                        /*Intent intentNavegacion17 = new Intent(PedidoActualActivity.this, NavegacionActivity.class);
                                                                        startActivity(intentNavegacion17);
                                                                        finish();*/
                                                                }
                                                            });

                                                    // Remember, create doesn't show the dialog
                                                    android.app.AlertDialog msgPedidoActual17 = MsgPedidoActual17.create();
                                                    msgPedidoActual17.show();

                                                    break;

                                                case "P018":

                                                    android.app.AlertDialog.Builder MsgPedidoActual18 = new android.app.AlertDialog.Builder(PedidoActualActivity.this);
                                                    MsgPedidoActual18.setTitle(getString(R.string.alert_title));
                                                    MsgPedidoActual18.setMessage("No se ha podido cambiar a abordo el estado del pedido ha cambiado, comuniquese con la central. Codigo de error P018");
                                                    MsgPedidoActual18.setPositiveButton("Aceptar",
                                                            new DialogInterface.OnClickListener() {

                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    // Do nothing but close the dialog

                                                                }
                                                            });

                                                    // Remember, create doesn't show the dialog
                                                    android.app.AlertDialog msgPedidoActual18 = MsgPedidoActual18.create();
                                                    msgPedidoActual18.show();

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



                    }
                });

        MsgPedidoActual.setNegativeButton("No",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                        
                        FncMostrarToast("¡Pedido aun en Curso!");
                    }
                });

        // Remember, create doesn't show the dialog
        android.app.AlertDialog msgPedidoActual = MsgPedidoActual.create();
        msgPedidoActual.show();


        // }



    }



    public void onClick_BtnPedidoActualMensajear(View v) {


        //ACCIONES
        final CharSequence[] items = { "Ninguno","Estoy Afuera","Estoy en camino","Ok","Gracias" };

        // int intConductorCanal = Integer.parseInt(ConductorCanal);
        //   intConductorCanal = intConductorCanal -1;

        // Creating and Building the Dialog
        android.support.v7.app.AlertDialog.Builder MsgPedidoActual = new android.support.v7.app.AlertDialog.Builder(this);
        MsgPedidoActual.setTitle("Escoja un opcion");
        //MsgPedidoActual.setCancelable(false);
        MsgPedidoActual.setSingleChoiceItems(items,0,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {

                        // TextView TxtCanalActual = (TextView) findViewById(R.id.CmpCanalActual);

                        switch (item) {
                            case 0:


                                dialog.dismiss();

                                break;
                            case 1:

                                final ProgressDialog PrgPedidoActual = new ProgressDialog(PedidoActualActivity.this);
                                PrgPedidoActual.setIcon(policar.policarappv6.R.mipmap.ic_launcher);
                                PrgPedidoActual.setMessage("Cargando...");
                                PrgPedidoActual.setCancelable(false);
                                PrgPedidoActual.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                PrgPedidoActual.show();

                                //TAREA CANCELAR TAXI - INICIO
                                Thread ntPedidoActual1 = new Thread() {
                                    @Override
                                    public void run() {

                                        try {

                                            final String resPedidoActual;
                                            resPedidoActual = MtdEnviarPedidoMensajeCliente(PedidoId,"Estoy afuera");

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {



                                                    String JsRespuesta = "";
                                                    String JsPedidoId = "";

                                                    try {

                                                        JSONObject jsonObject = new JSONObject(resPedidoActual);
                                                        JsRespuesta = jsonObject.getString("Respuesta");

                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }

                                                    PrgPedidoActual.cancel();

                                                    switch (JsRespuesta) {
                                                        case "P084":
                                                           FncMostrarToast("¡Mensaje Enviado!");
                                                            break;

                                                        case "P085":
                                                            FncMostrarMensaje("No se ha podido enviar el mensaje, intente nuevamente", false);
                                                            break;
                                                        case "P086":

                                                            //FncMostrarMensaje("No se ha podido identificar el servicio, proceso cancelado", true);
                                                            android.app.AlertDialog.Builder MsgPedidoActual83 = new android.app.AlertDialog.Builder(PedidoActualActivity.this);
                                                            MsgPedidoActual83.setTitle(getString(policar.policarappv6.R.string.alert_title));
                                                            MsgPedidoActual83.setMessage("No se ha podido identificar el servicio, se reiniciara la aplicación");
                                                            MsgPedidoActual83.setCancelable(false);
                                                            MsgPedidoActual83.setPositiveButton("Aceptar",
                                                                    new DialogInterface.OnClickListener() {

                                                                        public void onClick(DialogInterface dialog, int which) {

                                                                            Intent intentMain = new Intent(PedidoActualActivity.this, MainActivity.class);
                                                                            startActivity(intentMain);
                                                                            finish();

                                                                        }
                                                                    });

                                                            // Remember, create doesn't show the dialog
                                                            android.app.AlertDialog msgPedidoActual83 = MsgPedidoActual83.create();
                                                            msgPedidoActual83.show();

                                                            break;

                                                        default:
                                                            FncMostrarToast(getString(policar.policarappv6.R.string.message_error_interno));
                                                            break;
                                                    }

                                                }
                                            });

                                        } catch (Exception e) {
                                            // TODO: handle exception
                                            // Log.e("CancelarTaxiMsg", e.toString());
                                        }


                                    }
                                };
                                ntPedidoActual1.start();

                                dialog.dismiss();

                                break;


                            case 2:

                                final ProgressDialog PrgPedidoActual2 = new ProgressDialog(PedidoActualActivity.this);
                                PrgPedidoActual2.setIcon(policar.policarappv6.R.mipmap.ic_launcher);
                                PrgPedidoActual2.setMessage("Cargando...");
                                PrgPedidoActual2.setCancelable(false);
                                PrgPedidoActual2.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                PrgPedidoActual2.show();

                                //TAREA CANCELAR TAXI - INICIO
                                Thread ntPedidoActual2 = new Thread() {
                                    @Override
                                    public void run() {

                                        try {

                                            final String resPedidoActual;
                                            resPedidoActual = MtdEnviarPedidoMensajeCliente(PedidoId,"Estoy en camino.");

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {



                                                    String JsRespuesta = "";
                                                    String JsPedidoId = "";

                                                    try {

                                                        JSONObject jsonObject = new JSONObject(resPedidoActual);
                                                        JsRespuesta = jsonObject.getString("Respuesta");

                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }

                                                    PrgPedidoActual2.cancel();

                                                    switch (JsRespuesta) {
                                                        case "P084":
                                                           FncMostrarToast("¡Mensaje Enviado!");
                                                            break;

                                                        case "P085":
                                                            FncMostrarMensaje("No se ha podido enviar el mensaje, intente nuevamente", false);
                                                            break;
                                                        case "P086":

                                                            //FncMostrarMensaje("No se ha podido identificar el servicio, proceso cancelado", true);
                                                            android.app.AlertDialog.Builder MsgPedidoActual83 = new android.app.AlertDialog.Builder(PedidoActualActivity.this);
                                                            MsgPedidoActual83.setTitle(getString(policar.policarappv6.R.string.alert_title));
                                                            MsgPedidoActual83.setMessage("No se ha podido identificar el servicio, se reiniciara la aplicación");
                                                            MsgPedidoActual83.setCancelable(false);
                                                            MsgPedidoActual83.setPositiveButton("Aceptar",
                                                                    new DialogInterface.OnClickListener() {

                                                                        public void onClick(DialogInterface dialog, int which) {

                                                                            //Intent intentMainSesion = new Intent(PedidoActualActivity.this, NavegacionActivity.class);
                                                                            //startActivity(intentMainSesion);
                                                                            //finish();

                                                                            Intent intentNavegacion = new Intent(PedidoActualActivity.this, NavegacionActivity.class);

                                                                            Bundle bundleNavegacion = new Bundle();
                                                                            bundleNavegacion.putString("PedidoMensaje", "");
                                                                            intentNavegacion.putExtras(bundleNavegacion); //Put your id to your next Intent

                                                                            startActivity(intentNavegacion);
                                                                            finish();




                                                                        }
                                                                    });

                                                            // Remember, create doesn't show the dialog
                                                            android.app.AlertDialog msgPedidoActual83 = MsgPedidoActual83.create();
                                                            msgPedidoActual83.show();

                                                            break;

                                                        default:
                                                            FncMostrarToast(getString(policar.policarappv6.R.string.message_error_interno));
                                                            break;
                                                    }

                                                }
                                            });

                                        } catch (Exception e) {
                                            // TODO: handle exception
                                            // Log.e("CancelarTaxiMsg", e.toString());
                                        }


                                    }
                                };
                                ntPedidoActual2.start();

                                dialog.dismiss();

                                break;

                            case 3:

                                final ProgressDialog PrgPedidoActual3 = new ProgressDialog(PedidoActualActivity.this);
                                PrgPedidoActual3.setIcon(policar.policarappv6.R.mipmap.ic_launcher);
                                PrgPedidoActual3.setMessage("Cargando...");
                                PrgPedidoActual3.setCancelable(false);
                                PrgPedidoActual3.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                PrgPedidoActual3.show();

                                //TAREA CANCELAR TAXI - INICIO
                                Thread ntPedidoActual3 = new Thread() {
                                    @Override
                                    public void run() {

                                        try {

                                            final String resPedidoActual;
                                            resPedidoActual = MtdEnviarPedidoMensajeCliente(PedidoId,"Ok");

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {



                                                    String JsRespuesta = "";
                                                    String JsPedidoId = "";

                                                    try {

                                                        JSONObject jsonObject = new JSONObject(resPedidoActual);
                                                        JsRespuesta = jsonObject.getString("Respuesta");

                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }

                                                    PrgPedidoActual3.cancel();

                                                    switch (JsRespuesta) {
                                                        case "P084":
                                                           FncMostrarToast("¡Mensaje Enviado!");
                                                            break;

                                                        case "P085":
                                                            FncMostrarMensaje("No se ha podido enviar el mensaje, intente nuevamente", false);
                                                            break;
                                                        case "P086":

                                                            //FncMostrarMensaje("No se ha podido identificar el servicio, proceso cancelado", true);
                                                            android.app.AlertDialog.Builder MsgPedidoActual83 = new android.app.AlertDialog.Builder(PedidoActualActivity.this);
                                                            MsgPedidoActual83.setTitle(getString(policar.policarappv6.R.string.alert_title));
                                                            MsgPedidoActual83.setMessage("No se ha podido identificar el servicio, se reiniciara la aplicación");
                                                            MsgPedidoActual83.setCancelable(false);
                                                            MsgPedidoActual83.setPositiveButton("Aceptar",
                                                                    new DialogInterface.OnClickListener() {

                                                                        public void onClick(DialogInterface dialog, int which) {

                                                                            Intent intentMain = new Intent(PedidoActualActivity.this, MainActivity.class);
                                                                            startActivity(intentMain);
                                                                            finish();

                                                                        }
                                                                    });

                                                            // Remember, create doesn't show the dialog
                                                            android.app.AlertDialog msgPedidoActual83 = MsgPedidoActual83.create();
                                                            msgPedidoActual83.show();

                                                            break;

                                                        default:
                                                            FncMostrarToast(getString(policar.policarappv6.R.string.message_error_interno));
                                                            break;
                                                    }

                                                }
                                            });

                                        } catch (Exception e) {
                                            // TODO: handle exception
                                            // Log.e("CancelarTaxiMsg", e.toString());
                                        }


                                    }
                                };
                                ntPedidoActual3.start();

                                dialog.dismiss();

                                break;

                            case 4:

                                final ProgressDialog PrgPedidoActual4 = new ProgressDialog(PedidoActualActivity.this);
                                PrgPedidoActual4.setIcon(policar.policarappv6.R.mipmap.ic_launcher);
                                PrgPedidoActual4.setMessage("Cargando...");
                                PrgPedidoActual4.setCancelable(false);
                                PrgPedidoActual4.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                PrgPedidoActual4.show();

                                //TAREA CANCELAR TAXI - INICIO
                                Thread ntPedidoActual4 = new Thread() {
                                    @Override
                                    public void run() {

                                        try {

                                            final String resPedidoActual;
                                            resPedidoActual = MtdEnviarPedidoMensajeCliente(PedidoId,"Gracias");

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {



                                                    String JsRespuesta = "";
                                                    String JsPedidoId = "";

                                                    try {

                                                        JSONObject jsonObject = new JSONObject(resPedidoActual);
                                                        JsRespuesta = jsonObject.getString("Respuesta");

                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }

                                                    PrgPedidoActual4.cancel();

                                                    switch (JsRespuesta) {
                                                        case "P084":
                                                           FncMostrarToast("¡Mensaje Enviado!");
                                                            break;

                                                        case "P085":
                                                            FncMostrarMensaje("No se ha podido enviar el mensaje, intente nuevamente", false);
                                                            break;
                                                        case "P086":

                                                            //FncMostrarMensaje("No se ha podido identificar el servicio, proceso cancelado", true);
                                                            android.app.AlertDialog.Builder MsgPedidoActual83 = new android.app.AlertDialog.Builder(PedidoActualActivity.this);
                                                            MsgPedidoActual83.setTitle(getString(policar.policarappv6.R.string.alert_title));
                                                            MsgPedidoActual83.setMessage("No se ha podido identificar el servicio, se reiniciara la aplicación");
                                                            MsgPedidoActual83.setCancelable(false);
                                                            MsgPedidoActual83.setPositiveButton("Aceptar",
                                                                    new DialogInterface.OnClickListener() {

                                                                        public void onClick(DialogInterface dialog, int which) {

                                                                            //Intent intentMainSesion = new Intent(PedidoActualActivity.this, NavegacionActivity.class);
                                                                            //startActivity(intentMainSesion);
                                                                            //finish();
                                                                            Intent intentNavegacion = new Intent(PedidoActualActivity.this, NavegacionActivity.class);

                                                                            Bundle bundleNavegacion = new Bundle();
                                                                            bundleNavegacion.putString("PedidoMensaje", "");
                                                                            intentNavegacion.putExtras(bundleNavegacion); //Put your id to your next Intent

                                                                            startActivity(intentNavegacion);
                                                                            finish();


                                                                        }
                                                                    });

                                                            // Remember, create doesn't show the dialog
                                                            android.app.AlertDialog msgPedidoActual83 = MsgPedidoActual83.create();
                                                            msgPedidoActual83.show();

                                                            break;

                                                        default:
                                                            FncMostrarToast(getString(policar.policarappv6.R.string.message_error_interno));
                                                            break;
                                                    }

                                                }
                                            });

                                        } catch (Exception e) {
                                            // TODO: handle exception
                                            // Log.e("CancelarTaxiMsg", e.toString());
                                        }


                                    }
                                };
                                ntPedidoActual4.start();

                                dialog.dismiss();

                                break;

                        }

                    }
                });
        //levelDialog = MsgPedidoActual.create();
        //levelDialog.show();

        android.support.v7.app.AlertDialog msgNavegacion = MsgPedidoActual.create();
        msgNavegacion.show();


    }




    public void FncLlamar(){

        try {

            FncMostrarToast("Iniciando Llamada");

            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + ClienteCelular));
            startActivity(callIntent);
        } catch (Exception e) {
            FncMostrarToast("Ha ocurrido un error interno");
            //Log.e("helloandroid dialing example", "Call failed", e);
        }


    }

    public void FncLlamarTelefono(){

        try {

            FncMostrarToast("Iniciando Llamada");

            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + ClienteTelefono));
            startActivity(callIntent);
        } catch (Exception e) {
            FncMostrarToast("Ha ocurrido un error interno");
            //Log.e("helloandroid dialing example", "Call failed", e);
        }


    }


    public void onClick_BtnPedidoActualLlamar(View v){

        if(!ClienteCelular.equals("")) {

            if(checkPermission(7)){
                FncLlamar();
            }

            //FncLlamarNumero(ConductorCelular);
        }else if(!ClienteTelefono.equals("")){

            if(checkPermission(8)){
                FncLlamarTelefono();
            }

        }else{

            FncMostrarMensaje("No se encontró número de celular del cliente",false);

        }


    }




         /*
    ENVIO DE VARIABLES
     */

    public String MtdAbordoPedido(String oPedidoId, String oConductorId, String oVehiculoCoordenadaX,String oVehiculoCoordenadaY) {


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


            HashMap<String, String> postDataParams=new HashMap<>();
            postDataParams.put("PedidoId", oPedidoId);
            postDataParams.put("ConductorId", oConductorId);
            postDataParams.put("VehiculoCoordenadaX", oVehiculoCoordenadaX);
            postDataParams.put("VehiculoCoordenadaY", oVehiculoCoordenadaY);

            postDataParams.put("Identificador", Identificador);
            postDataParams.put("Accion", "AbordoPedido");

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();
            int responseCode=conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line=br.readLine()) != null) {
                    response+=line;
                }
            }
            else {
                response="";

            }

            Log.e("MsgPedidoActual15", response);

        } catch (Exception e) {

            Log.e("MsgPedidoActual16", e.toString());
            e.printStackTrace();
        }

        return response;


    }

    public String MtdAlertarPedido(String oPedidoId) {


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
            postDataParams.put("PedidoId", oPedidoId);

            postDataParams.put("Identificador", Identificador);
            postDataParams.put("Accion", "AlertarPedido");

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();
            int responseCode=conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line=br.readLine()) != null) {
                    response+=line;
                }
            }
            else {
                response="";

            }

            Log.e("MsgPedidoActual13", response);

        } catch (Exception e) {

            Log.e("MsgPedidoActual14", e.toString());
            e.printStackTrace();
        }

        return response;

    }






    public void onClick_BtnPedidoActualAccionar(View v) {


        //ACCIONES
        final CharSequence[] items = { "Ninguno","Pedido Recogido" };

        // int intConductorCanal = Integer.parseInt(ConductorCanal);
        //   intConductorCanal = intConductorCanal -1;

        // Creating and Building the Dialog
        android.support.v7.app.AlertDialog.Builder MsgPedidoActual = new android.support.v7.app.AlertDialog.Builder(this);
        MsgPedidoActual.setTitle("Escoja un opcion");
        MsgPedidoActual.setCancelable(true);
        MsgPedidoActual.setSingleChoiceItems(items,0,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {

                        // TextView TxtCanalActual = (TextView) findViewById(R.id.CmpCanalActual);

                        switch (item) {
                            case 0:

                                dialog.dismiss();

                                break;
                            case 1:

                                final ProgressDialog PrgPedidoActual = new ProgressDialog(PedidoActualActivity.this);
                                PrgPedidoActual.setIcon(policar.policarappv6.R.mipmap.ic_launcher);
                                PrgPedidoActual.setMessage("Cargando...");
                                PrgPedidoActual.setCancelable(false);
                                PrgPedidoActual.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                PrgPedidoActual.show();

                                //TAREA CANCELAR TAXI - INICIO
                                Thread ntPedidoActual1 = new Thread() {
                                    @Override
                                    public void run() {

                                        try {

                                            final String resMtdEnviarPedidoTipoAccionEstado;
                                            resMtdEnviarPedidoTipoAccionEstado = MtdEnviarPedidoTipoAccionEstado(PedidoId,"2");

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {



                                                    String JsRespuesta = "";
                                                    String JsPedidoId = "";

                                                    try {

                                                        JSONObject jsonObject = new JSONObject(resMtdEnviarPedidoTipoAccionEstado);
                                                        JsRespuesta = jsonObject.getString("Respuesta");

                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }

                                                    PrgPedidoActual.cancel();

                                                    switch (JsRespuesta) {
                                                        case "C040":
                                                            FncMostrarToast("¡Notificacion enviada!");
                                                            break;

                                                        case "C041":
                                                            FncMostrarMensaje("No se ha podido enviar la notificacion, intente nuevamente", false);
                                                            break;
                                                        case "C042":

                                                            //FncMostrarMensaje("No se ha podido identificar el servicio, proceso cancelado", true);
                                                            android.app.AlertDialog.Builder MsgPedidoActual83 = new android.app.AlertDialog.Builder(PedidoActualActivity.this);
                                                            MsgPedidoActual83.setTitle(getString(policar.policarappv6.R.string.alert_title));
                                                            MsgPedidoActual83.setMessage("No se ha podido identificar el servicio, se reiniciara la aplicación");
                                                            MsgPedidoActual83.setCancelable(false);
                                                            MsgPedidoActual83.setPositiveButton("Aceptar",
                                                                    new DialogInterface.OnClickListener() {

                                                                        public void onClick(DialogInterface dialog, int which) {

                                                                            Intent intentMain = new Intent(PedidoActualActivity.this, MainActivity.class);
                                                                            startActivity(intentMain);
                                                                            finish();

                                                                        }
                                                                    });

                                                            // Remember, create doesn't show the dialog
                                                            android.app.AlertDialog msgPedidoActual83 = MsgPedidoActual83.create();
                                                            msgPedidoActual83.show();

                                                            break;

                                                        default:
                                                            FncMostrarToast(getString(policar.policarappv6.R.string.message_error_interno));
                                                            break;
                                                    }

                                                }
                                            });

                                        } catch (Exception e) {
                                            // TODO: handle exception
                                            // Log.e("CancelarTaxiMsg", e.toString());
                                        }


                                    }
                                };
                                ntPedidoActual1.start();

                                dialog.dismiss();

                                break;

                        }

                    }
                });
        //levelDialog = MsgPedidoActual.create();
        //levelDialog.show();

        android.support.v7.app.AlertDialog msgNavegacion = MsgPedidoActual.create();
        msgNavegacion.show();


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

            Log.e("MsgPedidoActual11", response);

        } catch (Exception e) {

            Log.e("MsgPedidoActual12", e.toString());
            e.printStackTrace();
        }

        return response;

    }


    public String MtdObtenerPedido(String oPedidoId) {

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
            postDataParams.put("PedidoId", oPedidoId);

            postDataParams.put("Identificador", Identificador);
            postDataParams.put("Accion", "ObtenerPedido");

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

            Log.e("MsgPedidoActual9", response);

        } catch (Exception e) {

            Log.e("MsgPedidoActual10", e.toString());
            e.printStackTrace();
        }

        return response;

    }



/*
ENVIO DE VARIABLES
*/

   /* public String postEnviarPedidoAceptar(String oPedidoId , String oConductorId,String oVehiculoCoordenadaX,String oVehiculoCoordenadaY,String oAppVersion) {

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
            postDataParams.put("PedidoId", oPedidoId);
            postDataParams.put("ConductorId", oConductorId);

            postDataParams.put("VehiculoCoordenadaX", oVehiculoCoordenadaX);
            postDataParams.put("VehiculoCoordenadaY", oVehiculoCoordenadaY);
            postDataParams.put("AppVersion", oAppVersion);

            postDataParams.put("Identificador", Identificador);
            postDataParams.put("Accion", "AceptarPedido");

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

            Log.e("MsgPedidoActual7", response);

        } catch (Exception e) {

            Log.e("MsgPedidoActual8", e.toString());
            e.printStackTrace();
        }

        return response;

    }*/


    /*
    public String MtdObtenerRadioMensajes(String oConductorId,String oRadioMensajeCanal) {

        URL url;
        String response = "";

        try {

            url = new URL(getString(R.string.app_url)+"/webservice/JnRadioMensaje.php");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            HashMap<String, String>postDataParams=new HashMap<>();
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
            BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while ((line=br.readLine()) != null) {
                response+=line;
            }

            Log.e("MsgPedidoActual5", response);

        } catch (Exception e) {

            Log.e("MsgPedidoActual6", e.toString());
            e.printStackTrace();
        }

        return response;

    }
    */



    public String MtdEnviarPedidoMensajeCliente(String oPedidoId,String oMensaje) {

        URL url;
        String response = "";

        try {

            url = new URL(getString(policar.policarappv6.R.string.app_url)+"/webservice/JnPedido.php");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            HashMap<String, String> postDataParams=new HashMap<>();

            postDataParams.put("PedidoId", oPedidoId);
            postDataParams.put("PedidoMensajeCliente", oMensaje);

            postDataParams.put("Identificador", Identificador);
            postDataParams.put("ClienteAppVersion", getString(policar.policarappv6.R.string.app_version));
            postDataParams.put("Accion", "EnviarPedidoMensajeCliente");

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

            Log.e("MsgPedidoActual3", response);

        } catch (Exception e) {

            Log.e("MsgPedidoActual4", e.toString());
            e.printStackTrace();
        }

        return response;


    }

    public String MtdVerificarPedidoMensajeConductor(String oPedidoId) {

        URL url;
        String response = "";

        try {

            url = new URL(getString(policar.policarappv6.R.string.app_url)+"/webservice/JnPedido.php");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            HashMap<String, String> postDataParams=new HashMap<>();

            postDataParams.put("PedidoId", oPedidoId);

            postDataParams.put("Identificador", Identificador);
            postDataParams.put("ConductorAppVersion", getString(policar.policarappv6.R.string.app_version));
            postDataParams.put("Accion", "VerificarPedidoMensajeConductor");

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

            Log.e("MsgPedidoActual1", response);

        } catch (Exception e) {

            Log.e("MsgPedidoActual2", e.toString());
            e.printStackTrace();
        }

        return response;


    }



    public String MtdEnviarPedidoUbicacion(String oPedidoId,String oPedidoCoordenadaX,String oPedidoCoordenadaY,String oVehiculoCoordenadaX,String oVehiculoCoordenadaY) {

        URL url;
        String response = "";

        try {

            url = new URL(getString(policar.policarappv6.R.string.app_url)+"/webservice/JnPedido.php");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            HashMap<String, String> postDataParams=new HashMap<>();

            postDataParams.put("PedidoId", oPedidoId);
            postDataParams.put("PedidoCoordenadaX", oPedidoCoordenadaX);
            postDataParams.put("PedidoCoordenadaY", oPedidoCoordenadaY);
            postDataParams.put("VehiculoCoordenadaX", oVehiculoCoordenadaX);
            postDataParams.put("VehiculoCoordenadaY", oVehiculoCoordenadaY);


            postDataParams.put("Identificador", Identificador);
            postDataParams.put("ConductorAppVersion", getString(policar.policarappv6.R.string.app_version));
            postDataParams.put("Accion", "EnviarUbicacion");

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

            Log.e("MsgPedidoActual1", response);

        } catch (Exception e) {

            Log.e("MsgPedidoActual2", e.toString());
            e.printStackTrace();
        }

        return response;


    }

    public String MtdClienteReportarUbicacion(String oClienteId,String oClienteCoordenadaX,String oClienteCoordenadaY,String oVehiculoUnidad) {

        URL url;
        String response = "";

        try {

            url = new URL(getString(policar.policarappv6.R.string.app_url)+"/webservice/"+getString(policar.policarappv6.R.string.webservice_jnconductor));

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            HashMap<String, String> postDataParams=new HashMap<>();

            postDataParams.put("ClienteId", oClienteId);
            postDataParams.put("ClienteCoordenadaX", oClienteCoordenadaX);
            postDataParams.put("ClienteCoordenadaY", oClienteCoordenadaY);
            postDataParams.put("VehiculoUnidad", oVehiculoUnidad);

            postDataParams.put("Identificador", Identificador);
            postDataParams.put("ConductorAppVersion", getString(policar.policarappv6.R.string.app_version));
            postDataParams.put("Accion", "ReportarClienteUbicacion");

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

            Log.e("MsgPedidoActual1", response);

        } catch (Exception e) {

            Log.e("MsgPedidoActual2", e.toString());
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
            postDataParams.put("AppVersion", getString(R.string.app_version));
            postDataParams.put("Identificador", Identificador);

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

            Log.e("MsgPedidoActual15", response);

        } catch (Exception e) {

            Log.e("MsgPedidoActual16", e.toString());
            e.printStackTrace();
        }

        return response;

    }


    public String MtdEnviarPedidoTipoAccionEstado( String oPedidoId, String oPedidoTipoAccionEstado) {

        URL url;
        String response = "";

        try {

            url = new URL(getString(R.string.app_url) + "/webservice/"+getString(R.string.webservice_jnconductor));

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            HashMap<String, String> postDataParams = new HashMap<>();
            postDataParams.put("PedidoId", oPedidoId);
            postDataParams.put("PedidoTipoAccionEstado", oPedidoTipoAccionEstado);

            postDataParams.put("AppVersion", getString(R.string.app_version));
            postDataParams.put("Identificador", Identificador);

            postDataParams.put("Accion", "ActualizarPedidoTipoAccionEstado");

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

            Log.e("MsgPedidoActual25", response);

        } catch (Exception e) {

            Log.e("MsgPedidoActual26", e.toString());
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






    private String getDirectionsUrl(LatLng origin,LatLng dest){

        // Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;

        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service

        String parameters = str_origin+"&"+str_dest+"&"+sensor+ "&key=" + getString(R.string.app_google_apikey);

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;

        Log.e("ErrorRuta5:",url);

        return url;
    }

    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while( ( line = br.readLine()) != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Exception while dow url", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    /** A class to parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

            // Traversing through all the routes
            for(int i=0;i<result.size();i++){
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for(int j=0;j<path.size();j++){
                    HashMap<String,String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(5);
                lineOptions.color(Color.RED);
            }


            try{
                // Drawing polyline in the Google Map for the i-th route
                googleMap.addPolyline(lineOptions);

            } catch (Exception exception){
                // Toast.makeText(getApplicationContext(),
                //   exception.toString(),Toast.LENGTH_SHORT).show();
                FncMostrarToast("Ha ocurrido un error cargando la ruta: "+exception.toString());
            }


        }
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



}
