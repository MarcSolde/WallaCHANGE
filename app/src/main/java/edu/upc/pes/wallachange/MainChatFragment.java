package edu.upc.pes.wallachange;


import static com.android.volley.VolleyLog.TAG;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
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
        myActivity.setTitle(R.string.navigationChat_eng);
        currentUser = CurrentUser.getInstance();


        adapterAPI = new AdapterAPIRequest();

        getConverses(currentUser.getToken(), currentUser.getId());

        convs = new ArrayList<Conversa>();
        listconvs = (ListView) view.findViewById(R.id.listConverses);
        conversesAdapter = new ConversesAdapter(myActivity, R.layout.item_converses, convs);
        listconvs.setAdapter(conversesAdapter);
        conversesAdapter.notifyDataSetChanged();

        listconvs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Conversa c = convs.get(i);
                myActivity.changeToChat(c.getConv_id());
            }
        });

        listconvs.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                    final int pos, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                builder.setTitle(getResources().getString(R.string.wanna_delete));

                builder.setPositiveButton(R.string.yes,  new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Conversa c = convs.get(pos);
                        String conv_id = c.getConv_id();
                        Map<String,String> headers2 = new HashMap<String, String>();
                        headers2.put("x-access-token", currentUser.getToken());
                        adapterAPI.DELETERequestAPI("/chat/" + conv_id,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    convs.remove(pos);
                                    conversesAdapter.notifyDataSetChanged();
                                 }
                            },new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    VolleyLog.d(TAG,error.getMessage());
                                }
                            }, headers2);
                    }
                });
                builder.setNegativeButton(R.string.no,  new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog2 = builder.create();
                dialog2.show();

                return true;
            }
        });




        return view;
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
                            conversesAdapter.notifyDataSetChanged();
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

            adapterAPI.GETRequestAPI("/api/element/".concat(id),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String nomElem = response.getString("titol");
                                con.setNomElem1(nomElem);
                                conversesAdapter.notifyDataSetChanged();
                            } catch (JSONException e) {
                                e.printStackTrace();
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
        adapterAPI.GETRequestAPI("/api/element/".concat(id),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String nomElem = response.getString("titol");
                            con.setNomElem2(nomElem);
                            conversesAdapter.notifyDataSetChanged();
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
                                c.setConfirmat(var.getBoolean("confirmat"));
                                c.setCancelat(var.getBoolean("acceptat"));
                                String prov = var.getString("id1");
                                if (prov.equals(id)) {
                                    c.setId_owner(prov);
                                    c.setId_other(var.getString("id2"));
                                    c.setElem1(var.getString("idProd1"));
                                    c.setElem2(var.getString("idProd2"));
                                    getUsername(c.getId_other(), c);
                                    getElement1Name(c.getElem1(), c);
                                    getElement2Name(c.getElem2(), c);
                                }
                                else {
                                    c.setId_owner(var.getString("id2"));
                                    c.setId_other(prov);
                                    c.setElem1(var.getString("idProd2"));
                                    c.setElem2(var.getString("idProd1"));
                                    getUsername(prov, c);
                                    getElement1Name(c.getElem2(), c);
                                    getElement2Name(c.getElem1(), c);
                                }
                                convs.add(c);
//                                aux.add(c);
                            }
//                            inflateConverses(convs);
                            conversesAdapter.notifyDataSetChanged();

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
//TODO
//    Map<String,String> headers2 = new HashMap<String, String>();
//                        headers2.put("x-access-token", currentUser.getToken());
//                        adapterAPI.DELETERequestAPI("/chat/" + conv_id,
//            new Response.Listener<JSONObject>() {
//        @Override
//        public void onResponse(JSONObject response) {
//
//        }
//    },new Response.ErrorListener() {
//        @Override
//        public void onErrorResponse(VolleyError error) {
//            VolleyLog.d(TAG,error.getMessage());
//        }
//    }, headers2);
//
//    public void inflateConverses(ArrayList<Conversa> cs) {
////        convs = cs;
//        listconvs.setAdapter(conversesAdapter);
//        conversesAdapter.notifyDataSetChanged();
//    }

}
