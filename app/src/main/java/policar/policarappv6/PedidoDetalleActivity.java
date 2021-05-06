package policar.policarappv6;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
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
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.WindowManager;
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

public class PedidoDetalleActivity extends AppCompatActivity
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
    private String EmpresaId = "";

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


    private String PedidoTipoUnidad = "";
    private String PedidoTipoAccion = "";
    private String PedidoEstado = "";
    private String PedidoTipo = "";
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

    private Marker vehiculoMarker;
    private Marker pedidoMarker;

    /*
* dDATOS GPS
 */
    final String gpsLocationProvider = LocationManager.GPS_PROVIDER;
    final String networkLocationProvider = LocationManager.NETWORK_PROVIDER;

    /**
     * DATOS TIMER
     */

    Timer timerRecepcionar1;

    CountDownTimer countTimerRecepcionar2;
    CountDownTimer countTimerRecepcionar3;

    Timer timerPedidoDetalle;

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

    private Boolean MonitoreoSonido;
    private Boolean MonitoreoEncendido;


    /*
    * CAJAS
     */
    TextView txtClienteNombre;
    TextView txtPedidoDireccion;
    TextView txtPedidoReferencia;
    TextView txtPedidoDetalle;
    TextView txtPedidoLugarCompra;

    TextView txtPedidoTiempo;
    TextView txtPedidoDistancia;

    ImageView imgPedidoTipoAccion;

    /*
    * CAPAS
     */

    LinearLayout capTipoUnidad;
    LinearLayout capPedidoReferencia;
    LinearLayout capPedidoDetalle;
    LinearLayout capPedidoLugarCompra;


    LinearLayout capPedidoTiempo;
    LinearLayout capPedidoDistancia;

