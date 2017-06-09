package edu.upc.pes.wallachange;


import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
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
import edu.upc.pes.wallachange.Models.CurrentUser;
import edu.upc.pes.wallachange.Models.Element;
import edu.upc.pes.wallachange.Models.User;

import static com.android.volley.VolleyLog.TAG;


public class CloseOfferFragment extends Fragment {
    private MainActivity myActivity;
    private View myView;

    private static AdapterAPIRequest adapterAPI = new AdapterAPIRequest();

    private String id_offer, id_user1, id_user2,id_element1, id_element2;
    private boolean ableToClose;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_close_offer, container, false);
        myActivity = (MainActivity) getActivity();
        myActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        myActivity.setTitle(R.string.navigationClose_eng);
        myView = view;

        String id = getArguments().getString("id");
        ableToClose = false;

        CurrentUser user = CurrentUser.getInstance();
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("x-access-token",user.getToken());
        adapterAPI.GETRequestAPI("/intercanvi/"+id,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            CurrentUser user = CurrentUser.getInstance();
                            Log.i("JSON: ",response.toString());
                            //TODO: bool puede petar hasta cambio backend
                            if (response.getString("id1").equals(user.getId())) {
                                //loadOffer(response.getString("idIntercanvi"),response.getString("id2"),response.getString("idProd2"),
                                //        response.getString("id1"),response.getString("idProd1"),response.getBoolean("confirmat2"));
                                loadOffer(response.getString("idIntercanvi"),response.getString("id2"),response.getString("idProd2"),
                                        response.getString("id1"),response.getString("idProd1"),true);
                            }
                            else {
                                //loadOffer(response.getString("idIntercanvi"),response.getString("id1"),response.getString("idProd1"),
                                //        response.getString("id2"),response.getString("idProd2"),response.getBoolean("confirmat1"));
                                loadOffer(response.getString("idIntercanvi"),response.getString("id1"),response.getString("idProd1"),
                                        response.getString("id2"),response.getString("idProd2"),true);
                            }
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

        Button myButton = (Button) view.findViewById(R.id.close_trade_button);
        myButton.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                JSONObject body = new JSONObject();
                try {
                    RatingBar aux = (RatingBar) myView.findViewById(R.id.close_trade_ratingBar);
                    body.put("reputacio", aux.getRating()*20);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                CurrentUser user = CurrentUser.getInstance();
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("x-access-token",user.getToken());
                adapterAPI.PUTRequestAPI("/user/"+id_user1,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                JSONObject js = response;
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                VolleyLog.d(TAG, "Error: " + error.getMessage());
                            }
                        }, body, headers);

                //TODO:close
                if (ableToClose) {
                    Toast.makeText(myActivity,"BORRARRRRRRR",Toast.LENGTH_SHORT).show();
                }
                //TODO:changeTo
            }
        });


        return view;
    }

    private void loadOffer (String id_offer, String id_user1, String id_element1, String id_user2, String id_element2, boolean ableToClose) {
        this.id_offer = id_offer;
        this.id_user1 = id_user1;
        this.id_user2 = id_user2;
        this.id_element2 = id_element2;
        this.id_element1 = id_element1;
        this.ableToClose = ableToClose;

        CurrentUser user = CurrentUser.getInstance();
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("x-access-token",user.getToken());
        adapterAPI.GETRequestAPI("/user/"+id_user1,
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

        adapterAPI.GETRequestAPI("/api/element/"+id_element1,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.i("JSON: ",response.toString());
                            Element aux = new Element(response);
                            loadElement(aux);
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
    }

    private void loadUser (User e) {
        //TODo:img
        TextView aux = (TextView) myView.findViewById(R.id.close_trade_title_1);
        aux.setText(e.getUsername());
        RatingBar aux2 = (RatingBar) myView.findViewById(R.id.close_trade_ratingBar);
        aux2.setRating(e.getRating()/20);
    }

    private void loadElement (Element e) {
        //TODo:img
        TextView aux = (TextView) myView.findViewById(R.id.close_trade_title_2);
        aux.setText(e.getTitol());
    }

}
