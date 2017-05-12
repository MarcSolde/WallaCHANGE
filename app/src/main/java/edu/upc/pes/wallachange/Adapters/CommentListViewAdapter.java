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

import com.squareup.picasso.Picasso;

import java.util.List;

import edu.upc.pes.wallachange.Models.Comment;
import edu.upc.pes.wallachange.R;


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
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
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

        holder.reportedDateView.setText(data.get(position).getData());
        String comentarista = mContext.getString(R.string.by_eng) + ", " + data.get(position).getNomUsuari();
        holder.reporterNameView.setText(comentarista);
        holder.headlineView.setText(data.get(position).getTextComentari());
        return convertView;
    }

    static class ViewHolder {
        TextView headlineView;
        TextView reporterNameView;
        TextView reportedDateView;
    }
}
