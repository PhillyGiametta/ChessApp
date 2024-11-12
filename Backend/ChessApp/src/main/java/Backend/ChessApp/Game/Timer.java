package Backend.ChessApp.Game;

import jakarta.persistence.*;

import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Entity
public class Timer {
    @Transient
    private Duration timeLeft;
    private boolean isRunning = false;
    private long duration, previousDuration, startTime;
    @Id
    @Column(name = "timer_id", insertable = false, updatable = false)
    private int id;

    @ManyToOne
    @JoinColumn(name="chess_game_id")
    private ChessGame chessGame;

    public Timer(){
        this.timeLeft = Duration.ZERO;
        this.duration = timeLeft.toMillis();
    }

    public Timer(int startingTime) {

        this.timeLeft = Duration.ofMinutes(startingTime);
        this.duration = timeLeft.toMillis(); // Total countdown duration in milliseconds
    }

    public void resetDuration(int startingTime) {
        this.timeLeft = Duration.ofMinutes(startingTime);
        this.duration = timeLeft.toMillis();
        this.previousDuration = 0L;
        this.isRunning = false;
    }

    public void start() {
        isRunning = true;
        new Thread(() -> {
            while (isRunning && !timeLeft.isZero() && !timeLeft.isNegative()) {
                try {
                    Thread.sleep(1000); // Wait for 1 second
                    timeLeft = timeLeft.minusSeconds(1); // Decrease timeLeft by 1 second
                    System.out.println("Time Left: " + getFormattedDuration());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Preserve interrupt status
                }
            }
            if (timeLeft.isZero() || timeLeft.isNegative()) {
                isRunning = false;
                System.out.println("Time is up!");
            }
        }).start();
    }

    public void pause() {
        //setDuration();
        this.previousDuration = duration;
        this.isRunning = false;
    }

    public void stop() {
        setDuration();
        this.isRunning = false;
    }

    private void setDuration() {
        if (isRunning) {
            long elapsedTime = System.currentTimeMillis() - startTime;
            long newDuration = duration - elapsedTime; // Subtract elapsed time from total duration

            // Ensure that duration does not go below zero
            this.duration = Math.max(newDuration, 0);
            this.timeLeft = Duration.ofMillis(this.duration);
        }
    }

    public String getFormattedDuration() {
        long millis = timeLeft.toMillis();
        int seconds = (int) ((millis + 500L) / 1000L); // Round to nearest second
        int minutes = seconds / 60;
        int hours = minutes / 60;

        StringBuilder builder = new StringBuilder();
        if (hours > 0) {
            builder.append(hours).append(":");
        }

        minutes %= 60;
        if (hours > 0) {
            builder.append(String.format("%02d", minutes)).append(":");
        } else if (minutes > 0) {
            builder.append(minutes).append(":");
        }

        seconds %= 60;
        if (hours > 0 || minutes > 0) {
            builder.append(String.format("%02d", seconds));
        } else {
            builder.append(seconds);
        }

        return builder.toString();
    }

    public Duration getTimeLeft() {
        return timeLeft;
    }
}
