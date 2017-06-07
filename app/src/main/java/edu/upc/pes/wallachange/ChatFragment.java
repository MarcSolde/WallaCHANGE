package edu.upc.pes.wallachange;

import static com.android.volley.VolleyLog.TAG;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import edu.upc.pes.wallachange.APILayer.AdapterAPIRequest;
import edu.upc.pes.wallachange.Adapters.ChatAdapter;
import edu.upc.pes.wallachange.Adapters.PreferencesAdapter;
import edu.upc.pes.wallachange.Adapters.UserListAdapter;
import edu.upc.pes.wallachange.Models.Conversa;
import edu.upc.pes.wallachange.Models.CurrentUser;
import edu.upc.pes.wallachange.Models.Message;
import edu.upc.pes.wallachange.Models.User;

/**
 * Created by carlota on 3/6/17.
 */

public class ChatFragment extends Fragment implements View.OnClickListener {

    private Activity myActivity;
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view;
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        view = inflater.inflate(R.layout.fragment_chat_layout, container, false);
        myActivity = (MainActivity) getActivity();
        currentUser = CurrentUser.getInstance();

        recview = (RecyclerView) view.findViewById(R.id.recyclerViewChat);
        editTextChat = (EditText) view.findViewById(R.id.editTextChat);
        buttonSendChat = (ImageView) view.findViewById(R.id.buttonSendChat);

        messages = new ArrayList<Message>();

        LinearLayoutManager llm = new LinearLayoutManager(myActivity);
        llm.setStackFromEnd(true);
        recview.setLayoutManager(llm);

        chatAdapter = new ChatAdapter(messages);
        recview.setAdapter(chatAdapter);
        chatAdapter.notifyDataSetChanged();

        editTextChat.setOnClickListener(this);
        buttonSendChat.setOnClickListener(this);

        conv_id = getArguments().getString("conversa");
        Conversa conversa = currentUser.getConversa(conv_id);
        alterUserId = conversa.getId_other();
        otherUserName = conversa.getNomUserOther();
        myElementId = conversa.getElem1();
        yourElementId = conversa.getElem2();
        myElementName = conversa.getNomElem1();
        yourElementName = conversa.getNomElem2();
        getConversa();

//        getAlterUser(alterUserId);

//        getConverses(currentUser.getToken(), currentUser.getId());


//        conv_id = currentUser.existsConversa(alterUserId);
//        ArrayList<Conversa> auxi = currentUser.getConverses();
//        if (auxi.size() == 0) {
//            Log.i("size", "buit");
//        }
//        for (Conversa c : auxi) {
//            Log.i("convid", c.getConv_id());
//            Log.i("idowner", c.getId_owner());
//            Log.i("idother", c.getId_other());
//        }
//        Log.i("conv_id", conv_id);
//        if (conv_id.equals("no")) {
//            existeix = false;
//        }
//        else {
//            existeix = true;
//            Log.i("alter id", conv_id);
//            getConversa();
//        }
        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.editTextChat:
                break;
            case R.id.buttonSendChat:
                String textMessage = editTextChat.getText().toString();
                if (!textMessage.equals("")) {
//                    Message mes6 = new Message(textMessage, 1, getTime());
//                    if (!existeix) {
//                        crearConversa(alterUserId, mes6, currentUser.getId());
//                    }
//                    else {
                    Message mes6 = new Message(textMessage, currentUser.getId());
                    sendMissage(mes6);
                    editTextChat.setText("");
                }
                else {
                    String errorEmpty = getResources().getString(R.string.errorEmptyField_eng);
                    editTextChat.setError(errorEmpty);
                }
                break;
        }
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_fragment_chat, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.do_interchange:
                // do s.th.
                return true;
            case R.id.see_element:
                return true;
            case R.id.cancel_interchange:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
//    android:id="@+id/do_interchange"
//    android:title="@string/do_interchange"/>
//    <item
//    android:id="@+id/see_element"
//    android:title="@string/See_its_elements"/>
//    <item
//    android:id="@+id/cancel_interchange"



    public void setTitle(String string) {
        myActivity.setTitle(string);
    }

    public void getConversa() {
        Log.i("crida getConversa", alterUserId);
        Map<String, String> headers2 = new HashMap<>();
        headers2.put("x-access-token", currentUser.getToken());
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
                headers2
        );
    }


    public void sendMissage(final Message mes) {
        Log.i("crida sendMissage", mes.getMessage());
        Log.i("id conversa", conv_id);
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
//                        JSONObject js = response;
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

    public void crearConversa(final String alterUserId, final Message mes, final String ownId) {
        Log.i("crida crearConversa", mes.getMessage());
        JSONObject body2 = new JSONObject();
        try {
            body2.put("message", mes.getMessage());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Map<String, String> headers3 = new HashMap<>();
        headers3.put("x-access-token", currentUser.getToken());
        ///chat/?from:user_id&to:user_id
        adapterAPI.POSTRequestAPI(
                "/chat?from=" + ownId + "&to=" + alterUserId,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject js = response;
                        try {
                            Log.i("owner", ownId);
                            Log.i("alter", alterUserId);
                            conv_id = response.getString("conversation_id");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        existeix = true;
                        currentUser.addConversa(alterUserId, conv_id);
                        ArrayList<Message> arraymes = new ArrayList<Message>();
                        arraymes.add(mes);
                        loadMessages(arraymes);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                    }
                }, body2, headers3);
    }



    public void loadMessages(ArrayList<Message> ar ) {
        messages = new ArrayList<>();
        messages = ar;

        chatAdapter = new ChatAdapter(messages);
        recview.setAdapter(chatAdapter);
        chatAdapter.notifyDataSetChanged();
        recview.scrollToPosition(messages.size() - 1);
    }





}

//    public void getAlterUser(String alterUserId) {
//        Log.i("crida obtenirUser", alterUserId);
//        Map<String, String> headers1 = new HashMap<>();
//
//        adapterAPI = new AdapterAPIRequest();
//        final User u = new User();
//        adapterAPI.GETRequestAPI("/user/"+alterUserId,
//                new Response.Listener<JSONObject>() {
//
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        try {
//                            Log.i("JSONbooo ",response.toString());
//                            u.setUsername(response.getString("nom"));
//                            u.setId(response.getString("id"));
//                            setTitle(u.getUsername());
//                        }
//                        catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.i("JSONerror: ","");
//                    }
//                },
//                headers1
//        );
//
//    }

//    public String getTime() {
//        c = Calendar.getInstance();
//        String time;
//        int hour = c.get(Calendar.HOUR_OF_DAY);
//        int min = c.get(Calendar.MINUTE);
//        if (min < 10) {
//            time = hour + ".0" + min;
//        }
//        else time = hour + "." + min;
//        return time;
//    }

//    public void getConverses(String token, String id) {
//        Map<String, String> headers = new HashMap<>();
//        headers.put("x-access-token", token);
//        adapterAPI.GETJsonArrayRequestAPI("/chat/conversations/" + id,
//                new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        try {
//                            Log.i("resposta: ",response.toString());
//                            ArrayList<Conversa> aux = new ArrayList<> ();
//                            for (int i = 0;i < response.length();++i) {
//                                JSONObject var = response.getJSONObject(i);
//                                currentUser.addConversa(var.getString("user_id"), "asf");
//                            }
//                        }
//                        catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.i("JSONerror: ","");
//                    }
//                }, headers
//        );
//    }