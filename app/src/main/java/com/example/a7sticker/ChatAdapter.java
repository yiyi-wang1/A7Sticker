package com.example.a7sticker;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.annotation.NonNull;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatHolder>{
    private List<Message> messageList;
    private Context mContext;

    public ChatAdapter(List<Message> messageList, Context mContext) {
        this.messageList = messageList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ChatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
        return new ChatHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatHolder holder, int position) {
        mContext = mContext.getApplicationContext();
        Message currentMessage = messageList.get(position);
        Date date = currentMessage.getTimeStamp();
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
        String strDate = formatter.format(date);
        holder.time.setText(strDate);
        holder.sticker.setImageBitmap(Utils.getSticker(mContext, currentMessage.getStickerId()));
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }
}

class ChatHolder extends RecyclerView.ViewHolder {

    public TextView time;
    public ImageView sticker;

    public ChatHolder(View stickerView){
        super(stickerView);
        time = stickerView.findViewById(R.id.sendTime);
        sticker = stickerView.findViewById(R.id.sticker);
    }
}


