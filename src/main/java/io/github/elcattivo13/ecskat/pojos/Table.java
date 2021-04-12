package io.github.elcattivo13.ecskat.pojos;

import static io.github.elcattivo13.ecskat.errorhandling.EcSkatException.Reason.*;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.elcattivo13.ecskat.errorhandling.EcSkatException;
public class Table extends BaseObject {

	private static final long serialVersionUID = 6060804946738803L;
	private static final Logger log = LoggerFactory.getLogger(Table.class);
	
	private final String name;
    private final TableSettings settings;
    private List<Player> spieler = new ArrayList<>();
    private int indexGeber = 0;
    private Spiel spiel;
    private List<SpielResult> wertungen = new ArrayList<>();
    
    public Table(String name) {
        this(name, new TableSettings());
        log.info("Table constructor without settings called");
        
    }
    
    public Table(String name, TableSettings settings) {
        super();
        this.name = name;
        this.settings = settings;
        this.spieler = new ArrayList<>();
        log.info("Table constructor with settings called: {}", settings);
    }
    
    public void addPlayer(Player player) throws EcSkatException {
        if (spieler.size() >= 4 || (!settings.isZuViertOk() && spieler.size() >= 3)) {
            throw new EcSkatException(TABLE_IS_FULL);
        } else if (spieler.contains(player)) {
            throw new EcSkatException(PLAYER_ALREADY_AT_TABLE);
        } else if (spiel != null || !wertungen.isEmpty()) {
            throw new EcSkatException(SPIEL_ALREADY_STARTED);
        } else {
            spieler.add(player);
        }
    }
    
    public void removePlayer(Player player) {
        spieler.remove(player);
    }
    
    public void startNextSpiel(Player geber) throws EcSkatException {
        
        if (spieler.size() < 3) {
            throw new EcSkatException(NOT_ENOUGH_PLAYER_AT_TABLE);
        } else if (!spieler.get(indexGeber).equals(geber)) {
            throw new EcSkatException(NOT_YOUR_TURN);
        }
        
        Player vorhand = spieler.get((indexGeber + 1) % spieler.size());
        Player mittelhand = spieler.get((indexGeber + 2) % spieler.size());
        Player hinterhand = spieler.get((indexGeber + 3) % spieler.size());
        
        if (!vorhand.isReady() || !mittelhand.isReady() || !hinterhand.isReady()) {
          throw new EcSkatException(PLAYER_NOT_READY);
        }
        
        spiel = new Spiel(this, vorhand, mittelhand, hinterhand,
            spieler.get((indexGeber + spieler.size() - 1) % spieler.size()).getCutPosition()
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
    
    public List<SpielResult> getWertungen(){
      return this.wertungen;
    }
    
    public void setWertungen(List<SpielResult> wertungen){
      this.wertungen = wertungen;
    }
    
    public void addWertung(SpielResult wertung){
      this.wertungen.add(wertung);
    }

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Table [");
		if (name != null)
			builder.append("name=").append(name).append(", ");
		if (settings != null)
			builder.append("settings=").append(settings).append(", ");
		if (spieler != null)
			builder.append("spieler=").append(spieler).append(", ");
		builder.append("indexGeber=").append(indexGeber).append(", ");
		if (spiel != null)
			builder.append("spiel=").append(spiel).append(", ");
		builder.!=append("]");
		return builder.toString();
	}
    
    
}