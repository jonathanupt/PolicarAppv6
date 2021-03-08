package policar.policarappv6;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Jonathan on 24/08/2016.
 */
public class NavegacionAdapter extends BaseAdapter {


    private static ArrayList<NavegacionResults> searchArrayList;

    private LayoutInflater mInflater;

    public NavegacionAdapter(Context context, ArrayList<NavegacionResults> results) {
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

            convertView = mInflater.inflate(R.layout.row_navegacion, null);

            holder = new ViewHolder();

            holder.txtPedidoDireccion = (TextView) convertView.findViewById(R.id.CmpPedidoDireccion);
            holder.txtPedidoReferencia = (TextView) convertView.findViewById(R.id.CmpPedidoReferencia);

            holder.txtClienteNombre = (TextView) convertView.findViewById(R.id.CmpPedidoClienteNombre);

            //holder.txtPedidoFecha = (TextView) convertView.findViewById(R.id.CmpPedidoFecha);
           // holder.txtPedidoHora = (TextView) convertView.findViewById(R.id.CmpPedidoHora);
            holder.imgPedidoFoto = (ImageView) convertView.findViewById(R.id.imgConductorCanalFoto);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.txtPedidoDireccion.setText(searchArrayList.get(position).getPedidoDireccion());
        holder.txtPedidoReferencia.setText(searchArrayList.get(position).getPedidoReferencia());

        holder.txtClienteNombre.setText(searchArrayList.get(position).getClienteNombre());

       // holder.txtPedidoFecha.setText(searchArrayList.get(position).getPedidoFecha());
       // holder.txtPedidoHora.setText(searchArrayList.get(position).getPedidoHora());

/*
0 gris
a rojo
b verde
c azzul
*/
        if(searchArrayList.get(position).getPedidoTipo().equals("1")){

            holder.imgPedidoFoto.setImageResource(R.mipmap.icon_pedidoc150);

        }else if(searchArrayList.get(position).getPedidoTipo().equals("6") || searchArrayList.get(position).getPedidoTipo().equals("1")){

            holder.imgPedidoFoto.setImageResource(R.mipmap.icon_pedidod150);

        }else if(searchArrayList.get(position).getPedidoTipo().equals("4")){

            holder.imgPedidoFoto.setImageResource(R.mipmap.icon_pedidob150);

        }else if(searchArrayList.get(position).getPedidoTipo().equals("7") || searchArrayList.get(position).getPedidoTipo().equals("11")){

            holder.imgPedidoFoto.setImageResource(R.mipmap.icon_pedidoa150);

        }else{
            holder.imgPedidoFoto.setImageResource(R.mipmap.icon_pedido0150);
        }


        return convertView;
    }

    static class ViewHolder {

        TextView txtPedidoDireccion;
        TextView txtPedidoReferencia;

        TextView txtPedidoFecha;
        TextView txtPedidoHora;

        TextView txtClienteNombre;
        ImageView imgPedidoFoto;

    }

    
}
