package Backend.ChessApp.Game;

import jakarta.persistence.*;

import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Entity
@Table(schema = "DBChessApp", name = "timer")
public class Timer {
    @Transient
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private Duration timeLeft;
    private boolean isRunning = false;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "timer_id", nullable = false)
    private int id;

    @ManyToOne
    private ChessGame chessGame;

    public Timer(int startingTime) {
        this.timeLeft = Duration.ofMinutes(startingTime);
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
