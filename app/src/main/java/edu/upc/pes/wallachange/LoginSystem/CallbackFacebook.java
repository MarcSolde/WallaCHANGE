package edu.upc.pes.wallachange.LoginSystem;

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
    private static LoginActivity myActivity;
    private static AdapterAPIRequest adapter = new AdapterAPIRequest();


    public CallbackFacebook(LoginActivity context) {
        myActivity = context;
    }

    @Override
    public void onSuccess(LoginResult loginResult) {
        Map<String, String> params = new HashMap<String, String>();
        Map<String, String> headers = new HashMap<String,String>();
        AccessToken accessToken = loginResult.getAccessToken();
        params.put("token", accessToken.getToken());
        params.put("id", accessToken.getUserId());
        headers.put("Content-Type", "application/json");
        adapter.POSTSJsonObjectRequestAPI("http://10.0.2.2:3000/loginFB",
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject js = response;
                        try {
                            CurrentUser user = CurrentUser.getInstance();
                            user.setToken(js.getString("token"));
//                            user.setUsername(js.getString("nom"));
//                            user.setLocation(js.getString("localitat"));
//                            user.setPreferencesArray(js.getJSONArray("prefs"));
//                                user.setIntercanvisArray(js.getJSONArray("intercanvis"));
//                                user.setProductesArray(js.getJSONArray("productes"));
//                                user.setRating(js.getString("reputacio"));
//                                user.setPicture(js.getString("path"));
                            myActivity.login();



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

    @Override
    public void onCancel() {
        //TODO:
        Toast.makeText(myActivity, "login cancelled", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onError(FacebookException exception) {
        //TODO:
        Toast.makeText(myActivity, "Error login", Toast.LENGTH_LONG).show();
    }

    public static void checkLogin() {
        //FacebookSdk.sdkInitialize(myActivity);

        AccessToken accessToken = AccessToken.getCurrentAccessToken();

        if (accessToken != null){
            Map<String, String> params = new HashMap<String, String>();
            Map<String, String> headers = new HashMap<String,String>();
            params.put("token", accessToken.toString());
            params.put("id", accessToken.getUserId());
            headers.put("Content-Type", "application/json");
            adapter.POSTSJsonObjectRequestAPI("http://10.0.2.2:3000/loginFB",
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            JSONObject js = response;
                            try {
                                CurrentUser user = CurrentUser.getInstance();
                                user.setToken(js.getString("token"));
                                //user.setUsername(js.getString("nom"));
//                                user.setLocation(js.getString("localitat"));
//                                user.setPreferencesArray(js.getJSONArray("prefs"));
//                                user.setIntercanvisArray(js.getJSONArray("intercanvis"));
//                                user.setProductesArray(js.getJSONArray("productes"));
//                                user.setRating(js.getString("reputacio"));
//                                user.setPicture(js.getString("path"));
                                myActivity.login();
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
        }
    }
}
