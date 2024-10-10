package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class AA_RecyclerViewAdapter extends RecyclerView.Adapter<AA_RecyclerViewAdapter.MyViewHolder> {

    Context context;
    ArrayList<PlayerModel> players;

    public AA_RecyclerViewAdapter(Context context, ArrayList<PlayerModel> players){
        this.context = context;
        this.players = players;
    }

    @NonNull
    @Override
    public AA_RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_row, parent, false);
        return new AA_RecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AA_RecyclerViewAdapter.MyViewHolder holder, int position) {
        holder.username.setText(players.get(position).getName());
        holder.Rating.setText(players.get(position).getRating());
        holder.pfp.setImageResource(players.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        return players.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView pfp;
        TextView username;
        TextView Rating;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            pfp = itemView.findViewById(R.id.pfp);
            username = itemView.findViewById(R.id.Username);
            Rating = itemView.findViewById(R.id.rating);


        }
    }
}
