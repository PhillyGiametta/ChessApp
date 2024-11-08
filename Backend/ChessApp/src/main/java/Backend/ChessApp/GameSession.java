package Backend.ChessApp;

import Backend.ChessApp.Game.ChessGame;
import Backend.ChessApp.Settings.GameSettingsService;
import Backend.ChessApp.Settings.SettingGameStates;
import Backend.ChessApp.Users.User;

public class GameSession {
    private SettingGameStates gameSettings;
    private User admin;
    private GameSettingsService gameSettingsService;
    private ChessGame chessGame;

    public void setAdmin(User admin) {
        this.admin = admin;
    }

    public boolean isAdmin(User user) {
        return gameSettingsService.isAdmin(admin, chessGame);
    }

    public void initializeSettings(SettingGameStates settings) {
        this.gameSettings = settings;
        broadcastSettingsToPlayers();
    }

    public SettingGameStates getSettings() {
        return gameSettings;
    }

    private void broadcastSettingsToPlayers() {
        // Broadcast gameSettings to all connected players via WebSocket
    }
}