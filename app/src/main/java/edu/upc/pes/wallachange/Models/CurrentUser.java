package edu.upc.pes.wallachange.Models;

import static com.android.volley.VolleyLog.TAG;

import android.net.Uri;

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

/**
 * Created by carlota on 28/4/17.
 */

public class CurrentUser {

    private String token;
    private User user;
    private static AdapterAPIRequest adapter = new AdapterAPIRequest();


    private static final CurrentUser ourInstance = new CurrentUser();

    public static CurrentUser getInstance() {
        return ourInstance;
    }

    private CurrentUser() {
        user = new User();
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return user.getUsername();
    }

    public void setUsername(String username) {
        user.setUsername(username);
    }

    public String getLocation() {
        return user.getLocation();
    }

    public void setLocation(String location) {
        user.setLocation(location);
    }

    public float getRating() {
        return user.getRating();
    }

    public void setRating(String rating) {

        Float f = Float.parseFloat(rating);
        user.setRating(f);
    }

    public Uri getPicture() {
        return user.getPicture();
    }

    public void setPicture(String picture) {
        Uri uri = Uri.parse(picture);
        user.setPicture(uri);
    }

    public ArrayList<String> getPreferences() {
        return user.getPreferences();
    }


    public static CurrentUser getOurInstance() {
        return ourInstance;
    }

    public void setPreferencesArray(JSONArray newPreferences) {
        ArrayList<String> list = new ArrayList<String>();
        JSONArray jsonArray = (JSONArray) newPreferences;
        if (jsonArray != null) {
            int len = jsonArray.length();
            for (int i = 0; i < len; i++) {
                try {
                    list.add(jsonArray.get(i).toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        user.setPreferencesArray(list);
    }

    public void setPreferencesArrayList(ArrayList<String> newPreferences) {
        user.setPreferencesArray(newPreferences);
    }

    public void addPreference(String preference) {
        user.addPreference(preference);
    }

    public void deletePreference(String prefToDelete) {
        user.deletePreference(prefToDelete);

    }

    public boolean existsPref(String newPref) {
        return user.existsPref(newPref);
    }


    public void setIntercanvisArray(JSONArray intercanvisArray) {
        ArrayList<String> list = new ArrayList<String>();
        JSONArray jsonArray = (JSONArray) intercanvisArray;
        if (jsonArray != null) {
            int len = jsonArray.length();
            for (int i = 0; i < len; i++) {
                try {
                    list.add(jsonArray.get(i).toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        user.setIntercanvis(list);
    }

    public void setProductesArray(JSONArray productesArray) {
        ArrayList<String> list = new ArrayList<String>();
        JSONArray jsonArray = (JSONArray) productesArray;
        if (jsonArray != null) {
            int len = jsonArray.length();
            for (int i = 0; i < len; i++) {
                try {
                    list.add(jsonArray.get(i).toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        user.setProductes(list);
    }

    public void updateFields() {
        String location = user.getLocation();
        ArrayList<String> prefs = user.getPreferences();
//        String[] ja = new String[prefs.size()];
        JSONArray ja = new JSONArray();
        for (String p : prefs) {
            ja.put(p);
        }
        Map<String, String> body = new HashMap<String, String>();
        Map<String, String> headers = new HashMap<String, String>();
        body.put("token", this.getToken());
        body.put("localitat", location);
//        body.put("preferencies", ja); ???
//        body.put("preferencies", ja); ??COM HO FAIG?


        adapter.PUTStringRequestAPI("http://10.0.2.2:3000/updateUser/"+user.getUsername(),
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
}