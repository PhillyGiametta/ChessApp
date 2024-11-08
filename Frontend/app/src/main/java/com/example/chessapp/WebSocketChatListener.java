package com.example.chessapp;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import android.os.Handler;
import android.os.Looper;

import com.example.chessapp.ChatActivity;

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
