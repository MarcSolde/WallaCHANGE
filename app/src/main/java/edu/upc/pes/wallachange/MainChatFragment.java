package edu.upc.pes.wallachange;


import static com.android.volley.VolleyLog.TAG;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edu.upc.pes.wallachange.APILayer.AdapterAPIRequest;
import edu.upc.pes.wallachange.Adapters.ChatAdapter;
import edu.upc.pes.wallachange.Adapters.ConversesAdapter;
import edu.upc.pes.wallachange.Adapters.PreferencesAdapter;
import edu.upc.pes.wallachange.Models.Conversa;
import edu.upc.pes.wallachange.Models.CurrentUser;
import edu.upc.pes.wallachange.Models.Element;
import edu.upc.pes.wallachange.Models.User;


/**
 * Created by carlota on 7/6/17.
 */

public class MainChatFragment extends Fragment {
    private Activity myActivity;
    private View view;
    private CurrentUser currentUser;
    private AdapterAPIRequest adapterAPI;
    private ArrayList<Conversa> convs;
    private ListView listconvs;
    private ConversesAdapter conversesAdapter;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main_chat, container, false);
        myActivity = (MainActivity) getActivity();
        myActivity.setTitle(R.string.navigationProfile_eng);
        currentUser = CurrentUser.getInstance();


        adapterAPI = new AdapterAPIRequest();

        getConverses(currentUser.getToken(), currentUser.getId());

        Conversa provaconv = new Conversa();
        provaconv.setId_owner(currentUser.getId());
        provaconv.setId_other("id de laltre");
        provaconv.setConv_id("jsdfa");
        provaconv.setElem1("elemMeu");
        provaconv.setElem2("elemSeu");

        convs = new ArrayList<Conversa>();
        convs.add(provaconv);
        conversesAdapter = new ConversesAdapter(myActivity, R.layout.item_converses, convs);
        listconvs = (ListView) view.findViewById(R.id.listConverses);
        listconvs.setAdapter(conversesAdapter);
        conversesAdapter.notifyDataSetChanged();


        listconvs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                onClickConversa(i);
            }
        });

        return view;
    }

    private void onClickConversa (int i) {
//        myActivity.changeToChat(convs.get(i));
    }

    public void getUsername(String id, final Conversa con) {
        Map<String, String> headers = new HashMap<>();

        adapterAPI.GETRequestAPI("/user/"+id,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String username = response.getString("nom");
                            con.setNomUserOther(username);
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
                },
                headers
        );
    }

    public void getElement1Name(String id, final Conversa con) {

        Map<String, String> headers = new HashMap<>();
        headers.put("x-access-token", currentUser.getToken());
        headers.put("Content-Type", "application/json");
            //adapterAPIRequest.GETRequestAPI("http://104.236.98.100:3000/element/".concat(id),
            adapterAPI.GETRequestAPI("/api/element/"+id,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String nomElem1 = response.getString("titol");
                                con.setNomElem1(nomElem1);
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }


                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            VolleyLog.d(TAG, "Error: " + error.getMessage());
                        }
                    }, headers);

    }

    public void getElement2Name(String id, final Conversa con) {

        Map<String, String> headers = new HashMap<>();
        headers.put("x-access-token", currentUser.getToken());
        headers.put("Content-Type", "application/json");
        //adapterAPIRequest.GETRequestAPI("http://104.236.98.100:3000/element/".concat(id),
        adapterAPI.GETRequestAPI("/api/element/"+id,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String nomElem2 = response.getString("titol");
                            con.setNomElem2(nomElem2);
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                    }
                }, headers);

    }
    public void getConverses(String token, String id) {
        Map<String, String> headers = new HashMap<>();
        headers.put("x-access-token", token);
        adapterAPI.GETJsonArrayRequestAPI("/chat/conversations/" + id,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            Log.i("resposta: ",response.toString());
                            ArrayList<Conversa> aux = new ArrayList<> ();
                            for (int i = 0;i < response.length();++i) {
                                JSONObject var = response.getJSONObject(i);
                                //TODO: obtenir els parametres
                                currentUser.addConversa(var.getString("user_id"), "asf");
//                                getElement1Name(elem1, var);
//                                getElement2Name(elem2, var);
//                                getUsername(user_id, var);
                            }
                            inflateConverses();
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
    }

    public void inflateConverses() {
        convs = currentUser.getConverses();
        listconvs.setAdapter(conversesAdapter);
        conversesAdapter.notifyDataSetChanged();
    }

}
