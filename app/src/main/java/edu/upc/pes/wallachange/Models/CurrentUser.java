package edu.upc.pes.wallachange.Models;

import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by carlota on 28/4/17.
 */

public class CurrentUser {

    private String token;
    private User user;

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
            for (int i=0;i<len;i++){
                try {
                    list.add(jsonArray.get(i).toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        user.setPreferencesArray(list);
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
        JSONArray jsonArray = (JSONArray)intercanvisArray;
        if (jsonArray != null) {
            int len = jsonArray.length();
            for (int i=0;i<len;i++){
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
            for (int i=0;i<len;i++){
                try {
                    list.add(jsonArray.get(i).toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        user.setProductes(list);
    }
}
