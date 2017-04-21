package edu.upc.pes.wallachange;


import android.app.FragmentManager;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


public class HomeFragment extends Fragment implements MenuItem.OnMenuItemClickListener{

    private MainActivity myActivity;
    private MenuItem itemFilters;
    private FragmentManager myFragmentManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        myFragmentManager = getFragmentManager();
        myActivity = (MainActivity) getActivity();
        myActivity.setTitle(R.string.navigationHome_eng);
        myActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_home, menu);
        itemFilters = menu.findItem(R.id.itemFilters);
        itemFilters.setOnMenuItemClickListener(this);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemFilters:
                ElementFiltersFragment FiltersFragment = new ElementFiltersFragment();
                myFragmentManager.beginTransaction().replace(R.id.fragment, FiltersFragment).addToBackStack(null).commit();
                Intent intent = new Intent(getActivity(),ElementFiltersFragment.class);
                startActivity(intent);
                break;
        }
        return false;
    }
}
