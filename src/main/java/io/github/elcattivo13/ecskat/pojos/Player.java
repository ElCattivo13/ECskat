package io.github.elcattivo13.ecskat.pojos;

import static io.github.elcattivo13.ecskat.errorhandling.EcSkatException.Reason.CARD_NOT_PRESENT;
import static io.github.elcattivo13.ecskat.errorhandling.EcSkatException.Reason.INVALID_SKAT_SIZE;
import static io.github.elcattivo13.ecskat.websocket.SkatMessage.Key.KARTEN_BEKOMMEN;
import static io.github.elcattivo13.ecskat.websocket.SkatMessage.Key.TABLE_JOINED;
import static io.github.elcattivo13.ecskat.websocket.SkatMessage.Key.TABLE_LEFT;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.github.elcattivo13.ecskat.errorhandling.EcSkatException;
import io.github.elcattivo13.ecskat.websocket.SkatMessage;

public class Player extends BaseObject {

	private static final long serialVersionUID = 2619678530876426782L;
	private static final Logger log = LoggerFactory.getLogger(Player.class);
	
	private String name;
    private Table table;
    private List<Card> cards = new ArrayList<>();
    private List<Card> gewonneneStiche = new ArrayList<>();
    private CutPosition cutPosition = CutPosition.TOP;
    private int spitzen;
    private boolean stichErhalten = false;
    private boolean ready = false;
    private boolean achtzehnGesagt = false;
    
    public Player(String name) {
        super();
        this.name = name;
    }
    
    public void joinTable(Table table) throws EcSkatException {
    	log.info("joinTable - table: {}", table);
        table.addPlayer(this);
        setTable(table);
        getWebsocket().sendToPlayers(
            SkatMessage.of(TABLE_JOINED).setSubject(this),
            table.getSpieler());
    }
    
    public void leaveTable(Table table) throws EcSkatException {
        table.removePlayer(this);
        setTable(null);
        getWebsocket().sendToPlayers(
            SkatMessage.of(TABLE_LEFT).setSubject(this),
            table.getSpieler());
    }
    
    public void receiveCards(boolean notify, Card... newCards) throws EcSkatException {
        for (Card card : newCards) {
            this.cards.add(card);
        }
        if (notify) {
            getWebsocket().sendToPlayer(
                SkatMessage.of(KARTEN_BEKOMMEN).setKarten(getCards()),
                this);
        }
    }
    
    public void playCard(Card card) throws EcSkatException {
        if (!this.cards.remove(card)) {
            throw new EcSkatException(CARD_NOT_PRESENT);
        }
        
    }
    
    public boolean hasCardWithSameColor(Card card, Game game) {
        return cards.stream().anyMatch(c -> c.isGleicheFarbe(card, game));
    }
    
    public int sumPoints() {
        return this.gewonneneStiche.stream().mapToInt(Card::punkte).sum();
    }
    
    public void receiveStich(Stich stich, Game game) {
        gewonneneStiche.add(stich.getKarte1());
        gewonneneStiche.add(stich.getKarte2());
        gewonneneStiche.add(stich.getKarte3());
        this.stichErhalten = true;
    }
    
    public void skatDruecken(List<Card> cards) throws EcSkatException {
    	skatDruecken(cards, false);
    }
    
    public void skatDruecken(List<Card> cards, boolean ramschSkatWeiterreichen) throws EcSkatException {
        if (!cards.isEmpty() && cards.size() != 2) {
            throw new EcSkatException(INVALID_SKAT_SIZE);
        }
        for (Card card : cards) {
			playCard(card);
		}
        if (!ramschSkatWeiterreichen) {
            gewonneneStiche.addAll(cards);
        }
    }
    
    public void calcSpitzen(Game game) {
        if (game.isNullSpiel()) {
            spitzen = 0;
        }
        List<Card> alleTruempfe = Card.getDeck().stream()
            .filter(card -> card.isTrumpf(game))
            .sorted(Card.getComparator(game).reversed())
            .collect(Collectors.toList());
        List<Integer> eigeneTruempfe = cards.stream()
            .filter(card -> card.isTrumpf(game))
            .map(alleTruempfe::indexOf)
            .sorted()
            .collect(Collectors.toList());
        if (!eigeneTruempfe.contains(0)) {
            spitzen = eigeneTruempfe.get(0);
        } else {
            spitzen = IntStream.range(0,11)
                .filter(i -> !eigeneTruempfe.contains(i))
                .findFirst()
                .orElse(11);
        }
        
    }
    
    public void reset() {
        cards = new ArrayList<>();
        gewonneneStiche = new ArrayList<>();
        spitzen = 0;
        setStichErhalten(false);
        setReady(false);
        setAchtzehnGesagt(false);
    }
    
    public boolean karteGespielt() {
        return cards.size() < 10;
    }
    
    // TODO getter and setter
    
    @JsonIgnore
    public boolean isStichErhalten(){
        return this.stichErhalten;
    }
    
    @JsonIgnore
    public void setStichErhalten(boolean stichErhalten) {
      this.stichErhalten = stichErhalten;
    }
    
    @JsonIgnore
    public int getSpitzen(){
        return this.spitzen;
    }
    
    public void setCutPosition(CutPosition cutPosition){
        this.cutPosition = cutPosition;
    }
    
    public CutPosition getCutPosition(){
        return this.cutPosition;
    }
    
    public String getName(){
        return this.name;
    }
    
    public void setName(String name){
        this.name = name;
    }

	public boolean isReady() {
		return ready;
	}

	public void setReady(boolean ready) {
		this.ready = ready;
	}
    
    public boolean isAchtzehnGesagt(){
        return this.achtzehnGesagt;
    }
    
    public void setAchtzehnGesagt(boolean achtzehnGesagt){
        this.achtzehnGesagt = achtzehnGesagt;
    }
    
    @JsonIgnore
    public List<Card> getCards(){
        return this.cards;
    }
    
    @JsonIgnore
    public Table getTable(){
        return this.table;
    }
    
    @JsonIgnore
    public void setTable(Table table){
        this.table = table;
    }
}