/*
*SONIDOS
 */
    MediaPlayer mediaPlayerPedido;
    MediaPlayer mediaPlayerError;

    private FloatingActionButton fabAumentar, fabReducir, fabUbicar;

    //ONSTART -> ON CREATE -> ONRESTART
    @Override
    protected void onStart() {
        super.onStart();
        Log.e("MsgPedidoDetalle20", "Start");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("MsgPedidoDetalle20", "Resume");

    }

    //ONPAUSE -> ONRESUME -> ONSTOP
    @Override
    protected void onPause() {
        super.onPause();
        Log.e("MsgPedidoDetalle20", "Pause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("MsgPedidoDetalle20", "Stop");
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("MsgPedidoDetalle20", "Restart");




    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("MsgPedidoDetalle20", "Destroy");

        if(timerPedidoDetalle!=null) {
            timerPedidoDetalle.cancel();
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e("MsgPedidoDetalle20", "SaveInstance");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.e("MsgPedidoDetalle20", "RestoreInstance");
    }

    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido_detalle);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Log.e("MsgPedidoDetalle20", "onCreate");
        
      /*  FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
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

        //PREFERENCIAS
        sharedPreferences2 = getSharedPreferences("policar.policarappv6", Context.MODE_PRIVATE);

        //PREFERENCIAS
        MonitoreoSonido = sharedPreferences2.getBoolean("MonitoreoSonido",true);
        MonitoreoEncendido = sharedPreferences2.getBoolean("MonitoreoEncendido",true);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                + WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD|
                + WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|
                + WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        //CREANDO BARRA
        ActionBar actionBar = getSupportActionBar();

        //actionBar.setDisplayShowHomeEnabled(true);
        //actionBar.setIcon(R.mipmap.ic_launcher);             //Establecer icono
    //    actionBar.setTitle("Detalle de Servicio");        //Establecer titulo
        actionBar.setTitle(getString(R.string.title_activity_pedido_detalle));     //Establecer Subtitulo

        //PERMISOS
        context = getApplicationContext();
        activity = this;

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

        PedidoTipoUnidad = intentExtras.getStringExtra("PedidoTipoUnidad");
        PedidoTipoAccion = intentExtras.getStringExtra("PedidoTipoAccion");
        PedidoEstado = intentExtras.getStringExtra("PedidoEstado");
        PedidoTipo = intentExtras.getStringExtra("PedidoTipo");

        PedidoCoordenadaX = intentExtras.getStringExtra("PedidoCoordenadaX");
        PedidoCoordenadaY = intentExtras.getStringExtra("PedidoCoordenadaY");

        //VehiculoCoordenadaX = intentExtras.getStringExtra("VehiculoCoordenadaX");
        //VehiculoCoordenadaY = intentExtras.getStringExtra("VehiculoCoordenadaY");

        String Sonar = intentExtras.getStringExtra("Sonar");

         MonitoreoSonido = sharedPreferences2.getBoolean("MonitoreoSonido", true);
         MonitoreoEncendido = sharedPreferences2.getBoolean("MonitoreoEncendido", true);

        VehiculoCoordenadaX = sharedPreferences2.getString("VehiculoCoordenadaX", "0.00");
        VehiculoCoordenadaY = sharedPreferences2.getString("VehiculoCoordenadaY", "0.00");

        PedidoEsNoche = intentExtras.getStringExtra("PedidoEsNoche");
        PedidoEsFestivo = intentExtras.getStringExtra("PedidoEsFestivo");

        //RECUPERANDO VARIABLES v2
        displayUserSettings();

        //MOSTRANDO VARIABLES
        txtClienteNombre = (TextView) findViewById(R.id.CmpClienteNombre);
        txtPedidoDireccion = (TextView) findViewById(R.id.CmpPedidoDireccion);
        txtPedidoReferencia = (TextView) findViewById(R.id.CmpConductorNombre);
        txtPedidoDetalle = (TextView) findViewById(R.id.CmpPedidoDetalle);
        txtPedidoLugarCompra = (TextView) findViewById(R.id.CmpPedidoDetalleLugarCompra);

        txtPedidoTiempo = (TextView)  findViewById(R.id.CmpPedidoTiempo);
        txtPedidoDistancia = (TextView)  findViewById(R.id.CmpPedidoDistancia);

        imgPedidoTipoAccion = (ImageView)  findViewById(R.id.ImgPedidoDetalleTipoAccion);


        //MOSTRANDO VARIABLES
        txtClienteNombre.setText(ClienteNombre);
        txtPedidoDireccion.setText(PedidoDireccion);
        txtPedidoReferencia.setText(PedidoReferencia);
        txtPedidoDetalle.setText(PedidoDetalle);
        txtPedidoLugarCompra.setText(PedidoLugarCompra);

        //CAPAS
        capTipoUnidad = (LinearLayout)this.findViewById(R.id.CapTipoUnidad);
        capPedidoReferencia =(LinearLayout)this.findViewById(R.id.CapPedidoReferencia);
        capPedidoDetalle =(LinearLayout)this.findViewById(R.id.CapPedidoDetalle);
        capPedidoLugarCompra =(LinearLayout)this.findViewById(R.id.CapPedidoDetalleLugarCompra);

        capPedidoTiempo = (LinearLayout) findViewById(R.id.CapPedidoTiempo);
        capPedidoDistancia = (LinearLayout) findViewById(R.id.CapPedidoDistancia);

        fabAumentar = (FloatingActionButton) findViewById(R.id.FabPedidoDetalleAumentar);
        fabReducir = (FloatingActionButton) findViewById(R.id.FabPedidoDetalleReducir);
        //fabUbicar = (FloatingActionButton) findViewById(R.id.FabPedidoDetalleUbicar);

        //EVENTOS - 8
        fabAumentar.setOnClickListener(this);
        fabReducir.setOnClickListener(this);
        //fabUbicar.setOnClickListener(this);

        //MOSTRANDO CAPAS
        if(PedidoTipoUnidad.equals("Station")){
            capTipoUnidad.setVisibility(View.VISIBLE);
        }else{
            capTipoUnidad.setVisibility(View.GONE);
        }

        if(PedidoReferencia.equals("") || PedidoReferencia == null){
            capPedidoReferencia.setVisibility(View.GONE);
        }else{
            capPedidoReferencia.setVisibility(View.VISIBLE);
        }

        if(PedidoDetalle.equals("") || PedidoDetalle == null){
            capPedidoDetalle.setVisibility(View.GONE);
        }else{
            capPedidoDetalle.setVisibility(View.VISIBLE);
        }

        if(PedidoReferencia.equals("") || PedidoReferencia==null){
            capPedidoReferencia.setVisibility(View.GONE);
        }else{
            capPedidoReferencia.setVisibility(View.VISIBLE);
        }

        if(PedidoLugarCompra.equals("") || PedidoLugarCompra == null){
            capPedidoLugarCompra.setVisibility(View.GONE);
        }else{
            capPedidoLugarCompra.setVisibility(View.VISIBLE);
        }


        if(PedidoTiempo.equals("") || PedidoTiempo.equals("-") || PedidoTiempo == null|| PedidoTiempo.equals("null") || PedidoTiempo.equals("0.00") || PedidoTiempo.equals("0")  ){
            capPedidoTiempo.setVisibility(View.GONE);
        }else{
            capPedidoTiempo.setVisibility(View.VISIBLE);
        }

        if(PedidoDistancia.equals("") || PedidoDistancia.equals("-") || PedidoDistancia == null|| PedidoDistancia.equals("null") || PedidoDistancia.equals("0.00") || PedidoDistancia.equals("0") ){
            capPedidoDistancia.setVisibility(View.GONE);
        }else{
            capPedidoDistancia.setVisibility(View.VISIBLE);
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

        //SONIDOS
        mediaPlayerPedido = MediaPlayer.create(PedidoDetalleActivity.this, R.raw.sou_pedido2);
        mediaPlayerError = MediaPlayer.create(PedidoDetalleActivity.this, R.raw.sou_error);

        FncMostrarToast("¡Pedido recibido!");

        //RESETEAR VARIABLES
        SharedPreferences.Editor editor = sharedPreferences2.edit();
        editor.putFloat("KilometrajeRecorrido",  0);
        editor.putLong("PedTiempoCreacion",  SystemClock.elapsedRealtime());
        editor.apply();

        //CALCULAR TIEMPO DISTANCIA
        if(!PedidoCoordenadaX.equals("") && !PedidoCoordenadaY.equals("") && !PedidoCoordenadaX.equals("0.00") && !PedidoCoordenadaY.equals("0.00") && PedidoCoordenadaX != null  && PedidoCoordenadaY != null) {

            if(!VehiculoCoordenadaX.equals("") && !VehiculoCoordenadaY.equals("") && !VehiculoCoordenadaX.equals("0.00") && !VehiculoCoordenadaY.equals("0.00") && VehiculoCoordenadaX != null  && VehiculoCoordenadaY != null) {

                Double PedidoDistancia2 = redondear(calculateDistance(Double.parseDouble(VehiculoCoordenadaX),Double.parseDouble(VehiculoCoordenadaY),Double.parseDouble(PedidoCoordenadaX),Double.parseDouble(PedidoCoordenadaY)),2);
                Double PedidoTiempo2 = 0.00;

                PedidoTiempo2 = redondear((((PedidoDistancia2)/50)*60),2);

                PedidoDistancia = PedidoDistancia2.toString()+" km";
                PedidoTiempo = PedidoTiempo2.toString() + " min";

                txtPedidoDistancia.setText(PedidoDistancia);
                txtPedidoTiempo.setText(PedidoTiempo);

                if(PedidoTiempo.equals("") || PedidoTiempo.equals("-") || PedidoTiempo == null|| PedidoTiempo.equals("null") || PedidoTiempo.equals("0.00") || PedidoTiempo.equals("0")  ){
                    capPedidoTiempo.setVisibility(View.GONE);
                }else{
                    capPedidoTiempo.setVisibility(View.VISIBLE);
                }

                if(PedidoDistancia.equals("") || PedidoDistancia.equals("-") || PedidoDistancia == null|| PedidoDistancia.equals("null") || PedidoDistancia.equals("0.00") || PedidoDistancia.equals("0") ){
                    capPedidoDistancia.setVisibility(View.GONE);
                }else{
                    capPedidoDistancia.setVisibility(View.VISIBLE);
                }

            }

        }

        //PERMISOS
        context = getApplicationContext();
        activity = this;

        //OBTENER COORDENADAS
        if(checkPermission(2)){
            MtdObtenerCoordenadas();
        }

        //CREANDO MAPA
        if(checkPermission(1)){
            createMapView();
        }

/*
        final ProgressDialog PrgPedidoDetalle = new ProgressDialog(this);
        PrgPedidoDetalle.setIcon(R.mipmap.ic_launcher);
        PrgPedidoDetalle.setMessage("Cargando...");
        PrgPedidoDetalle.setCancelable(false);
        PrgPedidoDetalle.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        PrgPedidoDetalle.show();

        Thread nt = new Thread() {
            @Override
            public void run() {

                try {

                    final String resAceptarPedido;
                    resAceptarPedido = MtdAceptarPedido(PedidoId, ConductorId, VehiculoCoordenadaX, VehiculoCoordenadaY);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            String JsRespuesta = "";
                            String JsPedVehiculoUnidad = "";

                            String JsConSuspensionMotivo = "";
                            String JsConRetiroMotivo = "";

                            try {

                                JSONObject jsonObject = new JSONObject(resAceptarPedido);
                                JsRespuesta = jsonObject.getString("Respuesta");
                                JsPedVehiculoUnidad = jsonObject.getString("PedVehiculoUnidad");

                                JsConSuspensionMotivo = jsonObject.getString("ConSuspensionMotivo");
                                JsConRetiroMotivo = jsonObject.getString("ConRetiroMotivo");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            PrgPedidoDetalle.cancel();

                            switch(JsRespuesta){
                                case "P009":
                                    // FncMostrarMensaje("El PEDIDO fue ACEPTADO correctamente.");

                                    //GUARDAR MEMORIA
                                    SharedPreferences sharedPreferences = getSharedPreferences("policar.policarappv6", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();

                                    editor.putString("PedidoId", PedidoId.trim());

                                    editor.putString("ClienteNombre", ClienteNombre.trim());
                                    editor.putString("ClienteCelular", ClienteCelular.trim());
                                    editor.putString("ClienteTelefono", ClienteTelefono.trim());

                                    editor.putString("PedidoDireccion", PedidoDireccion.trim());
                                    editor.putString("PedidoReferencia", PedidoReferencia.trim());
                                    editor.putString("PedidoEstado", PedidoEstado.trim());

                                    editor.putString("PedidoCoordenadaX", PedidoCoordenadaX.trim());
                                    editor.putString("PedidoCoordenadaY", PedidoCoordenadaY.trim());

                                    editor.putString("VehiculoCoordenadaX", VehiculoCoordenadaX.trim());
                                    editor.putString("VehiculoCoordenadaY", VehiculoCoordenadaY.trim());

                                    editor.putInt("ConductorOcupado",111);
                                    editor.putBoolean("ConductorTienePedido", true);
                                    editor.apply();


                                    Intent intentPedidoActual = new Intent(PedidoDetalleActivity.this, PedidoActualActivity.class);
                                    Bundle bundlePedidoActual = new Bundle();

                                    bundlePedidoActual.putString("PedidoId", PedidoId);

                                    bundlePedidoActual.putString("ClienteNombre", ClienteNombre);
                                    bundlePedidoActual.putString("ClienteCelular", ClienteCelular);
                                    bundlePedidoActual.putString("ClienteTelefono", ClienteTelefono);

                                    bundlePedidoActual.putString("PedidoDireccion", PedidoDireccion);
                                    bundlePedidoActual.putString("PedidoReferencia", PedidoReferencia);
                                    bundlePedidoActual.putString("PedidoEstado", PedidoEstado);

                                    bundlePedidoActual.putString("PedidoCoordenadaX", PedidoCoordenadaX);
                                    bundlePedidoActual.putString("PedidoCoordenadaY", PedidoCoordenadaY);

                                    bundlePedidoActual.putString("VehiculoCoordenadaX", VehiculoCoordenadaX);
                                    bundlePedidoActual.putString("VehiculoCoordenadaY", VehiculoCoordenadaY);

                                    intentPedidoActual.putExtras(bundlePedidoActual);//Put your id to your next Intent
                                    startActivity(intentPedidoActual);
                                    PedidoDetalleActivity.this.overridePendingTransition(R.anim.anim_slide_in_left,
                                            R.anim.anim_slide_out_left);
                                    finish();

                                    break;

                                case "P010":

                                    //FncMostrarMensaje("El PEDIDO no pudo ser ACEPTADO.",true);
                                    android.app.AlertDialog.Builder MsgPedidoDetalle10 = new android.app.AlertDialog.Builder(PedidoDetalleActivity.this);
                                    MsgPedidoDetalle10.setTitle(getString(R.string.alert_title));
                                    MsgPedidoDetalle10.setMessage("No se ha podido aceptar el pedido, comuniquese con la central. Codigo de error P010");
                                    MsgPedidoDetalle10.setPositiveButton("Aceptar",
                                            new DialogInterface.OnClickListener() {

                                                public void onClick(DialogInterface dialog, int which) {
                                                    // Do nothing but close the dialog
                                                    Intent intentNavegacion10 = new Intent(PedidoDetalleActivity.this, NavegacionActivity.class);
                                                    startActivity(intentNavegacion10);
                                                    PedidoDetalleActivity.this.overridePendingTransition(R.anim.anim_slide_in_right,
                                                            R.anim.anim_slide_out_right);
                                                    finish();
                                                }
                                            });

                                    // Remember, create doesn't show the dialog
                                    android.app.AlertDialog msgPedidoDetalle10 = MsgPedidoDetalle10.create();
                                    msgPedidoDetalle10.show();

                                    break;

                                case "P011":

                                    String Mensaje = "EL pedido ya fue atendido por otra unidad";

                                    if(null != JsPedVehiculoUnidad && !JsPedVehiculoUnidad.equals("")){
                                        Mensaje += ": "+JsPedVehiculoUnidad;
                                    }

//                                        FncMostrarMensaje(Mensaje,true);
                                    android.app.AlertDialog.Builder MsgRecepcionar11 = new android.app.AlertDialog.Builder(PedidoDetalleActivity.this);
                                    MsgRecepcionar11.setTitle(getString(R.string.alert_title));
                                    MsgRecepcionar11.setMessage(Mensaje);
                                    MsgRecepcionar11.setPositiveButton("Aceptar",
                                            new DialogInterface.OnClickListener() {

                                                public void onClick(DialogInterface dialog, int which) {
                                                    // Do nothing but close the dialog

                                                    Intent intentNavegacion11 = new Intent(PedidoDetalleActivity.this, NavegacionActivity.class);
                                                    startActivity(intentNavegacion11);
                                                    PedidoDetalleActivity.this.overridePendingTransition(R.anim.anim_slide_in_right,
                                                            R.anim.anim_slide_out_right);
                                                    finish();

                                                }
                                            });

                                    // Remember, create doesn't show the dialog
                                    android.app.AlertDialog msgRecepcionar11 = MsgRecepcionar11.create();
                                    msgRecepcionar11.show();


                                    break;

                                case "P012":



//                                        FncMostrarMensaje("El PEDIDO fue ANULADO por el CLIENTE.",true);
                                    android.app.AlertDialog.Builder MsgRecepcionar12 = new android.app.AlertDialog.Builder(PedidoDetalleActivity.this);
                                    MsgRecepcionar12.setTitle(getString(R.string.alert_title));
                                    MsgRecepcionar12.setMessage("El pedido ha sido anulado.");
                                    MsgRecepcionar12.setPositiveButton("Aceptar",
                                            new DialogInterface.OnClickListener() {

                                                public void onClick(DialogInterface dialog, int which) {
                                                    // Do nothing but close the dialog

                                                    Intent intentNavegacion12 = new Intent(PedidoDetalleActivity.this, NavegacionActivity.class);
                                                    startActivity(intentNavegacion12);
                                                    PedidoDetalleActivity.this.overridePendingTransition(R.anim.anim_slide_in_right,
                                                            R.anim.anim_slide_out_right);
                                                    finish();

                                                }
                                            });

                                    // Remember, create doesn't show the dialog
                                    android.app.AlertDialog msgRecepcionar12 = MsgRecepcionar12.create();
                                    msgRecepcionar12.show();

                                    break;

                                case "P098":

                                    //FncMostrarMensaje("No se ha podido identificar el servicio, proceso cancelado", true);
                                    android.app.AlertDialog.Builder MsgRecepcionar13 = new android.app.AlertDialog.Builder(PedidoDetalleActivity.this);
                                    MsgRecepcionar13.setTitle(getString(R.string.alert_title));
                                    MsgRecepcionar13.setMessage("No se ha podido identificar su codigo de conductor, comuniquese con la central. Codigo de Error: P098");
                                    MsgRecepcionar13.setCancelable(false);
                                    MsgRecepcionar13.setPositiveButton("Aceptar",
                                            new DialogInterface.OnClickListener() {

                                                public void onClick(DialogInterface dialog, int which) {

                                                    Intent intentNavegacion13 = new Intent(PedidoDetalleActivity.this, NavegacionActivity.class);
                                                    startActivity(intentNavegacion13);
                                                    PedidoDetalleActivity.this.overridePendingTransition(R.anim.anim_slide_in_right,
                                                            R.anim.anim_slide_out_right);
                                                    finish();

                                                }
                                            });

                                    // Remember, create doesn't show the dialog
                                    android.app.AlertDialog msgRecepcionar13 = MsgRecepcionar13.create();
                                    msgRecepcionar13.show();

                                    break;

                                case "P049":

                                    //FncMostrarMensaje("No se ha podido identificar el servicio, proceso cancelado", true);
                                    android.app.AlertDialog.Builder MsgPedidoActua49 = new android.app.AlertDialog.Builder(PedidoDetalleActivity.this);
                                    MsgPedidoActua49.setTitle(getString(R.string.alert_title));
                                    //MsgPedidoActual6.setMessage("No se ha podido identificar el servicio, se reiniciara la aplicación");
                                    MsgPedidoActua49.setMessage("No se ha podido identificar su codigo de pedido, comuniquese con la central. Codigo de Error: P049");
                                    MsgPedidoActua49.setCancelable(false);
                                    MsgPedidoActua49.setPositiveButton("Aceptar",
                                            new DialogInterface.OnClickListener() {

                                                public void onClick(DialogInterface dialog, int which) {

                                                    Intent intentPedirTaxi = new Intent(PedidoDetalleActivity.this, NavegacionActivity.class);
                                                    startActivity(intentPedirTaxi);
                                                    PedidoDetalleActivity.this.overridePendingTransition(R.anim.anim_slide_in_right,
                                                            R.anim.anim_slide_out_right);
                                                    finish();

                                                }
                                            });

                                    // Remember, create doesn't show the dialog
                                    android.app.AlertDialog msgPedidoActua49 = MsgPedidoActua49.create();
                                    msgPedidoActua49.show();

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

        //final MediaPlayer player = MediaPlayer.create(PedidoDetalleActivity.this, R.raw.sou_pedido2);


        if(timerPedidoDetalle!=null) {
            timerPedidoDetalle.cancel();
        }

        timerPedidoDetalle = new Timer();
        timerPedidoDetalle.schedule(new TimerTask() {
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
                            String JsPedidoVehiculoUnidad = "";

                            try {

                                JSONObject jsonObject = new JSONObject(resObtenerPedido);
                                JsRespuesta = jsonObject.getString("Respuesta");
                                JsPedidoEstado = jsonObject.getString("PedidoEstado");
                                JsPedidoCancelarMotivo = jsonObject.getString("PedidoCancelarMotivo");
                                JsPedidoVehiculoUnidad = jsonObject.getString("PedidoVehiculoUnidad");

                            } catch (JSONException e) {
                                //Log.e("MsgPedidoActual", e.toString());
                                e.printStackTrace();
                            }

                            switch (JsRespuesta) {

                                case "P013":

                                    if(JsPedidoEstado.equals("3")){

                                        if(timerPedidoDetalle!=null){
                                            timerPedidoDetalle.cancel();
                                        }

                                        String mensaje = "El pedido ha sido cancelado.";

                                        if ((JsPedidoCancelarMotivo != null) && !JsPedidoCancelarMotivo.equals("") && !JsPedidoCancelarMotivo.equals("null")) {
                                            mensaje += " MOTIVO:" + JsPedidoCancelarMotivo;
                                        }

                                        Intent intentNavegacion = new Intent(PedidoDetalleActivity.this, NavegacionActivity.class);

                                        Bundle bundleNavegacion = new Bundle();
                                        bundleNavegacion.putString("PedidoMensaje", mensaje);
                                        intentNavegacion.putExtras(bundleNavegacion); //Put your id to your next Intent

                                        startActivity(intentNavegacion);
                                        finish();

                                    }else if(JsPedidoEstado.equals("2")){

                                        if(!JsPedidoVehiculoUnidad.equals(VehiculoUnidad) && !JsPedidoVehiculoUnidad.equals("")){

                                            if(timerPedidoDetalle!=null){
                                                timerPedidoDetalle.cancel();
                                            }

                                          //  player[0] = MediaPlayer.create(PedidoDetalleActivity.this, R.raw.sou_pedidov6);
                                           // player[0].stop();

                                            String mensaje = "El pedido fue atendido por otra unidad unidad";

                                            if(JsPedidoVehiculoUnidad!=null){
                                                mensaje += ": "+JsPedidoVehiculoUnidad;
                                            }

                                            Intent intentNavegacion = new Intent(PedidoDetalleActivity.this, NavegacionActivity.class);

                                            Bundle bundleNavegacion = new Bundle();
                                            bundleNavegacion.putString("PedidoMensaje", mensaje);
                                            intentNavegacion.putExtras(bundleNavegacion); //Put your id to your next Intent

                                            startActivity(intentNavegacion);
                                            finish();

                                        }


                                    }


                                   /* if(mediaPlayerPedido.isPlaying()){

                                    }else{

                                        if(MonitoreoSonido){
                                            mediaPlayerPedido.start();
                                        }

                                    }*/

                                    //MediaPlayer player = MediaPlayer.create(PedidoDetalleActivity.this, R.raw.sou_pedidov2);
                                    //player = MediaPlayer.create(PedidoDetalleActivity.this, R.raw.sou_pedidov2);
                                    //player.start();

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
        }, 1000, 4100);
        //TAREA OBTENER PEDIDO ESTADO



    }





    private boolean checkPermission(int permiso) {

        Log.e("MsgPedidoDetalle10","VERIFICAR PERMISO");
        boolean respuesta = false;

        switch(permiso){
            case 1:

                int result1 = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);

                if (result1 == PackageManager.PERMISSION_GRANTED) {
                    Log.e("MsgPedidoDetalle10","AAA");
                    respuesta = true;
                }else {
                    respuesta = false;
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, permiso);
                    Log.e("MsgPedidoDetalle10", "BBB");
                }

                break;

            case 2:

                int result2 = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);

                if (result2 == PackageManager.PERMISSION_GRANTED) {
                    Log.e("MsgPedidoDetalle10","AAA");
                    respuesta = true;
                }else {
                    respuesta = false;
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, permiso);
                    Log.e("MsgPedidoDetalle10", "BBB");
                }

                break;
        }


        return respuesta;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            Log.e("MsgPedidoDetalle10","PERMISO ACEPTADO");

            switch (requestCode) {
                case 1:
                    createMapView();
                    break;

                case 2:
                    MtdObtenerCoordenadas();
                    break;
            }

        } else {
            Log.e("MsgPedidoDetalle10","PERMISO NEGADO");
            FncMostrarToast("Permiso denegado, es posible que la aplicacion no funcione  correctamente.");
        }

    }




    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){


/*
            case R.id.FabPedidoDetalleUbicar:

                //if(!CambioUbicacion) {

                //OBTENER COORDENADAS
                if(checkPermission(2)){
                    MtdObtenerCoordenadas();
                }

                //}

                if(!ClienteCoordenadaX.equals("") & !ClienteCoordenadaY.equals("") & !ClienteCoordenadaX.equals("0.00") & !ClienteCoordenadaY.equals("0.00") & ClienteCoordenadaX!=null & ClienteCoordenadaY!=null){

                    if(googleMap!=null){

                        LatLng latLng = new LatLng(Double.parseDouble(ClienteCoordenadaX), Double.parseDouble(ClienteCoordenadaY));
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 18);
                        googleMap.animateCamera(cameraUpdate);

                    }else{
                        Log.e("CambioUbicacion", "Google Map Error");
                    }

                }else{
                    FncMostrarToast("No se pudo obtener su ubicación");
                }

                break;*/

            case R.id.FabPedidoDetalleReducir:

                if(null != googleMap){
                    googleMap.animateCamera(CameraUpdateFactory.zoomOut());
                }

                break;

            case R.id.FabPedidoDetalleAumentar:


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

            PedidoDetalleListener mlocListener = new PedidoDetalleListener();
            mlocListener.setPedidoDetalleActivity(this);
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

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.pedido_detalle, menu);
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

            Intent intentNavegacion = new Intent(PedidoDetalleActivity.this, NavegacionActivity.class);

            Bundle bundleNavegacion = new Bundle();
            bundleNavegacion.putString("PedidoMensaje", "");
            intentNavegacion.putExtras(bundleNavegacion); //Put your id to your next Intent

            startActivity(intentNavegacion);
            finish();


        } else if (id == R.id.nav_ver_cuenta) {
            // Handle the camera action

            Intent intenConductorDato = new Intent(PedidoDetalleActivity.this, ConductorDatoActivity.class);

            Bundle bundleConductorDato = new Bundle();
            bundleConductorDato.putString("ConductorId", ConductorId);

            intenConductorDato.putExtras(bundleConductorDato); //Put your id to your next Intent

            startActivity(intenConductorDato);
            finish();


        } else if (id == R.id.nav_mis_preferencias) {

            Intent intentMisPreferencias = new Intent(PedidoDetalleActivity.this, MisPreferenciasActivity.class);

            Bundle bundleMisPreferencias = new Bundle();
            bundleMisPreferencias.putString("ConductorId", ConductorId);

            intentMisPreferencias.putExtras(bundleMisPreferencias); //Put your id to your next Intent

            startActivity(intentMisPreferencias);
            finish();

        } else if (id == R.id.nav_historial) {

            Intent intentHistorial = new Intent(PedidoDetalleActivity.this, HistorialActivity.class);

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

                            Intent intentMainSesion = new Intent(PedidoDetalleActivity.this, MainActivity.class);
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


    private void FncMostrarToast(String oMensaje){

        Toast.makeText(PedidoDetalleActivity.this, oMensaje,
                Toast.LENGTH_LONG).show();
    }



    private void createMapView(){

        /**
         * Catch the null pointer exception that
         * may be thrown when initialising the map
         */
        try {

            if(null == googleMap){

                googleMap  = ((MapFragment) getFragmentManager().findFragmentById(R.id.map1)).getMap();
                googleMap.setMyLocationEnabled(false);
                googleMap.getUiSettings().setZoomControlsEnabled(false);
                googleMap.setPadding(0, 0, 0, 90);




                googleMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
                    @Override
                    public void onMyLocationChange(Location location) {

                        VehiculoCoordenadaX = Double.toString(location.getLatitude());
                        VehiculoCoordenadaY = Double.toString(location.getLongitude());

                        if( (!VehiculoCoordenadaX.equals("") && !VehiculoCoordenadaY.equals(""))  && (VehiculoCoordenadaX != null && VehiculoCoordenadaY != null)){

                            if(vehiculoMarker!=null){
                                vehiculoMarker.remove();
                            }

                            vehiculoMarker = googleMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(Double.parseDouble(VehiculoCoordenadaX), Double.parseDouble(VehiculoCoordenadaY)))
                                    .title("¡Aquì estoy!")
                                    .draggable(true)
                                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_taxi150)));

                            LatLng latLng = new LatLng(Double.parseDouble(VehiculoCoordenadaX),Double.parseDouble(VehiculoCoordenadaY));
                            CameraPosition cameraPosition = new CameraPosition.Builder()
                                    .target(latLng)      // Sets the center of the map to Mountain View
                                    .zoom(18)                   // Sets the zoom
                                    //.bearing(90)  //era 90              // Sets the orientation of the camera to east
                                    .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                                    .build();                   // Creates a CameraPosition from the builder
                            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                        }

                        if(!PedidoCoordenadaX.equals("") && !PedidoCoordenadaY.equals("") && !PedidoCoordenadaX.equals("0.00") && !PedidoCoordenadaY.equals("0.00") && PedidoCoordenadaX != null  && PedidoCoordenadaY != null) {

                            if(!VehiculoCoordenadaX.equals("") && !VehiculoCoordenadaY.equals("") && !VehiculoCoordenadaX.equals("0.00") && !VehiculoCoordenadaY.equals("0.00") && VehiculoCoordenadaX != null  && VehiculoCoordenadaY != null) {

                                Double PedidoDistancia2 = redondear(calculateDistance(Double.parseDouble(VehiculoCoordenadaX),Double.parseDouble(VehiculoCoordenadaY),Double.parseDouble(PedidoCoordenadaX),Double.parseDouble(PedidoCoordenadaY)),2);
                                Double PedidoTiempo2 = 0.00;

                                PedidoTiempo2 = redondear((((PedidoDistancia2)/50)*60),2);

                                PedidoDistancia = PedidoDistancia2.toString()+" km";
                                PedidoTiempo = PedidoTiempo2.toString() + " min";

                                txtPedidoDistancia.setText(PedidoDistancia);
                                txtPedidoTiempo.setText(PedidoTiempo);

                             /*   if(PedidoTiempo.equals("") || PedidoTiempo.equals("-") || PedidoTiempo == null|| PedidoTiempo.equals("null") || PedidoTiempo.equals("0.00") || PedidoTiempo.equals("0")  ){
                                    capPedidoTiempo.setVisibility(View.GONE);
                                }else{
                                    capPedidoTiempo.setVisibility(View.VISIBLE);
                                }
                                */

/*                                if(PedidoDistancia.equals("") || PedidoDistancia.equals("-") || PedidoDistancia == null|| PedidoDistancia.equals("null") || PedidoDistancia.equals("0.00") || PedidoDistancia.equals("0") ){
                                    capPedidoDistancia.setVisibility(View.GONE);
                                }else{
                                    capPedidoDistancia.setVisibility(View.VISIBLE);
                                }*/

                            }

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

                }

                if(!PedidoCoordenadaX.equals("") && !PedidoCoordenadaY.equals("")  && PedidoCoordenadaX !=null && PedidoCoordenadaY != null) {

                    if (pedidoMarker != null) {
                        pedidoMarker.remove();
                    }

                    pedidoMarker = googleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(Double.parseDouble(PedidoCoordenadaX), Double.parseDouble(PedidoCoordenadaY)))
                            .title(ClienteNombre)
                            .draggable(false)
                            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_pedido150)));
                }

                if(!PedidoCoordenadaX.equals("") && !PedidoCoordenadaY.equals("")  && PedidoCoordenadaX !=null && PedidoCoordenadaY != null && !PedidoCoordenadaX.equals("0.00") && !PedidoCoordenadaY.equals("0.00")){

                    if(!VehiculoCoordenadaX.equals("") && !VehiculoCoordenadaY.equals("")  &&  VehiculoCoordenadaX !=null && VehiculoCoordenadaY != null && !VehiculoCoordenadaX.equals("") && !VehiculoCoordenadaY.equals("")){

                        LatLngBounds.Builder builder = new LatLngBounds.Builder();

                        builder.include(new LatLng(Double.parseDouble(PedidoCoordenadaX),Double.parseDouble(PedidoCoordenadaY)));
                        builder.include(new LatLng(Double.parseDouble(VehiculoCoordenadaX),Double.parseDouble(VehiculoCoordenadaY)));

                        LatLngBounds bounds = builder.build();

                        int width = getResources().getDisplayMetrics().widthPixels;
                        int height = getResources().getDisplayMetrics().heightPixels;
                        int padding = (int) (width * 0.1); // offset from edges of the map 12% of screen

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



                if(!VehiculoCoordenadaX.equals("") && !VehiculoCoordenadaY.equals("")   &&  VehiculoCoordenadaX != null && VehiculoCoordenadaY != null && !VehiculoCoordenadaX.equals("0.00") && !VehiculoCoordenadaY.equals("0.00")) {

                    if (!PedidoCoordenadaX.equals("") && !PedidoCoordenadaY.equals("") && PedidoCoordenadaX != null && PedidoCoordenadaY != null&& !PedidoCoordenadaX.equals("0.00") && !PedidoCoordenadaY.equals("0.00")) {

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
* EVENTOS NAVEGACION
 */


    public void onClick_BtnPedidoDetalleAceptar(View v){

        //FncMostrarToast("Usted ha ACEPTADO el pedido");
        final ProgressDialog PrgPedidoDetalle = new ProgressDialog(this);
        PrgPedidoDetalle.setIcon(R.mipmap.ic_launcher);
        PrgPedidoDetalle.setMessage("Cargando...");
        PrgPedidoDetalle.setCancelable(false);
        PrgPedidoDetalle.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        PrgPedidoDetalle.show();

        Thread nt = new Thread() {
            @Override
            public void run() {

                try {

                    final String resAceptarPedido;
                    resAceptarPedido = MtdAceptarPedido(PedidoId, ConductorId, EmpresaId,VehiculoCoordenadaX, VehiculoCoordenadaY);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            String JsRespuesta = "";
                            String JsPedVehiculoUnidad = "";

                            String JsConSuspensionMotivo = "";
                            String JsConRetiroMotivo = "";

                            String JsEmpMonedaSimbolo = "";
                            String JsEmpTarifaMinima = "";
                            String JsEmpTarifaKilometro = "";
                            String JsEmpTarifaAdicionalNoche = "";
                            String JsEmpTarifaAdicionalFestivo = "";

                            try {

                                JSONObject jsonObject = new JSONObject(resAceptarPedido);
                                JsRespuesta = jsonObject.getString("Respuesta");
                                JsPedVehiculoUnidad = jsonObject.getString("PedVehiculoUnidad");

                                JsConSuspensionMotivo = jsonObject.getString("ConSuspensionMotivo");
                                JsConRetiroMotivo = jsonObject.getString("ConRetiroMotivo");

                                JsEmpMonedaSimbolo = jsonObject.getString("EmpMonedaSimbolo");
                                JsEmpTarifaMinima = jsonObject.getString("EmpTarifaMinima");
                                JsEmpTarifaKilometro = jsonObject.getString("EmpTarifaKilometro");
                                JsEmpTarifaAdicionalFestivo = jsonObject.getString("EmpTarifaAdicionalFestivo");
                                JsEmpTarifaAdicionalNoche = jsonObject.getString("EmpTarifaAdicionalNoche");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            PrgPedidoDetalle.cancel();

                            switch(JsRespuesta){
                                case "P009":


                                    MonedaSimbolo = JsEmpMonedaSimbolo;
                                    TarifaMinima = JsEmpTarifaMinima;
                                    TarifaKilometro = JsEmpTarifaKilometro;
                                    TarifaAdicionalFestivo = JsEmpTarifaAdicionalFestivo;
                                    TarifaAdicionalNoche = JsEmpTarifaAdicionalNoche;

                                    if(timerPedidoDetalle!=null){
                                        timerPedidoDetalle.cancel();
                                    }

                                    // FncMostrarMensaje("El PEDIDO fue ACEPTADO correctamente.");
                                    FncMostrarToast("!Pedido Aceptado!");

                                    //GUARDAR MEMORIA
                                    SharedPreferences sharedPreferences = getSharedPreferences("policar.policarappv6", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();

                                    editor.putString("PedidoId", PedidoId.trim());

                                    editor.putString("ClienteId", ClienteId.trim());
                                    editor.putString("ClienteNombre", ClienteNombre.trim());
                                    editor.putString("ClienteCelular", ClienteCelular.trim());
                                    editor.putString("ClienteTelefono", ClienteTelefono.trim());

                                    editor.putString("PedidoDireccion", PedidoDireccion.trim());
                                    editor.putString("PedidoReferencia", PedidoReferencia.trim());
                                    editor.putString("PedidoDetalle", PedidoDetalle.trim());
                                    editor.putString("PedidoLugarCompra", PedidoLugarCompra.trim());

                                    editor.putString("PedidoEstado", PedidoEstado.trim());
                                    editor.putString("PedidoTiempo", PedidoTiempo.trim());
                                    editor.putString("PedidoDistancia", PedidoDistancia.trim());

                                    editor.putString("PedidoCoordenadaX", PedidoCoordenadaX.trim());
                                    editor.putString("PedidoCoordenadaY", PedidoCoordenadaY.trim());

                                    editor.putString("VehiculoCoordenadaX", VehiculoCoordenadaX.trim());
                                    editor.putString("VehiculoCoordenadaY", VehiculoCoordenadaY.trim());

                                    editor.putString("PedidoTipo", PedidoTipo.trim());
                                    editor.putString("PedidoTipoUnidad", PedidoTipoUnidad.trim());
                                    editor.putString("PedidoTipoAccion", PedidoTipoAccion.trim());

                                    editor.putString("MonedaSimbolo", MonedaSimbolo.trim());
                                    editor.putString("TarifaMinima", TarifaMinima.trim());
                                    editor.putString("TarifaKilometro", TarifaKilometro.trim());
                                    editor.putString("TarifaAdicionalFestivo", TarifaAdicionalFestivo.trim());
                                    editor.putString("TarifaAdicionalNoche", TarifaAdicionalNoche.trim());

                                    editor.putString("PedidoEsFestivo", PedidoEsFestivo.trim());
                                    editor.putString("PedidoEsNoche", PedidoEsNoche.trim());

                                    editor.putInt("ConductorOcupado",111);
                                    editor.putBoolean("ConductorTienePedido", true);
                                    editor.apply();


                                    Intent intentPedidoActual = new Intent(PedidoDetalleActivity.this, PedidoActualActivity.class);
                                    Bundle bundlePedidoActual = new Bundle();

                                    bundlePedidoActual.putString("PedidoId", PedidoId);

                                    bundlePedidoActual.putString("ClienteId", ClienteId);
                                    bundlePedidoActual.putString("ClienteNombre", ClienteNombre);
                                    bundlePedidoActual.putString("ClienteCelular", ClienteCelular);
                                    bundlePedidoActual.putString("ClienteTelefono", ClienteTelefono);

                                    bundlePedidoActual.putString("PedidoDireccion", PedidoDireccion);
                                    bundlePedidoActual.putString("PedidoReferencia", PedidoReferencia);
                                    bundlePedidoActual.putString("PedidoDetalle", PedidoDetalle);
                                    bundlePedidoActual.putString("PedidoLugarCompra", PedidoLugarCompra);
                                    bundlePedidoActual.putString("PedidoEstado", PedidoEstado);

                                    bundlePedidoActual.putString("PedidoTipo", PedidoTipo);
                                    bundlePedidoActual.putString("PedidoTipoUnidad", PedidoTipoUnidad);
                                    bundlePedidoActual.putString("PedidoTipoAccion", PedidoTipoAccion);

                                    bundlePedidoActual.putString("PedidoTiempo", PedidoTiempo);
                                    bundlePedidoActual.putString("PedidoDistancia", PedidoDistancia);

                                    bundlePedidoActual.putString("PedidoCoordenadaX", PedidoCoordenadaX);
                                    bundlePedidoActual.putString("PedidoCoordenadaY", PedidoCoordenadaY);

                                    bundlePedidoActual.putString("VehiculoCoordenadaX", VehiculoCoordenadaX);
                                    bundlePedidoActual.putString("VehiculoCoordenadaY", VehiculoCoordenadaY);

                                    bundlePedidoActual.putString("MonedaSimbolo", MonedaSimbolo);
                                    bundlePedidoActual.putString("TarifaMinima", TarifaMinima);
                                    bundlePedidoActual.putString("TarifaKilometro", TarifaKilometro);
                                    bundlePedidoActual.putString("TarifaAdicionalFestivo", TarifaAdicionalFestivo);
                                    bundlePedidoActual.putString("TarifaAdicionalNoche", TarifaAdicionalNoche);

                                    bundlePedidoActual.putString("PedidoEsNoche", PedidoEsNoche);
                                    bundlePedidoActual.putString("PedidoEsFestivo", PedidoEsFestivo);



                                    intentPedidoActual.putExtras(bundlePedidoActual);//Put your id to your next Intent
                                    startActivity(intentPedidoActual);
                                    PedidoDetalleActivity.this.overridePendingTransition(R.anim.anim_slide_in_left,
                                            R.anim.anim_slide_out_left);
                                    finish();

                                    break;

                                case "P010":

                                    /*MediaPlayer player2 = MediaPlayer.create(PedidoDetalleActivity.this,R.raw.computer_error);
                                    player2 = MediaPlayer.create(PedidoDetalleActivity.this, R.raw.computer_error);
                                    player2.start();*/
                                    if(timerPedidoDetalle!=null){
                                        timerPedidoDetalle.cancel();
                                    }

                                    mediaPlayerError.start();

                                    //FncMostrarMensaje("El PEDIDO no pudo ser ACEPTADO.",true);
                                    android.app.AlertDialog.Builder MsgPedidoDetalle10 = new android.app.AlertDialog.Builder(PedidoDetalleActivity.this);
                                    MsgPedidoDetalle10.setTitle(getString(R.string.alert_title));
                                    MsgPedidoDetalle10.setMessage("No se ha podido aceptar el pedido, comuniquese con la central. Codigo de error P010");
                                   /* MsgPedidoDetalle10.setPositiveButton("Aceptar",
                                            new DialogInterface.OnClickListener() {

                                                public void onClick(DialogInterface dialog, int which) {
                                                    // Do nothing but close the dialog

                                                    Intent intentNavegacion = new Intent(PedidoDetalleActivity.this, NavegacionActivity.class);

                                                    Bundle bundleNavegacion = new Bundle();
                                                    bundleNavegacion.putString("PedidoMensaje", "");
                                                    intentNavegacion.putExtras(bundleNavegacion); //Put your id to your next Intent

                                                    startActivity(intentNavegacion);
                                                    finish();


                                                }
                                            });*/

                                    // Remember, create doesn't show the dialog
                                    final android.app.AlertDialog msgPedidoDetalle10 = MsgPedidoDetalle10.create();
                                    msgPedidoDetalle10.show();


                                    final Timer timer10 = new Timer();
                                    timer10.schedule(new TimerTask() {
                                        public void run() {

                                            if(msgPedidoDetalle10!=null && msgPedidoDetalle10.isShowing()){
                                                msgPedidoDetalle10.dismiss();
                                            }

                                            timer10.cancel(); //this will cancel the timer of the system

                                            Intent intentNavegacion = new Intent(PedidoDetalleActivity.this, NavegacionActivity.class);

                                            Bundle bundleNavegacion = new Bundle();
                                            bundleNavegacion.putString("PedidoMensaje", "");
                                            intentNavegacion.putExtras(bundleNavegacion); //Put your id to your next Intent

                                            startActivity(intentNavegacion);
                                            finish();


                                        }
                                    }, 1500); // the timer will count 5 seconds....


                                    break;

                                case "P011":

                                    if(timerPedidoDetalle!=null){
                                        timerPedidoDetalle.cancel();
                                    }

                                    //MediaPlayer playerError = MediaPlayer.create(PedidoDetalleActivity.this,R.raw.sou_error);
                                    //playerError = MediaPlayer.create(PedidoDetalleActivity.this, R.raw.sou_error);
                                    //playerError.start();
                                    mediaPlayerError.start();

                                    String Mensaje = "EL pedido ya fue atendido por otra unidad";

                                    if(null != JsPedVehiculoUnidad && !JsPedVehiculoUnidad.equals("")){
                                        Mensaje += ": "+JsPedVehiculoUnidad;
                                    }

//                                        FncMostrarMensaje(Mensaje,true);
                                    android.app.AlertDialog.Builder MsgPedidoDetalle11 = new android.app.AlertDialog.Builder(PedidoDetalleActivity.this);
                                    MsgPedidoDetalle11.setTitle(getString(R.string.alert_title));
                                    MsgPedidoDetalle11.setMessage(Mensaje);
                                   /* MsgPedidoDetalle11.setPositiveButton("Aceptar",
                                            new DialogInterface.OnClickListener() {

                                                public void onClick(DialogInterface dialog, int which) {
                                                    // Do nothing but close the dialog

                                                    Intent intentNavegacion = new Intent(PedidoDetalleActivity.this, NavegacionActivity.class);

                                                    Bundle bundleNavegacion = new Bundle();
                                                    bundleNavegacion.putString("PedidoMensaje", "");
                                                    intentNavegacion.putExtras(bundleNavegacion); //Put your id to your next Intent

                                                    startActivity(intentNavegacion);
                                                    finish();


                                                }
                                            });*/

                                    // Remember, create doesn't show the dialog
                                    final android.app.AlertDialog msgPedidoDetalle11 = MsgPedidoDetalle11.create();
                                    msgPedidoDetalle11.show();

                                    final Timer timer11 = new Timer();
                                    timer11.schedule(new TimerTask() {
                                        public void run() {

                                            if(msgPedidoDetalle11!=null && msgPedidoDetalle11.isShowing()){
                                                msgPedidoDetalle11.dismiss();
                                            }

                                            timer11.cancel(); //this will cancel the timer of the system

                                            Intent intentNavegacion = new Intent(PedidoDetalleActivity.this, NavegacionActivity.class);

                                            Bundle bundleNavegacion = new Bundle();
                                            bundleNavegacion.putString("PedidoMensaje", "");
                                            intentNavegacion.putExtras(bundleNavegacion); //Put your id to your next Intent

                                            startActivity(intentNavegacion);
                                            finish();


                                        }
                                    }, 1500); // the timer will count 5 seconds....


                                    break;

                                case "P012":

                                    if(timerPedidoDetalle!=null){
                                        timerPedidoDetalle.cancel();
                                    }

                                    mediaPlayerError.start();

                                   /* MediaPlayer player4 = MediaPlayer.create(PedidoDetalleActivity.this,R.raw.computer_error);
                                    player4 = MediaPlayer.create(PedidoDetalleActivity.this, R.raw.computer_error);
                                    player4.start();*/

//                                        FncMostrarMensaje("El PEDIDO fue ANULADO por el CLIENTE.",true);
                                    android.app.AlertDialog.Builder MsgRecepcionar12 = new android.app.AlertDialog.Builder(PedidoDetalleActivity.this);
                                    MsgRecepcionar12.setTitle(getString(R.string.alert_title));
                                    MsgRecepcionar12.setMessage("El pedido ha sido anulado.");
                                    /*MsgRecepcionar12.setPositiveButton("Aceptar",
                                            new DialogInterface.OnClickListener() {

                                                public void onClick(DialogInterface dialog, int which) {
                                                    // Do nothing but close the dialog

                                                    Intent intentNavegacion = new Intent(PedidoDetalleActivity.this, NavegacionActivity.class);

                                                    Bundle bundleNavegacion = new Bundle();
                                                    bundleNavegacion.putString("PedidoMensaje", "");
                                                    intentNavegacion.putExtras(bundleNavegacion); //Put your id to your next Intent

                                                    startActivity(intentNavegacion);
                                                    finish();


                                                }
                                            });*/

                                    // Remember, create doesn't show the dialog
                                    final android.app.AlertDialog msgRecepcionar12 = MsgRecepcionar12.create();
                                    msgRecepcionar12.show();

                                    final Timer timer12 = new Timer();
                                    timer12.schedule(new TimerTask() {
                                        public void run() {

                                            if(msgRecepcionar12!=null && msgRecepcionar12.isShowing()){
                                                msgRecepcionar12.dismiss();
                                            }

                                            timer12.cancel(); //this will cancel the timer of the system

                                            Intent intentNavegacion = new Intent(PedidoDetalleActivity.this, NavegacionActivity.class);

                                            Bundle bundleNavegacion = new Bundle();
                                            bundleNavegacion.putString("PedidoMensaje", "");
                                            intentNavegacion.putExtras(bundleNavegacion); //Put your id to your next Intent

                                            startActivity(intentNavegacion);
                                            finish();


                                        }
                                    }, 1500); // the timer will count 5 seconds....


                                    break;

                                case "P098":

                                    if(timerPedidoDetalle!=null){
                                        timerPedidoDetalle.cancel();
                                    }

                                    //FncMostrarMensaje("No se ha podido identificar el servicio, proceso cancelado", true);
                                    android.app.AlertDialog.Builder MsgPedidoDetalle13 = new android.app.AlertDialog.Builder(PedidoDetalleActivity.this);
                                    MsgPedidoDetalle13.setTitle(getString(R.string.alert_title));
                                    MsgPedidoDetalle13.setMessage("No se ha podido identificar su codigo de conductor, comuniquese con la central. Codigo de Error: P098");
                                    MsgPedidoDetalle13.setCancelable(false);
                                  /*  MsgPedidoDetalle13.setPositiveButton("Aceptar",
                                            new DialogInterface.OnClickListener() {

                                                public void onClick(DialogInterface dialog, int which) {

                                                    Intent intentNavegacion = new Intent(PedidoDetalleActivity.this, NavegacionActivity.class);

                                                    Bundle bundleNavegacion = new Bundle();
                                                    bundleNavegacion.putString("PedidoMensaje", "");
                                                    intentNavegacion.putExtras(bundleNavegacion); //Put your id to your next Intent

                                                    startActivity(intentNavegacion);
                                                    finish();

                                                }
                                            });*/

                                    // Remember, create doesn't show the dialog
                                    final android.app.AlertDialog msgPedidoDetalle13 = MsgPedidoDetalle13.create();
                                    msgPedidoDetalle13.show();

                                    final Timer timer13 = new Timer();
                                    timer13.schedule(new TimerTask() {
                                        public void run() {

                                            if(msgPedidoDetalle13!=null && msgPedidoDetalle13.isShowing()){
                                                msgPedidoDetalle13.dismiss();
                                            }

                                            timer13.cancel(); //this will cancel the timer of the system

                                            Intent intentNavegacion = new Intent(PedidoDetalleActivity.this, NavegacionActivity.class);

                                            Bundle bundleNavegacion = new Bundle();
                                            bundleNavegacion.putString("PedidoMensaje", "");
                                            intentNavegacion.putExtras(bundleNavegacion); //Put your id to your next Intent

                                            startActivity(intentNavegacion);
                                            finish();


                                        }
                                    }, 1500); // the timer will count 5 seconds....



                                    break;

                                case "P049":

                                    if(timerPedidoDetalle!=null){
                                        timerPedidoDetalle.cancel();
                                    }


                                    //FncMostrarMensaje("No se ha podido identificar el servicio, proceso cancelado", true);
                                    android.app.AlertDialog.Builder MsgPedidoDetalle14 = new android.app.AlertDialog.Builder(PedidoDetalleActivity.this);
                                    MsgPedidoDetalle14.setTitle(getString(R.string.alert_title));
                                    //MsgPedidoActual6.setMessage("No se ha podido identificar el servicio, se reiniciara la aplicación");
                                    MsgPedidoDetalle14.setMessage("No se ha podido identificar su codigo de pedido, comuniquese con la central. Codigo de Error: P049");
                                    MsgPedidoDetalle14.setCancelable(false);
                                   /* MsgPedidoDetalle14.setPositiveButton("Aceptar",
                                            new DialogInterface.OnClickListener() {

                                                public void onClick(DialogInterface dialog, int which) {


                                                    Intent intentNavegacion = new Intent(PedidoDetalleActivity.this, NavegacionActivity.class);

                                                    Bundle bundleNavegacion = new Bundle();
                                                    bundleNavegacion.putString("PedidoMensaje", "");
                                                    intentNavegacion.putExtras(bundleNavegacion); //Put your id to your next Intent

                                                    startActivity(intentNavegacion);
                                                    finish();



                                                }
                                            });*/

                                    // Remember, create doesn't show the dialog
                                    final android.app.AlertDialog msgPedidoDetalle14 = MsgPedidoDetalle14.create();
                                    msgPedidoDetalle14.show();

                                    final Timer timer14 = new Timer();
                                    timer14.schedule(new TimerTask() {
                                        public void run() {

                                            if(msgPedidoDetalle14!=null && msgPedidoDetalle14.isShowing()){
                                                msgPedidoDetalle14.dismiss();
                                            }

                                            timer14.cancel(); //this will cancel the timer of the system

                                            Intent intentNavegacion = new Intent(PedidoDetalleActivity.this, NavegacionActivity.class);

                                            Bundle bundleNavegacion = new Bundle();
                                            bundleNavegacion.putString("PedidoMensaje", "");
                                            intentNavegacion.putExtras(bundleNavegacion); //Put your id to your next Intent

                                            startActivity(intentNavegacion);
                                            finish();


                                        }
                                    }, 1500); // the timer will count 5 seconds....

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
    public void onClick_BtnPedidoDetalleRechazar(View v){

        // countTimerRecepcionar2.cancel();

        //SONIDO
        // MediaPlayer player = MediaPlayer.create(PedidoDetalleActivity.this, R.raw.sou_rechazar);
        //  player = MediaPlayer.create(PedidoDetalleActivity.this, R.raw.sou_rechazar);
        // player.start();

        final ProgressDialog PrgPedidoDetalle = new ProgressDialog(this);
        PrgPedidoDetalle.setIcon(R.mipmap.ic_launcher);
        PrgPedidoDetalle.setMessage("Cargando...");
        PrgPedidoDetalle.setCancelable(false);
        PrgPedidoDetalle.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        PrgPedidoDetalle.show();

        Thread nt = new Thread() {
            @Override
            public void run() {

                try {

                    final String resRechazarPedido;

                    resRechazarPedido = MtdRechazarPedido(PedidoId, ConductorId, VehiculoCoordenadaX, VehiculoCoordenadaY);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            String JsRespuesta = "";

                            try {

                                JSONObject jsonObject = new JSONObject(resRechazarPedido);
                                JsRespuesta = jsonObject.getString("Respuesta");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            PrgPedidoDetalle.cancel();

                            switch(JsRespuesta){
                                case "P060":

                                    if(timerPedidoDetalle!=null){
                                        timerPedidoDetalle.cancel();
                                    }

                                    FncMostrarToast("!Pedido rechazado!");

                                    Intent intentNavegacion = new Intent(PedidoDetalleActivity.this, NavegacionActivity.class);

                                    Bundle bundleNavegacion = new Bundle();
                                    bundleNavegacion.putString("PedidoMensaje", "");
                                    intentNavegacion.putExtras(bundleNavegacion); //Put your id to your next Intent

                                    startActivity(intentNavegacion);
                                    finish();


                                    break;

                                case "P061":

                                    if(timerPedidoDetalle!=null){
                                        timerPedidoDetalle.cancel();
                                    }

                                    mediaPlayerError.start();

                                    android.app.AlertDialog.Builder MsgPedidoDetalle61 = new android.app.AlertDialog.Builder(PedidoDetalleActivity.this);
                                    MsgPedidoDetalle61.setTitle(getString(R.string.alert_title));
                                    MsgPedidoDetalle61.setMessage("No se ha podido completar el rechazo de pedido, comuniquese con la central. Codigo de Error: P061");
                                    MsgPedidoDetalle61.setCancelable(false);
                                    /*MsgPedidoDetalle61.setPositiveButton("Aceptar",
                                            new DialogInterface.OnClickListener() {

                                                public void onClick(DialogInterface dialog, int which) {

                                                    Intent intentNavegacion = new Intent(PedidoDetalleActivity.this, NavegacionActivity.class);

                                                    Bundle bundleNavegacion = new Bundle();
                                                    bundleNavegacion.putString("PedidoMensaje", "");
                                                    intentNavegacion.putExtras(bundleNavegacion); //Put your id to your next Intent

                                                    startActivity(intentNavegacion);
                                                    finish();


                                                }
                                            });*/

                                    // Remember, create doesn't show the dialog
                                    final android.app.AlertDialog msgPedidoDetalle61 = MsgPedidoDetalle61.create();
                                    msgPedidoDetalle61.show();

                                    final Timer timer61 = new Timer();
                                    timer61.schedule(new TimerTask() {
                                        public void run() {

                                            if(msgPedidoDetalle61!=null && msgPedidoDetalle61.isShowing()){
                                                msgPedidoDetalle61.dismiss();
                                            }

                                            timer61.cancel(); //this will cancel the timer of the system

                                            Intent intentNavegacion = new Intent(PedidoDetalleActivity.this, NavegacionActivity.class);

                                            Bundle bundleNavegacion = new Bundle();
                                            bundleNavegacion.putString("PedidoMensaje", "");
                                            intentNavegacion.putExtras(bundleNavegacion); //Put your id to your next Intent

                                            startActivity(intentNavegacion);
                                            finish();


                                        }
                                    }, 1500); // the timer will count 5 seconds....


                                    break;

                                case "P062":

                                    if(timerPedidoDetalle!=null){
                                        timerPedidoDetalle.cancel();
                                    }

                                    mediaPlayerError.start();

                                    android.app.AlertDialog.Builder MsgPedidoDetalle62 = new android.app.AlertDialog.Builder(PedidoDetalleActivity.this);
                                    MsgPedidoDetalle62.setTitle(getString(R.string.alert_title));
                                    MsgPedidoDetalle62.setMessage("No se ha podido identificar su codigo de conductor, comuniquese con la central. Codigo de Error: P062");
                                    MsgPedidoDetalle62.setCancelable(false);
                                    MsgPedidoDetalle62.setPositiveButton("Aceptar",
                                            new DialogInterface.OnClickListener() {

                                                public void onClick(DialogInterface dialog, int which) {

                                                    Intent intentNavegacion = new Intent(PedidoDetalleActivity.this, NavegacionActivity.class);

                                                    Bundle bundleNavegacion = new Bundle();
                                                    bundleNavegacion.putString("PedidoMensaje", "");
                                                    intentNavegacion.putExtras(bundleNavegacion); //Put your id to your next Intent

                                                    startActivity(intentNavegacion);
                                                    finish();

                                                }
                                            });

                                    // Remember, create doesn't show the dialog
                                    final android.app.AlertDialog msgPedidoDetalle62 = MsgPedidoDetalle62.create();
                                    msgPedidoDetalle62.show();

                                    final Timer timer62 = new Timer();
                                    timer62.schedule(new TimerTask() {
                                        public void run() {

                                            if(msgPedidoDetalle62!=null && msgPedidoDetalle62.isShowing()){
                                                msgPedidoDetalle62.dismiss();
                                            }

                                            timer62.cancel(); //this will cancel the timer of the system

                                            Intent intentNavegacion = new Intent(PedidoDetalleActivity.this, NavegacionActivity.class);

                                            Bundle bundleNavegacion = new Bundle();
                                            bundleNavegacion.putString("PedidoMensaje", "");
                                            intentNavegacion.putExtras(bundleNavegacion); //Put your id to your next Intent

                                            startActivity(intentNavegacion);
                                            finish();


                                        }
                                    }, 1500); // the timer will count 5 seconds....


                                    break;

                                case "P063":

                                    if(timerPedidoDetalle!=null){
                                        timerPedidoDetalle.cancel();
                                    }

                                    android.app.AlertDialog.Builder MsgPedidoDetalle63 = new android.app.AlertDialog.Builder(PedidoDetalleActivity.this);
                                    MsgPedidoDetalle63.setTitle(getString(R.string.alert_title));
                                    MsgPedidoDetalle63.setMessage("No se ha podido identificar su codigo de pedido, comuniquese con la central. Codigo de Error: P063");
                                    MsgPedidoDetalle63.setCancelable(false);
                                   /* MsgPedidoDetalle63.setPositiveButton("Aceptar",
                                            new DialogInterface.OnClickListener() {

                                                public void onClick(DialogInterface dialog, int which) {

                                                    Intent intentNavegacion = new Intent(PedidoDetalleActivity.this, NavegacionActivity.class);

                                                    Bundle bundleNavegacion = new Bundle();
                                                    bundleNavegacion.putString("PedidoMensaje", "");
                                                    intentNavegacion.putExtras(bundleNavegacion); //Put your id to your next Intent

                                                    startActivity(intentNavegacion);
                                                    finish();

                                                }
                                            });*/

                                    // Remember, create doesn't show the dialog
                                    final android.app.AlertDialog msgPedidoDetalle63 = MsgPedidoDetalle63.create();
                                    msgPedidoDetalle63.show();

                                    final Timer timer63 = new Timer();
                                    timer63.schedule(new TimerTask() {
                                        public void run() {

                                            if(msgPedidoDetalle63!=null && msgPedidoDetalle63.isShowing()){
                                                msgPedidoDetalle63.dismiss();
                                            }

                                            timer63.cancel(); //this will cancel the timer of the system

                                            Intent intentNavegacion = new Intent(PedidoDetalleActivity.this, NavegacionActivity.class);

                                            Bundle bundleNavegacion = new Bundle();
                                            bundleNavegacion.putString("PedidoMensaje", "");
                                            intentNavegacion.putExtras(bundleNavegacion); //Put your id to your next Intent

                                            startActivity(intentNavegacion);
                                            finish();


                                        }
                                    }, 1500); // the timer will count 5 seconds....


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




    /*
    ENVIO VARIABLES
     */
    public String MtdAceptarPedido(String oPedidoId , String oConductorId, String oEmpresaId,String oVehiculoCoordenadaX,String oVehiculoCoordenadaY) {

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
            postDataParams.put("EmpresaId", oEmpresaId);


            postDataParams.put("VehiculoCoordenadaX", oVehiculoCoordenadaX);
            postDataParams.put("VehiculoCoordenadaY", oVehiculoCoordenadaY);
            postDataParams.put("AppVersion", getString(R.string.app_version));

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

            Log.e("MsgPedidoDetalle2", response);

        } catch (Exception e) {

            Log.e("MsgPedidoDetalle3", e.toString());
            e.printStackTrace();
        }

        return response;

    }


    public String MtdRechazarPedido(String oPedidoId , String oConductorId,String oVehiculoCoordenadaX,String oVehiculoCoordenadaY) {

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
            postDataParams.put("AppVersion", getString(R.string.app_version));

            postDataParams.put("Identificador", Identificador);
            postDataParams.put("Accion", "RechazarPedido");

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

/*            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line=br.readLine()) != null) {
                    response+=line;
                }
            }
            else {
                response="";

            }
*/
            Log.e("MsgPedidoDetalle5", response);

        } catch (Exception e) {

            Log.e("MsgPedidoDetalle6", e.toString());
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

            Log.e("MsgPedidoDetalle9", response);

        } catch (Exception e) {

            Log.e("MsgPedidoDetalle10", e.toString());
            e.printStackTrace();
        }

        return response;

    }


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

        EmpresaId = sharedPreferences.getString("EmpresaId","");

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
//        String parameters = str_origin+"&"+str_dest+"&"+sensor;
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
                lineOptions.width(10);
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
