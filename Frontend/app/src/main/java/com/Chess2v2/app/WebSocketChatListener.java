package com.Chess2v2.app;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

public class WebSocketChatListener extends WebSocketListener {
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final ChatActivity chatActivity;

    public WebSocketChatListener(ChatActivity chatActivity) {
        this.chatActivity = chatActivity;
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        handler.post(() -> chatActivity.updateChatView(text));
    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        handler.post(() -> chatActivity.updateChatView("Connected to chat server"));
    }

    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        handler.post(() -> chatActivity.updateChatView("Disconnected: " + reason));
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        handler.post(() -> chatActivity.updateChatView("Connection error: " + t.getMessage()));
    }
}
