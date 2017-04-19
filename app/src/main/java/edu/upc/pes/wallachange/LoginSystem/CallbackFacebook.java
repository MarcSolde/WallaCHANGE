package edu.upc.pes.wallachange.LoginSystem;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;

import org.json.JSONException;
import org.json.JSONObject;

import edu.upc.pes.wallachange.LoginActivity;



public class CallbackFacebook implements FacebookCallback<LoginResult> {
    private LoginActivity myActivity;

    public CallbackFacebook(LoginActivity context) {
        myActivity = context;
    }

    @Override
    public void onSuccess(LoginResult loginResult) {
        //TODO:revisar getUserId devuelve nombre o id
        //String name = accessToken.getUserId();
        //token
        //fbLogin(loginResult.getAccessToken());
        String Token = loginResult.getAccessToken().getToken();

        GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.v("LoginActivity", response.toString());

                        // Application code
                        try{
                            String email = object.getString("email");
                            String birthday = object.getString("birthday");
                            String name = object.getString("name");
                            String gender = object.getString("gender");
                            String id = object.getString("id");
                            myActivity.login(id, name);
                        }catch (JSONException e){
                            e.printStackTrace();
                        }

                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,gender,birthday");
        request.setParameters(parameters);
        request.executeAsync();
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
}
