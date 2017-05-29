package edu.upc.pes.wallachange.Adapters;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import edu.upc.pes.wallachange.R;
import edu.upc.pes.wallachange.Fragments.SearchUserFragment;
import edu.upc.pes.wallachange.Models.User;

public class SearchUserAdapter extends ArrayAdapter<User>{
    private Context myContext;
    private int layoutResourceId;
    private ArrayList<User> data;
    private SearchUserFragment callBack;

    public SearchUserAdapter(Context context, int layoutResourceId, ArrayList<User> data, SearchUserFragment callBack) {
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

        User var = data.get(position);

        //ImageView image = (ImageView) convertView.findViewById(R.id.item_search_user_image);
        //image.setImage
        TextView user = (TextView) convertView.findViewById(R.id.item_search_user_name);
        user.setText(var.getUsername());
        TextView preference = (TextView) convertView.findViewById(R.id.item_search_user_preference);
        ArrayList<String> preferences = var.getPreferences();
        String pref = "Preferences: ";
        for (int i = 0;i < preferences.size();++i) pref = pref + preferences.get(i) + " ";
        preference.setText(pref);
        TextView rating = (TextView) convertView.findViewById(R.id.item_search_user_rating);
        String rat = "Rating: " + var.getRating()/20 + "/5.0";
        rating.setText(rat);
        return convertView;
    }
}
