package edu.upc.pes.wallachange;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;

/**
 * Created by carlota on 17/3/17.
 */

public class ProfileEdit extends Fragment {

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.profile_edit, container, false);
        RatingBar mRatingBar = (RatingBar) view.findViewById(R.id.ratingBar);
        mRatingBar.setRating(5);
        return view;
    }
}
