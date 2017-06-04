package edu.upc.pes.wallachange.Adapters;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.upc.pes.wallachange.APILayer.AdapterAPIRequest;
import edu.upc.pes.wallachange.Models.Comment;
import edu.upc.pes.wallachange.R;

import static com.android.volley.VolleyLog.TAG;


public class CommentListViewAdapter extends ArrayAdapter<Comment>{

    private final Context mContext;
    private final int layoutResourceId;
    private final List<Comment> data;

    public CommentListViewAdapter(Context mContext, int layoutResourceId, List<Comment> data) {
        super(mContext, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Comment getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            final Uri uri = data.get(position).getFotoUsuari();
            ImageView imatge = (ImageView) convertView.findViewById(R.id.imatgeUsuari);
            Picasso.with(getContext()).load(uri).into(imatge);
            holder.headlineView = (TextView) convertView.findViewById(R.id.textComentari);
            holder.reporterNameView = (TextView) convertView.findViewById(R.id.nomUsuari);
            holder.reportedDateView = (TextView) convertView.findViewById(R.id.dataComentari);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        DateFormat df1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String dia = df1.format(data.get(position).getDia());
        dia = dia.replace(" "," " + getContext().getResources().getString(R.string.at_eng) + " ");
        holder.reportedDateView.setText(dia);

        Map<String, String> headers = new HashMap<>();
        AdapterAPIRequest adapterAPIRequest = new AdapterAPIRequest();
        adapterAPIRequest.GETRequestAPI("/user/"+data.get(position).getNomUsuari(),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String nomUsuari = response.getString("nom");
                            String comentarista = mContext.getString(R.string.by_eng) + ", " + nomUsuari;
                            holder.reporterNameView.setText(comentarista);
                            holder.headlineView.setText(data.get(position).getTextComentari());
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                    }
                },
                headers
        );

        // TODO aqui hauria de fer la crida per obtenir la foto de lusuari del comentari

        return convertView;
    }

    static class ViewHolder {
        TextView headlineView;
        TextView reporterNameView;
        TextView reportedDateView;
    }

}
