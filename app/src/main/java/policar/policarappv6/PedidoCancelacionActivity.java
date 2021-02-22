package policar.policarappv6;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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


public class PedidoCancelacionActivity extends AppCompatActivity
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


/*
* PEDIDOS
 */


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
    private String PedidoDetalle = "";
    private String PedidoLugarCompra = "";
    private String PedidoEstado = "";

    private String PedidoTipo = "";
    private String PedidoTipoUnidad = "";
    private String PedidoTipoAccion = "";

    private String PedidoDistancia = "";
    private String PedidoTiempo = "";

    private String PedidoCoordenadaX = "0.00";
    private String PedidoCoordenadaY = "0.00";


    private String PedidoCancelacionMotivo = "";
    private Integer PedidoCancelacionTotal = 2;

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


    //ONSTART -> ON CREATE -> ONRESTART
    @Override
    protected void onStart() {
        super.onStart();
        Log.e("PedidoCancelacion20", "Start");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("PedidoCancelacion20", "Resume");

    }

    //ONPAUSE -> ONRESUME -> ONSTOP
    @Override
    protected void onPause() {
        super.onPause();
        Log.e("PedidoCancelacion20", "Pause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("PedidoCancelacion20", "Stop");
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("PedidoCancelacion20", "Restart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("PedidoCancelacion20", "Destroy");

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e("PedidoCancelacion20", "SaveInstance");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.e("PedidoCancelacion20", "RestoreInstance");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido_cancelacion);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Log.e("PedidoCancelacion20", "RestoreInstance");
        
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
        //actionBar.setIcon(R.mipmap.ic_launcher);             //Establecer icono
        //actionBar.setTitle("Cancelar");        //Establecer titulo
        actionBar.setTitle(getString(R.string.title_activity_pedido_cancelacion));     //Establecer Subtitulo

        //RECUPERANDO VARIABLES v2
        displayUserSettings();

        //RECUPERANDO VARIABLES
        Intent intentExtras = getIntent();
        Bundle extrasBundle = intentExtras.getExtras();

        //PedidoId = intentExtras.getStringExtra("PedidoId");
        PedidoId = intentExtras.getStringExtra("PedidoId");

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

        VehiculoCoordenadaX = intentExtras.getStringExtra("VehiculoCoordenadaX");
        VehiculoCoordenadaY = intentExtras.getStringExtra("VehiculoCoordenadaY");

        PedidoCancelacionTotal = 0;

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroupCancelar);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                RadioButton rb = (RadioButton) findViewById(checkedId);
                //Toast.makeText(getApplicationContext(), rb.getText(), Toast.LENGTH_SHORT).show();
                PedidoCancelacionMotivo = rb.getText().toString();

                if (PedidoCancelacionMotivo.equals("No pude ubicar al cliente")) {
                    PedidoCancelacionTotal = 1;
                }

            }
        });



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

 /*   @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.pedido_cancelacion, menu);
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

                            Intent intentMainSesion = new Intent(PedidoCancelacionActivity.this, MainActivity.class);
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

        Toast.makeText(PedidoCancelacionActivity.this, oMensaje,
                Toast.LENGTH_LONG).show();
    }




    public void onClick_BtnPedidoCancelacionGuardar(View v){


        if (!PedidoCancelacionMotivo.equals("")){

            final ProgressDialog prgPedidoCancelacion = new ProgressDialog(PedidoCancelacionActivity.this);
            prgPedidoCancelacion.setIcon(R.mipmap.ic_launcher);
            prgPedidoCancelacion.setMessage("Cargando...");
            prgPedidoCancelacion.setCancelable(false);
            prgPedidoCancelacion.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            prgPedidoCancelacion.show();

            Thread nt = new Thread() {
                @Override
                public void run() {

                    try {

                        final String resPedidoCancelarConductor;
                        resPedidoCancelarConductor = MtdPedidoCancelarConductor(PedidoId,PedidoCancelacionMotivo,PedidoCancelacionTotal,ConductorId);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                String JsRespuesta = "";

                                try {

                                    JSONObject jsonObject = new JSONObject(resPedidoCancelarConductor);
                                    JsRespuesta = jsonObject.getString("Respuesta");

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                prgPedidoCancelacion.cancel();

                                switch(JsRespuesta){

                                    case "P026":

                                        SharedPreferences sharedPreferences = getSharedPreferences("policar.policarappv6", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();

                                        editor.putBoolean("ConductorTienePedido", false);
                                        editor.apply();


                                       /* android.app.AlertDialog.Builder MsgGeneral = new android.app.AlertDialog.Builder(PedidoCancelacionActivity.this);
                                        MsgGeneral.setTitle(getString(R.string.alert_title));
                                        MsgGeneral.setMessage("El pedido fue cancelado correctamente");
                                        MsgGeneral.setCancelable(false);
                                        MsgGeneral.setPositiveButton("Aceptar",
                                                new DialogInterface.OnClickListener() {

                                                    public void onClick(DialogInterface dialog, int which) {
                                                        // Do nothing but close the dialog

                                                        Intent intent = new Intent(PedidoCancelacionActivity.this, NavegacionActivity.class);
                                                        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                });

                                        // Remember, create doesn't show the dialog
                                        android.app.AlertDialog msgGeneral = MsgGeneral.create();
                                        msgGeneral.show();
*/
                                       /*Intent intentNavegacion26 = new Intent(PedidoCancelacionActivity.this, NavegacionActivity.class);
                                        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intentNavegacion26);
                                        finish();
*/
                                        Intent intentNavegacion26 = new Intent(PedidoCancelacionActivity.this, NavegacionActivity.class);

                                        Bundle bundleNavegacion26 = new Bundle();
                                        bundleNavegacion26.putString("PedidoMensaje", "");
                                        intentNavegacion26.putExtras(bundleNavegacion26); //Put your id to your next Intent

                                        startActivity(intentNavegacion26);
                                        finish();


                                        break;

                                    case "P027":
                                        //FncMostrarMensaje("No se ha podido cancelar el pedido, ha ocurrido un error",false);

                                        android.app.AlertDialog.Builder MsgPedidoCancelacion27 = new android.app.AlertDialog.Builder(PedidoCancelacionActivity.this);
                                        MsgPedidoCancelacion27.setTitle(getString(R.string.alert_title));
                                        MsgPedidoCancelacion27.setMessage("No se ha podido cancelar el pedido, comuniquese con la central. Codigo de error P027");
                                        MsgPedidoCancelacion27.setPositiveButton("Aceptar",
                                                new DialogInterface.OnClickListener() {

                                                    public void onClick(DialogInterface dialog, int which) {
                                                        // Do nothing but close the dialog
                                                                        /*Intent intentNavegacion17 = new Intent(PedidoCancelacionActivity.this, NavegacionActivity.class);
                                                                        startActivity(intentNavegacion17);
                                                                        finish();*/
                                                    }
                                                });

                                        // Remember, create doesn't show the dialog
                                        android.app.AlertDialog msgPedidoCancelacion27 = MsgPedidoCancelacion27.create();
                                        msgPedidoCancelacion27.show();

                                        break;

                                    case "P028":
                                        // FncMostrarMensaje("No se ha podido cancelar el pedido, verifique el estado del pedido",false);

                                        android.app.AlertDialog.Builder MsgPedidoCancelacion28 = new android.app.AlertDialog.Builder(PedidoCancelacionActivity.this);
                                        MsgPedidoCancelacion28.setTitle(getString(R.string.alert_title));
                                        MsgPedidoCancelacion28.setMessage("No se ha podido cancelar el estado del pedido a cambiado, comuniquese con la central. Codigo de error P028");
                                        MsgPedidoCancelacion28.setPositiveButton("Aceptar",
                                                new DialogInterface.OnClickListener() {

                                                    public void onClick(DialogInterface dialog, int which) {
                                                        // Do nothing but close the dialog
                                                                        /*Intent intentNavegacion17 = new Intent(PedidoCancelacionActivity.this, NavegacionActivity.class);
                                                                        startActivity(intentNavegacion17);
                                                                        finish();*/

                                                        Intent intentPedidoActual28 = new Intent(PedidoCancelacionActivity.this, PedidoActualActivity.class);
                                                        Bundle bundlePedidoActual28 = new Bundle();

                                                        bundlePedidoActual28.putString("PedidoId", PedidoId);

                                                        bundlePedidoActual28.putString("ClienteNombre", ClienteNombre);
                                                        bundlePedidoActual28.putString("ClienteCelular", ClienteCelular);
                                                        bundlePedidoActual28.putString("ClienteTelefono", ClienteTelefono);

                                                        bundlePedidoActual28.putString("PedidoDireccion", PedidoDireccion);
                                                        bundlePedidoActual28.putString("PedidoReferencia", PedidoReferencia);
                                                        bundlePedidoActual28.putString("PedidoDetalle", PedidoDetalle);
                                                        bundlePedidoActual28.putString("PedidoLugarCompra", PedidoLugarCompra);
                                                        bundlePedidoActual28.putString("PedidoEstado", PedidoEstado);

                                                        bundlePedidoActual28.putString("PedidoTipo", PedidoTipo);
                                                        bundlePedidoActual28.putString("PedidoTipoUnidad", PedidoTipoUnidad);
                                                        bundlePedidoActual28.putString("PedidoTipoAccion", PedidoTipoAccion);

                                                        bundlePedidoActual28.putString("PedidoTiempo", PedidoTiempo);
                                                        bundlePedidoActual28.putString("PedidoDistancia", PedidoDistancia);

                                                        bundlePedidoActual28.putString("PedidoCoordenadaX", PedidoCoordenadaX);
                                                        bundlePedidoActual28.putString("PedidoCoordenadaY", PedidoCoordenadaY);

                                                        bundlePedidoActual28.putString("VehiculoCoordenadaX", VehiculoCoordenadaX);
                                                        bundlePedidoActual28.putString("VehiculoCoordenadaY", VehiculoCoordenadaY);

                                                        intentPedidoActual28.putExtras(bundlePedidoActual28);//Put your id to your next Intent
                                                        startActivity(intentPedidoActual28);
                                                        finish();

                                                    }
                                                });

                                        // Remember, create doesn't show the dialog
                                        android.app.AlertDialog msgPedidoCancelacion28 = MsgPedidoCancelacion28.create();
                                        msgPedidoCancelacion28.show();


                                        break;

                                    default://NO ES CRITICO
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
            FncMostrarMensaje("Escoja un motivo de cancelacion de pedido",false);
        }




    }

    /**
     *  ENVIO DE VARIABLES
     * */


    public String MtdPedidoCancelarConductor(String oPedidoId,String oPedidoCancelacionMotivo, Integer oPedidoCancelacionTotal,String oConductorId) {

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

            postDataParams.put("PedidoCancelacionMotivoConductor", oPedidoCancelacionMotivo);
            postDataParams.put("PedidoCancelacionTotalConductor", oPedidoCancelacionTotal.toString());

            postDataParams.put("Identificador", Identificador);
            postDataParams.put("Accion", "CancelarPedidoConductor");

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

            /*
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line=br.readLine()) != null) {
                    response+=line;
                }
            }
            else {
                response="";

            }*/

            Log.e("PedidoCancelacion1", response);

        } catch (Exception e) {

            Log.e("PedidoCancelacion2", e.toString());
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
