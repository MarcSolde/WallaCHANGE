package edu.upc.pes.wallachange;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;

import edu.upc.pes.wallachange.APILayer.AdapterAPIRequest;

import static com.android.volley.VolleyLog.TAG;


public class ProfileEdit extends Fragment  implements View.OnClickListener {

    private int PICK_IMAGE = 1;
    private MainActivity myActivity;

    private User user;

    private String location;
    private ArrayList<String> prefs;

    private PreferencesAdapter preferencesAdapter;
    private ImageView fotoPerfil;
    private EditText locationTE;
    private EditText editTextPref;
    //private int rating;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.profile_edit, container, false);
        myActivity = (MainActivity) getActivity();

        user = new User();
        JSONObject js;

        //User de prova, li assigno els paràmetres
        //S'haurà d'esborrar en un futur
        user.setUsername(myActivity.getUsername());

        AdapterAPIRequest adapter = new AdapterAPIRequest();
        adapter.GETJsonObjectRequestAPI(
                "http://localhost:3000/user/"+"pepito",
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("tagtag", response.toString());
                        locationTE.setText("OKEY!");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                        locationTE.setText("Error");
                    }
                });

        user.setLocation("Sant Cugat");
        user.addPreference("esport");
        user.addPreference("patinatge");
        user.addPreference("skate");
        user.setRating(3);
        Uri imgProva=Uri.parse("android.resource://edu.upc.pes.wallachange/"+R.drawable.userpicture);
        user.setPicture(imgProva);

        ExpandableHeightGridView gridPrefs;
        ImageView addPref;

        RatingBar mRatingBar;
        TextView usernameField;
        ImageView cleanLocation;
        Button submitProfile;


        //get camps del layout
        fotoPerfil = (ImageView) view.findViewById(R.id.userPicture);
        usernameField = (TextView) view.findViewById(R.id.username);
        locationTE = (EditText) view.findViewById(R.id.editCity);
        cleanLocation = (ImageView) view.findViewById(R.id.cleanLocation);
        addPref = (ImageView) view.findViewById(R.id.prefAddButton);
        gridPrefs = (ExpandableHeightGridView) view.findViewById(R.id.gridPreferences);
        mRatingBar = (RatingBar) view.findViewById(R.id.ratingBar);
        submitProfile = (Button) view.findViewById(R.id.submitButton);
        editTextPref = (EditText) view.findViewById(R.id.addPreference);


        //set camps del layout
        String username;
        username = user.getUsername();
        usernameField.setText(username);

        fotoPerfil.setImageURI(null);
        fotoPerfil.setImageURI(user.getPicture());

        location = user.getLocation();
        //locationTE.setText(location);

        mRatingBar.setRating(user.getRating());
        mRatingBar.setEnabled(false);

        prefs = new ArrayList<String>();
        prefs = user.getPreferences();
        gridPrefs.setExpanded(true);
        preferencesAdapter = new PreferencesAdapter(myActivity, R.layout.pref_list_item, prefs, this);
        gridPrefs.setAdapter(preferencesAdapter);
        preferencesAdapter.notifyDataSetChanged();

//        locationTE.setOnClickListener(this);

        fotoPerfil.setOnClickListener(this);

        submitProfile.setOnClickListener(this);

        cleanLocation.setOnClickListener(this);

        addPref.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {



            case R.id.userPicture:
                Intent pickIntent;
                if (Build.VERSION.SDK_INT >= 19) {
                    pickIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                } else pickIntent = new Intent(Intent.ACTION_GET_CONTENT);
                pickIntent.setType("image/*");
                pickIntent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(Intent.createChooser(pickIntent, "Import img"), PICK_IMAGE);
                break;

            case R.id.prefAddButton:
                String newPref = editTextPref.getText().toString();
                if (newPref.trim().length() != 0) {
                    if (!user.existsPref(newPref)) {
                        prefs.add(newPref);
                        preferencesAdapter.notifyDataSetChanged();
                        editTextPref.setText("");
                    } else {
                        String errorExisting = getResources().getString(R.string.errorExistingPref_eng);
                        editTextPref.setError(errorExisting);
                    }
                } else {
                    String errorEmpty = getResources().getString(R.string.errorEmptyField_eng);
                    editTextPref.setError(errorEmpty);
                }
                break;

            case R.id.cleanLocation:
                locationTE.setText("");
                break;

            case R.id.submitButton:
                location = locationTE.getText().toString();
                if (location.trim().length() != 0) {
                    if (!user.getLocation().isEmpty()) {
                        if (!user.getLocation().equals(location)) user.setLocation(location);
                    } else user.setLocation(location);

                } else {
                    String errorEmpty = getResources().getString(R.string.errorEmptyField_eng);
                    locationTE.setError(errorEmpty);
                }
                user.setPreferencesArray(prefs);
                break;


            default:
                break;

        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {

            Uri selectedImage = data.getData();
            System.out.println(selectedImage.toString());

            Picasso.with(myActivity).load(selectedImage).resize(100, 100).transform(new CircleTransform()).into(fotoPerfil);
        }
    }

    public void decrementarNombrePrefs (ArrayList<String> newPrefs, String prefToDelete) {
            prefs = newPrefs;
            user.deletePreference(prefToDelete);
    }


}
