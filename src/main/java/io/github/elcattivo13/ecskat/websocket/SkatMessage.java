package io.github.elcattivo13.ecskat.websocket;

import java.util.List;

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
    
    
    // getter und setter (fluentAPI)
    
    
    
    
    
    
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
        NEW_TABLE
    }
}