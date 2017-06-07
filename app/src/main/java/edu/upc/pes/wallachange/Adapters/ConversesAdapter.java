package edu.upc.pes.wallachange.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import edu.upc.pes.wallachange.Models.Conversa;
import edu.upc.pes.wallachange.Models.User;
import edu.upc.pes.wallachange.R;

/**
 * Created by carlota on 7/6/17.
 */

public class ConversesAdapter extends ArrayAdapter<Conversa> {

    private Context myContext;
    private int layoutResourceId;
    private ArrayList<Conversa> data;


    public ConversesAdapter(Context context, int layoutResourceId, ArrayList<Conversa> data) {
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


        Conversa conv = data.get(position);
        TextView myElement = (TextView) convertView.findViewById(R.id.myElement);
        myElement.setText(conv.getNomElem1());

        TextView yourElement = (TextView) convertView.findViewById(R.id.alterElement);
        yourElement.setText(conv.getNomElem2());

        TextView alterUser = (TextView) convertView.findViewById(R.id.alterUser);
        alterUser.setText(conv.getNomUserOther());

        TextView message = (TextView) convertView.findViewById(R.id.message);
        message.setText(conv.getLastMessage());

//        //ImageView image = (ImageView) convertView.findViewById(R.id.item_search_user_image);
//        //image.setImage
//        TextView conv = (TextView) convertView.findViewById(R.id.chat_item);
//        user.setText(var.getUsername());
//        TextView preference = (TextView) convertView.findViewById(R.id.item_text2);
//        ArrayList<String> preferences = var.getPreferences();
//        String pref = "Preferences: ";
//        for (int i = 0;i < preferences.size();++i) pref = pref + preferences.get(i) + " ";
//        preference.setText(pref);
//        TextView rating = (TextView) convertView.findViewById(R.id.item_text3);
//        String rat = "Rating: " + var.getRating()/20 + "/5.0";
//        rating.setText(rat);
        return convertView;

    }
}
