package edu.upc.pes.wallachange;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.upc.pes.wallachange.debug.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CloseOfferFragment extends Fragment {


    public CloseOfferFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_close_offer, container, false);
    }

}
