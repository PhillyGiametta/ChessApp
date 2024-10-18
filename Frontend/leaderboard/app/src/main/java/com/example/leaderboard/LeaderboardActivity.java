package com.example.leaderboard;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class LeaderboardActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LeaderboardAdapter leaderboardAdapter;
    private List<LeaderboardItem> leaderboardItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        recyclerView = findViewById(R.id.recyclerViewLeaderboard);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Sample data
        leaderboardItems = new ArrayList<>();
        leaderboardItems.add(new LeaderboardItem("Alice", 1800, 1));
        leaderboardItems.add(new LeaderboardItem("Bob", 1750, 2));
        leaderboardItems.add(new LeaderboardItem("Charlie", 1700, 3));

        leaderboardAdapter = new LeaderboardAdapter(leaderboardItems);
        recyclerView.setAdapter(leaderboardAdapter);
    }
}
