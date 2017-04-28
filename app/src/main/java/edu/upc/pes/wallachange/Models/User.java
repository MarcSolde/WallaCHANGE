package edu.upc.pes.wallachange.Models;

import android.net.Uri;

import java.util.ArrayList;



public class User {

    private String username;
    private String location;
    private String password;
    private float rating;
    private Uri picture;
    private ArrayList<String> preferences;

    public User(String username, String location, String password, int rating, Uri picture, ArrayList<String> preferences) {
        this.username = username;
        this.location = location;
        this.password = password;
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

//    public String getPassword() {
//        return password;
//    }

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

//    public void setPassword(String password) {
//        this.password = password;
//    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setPicture(Uri picture) {
        this.picture = picture;
    }

    public void setPreferencesArray(ArrayList<String> newPreferences) {
            preferences.clear();
            preferences.addAll(newPreferences);
    }

    public void addPreference(String preference) {
        preferences.add(preference);
       // preferences.add("hola");
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

}
