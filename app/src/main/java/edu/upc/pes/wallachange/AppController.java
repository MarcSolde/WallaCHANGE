package edu.upc.pes.wallachange;

import android.app.Application;
import android.content.Context;

/**
 * Created by sejo on 6/05/17.
 */

public class AppController extends Application {
    private static AppController instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static AppController getInstance() {
        return instance;
    }

    public static Context getAppContext() {
        return instance.getApplicationContext();
    }
}
