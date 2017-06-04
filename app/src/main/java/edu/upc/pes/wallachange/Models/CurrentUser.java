package edu.upc.pes.wallachange.Models;

import static com.android.volley.VolleyLog.TAG;

import android.graphics.Bitmap;
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

    public void setId(String id) {
        user.setId(id);
    }
    
    public String getId () {
        return user.getId();
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

    public void setRatingString(String rating) {
        user.setRatingString(rating);
    }

    public Uri getPicture() {
        return user.getPicture();
    }

    public void setPicture(Uri picture) {
        user.setPicture(picture);
    }

    public void setPictureBitmap(Bitmap picture) {
        user.setPictureBitmap(picture);
    }

    public Bitmap getPictureBitmap() {
        return user.getPictureBitmap();
    }

    public ArrayList<String> getPreferences() {
        return user.getPreferences();
    }


    public static CurrentUser getOurInstance() {
        return ourInstance;
    }

    public void setPreferencesArray(JSONArray newPreferences) {
        user.setPreferencesArray(newPreferences);
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
        user.setIntercanvisArray(intercanvisArray);
    }

    public void setPreference(String pref) {
        user.setPreference(pref);
    }

    public void setProductesArray(JSONArray productesArray) {
        user.setProductesArray(productesArray);
    }
    public void setFacebookId(String fbId) {
        user.setFacebookId(fbId);
    }

}