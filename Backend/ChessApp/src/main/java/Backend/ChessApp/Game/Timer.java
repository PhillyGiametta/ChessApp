package Backend.ChessApp.Game;

import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Timer {
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private Duration timeLeft;
    private boolean isRunning = false;

    public Timer(Duration startingTime) {
        this.timeLeft = startingTime;
    }

    public void start() {
        if (!isRunning) {
            isRunning = true;
            scheduler.scheduleAtFixedRate(() -> {
                if (!timeLeft.isZero() && !timeLeft.isNegative()) {
                    timeLeft = timeLeft.minusSeconds(1);
                    System.out.println("Time Left: " + formatDuration(timeLeft));
                } else {
                    stop();
                    System.out.println("Time is up!");
                }
            }, 0, 1, TimeUnit.SECONDS);
        }
    }

    public void stop() {
        isRunning = false;
        scheduler.shutdownNow();
    }

    public void reset(Duration startingTime) {
        stop();
        timeLeft = startingTime;
        isRunning = false;
    }

    public String formatDuration(Duration duration) {
        long hours = duration.toHours();
        long minutes = duration.toMinutesPart();
        long seconds = duration.toSecondsPart();
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public Duration getTimeLeft() {
        return timeLeft;
    }
}
