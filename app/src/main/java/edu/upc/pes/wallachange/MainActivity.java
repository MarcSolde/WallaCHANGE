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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import edu.upc.pes.wallachange.Models.Element;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private HomeFragment homeFragment;

    private FragmentManager myFragmentManager;
    private AddElementFragment myAddElementFragment;
    private DrawerLayout myDrawer;
    private String user;
    private ViewElementFragment myViewElementFragment;


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
                                LoginActivity.logOut();
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
                myAddElementFragment = new AddElementFragment();
                myFragmentManager.beginTransaction().replace(R.id.fragment, myAddElementFragment).commit();
                break;
            case R.id.navigationSearchUser:
                SearchUserFragment searchUserFragment= new SearchUserFragment();
                myFragmentManager.beginTransaction().replace(R.id.fragment, searchUserFragment).commit();
                break;
            case R.id.navigationSearchItem:
                SearchElementFragment ElementsFragment = new SearchElementFragment();
                myFragmentManager.beginTransaction().replace(R.id.fragment, ElementsFragment).commit();
                break;
            case R.id.navigationProfile:
                ProfileFragment ProfileFragment = new ProfileFragment();
                myFragmentManager.beginTransaction().replace(R.id.fragment, ProfileFragment).commit();
                break;
            default:
                break;
        }
        myDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void changeToItem(Element e) {
        //TODO:
        // String temporalitat
        Bundle bundleViewElement = new Bundle();
        bundleViewElement.putString("titol",e.getTitol());
        bundleViewElement.putString("descripcio",e.getDescripcio());
        bundleViewElement.putString("categoria",e.getCategoria());
        bundleViewElement.putString("usuari",e.getUser());
        bundleViewElement.putString("temporalitat",e.getTemporalitat());
        bundleViewElement.putString("tipusProducte",e.getTipusProducte());
        bundleViewElement.putBoolean("tipusIntercanvi",e.getEsTemporal());
        bundleViewElement.putParcelableArrayList("fotografies",e.getFotografies());

        myViewElementFragment = new ViewElementFragment();
        myViewElementFragment.setArguments(bundleViewElement);
        myFragmentManager.beginTransaction().replace(R.id.fragment, myViewElementFragment).commit();
    }

    public void changeFragmentToHome () {
        //TODO:
        homeFragment = new HomeFragment();
        myFragmentManager.beginTransaction().replace(R.id.fragment, homeFragment).commit();
        NavigationView myNavigationView = (NavigationView) findViewById(R.id.navigationView);
        myNavigationView.getMenu().getItem(0).setChecked(true);
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
