package com.Chess2v2.groups;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Chess2v2.ChessApplication;
import com.Chess2v2.app.APIStuff;
import com.Chess2v2.app.R;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GroupActivity extends AppCompatActivity {

    private EditText groupNameInput;

    private GroupAdapter groupAdapter;
    private List<Group> groupList;
    private WebSocket webSocket;
    private APIStuff api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_finder);

        groupNameInput = findViewById(R.id.groupNameInput);
        Button createGroupButton = findViewById(R.id.createGroupButton);
        RecyclerView groupRecyclerView = findViewById(R.id.groupRecyclerView);

        groupList = new ArrayList<>();
        groupAdapter = new GroupAdapter(groupList);

        groupRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        groupRecyclerView.setAdapter(groupAdapter);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ChessApplication.getInstance().getBaseUrl())
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .build();

        api = retrofit.create(APIStuff.class);

        createGroupButton.setOnClickListener(v -> {
            String groupName = groupNameInput.getText().toString().trim();
            if (!TextUtils.isEmpty(groupName)) {
                createGroup(groupName);
            } else {
                Toast.makeText(this, "Please enter a group name.", Toast.LENGTH_SHORT).show();
            }
        });
        Button backButton =  findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> {
            finish();
        });

        loadGroups();
    }

    private void createGroup(String groupName) {
        api.createGroup(new Group(groupName)).enqueue(new Callback<Group>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<Group> call, @NonNull Response<Group> response) {
                if (response.isSuccessful() && response.body() != null) {
                    groupList.add(response.body());
                    groupAdapter.notifyDataSetChanged();
                    groupNameInput.setText("");
                    Toast.makeText(GroupActivity.this, "Group created: " + groupName, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(GroupActivity.this, "Failed to create group: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Group> call, @NonNull Throwable t) {
                Toast.makeText(GroupActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadGroups() {
        api.listGroups().enqueue(new Callback<List<Group>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<List<Group>> call, @NonNull Response<List<Group>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    groupList.clear();
                    groupList.addAll(response.body());
                    groupAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(GroupActivity.this, "Failed to load groups: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Group>> call, @NonNull Throwable t) {
                Toast.makeText(GroupActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupViewHolder> {

        private final List<Group> groupList;

        public GroupAdapter(List<Group> groupList) {
            this.groupList = groupList;
        }

        @NonNull
        @Override
        public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_item, parent, false);
            return new GroupViewHolder(view);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
            Group group = groupList.get(position);
            holder.groupName.setText(group.getGroupName());
            holder.playerCount.setText("Members: " + group.getPlayerCount());

            holder.joinButton.setOnClickListener(v -> joinGroup(group, holder.itemView.getContext()));
        }

        @Override
        public int getItemCount() {
            return groupList.size();
        }

        private void joinGroup(Group group, Context context) {
//            api.joinGroup(group.getId() + "").enqueue(new Callback<Void>() {
//                @Override
//                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
//                    if (response.isSuccessful()) {
//                        Toast.makeText(context, "Joined " + group.getGroupName(), Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(context, "Failed to join group: " + response.message(), Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//                @Override
//                public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
//                    Toast.makeText(context, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            });
             joinGroup(group.groupName, ChessApplication.getInstance().getUserName());
        }

        private void joinGroup(String groupName, String username) {
            OkHttpClient client = new OkHttpClient();
            if (webSocket != null) {
                try {
                    webSocket.cancel();
                } catch (Exception ignored) {
                }
            }
            Log.v("Joining group", groupName);
            Request request = new Request.Builder().url(
                    ChessApplication.getInstance().getBaseUrl() + "group/" + groupName + "/" + username
            ).build();
            webSocket = client.newWebSocket(request, new SocketListener());
        }

        class GroupViewHolder extends RecyclerView.ViewHolder {
            TextView groupName, playerCount;
            Button joinButton;

            public GroupViewHolder(@NonNull View itemView) {
                super(itemView);
                groupName = itemView.findViewById(R.id.groupName);
                playerCount = itemView.findViewById(R.id.playerCount);
                joinButton = itemView.findViewById(R.id.joinButton);
            }
        }
    }

    private class SocketListener extends WebSocketListener {
        @Override
        public void onOpen(@NonNull WebSocket webSocket, @NonNull okhttp3.Response response) {
            super.onOpen(webSocket, response);

            runOnUiThread(() -> {
                Toast.makeText(GroupActivity.this, "Socket Connection Successful", Toast.LENGTH_SHORT).show();
            });
        }

        @Override
        public void onMessage(@NonNull WebSocket webSocket, @NonNull String message) {
            Log.v("group-message", message);
//            if("connect".equals(incomingMessage.type)){
//                activeUsers.add(incomingMessage.username);
//            } else if ("disconnect".equals(incomingMessage.type)){
//                activeUsers.remove(incomingMessage.username);
//            }

        }
    }
}
