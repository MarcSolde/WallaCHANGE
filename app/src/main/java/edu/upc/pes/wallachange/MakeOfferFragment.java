package edu.upc.pes.wallachange;


import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edu.upc.pes.wallachange.APILayer.AdapterAPIRequest;
import edu.upc.pes.wallachange.Adapters.MakeOfferAdapter;
import edu.upc.pes.wallachange.Adapters.SeeProfileAdapter;
import edu.upc.pes.wallachange.Models.CurrentUser;
import edu.upc.pes.wallachange.Models.Element;
import edu.upc.pes.wallachange.Models.User;


public class MakeOfferFragment extends Fragment {
    private MainActivity myActivity;

    private MakeOfferAdapter adapter;
    private ListView myListView;
    private View myView;

    private Element e1, e2;
    private ArrayList<Element> elements;
    private static AdapterAPIRequest adapterAPI = new AdapterAPIRequest();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_make_offer, container, false);
        myView = view;
        myActivity = (MainActivity) getActivity();
        myActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        myActivity.setTitle(R.string.navigationOffer_eng);

        CurrentUser user = CurrentUser.getInstance();
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("x-access-token",user.getToken());

        String id = getArguments().getString("id");

        //TODO: get element 1
        adapterAPI.GETRequestAPI("http://104.236.98.100:3000/api/element/"+id,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.i("JSON: ",response.toString());
                            Element aux = new Element(response.getString("_id"),
                                    response.getString("titol"),
                                    null,
                                    null,
                                    null,
                                    response.getJSONObject("es_temporal").getString("temporalitat"),
                                    null,
                                    null,
                                    null
                            );
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
                }, headers
        );

        myListView = (ListView) view.findViewById(R.id.see_your_list);
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                onClickElement(i);
            }
        });

        adapterAPI.GETRequestAPI("http://104.236.98.100:3000/api/elements/"+user.getId(),
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            Log.i("JSON: ",response.toString());
                            ArrayList<Element> aux = new ArrayList<> ();
                            for (int i = 0;i < response.length();++i) {
                                JSONObject var = response.getJSONObject(i);
                                Element aux2 = new Element(var.getString("_id"),
                                        var.getString("titol"),
                                        null,
                                        null,
                                        null,
                                        var.getJSONObject("es_temporal").getString("temporalitat"),
                                        null,
                                        null,
                                        null
                                );
                            }
                            loadElements(aux);
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

        elements = new ArrayList<>();
        elements.add(new Element("1","aaa1",null,"aaa2",null,"aaa3",null,null,null));
        elements.add(new Element("2","bbb1",null,"bbb2",null,"bbb3",null,null,null));
        elements.add(new Element("3","ccc1",null,"ccc2",null,"ccc3",null,null,null));
        adapter = new MakeOfferAdapter(myActivity,R.layout.item_see_profile,elements,this);
        myListView.setAdapter(adapter);
        myListView.deferNotifyDataSetChanged();

        return view;
    }


    private void onClickElement (int i) {
        loadElement2(elements.get(i));
    }


    private void loadElements (ArrayList<Element> e) {

    }

    private void loadElement1(Element e) {

    }

    private void loadElement2(Element e) {

    }


}
