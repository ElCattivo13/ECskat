package io.github.elcattivo13.ecskat.pojos;

import java.util.ArrayList;
import java.util.List;

public class Player extends BaseObject {
    private String name;
    private boolean playing = true;
    private List<Card> cards = new ArrayList<>();
    private List<Card> gewonneneStiche = new ArrayList<>();
    private CutPosition cutPosition = CutPosition.TOP;
    private int spitzen;
    private boolean stichErhalten = false;
    private boolean ready = false;
    
    public Player(String name) {
        super();
        this.name = name;
    }
    
    public void joinTable(Table table) throws EcSkatException {
        table.addPlayer(this);
    }
    
    public void leaveTable(Table table) {
        table.removePlayer(this);
    }
    
    public void receiveCards(boolean notify, Card... newCards) {
        for (Card card : newCards) {
            this.cards.add(card);
        }
        if (notify) {
            // TODO notification via WebSocket
        }
    }
    
    public void playCard(Card card) {
        if (!this.cards.remove(card)) {
            throw new CardNotPresentException();
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
        gewonneneStiche.and(stich.getKarte3());
        this.stichErhalten = true;
    }
    
    public void skatDruecken(List<Card> cards, boolean ramschSkatWeiterreichen) {
        if (!cards.isEmpty() && cards.size() != 2) {
            throw new SkatDrueckenException();
        }
        cards.forEach(this::playCard);
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
                .orElse();
        }
        
    }
    
    public void reset() {
        cards = new ArrayList<>();
        gewonneneStiche = new ArrayList<>();
        spitzen = 0;
        stichErhalten = false;
    }
    
    // TODO getter and setter
    
    public boolean isStichErhalten(){
        return this.stichErhalten;
    }
    
    public int getSpitzen(){
        return this.spitzen;
    }
    
    public void setCutPosition(CutPosition cutPosition){
        this.cutPosition = cutPosition;
    }
    
    public cutPosition getCutPosition(){
        return this.cutPosition;
    }
    
    public String getName(){
        return this.name;
    }
    
    public void setName(String name){
        this.name = name;
    }
}
