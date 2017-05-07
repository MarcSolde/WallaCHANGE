package edu.upc.pes.wallachange.APILayer;

import static com.android.volley.VolleyLog.TAG;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;

import com.facebook.login.LoginResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edu.upc.pes.wallachange.LoginActivity;
import edu.upc.pes.wallachange.Models.CurrentUser;

/**
 * Created by carlota on 7/5/17.
 */

public class AdapterProva {
    private static LoginActivity myActivity;
    private AdapterAPIRequest adapter = new AdapterAPIRequest();


//    GET http://localhost:3000/user/”nom_user”
//    body: Buit (obviament)
//    nom_user: Ha de ser el nom de usuari
//	return: JSON de l’usuari
//obtenir info d'un user partint del seu nom

    public void getInfoUser(String username) {

        final String[] loc = new String[1];
        final JSONArray[] prefs = {new JSONArray()};
        final JSONArray[] prods = {new JSONArray()};
        final JSONArray[] inter = {new JSONArray()};
        final String[] rate = new String[1];
        final String[] fbid = new String[1];
        final String[] salt = new String[1];
        final String[] pw_hash = new String[1];
        final String[] path = new String[1];
        //twitter com?


//        headers.put("Content-Type", "application/json");
        adapter.GETJsonObjectRequestAPI("http://localhost:3000/" + username,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject js = response;
                        try {
                            loc[0] = js.getString("localitat");
                            prefs[0] = js.getJSONArray("preferencies");
                            prods[0] = js.getJSONArray("productes");
                            inter[0] = js.getJSONArray("intercanvis");
                            rate[0] = js.getString("reputacio");
                            fbid[0] = js.getString("facebookId");
                            salt[0] = js.getString("salt");
                            path[0] = js.getString("path");
                            pw_hash[0] = js.getString("password_hash");


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                    }
                });

    }

    public void getAllUsers() {
        adapter.GETJsonArrayRequestAPI("http://localhost:3000/allUsers",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        JSONArray ja = response;
//                        try {
//                            //tractament de la llista
//                            ja = response;
//                        }
//                        catch (JSONException e) {
//                            e.printStackTrace();
//                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                    }
                });
    }
//delete user
    public void deleteUser(String username){

        Map<String, String> body = new HashMap<String,String>();
        CurrentUser user = CurrentUser.getInstance();
        String tok = user.getToken();
        body.put("token", tok);


        adapter.DELETERequestAPI("http://localhost:3000/deleteUser/" +username,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        JSONArray ja = response;
//                       //resposta buida!!
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                    }
                });//hauria de tenir aixo: , body);
    }
//actualitzar la info d'un usuari
    public void updateUser(String username, String loc, ArrayList<String> prefs, String rate) {

        Map<String, String> body = new HashMap<String,String>();
        CurrentUser user = CurrentUser.getInstance();
        String tok = user.getToken();
        body.put("token", tok);
        body.put("localitat", loc);
//        body.put("preferencies", prefs); COM s'INTRODUEIX UN ARRAY EN UN JSON?
        body.put("reputacio", rate);

        //PUT hauria de ser JSONRequest..
        adapter.PUTStringRequestAPI("http://localhost:3000/updateUser" + username,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject js = response;
                        try {
                            CurrentUser user = CurrentUser.getInstance();
                            user.setLocation(js.getString("localitat"));
//                            user.setPreferencesArray(js.getJSONArray("preferencies"));
                            user.setRating(Float.parseFloat(js.getString("reputacio")));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                    }
                });
//                 }, body);

    }
}