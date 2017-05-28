package edu.upc.pes.wallachange;

import static android.content.Context.INPUT_METHOD_SERVICE;

import static com.android.volley.VolleyLog.TAG;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import edu.upc.pes.wallachange.APILayer.AdapterAPIRequest;
import edu.upc.pes.wallachange.APILayer.Proxy;
import edu.upc.pes.wallachange.Adapters.PreferencesAdapter;
import edu.upc.pes.wallachange.Models.CurrentUser;
import edu.upc.pes.wallachange.Others.CircleTransform;
import edu.upc.pes.wallachange.Others.ExpandableHeightGridView;


public class ProfileFragment extends Fragment  implements View.OnClickListener {

    private int PICK_IMAGE = 1;
    private MainActivity myActivity;

    private CurrentUser user;

    private String location;
    private ArrayList<String> prefs;

    private PreferencesAdapter preferencesAdapter;
    private ImageView fotoPerfil;
    private EditText locationTE;
    private EditText editTextPref;
    //private int rating;
    private Context context;
    private Boolean foto;
    private FragmentManager myFragmentManager;
    private View view;



    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        myActivity = (MainActivity) getActivity();
        myActivity.setTitle(R.string.navigationProfile_eng);
        user = CurrentUser.getInstance();
        hideSoftKeyboard();

        ExpandableHeightGridView gridPrefs;
        ImageView addPref;
        RatingBar mRatingBar;
        TextView usernameField;
        ImageView cleanLocation;
        Button submitProfile;
        Button cancelButton;


        //get camps del layout
        fotoPerfil = (ImageView) view.findViewById(R.id.userPicture);
        usernameField = (TextView) view.findViewById(R.id.username);
        locationTE = (EditText) view.findViewById(R.id.editCity);
        cleanLocation = (ImageView) view.findViewById(R.id.cleanLocation);
        addPref = (ImageView) view.findViewById(R.id.prefAddButton);
        gridPrefs = (ExpandableHeightGridView) view.findViewById(R.id.gridPreferences);
        mRatingBar = (RatingBar) view.findViewById(R.id.ratingBar);
        submitProfile = (Button) view.findViewById(R.id.submitButton);
        cancelButton = (Button) view.findViewById(R.id.cancelButton);
        editTextPref = (EditText) view.findViewById(R.id.addPreference);

        foto = false;

        usernameField.setText(user.getUsername());
        fotoPerfil.setImageURI(null);
        fotoPerfil.setImageURI(user.getPicture());
        locationTE.setText(user.getLocation());
        //locationTE.setText(location);

        mRatingBar.setRating(user.getRating());
        mRatingBar.setEnabled(false);

        prefs = new ArrayList<String>();
        prefs = user.getPreferences();
        gridPrefs.setExpanded(true);
        preferencesAdapter = new PreferencesAdapter(myActivity, R.layout.pref_list_item, prefs, this);
        gridPrefs.setAdapter(preferencesAdapter);
        preferencesAdapter.notifyDataSetChanged();
//        locationTE.setText(user.getPicture().toString());


        locationTE.setOnClickListener(this);

        editTextPref.setOnClickListener(this);

        fotoPerfil.setOnClickListener(this);

        submitProfile.setOnClickListener(this);

        cancelButton.setOnClickListener(this);

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
                foto = true;
                break;
            case R.id.editCity:
                showSoftKeyboard(view);
                locationTE.requestFocus();
                break;
            case R.id.addPreference:
                showSoftKeyboard(view);
                editTextPref.requestFocus();
                break;
            case R.id.prefAddButton:
                String newPref = editTextPref.getText().toString();
                if (!newPref.equals("")) {
                    if (!user.existsPref(newPref)) {
                        user.setPreference(newPref);
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
                hideSoftKeyboard();
                break;

            case R.id.cleanLocation:
                locationTE.setText("");
                break;

            case R.id.submitButton:
                user.setLocation(locationTE.getText().toString());
                ArrayList<String> afegirPreferencies = new ArrayList<>();
                afegirPreferencies = user.getPreferences();
                String token = user.getToken();
                String location = user.getLocation();

                JSONArray ja = new JSONArray();
                for (String p : afegirPreferencies) {
                    ja.put(p);
                }

                JSONObject body = new JSONObject();
                try {
                    body.put("token", token);
                    body.put("localitat", user.getLocation());
                    body.put("preferencies", ja);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Map<String,String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                AdapterAPIRequest adapter = new AdapterAPIRequest();
                adapter.PUTRequestAPI("http://104.236.98.100:3000/updateUser/MarcSoldevillaCuartiella",
//                adapter.PUTRequestAPI("http://104.236.98.100:3000/updateUser/" + user.getId(),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                JSONObject js = response;
                                //SI voleu aquí comprovar que s'ha fet bé
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                VolleyLog.d(TAG, "Error: " + error.getMessage());
                            }
                        }, body, headers);

                //COMENTAR LA SEGUENT LINIA PER A IMPLEMENTAR EL POST DE LA FOTO
                foto = false;
                if (foto) {
                    JSONObject bodyimatge = new JSONObject();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    Bitmap bitmap = user.getPictureBitmap();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 1, stream);
                    byte [] byte_arr = stream.toByteArray();
                    String image = Base64.encodeToString(byte_arr, Base64.DEFAULT);
                    Log.i("bitmap", image);

                    try {
                        bodyimatge.put("avatar", image);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Map<String, String> headersimatge = new HashMap<>();
                    headersimatge.put("x-access-token", user.getToken());
//                    headersimatge.put("Content-Type", "application/x-www-form-urlencoded");



                    Log.i("token", user.getToken());
                    adapter.POSTRequestAPI(
                            "http://104.236.98.100:3000/imatge/MarcSoldevillaCuartiella",
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    JSONObject js = response;
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                                }
                            }, bodyimatge, headersimatge);
                }
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(myActivity, "Profile changes updated successfully.", duration);
                toast.show();
                myActivity.changeFragmentToHome();
                break;

            case R.id.cancelButton:
                myActivity.changeFragmentToHome();
                break;

            default:
                break;

        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {

            Uri selectedImage = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(myActivity.getContentResolver(), selectedImage);

                user.setPictureBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }


            Picasso.with(myActivity).load(selectedImage).resize(100, 100).transform(new CircleTransform()).into(fotoPerfil);

            user.setPictureBitmap(bitmap);
        }
    }

    public void decrementarNombrePrefs (ArrayList<String> newPrefs, String prefToDelete) {
            user.deletePreference(prefToDelete);
            prefs = user.getPreferences();

    }

    public void hideSoftKeyboard() {
        if(myActivity.getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) myActivity.getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(myActivity.getCurrentFocus().getWindowToken(), 0);
        }
    }


    public void showSoftKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) myActivity.getSystemService(INPUT_METHOD_SERVICE);
        view.requestFocus();
        inputMethodManager.showSoftInput(view, 0);
    }





}
