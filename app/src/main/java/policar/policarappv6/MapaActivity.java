package policar.policarappv6;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
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
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.net.ssl.HttpsURLConnection;

public class MapaActivity extends AppCompatActivity {

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
    private Marker pedidoMarker;
    private Marker vehiculoMarker;
    private boolean CamaraSeguir;
    private float currentZoom;

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
    * TIMERS
     */

    Timer timerMapa1;
    Timer timerMapa2;

    private int ConductorActivos;
    private int ConductorInactivos;


    /*
    * OPCIONES
     */
    private Switch SwiCamaraSeguir;


    /*
    * REGION
     */

    private String RegionId = "";
    private String RegionNombre = "";

    private String SectorId = "";
    private String SectorNombre = "";

    private String EmpresaId = "";
    private String EmpresaNombre = "";

    /*
    * PREFERENCIAS
    * */
    SharedPreferences sharedPreferences2;


    /*
    * ALERTA
     */

    public String ConductorAlerta = "2";
    public String ConductorAlertaCoordenadaX = "0.00";
    public String ConductorAlertaCoordenadaY = "0.00";


    TextView txtConductorActivosTotal;


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("Navegacion20", "Destroy");


        if(timerMapa1!=null) {
            timerMapa1.cancel();
        }


        if(timerMapa2!=null) {
            timerMapa2.cancel();
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        //CREANDO BARRA
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        //actionBar.setDisplayShowHomeEnabled(true);
        // actionBar.setIcon(R.mipmap.ic_launcher);             //Establecer icono
        //actionBar.setTitle("Servicio Actual");        //Establecer titulo
        actionBar.setTitle(getString(R.string.title_activity_mapa));     //Establecer Subtitulo

        //PERMISOS
        context = getApplicationContext();
        activity = this;


        //PREFERENCIAS
        sharedPreferences2 = getSharedPreferences("policar.policarappv6", Context.MODE_PRIVATE);

        displayUserSettings();

        //OBTENER COORDENADAS
        if(checkPermission(2)){
            MtdObtenerCoordenadas();
        }

        //CREANDO MAPA
        if(checkPermission(1)){
            createMapView();
        }

        CamaraSeguir = true;
        currentZoom = 19;

        //CONTADOR CONDUCTORES
        ConductorActivos = 0;
        ConductorInactivos = 0;


        //RECUPERANDO VARIABLES
        Intent intentExtras = getIntent();
        Bundle extrasBundle = intentExtras.getExtras();

        ConductorId = intentExtras.getStringExtra("ConductorId");
        ConductorAlertaCoordenadaX = intentExtras.getStringExtra("ConductorAlertaCoordenadaX");
        ConductorAlertaCoordenadaY = intentExtras.getStringExtra("ConductorAlertaCoordenadaY");
        ConductorAlerta = intentExtras.getStringExtra("ConductorAlerta");

        txtConductorActivosTotal = (TextView) findViewById(R.id.CmpConductorActivosTotal);

        if(ConductorAlerta.equals("1")){
            //TipoCamara = 2;

            if(null != googleMap){

                FncMostrarToast("Ubicando alerta...");

                if(!ConductorAlertaCoordenadaX.equals("") && !ConductorAlertaCoordenadaY.equals("") && !ConductorAlertaCoordenadaX.equals("0.00") && !ConductorAlertaCoordenadaY.equals("0.00") && ConductorAlertaCoordenadaX != null  && ConductorAlertaCoordenadaY != null){

                    //TipoCamara = 2;

                    LatLng latLng = new LatLng(Double.parseDouble(ConductorAlertaCoordenadaX),Double.parseDouble(ConductorAlertaCoordenadaY));
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(latLng)      // Sets the center of the map to Mountain View
                            .zoom(currentZoom)                   // Sets the zoom
                            ////.bearing(90)                // Sets the orientation of the camera to east
                            .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                            .build();                   // Creates a CameraPosition from the builder
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


                }

            }

        }else{
            //TipoCamara = 1;

        }


        SwiCamaraSeguir = (Switch) findViewById(R.id.SwiCamaraSeguir);

// set the switch to ON
        //SwiMonitoreoEncendido.setChecked(true);
// attach a listener to check for changes in state
        SwiCamaraSeguir.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                //SharedPreferences sharedPreferences = getSharedPreferences("policar.policarappv6", Context.MODE_PRIVATE);
                //SharedPreferences.Editor editor = sharedPreferences.edit();

                if (isChecked) {
                    Log.e("MisPreferencias", "verda");
                    //editor.putBoolean("MonitoreoEncendido", true);
                    CamaraSeguir = true;
                    FncMostrarToast("¡Camara de seguimiento activada!");

                } else {
                    Log.e("MisPreferencias","falso");
                   // editor.putBoolean("MonitoreoEncendido", false);
                    CamaraSeguir = false;
                    FncMostrarToast("¡Camara de seguimiento desactivada!");
                    // Toast.makeText(getApplicationContext(),
                    //         “The switch is OFF”,Toast.LENGTH_SHORT).show();
                }

                //editor.apply();

            }
        });

        if(CamaraSeguir){
            SwiCamaraSeguir.setChecked(true);
        }else{
            SwiCamaraSeguir.setChecked(false);
        }





        if(timerMapa1!=null) {
            timerMapa1.cancel();
        }
        //TAREA ESPERAR PEDIDO

        timerMapa1 = new Timer();
        timerMapa1.schedule(new TimerTask(){
            @Override
            public void run() {

                // if( estaConectado(false)){

                try {

                    // final ArrayList<ConductoresResults> conductoresResults = new ArrayList<ConductoresResults>();

                    final String resMtdObtenerMapa;
                    resMtdObtenerMapa = MtdObtenerMapa(ConductorId,EmpresaId,EmpresaNombre,RegionId,RegionNombre,SectorId,SectorNombre,VehiculoCoordenadaX,VehiculoCoordenadaY);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            String JsRespuesta = "";
                            JSONArray JsDatos;

                            if (googleMap != null) {
                                googleMap.clear();
                            }

                            ConductorInactivos = 0;
                            ConductorActivos = 1;

                            if(!VehiculoCoordenadaX.equals("") && !VehiculoCoordenadaY.equals("")   && VehiculoCoordenadaX != null && VehiculoCoordenadaY != null){

                                if(vehiculoMarker!=null){
                                    vehiculoMarker.remove();
                                }

                                vehiculoMarker = googleMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(Double.parseDouble(VehiculoCoordenadaX), Double.parseDouble(VehiculoCoordenadaY)))
                                        .title("¡Aquì estoy!")
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

                                                    ConductorActivos = ConductorActivos + 1;
                                                    break;

                                                case "7":

                                                    googleMap.addMarker(new MarkerOptions()
                                                            .position(new LatLng(Double.parseDouble(JsDestinoCoordenadaX), Double.parseDouble(JsDestinoCoordenadaY)))
                                                            .title(JsDestinoNombre)
                                                            .draggable(false)
                                                            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_taxi_alerta150)));

                                                    ConductorActivos = ConductorActivos + 1;
                                                    break;

                                                default:

                                                    break;



                                            }


                                        }



                                    }

                                   /* ConductoresResults sr1 = new ConductoresResults();

                                    sr1.setConductorId(JsConductorId);

                                    sr1.setConductorNombre(JsConductorNombre);
                                    sr1.setVehiculoUnidad(JsVehiculoUnidad);
                                    sr1.setVehiculoCoordenadaX(JsVehiculoCoordenadaX);
                                    sr1.setVehiculoCoordenadaY(JsVehiculoCoordenadaY);

                                    conductoresResults.add(sr1);*/
                                }

                                switch(JsRespuesta){

                                    case "M001":

                                        txtConductorActivosTotal.setText(Integer.toString(ConductorActivos));

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




        if(timerMapa2!=null){
            timerMapa2.cancel();
        }

        //TAREA ENVIAR COORDENADAS - INICIO

        //final Timer timer3 = new Timer();
        timerMapa2 = new Timer();
        timerMapa2.schedule(new TimerTask(){
            @Override
            public void run() {

                SharedPreferences sharedPreferences = getSharedPreferences("policar.policarappv6", Context.MODE_PRIVATE);
                Integer conductorOcupado = sharedPreferences.getInt("ConductorOcupado", 2);

                if(conductorOcupado == 2){

                    // if(estaConectado(false)){

                    try {
                        //private String VehiculoDireccionActual = "";
                        final String resActualizarConductorCoordenada;
                        resActualizarConductorCoordenada = MtdActualizarConductorCoordenada("",ConductorId,ConductorNombre,ConductorNumeroDocumento,VehiculoCoordenadaX, VehiculoCoordenadaY,VehiculoUnidad,VehiculoPlaca);

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
        }, 1000, 3000);



    }

    @Override
    public void onBackPressed() {

        /*DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }*/

        Intent intentMain = new Intent(MapaActivity.this, MainActivity.class);
        startActivity(intentMain);
        finish();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
       /* if (id == R.id.action_settings) {
            return true;
        }*/

        if (id == android.R.id.home) {

            Intent intentMain = new Intent(MapaActivity.this, MainActivity.class);
            startActivity(intentMain);
            finish();

            return true;
        }

        return super.onOptionsItemSelected(item);
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

                                VehiculoCoordenadaX = Double.toString(location.getLatitude());
                                VehiculoCoordenadaY = Double.toString(location.getLongitude());

                                Thread nt = new Thread() {
                                    @Override
                                    public void run() {

                                        //if(estaConectado(false)){

                                        try {

                                            final String resActualizarConductorCoordenada;
                                            resActualizarConductorCoordenada = MtdActualizarConductorCoordenada("", ConductorId,ConductorNombre,ConductorNumeroDocumento, VehiculoCoordenadaX, VehiculoCoordenadaY,VehiculoUnidad,VehiculoPlaca);

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

                                return false;
                            }
                        }


                );


                googleMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
                    @Override
                    public void onMyLocationChange(Location location) {

                        VehiculoCoordenadaX = Double.toString(location.getLatitude());
                        VehiculoCoordenadaY = Double.toString(location.getLongitude());

                        if(!VehiculoCoordenadaX.equals("") && !VehiculoCoordenadaY.equals("")  &&  VehiculoCoordenadaX !=null && VehiculoCoordenadaY != null){

                            if(vehiculoMarker!=null){
                                vehiculoMarker.remove();
                            }

                            vehiculoMarker = googleMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(Double.parseDouble(VehiculoCoordenadaX), Double.parseDouble(VehiculoCoordenadaY)))
                                    .title("¡Aquì estoy!")
                                    .draggable(false)
                                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_taxi150)));

                            if(CamaraSeguir){

                                LatLng latLng = new LatLng(Double.parseDouble(VehiculoCoordenadaX),Double.parseDouble(VehiculoCoordenadaY));
                                CameraPosition cameraPosition = new CameraPosition.Builder()
                                        .target(latLng)      // Sets the center of the map to Mountain View
                                        .zoom(currentZoom)                   // Sets the zoom
                                        //.bearing(20)  //era 90              // Sets the orientation of the camera to east
                                        .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                                        .build();                   // Creates a CameraPosition from the builder
                                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                            }


                        }

                    }
                });

                googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {

                   // private float currentZoom = -1;
                    @Override
                    public void onCameraChange(CameraPosition pos) {
                        if (pos.zoom != currentZoom){
                            currentZoom = pos.zoom;
                            // do you action here
                            Log.e("ZOOM", String.valueOf(currentZoom));

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
                                        .zoom(19)                   // Sets the zoom
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

            Log.e("Mapa90", exception.toString());

        }

    }



    private boolean checkPermission(int permiso) {
        Log.e("Mapa10","VERIFICAR PERMISO");
        boolean respuesta = false;

        int MyVersion = Build.VERSION.SDK_INT;

        if (MyVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {

            switch(permiso){
                case 1:

                    int result1 = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);

                    if (result1 == PackageManager.PERMISSION_GRANTED) {
                        Log.e("Mapa10","1AAA");
                        respuesta = true;
                    }else {
                        respuesta = false;
                        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, permiso);
                        Log.e("Mapa10", "1BBB");
                    }

                    break;

                case 2:

                    int result2 = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION);

                    if (result2 == PackageManager.PERMISSION_GRANTED) {
                        Log.e("Mapa10","2AAA");
                        respuesta = true;
                    }else {
                        respuesta = false;
                        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, permiso);
                        Log.e("Mapa10", "2BBB");
                    }

                    break;

                case 3:

                    int result3 = ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO);

                    if (result3 == PackageManager.PERMISSION_GRANTED) {
                        Log.e("Mapa10","3AAA");
                        respuesta = true;
                    }else {
                        respuesta = false;
                        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.RECORD_AUDIO}, permiso);
                        Log.e("Mapa10", "3BBB");
                    }

                    break;

                case 4:

                    int result4 = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);

                    if (result4 == PackageManager.PERMISSION_GRANTED) {
                        Log.e("Mapa10","4AAA");
                        respuesta = true;
                    }else {
                        respuesta = false;
                        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, permiso);
                        Log.e("Mapa10", "4BBB");
                    }

                    break;

                case 5:

                    int result5 = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE);

                    if (result5 == PackageManager.PERMISSION_GRANTED) {
                        Log.e("Mapa10","5AAA");
                        respuesta = true;
                    }else {
                        respuesta = false;
                        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, permiso);
                        Log.e("Mapa10", "5BBB");
                    }

                    break;

                case 6:

                    int result6 = ContextCompat.checkSelfPermission(context, Manifest.permission.VIBRATE);

                    if (result6 == PackageManager.PERMISSION_GRANTED) {
                        Log.e("Mapa10","5AAA");
                        respuesta = true;
                    }else {
                        respuesta = false;
                        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.VIBRATE}, permiso);
                        Log.e("Mapa10", "5BBB");
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

            Log.e("Mapa10","PERMISO ACEPTADO");

            switch (requestCode) {
                case 1:
                    createMapView();
                    break;

                case 2:
                    MtdObtenerCoordenadas();
                    break;

            }

        } else {
            Log.e("Mapa10","PERMISO NEGADO");
            FncMostrarToast("Permiso denegado, es posible que la aplicacion no funcione  correctamente.");
        }

    }



    public void MtdObtenerCoordenadas() {

        //OBTENIENDO UBICACION
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //Toast.makeText(this, "GPS is Enabled in your devide", Toast.LENGTH_SHORT).show();

            MapaListener mlocListener = new MapaListener();
            mlocListener.setMapaActivity(this);
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

        Toast.makeText(MapaActivity.this, oMensaje,
                Toast.LENGTH_LONG).show();
    }


    protected void displayNotificationOne(String oTitulo,String oContenido,String oContenidoGrande) {

        int notificationId = 001;

        // Build intent for notification content
        Intent viewIntent = new Intent(MapaActivity.this, MainActivity.class);
        //viewIntent.putExtra(EXTRA_EVENT_ID, eventId);
        PendingIntent viewPendingIntent =
                PendingIntent.getActivity(MapaActivity.this, 0, viewIntent, 0);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(MapaActivity.this)
                        .setSmallIcon(policar.policarappv6.R.mipmap.ic_launcher)
                        .setContentTitle(oTitulo)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(oContenidoGrande))
                        .setContentText(oContenido)
                        //.setVibrate(new long[] { 100, 250 })
                        .setDefaults(Notification.DEFAULT_SOUND).setContentIntent(viewPendingIntent)
                ;

        //.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000}).build();

        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(MapaActivity.this);

        notificationManager.notify(notificationId, notificationBuilder.build());

    }






    public String MtdActualizarConductorCoordenada(String oPedidoId, String oConductorId,String oConductorNombre,String oConductorNumeroDocumento,String oConductorCoordenadaX,String oConductorCoordenadaY,String oVehiculoUnidad,String oVehiculoPlaca) {

        URL url;
        String response = "";

        try {

            url = new URL(getString(R.string.app_url2)+"/webservice/"+getString(R.string.webservice_jnconductorubicacion));

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            HashMap<String, String>postDataParams=new HashMap<>();
            postDataParams.put("PedidoId", oPedidoId);

            postDataParams.put("ConductorId", oConductorId);
            postDataParams.put("ConductorNombre", oConductorNombre);
            postDataParams.put("ConductorNumeroDocumento", oConductorNumeroDocumento);

            postDataParams.put("VehiculoUnidad", oVehiculoUnidad);
            postDataParams.put("VehiculoPlaca", oVehiculoPlaca);
            postDataParams.put("VehiculoCoordenadaX", oConductorCoordenadaX);
            postDataParams.put("VehiculoCoordenadaY", oConductorCoordenadaY);
            postDataParams.put("Formulario","MapaActivity");

            postDataParams.put("Identificador", Identificador);
            postDataParams.put("Accion", "ActualizarConductorCoordenada");

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

            Log.e("Mapa3", response);

        } catch (Exception e) {

            Log.e("Mapa4", e.toString());
            e.printStackTrace();
        }

        return response;

    }



    public String MtdObtenerMapa(String oConductorId,String oEmpresaId,String oEmpresaNombre, String oRegionId,String oRegionNombre,String oSectorId,String oSectorNombre,String oCoordenadaX,String oCoordenadaY) {



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
            postDataParams.put("EmpresaId", oEmpresaId);
            postDataParams.put("EmpresaNombre", oEmpresaNombre);

            postDataParams.put("RegionId", oRegionId);
            postDataParams.put("RegionNombre", oRegionNombre);

            postDataParams.put("SectorId", oRegionId);
            postDataParams.put("SectorNombre", oSectorNombre);

            postDataParams.put("CoordenadaX", oCoordenadaX);
            postDataParams.put("CoordenadaY", oCoordenadaY);

            postDataParams.put("Cercanos", "2");

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

            Log.e("Mapa1", response);

        } catch (Exception e) {

            Log.e("Mapa2", e.toString());
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

        RegionId = sharedPreferences.getString("RegionId","");
        RegionNombre = sharedPreferences.getString("RegionNombre","");

        EmpresaId = sharedPreferences.getString("EmpresaId","");
        EmpresaNombre = sharedPreferences.getString("EmpresaNombre","");

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
        String parameters = str_origin+"&"+str_dest+"&"+sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;

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





    private class GetImages extends AsyncTask<Object, Object, Object> {
        private String requestUrl, imagename_;
        private ImageView view;
        private Bitmap bitmap ;
        private FileOutputStream fos;
        //private GetImages(String requestUrl, ImageView view, String _imagename_) {
        private GetImages(String requestUrl, String _imagename_) {
            this.requestUrl = requestUrl;
            this.view = view;
            this.imagename_ = _imagename_ ;
        }

        @Override
        protected Object doInBackground(Object... objects) {
            try {
                URL url = new URL(requestUrl);
                URLConnection conn = url.openConnection();
                bitmap = BitmapFactory.decodeStream(conn.getInputStream());
            } catch (Exception ex) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            if(!ImageStorage.checkifImageExists(imagename_))
            {
                //view.setImageBitmap(bitmap);
                ImageStorage.saveToSdCard(bitmap, imagename_);
            }
        }
    }







}
