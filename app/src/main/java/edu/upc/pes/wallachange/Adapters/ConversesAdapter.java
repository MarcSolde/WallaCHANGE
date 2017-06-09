package edu.upc.pes.wallachange.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
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

        Boolean confirmat = conv.getConfirmat();

        TextView myElement = (TextView) convertView.findViewById(R.id.myElement);
//        myElement.setText("ola");
        myElement.setText(conv.getNomElem1());


        TextView yourElement = (TextView) convertView.findViewById(R.id.alterElement);
        yourElement.setText(conv.getNomElem2());

        TextView alterUser = (TextView) convertView.findViewById(R.id.alterUser);
        alterUser.setText(conv.getNomUserOther());

        if (confirmat) {
            LinearLayout linlay = (LinearLayout) convertView.findViewById(R.id.layout_c);
            linlay.setBackgroundColor(0xFFCBF9CE);
        }
        if (conv.getCancelat()) {
            LinearLayout linlay = (LinearLayout) convertView.findViewById(R.id.layout_c);
            linlay.setBackgroundColor(0xFFFFCCCC);
        }

        return convertView;

    }


}
