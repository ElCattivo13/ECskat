package io.github.elcattivo13.ecskat.beans;

import static io.github.elcattivo13.ecskat.errorhandling.EcSkatException.Reason.UNKNOWN_PLAYER;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Singleton;

import io.github.elcattivo13.ecskat.errorhandling.EcSkatException;
import io.github.elcattivo13.ecskat.pojos.Player;

@ApplicationScoped
public class PlayerCache {
    private Map<String, Player> players = new HashMap<>();
    
    public void addPlayer(Player player) {
        this.players.put(player.getId(), player);
    }
    
    public void removePlayer(String playerId) {
        this.players.remove(playerId);
    }
    
    public Player findPlayer(String playerId) throws EcSkatException {
        if (playerId == null || this.players.get(playerId) == null) {
            throw new EcSkatException(UNKNOWN_PLAYER);
        }
        return this.players.get(playerId);
    }
    
    public List<Player> findAllPlayers() {
        return new ArrayList<>(players.values());
    }
}
