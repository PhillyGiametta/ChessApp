package Backend.ChessApp;

import Backend.ChessApp.Settings.SettingGameStates;
import Backend.ChessApp.Users.User;

public class GameSession {
    private SettingGameStates gameSettings;
    private User admin;

    public void setAdmin(User admin) {
        this.admin = admin;
    }

    public boolean isAdmin(User user) {
        return user.equals(admin);
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