package com.example.chessapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView messageText;
    private TextView usernameText;
    private Button loginButton;
    private Button signupButton;
    private Button profileButton;
    private Button chatButton;
    private Button playNowButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        messageText = findViewById(R.id.main_msg_txt);
        usernameText = findViewById(R.id.main_username_txt);
        loginButton = findViewById(R.id.main_login_btn);
        signupButton = findViewById(R.id.main_signup_btn);
        profileButton = findViewById(R.id.main_profile_btn);
        chatButton = findViewById(R.id.chat_button);
        playNowButton = findViewById(R.id.play_now_button);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            messageText.setText("ChessApp");
            usernameText.setVisibility(View.INVISIBLE);
        } else {
            messageText.setText("ChessApp");
            usernameText.setText(extras.getString("USERNAME"));
            loginButton.setVisibility(View.INVISIBLE);
            signupButton.setVisibility(View.INVISIBLE);
        }

        loginButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        signupButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SignupActivity.class);
            startActivity(intent);
        });

        profileButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            intent.putExtra("USERNAME", usernameText.getText().toString());
            startActivity(intent);
        });

        chatButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ChatActivity.class);
            intent.putExtra("USERNAME", usernameText.getText().toString());
            startActivity(intent);
        });

        playNowButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ChessBoardActivity.class);
            startActivity(intent);
        });
    }
}
