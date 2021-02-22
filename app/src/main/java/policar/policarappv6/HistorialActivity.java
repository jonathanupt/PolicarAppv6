package policar.policarappv6;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.text.Html;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class HistorialActivity extends AppCompatActivity
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


    static final int DATE_DIALOG_ID = 999;
    static final int TIME_DIALOG_ID = 998;

    private int year;
    private int month;
    private int day;

    private int hour;
    private int minute;
    private int second;

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
        Log.e("Historial20", "Start");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("Historial20", "Resume");

    }

    //ONPAUSE -> ONRESUME -> ONSTOP
    @Override
    protected void onPause() {
        super.onPause();
        Log.e("Historial20", "Pause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("Historial20", "Stop");
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("Historial20", "Restart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("Historial20", "Destroy");

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e("Historial20", "SaveInstance");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.e("Historial20", "RestoreInstance");
    }
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Log.e("Historial20", "onCreate");
        
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

    /*    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

       // actionBar.setIcon(R.mipmap.ic_launcher);             //Establecer icono
        //actionBar.setTitle("Historial");        //Establecer titulo
         actionBar.setTitle(getString(R.string.title_activity_historial));     //Establecer Subtitulo

        //RECUPERANDO VARIABLES v2
        displayUserSettings();

        //PERMISOS
        context = getApplicationContext();
        activity = this;

        //CAB MENU
      /*  View header = navigationView.getHeaderView(0);
        TextView txtUsuario = (TextView) header.findViewById(R.id.CmpUsuario);
        txtUsuario.setText(ConductorNombre);
*/


        //PREFERENCIAS
        sharedPreferences2 = getSharedPreferences("policar.policarappv6", Context.MODE_PRIVATE);


        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        String dateStr = String.format("%02d/%02d/%04d", day,(month+1),year);

        TextView TxtEventoFecha = (TextView) findViewById(R.id.CmpFecha);
        TxtEventoFecha.setText(dateStr);

        DatePicker DapEventoFecha = (DatePicker) findViewById(R.id.datePicker);
        DapEventoFecha.init(year, month, day, null);

        MtdListarHistorial();
    }

    @Override
    public void onBackPressed() {
       /* DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }*/

        Intent intentMain = new Intent(HistorialActivity.this, MainActivity.class);
        startActivity(intentMain);
        finish();

    }



    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                // set date picker as current date
                return new DatePickerDialog(this, datePickerListener,
                        year, month,day);

            /*case TIME_DIALOG_ID:
                // set date picker as current date
                return new TimePickerDialog(this, timePickerListener,
                        hour, minute,false);*/
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener datePickerListener
            = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;

            TextView TxtFecha = (TextView) findViewById(R.id.CmpFecha);

            String dateStr = String.format("%02d/%02d/%04d", day,(month+1),year);
            TxtFecha.setText(dateStr);

            MtdListarHistorial();

        }
    };


    public void FncEscogerFecha(View view){

        showDialog(DATE_DIALOG_ID);
        //  pickerDialogs pickerDialogs=new pickerDialogs();
        // pickerDialogs.show(getSupportFragmentManager(), "date_picker");

    }



    public void onClick_BtnHistorialFiltrar(View v){


        MtdListarHistorial();

    }



    public void MtdListarHistorial() {


        final ArrayList<HistorialResults> historialResults = new ArrayList<HistorialResults>();

        String preFiltro = ((EditText)findViewById(policar.policarappv6.R.id.CmpFiltro)).getText().toString();
        String preFecha= ((TextView)findViewById(policar.policarappv6.R.id.CmpFecha)).getText().toString();

        CharSequence chaFiltro = Html.fromHtml(preFiltro);
        CharSequence chaFecha = Html.fromHtml(preFecha);

        final String valFiltro = chaFiltro.toString();
        final String valFecha = chaFecha.toString();

        if(valFecha.equals("")){

            FncMostrarMensaje("No haz escogido una fecha",false);

        }else{

            final ProgressDialog prgHistorial = new ProgressDialog(HistorialActivity.this);
            prgHistorial.setIcon(R.mipmap.ic_launcher);
            prgHistorial.setMessage("Cargando...");
            prgHistorial.setCancelable(false);
            prgHistorial.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            prgHistorial.show();



            //OBTENIENDO HISTORIAL
            Thread nt = new Thread() {
                @Override
                public void run() {

                    try {

                        Log.e("MsgHistorial9","cargando...");

                        final String resObtenerHistorial;
                        resObtenerHistorial = enviarPostObtenerHistoriales(ConductorId,valFiltro,valFecha);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Log.e("MsgHistorial9","cargando2...");

                                String JsRespuesta = "";
                                JSONArray JsDatos;

                                try {

                                    Log.e("MsgHistorial9","cargando3...");

                                    JSONObject jsonObject = new JSONObject(resObtenerHistorial);
                                    JsRespuesta = jsonObject.getString("Respuesta");
                                    JsDatos = jsonObject.getJSONArray("Datos");

                                    TextView TxtHistorialTotal = (TextView) findViewById(R.id.CmpHistorialTotal);
                                    //TxtFavoritosTotal.setText(""+JsDatos.length()+" pedidos");
                                    TxtHistorialTotal.setText("Tienes ("+JsDatos.length()+") servicios en tu historial");

                                    for (int i = 0; i < JsDatos.length(); i++) {

                                        JSONObject jsonObj = JsDatos.getJSONObject(i);

                                        String PedidoDireccion = jsonObj.getString("PedidoDireccion");

                                        String PedidoReferencia = jsonObj.getString("PedidoReferencia");

                                        String PedidoFecha = jsonObj.getString("PedidoFecha");
                                        String PedidoHora = jsonObj.getString("PedidoHora");

                                        String VehiculoUnidad = jsonObj.getString("VehiculoUnidad");
                                        String VehiculoPlaca = jsonObj.getString("VehiculoPlaca");
                                        String VehiculoColor = jsonObj.getString("VehiculoColor");

                                        String VehiculoConductor = jsonObj.getString("VehiculoConductor");

                                        String ClienteNombre = jsonObj.getString("ClienteNombre");


                                        HistorialResults sr1 = new HistorialResults();
                                        sr1.setPedidoDireccion(PedidoDireccion);
                                        sr1.setPedidoReferencia(PedidoReferencia);

                                        sr1.setPedidoFecha(PedidoFecha);
                                        sr1.setPedidoHora(PedidoHora);

                                        sr1.setVehiculoUnidad(VehiculoUnidad);
                                        sr1.setVehiculoPlaca(VehiculoPlaca);
                                        sr1.setVehiculoColor(VehiculoColor);

                                        sr1.setVehiculoConductor(VehiculoConductor);

                                        sr1.setClienteNombre(ClienteNombre);

                                        historialResults.add(sr1);

                                    }

                                    final ListView lstHistorial = (ListView) findViewById(R.id.LstRanking);
                                    lstHistorial.setAdapter(new HistorialAdapter(HistorialActivity.this, historialResults));
 /*


                                lstHistorial.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> a, View v, int position, long id) {

                                        Object o = lstHistorial.getItemAtPosition(position);
                                        HistorialResults fullObject = (HistorialResults)o;

                                    }
                                });*/

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.e("MsgHistorial7", e.toString());
                                }

                                prgHistorial.cancel();

                                switch(JsRespuesta){

                                    case "C020":
                                        break;

                                    case "C021":
                                        FncMostrarToast("No se encontraron servicios en tu historial.");
                                        // FncMostrarMensaje(getString(R.string.message_no_proceso),false);
                                        break;

                                    case "C022":
                                        break;

                                    default:
                                        FncMostrarToast(getString(R.string.message_error_interno));
                                        //FncMostrarMensaje(getString(R.string.message_error_interno),false);
                                        break;

                                }

                            }
                        });
                    } catch (final Exception e) {
                        // TODO: handle exception
                        Log.e("MsgHistorial6", e.toString());
                    }

                }

            };
            nt.start();
        }






    }

