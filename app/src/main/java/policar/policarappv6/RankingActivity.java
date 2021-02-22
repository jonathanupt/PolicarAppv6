package policar.policarappv6;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
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
import java.util.HashMap;
import java.util.Map;

public class RankingActivity extends AppCompatActivity {

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
    private String ConductorCanal = "";
    //private String ConductorFoto = "";
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
    * REGION
     */

    private String RegionId = "";
    private String RegionNombre = "";

    private String SectorId = "";
    private String SectorNombre = "";


    /*
    * PREFERENCIAS
    * */
    SharedPreferences sharedPreferences2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
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

        Log.e("MsgCanalConductor20", "onCreate");


        //CREANDO BARRA
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        //actionBar.setDisplayShowHomeEnabled(true);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // actionBar.setIcon(R.mipmap.ic_launcher);             //Establecer icono
        //actionBar.setTitle("Historial");        //Establecer titulo
        actionBar.setTitle(getString(R.string.title_activity_ranking));     //Establecer Subtitulo

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


        //RECUPERANDO VARIABLES
        Intent intentExtras = getIntent();
        Bundle extrasBundle = intentExtras.getExtras();

        //PREFERENCIAS
        sharedPreferences2 = getSharedPreferences("policar.policarappv6", Context.MODE_PRIVATE);
        RegionId = sharedPreferences2.getString("RegionId", "");
        SectorId = sharedPreferences2.getString("SectorId", "");

        MtdListarRanking();

    }


    @Override
    public void onBackPressed() {
       /* DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }*/

        Intent intentMain = new Intent(RankingActivity.this, MainActivity.class);
        startActivity(intentMain);
        finish();

    }


    public void MtdListarRanking() {

        final ArrayList<RankingResults> rankingResults = new ArrayList<RankingResults>();

        final ProgressDialog PrgRanking = new ProgressDialog(RankingActivity.this);
        PrgRanking.setIcon(R.mipmap.ic_launcher);
        PrgRanking.setMessage("Cargando...");
        PrgRanking.setCancelable(false);
        PrgRanking.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        PrgRanking.show();


        //OBTENIENDO HISTORIAL
        Thread nt = new Thread() {
            @Override
            public void run() {

                try {

                    final String resObtenerRanking;
                    resObtenerRanking = MtdObtenerRanking(ConductorId,RegionId);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            String JsRespuesta = "";
                            JSONArray JsDatos;

                            try {
                                JSONObject jsonObject = new JSONObject(resObtenerRanking);
                                JsRespuesta = jsonObject.getString("Respuesta");
                                JsDatos = jsonObject.getJSONArray("Datos");

                                // TextView TxtHistorialTotal = (TextView) findViewById(R.id.CmpHistorialTotal);
                                // TxtHistorialTotal.setText("Tienes ("+JsDatos.length()+") pedidos en tu historial ");

                                for (int i = 0; i < JsDatos.length(); i++) {

                                    JSONObject jsonObj = JsDatos.getJSONObject(i);

                                    String JsConductorId = jsonObj.getString("ConductorId");
                                    String JsConductorNombre = jsonObj.getString("ConductorNombre");
                                    String JsVehiculoUnidad = jsonObj.getString("VehiculoUnidad");
                                    String JsRankingTotal = jsonObj.getString("RankingTotal");

                                    RankingResults sr1 = new RankingResults();

                                    sr1.setConductorId(JsConductorId);
                                    sr1.setConductorNombre(JsConductorNombre);
                                    sr1.setVehiculoUnidad(JsVehiculoUnidad);
                                    sr1.setRankingTotal(JsRankingTotal);

                                    rankingResults.add(sr1);

                                }

                                final ListView lstRanking = (ListView) findViewById(R.id.LstRanking);
                                lstRanking.setAdapter(new RankingAdapter(RankingActivity.this, rankingResults));


                            } catch (JSONException e) {
                                e.printStackTrace();
//                              FncMostrarToast(e.toString());
                            }

                            PrgRanking.cancel();

                            switch(JsRespuesta){
                                case "C110":
                                    break;

                                case "C111":
                                    FncMostrarToast("No se encontraron registros");
                                    break;

                                case "C112":
                                    break;
                            }

                        }
                    });
                } catch (final Exception e) {
                    // TODO: handle exception
                    Log.e("Ranking6", e.toString());
                }

            }

        };
        nt.start();


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

            Intent intentMain = new Intent(RankingActivity.this, MainActivity.class);
            startActivity(intentMain);
            finish();

            return true;
        }

        return super.onOptionsItemSelected(item);
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

        Toast.makeText(RankingActivity.this, oMensaje,
                Toast.LENGTH_LONG).show();

    }



    /*
   * ENVIO DE VARIABLES
     */

    public String MtdObtenerRanking(String oConductorId,String oRegionId) {

        URL url;
        String response = "";

        try {

            url = new URL(getString(R.string.app_url)+"/webservice/"+getString(R.string.webservice_jnrankingconductor));

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            HashMap<String, String> postDataParams=new HashMap<>();
            postDataParams.put("ConductorId", oConductorId);
            postDataParams.put("RegionId", oRegionId);

            postDataParams.put("Identificador", Identificador);
            postDataParams.put("Accion", "ObtenerRanking");

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

            Log.e("MsgRanking1", response);

        } catch (Exception e) {

            Log.e("MsgRanking2", e.toString());
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

        editor.putString("ConductorCanal", ConductorCanal.trim());
        editor.putString("ConductorFoto", ConductorFoto.trim());
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

        ConductorCanal = sharedPreferences.getString("ConductorCanal", "");
        ConductorFoto = sharedPreferences.getString("ConductorFoto", "");
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
