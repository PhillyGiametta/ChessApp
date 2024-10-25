package Backend.ChessApp.Settings;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/*
       A class that stores all the different settings options for the user,
       should link to the user, the game, the frontend, and perhaps admin/spectator in future

 */
@Entity
@Table(name = "GameStates", schema = "DBChessApp")
public class SettingGameStates {
}
