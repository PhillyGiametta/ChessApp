package com.Chess2v2.chess;

import android.annotation.SuppressLint;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;

public class Clock {

    private long totalTimeMillis;
    private long remainingTimeMillis;
    private CountDownTimer countDownTimer;
    private final Handler handler;
    private boolean isRunning;

    // Constructor
    public Clock(Handler handler) {
        this.handler = handler;
        this.totalTimeMillis = 0;
        this.remainingTimeMillis = 0;
        this.isRunning = false;
    }

    public void setTime(int minutes, int seconds) {
        this.totalTimeMillis = (minutes * 60L + seconds) * 1000L;
        this.remainingTimeMillis = totalTimeMillis;
        updateUI(remainingTimeMillis);
    }

    public void start() {
        if (!isRunning) {
            isRunning = true;
            countDownTimer = new CountDownTimer(remainingTimeMillis, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    remainingTimeMillis = millisUntilFinished;
                    updateUI(millisUntilFinished);
                }

                @Override
                public void onFinish() {
                    isRunning = false;
                    remainingTimeMillis = 0;
                    updateUI(0);
                }
            };
            countDownTimer.start();
        }
    }

    public void pause() {
        if (isRunning) {
            isRunning = false;
            countDownTimer.cancel();
        }
    }

    public void reset(int minutes, int seconds) {
        pause();
        setTime(minutes, seconds);
    }

    private void updateUI(long millis) {
        int seconds = (int) (millis / 1000);
        int minutes = seconds / 60;
        seconds = seconds % 60;

        @SuppressLint("DefaultLocale") String time = String.format("%02d:%02d", minutes, seconds);
        Message msg = handler.obtainMessage();
        msg.obj = time; // Send the formatted time to the UI
        handler.sendMessage(msg);
    }

    public boolean isRunning() {
        return isRunning;
    }
}
