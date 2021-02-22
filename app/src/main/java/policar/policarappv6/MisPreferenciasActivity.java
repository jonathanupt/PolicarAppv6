package policar.policarappv6;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;


public class MisPreferenciasActivity extends AppCompatActivity
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
    * OPCIONES
     */
    private Switch SwiMonitoreoEncendido;
    private Switch SwiMonitoreoSonido;

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
        Log.e("MisPreferencias20", "Start");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("MisPreferencias20", "Resume");

    }

    //ONPAUSE -> ONRESUME -> ONSTOP
    @Override
    protected void onPause() {
        super.onPause();
        Log.e("MisPreferencias20", "Pause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("MisPreferencias20", "Stop");
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("MisPreferencias20", "Restart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("MisPreferencias20", "Destroy");

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e("MisPreferencias20", "SaveInstance");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.e("MisPreferencias20", "RestoreInstance");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_preferencias);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Log.e("MisPreferencias20", "onCreate");
        
    /*    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
*/
       /* DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //actionBar.setDisplayShowHomeEnabled(true);
        //actionBar.setIcon(R.mipmap.ic_launcher);             //Establecer icono
        //actionBar.setTitle("Mis Preferencias");        //Establecer titulo
        actionBar.setTitle(getString(R.string.title_activity_mis_preferencias));     //Establecer Subtitulo


//PERMISOS
        context = getApplicationContext();
        activity = this;

        //RECUPERAR VARIABLES
        displayUserSettings();


        sharedPreferences2 = getSharedPreferences("policar.policarappv6", Context.MODE_PRIVATE);

        //CAB MENU

    /*    View header = navigationView.getHeaderView(0);
        TextView txtUsuario = (TextView) header.findViewById(R.id.CmpUsuario);
        txtUsuario.setText(ConductorNombre);
      */
        
        SwiMonitoreoEncendido = (Switch) findViewById(R.id.SwiMonitoreoEncendido);

// set the switch to ON
        //SwiMonitoreoEncendido.setChecked(true);
// attach a listener to check for changes in state
        SwiMonitoreoEncendido.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                SharedPreferences sharedPreferences = getSharedPreferences("policar.policarappv6", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                if (isChecked) {
                    Log.e("MisPreferencias", "verda");
                    editor.putBoolean("MonitoreoEncendido", true);


                } else {
                    Log.e("MisPreferencias","falso");
                    editor.putBoolean("MonitoreoEncendido", false);
                    // Toast.makeText(getApplicationContext(),
                    //         “The switch is OFF”,Toast.LENGTH_SHORT).show();
                }

                editor.apply();

            }
        });


        SwiMonitoreoSonido = (Switch) findViewById(R.id.SwiMonitoreoSonido);

        SwiMonitoreoSonido.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                SharedPreferences sharedPreferences = getSharedPreferences("policar.policarappv6", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                if (isChecked) {
                    Log.e("MisPreferencias","2verda");
                    editor.putBoolean("MonitoreoSonido", true);


                } else {
                    Log.e("MisPreferencias","2falso");
                    editor.putBoolean("MonitoreoSonido", false);
                    // Toast.makeText(getApplicationContext(),
                    //         “The switch is OFF”,Toast.LENGTH_SHORT).show();
                }

                editor.apply();

            }
        });




        RadioGroup tipoRadioGroup = (RadioGroup) findViewById(R.id.TipoRadioGroup);

        tipoRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                RadioButton rb = (RadioButton) findViewById(checkedId);
                //Toast.makeText(getApplicationContext(), rb.getText(), Toast.LENGTH_SHORT).show();

                SharedPreferences.Editor editor = sharedPreferences2.edit();

                switch(rb.getText().toString()){
                    case "TMP3":
                        editor.putString("TipoRadio","TMP3");
                        editor.apply();
                        break;

                    case "T3GP":
                        editor.putString("TipoRadio","T3GP");
                        editor.apply();
                        break;

                    default:
                        editor.putString("TipoRadio","T3GP");
                        editor.apply();
                        break;

                }

            }
        });




        SharedPreferences sharedPreferences = getSharedPreferences("policar.policarappv6", Context.MODE_PRIVATE);

        boolean MonitoreoEncendido = sharedPreferences.getBoolean("MonitoreoEncendido", true);
        boolean MonitoreoSonido = sharedPreferences.getBoolean("MonitoreoSonido",true);
        String TipoRadio = sharedPreferences.getString("TipoRadio","T3GP");

        if(MonitoreoEncendido){
            SwiMonitoreoEncendido.setChecked(true);
        }else{
            SwiMonitoreoEncendido.setChecked(false);
        }

        if(MonitoreoSonido){
            SwiMonitoreoSonido.setChecked(true);
        }else{
            SwiMonitoreoSonido.setChecked(false);
        }

        switch(TipoRadio){
            case "T3GP":
                tipoRadioGroup.check(R.id.TipoRadioButton1);
                break;

            case "TMP3":
                tipoRadioGroup.check(R.id.TipoRadioButton2);
                break;
            default:
                tipoRadioGroup.check(R.id.TipoRadioButton1);
                break;

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
        getMenuInflater().inflate(R.menu.mis_preferencias, menu);
        return true;
    }
