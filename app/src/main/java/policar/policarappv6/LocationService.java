package policar.policarappv6;

/**
 * Created by Jonathan on 15/12/2016.
 */

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

//import com.google.android.gms.common.GooglePlayServicesUtil;

public class LocationService extends Service implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private static final String TAG = "LocationService";

    // use the websmithing defaultUploadWebsite for testing and then check your
    // location with your browser here: https://www.websmithing.com/gpstracker/displaymap.php
    private String defaultUploadWebsite;

    private boolean currentlyProcessingLocation = false;
    private LocationRequest locationRequest;
    private GoogleApiClient googleApiClient;





    // Binder given to clients
    // Registered callbacks
    private static final String LOGSERVICE = "#######";
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    Context c = this;
    NotificationClass nc = new NotificationClass();
    public static int PacketSize;
    public static int LocationInterval;
    public static int LocationFastestInterval;
    /// private static final int drawableIcon = R.drawable.cast_ic_notification_small_icon;
    public static PendingIntent contentIntent;
    public static String NotificationTxt;
    public static String NotificationTitle;
    public static int drawable_small;


    @Override
    public void onCreate() {
        super.onCreate();
        //defaultUploadWebsite = getString(R.string.default_upload_website);
        defaultUploadWebsite = getString(R.string.app_url2)+"/webservice/"+getString(R.string.webservice_jnconductorubicacion);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            Log.e("LocationService","OREO SI");

            //Creating Channel
            nc.createMainNotificationChannel(this);
            //building Notification.
            Notification.Builder notifi = new Notification.Builder(getApplicationContext(), nc.getMainNotificationId());
            // notifi.setSmallIcon(drawable_small);
            notifi.setSmallIcon(R.mipmap.icon_logo75);
            notifi.setContentTitle(NotificationTitle);
            notifi.setContentText(NotificationTxt);
            notifi.setContentIntent(contentIntent);
            //getting notification object from notification builder.
            Notification n = notifi.build();

            int mNotificationId = 001;

            NotificationManager mNotificationManager =
                    (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(mNotificationId, n);

            //  startting foreground
            startForeground(1, n);


        } else {
            Log.e("LocationService","OREO NO");
            //for devices less than API Level 26
            Notification notification = new Notification.Builder(getApplicationContext())
                    .setContentTitle(NotificationTitle)
                    .setContentText(NotificationTxt)
                    // .setSmallIcon(drawable_small)
                    .setSmallIcon(R.mipmap.icon_logo75)
                    .setContentIntent(contentIntent)
                    .setOngoing(true).build();
            startForeground(1, notification);

        }




    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // if we are currently trying to get a location and the alarm manager has called this again,
        // no need to start processing a new location.
        if (!currentlyProcessingLocation) {
            currentlyProcessingLocation = true;
            startTracking();
        }

        return START_NOT_STICKY;
    }

    private void startTracking() {
        Log.d(TAG, "startTracking");

        if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(this) == ConnectionResult.SUCCESS) {

            googleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();

            if (!googleApiClient.isConnected() || !googleApiClient.isConnecting()) {
                googleApiClient.connect();
            }
        } else {
            Log.e(TAG, "unable to connect to google play services.");
        }
    }

    protected void sendLocationDataToWebsite(Location location) {

        // formatted for mysql datetime format
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getDefault());
        Date date = new Date(location.getTime());

        //SharedPreferences sharedPreferences = this.getSharedPreferences("com.websmithing.gpstracker.prefs", Context.MODE_PRIVATE);


        SharedPreferences sharedPreferences = getSharedPreferences("policar.policarappv6", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String ConductorId = sharedPreferences.getString("ConductorId","");
        String ConductorNumeroDocumento = sharedPreferences.getString("ConductorNumeroDocumento","");
        String VehiculoUnidad = sharedPreferences.getString("VehiculoUnidad","");
        String VehiculoPlaca = sharedPreferences.getString("VehiculoPlaca","");
        String ConductorNombre = sharedPreferences.getString("ConductorNombre","");

        String PedidoId = sharedPreferences.getString("PedidoId","");
        String PedidoCoordenadaX = sharedPreferences.getString("PedidoCoordenadaX","");
        String PedidoCoordenadaY = sharedPreferences.getString("PedidoCoordenadaY","");

        String RegionId = sharedPreferences.getString("RegionId","");
        String RegionNombre = sharedPreferences.getString("RegionNombre","");
        String EmpresaId = sharedPreferences.getString("EmpresaId","");
        String EmpresaNombre = sharedPreferences.getString("EmpresaNombre","");

       // float totalDistanceInMeters = sharedPreferences.getFloat("totalDistanceInMeters", 0f);

       // boolean firstTimeGettingPosition = sharedPreferences.getBoolean("firstTimeGettingPosition", true);

       // if (firstTimeGettingPosition) {
      //      editor.putBoolean("firstTimeGettingPosition", false);
     //   } else {
      //      Location previousLocation = new Location("");
       //     previousLocation.setLatitude(sharedPreferences.getFloat("previousLatitude", 0f));
       //     previousLocation.setLongitude(sharedPreferences.getFloat("previousLongitude", 0f));

        //    float distance = location.distanceTo(previousLocation);
       //     totalDistanceInMeters += distance;
       ///     editor.putFloat("totalDistanceInMeters", totalDistanceInMeters);

        String VehiculoCoordenadaX = "";
        String VehiculoCoordenadaY = "";

        if(location.getLatitude() != 0 && location.getLongitude() != 0){

            editor.putString("VehiculoCoordenadaX", Double.toString(location.getLatitude()));
            editor.putString("VehiculoCoordenadaY", Double.toString(location.getLongitude()));
            editor.apply();

            VehiculoCoordenadaX = Double.toString(location.getLatitude());
            VehiculoCoordenadaY = Double.toString(location.getLongitude());

        }

      /*  SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("VehiculoCoordenadaX",  Double.toString(location.getLatitude()));
        editor.putString("VehiculoCoordenadaY", Double.toString(location.getLongitude()));
        editor.apply();*/

        final RequestParams requestParams = new RequestParams();

        requestParams.put("PedidoId", PedidoId);
        requestParams.put("ConductorId", ConductorId);
        requestParams.put("ConductorNumeroDocumento", ConductorNumeroDocumento);
        requestParams.put("ConductorNombre", ConductorNombre);

        requestParams.put("VehiculoUnidad", VehiculoUnidad);
        requestParams.put("VehiculoPlaca", VehiculoPlaca);

        requestParams.put("VehiculoCoordenadaX", VehiculoCoordenadaX);
        requestParams.put("VehiculoCoordenadaY", VehiculoCoordenadaY);
        requestParams.put("PedidoCoordenadaX", PedidoCoordenadaX);
        requestParams.put("PedidoCoordenadaY", PedidoCoordenadaY);

        requestParams.put("RegionId", RegionId);
        requestParams.put("RegionNombre", RegionNombre);
        requestParams.put("EmpresaId", EmpresaId);
        requestParams.put("EmpresaNombre", EmpresaNombre);

        requestParams.put("Formulario", "LocationService");
        requestParams.put("AppVersion", getString(R.string.app_version));

        requestParams.put("Accion", "ActualizarConductorVehiculoCoordenadaPermanente");

        //requestParams.put("latitude", Double.toString(location.getLatitude()));
       // requestParams.put("longitude", Double.toString(location.getLongitude()));

        Double speedInMilesPerHour = location.getSpeed()* 2.2369;
        requestParams.put("VehiculoVelocidad",  Integer.toString(speedInMilesPerHour.intValue()));

        //try {
        //    requestParams.put("date", URLEncoder.encode(dateFormat.format(date), "UTF-8"));
      //  } catch (UnsupportedEncodingException e) {
       //
       // }

        requestParams.put("VehiculoGPSProveedor", location.getProvider());

        //if (totalDistanceInMeters > 0) {
        //    requestParams.put("distance", String.format("%.1f", totalDistanceInMeters / 1609)); // in miles,
        //} else {
        //    requestParams.put("distance", "0.0"); // in miles
       // }

        //requestParams.put("username", sharedPreferences.getString("userName", ""));
        //requestParams.put("phonenumber", sharedPreferences.getString("appID", "")); // uuid
      //  requestParams.put("sessionid", sharedPreferences.getString("sessionID", "")); // uuid

        Double accuracyInFeet = location.getAccuracy()* 3.28;
        requestParams.put("VehiculoGPSExactitud",  Integer.toString(accuracyInFeet.intValue()));

        //Double altitudeInFeet = location.getAltitude() * 3.28;
      //  requestParams.put("extrainfo",  Integer.toString(altitudeInFeet.intValue()));

        //requestParams.put("eventtype", "android");

        Float direction = location.getBearing();
        requestParams.put("VehiculoGPSOrientacion",  Integer.toString(direction.intValue()));

//        final String uploadWebsite = sharedPreferences.getString("defaultUploadWebsite", defaultUploadWebsite);
        //getString(R.string.app_url)+"/webservice/"+getString(R.string.webservice_jnconductor)

        Log.e("Navegacion8", "Tran coord");

        LoopjHttpClient.post(defaultUploadWebsite, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                LoopjHttpClient.debugLoopJ(TAG, "sendLocationDataToWebsite - success", defaultUploadWebsite, requestParams, responseBody, headers, statusCode, null);
                stopSelf();
            }
            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] errorResponse, Throwable e) {
                LoopjHttpClient.debugLoopJ(TAG, "sendLocationDataToWebsite - failure", defaultUploadWebsite, requestParams, errorResponse, headers, statusCode, e);
                stopSelf();
            }
        });


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            Log.e(TAG, "position: " + location.getLatitude() + ", " + location.getLongitude() + " accuracy: " + location.getAccuracy());

            // we have our desired accuracy of 500 meters so lets quit this service,
            // onDestroy will be called and stop our location uodates
            if (location.getAccuracy() < 500.0f) {
                stopLocationUpdates();
                sendLocationDataToWebsite(location);
            }
        }
    }

    private void stopLocationUpdates() {
        if (googleApiClient != null && googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }

    /**
     * Called by Location Services when the request to connect the
     * client finishes successfully. At this point, you can
     * request the current location or start periodic updates
     */
    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "onConnected");

        locationRequest = LocationRequest.create();
        locationRequest.setInterval(1000); // milliseconds
        locationRequest.setFastestInterval(1000); // the fastest rate in milliseconds at which your app can handle location updates
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationServices.FusedLocationApi.requestLocationUpdates(
                googleApiClient, locationRequest, this);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(TAG, "onConnectionFailed");

        stopLocationUpdates();
        stopSelf();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e(TAG, "GoogleApiClient connection has been suspend");
    }
}

