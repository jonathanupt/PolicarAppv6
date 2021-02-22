package policar.policarappv6;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

/**
 * Created by USUARIO on 20/03/2016.
 */

public class PedidoActualListener implements LocationListener {

    PedidoActualActivity pedidoActualActivity;

    public PedidoActualActivity getPedidoActualActivity() {
        return pedidoActualActivity;
    }

    public void setPedidoActualActivity(PedidoActualActivity pedidoActualActivity) {
        this.pedidoActualActivity = pedidoActualActivity;
    }

    @Override
    public void onLocationChanged(Location loc) {
        // Este metodo se ejecuta cada vez que el GPS recibe nuevas coordenadas
        // debido a la detecci—n de un cambio de ubicacion
        loc.getLatitude();
        loc.getLongitude();
        // String Text = "Mi ubicaci—n actual es: " + "\n Lat = "
        //         + loc.getLatitude() + "\n Long = " + loc.getLongitude();
        // messageTextView.setText(Text);
        // this.mainActivity.setLocation(loc);
    }

    @Override
    public void onProviderDisabled(String provider) {
        // Este mŽtodo se ejecuta cuando el GPS es desactivado
        // messageTextView.setText("GPS Desactivado");
    }

    @Override
    public void onProviderEnabled(String provider) {
        // Este mŽtodo se ejecuta cuando el GPS es activado
        //messageTextView.setText("GPS Activado");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // Este mŽtodo se ejecuta cada vez que se detecta un cambio en el
        // status del proveedor de localizaci—n (GPS)
        // Los diferentes Status son:
        // OUT_OF_SERVICE -> Si el proveedor esta fuera de servicio
        // TEMPORARILY_UNAVAILABLE -> Temp˜ralmente no disponible pero se
        // espera que este disponible en breve
        // AVAILABLE -> Disponible
    }


}