*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        /*//noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }*/
        if (id == android.R.id.home) {

            Intent intentMain = new Intent(MisPreferenciasActivity.this, MainActivity.class);
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




            Intent intentNavegacion = new Intent(MisPreferenciasActivity.this, NavegacionActivity.class);

            Bundle bundleNavegacion = new Bundle();
            bundleNavegacion.putString("PedidoMensaje", "");
            intentNavegacion.putExtras(bundleNavegacion); //Put your id to your next Intent

            startActivity(intentNavegacion);
            finish();

        } else if (id == R.id.nav_ver_cuenta) {
            // Handle the camera action

            Intent intenConductorDato = new Intent(MisPreferenciasActivity.this, ConductorDatoActivity.class);

            Bundle bundleConductorDato = new Bundle();
            bundleConductorDato.putString("ConductorId", ConductorId);

            intenConductorDato.putExtras(bundleConductorDato); //Put your id to your next Intent

            startActivity(intenConductorDato);
            finish();


        } else if (id == R.id.nav_mis_preferencias) {

            Intent intentMisPreferencias = new Intent(MisPreferenciasActivity.this, MisPreferenciasActivity.class);

            Bundle bundleMisPreferencias = new Bundle();
            bundleMisPreferencias.putString("ConductorId", ConductorId);

            intentMisPreferencias.putExtras(bundleMisPreferencias); //Put your id to your next Intent

            startActivity(intentMisPreferencias);
            finish();

        } else if (id == R.id.nav_historial) {

            Intent intentHistorial = new Intent(MisPreferenciasActivity.this, HistorialActivity.class);

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

                            Intent intentMainSesion = new Intent(MisPreferenciasActivity.this, MainActivity.class);
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

        Toast.makeText(MisPreferenciasActivity.this, oMensaje,
                Toast.LENGTH_LONG).show();
    }









    public void onClick_BtnMisPreferenciasAjustarGPS(View v) {

        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));

    }


    public void onClick_BtnMisPreferenciasLimpiarTemporales(View v) {

        //startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));

        AlertDialog.Builder MsgMisPreferencias = new AlertDialog.Builder(this);
        MsgMisPreferencias.setTitle(getString(R.string.app_titulo));
        MsgMisPreferencias.setMessage("¿Realmente deseas borrar los archivos temporales?");
        MsgMisPreferencias.setCancelable(false);
        MsgMisPreferencias.setPositiveButton("Aceptar",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        //audioFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/myaudio"+ts+"_"+ConductorId+"_"+ConductorCanal+"_"+ConductorIdDestino+".3gp";
                        File dir = new File(Environment.getExternalStorageDirectory()+"/radio");
//comprueba si es directorio.
                        if (dir.isDirectory())
                        {
                            //obtiene un listado de los archivos contenidos en el directorio.
                            String[] hijos = dir.list();
                            //Elimina los archivos contenidos.
                            for (int i = 0; i < hijos.length; i++)
                            {
                                new File(dir, hijos[i]).delete();
                            }
                        }

                        // Do nothing but close the dialog

                    }
                });

        // Remember, create doesn't show the dialog
        AlertDialog msgMisPreferencias = MsgMisPreferencias.create();
        msgMisPreferencias.show();



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
