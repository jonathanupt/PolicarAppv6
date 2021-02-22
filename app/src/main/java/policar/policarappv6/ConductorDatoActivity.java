package policar.policarappv6;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class ConductorDatoActivity extends AppCompatActivity
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

    private String VehiculoUnidad = "";
    private String VehiculoPlaca = "";
    private String VehiculoColor = "";
    private String VehiculoModelo = "";
    private String VehiculoTipo = "";

    private String VehiculoCoordenadaX = "0.00";
    private String VehiculoCoordenadaY = "0.00";

    private String RegionId = "";
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


    TextView txtConductorNombre;
    TextView txtConductorNumeroDocumento;
    TextView txtConductorCelular;
    TextView txtVehiculoUnidad;
    TextView txtVehiculoPlaca;
    TextView txtVehiculoModelo;
    TextView txtVehiculoColor;


    TextView txtLicenciaFechaFin;
    TextView txtSOATFEchaFin;
    TextView txtRevisionTecnicaFechaFin;
    TextView txtCredencialFechaFin;
    TextView txtCredencialConductorFechaFin;

    TextView txtEmpresaNombre;

    ImageView imgConductorFoto;
    ImageView imgEmpresaFoto;


    //ONSTART -> ON CREATE -> ONRESTART
    @Override
    protected void onStart() {
        super.onStart();
        Log.e("ConductorDato20", "Start");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("ConductorDato20", "Resume");

    }

    //ONPAUSE -> ONRESUME -> ONSTOP
    @Override
    protected void onPause() {
        super.onPause();
        Log.e("ConductorDato20", "Pause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("ConductorDato20", "Stop");
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("ConductorDato20", "Restart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("ConductorDato20", "Destroy");

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e("ConductorDato20", "SaveInstance");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.e("ConductorDato20", "RestoreInstance");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conductor_dato);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Log.e("ConductorDato20", "onCreate");

      /*  FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

     /*   DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);*/

        //CREANDO BARRA
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        //actionBar.setDisplayShowHomeEnabled(true);
      //  getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //actionBar.setIcon(R.mipmap.ic_launcher);             //Establecer icono
        //actionBar.setTitle("Mis Datos");        //Establecer titulo
         actionBar.setTitle(getString(R.string.title_activity_conductor_dato));     //Establecer Subtitulo


        //PERMISOS
        context = getApplicationContext();
        activity = this;

        //RECUPERANDO VARIABLES
        displayUserSettings();

        //CAB MENU
      /*  View header = navigationView.getHeaderView(0);
        TextView txtUsuario = (TextView) header.findViewById(R.id.CmpUsuario);
        txtUsuario.setText(ConductorNombre);
*/


        //CARGANDO FORMULARIO

        txtConductorNombre = (TextView) findViewById(R.id.CmpConductorNombre);
        txtConductorNumeroDocumento = (TextView) findViewById(R.id.CmpConductorNumeroDocumento);
        txtConductorCelular = (TextView) findViewById(R.id.CmpConductorCelular);
        txtVehiculoUnidad = (TextView) findViewById(R.id.CmpVehiculoUnidad);
        txtVehiculoPlaca = (TextView) findViewById(R.id.CmpVehiculoPlaca);
        txtVehiculoModelo = (TextView) findViewById(R.id.CmpVehiculoModelo);
        txtVehiculoColor = (TextView) findViewById(R.id.CmpVehiculoColor);

        txtLicenciaFechaFin = (TextView) findViewById(R.id.CmpConductorDatoLicenciaFechaFin);
        txtSOATFEchaFin = (TextView) findViewById(R.id.CmpConductorDatoSOATFechaFin);
        txtRevisionTecnicaFechaFin = (TextView) findViewById(R.id.CmpConductorDatoRevisionTecnicaFechaFin);
        txtCredencialFechaFin = (TextView) findViewById(R.id.CmpConductorDatoCredencialFechaFin);
        txtCredencialConductorFechaFin = (TextView) findViewById(R.id.CmpConductorDatoCredencialConductorFechaFin);

        txtEmpresaNombre = (TextView) findViewById(R.id.CmpConductorDatoEmpresaNombre);

        imgConductorFoto = (ImageView) findViewById(R.id.ImgConductorDatoFoto);
        imgEmpresaFoto = (ImageView) findViewById(R.id.ImgConductorDatoEmpresaFoto);

//MOSTRANDO VARIABLES

        txtConductorNombre.setText(ConductorNombre);
        txtConductorNumeroDocumento.setText(ConductorNumeroDocumento);
        txtConductorCelular.setText(ConductorCelular);
        txtVehiculoUnidad.setText(VehiculoUnidad);
        txtVehiculoPlaca.setText(VehiculoPlaca);
        txtVehiculoModelo.setText(VehiculoModelo);
        txtVehiculoColor.setText(VehiculoColor);

        txtLicenciaFechaFin.setText(ConductorLicenciaFechaFin);
        txtSOATFEchaFin.setText(ConductorSOATFechaFin);
        txtRevisionTecnicaFechaFin.setText(ConductorRevisionTecnicaFechaFin);
        txtCredencialFechaFin.setText(ConductorCredencialFechaFin);
        txtCredencialConductorFechaFin.setText(ConductorCredencialConductorFechaFin);

        txtEmpresaNombre.setText(EmpresaNombre);

        imgConductorFoto.setImageResource(R.drawable.foto_conductor);
        imgEmpresaFoto.setImageResource(R.mipmap.ic_launcher);

        if(!"".equals(ConductorFoto)){
            new DownloadImageTask(imgConductorFoto).execute(ConductorFoto);
            //new DownloadImageTask((ImageView) findViewById(R.id.imageView6)).execute(ConductorFoto);
        }

        if(!"".equals(EmpresaFoto)){
            new DownloadImageTask(imgEmpresaFoto).execute(EmpresaFoto);
            //new DownloadImageTask((ImageView) findViewById(R.id.imageView6)).execute(ConductorFoto);
        }

        imgConductorFoto.setClickable(true);
        //finish the activity (dismiss the image dialog) if the user clicks
        //anywhere on the image
        imgConductorFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                android.app.AlertDialog.Builder imageDialog = new android.app.AlertDialog.Builder(ConductorDatoActivity.this);
                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

                View layout = inflater.inflate(R.layout.conductor_foto,
                        (ViewGroup) findViewById(R.id.layout_root));
                ImageView image = (ImageView) layout.findViewById(R.id.fullimage);
               // image.setMaxHeight(300);
               // image.setLa
                image.setImageDrawable(imgConductorFoto.getDrawable());
                imageDialog.setView(layout);
                /*imageDialog.setPositiveButton("Cerrar", new DialogInterface.OnClickListener(){

                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }

                });*/

                imageDialog.create();
                imageDialog.show();

            }
        });


        //CONDUCTOR FOTO
       /* DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        double ancho =  (metrics.widthPixels) - (metrics.widthPixels*(0.2));
        double alto =  metrics.heightPixels  - (metrics.heightPixels*(0.4));

        int n_ancho = (int) ancho;
        int n_alto = (int) alto;

        final ImagePopup imagePopup = new ImagePopup(this);
        imagePopup.setBackgroundColor(Color.BLACK);
        imagePopup.setWindowWidth(n_ancho);
        imagePopup.setWindowHeight(n_alto);
        imagePopup.setHideCloseIcon(true);
        imagePopup.setImageOnClickClose(true);
        imagePopup.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imagePopup.setBaselineAlignBottom(true);

        imgConductorFoto = (ImageView) findViewById(R.id.ImgConductorFoto);

        imgConductorFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(imgConductorFoto.getDrawable()!=null){
                    imagePopup.initiatePopup(imgConductorFoto.getDrawable());
                }

            }
        });*/

        if(ConductorLicenciaFechaFinNivel.equals("1")){
            txtLicenciaFechaFin.setTextColor(Color.BLUE);
        }else if(ConductorLicenciaFechaFinNivel.equals("2")){
            txtLicenciaFechaFin.setTextColor(Color.RED);
        }

        if(ConductorSOATFechaFinNivel.equals("1")){
            txtSOATFEchaFin.setTextColor(Color.BLUE);
        }else if(ConductorSOATFechaFinNivel.equals("2")){
            txtSOATFEchaFin.setTextColor(Color.RED);
        }
        if(ConductorCredencialFechaFinNivel.equals("1")){
            txtCredencialFechaFin.setTextColor(Color.BLUE);
        }else if(ConductorCredencialFechaFinNivel.equals("2")){
            txtCredencialFechaFin.setTextColor(Color.RED);
        }

        if(ConductorCredencialConductorFechaFinNivel.equals("1")){
            txtCredencialConductorFechaFin.setTextColor(Color.BLUE);
        }else if(ConductorCredencialConductorFechaFinNivel.equals("2")){
            txtCredencialConductorFechaFin.setTextColor(Color.RED);
        }



        if(ConductorRevisionTecnicaFechaFinNivel.equals("1")){
            txtCredencialConductorFechaFin.setTextColor(Color.BLUE);
        }else if(ConductorRevisionTecnicaFechaFinNivel.equals("2")){
            txtCredencialConductorFechaFin.setTextColor(Color.RED);
        }



        final ProgressDialog prgConductorDato = new ProgressDialog(this);
        prgConductorDato.setIcon(R.mipmap.ic_launcher);
        prgConductorDato.setMessage("Cargando...");
        prgConductorDato.setCancelable(false);
        prgConductorDato.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        prgConductorDato.show();

        Thread nt = new Thread() {
            @Override
            public void run() {

                try {

                    final String resObtenerConductor;

                    resObtenerConductor = MtdObtenerConductor(ConductorId);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            String JsRespuesta = "";


                            String JsIdentificador = "";

                            String JsRegionId = "";

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

                                JSONObject jsonObject = new JSONObject(resObtenerConductor);
                                JsRespuesta = jsonObject.getString("Respuesta");

                                JsRegionId = jsonObject.getString("RegionId");

                                JsConductorId = jsonObject.getString("ConductorId");

                                JsEmpresaId = jsonObject.getString("EmpresaId");
                                        JsEmpresaNombre = jsonObject.getString("EmpresaNombre");
                                JsEmpresaFoto = jsonObject.getString("EmpresaFoto");

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

                            prgConductorDato.cancel();

                            switch(JsRespuesta){
                                case "C005":

                                    FncMostrarToast("Se actualizó su información correctamente");

                                    RegionId = JsRegionId;

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

                                    //MOSTRANDO VARIABLES
                                    txtConductorNombre.setText(ConductorNombre);
                                    txtConductorNumeroDocumento.setText(ConductorNumeroDocumento);
                                    txtConductorCelular.setText(ConductorCelular);
                                    txtVehiculoUnidad.setText(VehiculoUnidad);
                                    txtVehiculoPlaca.setText(VehiculoPlaca);
                                    txtVehiculoModelo.setText(VehiculoModelo);
                                    txtVehiculoColor.setText(VehiculoColor);

                                    txtLicenciaFechaFin.setText(ConductorLicenciaFechaFin);
                                    txtSOATFEchaFin.setText(ConductorSOATFechaFin);
                                    txtRevisionTecnicaFechaFin.setText(ConductorRevisionTecnicaFechaFin);
                                    txtCredencialFechaFin.setText(ConductorCredencialFechaFin);
                                    txtCredencialConductorFechaFin.setText(ConductorCredencialConductorFechaFin);

                                    txtEmpresaNombre.setText(EmpresaNombre);

                                    imgConductorFoto.setImageResource(R.drawable.foto_conductor);
                                    imgEmpresaFoto.setImageResource(R.mipmap.ic_launcher);

                                    if(!"".equals(ConductorFoto)){
                                        new DownloadImageTask(imgConductorFoto).execute(ConductorFoto);
                                    }

                                    if(!"".equals(EmpresaFoto)){
                                        new DownloadImageTask(imgEmpresaFoto).execute(EmpresaFoto);
                                    }

                                    if(ConductorLicenciaFechaFinNivel.equals("1")){
                                        txtLicenciaFechaFin.setTextColor(Color.BLUE);
                                    }else if(ConductorLicenciaFechaFinNivel.equals("2")){
                                        txtLicenciaFechaFin.setTextColor(Color.RED);
                                    }

                                    if(ConductorSOATFechaFinNivel.equals("1")){
                                        txtSOATFEchaFin.setTextColor(Color.BLUE);
                                    }else if(ConductorSOATFechaFinNivel.equals("2")){
                                        txtSOATFEchaFin.setTextColor(Color.RED);
                                    }
                                    if(ConductorCredencialFechaFinNivel.equals("1")){
                                        txtCredencialFechaFin.setTextColor(Color.BLUE);
                                    }else if(ConductorCredencialFechaFinNivel.equals("2")){
                                        txtCredencialFechaFin.setTextColor(Color.RED);
                                    }

                                    if(ConductorCredencialConductorFechaFinNivel.equals("1")){
                                        txtCredencialConductorFechaFin.setTextColor(Color.BLUE);
                                    }else if(ConductorCredencialConductorFechaFinNivel.equals("2")){
                                        txtCredencialConductorFechaFin.setTextColor(Color.RED);
                                    }

                                    if(ConductorRevisionTecnicaFechaFinNivel.equals("1")){
                                        txtCredencialConductorFechaFin.setTextColor(Color.BLUE);
                                    }else if(ConductorRevisionTecnicaFechaFinNivel.equals("2")){
                                        txtCredencialConductorFechaFin.setTextColor(Color.RED);
                                    }

                                    break;

                                case "C006":
                                    //FncMostrarToast("");
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
        getMenuInflater().inflate(R.menu.conductor_dato, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //if (id == R.id.action_sincronizar) {

           /* final ProgressDialog prgConductorDato = new ProgressDialog(this);
            prgConductorDato.setIcon(R.mipmap.ic_launcher);
            prgConductorDato.setMessage("Cargando...");
            prgConductorDato.setCancelable(false);
            prgConductorDato.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            prgConductorDato.show();

            Thread nt = new Thread() {
                @Override
                public void run() {

                    try {

                        final String resObtenerConductor;

                        resObtenerConductor = MtdObtenerConductor(ConductorId);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                String JsRespuesta = "";

                                String JsIdentificador = "";

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

                                try {

                                    JSONObject jsonObject = new JSONObject(resObtenerConductor);
                                    JsRespuesta = jsonObject.getString("Respuesta");

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

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                prgConductorDato.cancel();

                                switch(JsRespuesta){
                                    case "C005":

                                        FncMostrarToast("Se actualizó su información correctamente");

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

                                        saveUserSettings();


                                        //MOSTRANDO VARIABLES

                                        TextView TxtConductorNombre = (TextView) findViewById(R.id.CmpConductorNombre);
                                        TxtConductorNombre.setText(ConductorNombre);

                                        TextView TxtConductorNumeroDocumento = (TextView) findViewById(R.id.CmpConductorNumeroDocumento);
                                        TxtConductorNumeroDocumento.setText(ConductorNumeroDocumento);

                                        TextView TxtConductorCelular = (TextView) findViewById(R.id.CmpConductorCelular);
                                        TxtConductorCelular.setText(ConductorCelular);


                                        TextView TxtVehiculoUnidad = (TextView) findViewById(R.id.CmpVehiculoUnidad);
                                        TxtVehiculoUnidad.setText(VehiculoUnidad);

                                        TextView TxtVehiculoPlaca = (TextView) findViewById(R.id.CmpVehiculoPlaca);
                                        TxtVehiculoPlaca.setText(VehiculoPlaca);

                                        TextView TxtVehiculoModelo = (TextView) findViewById(R.id.CmpVehiculoModelo);
                                        TxtVehiculoModelo.setText(VehiculoModelo);

                                        TextView TxtVehiculoColor = (TextView) findViewById(R.id.CmpVehiculoColor);
                                        TxtVehiculoColor.setText(VehiculoColor);

                                        if(!"".equals(ConductorFoto)){
                                            new DownloadImageTask((ImageView) findViewById(R.id.imageView6)).execute(ConductorFoto);
                                        }else {
                                            //ImageView imageView = (ImageView)  findViewById(R.id.imageView4);
                                            // imageView.setImageResource(R.mipmap.icon_conductor_foto250);
                                        }

                                        break;

                                    case "C006":
                                        //FncMostrarToast("");
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
*/


          /*    return true;

      }else  */if (id == android.R.id.home) {

            Intent intentMain = new Intent(ConductorDatoActivity.this, MainActivity.class);
            startActivity(intentMain);
            finish();

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

               Intent intentNavegacion = new Intent(ConductorDatoActivity.this, NavegacionActivity.class);

            Bundle bundleNavegacion = new Bundle();
            bundleNavegacion.putString("PedidoMensaje", "");
            intentNavegacion.putExtras(bundleNavegacion); //Put your id to your next Intent

            startActivity(intentNavegacion);
            finish();



        } else if (id == R.id.nav_ver_cuenta) {
            // Handle the camera action

            Intent intenConductorDato = new Intent(ConductorDatoActivity.this, ConductorDatoActivity.class);

            Bundle bundleConductorDato = new Bundle();
            bundleConductorDato.putString("ConductorId", ConductorId);

            intenConductorDato.putExtras(bundleConductorDato); //Put your id to your next Intent

            startActivity(intenConductorDato);
            finish();


        } else if (id == R.id.nav_mis_preferencias) {

            Intent intentMisPreferencias = new Intent(ConductorDatoActivity.this, MisPreferenciasActivity.class);

            Bundle bundleMisPreferencias = new Bundle();
            bundleMisPreferencias.putString("ConductorId", ConductorId);

            intentMisPreferencias.putExtras(bundleMisPreferencias); //Put your id to your next Intent

            startActivity(intentMisPreferencias);
            finish();

        } else if (id == R.id.nav_historial) {

            Intent intentHistorial = new Intent(ConductorDatoActivity.this, HistorialActivity.class);

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

                            Intent intentMainSesion = new Intent(ConductorDatoActivity.this, MainActivity.class);
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

    public void onClick_BtnConductorDatoActualizar(View v){


        final ProgressDialog prgConductorDato = new ProgressDialog(this);
        prgConductorDato.setIcon(R.mipmap.ic_launcher);
        prgConductorDato.setMessage("Cargando...");
        prgConductorDato.setCancelable(false);
        prgConductorDato.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        prgConductorDato.show();

        Thread nt = new Thread() {
            @Override
            public void run() {

                try {

                    final String resObtenerConductor;

                    resObtenerConductor = MtdObtenerConductor(ConductorId);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            String JsRespuesta = "";

                            String JsIdentificador = "";

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

                            try {

                                JSONObject jsonObject = new JSONObject(resObtenerConductor);
                                JsRespuesta = jsonObject.getString("Respuesta");

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

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            prgConductorDato.cancel();

                            switch(JsRespuesta){
                                case "C005":

                                    FncMostrarToast("Se actualizó su información correctamente");

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

                                    saveUserSettings();


                                    //MOSTRANDO VARIABLES

                                    TextView TxtConductorNombre = (TextView) findViewById(R.id.CmpConductorNombre);
                                    TxtConductorNombre.setText(ConductorNombre);

                                    TextView TxtConductorNumeroDocumento = (TextView) findViewById(R.id.CmpConductorNumeroDocumento);
                                    TxtConductorNumeroDocumento.setText(ConductorNumeroDocumento);

                                    TextView TxtConductorCelular = (TextView) findViewById(R.id.CmpConductorCelular);
                                    TxtConductorCelular.setText(ConductorCelular);


                                    TextView TxtVehiculoUnidad = (TextView) findViewById(R.id.CmpVehiculoUnidad);
                                    TxtVehiculoUnidad.setText(VehiculoUnidad);

                                    TextView TxtVehiculoPlaca = (TextView) findViewById(R.id.CmpVehiculoPlaca);
                                    TxtVehiculoPlaca.setText(VehiculoPlaca);

                                    TextView TxtVehiculoModelo = (TextView) findViewById(R.id.CmpVehiculoModelo);
                                    TxtVehiculoModelo.setText(VehiculoModelo);

                                    TextView TxtVehiculoColor = (TextView) findViewById(R.id.CmpVehiculoColor);
                                    TxtVehiculoColor.setText(VehiculoColor);

                                    if(!"".equals(ConductorFoto)){
                                        new DownloadImageTask((ImageView) findViewById(R.id.imageView6)).execute(ConductorFoto);
                                    }else {
                                        //ImageView imageView = (ImageView)  findViewById(R.id.imageView4);
                                        // imageView.setImageResource(R.mipmap.icon_conductor_foto250);
                                    }

                                    break;

                                case "C006":
                                    //FncMostrarToast("");
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


*/



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

        Toast.makeText(ConductorDatoActivity.this, oMensaje,
                Toast.LENGTH_LONG).show();
    }


/*
ENVIO DE VARIABLES
*/

    public String MtdObtenerConductor(String oConductorId) {

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

            postDataParams.put("ConductorId", oConductorId);

            postDataParams.put("Identificador", Identificador);
            postDataParams.put("ConductorAppVersion", getString(R.string.app_version));
            postDataParams.put("Accion", "ObtenerConductor");

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

        editor.putString("EmpresaId", EmpresaId.trim());
        editor.putString("EmpresaNombre", EmpresaNombre.trim());
        editor.putString("EmpresaFoto", EmpresaFoto.trim());

        //editor.putString("ConductorId", ConductorId.trim());
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

        RegionId = sharedPreferences.getString("RegionId","");

        EmpresaId = sharedPreferences.getString("EmpresaId","");
        EmpresaNombre = sharedPreferences.getString("EmpresaNombre","");
        EmpresaFoto = sharedPreferences.getString("EmpresaFoto","");

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



    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
    
    
}
