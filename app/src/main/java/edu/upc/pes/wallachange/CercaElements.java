package edu.upc.pes.wallachange;

import android.app.Fragment;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by carlota on 19/4/17.
 */


public class CercaElements extends Fragment implements View.OnClickListener{

    private MainActivity myActivity;
    private Element element1;
    private Element element2;
    private Element element3;
    private Element element4;
    private Element element5;
    private Element element6;
    private ArrayList<Element> elements;
    private ListElementsAdapter listElementsAdapter;
    private EditText finder;




    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.cerca_elements, container, false);
        myActivity = (MainActivity) getActivity();
        myActivity.setTitle(R.string.navigationSearch_eng);
        ArrayList<Uri> list = new ArrayList<>();
        Uri imgProva=Uri.parse("android.resource://edu.upc.pes.wallachange/"+R.drawable.empty_picture);
        list.add(imgProva);

        ListView listElemsView = (ListView) view.findViewById(R.id.listElements);


        //s'haura de fer GET a la db, no això.
        element1 = new Element();
        element1.setTitol("titol1");
        element1.setCategoria("cat1");
        element1.setUser("user1");
        element1.setFotografies(list);

        element2 = new Element();
        element2.setTitol("titol2");
        element2.setCategoria("cat2");
        element2.setUser("user2");
        element2.setFotografies(list);

        element3 = new Element();
        element3.setTitol("titol3");
        element3.setCategoria("cat3");
        element3.setUser("user3");
        element3.setFotografies(list);

        element4 = new Element();
        element4.setTitol("titol4");
        element4.setCategoria("cat4");
        element4.setUser("user4");
        element4.setFotografies(list);

        element5 = new Element();
        element5.setTitol("titol5");
        element5.setCategoria("cat5");
        element5.setUser("user5");
        element5.setFotografies(list);

        element6 = new Element();
        element6.setTitol("titol6");
        element6.setCategoria("cat6");
        element6.setUser("user6");
        element6.setFotografies(list);

        //Llavors el que em retorna la db ho afegeixo a la llista elements
        elements = new ArrayList<Element>();
        elements.add(element1);
        elements.add(element2);
        elements.add(element3);
        elements.add(element4);
        elements.add(element5);
        elements.add(element6);


        finder = (EditText) view.findViewById(R.id.finder);
        listElementsAdapter = new ListElementsAdapter(myActivity, R.layout.product_on_list, elements, this);
        listElemsView.setAdapter(listElementsAdapter);
        listElementsAdapter.notifyDataSetChanged();

        ImageView clearField = (ImageView) view.findViewById(R.id.cleanFieldSearch);
        clearField.setOnClickListener(this);

        ImageView findButt = (ImageView) view.findViewById(R.id.SearchButt);
        findButt.setOnClickListener(this);
        return view;
    }

//    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cleanFieldSearch:
                finder.setText("");
                break;
            case R.id.SearchButt:
                break;
        }
    }
}

