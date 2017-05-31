package edu.upc.pes.wallachange.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import edu.upc.pes.wallachange.AddElementFragment;
import edu.upc.pes.wallachange.R;
import edu.upc.pes.wallachange.ViewElementFragment;

/**
 * Created by Usuario on 31/05/2017.
 */

public class CategoriesViewElementAdapter extends ArrayAdapter<String> {

    private Context mContext;
    private int layoutResourceId;
    private ArrayList<String> data;
    private ViewElementFragment callBack;
    private Boolean editables;

    public CategoriesViewElementAdapter(Context context, int layoutResourceId, ArrayList<String> data, ViewElementFragment viewElementFragment, boolean tagsEditables) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.mContext = context;
        this.data = data;
        this.callBack = viewElementFragment;
        this.editables = tagsEditables;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        if(convertView==null){
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);
        }

        String categoria = data.get(position);

        TextView textViewCategoria = (TextView) convertView.findViewById(R.id.textViewCategory);
        textViewCategoria.setText(categoria);
        ImageButton botoEsborrarCategoria = (ImageButton) convertView.findViewById(R.id.botoEsborrarCategoria);
        if (!editables){
            botoEsborrarCategoria.setVisibility(View.GONE);
            botoEsborrarCategoria.setEnabled(false);
        }
        botoEsborrarCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.remove(position);
                notifyDataSetChanged();
                callBack.actualitzarCategories(data);

            }
        });
        return convertView;
    }
}
