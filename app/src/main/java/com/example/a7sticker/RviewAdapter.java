package com.example.a7sticker;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RviewAdapter extends RecyclerView.Adapter<RviewHolder>{
    private List<String> senderList;
    private Context mContext;
    private String user;

    public RviewAdapter(List<String> senderList, Context mContext, String user) {
        this.senderList = senderList;
        this.mContext = mContext;
        this.user = user;
    }

    @NonNull
    @Override
    public RviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sender_list, parent, false);
        return new RviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RviewHolder holder, int position) {
        String currentSender = senderList.get(position);
        holder.UserName.setText(currentSender);

        //If clicks on the user name, see all the message from the user
        holder.UserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ChatActivity.class);
                intent.putExtra("Sender", currentSender);
                intent.putExtra("username", user);
                mContext.startActivity(intent);
            }
        });

        //If clicks on the send button, send sticker back to user
        holder.sendSticker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, SendStickerActivity.class);
                intent.putExtra("receiver", currentSender);
                intent.putExtra("username", user);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return senderList.size();
    }
}

class RviewHolder extends RecyclerView.ViewHolder{
    public TextView UserName;
    public Button sendSticker;

    public RviewHolder(View senderView){
        super(senderView);
        UserName = senderView.findViewById(R.id.friends);
        sendSticker = senderView.findViewById(R.id.btn_send);
    }
}