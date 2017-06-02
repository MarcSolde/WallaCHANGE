package edu.upc.pes.wallachange.Adapters;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import edu.upc.pes.wallachange.Models.Element;
import edu.upc.pes.wallachange.R;
import edu.upc.pes.wallachange.SearchUserFragment;
import edu.upc.pes.wallachange.Models.User;

public class UserListAdapter extends ArrayAdapter<User>{
    private Context myContext;
    private int layoutResourceId;
    private ArrayList<User> data;

    public UserListAdapter(Context context, int layoutResourceId, ArrayList<User> data) {
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

        User var = data.get(position);

        //ImageView image = (ImageView) convertView.findViewById(R.id.item_search_user_image);
        //image.setImage
        TextView user = (TextView) convertView.findViewById(R.id.item_text1);
        user.setText(var.getUsername());
        TextView preference = (TextView) convertView.findViewById(R.id.item_text2);
        ArrayList<String> preferences = var.getPreferences();
        String pref = "Preferences: ";
        for (int i = 0;i < preferences.size();++i) pref = pref + preferences.get(i) + " ";
        preference.setText(pref);
        TextView rating = (TextView) convertView.findViewById(R.id.item_text3);
        String rat = "Rating: " + var.getRating()/20 + "/5.0";
        rating.setText(rat);
        if (var.getPictureBitmap() != null) {
            ImageView foto = (ImageView) convertView.findViewById(R.id.item_search_user_image);
            foto.setImageBitmap(var.getPictureBitmap());
        }
        return convertView;
    }
}
