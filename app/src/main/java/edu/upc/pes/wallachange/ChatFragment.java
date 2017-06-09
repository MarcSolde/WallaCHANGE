package edu.upc.pes.wallachange;

import static com.android.volley.VolleyLog.TAG;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import edu.upc.pes.wallachange.APILayer.AdapterAPIRequest;
import edu.upc.pes.wallachange.Adapters.ChatAdapter;
import edu.upc.pes.wallachange.Models.Conversa;
import edu.upc.pes.wallachange.Models.CurrentUser;
import edu.upc.pes.wallachange.Models.Message;
import edu.upc.pes.wallachange.Models.User;


/**
 * Created by carlota on 3/6/17.
 */

public class ChatFragment extends Fragment implements View.OnClickListener {

    private MainActivity myActivity;
    private ChatAdapter chatAdapter;
    private EditText editTextChat;
    private ImageView buttonSendChat;
    private RecyclerView recview;
    private Calendar c;
    private ArrayList<Message> messages;
    private AdapterAPIRequest adapterAPI;
    private CurrentUser currentUser;
    private String alterUserId;
    private Boolean existeix;
    private String conv_id;
    private String myElementId;
    private String yourElementId;
    private String myElementName;
    private String yourElementName;
    private String otherUserName;
    private View dialogView;
    private Conversa conversa;
    private User otherUser;
    private Boolean confirmat;
    private Boolean cancelat;
    private View view;
    private com.github.nkzawa.socketio.client.Socket mSocket;
    {
        try {
            //mSocket = IO.socket("http://10.0.2.2:80/");
            mSocket = IO.socket("http://104.236.98.100:80/");
        } catch (URISyntaxException e) {}
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        conv_id = getArguments().getString("conversa");
//        mSocket.on("room", conv_id);
        mSocket.on("msg", onNewMessage);
        mSocket.connect();
        mSocket.emit("room", conv_id);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSocket.disconnect();
        mSocket.off("msg", onNewMessage);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        view = inflater.inflate(R.layout.fragment_chat_layout, container, false);
        dialogView = inflater.inflate(R.layout.custom_dialog_chat, null);

        myActivity = (MainActivity) getActivity();
        myActivity.setTitle(R.string.navigationChat_eng);
        currentUser = CurrentUser.getInstance();

        recview = (RecyclerView) view.findViewById(R.id.recyclerViewChat);
        editTextChat = (EditText) view.findViewById(R.id.editTextChat);
        buttonSendChat = (ImageView) view.findViewById(R.id.buttonSendChat);

        messages = new ArrayList<Message>();
        cancelat = false;
        confirmat = false;

        adapterAPI = new AdapterAPIRequest();

        LinearLayoutManager llm = new LinearLayoutManager(myActivity);
        llm.setStackFromEnd(true);
        recview.setLayoutManager(llm);

        chatAdapter = new ChatAdapter(messages);
        recview.setAdapter(chatAdapter);
        chatAdapter.notifyDataSetChanged();

        editTextChat.setOnClickListener(this);
        buttonSendChat.setOnClickListener(this);

        conv_id = getArguments().getString("conversa");
        Log.i("IDDDDDD", conv_id);
        getParamsConversa();
        getConversa();

        return view;
    }



    private void attemptSend() {
        String message = editTextChat.getText().toString().trim();
        if (TextUtils.isEmpty(message)) {
            return;
        }

        editTextChat.setText("");
        JSONObject js = new JSONObject();
        try {
            js.put("room", conv_id);
            js.put("msg", message);
            js.put("author", currentUser.getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        Message mes = new Message();
//        mes.setMessage(message);
//        mes.setOwner(currentUser.getId());
//        messages.add(mes);
//        loadMessages(messages);
        mSocket.emit("msg", js);
    }


    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String username;
                    String message;
                    try {
                        username = data.getString("author");
                        message = data.getString("msg");
                    } catch (JSONException e) {
                        return;
                    }
                    Log.i("message", message);
                    Message m = new Message();
                    m.setMessage(message);
                    m.setOwner(username);
                    messages.add(m);
                    chatAdapter.notifyDataSetChanged();
                    recview.scrollToPosition(messages.size() - 1);
//                    loadMessages(messages);
                }
            });
        }
    };


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.editTextChat:
                break;
            case R.id.buttonSendChat:
                attemptSend();
