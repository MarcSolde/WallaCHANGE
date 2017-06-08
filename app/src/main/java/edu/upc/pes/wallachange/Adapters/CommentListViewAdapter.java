package edu.upc.pes.wallachange.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
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
import java.util.Objects;

import edu.upc.pes.wallachange.APILayer.AdapterAPIRequest;
import edu.upc.pes.wallachange.Models.Comment;
import edu.upc.pes.wallachange.Models.CurrentUser;
import edu.upc.pes.wallachange.R;

import static com.android.volley.VolleyLog.TAG;


public class CommentListViewAdapter extends ArrayAdapter<Comment>{

    private final Context mContext;
    private final int layoutResourceId;
    private final List<Comment> data;
    private final CurrentUser cu;
    private final String idElement;

    public CommentListViewAdapter(Context mContext, int layoutResourceId, List<Comment> data, String idElement) {
        super(mContext, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.data = data;
        this.cu = CurrentUser.getInstance();
        this.idElement = idElement;
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
            holder.textComentariTextView = (TextView) convertView.findViewById(R.id.textComentari);
            holder.nomUsuariTextView = (TextView) convertView.findViewById(R.id.nomUsuari);
            holder.dataComentariTextView = (TextView) convertView.findViewById(R.id.dataComentari);
            holder.deleteCommentImageButton = (ImageButton) convertView.findViewById(R.id.esborrarComentari);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        DateFormat df1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String dia = df1.format(data.get(position).getDia());
        dia = dia.replace(" "," - ");
        holder.dataComentariTextView.setText(dia);
        holder.textComentariTextView.setText(data.get(position).getTextComentari());

        String idUsuariAnunci = data.get(position).getIdUsuari();
        if (Objects.equals(cu.getId(), idUsuariAnunci)){
            holder.deleteCommentImageButton.setEnabled(true);
            holder.deleteCommentImageButton.setVisibility(View.VISIBLE);
            holder.deleteCommentImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // mostrar el dialeg de confirmacio
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle(R.string.delete_comment_eng);
                    builder.setIcon(R.drawable.ic_warning);
                    builder.setMessage(R.string.are_you_sure_you_want_delete_your_comment_eng);
                    builder.setPositiveButton(R.string.ok_eng, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            AdapterAPIRequest adapterAPIRequest = new AdapterAPIRequest();
                            Map<String, String> headers = new HashMap<>();
                            headers.put("Content-Type", "application/json");
                            headers.put("x-access-token",cu.getToken());
                            String idComentari = data.get(position).getIdComentari();
                            String url = "/api/element/".concat(idElement).concat("/comment/").concat(idComentari);
                            //adapter.POSTRequestAPI("http://104.236.98.100:3000/loginFB"
                            adapterAPIRequest.DELETERequestAPI(url,
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            data.remove(position);
                                            notifyDataSetChanged();
                                        }
                                    },new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            VolleyLog.d(TAG,error.getMessage());
                                        }
                                    }, headers);
                        }
                    });
                    builder.setNegativeButton(R.string.cancelProfile_eng, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                        }
                    });
                    builder.show();
                }
            });
        }else{
            holder.deleteCommentImageButton.setEnabled(false);
            holder.deleteCommentImageButton.setVisibility(View.GONE);
        }

        Map<String, String> headers = new HashMap<>();
        AdapterAPIRequest adapterAPIRequest = new AdapterAPIRequest();
        adapterAPIRequest.GETRequestAPI("/user/"+idUsuariAnunci,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String nomUsuari = response.getString("nom");
                            String comentarista = mContext.getString(R.string.by_eng) + ", " + nomUsuari;
                            holder.nomUsuariTextView.setText(comentarista);
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
        TextView textComentariTextView;
        TextView nomUsuariTextView;
        TextView dataComentariTextView;
        ImageButton deleteCommentImageButton;
    }

}
