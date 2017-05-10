package edu.upc.pes.wallachange.LoginSystem;

import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.facebook.AccessToken;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
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
    private AdapterAPIRequest adapter = new AdapterAPIRequest();


    public CallbackFacebook(LoginActivity context) {
        myActivity = context;
    }

    @Override
    public void onSuccess(LoginResult loginResult) {
        //TODO:revisar getUserId devuelve nombre o id
        //String name = accessToken.getUserId();
        //token
        //fbLogin(loginResult.getAccessToken());
        //String Token = loginResult.getAccessToken().getToken();
        Map<String, String> params = new HashMap<String, String>();
        Map<String, String> headers = new HashMap<String,String>();
        params.put("token", loginResult.getAccessToken().getToken());
        params.put("id", loginResult.getAccessToken().getUserId());
        headers.put("Content-Type", "application/json");
        adapter.POSTSJsonObjectRequestAPI("http://10.0.2.2:3000/loginFB",
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject js = response;
                        try {
                            CurrentUser user = CurrentUser.getInstance();
                            user.setToken(js.getString("token"));
                            user.setUsername(js.getString("nom"));
                            user.setLocation(js.getString("localitat"));
                            user.setPreferencesArray(js.getJSONArray("prefs"));
                            user.setIntercanvisArray(js.getJSONArray("intercanvis"));
                            user.setProductesArray(js.getJSONArray("productes"));
                            user.setRating(js.getString("reputacio"));
                            user.setPicture(js.getString("path"));



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                        LoginManager.getInstance().logOut();
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
        AccessToken accessToken = AccessToken.getCurrentAccessToken();


        myActivity.login(accessToken.getUserId(), accessToken.getToken());
//        if (accessToken != null){
//            GraphRequest request = GraphRequest.newMeRequest(accessToken, new CallbackGraphJSONObject(myActivity));
//            Bundle parameters = new Bundle();
//            parameters.putString("fields", "id,name,email,gender,birthday");
//            request.setParameters(parameters);
//            request.executeAsync();
//        }
    }
}
