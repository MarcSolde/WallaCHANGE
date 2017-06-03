package edu.upc.pes.wallachange.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.vision.barcode.Barcode;

import edu.upc.pes.wallachange.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.upc.pes.wallachange.ProfileFragment;

/**
 * Created by carlota on 3/6/17.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {

    private List<String> messages;

    public ChatAdapter(List<String> messages) {
        this.messages = messages;
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
        myViewHolder.message.setText(messages.get(i));
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.chat_item, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        protected TextView message;


        public MyViewHolder(View v) {
            super(v);
            message = (TextView) v.findViewById(R.id.TextViewChat);

        }
    }
    }

