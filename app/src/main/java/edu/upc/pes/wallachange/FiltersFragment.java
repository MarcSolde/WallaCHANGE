package edu.upc.pes.wallachange;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RadioButton;

import edu.upc.pes.wallachange.Models.FilterElement;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link FiltersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FiltersFragment extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

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

    //private OnFragmentInteractionListener mListener;

    public FiltersFragment() {
        // Required empty public constructor
        filter = new FilterElement();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FiltersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FiltersFragment newInstance(String param1, String param2) {
        FiltersFragment fragment = new FiltersFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
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

        auto = (MultiAutoCompleteTextView) view.findViewById(R.id.multiAutoCompleteTextView1);
        radioTemp = (RadioButton) view.findViewById(R.id.radioButtonTemporal);
        radioPermanent = (RadioButton) view.findViewById(R.id.radioButtonPermanent);
        radioProd = (RadioButton) view.findViewById(R.id.radioButtonProd);
        radioProd.setChecked(filter.getTipus_element());
        radioExp = (RadioButton) view.findViewById(R.id.radioButtonExp);
        submitButton = (Button) view.findViewById(R.id.submitButton1);
        cancelButton = (Button) view.findViewById(R.id.cancelButton1);
        searchElemFragment = new SearchElementFragment();
        ArrayAdapter<String> adapter = new ArrayAdapter
                (myActivity,android.R.layout.simple_list_item_1,countries);
        auto.setAdapter(adapter);
        auto.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        submitButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);

        // Inflate the layout for this fragment
        return view;
    }
//
//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }

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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }

}
