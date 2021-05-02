package io.github.elcattivo13.ecskat.websocket;

import java.util.List;

import io.github.elcattivo13.ecskat.pojos.*;

public class SkatMessage {
    
	private final Key key;
    private List<Card> karten;
    private Player subject;
    private Action naechsteAktion;
    private int aktuellerReizwert;
    private SpielResult result;
    private Game game;
    private GameLevel gameLevel;
    private boolean ready;
    private Table table;
    
    public SkatMessage(Key key) {
        this.key = key;
    }
    
    public static SkatMessage of(Key key) {
        return new SkatMessage(key);
    }
    
    public List<Card> getKarten() {
		return karten;
	}

	public SkatMessage setKarten(List<Card> karten) {
		this.karten = karten;
		return this;
	}

	public Player getSubject() {
		return subject;
	}

	public SkatMessage setSubject(Player subject) {
		this.subject = subject;
		return this;
	}

	public Action getNaechsteAktion() {
		return naechsteAktion;
	}

	public SkatMessage setNaechsteAktion(Action naechsteAktion) {
		this.naechsteAktion = naechsteAktion;
		return this;
	}

	public int getAktuellerReizwert() {
		return aktuellerReizwert;
	}

	public SkatMessage setAktuellerReizwert(int aktuellerReizwert) {
		this.aktuellerReizwert = aktuellerReizwert;
		return this;
	}

	public SpielResult getResult() {
		return result;
	}

	public SkatMessage setResult(SpielResult result) {
		this.result = result;
		return this;
	}

	public Game getGame() {
		return game;
	}

	public SkatMessage setGame(Game game) {
		this.game = game;
		return this;
	}

	public GameLevel getGameLevel() {
		return gameLevel;
	}

	public SkatMessage setGameLevel(GameLevel gameLevel) {
		this.gameLevel = gameLevel;
		return this;
	}

	public boolean isReady() {
		return ready;
	}

	public SkatMessage setReady(boolean ready) {
		this.ready = ready;
		return this;
	}

	public Table getTable() {
		return table;
	}

	public SkatMessage setTable(Table table) {
		this.table = table;
		return this;
	}

	public Key getKey() {
		return key;
	}








	public static enum Key {
        KARTEN_BEKOMMEN,
        WER_IST_DRAN,
        AKTUELLER_REIZWERT,
        SKAT_AUFGENOMMEN,
        SPIELANSAGE,
        ANSAGE_ERHOEHEN,
        SPIELRESULTAT,
        KONTRA_GESAGT,
        RE_GESAGT,
        READY,
        NEW_TABLE,
        TABLE_JOINED,
        TABLE_LEFT
    }
}