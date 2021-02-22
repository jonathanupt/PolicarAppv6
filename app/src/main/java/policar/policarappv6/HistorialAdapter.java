package policar.policarappv6;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by USUARIO on 02/07/2015.
 */
public class HistorialAdapter extends BaseAdapter {

    private static ArrayList<HistorialResults> searchArrayList;

    private LayoutInflater mInflater;

    public HistorialAdapter(Context context, ArrayList<HistorialResults> results) {
        searchArrayList = results;
        mInflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return searchArrayList.size();
    }

    public Object getItem(int position) {
        return searchArrayList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {

            convertView = mInflater.inflate(R.layout.row_historial, null);

            holder = new ViewHolder();

            holder.txtPedidoDireccion = (TextView) convertView.findViewById(R.id.CmpPedidoDireccion);
            //holder.txtNumero = (TextView) convertView.findViewById(R.id.CmpNumero);
            holder.txtPedidoReferencia = (TextView) convertView.findViewById(R.id.CmpConductorNombre);
            holder.txtPedidoFecha = (TextView) convertView.findViewById(R.id.CmpPedidoFecha);
            holder.txtPedidoHora = (TextView) convertView.findViewById(R.id.CmpPedidoHora);
            ///holder.txtVehiculoUnidad = (TextView) convertView.findViewById(R.id.CmpVehiculoUnidad);
            //holder.txtVehiculoPlaca = (TextView) convertView.findViewById(R.id.CmpVehiculoPlaca);
            //holder.txtVehiculoColor = (TextView) convertView.findViewById(R.id.CmpVehiculoColor);
            //holder.txtVehiculoConductor = (TextView) convertView.findViewById(R.id.CmpCliente);
            holder.txtClienteNombre = (TextView) convertView.findViewById(R.id.CmpClienteNombre);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.txtPedidoDireccion.setText(searchArrayList.get(position).getPedidoDireccion());
        holder.txtPedidoReferencia.setText(searchArrayList.get(position).getPedidoReferencia());

        holder.txtPedidoFecha.setText(searchArrayList.get(position).getPedidoFecha());
        holder.txtPedidoHora.setText(searchArrayList.get(position).getPedidoHora());

            holder.txtClienteNombre.setText(searchArrayList.get(position).getClienteNombre());

        return convertView;
    }

    static class ViewHolder {

        TextView txtPedidoDireccion;
        TextView txtPedidoReferencia;

        TextView txtPedidoFecha;
        TextView txtPedidoHora;

        TextView txtClienteNombre;

    }


}
