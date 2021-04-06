import io.github.elcattivo13.pojos.Player;

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
}