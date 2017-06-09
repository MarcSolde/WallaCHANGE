package edu.upc.pes.wallachange;

import android.app.FragmentManager;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RadioButton;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

import edu.upc.pes.wallachange.APILayer.AdapterAPIRequest;
import edu.upc.pes.wallachange.Models.CurrentUser;
import edu.upc.pes.wallachange.Models.Element;
import edu.upc.pes.wallachange.Models.FilterElement;

import static com.twitter.sdk.android.core.TwitterCore.TAG;

public class FiltersFragment extends Fragment implements View.OnClickListener{
    private FilterElement filter;
    private MainActivity myActivity;
    private MultiAutoCompleteTextView auto;
    private RadioButton radioTemp;
    private RadioButton radioPermanent;
    private RadioButton radioProd;
    private RadioButton radioExp;
    private Button submitButton;
    private Button cancelButton;
    private FragmentManager myFragmentManager;
    private SearchElementFragment searchElemFragment;

    private AdapterAPIRequest APIadapter = new AdapterAPIRequest();
    private Map<String, String> headers = new HashMap<>();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

//    public FiltersFragment() {
//        Log.d(TAG, "FiltersFragment: ");
//    }


    private static final ArrayList tags = new ArrayList();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.fragment_filters, container, false);
        myActivity = (MainActivity) getActivity();
        myActivity.setTitle("Filters");

        filter = new FilterElement();
        auto = (MultiAutoCompleteTextView) view.findViewById(R.id.multiAutoCompleteTextView1);
        radioTemp = (RadioButton) view.findViewById(R.id.radioButtonTemporal);
        radioPermanent = (RadioButton) view.findViewById(R.id.radioButtonPermanent);
        radioProd = (RadioButton) view.findViewById(R.id.radioButtonProd);
        radioExp = (RadioButton) view.findViewById(R.id.radioButtonExp);
        submitButton = (Button) view.findViewById(R.id.submitButton1);
        cancelButton = (Button) view.findViewById(R.id.cancelButton1);
        searchElemFragment = new SearchElementFragment();
//        tags.add("Belgium");
//        tags.add("France");

        final LinkedHashSet<String> hashSet = new LinkedHashSet<>();
        CurrentUser us = CurrentUser.getInstance();
        headers.put("x-access-token", us.getToken());
        APIadapter.GETJsonArrayRequestAPI("/api/tags", new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        JSONArray ja = response;
                        try {
                            if (ja != null) {
                                int len = ja.length();
                                tags.clear();
                                for (int i = 0; i < len; i++) {
                                    String[] auxString = ja.getJSONObject(i).getString("_id").split(",");
                                    for (int j = 0; j < auxString.length; j++) {
                                        if (hashSet.add(auxString[j])) tags.add(auxString[j]);
                                    }

                                }

                            }
                        } catch(JSONException e){
                            e.printStackTrace();
                        }

                    }},
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(VolleyLog.TAG, "Error: " + error.getMessage());

                    }
                },headers);

        ArrayAdapter<String> adapter = new ArrayAdapter
                (myActivity, android.R.layout.simple_list_item_1, tags);
        auto.setAdapter(adapter);
        auto.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        submitButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);

        // Inflate the layout for this fragment
        return view;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancelButton1:
                myActivity.changeFragmentToHome();
                break;
            case R.id.submitButton1:
                String tags = auto.getText().toString().concat("fin");

                String temporalitat = "";
                String es_producte = "";
                if (radioTemp.isChecked()) temporalitat = "temporal";
                if (radioPermanent.isChecked()) temporalitat = "permanent";
                if (radioExp.isChecked()) es_producte = "experiencia";
                if (radioProd.isChecked()) es_producte = "producte";
                myActivity.changeToHomeAndSetFilter(tags, temporalitat, es_producte);
                break;


        }
    }
}



