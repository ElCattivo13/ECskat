package io.github.elcattivo13.ecskat.pojos;

import java.util.HashMap;
import java.util.Map;

public class SpielResult{
    
    private int punkteAlleinspieler;
    private Player alleinspieler;
    private int punkteGegenspieler;
    private Map<Player, Integer> wertungen = new HashMap<>();
    private boolean eingemischt;
    private Game game;
    private GameLevel gameLevel;
    private boolean gespaltenes;
    private List<Card> skat = new ArrayList<>();
    private Map<Player, List<Card>> ausgeteilteKarten = new HashMap<>();
    
    public List<Card> getSkat(){
        return this.skat;
    }
    
    public void setSkat(List<Card> skat){
        this.skat = skat;
    }
    
    public Map<Player, List<Card>> getAusgeteilteKarten(){
        return this.ausgeteilteKarten;
    }
    
    public void setAusgeteilteKarten(Map<Player, List<Card>> ausgeteilteKarten){
        this.ausgeteilteKarten = ausgeteilteKarten;
    }
    
    public void putAusgeteilteKarten(Player spieler, List<Card> karten) {
        this.ausgeteilteKarten.put(spieler, karten);
    }
    
    public Map<Player, Integer> getWertungen(){
        return this.wertungen;
    }
    
    public void setWertungen(Map<Player, Integer> wertungen){
        this.wertungen = wertungen;
    }
    
    public void putWertung(Player player, int wertung) {
        this.wertungen.put(player, wertung);
    }
    
    public void setPunkteAlleinspieler(int punkteAlleinspieler) {
        this.punkteAlleinspieler = punkteAlleinspieler;
    }
    
    public int getPunkteAlleinspieler() {
        return this.punkteAlleinspieler;
    }
    
    public void setPunkteGegenspieler(int punkteGegenspieler) {
        this.punkteGegenspieler = punkteGegenspieler;
    }
    
    public int getPunkteGegenspieler() {
        return this.punkteGegenspieler;
    }
    
    public void setEingemischt(boolean eingemischt) {
        this.eingemischt = eingemischt;
    }
    
    public boolean isEingemischt() {
        return this.eingemischt;
    }
    
    public void setGame(Game game){
        this.game = game;
    }
    
    public Game getGame(){
        return this.game;
    }
    
    public void setGameLevel(GameLevel gameLevel){
        this.gameLevel = gameLevel;
    }
    
    public GameLevel getGameLevel(){
        return this.gameLevel;
    }
    
    public void setGespaltenes(boolean gespaltenes){
        this.gespaltenes = gespaltenes;
    }
    
    public boolean isGespaltenes(){
        return this.gespaltenes;
    }

	public Player getAlleinspieler() {
		return alleinspieler;
	}

	public void setAlleinspieler(Player alleinspieler) {
		this.alleinspieler = alleinspieler;
	}
    
}
