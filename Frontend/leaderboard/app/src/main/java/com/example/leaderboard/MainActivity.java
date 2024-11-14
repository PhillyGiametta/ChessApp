package com.Chess2v2.leaderboard;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class MainActivity extends AppCompatActivity {
    interface RequestUser{
        @GET("leaderboard/{id}")
        Call<LeaderboardEntry> getUser(@Path("id") String id);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        TextView textView = findViewById(R.id.textView);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://coms-3090-050.class.las.iastate.edu:8080")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RequestUser requestUser = retrofit.create(RequestUser.class);

        requestUser.getUser("1").enqueue(new Callback<LeaderboardEntry>() {
            @Override
            public void onResponse(Call<LeaderboardEntry> call, Response<LeaderboardEntry> response) {
                textView.setText(response.body().getRankPosition());

            }

            @Override
            public void onFailure(Call<LeaderboardEntry> call, Throwable throwable) {
                textView.setText(throwable.getMessage());

            }
        });


    }
}