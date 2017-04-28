package edu.upc.pes.wallachange;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private HomeFragment homeFragment;

    private FragmentManager myFragmentManager;
    private FragmentAddElement myFragmentAddElement;
    private DrawerLayout myDrawer;
    private String user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        myFragmentManager = getFragmentManager();
        homeFragment = new HomeFragment();

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        myDrawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, myDrawer, myToolbar, R.string.navigationOpen, R.string.navigationClose);
        //myDrawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView myNavigationView = (NavigationView) findViewById(R.id.navigationView);
        myNavigationView.setNavigationItemSelectedListener(this);

        Log.i("MAIN","Create ok");
        Bundle extras = getIntent().getExtras();
        user = extras.getString("user");
        //TODO:lenguage

        myFragmentManager.beginTransaction().replace(R.id.fragment,homeFragment).commit();
        Log.i("MAIN","Transaction ok");
        TextView textUser = (TextView) myNavigationView.getHeaderView(0).findViewById(R.id.navigationText);
        String text = getResources().getString(R.string.user_eng);
        text = text + " "+user;
        textUser.setText(text);
        Log.i("MAIN","Set text ok");

    }

    @Override
    public void onBackPressed () {
        if (myDrawer.isDrawerOpen(GravityCompat.START)) myDrawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigationMain:
                myFragmentManager.beginTransaction().replace(R.id.fragment,homeFragment).commit();
                break;
            case R.id.navigationLogout:
                AlertDialog.Builder alertBuilder2 = new AlertDialog.Builder(this);
                alertBuilder2.setTitle(R.string.logout_dialog_1);
                alertBuilder2.setMessage(R.string.logout_dialog_2)
                        .setPositiveButton(R.string.logout_dialog_3, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton(R.string.logout_dialog_4, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog dialog2 = alertBuilder2.create();
                dialog2.show();
                break;
            case R.id.navigationNewItem:
                myFragmentAddElement = new FragmentAddElement();
                myFragmentManager.beginTransaction().replace(R.id.fragment, myFragmentAddElement).commit();
                break;
            case R.id.navigationSearchUser:
                SearchUserFragment searchUserFragment= new SearchUserFragment();
                myFragmentManager.beginTransaction().replace(R.id.fragment, searchUserFragment).commit();
                break;
            case R.id.navigationSearchItem:
                CercaElements ElementsFragment = new CercaElements();
                myFragmentManager.beginTransaction().replace(R.id.fragment, ElementsFragment).commit();
                break;
            case R.id.navigationProfile:
                ProfileEdit ProfileFragment = new ProfileEdit();
                myFragmentManager.beginTransaction().replace(R.id.fragment, ProfileFragment).commit();
                break;
            default:
                break;
        }
        myDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public String getUsername() {
        return user;
    }

    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
