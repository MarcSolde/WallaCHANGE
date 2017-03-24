package edu.upc.pes.wallachange;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import java.util.Locale;

import io.fabric.sdk.android.Fabric;

public class LoginActivity extends AppCompatActivity {
    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "6YPYzTVYffWUgukyKkaNry1Lg";
    private static final String TWITTER_SECRET = "ea81fYjYJsa2RcqlA5H3QmZ9RKxoQZtgmg0bNdFlzVVanBOj4Q";

    private TextView loginInfo;
    private TwitterLoginButton twLoginButton;
    private LoginButton fbLoginButton;
    CallbackManager callbackManager;
    private String lenguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
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
        twLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                // The TwitterSession is also available through:
                // Twitter.getInstance().core.getSessionManager().getActiveSession()
                TwitterSession session = result.data;
                String name = session.getUserName();
                TwitterAuthToken token = session.getAuthToken();
                /**
                TwitterAuthClient authClient = new TwitterAuthClient();
                authClient.requestEmail(session, new Callback<String>() {
                    @Override
                    public void success(Result<String> result) {
                        String email = result.data;
                        Toast.makeText(getApplicationContext(),email, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void failure(TwitterException exception) {
                        Toast.makeText(getApplicationContext(), "Email not authorited", Toast.LENGTH_LONG).show();
                    }
                });
                **/
                //TODO: Database
                login(name);
            }
            @Override
            public void failure(TwitterException exception) {
                //TODO: Get preferences
                if (lenguage.equals(getString(R.string.lenguage_eng))) Toast.makeText(getApplicationContext(),R.string.errorIncorrectLogin_eng, Toast.LENGTH_LONG).show();
                else Toast.makeText(getApplicationContext(),R.string.errorIncorrectLogin_esp, Toast.LENGTH_LONG).show();
            }
        });

        loginInfo = (TextView)findViewById(R.id.loginInfo);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();


        fbLoginButton = (LoginButton) findViewById(R.id.fb_login_button);
        fbLoginButton.setReadPermissions("email");
        // If using in a fragment
        //loginButton.setFragment(this);

        // Callback registration
        fbLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                loginInfo.setText(
                        "User ID: "
                                + loginResult.getAccessToken().getUserId()
                                + "\n" +
                                "Auth Token: "
                                + loginResult.getAccessToken().getToken()
                );
            }

            @Override
            public void onCancel() {
                loginInfo.setText("Login attempt canceled.");
            }

            @Override
            public void onError(FacebookException exception) {
                loginInfo.setText("Login attempt failed.");
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Make sure that the loginButton hears the result from any
        // Activity that it triggered.
        twLoginButton.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void login (String name) {
        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra("user",name);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
