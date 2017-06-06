package edu.upc.pes.wallachange.LoginSystem;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.facebook.AccessToken;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import edu.upc.pes.wallachange.APILayer.AdapterAPIRequest;
import edu.upc.pes.wallachange.LoginActivity;
import edu.upc.pes.wallachange.Models.CurrentUser;

import static com.android.volley.VolleyLog.TAG;


public class CallbackFacebook implements FacebookCallback<LoginResult> {
    final public static String MyPREFERENCES = "MyPrefs";
    final public static String MyTokenPref = "MyFBToken";
    final public static String MyFBidPref = "MyFBid";

    private static LoginActivity myActivity;
    private static AdapterAPIRequest adapter = new AdapterAPIRequest();
    private static SharedPreferences sharedPreferences;


    public CallbackFacebook(LoginActivity context) {
        myActivity = context;
        sharedPreferences = myActivity.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
    }

    @Override
    public void onSuccess(LoginResult loginResult) {
        JSONObject params = new JSONObject();
        Map<String, String> headers = new HashMap<String,String>();
        SharedPreferences.Editor editor  = sharedPreferences.edit();
        AccessToken accessToken = loginResult.getAccessToken();

        String id = accessToken.getUserId();
        String token = accessToken.getToken();
        try {
            params.put("token", token);
            params.put("id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        editor.putString(MyTokenPref, token);
        editor.putString(MyFBidPref, id);

        editor.commit();

        headers.put("Content-Type", "application/json");

        adapter.POSTRequestAPI("/loginFB",

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            setCurrentUser(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                        myActivity.logOut();
                    }
                }, params, headers);

//        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new CallbackGraphJSONObject(myActivity));
//        Bundle parameters = new Bundle();
//        parameters.putString("fields", "id,name,email,gender,birthday");
//        request.setParameters(parameters);
//        request.executeAsync();
    }

    private static void setCurrentUser(JSONObject response) throws JSONException {
        CurrentUser user = CurrentUser.getInstance();
        user.setToken(response.getString("token"));
        user.setId(response.getString("facebookId"));
        user.setUsername(response.getString("nom"));
//      user.setLocation(js.getString("localitat"));
        user.setPreferencesArray(response.getJSONArray("preferencies"));
        user.setIntercanvisArray(response.getJSONArray("intercanvis"));
        user.setProductesArray(response.getJSONArray("productes"));
        user.setRating(response.getString("reputacio"));
//      user.setPicture(js.getString("path"));
        myActivity.login();
    }

    @Override
    public void onCancel() {
        //TODO:
        Toast.makeText(myActivity, "login cancelled", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onError(FacebookException exception) {
        //TODO:
        Toast.makeText(myActivity, "Error login", Toast.LENGTH_LONG).show();
        Log.e("CAACA", "exceptionKAKA", exception);
    }

    public static void checkLogin() throws JSONException {
        //FacebookSdk.sdkInitialize(myActivity);

        //AccessToken accessToken = AccessToken.getCurrentAccessToken();

        String token = sharedPreferences.getString(MyTokenPref, null);
        String id = sharedPreferences.getString(MyFBidPref, null);

        if (token != null && id != null){
            JSONObject params = new JSONObject();
            Map<String, String> headers = new HashMap<String,String>();
            params.put("token", token);
            params.put("id", id);
            headers.put("Content-Type", "application/json");


            adapter.POSTRequestAPI("/loginFB",

                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            try {
                                setCurrentUser(response);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            VolleyLog.d(TAG, "Error: " + error.getMessage());
                            myActivity.logOut();
                        }
                    }, params, headers);
        } else {
            VolleyLog.d(TAG, "Error login");
            myActivity.logOut();
        }
    }
}