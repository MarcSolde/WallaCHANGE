package edu.upc.pes.wallachange.Models;

import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;



public class User {

    private String username;
    private String location;
    private float rating;
    private Uri picture;
    private ArrayList<String> preferences;
    private ArrayList<String> intercanvis;
    private ArrayList<String> productes;
    private String facebookId;

    public User(String username, String location, String password, int rating, Uri picture, ArrayList<String> preferences) {
        this.username = username;
        this.location = location;
        this.rating = rating;
        this.picture = picture;
        this.preferences = preferences;
    }

    public User() {
        preferences = new ArrayList<String>();
    }

    public String getUsername() {
        return username;
    }

    public String getLocation() {
        return location;
    }

    public float getRating() {
        return rating;
    }

    public Uri getPicture() {
        return picture;
    }

    public ArrayList<String> getPreferences() {
        return preferences;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public void setPicture(Uri picture) {
        this.picture = picture;
    }

    public void setPreferencesArray(ArrayList<String> newPreferences) {
        preferences.clear();
        preferences.addAll(newPreferences);
    }

    public void addPreference(String pref) {
        preferences.add(pref);
    }

    public void deletePreference(String prefToDelete) {
        for (int i = 0; i < preferences.size(); ++i) {
            if (preferences.get(i).equals(prefToDelete)) {
                preferences.remove(i);
            }
        }

    }

    public boolean existsPref(String newPref) {
        for (int i = 0; i < preferences.size(); ++i) {
            if (preferences.get(i).equals(newPref)) {
                return true;
            }
        }
        return false;
    }
    public void setRatingString(String rating) {
        Float f = Float.parseFloat(rating);
        this.rating = f;
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
        this.intercanvis= list;
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
        this.preferences = list;
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
        this.productes = list;
    }

    public void setFacebookId(String fbId) {
        this.facebookId = fbId;
    }

    public void setIntercanvis(ArrayList<String> intercanvis) {
        this.intercanvis = intercanvis;
    }

    public void setProductes(ArrayList<String> productes) {
        this.productes = productes;
    }

}
