package Backend.ChessApp.Settings;


/*
        This is for the user only, things about appearance of app,
        and how they interact with the app
 */

import Backend.ChessApp.Users.User;
import jakarta.persistence.*;

@Entity
@Table(name = "user_settings", schema = "DBChessApp")
public class SettingsUserStates {
    short boardTheme;
    short pieceTheme;
    boolean appTheme; //dark->true or light->false
    boolean sounds;
    boolean moveHighlighting;
    boolean menuMusic;

    @Id
    @Column(name="settings_id")
    private int id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    public SettingsUserStates() {

    }

    public SettingsUserStates(User user){
        this.user = user;
        this.appTheme = false; //light default
        this.boardTheme = 0;
        this.pieceTheme = 0;
        this.sounds = true;
        this.moveHighlighting = true;
        this.menuMusic = true;
    }

    public SettingsUserStates(short boardTheme, short pieceTheme, boolean appTheme, boolean sounds, boolean moveHighlighting, boolean menuMusic) {
        this.appTheme = appTheme;
        this.boardTheme = boardTheme;
        this.pieceTheme = pieceTheme;
        this.sounds = sounds;
        this.moveHighlighting = moveHighlighting;
        this.menuMusic = menuMusic;
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

    public boolean isMenuMusic(){return this.menuMusic;}

    public void setMenuMusic(boolean menuMusic) { this.menuMusic = menuMusic;}
}
