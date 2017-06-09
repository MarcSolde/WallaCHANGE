package edu.upc.pes.wallachange;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
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
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import edu.upc.pes.wallachange.APILayer.AdapterAPIRequest;
import edu.upc.pes.wallachange.Models.Conversa;
import edu.upc.pes.wallachange.Models.CurrentUser;

import edu.upc.pes.wallachange.Models.Element;
import edu.upc.pes.wallachange.Models.FilterElement;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private MainActivity myActivity;
    private FragmentManager myFragmentManager;
    private DrawerLayout myDrawer;
    private FilterElement filterElement = new FilterElement();

    private NavigationView myNavigationView;

    //OnBack
    private ArrayList<Integer> backFlow;
    private AddElementFragment myAddElementFragment;        //id:1
    private ViewElementFragment myViewElementFragment;      //id:2
    private YourItemsFragment myYourItemsFragment;          //id:3
    private ProfileFragment myProfileFragment;              //id:4
    private SearchUserFragment mySearchUserFragment;        //id:5
    private SeeProfileFragment mySeeProfileFragment;        //id:6
    private MakeOfferFragment myMakeOfferFragment;          //id:7
    private SearchElementFragment mySearchElementFragment;  //id:8
    private MainChatFragment myMainChatFragment;            //id:9
    private ChatFragment myChatFragment;                    //id:10

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        myFragmentManager = getFragmentManager();

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        myDrawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, myDrawer, myToolbar, R.string.navigationOpen, R.string.navigationClose);
        //myDrawer.setDrawerListener(toggle);
        toggle.syncState();

        myNavigationView = (NavigationView) findViewById(R.id.navigationView);
        myNavigationView.setNavigationItemSelectedListener(this);
        hideKeyboardStart();

        changeToHomeAndSetFilter("fin", "", "");

        TextView textUser = (TextView) myNavigationView.getHeaderView(0).findViewById(R.id.navigationText);
        CurrentUser user = CurrentUser.getInstance();
        String text = getResources().getString(R.string.user_eng);
        text = text + " "+ user.getUsername();
        textUser.setText(text);


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
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
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
                resetOnBackFlow(1);
                myAddElementFragment = new AddElementFragment();
                myFragmentManager.beginTransaction().replace(R.id.fragment, myAddElementFragment).commit();
                break;
            case R.id.navigationYourItems:
                resetOnBackFlow(3);
                myYourItemsFragment = new YourItemsFragment();
                myFragmentManager.beginTransaction().replace(R.id.fragment, myYourItemsFragment).commit();
                break;
            case R.id.navigationSearchUser:
                resetOnBackFlow(5);
                mySearchUserFragment= new SearchUserFragment();
                myFragmentManager.beginTransaction().replace(R.id.fragment, mySearchUserFragment).commit();
                break;
            case R.id.navigationSearchItem:

                changeFragmentToHome();

                break;
            case R.id.navigationProfile:
                resetOnBackFlow(4);
                myProfileFragment = new ProfileFragment();
                myFragmentManager.beginTransaction().replace(R.id.fragment, myProfileFragment).commit();
                break;
            case R.id.navigationChat:
                resetOnBackFlow(9);
                myMainChatFragment = new MainChatFragment();
                myFragmentManager.beginTransaction().replace(R.id.fragment, myMainChatFragment).commit();

            default:
                break;
        }
        myDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void changeToItem(String id) {
        backFlow.add(2);
        Bundle bundleViewElement = new Bundle();
        bundleViewElement.putString("id",id);
        myViewElementFragment = new ViewElementFragment();
        myViewElementFragment.setArguments(bundleViewElement);
        myFragmentManager.beginTransaction().replace(R.id.fragment, myViewElementFragment).commit();
    }

    public void changeToItemChat(String id) {
        backFlow.add(2);
        Bundle bundleViewElement = new Bundle();
        bundleViewElement.putString("id",id);
        bundleViewElement.putBoolean("chat",true);
        myViewElementFragment = new ViewElementFragment();
        myViewElementFragment.setArguments(bundleViewElement);
        myFragmentManager.beginTransaction().replace(R.id.fragment, myViewElementFragment).commit();
    }

    public void changeFragmentToHome () {
        backFlow.add(8);
        mySearchElementFragment = new SearchElementFragment();
        Bundle args = new Bundle();
        args.putString("tags",filterElement.getTags());
        args.putString("temporalitat", filterElement.getTemporalitat());
        args.putString("es_producte", filterElement.getTipus_element());
        mySearchElementFragment.setArguments(args);
        myFragmentManager.beginTransaction().replace(R.id.fragment, mySearchElementFragment).commit();
    }

    public  void changeToOtherUserProfile (String id) {
        backFlow.add(6);
        mySeeProfileFragment = new SeeProfileFragment();
        Bundle args = new Bundle();
        args.putString("id",id);
        mySeeProfileFragment.setArguments(args);
        myFragmentManager.beginTransaction().replace(R.id.fragment, mySeeProfileFragment).commit();
    }

    public void changeToMakeOffer (String id) {
        backFlow.add(7);
        myMakeOfferFragment = new MakeOfferFragment();
        Bundle args = new Bundle();
        args.putString("id",id);
        myMakeOfferFragment.setArguments(args);
        myFragmentManager.beginTransaction().replace(R.id.fragment, myMakeOfferFragment).commit();
    }


    public void changeFragmentToFilters () {
        FiltersFragment filtersFragment = new FiltersFragment();
        myFragmentManager.beginTransaction().replace(R.id.fragment, filtersFragment).commit();
    }

    public void changeToHomeAndSetFilter (String tags, String temporalitat, String es_producte) {
        filterElement.setTags(tags);
        filterElement.setTemporalitat(temporalitat);
        filterElement.setEs_producte(es_producte);
        mySearchElementFragment = new SearchElementFragment();
        Bundle args = new Bundle();
        args.putString("tags", tags);
        args.putString("temporalitat", temporalitat);
        args.putString("es_producte", es_producte);
        mySearchElementFragment.setArguments(args);
        myFragmentManager.beginTransaction().replace(R.id.fragment, mySearchElementFragment).commit();
        myNavigationView.getMenu().getItem(0).setChecked(true);
        backFlow = new ArrayList<>();
        resetOnBackFlow(8);
    }

    public void changeToYourItems(){
        resetOnBackFlow(3);
        myYourItemsFragment = new YourItemsFragment();
        myFragmentManager.beginTransaction().replace(R.id.fragment, myYourItemsFragment).commit();
    }


    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void hideKeyboardStart() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View view = getCurrentFocus();
        if (view != null && (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) && view instanceof EditText && !view.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            view.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + view.getLeft() - scrcoords[0];
            float y = ev.getRawY() + view.getTop() - scrcoords[1];
            if (x < view.getLeft() || x > view.getRight() || y < view.getTop() || y > view.getBottom())
                ((InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow((this.getWindow().getDecorView().getApplicationWindowToken()), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onBackPressed () {
        if (myDrawer.isDrawerOpen(GravityCompat.START)) myDrawer.closeDrawer(GravityCompat.START);
        if (!backFlow.isEmpty()) {
            int aux = backFlow.size() - 2;
            if (backFlow.get(aux) != 0) {
                //TODO: revisar transiciones
                switch (backFlow.get(aux)) {
                    case 1:
                        resetOnBackFlow(aux+1);  //AddElement
                        break;
                    case 2:
                        backFlow.remove(aux+1);
                        myFragmentManager.beginTransaction().replace(R.id.fragment, myViewElementFragment).commit();
                        break;
                    case 3:
                        backFlow.remove(aux+1);
                        myFragmentManager.beginTransaction().replace(R.id.fragment, myYourItemsFragment).commit();
                        break;
                    case 4:
                        backFlow.remove(aux+1);
                        myFragmentManager.beginTransaction().replace(R.id.fragment, myProfileFragment).commit();
                        break;
                    case 5:
                        backFlow.remove(aux+1);
                        myFragmentManager.beginTransaction().replace(R.id.fragment, mySearchUserFragment).commit();
                        break;
                    case 6:
                        backFlow.remove(aux+1);
                        myFragmentManager.beginTransaction().replace(R.id.fragment, mySeeProfileFragment).commit();
                        break;
                    case 7:
                        resetOnBackFlow(aux+1); //MakeOffer
                        break;
                    case 8:
                        backFlow.remove(aux+1);
                        myFragmentManager.beginTransaction().replace(R.id.fragment, mySearchElementFragment).commit();
                        break;
                    case 9:
                        backFlow.remove(aux+1);
                        myFragmentManager.beginTransaction().replace(R.id.fragment, myMainChatFragment).commit();
                        break;
                    case 10:
                        backFlow.remove(aux+1);
                        myFragmentManager.beginTransaction().replace(R.id.fragment, myChatFragment).commit();
                        break;
                    default:
                        break;
                }
            }
        }
    }

    public void changeToChat (String c) {
        backFlow.add(10);
        myChatFragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putString("conversa", c);
        myChatFragment.setArguments(args);
        myFragmentManager.beginTransaction().replace(R.id.fragment, myChatFragment).commit();
    }
    private void resetOnBackFlow(int i) {
        backFlow.clear();
        backFlow.add(0);
        backFlow.add(i);
    }
}
