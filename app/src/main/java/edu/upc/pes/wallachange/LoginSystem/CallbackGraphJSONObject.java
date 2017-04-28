package edu.upc.pes.wallachange.LoginSystem;

import android.util.Log;

import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import org.json.JSONException;
import org.json.JSONObject;

import edu.upc.pes.wallachange.LoginActivity;

/**
 * Created by sejo on 13/04/17.
 */

public class CallbackGraphJSONObject implements GraphRequest.GraphJSONObjectCallback {
    private static LoginActivity myActivity;
    public CallbackGraphJSONObject(LoginActivity context) {
        myActivity = context;
    }

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
}
