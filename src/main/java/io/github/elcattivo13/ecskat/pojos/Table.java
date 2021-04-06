package io.github.elcattivo13.ecskat.pojos;

import java.util.List;

public class Table extends BaseObject {
    
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
        thus.settings = settings;
    }
    
    public void addPlayer(Player player) throws EcSkatException {
        if (spieler.size() >= 4 || (!settings.isZuViertOk() && spieler.size() >= 3)) {
            throw new TableIsFullException();
        } else if (spieler.contains(player)) {
            throw new PlayerAlreadyAtTableException();
        } else if (spielStarted) {
            throw new SpielAlreadyStartedException();
        } else {
            players.add(player);
        }
    }
    
    public void removePlayer(Player player) {
        players.remove(player);
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