package edu.upc.pes.wallachange;

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

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view;
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        view = inflater.inflate(R.layout.fragment_chat_layout, container, false);
        myActivity = (MainActivity) getActivity();
        CurrentUser currentUser = CurrentUser.getInstance();


        String alterUserId = getArguments().getString("alterUserId");
        Map<String, String> headers = new HashMap<>();


        final User u = new User();
        adapterAPI.GETRequestAPI("/user/"+alterUserId,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.i("JSON: ",response.toString());
                            JSONArray var2 = response.getJSONArray("preferencies");
                            ArrayList<String> aux2 = new ArrayList<> ();
                            for (int j = 0; j < var2.length();++j) {
                                aux2.add(var2.get(j).toString());
                            }
                            u.setUsername(response.getString("nom"));
                            u.setId(response.getString("id"));
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
        String username = u.getUsername();
        myActivity.setTitle(username);

        recview = (RecyclerView) view.findViewById(R.id.recyclerViewChat);
        editTextChat = (EditText) view.findViewById(R.id.editTextChat);
        buttonSendChat = (ImageView) view.findViewById(R.id.buttonSendChat);

        messages = new ArrayList<Message>();


        LinearLayoutManager llm = new LinearLayoutManager(myActivity);
        llm.setStackFromEnd(true);
//        llm.setReverseLayout(true);
        recview.setLayoutManager(llm);

        Message mes = new Message("hola", 1, getTime());
        messages.add(mes);
        Message mes5 = new Message("ei!!", 0, getTime());
        messages.add(mes5);
        Message mes2 = new Message("k tal", 1, getTime());
        messages.add(mes2);
        Message mes3 = new Message("b i tu", 0, getTime());
        messages.add(mes3);
        Message mes4 = new Message("tb", 1, getTime());
        messages.add(mes4);

//        messages =;





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
                    Log.i("time", mes6.getTime());
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




}
