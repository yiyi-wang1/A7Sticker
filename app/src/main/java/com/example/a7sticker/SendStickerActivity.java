package com.example.a7sticker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

public class SendStickerActivity extends AppCompatActivity {
    private String currentUser;
    private String receiver;
    private FirebaseDatabase mDatabase;
    private static final String TAG = "FCMActivity";
    private static String SERVER_KEY;
    private static String RECEIVER_REGISTRATION_TOKEN;
    private ImageView stickerView1;
    private ImageView stickerView2;
    private ImageView stickerView3;
    private ImageView stickerView4;
    private int stickerId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_sticker);
        receiver = getIntent().getExtras().getString("receiver");
        currentUser = getIntent().getExtras().getString("username");
        mDatabase = FirebaseDatabase.getInstance();
        SERVER_KEY = "key=" + Utils.getProperties(getApplicationContext()).getProperty("SERVER_KEY");
        stickerId = 0;

//        Log.v("test", SERVER_KEY);

        //Set the view
        stickerView1 = findViewById(R.id.sticker_1);
        stickerView2 = findViewById(R.id.sticker_2);
        stickerView3 = findViewById(R.id.sticker_3);
        stickerView4 = findViewById(R.id.sticker_4);

        //Load the sticker
        stickerView1.setImageBitmap(Utils.getSticker(this, 1));
        stickerView2.setImageBitmap(Utils.getSticker(this, 2));
        stickerView3.setImageBitmap(Utils.getSticker(this, 3));
        stickerView4.setImageBitmap(Utils.getSticker(this, 4));

        stickerView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stickerId = 1;
                sendSticker(currentUser,receiver,stickerId);
            }
        });

        stickerView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stickerId = 2;
                sendSticker(currentUser,receiver,stickerId);
            }
        });

        stickerView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stickerId = 3;
                sendSticker(currentUser,receiver,stickerId);
            }
        });

        stickerView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stickerId = 4;
                sendSticker(currentUser,receiver,stickerId);
            }
        });
    }

    private void sendSticker(String currentUser, String receiver, int stickerId){
        //check the parameters
        if(currentUser == null || receiver == null || stickerId == 0 || currentUser == "" || receiver == ""){
            Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT);
        }
        //get the token for receiver
        mDatabase.getReference().child("users").child(receiver).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User receiverUser = snapshot.getValue(User.class);
                if (receiverUser == null) {
                    Utils.postToastMessage(String.format("Receiver %s doesn't exist", receiver),
                            getApplicationContext());
                    return;
                }
                if (receiverUser.token.isEmpty()) {
                    Utils.postToastMessage(
                            String.format("Receiver %s doesn't have a registered device", receiver),
                            getApplicationContext());
                    return;
                }

                String messageId = UUID.randomUUID().toString();
                Message message = new Message(messageId,currentUser, receiver,
                        new Date(System.currentTimeMillis()), stickerId);
                mDatabase.getReference().child("messages").child(messageId).setValue(message);
                RECEIVER_REGISTRATION_TOKEN = receiverUser.token;
                sendMessageToDevice(currentUser, receiver, stickerId,
                        receiverUser.token);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    //Send the message to receiver by using FCM notification service
    public void sendMessageToDevice(String currentUser, String receiver, int StickerId, String token) {
        JSONObject jPayload = new JSONObject();
        JSONObject jdata = new JSONObject();
        try {
            jdata.put("title", String.format("%s sends you a new message", currentUser));
            jdata.put("Sender", currentUser);
            jdata.put("stickerId", stickerId);
            jdata.put("receiver", receiver);

            // If sending to a single client
            jPayload.put("to", token);
            jPayload.put("priority", "high");
            jPayload.put("data", jdata);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                final String resp =Utils.fcmHttpConnection(SERVER_KEY, jPayload);
                Log.i(TAG, String.format("FCM Server response: %s", resp));
                try {
                    JSONObject responseJson = new JSONObject(resp);
                    if (responseJson.has("success") && responseJson.getInt("success") == 1) {
                        Utils.postToastMessage("Sticker sent successfully!", getApplicationContext());
                    } else {
                        Utils.postToastMessage("Sticker sent failed! " +
                                        responseJson.getJSONArray("results").getJSONObject(0).get("error"),
                                getApplicationContext());
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "error: " + e.toString());
                    Utils.postToastMessage("Sticker sent failed!", getApplicationContext());
                }
            }
        });
        t.start();
    }
}