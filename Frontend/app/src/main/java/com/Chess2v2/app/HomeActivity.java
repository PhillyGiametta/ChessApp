package com.Chess2v2.app;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.Chess2v2.leaderboard.LeaderboardActivity;
import com.Chess2v2.chess.ChessBoardActivity;
import com.Chess2v2.groups.Group_finder; // Import the GroupFinder activity

public class HomeActivity extends AppCompatActivity {

    private Button profileButton;
    private Button chatButton;
    private Button playNowButton;
    private Button leaderboardButton;
    private Button findGroupButton; // New button for Find Group

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        profileButton = findViewById(R.id.home_profile_btn);
        chatButton = findViewById(R.id.home_chat_btn);
        playNowButton = findViewById(R.id.home_play_now_btn);
        leaderboardButton = findViewById(R.id.home_leaderboard_btn);
        findGroupButton = findViewById(R.id.find_group_button); // Initialize the new button

        profileButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
            startActivity(intent);
        });

        chatButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, ChatActivity.class);
            startActivity(intent);
        });

        playNowButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, ChessBoardActivity.class);
            startActivity(intent);
        });

        leaderboardButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, LeaderboardActivity.class);
            startActivity(intent);
        });

        // Set OnClickListener for the new Find Group button
        findGroupButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, Group_finder.class);
            startActivity(intent);
        });
        //MediaPlayer mediaPlayer = new MediaPlayer(this, R.raw.music);
    }
}