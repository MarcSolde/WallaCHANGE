package edu.upc.pes.wallachange;

import android.app.Activity;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import edu.upc.pes.wallachange.Adapters.ChatAdapter;
import edu.upc.pes.wallachange.Adapters.PreferencesAdapter;

/**
 * Created by carlota on 3/6/17.
 */

public class ChatFragment extends Fragment {

    private Activity myActivity;
    private ArrayList<String> messages;
    private ChatAdapter chatAdapter;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.fragment_chat_layout, container, false);
        myActivity = (MainActivity) getActivity();


        RecyclerView recview = (RecyclerView) view.findViewById(R.id.recyclerViewChat);
        messages = new ArrayList<String>();
        messages.add("hola");
        messages.add("k tal");
        messages.add("eo k k tal");

//        messages =;
        chatAdapter = new ChatAdapter(messages);
        recview.setAdapter(chatAdapter);
        chatAdapter.notifyDataSetChanged();


        return view;
    }
}
