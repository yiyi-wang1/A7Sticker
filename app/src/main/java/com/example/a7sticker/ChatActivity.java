package com.example.a7sticker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private TextView friendName;
    private String sender;
    private String user;
    private List<Message> messageList;
    private ChatAdapter chatAdapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager rLayoutManger;
    private FirebaseDatabase mDatabase;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        sender = getIntent().getExtras().getString("Sender");
        user = getIntent().getExtras().getString("username");

        //get list of message from database
        friendName = findViewById(R.id.friendName_chat);
        friendName.setText(sender);
        messageList = new ArrayList<>();

        mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = mDatabase.getReference();
        //get the list received by current user
        reference.child("messages").orderByChild("receiver").equalTo(user).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageList.clear();
                if (snapshot.hasChildren()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Message message = dataSnapshot.getValue(Message.class);
                        if (message.getSender().equals(sender)){
                            messageList.add(message);
                        }
                    }

                    messageList.sort(new Comparator<Message>() {
                        @Override
                        public int compare(Message message, Message t1) {
                            return message.timeStamp.compareTo(t1.timeStamp);
                        }
                    });
                    createView();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void createView(){
        rLayoutManger = new LinearLayoutManager(ChatActivity.this);
        recyclerView = findViewById(R.id.recyclerView_chat);
        chatAdapter = new ChatAdapter(messageList, ChatActivity.this);
        recyclerView.setAdapter(chatAdapter);
        recyclerView.setLayoutManager(rLayoutManger);
    }


}