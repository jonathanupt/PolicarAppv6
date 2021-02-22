package policar.policarappv6;

import android.content.Intent;
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
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
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


public class CrearCuentaActivity extends AppCompatActivity
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_cuenta);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

  /*      DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
*/

        //CREANDO BARRA
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        //actionBar.setDisplayShowHomeEnabled(true);
        //actionBar.setIcon(R.mipmap.ic_launcher);             //Establecer icono
        //actionBar.setTitle("Pre-Registro");        //Establecer titulo
        actionBar.setTitle(getString(R.string.title_activity_crear_cuenta));     //Establecer Subtitulo


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
        getMenuInflater().inflate(R.menu.crear_cuenta, menu);
        return true;
    }
*/
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

            Intent intentMain = new Intent(CrearCuentaActivity.this, MainActivity.class);
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

            Intent intenMain = new Intent(CrearCuentaActivity.this, MainActivity.class);
            startActivity(intenMain);
            finish();


        } else if(id == R.id.nav_crear_cuenta) {

            Intent intenCrearCuenta = new Intent(CrearCuentaActivity.this, CrearCuentaActivity.class);
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



    public void onClick_BtnCrearCuentaGuardar(View v) {


        final ProgressDialog prgCrearCuenta = new ProgressDialog(this);
        prgCrearCuenta.setIcon(R.mipmap.ic_launcher);
        prgCrearCuenta.setMessage("Cargando...");
        prgCrearCuenta.setCancelable(false);
        prgCrearCuenta.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        prgCrearCuenta.show();

        Thread nt = new Thread() {
            @Override
            public void run() {

                try {


                    final EditText valConductorNumeroDocumento = (EditText) findViewById(R.id.CmpConductorNumeroDocumento);
                    final EditText valConductorNombre = (EditText) findViewById(R.id.CmpConductorNombre);
                    final EditText valConductorCelular = (EditText) findViewById(R.id.CmpConductorCelular);
                    final EditText valConductorEmail = (EditText) findViewById(R.id.CmpConductorEmail);
                    final EditText valConductorObservacion = (EditText) findViewById(R.id.CmpConductorObservacion);

                    if ("".equals(valConductorNumeroDocumento.getText().toString())) {

                        FncMostrarMensaje("No ha ingresado su DNI", false);

                    } else if ("".equals(valConductorNombre.getText().toString())) {

                        FncMostrarMensaje("No ha ingresado su nombre", false);

                    } else if ("".equals(valConductorCelular.getText().toString())) {

                        FncMostrarMensaje("No ha ingresado su número de celular", false);


                    } else {

                        final String resRegistrarConductor;

                        resRegistrarConductor = MtdRegistrarConductor(
                                valConductorNumeroDocumento.getText().toString(),
                                valConductorNombre.getText().toString(),
                                valConductorCelular.getText().toString(),
                                valConductorEmail.getText().toString(),
                                valConductorObservacion.getText().toString()
                        );

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                String JsRespuesta = "";

                                try {

                                    JSONObject jsonObject = new JSONObject(resRegistrarConductor);
                                    JsRespuesta = jsonObject.getString("Respuesta");

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                prgCrearCuenta.cancel();

                                switch (JsRespuesta) {
                                    case "C035":

                                        AlertDialog.Builder MsgCrearCuenta35 = new AlertDialog.Builder(CrearCuentaActivity.this);
                                        MsgCrearCuenta35.setTitle(getString(R.string.app_titulo));
                                        MsgCrearCuenta35.setMessage("Se guardo su pre-registro correctamente.");
                                        MsgCrearCuenta35.setCancelable(false);
                                        MsgCrearCuenta35.setPositiveButton("Aceptar",
                                                new DialogInterface.OnClickListener() {

                                                    public void onClick(DialogInterface dialog, int which) {
                                                        // Do nothing but close the dialog
                                                        Intent intentMain = new Intent(CrearCuentaActivity.this, MainActivity.class);
                                                        startActivity(intentMain);
                                                        finish();

                                                    }
                                                });

                                        // Remember, create doesn't show the dialog
                                        AlertDialog msgCrearCuenta35 = MsgCrearCuenta35.create();
                                        msgCrearCuenta35.show();

                                        break;

                                    case "C036":

                                        AlertDialog.Builder MsgCrearCuenta36 = new AlertDialog.Builder(CrearCuentaActivity.this);
                                        MsgCrearCuenta36.setTitle(getString(R.string.app_titulo));
                                        MsgCrearCuenta36.setMessage("No se ha podido guardar su pre-registro, intente nuevamente.");
                                        MsgCrearCuenta36.setCancelable(false);
                                        MsgCrearCuenta36.setPositiveButton("Aceptar",
                                                new DialogInterface.OnClickListener() {

                                                    public void onClick(DialogInterface dialog, int which) {
                                                        // Do nothing but close the dialog
                                                        Intent intentMain = new Intent(CrearCuentaActivity.this, MainActivity.class);
                                                        startActivity(intentMain);
                                                        finish();

                                                    }
                                                });

                                        // Remember, create doesn't show the dialog
                                        AlertDialog msgCrearCuenta36 = MsgCrearCuenta36.create();
                                        msgCrearCuenta36.show();

                                        break;

                                    default:
                                        //FncMostrarMensaje(getString(R.string.message_error_interno),true);
                                        FncMostrarToast(getString(R.string.message_error_interno));
                                        break;
                                }

                            }
                        });


                    }


                } catch (Exception e) {
                    // TODO: handle exception
                }

            }
        };


        nt.start();

    }

    public void onClick_BtnCrearCuentaCancelar(View v) {

        Intent intentMain = new Intent(CrearCuentaActivity.this, MainActivity.class);
        startActivity(intentMain);
        finish();

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

        Toast.makeText(CrearCuentaActivity.this, oMensaje,
                Toast.LENGTH_LONG).show();
    }



/*
ENVIO DE VARIABLES
*/

    /*
     resRegistrarConductor = MtdRegistrarConductor(
                                valConductorNumeroDocumento.getText().toString(),
                                valConductorNombre.getText().toString(),
                                valConductorCelular.getText().toString(),
                                valConductorEmail.getText().toString()

                        );

     */
    public String MtdRegistrarConductor(String oConductorNumeroDocumento, String oConductorNombre, String oConductorCelular, String oConductorEmail, String oConductorObservacion) {

        URL url;
        String response = "";

        try {

            url = new URL(getString(R.string.app_url) + "/webservice/JnConductor.php");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            HashMap<String, String> postDataParams = new HashMap<>();

            postDataParams.put("ConductorNumeroDocumento", oConductorNumeroDocumento);
            postDataParams.put("ConductorNombre", oConductorNombre);
            postDataParams.put("ConductorCelular", oConductorCelular);
            postDataParams.put("ConductorEmail", oConductorEmail);
            postDataParams.put("ConductorObservacion", oConductorObservacion);

            postDataParams.put("Identificador", Identificador);
            postDataParams.put("ConductorAppVersion", getString(R.string.app_version));
            postDataParams.put("Accion", "RegistrarConductor");

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

            Log.e("CrearCuenta1", response);

        } catch (Exception e) {

            Log.e("CrearCuenta2", e.toString());
            e.printStackTrace();
        }

        return response;


    }


    private boolean saveUserSettings() {

        SharedPreferences sharedPreferences = getSharedPreferences("inkataxi.inkataxiappv6", Context.MODE_PRIVATE);
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

        SharedPreferences sharedPreferences = getSharedPreferences("inkataxi.inkataxiappv6", Context.MODE_PRIVATE);

        Identificador = sharedPreferences.getString("Identificador", "");

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


}
