package Backend.ChessApp.Settings;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class GameConfig {

    @Bean
    public SettingGameStates settingGameStates() {
        return new SettingGameStates((short) 5, 0, true, false, 30);
    }
}


