package io.github.elcattivo13.ecskat.websocket;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.enterprise.context.ApplicationScoped;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.Session;

@ServerEndpoint("/ws/{userId}")         
@ApplicationScoped
public class SkatWebsocket {

    private static final Logger log = LoggerFactory.getLogger(SkatWebsocket.class);

    Map<String, Session> sessions = new ConcurrentHashMap<>(); 

    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId) {
        sessions.put(userId, session);
        log.info("User {} joined", userId);
    }

    @OnClose
    public void onClose(Session session, @PathParam("userId") String userId) {
        sessions.remove(userId);
        log.info("User {} left", userId);
    }

    @OnError
    public void onError(Session session, @PathParam("userId") String userId, Throwable throwable) {
        sessions.remove(userId);
        log.warn("User {} left on error: {}", userId, throwable);
    }

    @OnMessage
    public void onMessage(SkatMessage message, @PathParam("userId") String userId) {
        if (message == null) {
            // TODO errorhandling
        }
        switch (message.getKey()) {
            case xxx: 
                // do something
                break;
            default:
                log.warn("Unknown message key: {}. Ignoring message!", message.getKey());
        }
            
    }

    public void sendToAll(SkatMessage message) {
        sessions.entrySet().forEach(entry -> {
            entry.getValue().getAsyncRemote().sendObject(message, result ->  {
                if (result.getException() != null) {
                    log.error("Unable to send message {} to player {} due to {}", message, entry.getKey(), result.getException());
                }
            });
        });
    }
    
    public void sendToPlayers(SkatMessage msg, List<Player> players) {
        players.forEach(p -> sendToPlayer(msg, p));
    }
    
    public void sendToPlayer(SkatMessage msg, Player player) {
        Session session = sessions.get(player.getId());
        if (session != null) {
            session.getAsyncRemote().sendObject(msg, result -> {
                if (result.getException() == null) {
                    log.debug("Message with key {} sent successfully to {}", msg.getKey(), player.getName());
                } else {
                    log.error("Unable to send message {} to player {} due to {}", msg, player.getName(), result.getException());
                }
            });
        } else {
            log.error("Cannot find player {} ({}). Giving up! {}", player.getName(), player.getId(), msg);
        }
    }

}