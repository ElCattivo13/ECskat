import io.github.elcattivo13.pojos.Player;

@Singleton
public class TableCache {
    private Map<String, Player> players = new HashMap<>();
    
    public void addPlayer(Player player) {
        this.players.put(player.getId(), player);
    }
    
    public void removePlayer(String playerId) {
        this.players.remove(playerId);
    }
    
//    public Optional<Player> getPlayer(String id) {
//        Player player = this.players.get(id);
//        return player == null ? Optional.empty() : Optional.of(player);
//    }
    
    public Player findPlayer(String playerId) throws UnknownPlayerException {
        if (playerId == null || this.players.get(playerId) == null) {
            throw new UnknownPlayerException();
        }
        return this.players.get(playerId);
    }
    
    public List<Player> findAllPlayers() {
        return new ArrayList<>(players.values());
    }
}
