package edu.upc.pes.wallachange.Models;

import android.net.Uri;

import org.json.JSONArray;

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

        //TODO string->float
        user.setRating(rating);
    }

    public Uri getPicture() {
        return user.getPicture();
    }

    public void setPicture(String picture) {

        //TODO string->uri
        user.setPicture(picture);
    }

    public ArrayList<String> getPreferences() {
        return user.getPreferences();
    }



    public static CurrentUser getOurInstance() {
        return ourInstance;
    }

    public void setPreferencesArray(ArrayList<String> newPreferences) {
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
        //TODO
        this.intercanvisArray = intercanvisArray;
    }

    public void setProductesArray(JSONArray productesArray) {
        //TODO
        this.productesArray = productesArray;
    }
}
