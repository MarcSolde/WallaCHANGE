package edu.upc.pes.wallachange.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import edu.upc.pes.wallachange.SearchElementFragment;
import edu.upc.pes.wallachange.Models.Element;
import edu.upc.pes.wallachange.R;


public class ListElementsAdapter extends ArrayAdapter<Element> {

    private Context mContext;
    private int layoutResourceId;
    private ArrayList<Element> data;
    private SearchElementFragment callBack;




    public ListElementsAdapter(Context context, int layoutResourceId, ArrayList<Element> data, SearchElementFragment searchElementFragment) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.mContext = context;
        this.data = data;
        this.callBack = searchElementFragment;
    }


    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        if(convertView==null){
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);
        }

        Element elem1 = data.get(position);

        TextView texttitol = (TextView) convertView.findViewById(R.id.nameProductText);
        texttitol.setText(elem1.getTitol());
        TextView textcat = (TextView) convertView.findViewById(R.id.categoryProductText);
        textcat.setText(elem1.getCategoria());
        TextView textuser = (TextView) convertView.findViewById(R.id.userProductText);
        textuser.setText(elem1.getUser());
//        ImageView imatge = (ImageView) convertView.findViewById(R.id.imatgeElement);
//        imatge.setImageURI(elem1.getFotografies())
        return convertView;
    }

}
