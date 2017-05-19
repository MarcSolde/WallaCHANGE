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
import android.widget.RatingBar;
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
import edu.upc.pes.wallachange.Adapters.SeeProfileAdapter;
import edu.upc.pes.wallachange.Models.Element;
import edu.upc.pes.wallachange.Models.User;


public class SeeProfileFragment extends Fragment {
    private MainActivity myActivity;

    private SeeProfileAdapter adapter;
    private ListView myListView;
    private View myView;

    private User user;
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
        //TODO: enlace DB
        Map<String, String> headers = new HashMap<>();
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
                            User u = new User(response.getString("nom_user"),
                                    response.getString("nom"),
                                    response.getString("localitat"),
                                    null,
                                    Float.parseFloat(response.getString("reputacio")),
                                    Uri.parse(response.getString("path")),
                                    aux2);
                            int a = 0;
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

        loadList();

        return view;
    }

    private void loadUser (User user) {
        this.user = user;
        TextView aux = (TextView) myView.findViewById(R.id.see_user_username);
        aux.setText(user.getUsername());
        aux = (TextView) myView.findViewById(R.id.see_user_city);
        aux.setText(user.getLocation());
        aux = (TextView) myView.findViewById(R.id.see_user_preference);
        String aux2 = TextUtils.join(", ", user.getPreferences());
        aux.setText(aux2);
        RatingBar aux3 = (RatingBar)myView.findViewById(R.id.see_user_rating);
        aux3.setRating(user.getRating()/20);
    }

    private void loadList () {
        //TODO:
        elements = new ArrayList<>();
        elements.add(new Element("1","aaa1",null,"aaa2",null,"aaa3",null,null,null));
        elements.add(new Element("2","bbb1",null,"bbb2",null,"bbb3",null,null,null));
        elements.add(new Element("3","ccc1",null,"ccc2",null,"ccc3",null,null,null));
        adapter = new SeeProfileAdapter(myActivity,R.layout.item_see_profile,elements,this);
        myListView.setAdapter(adapter);
        myListView.deferNotifyDataSetChanged();
    }

    private void onClickElement (int i) {
        myActivity.changeToItem(elements.get(i));
    }
}
