package edu.upc.pes.wallachange;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;

import java.util.Locale;

/**
 * Created by carlota on 31/5/17.
 */

public class Translate extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @SuppressWarnings("deprecation")
    public static void changeLocale(Resources res, String locale) {
        Configuration config;
        config = new Configuration(res.getConfiguration());
        switch (locale) {
            case "en":
                config.locale = new Locale("en");
                break;
            case "es":
                config.locale = new Locale("es");
                break;
            case "ca":
                config.locale = new Locale("ca");
                break;
        }
        res.updateConfiguration(config, res.getDisplayMetrics());
    }

}