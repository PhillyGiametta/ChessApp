package com.example.chessapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.java_websocket.handshake.ServerHandshake;

import java.util.ArrayList;

public class In_Group extends AppCompatActivity implements WebSocketListener {
    String groupURL;
    String[] connectedUsers = {"","","",""};
    TextView player1;
    TextView player2;
    TextView player3;
    TextView player4;
    String serverUrl = "ws://coms-3090-050.class.las.iastate.edu:8080/group/test/e";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.found_group);

        player1 = findViewById(R.id.user1);
        player2 = findViewById(R.id.user2);
        player3 = findViewById(R.id.user3);
        player4 = findViewById(R.id.user4);

        WebSocketManager.getInstance().connectWebSocket(serverUrl);
        WebSocketManager.getInstance().setWebSocketListener(In_Group.this);
    }

    @Override
    public void onWebSocketOpen(ServerHandshake handshakedata) {

    }

    @Override
    public void onWebSocketMessage(String message) {
        Log.d("Websocket", message);
        String[] users = message.split(",");
        if(users.length == 2){
            player1.setText(users[1]);
        } else if (users.length == 3){
            player1.setText(users[1]);
            player2.setText(users[2]);
        } else if(users.length == 4){
            player1.setText(users[1]);
            player2.setText(users[2]);
            player3.setText(users[3]);
        } else if(users.length == 5){
            player1.setText(users[1]);
            player2.setText(users[2]);
            player3.setText(users[3]);
            player4.setText(users[4]);
        }
    }

    @Override
    public void onWebSocketClose(int code, String reason, boolean remote) {

    }

    @Override
    public void onWebSocketError(Exception ex) {

    }
}
