package edu.upc.pes.wallachange.Adapters;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import edu.upc.pes.wallachange.FragmentAddElement;
import edu.upc.pes.wallachange.R;


public class ImatgesMiniaturaListViewAdapter extends ArrayAdapter<Uri> {
    private Context mContext;
    private int layoutResourceId;
    private List<Uri> data;
    private FragmentAddElement callBack;

    public ImatgesMiniaturaListViewAdapter(Context mContext, int layoutResourceId, List<Uri> data, FragmentAddElement fragmentAddElement) {
        super(mContext, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.data = data;
        this.callBack = fragmentAddElement;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        if(convertView==null){
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);
        }
        final Uri uri = data.get(position);
        ImageView imatge = (ImageView) convertView.findViewById(R.id.imatgeMiniatura);
        Picasso.with(getContext()).load(uri).resize(100, 100).into(imatge);
        ImageButton botoTreureImatge = (ImageButton) convertView.findViewById(R.id.botoEsborrarImatge);
        botoTreureImatge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.remove(position);
                notifyDataSetChanged();
                callBack.actualitzarNombreImatges(data);
            }
        });
        return convertView;
    }

    @Override
    public int getViewTypeCount() {

        return getCount();
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

}