//                else {
//                    String errorEmpty = getResources().getString(R.string.errorEmptyField_eng);
//                    editTextChat.setError(errorEmpty);
//                }
                break;
        }
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_fragment_chat, menu);

    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        if (cancelat || confirmat) {
            menu.getItem(0).setEnabled(false); // here pass the index of save menu item
            menu.getItem(2).setEnabled(false); // here pass the index of save menu item

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        float rating;
        switch (item.getItemId()) {
            case R.id.do_interchange:
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity(), R.style.CustomAlertDialog);
                        alert.setView(dialogView);
                        RatingBar valoration = (RatingBar) dialogView.findViewById(R.id.ratingFinal);
                        rating = valoration.getRating();
                        alert.setPositiveButton("Ok",  new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Map<String,String> headers5 = new HashMap<>();
                                headers5.put("Content-Type", "application/json");
                                JSONObject body = new JSONObject();
                                try {
                                    body.put("token", currentUser.getToken());
                                    body.put("confirmat", true);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                adapterAPI.PUTRequestAPI("/intercanvi/" + conv_id,
                                        new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                JSONObject js = response;
                                                cancelat = false;
                                                confirmat = true;
                                                canviaFons(cancelat, confirmat);
                                            }
                                        },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                VolleyLog.d(TAG, "Error: " + error.getMessage());
                                            }
                                        }, body, headers5);
                            }
                        });

                AlertDialog dialog = alert.create();
                dialog.show();
                return true;
            case R.id.see_element:
                myActivity.changeToItemChat(yourElementId);


                return true;
            case R.id.cancel_interchange:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(getResources().getString(R.string.wanna_cancel));
                builder.setPositiveButton(R.string.yes,  new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Map<String,String> headers3 = new HashMap<>();
                        headers3.put("Content-Type", "application/json");
                        JSONObject body = new JSONObject();
                        try {
                            body.put("token", currentUser.getToken());
                            body.put("acceptat", true);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        adapterAPI.PUTRequestAPI("/intercanvi/" + conv_id,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        JSONObject js = response;
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                                    }
                                }, body, headers3);
                        cancelat = true;
                        myActivity.changeFragmentToHome();
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }




    public void setTitle(String string) {
        myActivity.setTitle(string);
    }

    public void getParamsConversa() {

        Map<String, String> headers = new HashMap<>();
        headers.put("x-access-token", currentUser.getToken());
        adapterAPI.GETRequestAPI("/intercanvi/" + conv_id,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String prov = response.getString("id1");
                            cancelat = response.getBoolean("acceptat");
                            confirmat = response.getBoolean("confirmat");
                            canviaFons(cancelat, confirmat);
                            if (prov.equals(currentUser.getId())) {
                                alterUserId = response.getString("id2");
                                myElementId = response.getString("idProd1");
                                yourElementId = response.getString("idProd2");
                            }
                            else {
                                alterUserId = response.getString("id1");
                                myElementId = response.getString("idProd2");
                                yourElementId = response.getString("idProd1");
                            }
                            getUsername(alterUserId);
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

//        GET http://localhost:3000/intercanvi/idIntercanvi
//        body: -
//                header: x-access-token
//        return: JSON de l’intercanvi amb id = idIntercanv
    }


    public void getConversa() {

        Map<String, String> headers = new HashMap<>();
        headers.put("x-access-token", currentUser.getToken());
        adapterAPI.GETJsonArrayRequestAPI("/chat/"+conv_id,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            ArrayList<Message> aux = new ArrayList<Message>();
                            messages.clear();
                            for (int i = 0; i < response.length(); ++i) {
                                JSONObject var = response.getJSONObject(i);
                                Message messvar = new Message();
                                messvar.setMessage(var.getString("body"));
                                if (var.getString("author").equals(currentUser.getId()))
                                messvar.setOwner(currentUser.getId());
                                else messvar.setOwner(alterUserId);
                                aux.add(messvar);
                            }
                            loadMessages(aux);
                        }
                        catch (JSONException e) {
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


    public void sendMissage(final Message mes) {
        JSONObject body3 = new JSONObject();
        try {
            body3.put("message", mes.getMessage());
            body3.put("author", currentUser.getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Map<String, String> headers4 = new HashMap<>();
        headers4.put("x-access-token", currentUser.getToken());
        adapterAPI.POSTRequestAPI("/chat/" + conv_id,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        messages.add(mes);
                        loadMessages(messages);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                    }
                }, body3, headers4);
    }





    public void loadMessages(ArrayList<Message> ar ) {
        messages = new ArrayList<>();
        messages = ar;

        chatAdapter = new ChatAdapter(messages);
        recview.setAdapter(chatAdapter);
        chatAdapter.notifyDataSetChanged();
        recview.scrollToPosition(messages.size() - 1);
    }


    public void getUsername(String alterUserId) {
        Map<String, String> headers1 = new HashMap<>();
        adapterAPI.GETRequestAPI("/user/"+alterUserId,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            setTitle(response.getString("nom"));

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
                headers1
        );

    }

    public void canviaFons(Boolean cancelat, Boolean confirmat) {
        RelativeLayout linlay = (RelativeLayout) view.findViewById(R.id.fondochat);
        if (cancelat) {
            linlay.setBackgroundColor(0xFFFFCCCC);
        }
        else if (confirmat) {
            linlay.setBackgroundColor(0xFFCBF9CE);
        }
    }





}