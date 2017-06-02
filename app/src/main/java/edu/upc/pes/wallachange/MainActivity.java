package edu.upc.pes.wallachange;

import static java.security.AccessController.getContext;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import edu.upc.pes.wallachange.APILayer.AdapterAPIRequest;
import edu.upc.pes.wallachange.Models.CurrentUser;
import edu.upc.pes.wallachange.Models.Element;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private SearchElementFragment homeFragment;

    private FragmentManager myFragmentManager;
    private DrawerLayout myDrawer;

    private NavigationView myNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        myFragmentManager = getFragmentManager();
        homeFragment = new SearchElementFragment();

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        myDrawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, myDrawer, myToolbar, R.string.navigationOpen, R.string.navigationClose);
        //myDrawer.setDrawerListener(toggle);
        toggle.syncState();

        myNavigationView = (NavigationView) findViewById(R.id.navigationView);
        myNavigationView.setNavigationItemSelectedListener(this);

        Log.i("MAIN","Create ok");
        //TODO:lenguage

        myFragmentManager.beginTransaction().replace(R.id.fragment,homeFragment).commit();
        Log.i("MAIN","Transaction ok");
        TextView textUser = (TextView) myNavigationView.getHeaderView(0).findViewById(R.id.navigationText);

        CurrentUser user = CurrentUser.getInstance();
        String text = getResources().getString(R.string.user_eng);
        text = text + " "+ user.getUsername();
        textUser.setText(text);
        Log.i("MAIN","Set text ok");


        final ImageView button = (ImageView) findViewById(R.id.translateButton);
        button.setImageDrawable(null);   //This will force the image to properly refresh
        button.setImageResource(R.drawable.prova);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                callActivity();
            }
        });


    }
    @SuppressWarnings("deprecation")
    public void callActivity() {

        String eng = getResources().getString(R.string.english_eng);
        String esp = getResources().getString(R.string.spanish_eng);
        String cat = getResources().getString(R.string.catalan_eng);
        final String[] items = {eng, esp, cat};
        int inputSelection = 1;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(getResources().getString(R.string.language_eng));
        Locale current = getResources().getConfiguration().locale;
        String local = current.toString();
        if (local.equals("es")) {
            inputSelection = 1;
        }
        else if (local.equals("ca")) {
            inputSelection = 2;
        }
        else inputSelection = 0;

        builder.setSingleChoiceItems(items,inputSelection,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        if (item == 0)
                            Translate.changeLocale(getResources(), "en_US");
                        else if (item == 1)
                            Translate.changeLocale(getResources(), "es");
                        else if (item == 2) {
                            Translate.changeLocale(getResources(), "ca");
                        }
                        changeNav();
                        dialog.dismiss();
                        final ImageView button = (ImageView) findViewById(R.id.translateButton);
                        button.setImageDrawable(null);   //This will force the image to properly refresh
                        button.setImageResource(R.drawable.prova);

                    }
                });
        AlertDialog levelDialog = builder.create();
        levelDialog.show();
    }

    public void changeNav() {
        recreate();

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
                AddElementFragment myAddElementFragment = new AddElementFragment();
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

            case R.id.navigationFilters:
                FiltersFragment FiltersFragment = new FiltersFragment();
                myFragmentManager.beginTransaction().replace(R.id.fragment, FiltersFragment).commit();
                break;
            default:
                break;
        }
        myDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void changeToItem(String id) {
        //TODO:
        Bundle bundleViewElement = new Bundle();
        bundleViewElement.putString("id",id);
        ViewElementFragment myViewElementFragment = new ViewElementFragment();
        myViewElementFragment.setArguments(bundleViewElement);
        myFragmentManager.beginTransaction().replace(R.id.fragment, myViewElementFragment).commit();
    }

    public void changeFragmentToHome () {
        //TODO:
        homeFragment = new SearchElementFragment();
        myFragmentManager.beginTransaction().replace(R.id.fragment, homeFragment).commit();
        NavigationView myNavigationView = (NavigationView) findViewById(R.id.navigationView);
        myNavigationView.getMenu().getItem(0).setChecked(true);
    }

    public String getUsername() {
        //TODO: eliminar
        return "quitar esto";
    }

    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public  void changeToOtherUserProfile (String id) {
        SeeProfileFragment seeProfileFragment = new SeeProfileFragment();
        Bundle args = new Bundle();
        args.putString("id",id);
        seeProfileFragment.setArguments(args);
        myFragmentManager.beginTransaction().replace(R.id.fragment, seeProfileFragment).commit();
    }



}
