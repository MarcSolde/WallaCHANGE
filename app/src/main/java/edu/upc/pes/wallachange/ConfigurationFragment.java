package edu.upc.pes.wallachange;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import java.util.Locale;

import edu.upc.pes.wallachange.MainActivity;
import edu.upc.pes.wallachange.R;

/**
 * Created by carlota on 28/5/17.
 */

public class ConfigurationFragment extends Fragment /*implements View.OnClickListener*/{

    private Activity myActivity;
    Locale myLocale;
    private RadioGroup rg;
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view;
        view = inflater.inflate(R.layout.configuration_fragment, container, false);
        myActivity = (MainActivity) getActivity();
        myActivity.setTitle(R.string.navigationConfiguration_eng);
//        setContentView(R.layout.configuration_fragment);
        rg = (RadioGroup) view.findViewById(R.id.radiogroup);
        rg.setOnCheckedChangeListener(  new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId) {
                    case R.id.butEng:
                        setLocale("en");
                        break;
                    case R.id.butCat:
                        Log.i("hola", "estic a ca");
                        setLocale("ca");
                        break;
                    case R.id.butEsp:
                        setLocale("es");
                }
            }
        });

        return view;
    }

    public void setLocale(String lang) {
        myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.setLocale(myLocale);
        myActivity.createConfigurationContext(conf);
//        Intent refresh = new Intent(this, AndroidLocalize.class);
//        startActivity(refresh);
    }

}