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



public class SeeProfileAdapter extends ArrayAdapter<Element> {
    private Context myContext;
    private int layoutResourceId;
    private ArrayList<Element> data;
    private SeeProfileFragment callBack;

    public SeeProfileAdapter(Context context, int layoutResourceId, ArrayList<Element> data, SeeProfileFragment callBack) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.myContext = context;
        this.data = data;
        this.callBack = callBack;
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
        TextView aux = (TextView) convertView.findViewById(R.id.item_see_profile_title);
        aux.setText(var.getTitol());
        aux = (TextView) convertView.findViewById(R.id.item_see_profile_category);
        // TODO: ara mateix aqui nomes safegeix la primera categoria. S'haurien d'afegir les que es cregui convenient
        //aux.setText(var.getTags().get(0));
        aux = (TextView) convertView.findViewById(R.id.item_see_profile_temporal);
        aux.setText(var.getTemporalitat());
        return convertView;
    }
}
