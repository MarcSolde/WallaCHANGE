package edu.upc.pes.wallachange;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        FragmentManager fragmentManager;
        fragmentManager = getFragmentManager();
        ProfileEdit ProfileFragment = new ProfileEdit();
        fragmentManager.beginTransaction().replace(R.id.fragment, ProfileFragment).commit();
    }
}
