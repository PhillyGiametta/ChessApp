package com.Chess2v2.leaderboard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Chess2v2.app.R;

import java.util.List;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.LeaderboardViewHolder> {

    private List<LeaderboardItem> leaderboardItems;

    public LeaderboardAdapter(List<LeaderboardItem> leaderboardItems) {
        this.leaderboardItems = leaderboardItems;
    }

    @NonNull
    @Override
    public LeaderboardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activityleaderboard_item, parent, false);
        return new LeaderboardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LeaderboardViewHolder holder, int position) {
        LeaderboardItem item = leaderboardItems.get(position);
        holder.tvRank.setText(String.valueOf(item.getRank()));
        holder.tvPlayerName.setText(item.getPlayerName());
        holder.tvPlayerRating.setText(String.valueOf(item.getPlayerRating()));
    }

    @Override
    public int getItemCount() {
        return leaderboardItems.size();
    }

    public static class LeaderboardViewHolder extends RecyclerView.ViewHolder {
        TextView tvRank;
        TextView tvPlayerName;
        TextView tvPlayerRating;

        public LeaderboardViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRank = itemView.findViewById(R.id.tvRank);
            tvPlayerName = itemView.findViewById(R.id.tvPlayerName);
            tvPlayerRating = itemView.findViewById(R.id.tvPlayerRating);
        }
    }
}
