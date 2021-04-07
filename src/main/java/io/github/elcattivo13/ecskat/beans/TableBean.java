

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
        findPlayer(creatorId).joinTable(table);
        return table.getId();
    }
    
    public void joinTable(String playerId, String tableId) throws EcSkatException {
        findPlayer(playerId).joinTable(findTable(tableId));
    }
    
    public List<Table> findAllTables() {
        return tableCache.findAllTables();
    }
    
    
    public xxx playCard(String playerId, String tableId, String blatt, String farbe) throws EcSkatException {
        PlayerTableSpiel pts = findPojosWithSpiel(playerId, tableId);
        Card karte = new Card(Farbe.of(farbe), Blatt.of(blatt));
        Optional<SpielResult> res = pts.spiel.karteSpielen(pts.spieler, karte);
        
        // TODO was tun mit dem SpielResult
        
    }
    
    public void austeilen(String playerId, String tableId) throws EcSkatException {
        PlayerTable pt = findPojosWithoutSpiel(playerId, tableId);
        pt.table.startNextGame(pt.player);
    }
    
    public void sagen(String playerId, String tableId, Integer reizwert) throws EcSkatException {
        PlayerTableSpiel pts = findPojosWithSpiel(playerId, tableId);
        Optional<SpielResult> res = pts.spiel.reizenSagen(pts.player, reizwert);
        if (res.isPresent()) {
            // TODO Spiel wurde eingemischt, wie weiter?
        }
    }
    
    public void hoeren(String playerId, String tableId, boolean ja) throws EcSkatException {
        PlayerTableSpiel pts = findPojosWithSpiel(playerId, tableId);
        ptsspiel.reizenHoeren(pts.player, ja);
    }
    
    public void skatAufnehmen(Sting playerId, String tableId) throws EcSkatException {
        PlayerTableSpiel pts = findPojosWithSpiel(playerId, tableId);
        pts.spiel.skatAufnehmen(pts.player);
    }
    
    
    /*
        private helper functions
    */
    
    private PlayerTable findPojosWithoutSpiel(String playerId, String tableId) throws EcSkatException {
        PlayerTable res = new PlayerTable();
        res.player = findPlayer(playerId);
        res.table = findTable(tableId);
        checkPlayerAtTable(res);
        return res;
    }
    
    private PlayerTableSpiel findPojosWithSpiel(String playerId, String tableId) throws EcSkatException {
        PlayerTableSpiel res = new PlayerTableSpiel(findPojosWithoutSpiel(playerId, tableId));
        res.spiel = findSpiel(res.table);
        return res;
    }
    
    private Table findTable(String tableId) throws UnknownTableException {
        if (tableId == null) {
            throw new UnknownTableException();
        }
        return this.tableCache.getTable(tableId).orElseThrow(UnknownTableException::new);
    }
    
    private Player findPlayer(String playerId) throws UnknownPlayerException {
        rwturn playerCache.findPlayer(playerId);
    }
    
    private void checkPlayerAtTable(PlayerTable pt) throws PlayerNotAtTableExceptionb {
        if (!pt.table.getSpieler().contains(pt.player)) {
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
    
    private static class PlayerTable {
        private Player player;
        private Table table;
        
        public PlayerTable() {}
        
        public PlayerTable(PlayerTable that) {
            this.player = that.player;
            this.table = that.table;
        }
    }
    
    private static class PlayerTableSpiel extends PlayerTable {
        private Spiel spiel;
        
        public PlayerTableSpiel(PlayerTable pt) {
            super(pt);
        }
    }
}
