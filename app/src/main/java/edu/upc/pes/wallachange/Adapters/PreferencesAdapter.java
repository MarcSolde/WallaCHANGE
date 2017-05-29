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

import edu.upc.pes.wallachange.Fragments.ProfileFragment;
import edu.upc.pes.wallachange.R;


public class PreferencesAdapter extends ArrayAdapter<String> {

    private Context mContext;
    private int layoutResourceId;
    private ArrayList<String> data;
    private ProfileFragment callBack;

//    public PreferencesAdapter(@NonNull Context context, @LayoutRes int layoutResourceId, ArrayList<String> data) {
//        super(context, layoutResourceId, data);
//        this.layoutResourceId = layoutResourceId;
//        this.mContext = context;
//        this.data = data;
//        //this.callBack = profileEdit;
//    }


    public PreferencesAdapter(Context context, int layoutResourceId, ArrayList<String> data, ProfileFragment profileFragment) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.mContext = context;
        this.data = data;
        this.callBack = profileFragment;
    }



/*
    public PreferencesAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public PreferencesAdapter(Context context, int resource, List<Item> items) {
        super(context, resource, items);
    }*/




    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        if(convertView==null){
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);
        }

        String pref1 = data.get(position);

        TextView texttext = (TextView) convertView.findViewById(R.id.textViewPref);
        texttext.setText(pref1);
        /*final Uri uri = data.get(position);
        ImageView imatge = (ImageView) convertView.findViewById(R.id.imatgeMiniatura);
        Picasso.with(getContext()).load(uri).resize(100, 100).into(imatge);*/
        ImageButton botoEsborrarPref = (ImageButton) convertView.findViewById(R.id.botoEsborrarPref);
        botoEsborrarPref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String prefToDelete = data.get(position);
                data.remove(position);
                notifyDataSetChanged();
                callBack.decrementarNombrePrefs(data, prefToDelete);

            }
        });
        return convertView;
    }
}
