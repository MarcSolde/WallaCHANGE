package edu.upc.pes.wallachange;

import android.app.Fragment;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by carlota on 19/4/17.
 */


public class CercaElements extends Fragment /*implements View.OnClickListener*/{

    private MainActivity myActivity;
    private Element element1;
    private Element element2;
    private Element element3;
    private Element element4;
    private Element element5;
    private Element element6;
    private ArrayList<Element> elements;
    private ListElementsAdapter listElementsAdapter;




    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.cerca_elements, container, false);
        myActivity = (MainActivity) getActivity();

        ListView listElemsView = (ListView) view.findViewById(R.id.listElements);
        element1 = new Element();
        element1.setTitol("titol1");
        element1.setCategoria("cat1");
        element1.setUser("user1");

        element2 = new Element();
        element2.setTitol("titol2");
        element2.setCategoria("cat2");
        element2.setUser("user2");

        element3 = new Element();
        element3.setTitol("titol3");
        element3.setCategoria("cat3");
        element3.setUser("user3");

        element4 = new Element();
        element4.setTitol("titol4");
        element4.setCategoria("cat4");
        element4.setUser("user4");

        element5 = new Element();
        element5.setTitol("titol5");
        element5.setCategoria("cat5");
        element5.setUser("user5");

        element6 = new Element();
        element6.setTitol("titol6");
        element6.setCategoria("cat6");
        element6.setUser("user6");

        elements = new ArrayList<Element>();
        elements.add(element1);
        elements.add(element2);
        elements.add(element3);
        elements.add(element4);
        elements.add(element5);
        elements.add(element6);


        listElementsAdapter = new ListElementsAdapter(myActivity, R.layout.product_on_list, elements, this);
        listElemsView.setAdapter(listElementsAdapter);
        listElementsAdapter.notifyDataSetChanged();


        return view;
    }
}

