package edu.upc.pes.wallachange;

import android.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "QFTeG0RyQsc57qVkFh9tTUjj0";
    private static final String TWITTER_SECRET = "dHoLLt2b4yDA9iWbXQdfyh7c5d3nYU2PVQNlNziQ7L0BijETkW";

    private HomeFragment homeFragment;

    private FragmentManager myFragmentManager;
    private DrawerLayout myDrawer;
    private String user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            user = extras.getString("user");
        }
        //TODO:lenguage
        NavigationView myNavigation = (NavigationView) findViewById(R.id.navigationView);
        myNavigation.setNavigationItemSelectedListener(this);

        myDrawer = (DrawerLayout) findViewById(R.id.drawerLayout);

        myFragmentManager = getFragmentManager();

        homeFragment = new HomeFragment();


    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigationMain;
        }
    }
}
