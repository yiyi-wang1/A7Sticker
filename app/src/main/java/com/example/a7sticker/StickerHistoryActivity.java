package com.example.a7sticker;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StickerHistoryActivity extends AppCompatActivity {

    private String username;
    List<Message> sentList;
    List<SentSticker> stickerList;
    private RecyclerView recyclerView;
    private StickerAdapter stickerAdapter;
    private RecyclerView.LayoutManager rLayoutManger;
    private FirebaseDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sent_sticker);

        //Get the current username
        username = getIntent().getExtras().getString("user");

        //Get the sticker that sent by user
        sentList = new ArrayList<>();
        stickerList = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = mDatabase.getReference();
        reference.child("messages").orderByChild("sender").equalTo(username).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        sentList.clear();
                        if (snapshot.hasChildren()) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                Message message = dataSnapshot.getValue(Message.class);
                                sentList.add(message);
                            }
                            CountSticker(sentList);
                            //Create the recycle view
                            creatView();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
    }

    private void creatView(){
        rLayoutManger = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.sent_sticker_list);
        stickerAdapter = new StickerAdapter(this,stickerList);
        recyclerView.setAdapter(stickerAdapter);
        recyclerView.setLayoutManager(rLayoutManger);
    }

    private void CountSticker(List<Message> sentList) {
        stickerList.clear();
        Map<Integer, Integer> countByStickerId = new HashMap<>();
        for (Message message : sentList) {
            countByStickerId.put(message.stickerId, countByStickerId.getOrDefault(message.stickerId, 0) + 1);
        }
        for (Map.Entry<Integer, Integer> entry : countByStickerId.entrySet()) {
            Integer stickerID = entry.getKey();
            Integer count = entry.getValue();
            stickerList.add(new SentSticker(stickerID, count));
        }
    }
}