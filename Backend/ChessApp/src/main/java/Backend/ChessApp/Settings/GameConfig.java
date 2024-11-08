package Backend.ChessApp.Settings;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class GameConfig {

    @Bean
    public SettingGameStates settingGameStates() {
        return new SettingGameStates((short) 30, 1, true, false, 30);
    }
}


