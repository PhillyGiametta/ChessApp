package com.Chess2v2.leaderboard;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Chess2v2.app.ApiService;
import com.Chess2v2.app.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
/**
 * sets up the Leaderboard and fills its recycler view with a list
 * of LeaderboardItem objects created from the database
 */
public class LeaderboardActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LeaderboardAdapter leaderboardAdapter;
    private List<LeaderboardItem> leaderboardItems;
    private Button refreshButton;
    private ApiService apiService;

    @Override
    /**
    @param sets up the RecyclerView, connects widgets to XML view
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);


        Button backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());

        recyclerView = findViewById(R.id.recyclerViewLeaderboard);
        refreshButton = findViewById(R.id.refreshButton);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.90.73.46:8080/")  // Update the URL to your server
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);

        // Initialize the leaderboard items and adapter
        leaderboardItems = new ArrayList<>();
        leaderboardAdapter = new LeaderboardAdapter(leaderboardItems);
        recyclerView.setAdapter(leaderboardAdapter);

        // Load initial data
        loadLeaderboardData();

        // Set up the refresh button listener
        refreshButton.setOnClickListener(v -> loadLeaderboardData());
    }

    /*
    @Param testing testing 123
     */
    private void loadLeaderboardData() {
        Call<List<LeaderboardEntry>> call = apiService.getLeaderboard();

        call.enqueue(new Callback<List<LeaderboardEntry>>() {
            @Override
            public void onResponse(Call<List<LeaderboardEntry>> call, Response<List<LeaderboardEntry>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    leaderboardItems.clear();
                    for (LeaderboardEntry entry : response.body()) {
                        leaderboardItems.add(new LeaderboardItem(entry.getUserName(), entry.getRating(), entry.getRankPosition()));
                    }
                    leaderboardAdapter.notifyDataSetChanged();
                } else {
                    Log.e("LeaderboardActivity", "Failed to load leaderboard data");
                }
            }

            @Override
            public void onFailure(Call<List<LeaderboardEntry>> call, Throwable t) {
                Log.e("LeaderboardActivity", "Error: " + t.getMessage());
            }
        });
    }
}
