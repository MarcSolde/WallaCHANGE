package edu.upc.pes.wallachange;


import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edu.upc.pes.wallachange.APILayer.AdapterAPIRequest;
import edu.upc.pes.wallachange.Adapters.ElementListAdapter;
import edu.upc.pes.wallachange.Models.CurrentUser;
import edu.upc.pes.wallachange.Models.Element;


public class MakeOfferFragment extends Fragment {
    private MainActivity myActivity;

    private ListView myListView;

    private Element element1, element2;
    private ArrayList<Element> elements;
    private static AdapterAPIRequest adapterAPI = new AdapterAPIRequest();

    private ImageView img1, img2;
    private TextView title1, title2, temporal1, temporal2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_make_offer, container, false);
        myActivity = (MainActivity) getActivity();
        myActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        myActivity.setTitle(R.string.navigationOffer_eng);

        element1 = element2 = null;

        CurrentUser user = CurrentUser.getInstance();
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("x-access-token",user.getToken());

        String id = getArguments().getString("id");

        adapterAPI.GETRequestAPI("/api/element/"+id,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.i("JSON: ",response.toString());
                            Element aux = new Element(response);
                            loadElement1(aux);
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("JSONerror: ","");
                    }
                },
                headers
        );

        myListView = (ListView) view.findViewById(R.id.see_your_list);
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                onClickElement(i);
            }
        });

        Button myButton = (Button) view.findViewById(R.id.make_offer_button);
        myButton.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (element2 == null) {
                    Toast.makeText(myActivity,R.string.offerExchangeError_eng,Toast.LENGTH_SHORT).show();
                }
                else {
                    //TODO:make offer
                    CurrentUser user = CurrentUser.getInstance();
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json");
                    headers.put("x-access-token",user.getToken());
                    JSONObject body = new JSONObject();
                    try {
                        body.put("id1", element1.getUser());
                        body.put("id2", element2.getUser());
                        body.put("idProd1", element1.getId());
                        body.put("idProd2", element2.getId());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    adapterAPI.POSTRequestAPI("/intercanvi/",
                            new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        Log.i("JSON: ",response.toString());
                                        myActivity.changeToCloseOffer(response.getString("idIntercanvi"));
                                    }
                                    catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            },
                            new Response.ErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.i("JSONerror: ","");
                                }
                            },
                            body,headers
                    );
                    //TODO:eliminar lo de arriba
                }
            }
        });
        adapterAPI.GETJsonArrayRequestAPI("/api/element/user/" + user.getId(),
                new Response.Listener<JSONArray>() {

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

        img1 = (ImageView) view.findViewById(R.id.item_image_1);
        img2 = (ImageView) view.findViewById(R.id.item_image_2);
        title1 = (TextView) view.findViewById(R.id.item_title_1);
        title2 = (TextView) view.findViewById(R.id.item_title_2);
        temporal1 = (TextView) view.findViewById(R.id.item_temporal_1);
        temporal2 = (TextView) view.findViewById(R.id.item_temporal_2);

        return view;
    }


    private void onClickElement (int i) {
        loadElement2(elements.get(i));
    }


    private void loadList (ArrayList<Element> e) {
        elements = new ArrayList<>();
        elements = e;
        ElementListAdapter adapter = new ElementListAdapter(myActivity,R.layout.item_default,elements);
        myListView.setAdapter(adapter);
        myListView.deferNotifyDataSetChanged();
    }

    private void loadElement1(Element e) {
        element1 = e;
        //TODO:img
        title1.setText(element1.getTitol());
        String aux;
        if (e.getTipusProducte()) aux = myActivity.getString(R.string.product_eng);
        else aux = myActivity.getString(R.string.experience_eng);
        if (e.getEsTemporal()) {
            temporal1.setText(aux + " - " + e.getTemporalitat());
        }
        else temporal1.setText(aux + " - " + myActivity.getString(R.string.permanent_eng));
    }

    private void loadElement2(Element e) {
        element2 = e;
        //TODO:img
        title2.setText(element2.getTitol());
        String aux;
        if (e.getTipusProducte()) aux = myActivity.getString(R.string.product_eng);
        else aux = myActivity.getString(R.string.experience_eng);
        if (e.getEsTemporal()) {
            temporal2.setText(aux + " - " + e.getTemporalitat());
        }
        else temporal2.setText(aux + " - " + myActivity.getString(R.string.permanent_eng));
    }


}
