package com.Chess2v2.groups;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Chess2v2.ChessApplication;
import com.Chess2v2.app.APIStuff;
import com.Chess2v2.app.ProfileActivity;
import com.Chess2v2.app.R;
import com.Chess2v2.app.UserData;
import com.Chess2v2.app.UserResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import okhttp3.WebSocket;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Group_finder extends AppCompatActivity {

    private RecyclerView lstGroups;
    private GroupAdapter lstGroupsAdapter;
    private List<Group> groups;
    private ArrayList<UserData> Activeusers;
    private APIStuff api;
    private WebSocket webSocket;
    private Set<String> activeUsers = Collections.synchronizedSet(new HashSet<>());
    private Gson gson = new Gson();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_finder);

        Button backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());

        lstGroups = findViewById(R.id.group_list);
        lstGroups.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        lstGroupsAdapter = new GroupAdapter();
        lstGroupsAdapter.setOnItemSelectedLister(this::joinGroup);
        lstGroups.setAdapter(lstGroupsAdapter);

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        // Initialize Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ChessApplication.getInstance().getBaseUrl())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        api = retrofit.create(APIStuff.class);
        loadGroups();
    }

    private void joinGroup(String groupname){
        for(Group group: groups){
            if(group.getGroupName().equals(groupname)){
                Toast.makeText(this, String.format("You have selected  %s", groupname), Toast.LENGTH_SHORT).show();
                ChessApplication.getInstance().setGroupName(groupname);
                return;
            }
        }
        Toast.makeText(this, String.format("Group: %s does not exist", groupname), Toast.LENGTH_SHORT).show();
    }

    private void loadGroups() {
        api.listGroups().enqueue(new Callback<List<Group>>() {
            @Override
            public void onResponse(Call<List<Group>> call, retrofit2.Response<List<Group>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Group> groups = response.body();
                    Group_finder.this.groups = groups;
                    lstGroupsAdapter.setGroups(groups);
                } else {
                    Toast.makeText(Group_finder.this, "Failed to load profiles: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Group>> call, Throwable t) {
                Toast.makeText(Group_finder.this, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected static class GroupViewHolder extends RecyclerView.ViewHolder {

        private final TextView groupName;
        private final TextView playerCount;

        public GroupViewHolder(View view) {
            super(view);
            groupName = (TextView) view.findViewById(R.id.tvGroupName);
            playerCount = (TextView) view.findViewById(R.id.tvPlayerCount);
        }

        public void setGroupName(String groupName) {
            this.groupName.setText(groupName);
        }

        public void setPlayerCount(int count) {
            this.playerCount.setText(String.valueOf(count));
        }
    }
    protected static class GroupAdapter extends RecyclerView.Adapter<GroupViewHolder> {

        private Group[] groups;

        protected OnItemSelectedLister listener;

        @NonNull
        @Override
        public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_item, parent, false);
            view.setOnClickListener(v -> {
                if(listener != null) {
                    TextView textView = (TextView) view.findViewById(R.id.tvGroupName);
                    listener.onItemSelected(textView.getText().toString());
                }
            });
            return new GroupViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
            Group group = groups[position];
            holder.setGroupName(group.getGroupName());
            holder.setPlayerCount(group.getPlayerCount());
        }

        @Override
        public int getItemCount() {
            return groups != null ? groups.length : 0;
        }

        public void setGroups(List<Group> groups) {
            this.groups = groups.toArray(new Group[0]);
            notifyDataSetChanged();
        }

        public void setOnItemSelectedLister(OnItemSelectedLister listener) {
            this.listener = listener;
        }

        public interface OnItemSelectedLister {
            public void onItemSelected(String item);
        }
    }

//    private void initiateSocketConnection(){
//        OkHttpClient client = new OkHttpClient();
//        Request request = new Request.Builder().url(SERVER_PATH).build();
//        webSocket = client.newWebSocket(request, new SocketListener());
//    }
//    private class SocketListener extends WebSocketListener{
//        @Override
//        public void onOpen(WebSocket webSocket, Response response) {
//            super.onOpen(webSocket, response);
//
//            runOnUiThread(() -> {
//                Toast.makeText(Group_finder.this, "Socket Connection Successful", Toast.LENGTH_SHORT).show();
//            });
//        }
//
//        @Override
//        public void onMessage(WebSocket webSocket, String message) {
//            Message incomingMessage = gson.fromJson(message, Message.class);
//
//            if("connect".equals(incomingMessage.type)){
//                activeUsers.add(incomingMessage.username);
//            } else if ("disconnect".equals(incomingMessage.type)){
//                activeUsers.remove(incomingMessage.username);
//            }
//
//        }
//    }
}
