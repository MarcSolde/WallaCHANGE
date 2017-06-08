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

import java.lang.reflect.Array;
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
    private MainActivity myActivity;
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
        provaconv.setNomUserOther("id de laltre");
        provaconv.setLastMessage("jsdfa");
        provaconv.setNomElem1("elemMeu");
        provaconv.setNomElem2("elemSeu");
        Conversa provaconv2 = new Conversa();
        provaconv2.setId_owner(currentUser.getId());
        provaconv2.setNomUserOther("id de laltre");
        provaconv2.setLastMessage("jsdfa");
        provaconv2.setNomElem1("elemMeu");
        provaconv2.setNomElem2("elemSeu");
        provaconv2.setConfirmat();

        convs = new ArrayList<Conversa>();
        listconvs = (ListView) view.findViewById(R.id.listConverses);
        conversesAdapter = new ConversesAdapter(myActivity, R.layout.item_converses, convs);
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
        Conversa c = convs.get(i);
        myActivity.changeToChat(c.getConv_id());
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


//    Get user’s intercanvis
//    GET http://localhost:3000/intercanvi/user/user_id
//    body: -
//    header: x-access-token
//return: array of a user’s intercanvis (sense missatges)
    public void getConverses(String token, final String id) {
        Map<String, String> headers = new HashMap<>();
        headers.put("x-access-token", token);
        adapterAPI.GETJsonArrayRequestAPI("/intercanvi/user/" + id,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            Log.i("resposta: ",response.toString());
                            ArrayList<Conversa> aux = new ArrayList<> ();
                            for (int i = 0;i < response.length();++i) {
                                JSONObject var = response.getJSONObject(i);
                                Conversa c = new Conversa();
                                c.setConv_id(var.getString("idIntercanvi"));
                                String prov = var.getString("id1");
                                if (prov.equals(id)) {
                                    c.setId_owner(prov);
                                    c.setId_other(var.getString("id2"));
                                    c.setElem1(var.getString("idProd1"));
                                    c.setElem2(var.getString("idProd2"));
                                    getUsername("id2", c);
                                    getElement1Name("idProd1", c);
                                    getElement2Name("idProd2", c);
                                }
                                else {
                                    c.setId_owner(var.getString("id2"));
                                    c.setId_other(prov);
                                    c.setElem1(var.getString("idProd2"));
                                    c.setElem2(var.getString("idProd1"));
                                    getUsername(prov, c);
                                    getElement1Name("idProd2", c);
                                    getElement2Name("idProd1", c);
                                }
                                aux.add(c);
                            }
                            inflateConverses(aux);
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

    public void inflateConverses(ArrayList<Conversa> cs) {
        convs = cs;
        listconvs.setAdapter(conversesAdapter);
        conversesAdapter.notifyDataSetChanged();
    }

}