/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.historial, menu);
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

  /*          Intent intentMain = new Intent(HistorialActivity.this, MainActivity.class);
            startActivity(intentMain);
            finish();
*/
            Intent intentNavegacion = new Intent(HistorialActivity.this, NavegacionActivity.class);

            Bundle bundleNavegacion = new Bundle();
            bundleNavegacion.putString("PedidoMensaje", "");
            intentNavegacion.putExtras(bundleNavegacion); //Put your id to your next Intent

            startActivity(intentNavegacion);
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

            Intent intentNavegacion = new Intent(HistorialActivity.this, NavegacionActivity.class);

            Bundle bundleNavegacion = new Bundle();
            bundleNavegacion.putString("PedidoMensaje", "");
            intentNavegacion.putExtras(bundleNavegacion); //Put your id to your next Intent

            startActivity(intentNavegacion);
            finish();

        } else if (id == R.id.nav_ver_cuenta) {
            // Handle the camera action

            Intent intenConductorDato = new Intent(HistorialActivity.this, ConductorDatoActivity.class);

            Bundle bundleConductorDato = new Bundle();
            bundleConductorDato.putString("ConductorId", ConductorId);

            intenConductorDato.putExtras(bundleConductorDato); //Put your id to your next Intent

            startActivity(intenConductorDato);
            finish();


        } else if (id == R.id.nav_mis_preferencias) {

            Intent intentMisPreferencias = new Intent(HistorialActivity.this, MisPreferenciasActivity.class);

            Bundle bundleMisPreferencias = new Bundle();
            bundleMisPreferencias.putString("ConductorId", ConductorId);

            intentMisPreferencias.putExtras(bundleMisPreferencias); //Put your id to your next Intent

            startActivity(intentMisPreferencias);
            finish();

        } else if (id == R.id.nav_historial) {

            Intent intentHistorial = new Intent(HistorialActivity.this, HistorialActivity.class);

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

                            Intent intentMainSesion = new Intent(HistorialActivity.this, MainActivity.class);
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
   AUXILIARES
    */
    private void FncMostrarAcercaDe() {

        AlertDialog.Builder MsgAcercaDe = new AlertDialog.Builder(this);
        MsgAcercaDe.setTitle("ACERCA DE");
        MsgAcercaDe.setMessage(getString(R.string.app_version));
        MsgAcercaDe.setPositiveButton("Aceptar",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                    }
                });

        // Remember, create doesn't show the dialog
        AlertDialog msgAcercaDe = MsgAcercaDe.create();
        msgAcercaDe.show();

    }

    private void FncMostrarMensaje(String oMensaje, final Boolean oCerrar) {

        AlertDialog.Builder MsgGeneral = new AlertDialog.Builder(this);
        MsgGeneral.setTitle(getString(R.string.alert_title));
        MsgGeneral.setMessage(oMensaje);
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

        Toast.makeText(HistorialActivity.this, oMensaje,
                Toast.LENGTH_LONG).show();

    }


    /*
   * ENVIO DE VARIABLES
     */


    public String enviarPostObtenerHistoriales(String oConductorId,String oFiltro,String oFecha) {

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
            postDataParams.put("Filtro", oFiltro);
            postDataParams.put("Fecha", oFecha);

            postDataParams.put("Identificador", Identificador);
            postDataParams.put("Accion", "ObtenerHistorial");

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

            Log.e("Historial1", response);

        } catch (Exception e) {

            Log.e("Historial2", e.toString());
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
