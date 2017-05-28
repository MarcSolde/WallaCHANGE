package edu.upc.pes.wallachange;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.facebook.CallbackManager;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.LoginButton;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import org.json.JSONException;

import java.util.Arrays;
import java.util.Locale;

import edu.upc.pes.wallachange.LoginSystem.CallbackFacebook;
import edu.upc.pes.wallachange.LoginSystem.CallbackTwitter;
import edu.upc.pes.wallachange.Models.CurrentUser;
import io.fabric.sdk.android.Fabric;


public class LoginActivity extends AppCompatActivity {
    private TwitterLoginButton twLoginButton;
    private LoginButton fbLoginButton;
    CallbackManager callbackManager;
    private String lenguage;
    final public static String MyPREFERENCES = "MyPrefs";
    final public static String MyTokenPref = "MyFBToken";
    final public static String MyFBidPref = "MyFBid";
    public static LoginActivity context;

    @Override
    protected void onStart(){
        super.onStart();
        context = this;
        try {
            CallbackFacebook.checkLogin();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

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
                lenguage = getString(R.string.lenguage_cat);
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
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Make sure that the loginButton hears the result from any
        // Activity that it triggered.
        twLoginButton.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void login() {
        CurrentUser user = CurrentUser.getInstance();

        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra("user",user.getUsername());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Log.i("LOGIN","Login to Main");
        startActivity(intent);
    }

    public static void logOut() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().commit();
        LoginManager.getInstance().logOut();
        Log.i("LOGIN","Logout from Main");
    }
}
