package fpt.project.bsmart.config.socket;

import com.sun.security.auth.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;
import java.util.UUID;

public class ClientHandshakeHandler extends DefaultHandshakeHandler {

    private final Logger logger = LoggerFactory.getLogger(ClientHandshakeHandler.class);

    @Override
    protected Principal determineUser(ServerHttpRequest req, WebSocketHandler weHandler, Map<String, Object> attributes) {
        final String randId = UUID.randomUUID().toString();
        logger.debug("{}",attributes.get("name"));
        logger.debug("User opened client unique ID {}, ipAddress {}",randId,req.getRemoteAddress());
        return new UserPrincipal(randId);
    }

}