package edu.upc.pes.wallachange;

import static com.android.volley.VolleyLog.TAG;

import android.app.Fragment;
import android.app.FragmentManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
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

    private FragmentManager myFragmentManager;

    private AdapterAPIRequest adapter = new AdapterAPIRequest();
    private Map<String, String> headers = new HashMap<>();



    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.fragment_search_element, container, false);
        myActivity = (MainActivity) getActivity();
        myActivity.setTitle("Home");
        ArrayList<Uri> list = new ArrayList<>();
        Uri imgProva=Uri.parse("android.resource://edu.upc.pes.wallachange/"+R.drawable.empty_picture);
        list.add(imgProva);

        ListView listElemsView = (ListView) view.findViewById(R.id.listElements);

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
        elements = new ArrayList<>();
        listElementsAdapter = new ListElementsAdapter(myActivity, R.layout.product_on_list, elements, this);
        listElemsView.setAdapter(listElementsAdapter);
        listElementsAdapter.notifyDataSetChanged();

        ImageView clearField = (ImageView) view.findViewById(R.id.cleanFieldSearch);
        clearField.setOnClickListener(this);

        ImageView findButt = (ImageView) view.findViewById(R.id.SearchButt);
        findButt.setOnClickListener(this);

        Button filterButt = (Button) view.findViewById(R.id.filterButton);
        filterButt.setOnClickListener(this);
        findButt.performClick();

        return view;
    }

    //    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cleanFieldSearch:
                finder.setText("");
                break;
            case R.id.filterButton:
                //TODO:
                myActivity.changeFragmentToFilters();

                break;
            case R.id.SearchButt:
                String title = finder.getText().toString();
                CurrentUser us = CurrentUser.getInstance();
                headers.put("x-access-token", us.getToken());
                headers.put("titol", title);
                adapter.GETJsonArrayRequestAPI("/api/element", new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                JSONArray ja = response;
                                try {
                                    if (ja != null) {
                                        int len = ja.length();
                                        elements.clear();
                                        for (int i = 0; i < len; i++) {
                                            Element elem = new Element();
                                            elem.setTitol(ja.getJSONObject(i).getString("titol"));
                                            elem.setDescripcio(ja.getJSONObject(i).getString("descripcio"));
//                                            elem.setTipusProducte(ja.getJSONObject(i).getString("tipus_element"));
                                            elem.setId(ja.getJSONObject(i).getString("id"));
//                                            elem.setTagsArray(ja.getJSONObject(i).getJSONArray("tags"));
//                                            elem.setFotografiesArray(ja.getJSONObject(i).getJSONArray("imatges"));
                                            elem.setUser(ja.getJSONObject(i).getString("user_id"));
//                                            if (ja.getJSONObject(i).getBoolean("es_temporal"))
//                                                elem.setTipusIntercanvi("Temporal");
//                                            else
//                                                elem.setTipusIntercanvi("Permanent");
//                                            //elem.setComentarisArray(ja.getJSONObject(i).getJSONArray("comentaris"));
                                            //elem.setCoordenadesArray(ja.getJSONObject(i).getJSONArray("coordenades"));
                                            elements.add(elem);

                                        }
                                        listElementsAdapter.notifyDataSetChanged();

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

                break;
        }
    }
}
