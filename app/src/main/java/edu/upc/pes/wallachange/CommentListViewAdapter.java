package edu.upc.pes.wallachange;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Usuario on 28/04/2017.
 */

public class CommentListViewAdapter extends ArrayAdapter<Comment>{

    private Context mContext;
    private int layoutResourceId;
    private List<Comment> data;
    private FragmentViewElement callBack;

    public CommentListViewAdapter(Context mContext, int layoutResourceId, List<Comment> data, FragmentViewElement fragmentViewElement) {
        super(mContext, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.data = data;
        this.callBack = fragmentViewElement;
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

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            final Uri uri = data.get(position).getFotoUsuari();
            ImageView imatge = (ImageView) convertView.findViewById(R.id.imatgeUsuari);
            Picasso.with(getContext()).load(uri).resize(100, 100).into(imatge);
            holder.headlineView = (TextView) convertView.findViewById(R.id.textComentari);
            holder.reporterNameView = (TextView) convertView.findViewById(R.id.nomUsuari);
            holder.reportedDateView = (TextView) convertView.findViewById(R.id.dataComentari);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.reportedDateView.setText(data.get(position).getData().toString());
        holder.reporterNameView.setText("By, " + data.get(position).getNomUsuari());
        holder.headlineView.setText(data.get(position).getTextComentari());
        return convertView;
    }

    static class ViewHolder {
        TextView headlineView;
        TextView reporterNameView;
        TextView reportedDateView;
    }
}
