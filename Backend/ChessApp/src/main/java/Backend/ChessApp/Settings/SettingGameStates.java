package Backend.ChessApp.Settings;


import Backend.ChessApp.Game.ChessGame;
import jakarta.persistence.*;
import org.springframework.stereotype.Component;

/*
       A class that stores all the different settings options for the user,
       should link to the user, the game, the frontend, and perhaps admin/spectator in future

 */
//No entity this is temporary should be in memory only
@Entity
public class SettingGameStates {
    @Id
    @Column(name = "game_settings_id")
    private int id;

    @OneToOne(mappedBy = "settingGameStates")
    @MapsId
    private ChessGame chessGame;

    short timeController; //in minutes
    int incrementTimer; //in seconds
    boolean allowUndos;
    boolean enableLimitMoveTime; //eg. only have 30 sec. or no move
    int limitMoveTime; //in seconds

    public SettingGameStates(short time, int inc, boolean undo, boolean limiter, int limiterTimer) {
        this.timeController = time;
        this.incrementTimer = inc;
        this.allowUndos = undo;
        this.enableLimitMoveTime = limiter;
        this.limitMoveTime = limiterTimer;
    }

    @Override
    public String toString() {
        String s = "";
        s += String.format("Time: " + timeController + " \nIncrement: " + incrementTimer + " \nAllow undo: " + allowUndos + " \nEnable limiter: " + enableLimitMoveTime + " \nlimterTimer (if applicaple) " + limitMoveTime);
        return s;
    }


    public short getTimeController() {
        return timeController;
    }

    public boolean isAllowUndos() {
        return allowUndos;
    }

    public boolean isEnableLimitMoveTime() {
        return enableLimitMoveTime;
    }

    public int getIncrementTimer() {
        return incrementTimer;
    }

    public int getLimitMoveTime() {
        return limitMoveTime;
    }

    public void setAllowUndos(boolean allowUndos) {
        this.allowUndos = allowUndos;
    }

    public void setEnableLimitMoveTime(boolean enableLimitMoveTime) {
        this.enableLimitMoveTime = enableLimitMoveTime;
    }

    public void setIncrementTimer(int incrementTimer) {
        this.incrementTimer = incrementTimer;
    }

    public void setLimitMoveTime(int limitMoveTime) {
        this.limitMoveTime = limitMoveTime;
    }

    public void setTimeController(short timeController) {
        this.timeController = timeController;
    }
}
