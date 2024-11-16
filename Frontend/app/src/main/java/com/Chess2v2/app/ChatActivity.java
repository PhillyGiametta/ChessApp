package com.Chess2v2.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.Chess2v2.ChessApplication;
import com.Chess2v2.groups.Group_finder;

/**
 * ChatActivity handles the chat functionality for the Chess2v2 app.
 * It connects to a WebSocket server to enable real-time communication between users in the same group.
 */
public class ChatActivity extends AppCompatActivity {

    /** WebSocketManager to manage WebSocket connections and communications. */
    private WebSocketManager webSocketManager;

    /** TextView to display chat messages. */
    private TextView chatView;

    /** EditText for the user to input chat messages. */
    private EditText messageEditText;

    /** Button to send the chat message. */
    private Button sendButton;

    /**
     * Called when the activity is created. Sets up the view, initializes UI components,
     * and establishes a WebSocket connection for chat communication.
     *
     * @param savedInstanceState Bundle containing the saved state of the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // Initialize back button to finish the activity
        Button backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());

        // Initialize UI components
        chatView = findViewById(R.id.chat_view);
        messageEditText = findViewById(R.id.message_edit_text);
        sendButton = findViewById(R.id.send_button);

        WebSocketChatListener listener = new WebSocketChatListener(this);
        webSocketManager = new WebSocketManager(listener);

        // Construct the WebSocket URL using the group name and user name
        String webSocketUrl = ChessApplication.getInstance().getWebSocketBaseUrl() + "group/" + ChessApplication.getInstance().getGroupName() + "/" + ChessApplication.getInstance().getUserName();

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

    /**
     * Updates the chat view with new messages received from the WebSocket server.
     *
     * @param message The message to be displayed in the chat view.
     */
    public void updateChatView(String message) {
        runOnUiThread(() -> {
            chatView.append("\n" + message);
        });
    }

    /**
     * Called when the activity is destroyed. Closes the WebSocket connection to free up resources.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webSocketManager != null) {
            webSocketManager.close();
        }
    }
}
