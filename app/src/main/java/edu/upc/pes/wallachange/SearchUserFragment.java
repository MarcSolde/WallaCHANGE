package edu.upc.pes.wallachange;


import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;

import edu.upc.pes.wallachange.Adapters.SearchUserAdapter;


public class SearchUserFragment extends Fragment {
    private MainActivity myActivity;

    private SearchUserAdapter adapter;
    private ListView myListView;
    private EditText myText;

    private List<User> userList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_user, container, false);
        myActivity = (MainActivity) getActivity();
        myActivity.setTitle(R.string.navigationSearchUser_eng);
        myActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        myText = (EditText) view.findViewById(R.id.search_user_text);
        myText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (i) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            onSearch();
                            return true;
                        default: break;
                    }
                }
                return false;
            }
        });
        myListView = (ListView) view.findViewById(R.id.search_user_list);
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                onClickUser(i);
            }
        });
        loadList();
        Button mySearch = (Button) view.findViewById(R.id.search_user_search);
        mySearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSearch();
            }
        });
        return view;
    }

    private void loadList () {
        ArrayList<User> items = new ArrayList<>();
        ArrayList<String> aux = new ArrayList<> ();
        aux.add("aaa");
        aux.add("bbb");
        aux.add("ccc");
        User aux2 = new User("pepe","bcn","aaa",4,null,aux);
        items.add(aux2);
        items.add(new User("juanjo","mdr","aaa",3,null,aux));
        items.add(new User("phol","par","aaa",5,null,aux));
        adapter = new SearchUserAdapter(myActivity,R.layout.item_search_user,items,this);
        myListView.setAdapter(adapter);
        myListView.deferNotifyDataSetChanged();
    }

    public void onSearch() {
        String var = myText.getText().toString();
        myActivity.hideKeyboard();
        //TODO: enlace DB
    }

    private void onClickUser (int i) {
        //User user = userList.get(i);
        //TODO: ir al usuario
    }

}
