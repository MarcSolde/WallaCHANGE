package edu.upc.pes.wallachange.Adapters;

import android.content.Context;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.vision.barcode.Barcode;

import edu.upc.pes.wallachange.Models.Message;
import edu.upc.pes.wallachange.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import edu.upc.pes.wallachange.ProfileFragment;

/**
 * Created by carlota on 3/6/17.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {

    private ArrayList<Message> messages;

    public ChatAdapter(ArrayList<Message> messages) {
        this.messages = messages;
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {

        final int itemType = getItemViewType(i);
        myViewHolder.message.setText(messages.get(i).getMessage());
        myViewHolder.time.setText(messages.get(i).getTime());
    }

    @Override
    public int getItemViewType(int position) {
        Message mess = messages.get(position);
        if (mess.getOwner() == 1){
            return 1;
        }
        else {
            return 0;
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        MyViewHolder holder;
        if(viewType==0){
            view=LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_response,parent,false);
        }
        else{
            view=LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item,parent,false);
        }
        return new MyViewHolder(view);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        protected TextView message;
        protected TextView time;


        public MyViewHolder(View v) {
            super(v);
            message = (TextView) v.findViewById(R.id.TextViewChat);
            time = (TextView) v.findViewById(R.id.timeTextView);

        }
    }
    }

