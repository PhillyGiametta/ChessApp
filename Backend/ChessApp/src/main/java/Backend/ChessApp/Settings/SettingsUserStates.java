package Backend.ChessApp.Settings;


/*
        This is for the user only, things about appearance of app,
        and how they interact with the app
 */

public class SettingsUserStates {
        short boardTheme;
        short pieceTheme;
        boolean appTheme; //dark->true or light->false
        boolean sounds;
        boolean moveHighlighting;

        public SettingsUserStates(short boardTheme, short pieceTheme, boolean appTheme, boolean sounds, boolean moveHighlighting) {
            this.appTheme = appTheme;
            this.boardTheme = boardTheme;
            this.pieceTheme = pieceTheme;
            this.sounds = sounds;
            this.moveHighlighting = moveHighlighting;
        }









    public short getBoardTheme() {
        return boardTheme;
    }

    public short getPieceTheme() {
        return pieceTheme;
    }

    public boolean isAppTheme() {
        return appTheme;
    }

    public boolean isMoveHighlighting() {
        return moveHighlighting;
    }

    public boolean isSounds() {
        return sounds;
    }

    public void setAppTheme(boolean appTheme) {
        this.appTheme = appTheme;
    }

    public void setBoardTheme(short boardTheme) {
        this.boardTheme = boardTheme;
    }

    public void setMoveHighlighting(boolean moveHighlighting) {
        this.moveHighlighting = moveHighlighting;
    }
    public void setPieceTheme(short pieceTheme) {
        this.pieceTheme = pieceTheme;
    }

    public void setSounds(boolean sounds) {
        this.sounds = sounds;
    }
}
