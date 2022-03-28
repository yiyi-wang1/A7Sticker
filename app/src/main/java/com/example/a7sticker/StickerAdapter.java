package com.example.a7sticker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StickerAdapter extends RecyclerView.Adapter<StickerHolder> {
    private Context mContext;
    private List<SentSticker> list;

    public StickerAdapter(Context mContext, List<SentSticker> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @NonNull
    @Override
    public StickerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sticker_item, parent, false);
        return new StickerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StickerHolder holder, int position) {
        SentSticker currentsticker = list.get(position);
        holder.countView.setText(String.valueOf(currentsticker.getCount()));
        holder.image.setImageBitmap(Utils.getSticker(mContext,currentsticker.getStickerId()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

class StickerHolder extends RecyclerView.ViewHolder{
    public TextView countView;
    public ImageView image;

    public StickerHolder(@NonNull View itemView) {
        super(itemView);
        countView = itemView.findViewById(R.id.sticker_count);
        image = itemView.findViewById(R.id.sticker_image);
    }
}
