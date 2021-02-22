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
 * Created by Jonathan on 20/06/2017.
 */
public class RankingAdapter extends BaseAdapter {

    private static ArrayList<RankingResults> searchArrayList;

    private LayoutInflater mInflater;

    public RankingAdapter(Context context, ArrayList<RankingResults> results) {
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

            convertView = mInflater.inflate(R.layout.row_ranking, null);

            holder = new ViewHolder();

            holder.txtConductorNombre = (TextView) convertView.findViewById(R.id.CmpConductorNombre);
            holder.txtVehiculoUnidad = (TextView) convertView.findViewById(R.id.CmpConductorVehiculoUnidad);

            //   holder.txtPedidoFecha = (TextView) convertView.findViewById(R.id.CmpPedidoFecha);
            holder.txtRankingTotal = (TextView) convertView.findViewById(R.id.CmpRankingTotal);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtConductorNombre.setText(searchArrayList.get(position).getConductorNombre());
        holder.txtVehiculoUnidad.setText(searchArrayList.get(position).getVehiculoUnidad());

        holder.txtRankingTotal.setText(searchArrayList.get(position).getRankingTotal());


        return convertView;
    }

    static class ViewHolder {

        TextView txtConductorNombre;
        TextView txtVehiculoUnidad;
        ImageView imgConductorFoto;
        TextView txtRankingTotal;

    }
}
