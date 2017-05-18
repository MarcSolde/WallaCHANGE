package edu.upc.pes.wallachange;

import static com.android.volley.VolleyLog.TAG;

import android.app.Fragment;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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

        Button filterButt = (Button) view.findViewById(R.id.filterButton);
        filterButt.setOnClickListener(this);
        return view;
    }

//    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cleanFieldSearch:
                finder.setText("");
                break;
            case R.id.filterButton:
                //aquí es crida el fragment del sergi
//                myFragmentManager.beginTransaction().replace(R.id.fragment,FiltersFragment).commit();

                break;
            case R.id.SearchButt:
                String title = finder.getText().toString();
                AdapterAPIRequest adapter = new AdapterAPIRequest();
                JSONObject body = new JSONObject();
                Map<String, String> headers = new HashMap<>();
                CurrentUser us = CurrentUser.getInstance();
                headers.put("x-access-token", us.getToken());
                headers.put("titol", title);
                //headers.put("Content-Type", "application/json");

                final ArrayList<Element> elements2 = new ArrayList<>();
                adapter.GETRequestAPI("http://10.0.2.2:3000/elements", new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                JSONArray ja = response;
                                try {
                                    if (ja != null) {
                                        int len = ja.length();
                                        for (int i = 0; i < len; i++) {
                                            Element elem = new Element();
                                            elem.setTitol(ja.getJSONObject(i).getString("titol"));
                                            elem.setCategoria(ja.getJSONObject(i).getString("categoria"));
                                            elem.setDescripcio(ja.getJSONObject(i).getString("descripcio"));
                                            elem.setTipusProducte(ja.getJSONObject(i).getString("tipus_element"));
                                            elem.setId(ja.getJSONObject(i).getString("id"));
                                            elem.setTagsArray(ja.getJSONObject(i).getJSONArray("tags"));
                                            elem.setFotografiesArray(ja.getJSONObject(i).getJSONArray("imatges"));
                                            elem.setUser(ja.getJSONObject(i).getString("nom_user"));
                                            if (ja.getJSONObject(i).getBoolean("es_temporal"))
                                                elem.setTipusIntercanvi("Temporal");
                                            else
                                                elem.setTipusIntercanvi("Permanent");
                                            elem.setComentarisArray(ja.getJSONObject(i).getJSONArray("comentaris"));
                                            elem.setCoordenadesArray(ja.getJSONObject(i).getJSONArray("coordenades"));
                                            elements2.add(elem);
                                        }
                                    }
                                } catch(JSONException e){
                                    e.printStackTrace();
                                }

                            }},
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                VolleyLog.d(TAG, "Error: " + error.getMessage());

                            }
                        },headers);

                if (!elements2.isEmpty()) {
                    elements = elements2;
                    listElementsAdapter.notifyDataSetChanged();
                }
                break;
        }
    }
}

