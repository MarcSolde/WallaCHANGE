package edu.upc.pes.wallachange;


import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

import edu.upc.pes.wallachange.Adapters.SeeProfileAdapter;
import edu.upc.pes.wallachange.Models.Element;
import edu.upc.pes.wallachange.Models.User;


public class SeeProfileFragment extends Fragment {
    private MainActivity myActivity;
    private User user;

    private SeeProfileAdapter adapter;
    private ListView myListView;

    private ArrayList<Element> elements;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_see_profile, container, false);
        myActivity = (MainActivity) getActivity();
        myActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        myActivity.setTitle(R.string.navigationProfile_eng);


        String id = getArguments().getString("id");
        //TODO:get user
        ArrayList<String> aux5 = new ArrayList<> ();
        aux5.add("aaa");
        aux5.add("bbb");
        aux5.add("ccc");
        user = new User("id","Pepe","Sant Cugat","aaa",4,null,aux5);

        TextView aux = (TextView) view.findViewById(R.id.see_user_username);
        aux.setText(user.getUsername());
        aux = (TextView) view.findViewById(R.id.see_user_city);
        aux.setText(user.getLocation());
        aux = (TextView) view.findViewById(R.id.see_user_preference);
        String aux2 = TextUtils.join(", ", user.getPreferences());
        aux.setText(aux2);
        RatingBar aux3 = (RatingBar) view.findViewById(R.id.see_user_rating);
        aux3.setRating(user.getRating());

        myListView = (ListView) view.findViewById(R.id.see_user_list);
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                onClickElement(i);
            }
        });
        loadList();

        return view;
    }

    private void loadList () {
        //TODO:
        elements = new ArrayList<>();
        elements.add(new Element("1","aaa1",null,"aaa2",null,"aaa3",null,null,null));
        elements.add(new Element("2","bbb1",null,"bbb2",null,"bbb3",null,null,null));
        adapter = new SeeProfileAdapter(myActivity,R.layout.item_see_profile,elements,this);
        myListView.setAdapter(adapter);
        myListView.deferNotifyDataSetChanged();
    }

    private void onClickElement (int i) {
        myActivity.changeToItem(elements.get(i));
    }
}
