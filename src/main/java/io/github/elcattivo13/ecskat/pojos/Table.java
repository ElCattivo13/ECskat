package io.github.elcattivo13.ecskat.pojos;

import static io.github.elcattivo13.ecskat.errorhandling.EcSkatException.Reason.PLAYER_ALREADY_AT_TABLE;
import static io.github.elcattivo13.ecskat.errorhandling.EcSkatException.Reason.SPIEL_ALREADY_STARTED;
import static io.github.elcattivo13.ecskat.errorhandling.EcSkatException.Reason.TABLE_IS_FULL;

import java.util.List;

import io.github.elcattivo13.ecskat.errorhandling.EcSkatException;

public class Table extends BaseObject {

	private static final long serialVersionUID = 6060804946738803L;
	
	private final String name;
    private final TableSettings settings;
    private List<Player> spieler;
    private int indexGeber = 0;
    private Spiel spiel;
    private boolean spielStarted;
    
    public Table(String name) {
        this(name, new TableSettings());
    }
    
    public Table(String name, TableSettings settings) {
        super();
        this.name = name;
        this.settings = settings;
    }
    
    public void addPlayer(Player player) throws EcSkatException {
        if (spieler.size() >= 4 || (!settings.isZuViertOk() && spieler.size() >= 3)) {
            throw new EcSkatException(TABLE_IS_FULL);
        } else if (spieler.contains(player)) {
            throw new EcSkatException(PLAYER_ALREADY_AT_TABLE);
        } else if (spielStarted) {
            throw new EcSkatException(SPIEL_ALREADY_STARTED);
        } else {
            spieler.add(player);
        }
    }
    
    public void removePlayer(Player player) {
        spieler.remove(player);
    }
    
    public void startNextSpiel() {
        spielStarted = true;
        spiel = new Spiel(
            this,
            spieler.get((indexGeber + 1) % spieler.size()),
            spieler.get((indexGeber + 2) % spieler.size()),
            spieler.get((indexGeber + 3) % spieler.size()),
            spieler.get((indexGeber - 1) % spieler.size()).getCutPosition()
        );
        indexGeber++;
    }
    
    
    
    
    
    
    
    
    
    
    public String getName() {
        return name;
    }
    
    public TableSettings getSettings() {
        return settings;
    }
    
    public List<Player> getSpieler(){
        return this.spieler;
    }
    
    public Spiel getSpiel() {
        return this.spiel;
    }
}