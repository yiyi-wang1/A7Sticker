package com.example.a7sticker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
import java.util.List;

public class MessageActivity extends AppCompatActivity {

    String username;
    List<String> userList;
    Button btn_viewStickers;
    TextView txt_username;
    private RecyclerView recyclerView;
    private RviewAdapter rviewAdapter;
    private RecyclerView.LayoutManager rLayoutManger;
    private FirebaseDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);
        username = null;

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            Log.v("Empty state", " this is an error");
            if(extras == null) {
                Log.v("Error", " this is an error");
            } else {
                username = extras.getString("username");
            }
        }else{
            username = savedInstanceState.getString("username");
        }

        btn_viewStickers = findViewById(R.id.btn_sticker);
        txt_username = findViewById(R.id.txt_username2);
        mDatabase = FirebaseDatabase.getInstance();

        //Set the username to currentUser
        txt_username.setText(username);

        //Get all user and store into userList
        userList = new ArrayList<>();
        DatabaseReference reference = mDatabase.getReference().child("users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();//clear the list first
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                   String Username = snapshot.getValue(User.class).getUsername();
                   //Skip the current user
                   if(Username.equals(username)){
                       continue;
                   }
                    userList.add(Username);
                }
                //Create the recycle view for all user
                rLayoutManger = new LinearLayoutManager(MessageActivity.this);
                recyclerView = findViewById(R.id.userlist);
                recyclerView.setLayoutManager(rLayoutManger);
                //After the list is generated, set the view
                rviewAdapter = new RviewAdapter(userList,MessageActivity.this, username);
                recyclerView.setAdapter(rviewAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        btn_viewStickers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MessageActivity.this, StickerHistoryActivity.class);
                intent.putExtra("user",username);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("username",username);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        username = savedInstanceState.getString("username");
    }
}
