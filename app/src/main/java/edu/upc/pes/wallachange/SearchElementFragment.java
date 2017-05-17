package edu.upc.pes.wallachange;

import static com.android.volley.VolleyLog.TAG;

import android.app.Fragment;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edu.upc.pes.wallachange.APILayer.AdapterAPIRequest;
import edu.upc.pes.wallachange.APILayer.Proxy;
import edu.upc.pes.wallachange.Adapters.ListElementsAdapter;
import edu.upc.pes.wallachange.Models.CurrentUser;
import edu.upc.pes.wallachange.Models.Element;



public class SearchElementFragment extends Fragment implements View.OnClickListener{

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
        view = inflater.inflate(R.layout.fragment_search_element, container, false);
        myActivity = (MainActivity) getActivity();
        myActivity.setTitle(R.string.navigationSearchItem_eng);
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
        finder.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (i) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            myActivity.hideKeyboard();
                            return true;
                        default: break;
                    }
                }
                return false;
            }
        });
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
                Proxy proxy = new Proxy();
                ArrayList<Element> elements2 = null;
                try {
                    elements2 = proxy.getElements(finder.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (!elements2.isEmpty()) {
                    elements = elements2;
                    listElementsAdapter.notifyDataSetChanged();
                }
                break;
        }
    }
}

