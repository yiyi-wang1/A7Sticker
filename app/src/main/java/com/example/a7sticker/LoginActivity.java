package com.example.a7sticker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    Button btn_login;
    EditText txt_username;
    String username;
    String userToken;
    FirebaseDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mDatabase = FirebaseDatabase.getInstance();
        btn_login = findViewById(R.id.btn_login);
        txt_username = findViewById(R.id.txt_username);

        //GET FCM TOKEN FOR FIRST LOGIN
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(
                new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Utils.postToastMessage("Fetching FCM registration token failed", getApplicationContext());
                            return;
                        }
                        // Get new FCM token
                        userToken = task.getResult();
                        Utils.postToastMessage("Fetched token is:"+ userToken, getApplicationContext());
                    }
                });

        //CLICK LOGIN TO MAIN PAGE
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = txt_username.getText().toString();
                if (username == null || username.length()< 1){
                    Utils.postToastMessage("Please enter a username", getApplicationContext());
                }else if(userToken == null){
                    Utils.postToastMessage("Fetching FCM registration token failed", getApplicationContext());
                }else{
                    registerUser(username);
                    Intent intent = new Intent(LoginActivity.this, MessageActivity.class);
                    intent.putExtra("username", username);
                    startActivity(intent);
                }
            }
        });
    }

    private void registerUser(String username){
        User user;
        user = new User(username,userToken);
        Task<Void> t = mDatabase.getReference().child("users").child(user.username).setValue(user);
        t.addOnCompleteListener(task -> {
            if(!t.isSuccessful()){
                Utils.postToastMessage("Login failed", getApplicationContext());
            } else {
                Utils.postToastMessage("Login as " + username, getApplicationContext());
            }
        });
    }
}