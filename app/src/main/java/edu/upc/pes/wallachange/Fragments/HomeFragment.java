package edu.upc.pes.wallachange.Fragments;


import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.upc.pes.wallachange.MainActivity;
import edu.upc.pes.wallachange.R;


public class HomeFragment extends Fragment {
    private MainActivity myActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        myActivity = (MainActivity) getActivity();
        myActivity.setTitle(R.string.navigationHome_eng);
        myActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        return view;
    }
}
