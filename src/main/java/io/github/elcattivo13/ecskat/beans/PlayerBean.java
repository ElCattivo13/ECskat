package io.github.elcattivo13.ecskat.beans;



@Stateless
public class PlayerBean {
    
    @Inject
    private PlayerCache playerCache;
    
    public String createPlayer(String name) {
        Player player = new Player(name);
        this.playerCache.addPlayer(player);
        return player.getId();
    }
    
    public List<Player> findAllPlayers() {
        return playerCache.findAllPlayers();
    }
    
    public Player findPlayer(String playerId) throws UnknownPlayerException {
        return playerCache.findPlayer(playerId);
    }
    
    public void toggleReady(String playerId, boolean ready) throws UnknownPlayerException {
        findPlayer(playerId).setReady(ready);
    }
}