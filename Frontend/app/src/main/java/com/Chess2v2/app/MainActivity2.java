//package com.example.chessapp;
//
//import android.os.Bundle;
//import android.os.Bundle;
//import android.util.Log;
//import android.widget.TextView;
//
//import androidx.activity.EdgeToEdge;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.graphics.Insets;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowInsetsCompat;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//import retrofit2.Retrofit;
//import retrofit2.converter.gson.GsonConverterFactory;
//import retrofit2.http.Body;
//import retrofit2.http.GET;
//import retrofit2.http.POST;
//import retrofit2.http.Path;
//
//public class MainActivity2 extends AppCompatActivity {
//    interface RequestUser{
//        @GET("/leaderboard")
//        Call<List<LeaderboardEntry>> listProfiles();
//
//        @POST("/leaderboard/{id}")
//        Call<LeaderboardEntry> newLeaderboardEntry(@Body LeaderboardEntry entry, @Path("id") int id);
//
//    }
//
//    private RecyclerView recyclerView;
//    private LeaderboardAdapter leaderboardAdapter;
//    private List<LeaderboardItem> leaderboardItems;
//    private List<LeaderboardEntry> users;
//    private List<LeaderboardEntry> users2;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_leaderboard);
//
//        //TextView textView = findViewById(R.id.textView);
//
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://10.90.73.46:8080/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        RequestUser requestUser = retrofit.create(RequestUser.class);
//
//        recyclerView = findViewById(R.id.recyclerViewLeaderboard);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//        // Sample data
//        leaderboardItems = new ArrayList<>();
//        leaderboardItems.add(new LeaderboardItem("Alice", 1800, 1));
//        leaderboardItems.add(new LeaderboardItem("Bob", 1750, 2));
//        leaderboardItems.add(new LeaderboardItem("Charlie", 1700, 3));
//
//        leaderboardAdapter = new LeaderboardAdapter(leaderboardItems);
//        recyclerView.setAdapter(leaderboardAdapter);
//
//        UserData Noah = new UserData("nbusack@iastate.edu", "Noah", "Booga", "79");
//        LeaderboardEntry entry = new LeaderboardEntry(79,1200, 6, 2, 3.0);
//
//        requestUser.newLeaderboardEntry(entry, 79);
//        Log.d("HERE I AM", "");
//
//        requestUser.listProfiles().enqueue(new Callback<List<LeaderboardEntry>>() {
//            @Override
//            public void onResponse(Call<List<LeaderboardEntry>> call, Response<List<LeaderboardEntry>> response) {
//                users = (response.body());
//                for(int i = 0; i < users.size(); i++){
//                    LeaderboardEntry temp = users.get(i);
//                    leaderboardItems.add(new LeaderboardItem(temp.getUser().getUserName(), temp.getRating(), temp.getRankPosition()));
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<LeaderboardEntry>> call, Throwable throwable) {
//               //textView.setText(throwable.getMessage());
//                leaderboardItems.add(new LeaderboardItem("Failure", 100, 4));
//
//            }
//        });
//        try {
//            Thread.sleep(10000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//    }
//}