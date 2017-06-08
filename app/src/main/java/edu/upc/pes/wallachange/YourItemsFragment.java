package edu.upc.pes.wallachange;


import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
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

public class YourItemsFragment extends Fragment {
    private MainActivity myActivity;

    private ListView myListView;
    private ArrayList<Element> elements;

    private static AdapterAPIRequest adapterAPI = new AdapterAPIRequest();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_your_items, container, false);
        myActivity = (MainActivity) getActivity();
        myActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        myActivity.setTitle(R.string.navigationYourItems_eng);

        myListView = (ListView) view.findViewById(R.id.see_your_items_list);
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                onClickElement(i);
            }
        });

        CurrentUser user = CurrentUser.getInstance();
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("x-access-token",user.getToken());
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
        return view;
    }

    private void onClickElement (int i) {
        myActivity.changeToItem(elements.get(i).getId());
    }

    private void loadList (ArrayList<Element> e) {
        if (e.isEmpty()) {
            Toast.makeText(myActivity,R.string.search_no_result,Toast.LENGTH_SHORT).show();
        }
        elements = new ArrayList<>();
        elements = e;
        ElementListAdapter adapter = new ElementListAdapter(myActivity,R.layout.item_default,elements);
        myListView.setAdapter(adapter);
        myListView.deferNotifyDataSetChanged();
    }

}
