package edu.upc.pes.wallachange;


import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

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
import edu.upc.pes.wallachange.Models.User;


public class SeeProfileFragment extends Fragment {
    private MainActivity myActivity;

    private ListView myListView;
    private View myView;

    private ArrayList<Element> elements;
    private static AdapterAPIRequest adapterAPI = new AdapterAPIRequest();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_see_profile, container, false);
        myView = view;
        myActivity = (MainActivity) getActivity();
        myActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        myActivity.setTitle(R.string.navigationProfile_eng);

        String id = getArguments().getString("id");

        Map<String, String> headers = new HashMap<>();
        //adapterAPI.GETRequestAPI("http://104.236.98.100:3000/user/"+id,
        adapterAPI.GETRequestAPI("http://10.0.2.2:3000/user/"+id,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.i("JSON: ",response.toString());
                            JSONArray var2 = response.getJSONArray("preferencies");
                            ArrayList<String> aux2 = new ArrayList<> ();
                            for (int j = 0; j < var2.length();++j) {
                                aux2.add(var2.get(j).toString());
                            }
                            User u = new User(response.getString("id"),
                                    response.getString("nom"),
                                    response.getString("localitat"),
                                    null,
                                    Float.parseFloat(response.getString("reputacio")),
                                    Uri.parse(response.getString("path")),
                                    aux2);
                            loadUser(u);
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

        myListView = (ListView) view.findViewById(R.id.see_user_list);
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                onClickElement(i);
            }
        });

        CurrentUser user = CurrentUser.getInstance();
        headers.put("Content-Type", "application/json");
        headers.put("x-access-token",user.getToken());
        adapterAPI.GETJsonArrayRequestAPI("http://10.0.2.2:3000/api/elements/"+"JordiFructos",
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
                headers,null
        );

        return view;
    }

    private void loadUser (User u) {
        //this.user = user;
        TextView aux = (TextView) myView.findViewById(R.id.see_user_username);
        aux.setText(u.getUsername());
        aux = (TextView) myView.findViewById(R.id.see_user_city);
        aux.setText(u.getLocation());
        aux = (TextView) myView.findViewById(R.id.see_user_preference);
        String aux2 = TextUtils.join(", ", u.getPreferences());
        aux.setText(aux2);
        aux = (TextView) myView.findViewById(R.id.see_user_rating);
        aux.setText(u.getRating()/20 +"/5.0");
    }

    private void loadList (ArrayList<Element> e) {
        elements = new ArrayList<>();
        elements = e;
        ElementListAdapter adapter = new ElementListAdapter(myActivity,R.layout.item_default,elements);
        myListView.setAdapter(adapter);
        myListView.deferNotifyDataSetChanged();
    }

    private void onClickElement (int i) {
        //TODO:
        //myActivity.changeToItem(elements.get(i).getId());
        myActivity.changeToMakeOffer(elements.get(i).getId());
    }
}
