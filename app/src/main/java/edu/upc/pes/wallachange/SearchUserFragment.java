package edu.upc.pes.wallachange;


import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import edu.upc.pes.wallachange.APILayer.AdapterAPIRequest;
import edu.upc.pes.wallachange.Adapters.UserListAdapter;
import edu.upc.pes.wallachange.Models.CurrentUser;
import edu.upc.pes.wallachange.Models.User;


public class SearchUserFragment extends Fragment implements View.OnClickListener{
    private MainActivity myActivity;

    private UserListAdapter adapter;
    private ListView myListView;
    private EditText myText;

    private ArrayList<User> users;
    private static AdapterAPIRequest adapterAPI = new AdapterAPIRequest();

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
                            myActivity.hideKeyboard();
                            return true;
                        default: break;
                    }
                }
                return false;
            }
        });
        Button auxButton = (Button) view.findViewById(R.id.search_user_search);
        auxButton.setOnClickListener(this);
        auxButton = (Button) view.findViewById(R.id.search_user_filter) ;
        auxButton.setOnClickListener(this);
        myListView = (ListView) view.findViewById(R.id.search_user_list);
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                onClickUser(i);
            }
        });
        return view;
    }

    private void loadList (ArrayList<User> al) {
        ArrayList<String> aux = new ArrayList<> ();
        users = new ArrayList<> ();
        users = al;

        adapter = new UserListAdapter(myActivity,R.layout.item_default,users);
        myListView.setAdapter(adapter);
        myListView.deferNotifyDataSetChanged();
    }

    private void onClickUser (int i) {
        myActivity.changeToOtherUserProfile(users.get(i).getId());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_user_search:
                //TODO: revisar ocultacion teclado
                myActivity.hideKeyboard();
                //TODO: enlace DB
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");

                String aux = myText.getText().toString();
                if (aux.isEmpty()) {
                    aux = "/users";

                } else {
                    headers.put("preferencies", aux);
                    aux = "/user";
                }
                adapterAPI.GETJsonArrayRequestAPI(aux,
                        new Response.Listener<JSONArray>() {

                            @Override
                            public void onResponse(JSONArray response) {
                                try {
                                    CurrentUser user = CurrentUser.getInstance();
                                    Log.i("JSON: ",response.toString());
                                    ArrayList<User> aux = new ArrayList<> ();
                                    for (int i = 0;i < response.length();++i) {
                                        JSONObject var = response.getJSONObject(i);
                                        if (!user.getId().equals(var.getString("id"))) {
                                            JSONArray var2 = var.getJSONArray("preferencies");
                                            ArrayList<String> aux2 = new ArrayList<> ();
                                            for (int j = 0; j < var2.length();++j) {
                                                aux2.add(var2.get(j).toString());
                                            }
                                            if (aux2.size() == 0) aux2.add("No preference recorded");
                                            User u = new User(var.getString("id"),
                                                    var.getString("nom"),
                                                    null,
                                                    null,
                                                    Float.parseFloat(var.getString("reputacio")),
                                                    Uri.parse(var.getString("path")),
                                                    aux2);
                                            aux.add(u);
                                        }
                                    }
                                    loadList(aux);
                                }
                                catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.i("JSONerror: ","");
                            }
                        }, headers
                );
                break;
            case R.id.search_user_filter:
                break;
            default:
                break;
        }
    }


}
