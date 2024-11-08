package com.example.chessapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okhttp3.internal.ws.RealWebSocket;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.google.gson.Gson;

import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Group_finder extends AppCompatActivity {

    private Button group1;
    private Button group2;
    private Button group3;
    private Button group4;
    private ArrayList<Group> groups;
    private ArrayList<UserData> Activeusers;
    private APIStuff api;
    private WebSocket webSocket;
    private String SERVER_PATH = "ws://coms-3090-050.class.las.iastate.edu:8080/group/test/e";
    private Set<String> activeUsers = Collections.synchronizedSet(new HashSet<>());
    private Gson gson = new Gson();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_finder);

        group1 = findViewById(R.id.group1);
        group2 = findViewById(R.id.group2);
        group3 = findViewById(R.id.group3);
        group4 = findViewById(R.id.group4);

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        // Initialize Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://coms-3090-050.class.las.iastate.edu:8080/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        api = retrofit.create(APIStuff.class);

        group1.setOnClickListener(null);
        group2.setOnClickListener(null);
        group3.setOnClickListener(null);
        group4.setOnClickListener(null);



    }

    private void joinGroup(String groupname){
        for(Group group: groups){
            if(group.getGroupName() == groupname){
                //JOIN THE GROUP
                return;
            }
        }
        //SEND SOME ERROR MESSAGE
    }

//    private void initiateSocketConnection(){
//        OkHttpClient client = new OkHttpClient();
//        Request request = new Request.Builder().url(SERVER_PATH).build();
//        webSocket = client.newWebSocket(request, new SocketListener());
//    }
//    private class SocketListener extends WebSocketListener{
//        @Override
//        public void onOpen(WebSocket webSocket, Response response) {
//            super.onOpen(webSocket, response);
//
//            runOnUiThread(() -> {
//                Toast.makeText(Group_finder.this, "Socket Connection Successful", Toast.LENGTH_SHORT).show();
//            });
//        }
//
//        @Override
//        public void onMessage(WebSocket webSocket, String message) {
//            Message incomingMessage = gson.fromJson(message, Message.class);
//
//            if("connect".equals(incomingMessage.type)){
//                activeUsers.add(incomingMessage.username);
//            } else if ("disconnect".equals(incomingMessage.type)){
//                activeUsers.remove(incomingMessage.username);
//            }
//
//        }
//    }
}
