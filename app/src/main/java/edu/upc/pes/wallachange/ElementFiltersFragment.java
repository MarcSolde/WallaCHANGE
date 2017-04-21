package edu.upc.pes.wallachange;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by masca on 21/04/2017.
 */

public class ElementFiltersFragment extends Fragment implements View.OnClickListener{

    private MainActivity myActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.element_filters, container, false);
        myActivity = (MainActivity) getActivity();
        Button btnCancel = (Button) view.findViewById(R.id.cancelButton);
        btnCancel.setOnClickListener(this);
        Button btnApply = (Button) view.findViewById(R.id.applyButton);
        btnApply.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancelButton:
                getActivity().onBackPressed();
                break;
            case R.id.applyButton:
                Toast.makeText(getContext(),"This functionality is yet to be implemented", Toast.LENGTH_SHORT).show();
                getActivity().onBackPressed();
                break;
        }
    }
}
