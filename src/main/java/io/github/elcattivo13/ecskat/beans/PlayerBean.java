package io.github.elcattivo13.ecskat.beans;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import io.github.elcattivo13.ecskat.errorhandling.EcSkatException;
import io.github.elcattivo13.ecskat.pojos.Player;

@RequestScoped // @Stateless TODO welche Annotation in Quarkus? @RequestScoped
public class PlayerBean {
    
    @Inject
    PlayerCache playerCache;
    
    public String createPlayer(String name) {
        Player player = new Player(name);
        this.playerCache.addPlayer(player);
        return player.getId();
    }
    
    public List<Player> findAllPlayers() {
        return playerCache.findAllPlayers();
    }
    
    public Player findPlayer(String playerId) throws EcSkatException {
        return playerCache.findPlayer(playerId);
    }
    
    public void toggleReady(String playerId, boolean ready) throws EcSkatException {
        findPlayer(playerId).setReady(ready);
    }
    
    public void setCutPosition(String playerId, CutPosition pos) throws EcSkatException {
        findPlayer(playerId).setCutPosition(pos);
    }
}