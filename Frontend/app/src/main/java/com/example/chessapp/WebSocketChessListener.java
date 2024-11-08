package com.example.chessapp;

import android.os.Handler;
import android.os.Looper;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import org.json.JSONException;
import org.json.JSONObject;

public class WebSocketChessListener extends WebSocketListener {
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final ChessBoardActivity chessBoardActivity;

    public WebSocketChessListener(ChessBoardActivity chessBoardActivity) {
        this.chessBoardActivity = chessBoardActivity;
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        handler.post(() -> {
            try {
                JSONObject response = new JSONObject(text);
                String type = response.getString("type");

                if ("moveConfirmed".equals(type)) {
                    int fromX = response.getInt("rowStart");
                    int fromY = response.getInt("colStart");
                    int toX = response.getInt("rowEnd");
                    int toY = response.getInt("colEnd");

                    // Update the board if the move was confirmed
                    chessBoardActivity.updateBoard(fromX, fromY, toX, toY);
                } else if ("invalidMove".equals(type)) {
                    chessBoardActivity.displayInvalidMoveMessage();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        handler.post(() -> chessBoardActivity.displayConnectionStatus("Connected to game server"));
    }

    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        handler.post(() -> chessBoardActivity.displayConnectionStatus("Disconnected: " + reason));
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        handler.post(() -> chessBoardActivity.displayConnectionStatus("Connection error: " + t.getMessage()));
    }
}
