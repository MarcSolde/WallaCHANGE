package edu.upc.pes.wallachange.LoginSystem;

import android.os.Bundle;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.login.LoginResult;

import edu.upc.pes.wallachange.LoginActivity;

/**
 * Created by sejo on 31/03/17.
 */

public class CallbackFacebook implements FacebookCallback<LoginResult> {
    private static LoginActivity myActivity;

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

        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new CallbackGraphJSONObject(myActivity));
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

    public static void checkLogin() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        //myActivity.login(accessToken.getUserId(), accessToken.getToken());
        if (accessToken != null){
            GraphRequest request = GraphRequest.newMeRequest(accessToken, new CallbackGraphJSONObject(myActivity));
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email,gender,birthday");
            request.setParameters(parameters);
            request.executeAsync();
        }
    }
}
