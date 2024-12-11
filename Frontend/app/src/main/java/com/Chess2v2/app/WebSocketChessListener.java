package com.Chess2v2.app;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.Chess2v2.chess.ChessBoardActivity;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

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
                Log.v("Server Response", text);
                // Check if the message starts and ends with curly braces, indicating it is a JSON object.
                if (text.startsWith("{") && text.endsWith("}")) {
                    JSONObject response = new JSONObject(text);
                    String type = response.optString("type", "undefined");

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
                    // TODO: set the clocke based on the server message
                    //if("server clocke message"){
                    //chessBoardActivity.getWhitePlayer().getClock().setTime(whitePlayerTime);
                    //chessBoardActivity.getWhitePlayer().getClock().setTime(blackPlayerTime);
                    //}
                    //TODO: Set Game Over
                    // chessBoardActivity.getOver(message)


                } else {
                    // Log the non-JSON message if needed or handle it as a non-JSON message.
                    Log.d("WebSocketChessListener", "Non-JSON message received: " + text);
                }
            } catch (JSONException e) {
                // Remove or lower the priority of the JSON parsing error log to avoid clutter in logcat.
                Log.v("WebSocketChessListener", "Ignoring non-JSON message: " + e.getMessage());
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
