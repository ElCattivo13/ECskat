package io.github.elcattivo13.ecskat.beans;

import static io.github.elcattivo13.ecskat.errorhandling.EcSkatException.Reason.PLAYER_NOT_AT_TABLE;
import static io.github.elcattivo13.ecskat.errorhandling.EcSkatException.Reason.SPIEL_ALREADY_STARTED;

import java.util.List;
import java.util.Optional;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.elcattivo13.ecskat.errorhandling.EcSkatException;
import io.github.elcattivo13.ecskat.pojos.Blatt;
import io.github.elcattivo13.ecskat.pojos.Card;
import io.github.elcattivo13.ecskat.pojos.Farbe;
import io.github.elcattivo13.ecskat.pojos.Player;
import io.github.elcattivo13.ecskat.pojos.Spiel;
import io.github.elcattivo13.ecskat.pojos.SpielResult;
import io.github.elcattivo13.ecskat.pojos.Table;
import io.github.elcattivo13.ecskat.pojos.TableSettings;

@RequestScoped //@Stateless TODO welche Annotation in Quarkus? @RequestScoped
public class TableBean {
    
	private static final Logger log = LoggerFactory.getLogger(TableBean.class);
	
    @Inject
    TableCache tableCache;
    
    @Inject 
    PlayerCache playerCache;
    
    public String createTable(String name, String creatorId, TableSettings settings) throws EcSkatException {
        Table table;
        log.info("Name: {}, creatorId: {}, settings: {}", name, creatorId, settings);
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
    
    
    public void playCard(String playerId, String tableId, String blatt, String farbe) throws EcSkatException {
        PlayerTableSpiel pts = findPojosWithSpiel(playerId, tableId);
        Card karte = new Card(Farbe.of(farbe), Blatt.of(blatt));
        Optional<SpielResult> res = pts.spiel.karteSpielen(pts.player, karte);
        if (res.isPresent()) {
          pts.table.addWertung(res.get());
          // TODO notify player about game result
        }
    }
    
    public void austeilen(String playerId, String tableId) throws EcSkatException {
        PlayerTable pt = findPojosWithoutSpiel(playerId, tableId);
        pt.table.startNextSpiel(pt.player);
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
        pts.spiel.reizenHoeren(pts.player, ja);
    }
    
    public void skatAufnehmen(String playerId, String tableId) throws EcSkatException {
        PlayerTableSpiel pts = findPojosWithSpiel(playerId, tableId);
        pts.spiel.skatAufnehmen(pts.player);
    }
    
    public void kontraSagen(String playerId, String tableId) throws EcSkatException {
        PlayerTableSpiel pts = findPojosWithSpiel(playerId, tableId);
        pts.spiel.kontraSagen(pts.player);
    }
    
    public void reSagen(String playerId, String tableId) throws EcSkatException {
        PlayerTableSpiel pts = findPojosWithSpiel(playerId, tableId);
        pts.spiel.reSagen(pts.player);
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
    
    private Table findTable(String tableId) throws EcSkatException {
        return this.tableCache.findTable(tableId);
    }
    
    private Player findPlayer(String playerId) throws EcSkatException {
        return playerCache.findPlayer(playerId);
    }
    
    private void checkPlayerAtTable(PlayerTable pt) throws EcSkatException {
        if (!pt.table.getSpieler().contains(pt.player)) {
            throw new EcSkatException(PLAYER_NOT_AT_TABLE);
        }
    }
    
    private Spiel findSpiel(Table t) throws EcSkatException {
        if (t.getSpiel() == null) {
            throw new EcSkatException(SPIEL_ALREADY_STARTED);
        } else {
            return t.getSpiel();
        }
    }
    
    private static class PlayerTable {
        public Player player;
        public Table table;
        
        public PlayerTable() {}
        
        public PlayerTable(PlayerTable that) {
            this.player = that.player;
            this.table = that.table;
        }
    }
    
    private static class PlayerTableSpiel extends PlayerTable {
        public Spiel spiel;
        
        public PlayerTableSpiel(PlayerTable pt) {
            super(pt);
        }
    }
}
