package com.example.chessapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ChatActivity extends AppCompatActivity {
    private WebSocketManager webSocketManager;
    private TextView chatView;
    private EditText messageEditText;
    private Button sendButton;

    private static final String GROUP_NAME = "test";  // Hardcoded group name
    private static final String USER_NAME = "lofe";   // Hardcoded username

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chatView = findViewById(R.id.chat_view);
        messageEditText = findViewById(R.id.message_edit_text);
        sendButton = findViewById(R.id.send_button);

        WebSocketChatListener listener = new WebSocketChatListener(this);
        webSocketManager = new WebSocketManager(listener);

        // Construct the WebSocket URL using the group name and user name
        String webSocketUrl = "ws://10.90.73.46:8080/group/" + GROUP_NAME + "/" + USER_NAME;

        // Connect to WebSocket server using the constructed URL
        webSocketManager.connect(webSocketUrl);

        // Set up send button listener to send messages through WebSocket
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = messageEditText.getText().toString();
                if (!message.isEmpty()) {
                    webSocketManager.sendMessage(message);
                    messageEditText.setText(""); // Clear the input field after sending the message
                }
            }
        });
    }

    public void updateChatView(String message) {
        // Update chat view with new messages received from server
        runOnUiThread(() -> {
            chatView.append("\n" + message);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webSocketManager != null) {
            webSocketManager.close();
        }
    }
}
