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
public class CanalConductorAdapter extends BaseAdapter {

    private static ArrayList<ConductoresResults> searchArrayList;

    private LayoutInflater mInflater;

    public CanalConductorAdapter(Context context, ArrayList<ConductoresResults> results) {
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

            convertView = mInflater.inflate(R.layout.row_canal_conductor, null);

            holder = new ViewHolder();

            holder.txtConductorNombre = (TextView) convertView.findViewById(R.id.CmpConductorNombre);
            holder.txtConductorVehiculoUnidad = (TextView) convertView.findViewById(R.id.CmpConductorVehiculoUnidad);
            //holder.txtConductorCanal = (TextView) convertView.findViewById(R.id.CmpConductorCanal);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.txtConductorNombre.setText(searchArrayList.get(position).getConductorNombre());
        holder.txtConductorVehiculoUnidad.setText(searchArrayList.get(position).getConductorVehiculoUnidad());
       // holder.txtConductorCanal.setText(searchArrayList.get(position).getConductorCanal());

        return convertView;
    }

    static class ViewHolder {

        TextView txtConductorNombre;
        TextView txtConductorVehiculoUnidad;
        //TextView txtConductorCanal;

    }


}
