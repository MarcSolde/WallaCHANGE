package edu.upc.pes.wallachange;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import java.util.Arrays;
import java.util.Locale;

import edu.upc.pes.wallachange.LoginSystem.CallbackFacebook;
import edu.upc.pes.wallachange.LoginSystem.CallbackTwitter;
import io.fabric.sdk.android.Fabric;

public class LoginActivity extends AppCompatActivity {
    private TwitterLoginButton twLoginButton;
    private LoginButton fbLoginButton;
    CallbackManager callbackManager;
    private String lenguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(getResources().getString(R.string.twitterKey), getResources().getString(R.string.twitterSecret));
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.activity_login);

        SharedPreferences preferences = this.getPreferences(Context.MODE_PRIVATE);
        final String prefLenguage = preferences.getString("language","empty");
        if (prefLenguage.equals("empty")) {
            String lenguage = Locale.getDefault().getDisplayLanguage();
            SharedPreferences.Editor editor = preferences.edit();
            if (lenguage.equals("Español")) {
                lenguage = getString(R.string.lenguage_esp);
                editor.putString("language",lenguage);
            }
            else  {
                lenguage = getString(R.string.lenguage_eng);
                editor.putString("language",lenguage);
            }
            editor.apply();
        }
        else lenguage = prefLenguage;

        twLoginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        twLoginButton.setCallback(new CallbackTwitter(this));

        //FacebookSdk.sdkInitialize(getApplicationContext());

        fbLoginButton = (LoginButton) findViewById(R.id.fb_login_button);
        fbLoginButton.setReadPermissions(Arrays.asList(
                    "public_profile", "email", "user_birthday", "user_friends"));
        callbackManager = CallbackManager.Factory.create();

        fbLoginButton.registerCallback(callbackManager, new CallbackFacebook(this));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Make sure that the loginButton hears the result from any
        // Activity that it triggered.
        twLoginButton.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    /*private void fbLogin(AccessToken accessToken){
        new GraphRequest(
                accessToken,
                "/{user-id}",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        // handle the result
                        try {
                            Log.d("JSONResponse",response.getJSONArray().toString());
                            login(response.getJSONObject().getJSONObject("data").getString("name"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        ).executeAsync();
    }*/

    public void login (String id, String name) {
        //TODO:database
        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra("user",name);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Log.i("LOGIN","Login to Main");
        startActivity(intent);
    }
}
