package com.Chess2v2.app;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.Chess2v2.chess.ChessBoardActivity;

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
        Log.d("WebSocketChessListener", "Raw message received: " + text);
        handler.post(() -> {
            try {
                JSONObject response = new JSONObject(text);
                String type = response.getString("type");

                if ("moveConfirmed".equals(type)) {
                    int fromX = response.getInt("rowStart");
                    int fromY = response.getInt("colStart");
                    int toX = response.getInt("rowEnd");
                    int toY = response.getInt("colEnd");
                    Log.d("WebSocketChessListener", "Move confirmed: from (" + fromX + ", " + fromY + ") to (" + toX + ", " + toY + ")");
                    chessBoardActivity.updateBoard(fromX, fromY, toX, toY);
                } else if ("invalidMove".equals(type)) {
                    Log.d("WebSocketChessListener", "Received invalid move message");
                    chessBoardActivity.undoInvalidMove();
                }
            } catch (JSONException e) {
                Log.e("WebSocketChessListener", "JSON parsing error: " + e.getMessage());
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
