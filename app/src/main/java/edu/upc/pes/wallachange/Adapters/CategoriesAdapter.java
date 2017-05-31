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


public class CategoriesAdapter extends ArrayAdapter<String> {

    private Context mContext;
    private int layoutResourceId;
    private ArrayList<String> data;
    private AddElementFragment callBack;

    public CategoriesAdapter(Context context, int layoutResourceId, ArrayList<String> data, AddElementFragment addElementFragment) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.mContext = context;
        this.data = data;
        this.callBack = addElementFragment;
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
