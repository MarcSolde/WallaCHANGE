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

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

//    public FiltersFragment() {
//        Log.d(TAG, "FiltersFragment: ");
//    }


    private static final String[] countries = new String[] {
            "Belgium", "France", "Italy", "Germany", "Spain"
    };

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
        ArrayAdapter<String> adapter = new ArrayAdapter
                (myActivity, android.R.layout.simple_list_item_1, countries);
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
                myFragmentManager = getFragmentManager();
                myFragmentManager.beginTransaction().replace(R.id.fragment, searchElemFragment).commit();
                break;
            case R.id.submitButton1:
                String intermediate_text = auto.getText().toString();
                String[] finalTags = intermediate_text.split(",");
//                String finalTags = intermediate_text.substring(intermediate_text.lastIndexOf(",") + 1);
                filter.setTags(finalTags);

                if (radioTemp.isChecked()) filter.setTemporalitat(true);
                else filter.setTemporalitat(false);

                filter.setEs_producte(radioProd.isChecked());
                for (int i = 0; i < finalTags.length; i++) Log.d("MyTagGoesHere", finalTags[i]);
                myFragmentManager = getFragmentManager();
                myFragmentManager.beginTransaction().replace(R.id.fragment, searchElemFragment).commit();
                break;


        }
    }
}



