package Backend.ChessApp.Game;

import jakarta.websocket.server.ServerEndpointConfig;

public class SpringConfigurator extends ServerEndpointConfig.Configurator {

    @Override
    public <T> T getEndpointInstance(Class<T> endpointClass) throws InstantiationException {
        return SpringContextProvider.getBean(endpointClass);
    }
}