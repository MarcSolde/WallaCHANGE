package edu.upc.pes.wallachange.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import edu.upc.pes.wallachange.Models.Element;
import edu.upc.pes.wallachange.R;
import edu.upc.pes.wallachange.SeeProfileFragment;



public class ElementListAdapter extends ArrayAdapter<Element> {
    private Context myContext;
    private int layoutResourceId;
    private ArrayList<Element> data;

    public ElementListAdapter(Context context, int layoutResourceId, ArrayList<Element> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.myContext = context;
        this.data = data;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        if(convertView==null){
            LayoutInflater inflater = ((Activity) myContext).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);
        }

        Element var = data.get(position);

        //ImageView image = (ImageView) convertView.findViewById(R.id.item_search_user_image);
        //image.setImage
        TextView aux = (TextView) convertView.findViewById(R.id.item_text1);
        aux.setText(var.getTitol());

        aux = (TextView) convertView.findViewById(R.id.item_text2);
        ArrayList<String> aux2 = var.getTags();
        if (aux2.size() == 0) aux.setText("[...]");
        else aux.setText(var.getTags().toString());

        aux = (TextView) convertView.findViewById(R.id.item_text3);
        String aux3;
        if (var.getTipusProducte()) aux3 = myContext.getString(R.string.product_eng);
        else aux3 = myContext.getString(R.string.experience_eng);
        if (var.getEsTemporal()) {
            aux.setText(aux3 + " - " + var.getTemporalitat());
        }
        else aux.setText(aux3 + " - " + myContext.getString(R.string.permanent_eng));
        return convertView;
    }
}
