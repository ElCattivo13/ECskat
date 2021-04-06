import io.github.elcattivo13.pojos.Player;
import io.github.elcattivo13.pojos.Table;
import io.github.elcattivo13.pojos.TableSettings;

@Stateless
public class TableBean {
    
    @Inject
    private TableCache tableCache;
    
    @Inject 
    private PlayerCache playerCache;
    
    public String createTable(String name, String creatorId, TableSettings settings) throws UnknownPlayerException {
        Table table;
        if (settings == null) {
            table = new Table(name);
        } else {
            table = new Table(name, settings);
        }
        this.tableCache.addTable(table);
        playerCache.findPlayer(creatorId).joinTable(table);
        return table.getId();
    }
    
    public void joinTable(String playerId, String tableId) throws EcSkatException {
        playerCache.findPlayer(playerId).joinTable(findTable(tableId));
    }
    
    public List<Table> findAllTables() {
        return tableCache.findAllTables();
    }
    
    
    public xxx playCard(String playerId, String tableId, String blatt, String farbe) {
        Player p = playerCache.findPlayer(playerId);
        Table t = findTable(tableId);
        checkPlayerAtTable(p,t);
        Spiel s = findSpiel(t);
        Card c = new Card(Farbe.of(farbe), Blatt.of(blatt));
        Optional<SpielResult> res = s.karteSpielen(p, c);
        
        // TODO was tun mit dem SpielResult
        
    }
    
    
    
    
    /*
        private helper functions
    */
    
    private Table findTable(String tableId) throws UnknownTableException {
        if (tableId == null) {
            throw new UnknownTableException();
        }
        return this.tableCache.getTable(tableId).orElseThrow(UnknownTableException::new);
    }
    
    private void checkPlayerAtTable(Player player, Table table) {
        if (!table.getSpieler().contains(PlayerNotAtTableException)) {
            throw new PlayerNotAtTableException();
        }
    }
    
    private Spiel findSpiel(Table t) {
        if (t.getSpiel() == null) {
            throw new SpielNotStartedException();
        } else {
            return t.getSpiel();
        }
    }
}
