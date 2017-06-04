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

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view;
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        view = inflater.inflate(R.layout.fragment_chat_layout, container, false);
        myActivity = (MainActivity) getActivity();
        currentUser = CurrentUser.getInstance();


        alterUserId = getArguments().getString("alterUserId");
        Map<String, String> headers = new HashMap<>();

        //TODO: mirar a bd si existeix conversa. Si existeix, obtenir-la. Sino, crearne una de nova en el moment de enviar un missatge

        existeix = false;

        adapterAPI = new AdapterAPIRequest();
        final User u = new User();
        adapterAPI.GETRequestAPI("/user/"+alterUserId,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.i("JSONbooo ",response.toString());
                            u.setUsername(response.getString("nom"));
                            u.setId(response.getString("id"));
                            setTitle(u.getUsername());
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
                    Message mes6 = new Message(textMessage, 1, getTime());

                    //TODO: mirar a bd si existeix conversa. si no exissteix crearne una de nova aqui
                    if (!existeix) {
                        JSONObject body = new JSONObject();
                        try {
                            body.put("message", mes6.getMessage());
                            body.put("author", currentUser.getId());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Map<String, String> headers = new HashMap<>();
                        headers.put("x-access-token", currentUser.getToken());
                        adapterAPI.POSTRequestAPI(
                                "/chat/?from:" + currentUser.getId() + "&to:" + alterUserId,
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
                                }, body, headers);


                        //                    POST http://localhost:3000/chat/?from:user_id&to:user_id

                        //                    body: JSON: {“message”: “missatge”}
                        //                    header: x-access-token
                        //                    return: conversation.id
                    }
                    else {


//                    Send message
//                    POST http://localhost:3000/chat/conversation_id
//                    body: JSON: {“message”: “missatge”, “author”: “user_id”}
//                    header: x-access-token
//                    return: empty
//
//                    Get conversation
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("message", mes6.getMessage());
                            jsonObject.put("author", currentUser.getId());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Map<String, String> headers = new HashMap<>();
                        headers.put("x-access-token", currentUser.getToken());

                        //TODO: crida x enviar missatge
//                    adapterAPI.POSTRequestAPI("/chat/" );
                    }
                    messages.add(mes6);
                    chatAdapter.notifyDataSetChanged();
                    recview.scrollToPosition(messages.size() - 1);
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
//         handle item selection
        switch (item.getItemId()) {
            case R.id.see_elements_user:
                // do s.th.
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public String getTime() {
        c = Calendar.getInstance();
        String time;
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int min = c.get(Calendar.MINUTE);
        if (min < 10) {
            time = hour + ".0" + min;
        }
        else time = hour + "." + min;
        return time;
    }

    public void setTitle(String string) {
        myActivity.setTitle(string);
    }




}
