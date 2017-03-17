package edu.upc.pes.wallachange;

import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager myFragmentManager = getFragmentManager();

        FragmentAddElement fragmentAddElement = new FragmentAddElement();

        myFragmentManager.beginTransaction().replace(R.id.fragment, fragmentAddElement).commit();

    }
}
