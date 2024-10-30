package Backend.ChessApp.Settings;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/*
       A class that stores all the different settings options for the user,
       should link to the user, the game, the frontend, and perhaps admin/spectator in future

 */
//No entity this is temporary should be in memory only
public class SettingGameStates {
        short timeController; //in minutes
        int incrementTimer; //in seconds
        boolean allowUndos;
        boolean enableLimitMoveTime; //eg. only have 30 sec. or no move
        int limitMoveTime; //in seconds

        public SettingGameStates(short time, int inc, boolean undo, boolean limiter, int limiterTimer){
            this.timeController = time;
            this.incrementTimer = inc;
            this.allowUndos = undo;
            this.enableLimitMoveTime = limiter;
            this.limitMoveTime = limiterTimer;
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
