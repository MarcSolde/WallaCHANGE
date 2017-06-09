package edu.upc.pes.wallachange;

import static com.android.volley.VolleyLog.TAG;

import android.app.Fragment;
import android.app.FragmentManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v7.graphics.Palette;
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
import android.widget.Toast;

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
import edu.upc.pes.wallachange.Adapters.ElementListAdapter;
import edu.upc.pes.wallachange.Adapters.ListElementsAdapter;
import edu.upc.pes.wallachange.Models.CurrentUser;
import edu.upc.pes.wallachange.Models.Element;
import edu.upc.pes.wallachange.Models.FilterElement;


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

    private FilterElement filterElement = new FilterElement();

    private ListView listElemsView;

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

        listElemsView = (ListView) view.findViewById(R.id.listElements);

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

        ImageView clearField = (ImageView) view.findViewById(R.id.cleanFieldSearch);
        clearField.setOnClickListener(this);

        ImageView findButt = (ImageView) view.findViewById(R.id.SearchButt);
        findButt.setOnClickListener(this);

        Button filterButt = (Button) view.findViewById(R.id.filterButton);
        filterButt.setOnClickListener(this);

        setFilter(getArguments().getString("tags"), getArguments().getString("temporalitat"), getArguments().getString("es_producte"));

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
                headers.put("tags", filterElement.getTags());
                headers.put("es_producte", filterElement.getTipus_element());
                headers.put("temporalitat", filterElement.getTemporalitat());

                adapter.GETJsonArrayRequestAPI("/api/element", new Response.Listener<JSONArray>() {

                            @Override
                            public void onResponse(JSONArray response) {
                                try {
                                    Log.i("JSON: ",response.toString());
                                    ArrayList<Element> aux = new ArrayList<> ();
                                    for (int i = 0;i < response.length();++i) {
                                        JSONObject var = response.getJSONObject(i);
                                        Element aux2 = new Element(var);
                                        aux.add(aux2);
                                    }
                                    loadList(aux);
                                }
                                catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.i("JSONerror: ",error.getMessage());
                            }
                        },
                        headers
                );

                break;
        }
    }

    private void setFilter(String tags, String temporalitat, String es_producte){
        filterElement.setTags(tags);
        filterElement.setEs_producte(es_producte);
        filterElement.setTemporalitat(temporalitat);
    }

    private void loadList (ArrayList<Element> e) {
        if (e.isEmpty()) {
            Toast.makeText(myActivity,R.string.search_no_result,Toast.LENGTH_SHORT).show();
        }
        elements = new ArrayList<>();
        elements = e;
        ElementListAdapter adapter = new ElementListAdapter(myActivity,R.layout.item_default,elements);
        listElemsView.setAdapter(adapter);
        listElemsView.deferNotifyDataSetChanged();
    }
}
