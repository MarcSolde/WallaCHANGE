package edu.upc.pes.wallachange.LoginSystem;

import android.widget.Toast;

import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;

import edu.upc.pes.wallachange.LoginActivity;
import edu.upc.pes.wallachange.R;

public class CallbackTwitter extends com.twitter.sdk.android.core.Callback<TwitterSession> {
    private LoginActivity myActivity;

    public CallbackTwitter(LoginActivity context) {
        myActivity = context;
    }

    @Override
    public void success(Result<TwitterSession> result) {
            // The TwitterSession is also available through:
            // Twitter.getInstance().core.getSessionManager().getActiveSession()
            TwitterSession session = result.data;
            final String name = session.getUserName();
            final TwitterAuthToken token = session.getAuthToken();
            myActivity.login();
            /*
            TwitterAuthClient authClient = new TwitterAuthClient();
            authClient.requestEmail(session, new Callback<String>() {
                @Override
                public void success(Result<String> result) {
                        String email = result.data;
                        myActivity.login(name,String.valueOf(token),email);

                }

                @Override
                public void failure(TwitterException exception) {
                        Toast.makeText(myActivity, R.string.errorAuthEmail_eng, Toast.LENGTH_LONG).show();
                }
            });
            */
    }

    @Override
    public void failure(TwitterException exception) {
            Toast.makeText(myActivity, R.string.errorIncorrectLogin_eng, Toast.LENGTH_LONG).show();
    }

}
