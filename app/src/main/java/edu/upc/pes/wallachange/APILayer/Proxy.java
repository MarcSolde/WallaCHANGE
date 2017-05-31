/*package edu.upc.pes.wallachange.APILayer;

import static com.android.volley.VolleyLog.TAG;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edu.upc.pes.wallachange.Models.CurrentUser;
import edu.upc.pes.wallachange.Models.Element;
import edu.upc.pes.wallachange.Models.User;

/**
 * Created by carlota on 17/5/17.


public class Proxy {

    private static AdapterAPIRequest adapter = new AdapterAPIRequest();


    public User getInfoUser(String username) {
        final User newUser = new User();
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        adapter.GETRequestAPI("http://10.0.2.2:3000/userser/"+username,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject jo = response;
                        try {
                            if (jo != null) {
                                newUser.setLocation(jo.getString("localitat"));
                                newUser.setPreferencesArray(jo.getJSONArray("preferencies"));
                                newUser.setProductesArray(jo.getJSONArray("productes"));
                                newUser.setIntercanvisArray(jo.getJSONArray("intercanvis"));
                                newUser.setRatingString(jo.getString("reputacio"));
                            } }

                        catch (JSONException e) {
                                e.printStackTrace();


                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                    }
                }, headers);

        return newUser;
    }
    public void updateUser(CurrentUser user) throws JSONException {

        String token = user.getToken();
        String location = user.getLocation();
        ArrayList<String> prefs = user.getPreferences();

        JSONArray ja = new JSONArray();
        for (String p : prefs) {
            ja.put(p);
        }

        JSONObject body = new JSONObject();
        body.put("token", token);
        body.put("localitat", location);
        body.put("preferencies", ja);

        Map<String,String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        adapter.PUTRequestAPI("http://10.0.2.2:3000/updateUser/"+user.getUsername(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject js = response;
                        //SI voleu aquí comprovar que s'ha fet bé
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                    }
                }, body, headers);

    }

    public ArrayList<Element> getElements(String title) throws JSONException {

        JSONObject body = new JSONObject();
        Map<String, String> headers = new HashMap<>();
        CurrentUser us = CurrentUser.getInstance();
        headers.put("token", us.getToken());
        headers.put("titol", title);
        headers.put("Content-Type", "application/json");

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
        return elements2;
    }
}
*/