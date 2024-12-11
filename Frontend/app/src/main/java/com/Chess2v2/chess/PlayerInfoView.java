package com.Chess2v2.chess;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.Chess2v2.app.R;

public class PlayerInfoView extends LinearLayout {

    private TextView playerNameTextView;
    private TextView pieceDifferenceTextView;
    private TextView playerClockTextView;

    public PlayerInfoView(Context context) {
        super(context);
        init(context);
    }

    public PlayerInfoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PlayerInfoView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.player_info, this, true);

        playerNameTextView = findViewById(R.id.playerName);
        pieceDifferenceTextView = findViewById(R.id.playerPieceDifference);
        playerClockTextView = findViewById(R.id.playerClock);
    }

    public void setPlayerName(String name) {
        playerNameTextView.setText(name);
    }

    public void setPieceDifference(String difference) {
        pieceDifferenceTextView.setText(difference);
    }

    public void setClockTime(String time) {
        playerClockTextView.setText(time);
    }

    public void setClockActive(boolean isActive) {
        int backgroundResource = isActive
                ? R.drawable.clock_background_active
                : R.drawable.clock_background_inactive;
        playerClockTextView.setBackgroundResource(backgroundResource);
    }
}

