package io.github.elcattivo13.ecskat.pojos;

import static io.github.elcattivo13.ecskat.errorhandling.EcSkatException.Reason.KARTE_NOT_ALLOWED;

import java.util.Comparator;
import java.util.Optional;

import io.github.elcattivo13.ecskat.errorhandling.EcSkatException;
public class Stich {
    private final Game game;
    private final boolean readOnly;
    private Player winner;
    private Player spieler1;
    private Player spieler2;
    private Player spieler3;
    private Card karte1;
    private Card karte2;
    private Card karte3;
    
    public Stich(Game game) {
        this.game = game;
        this.readOnly = false;
        this.winner = null;
    }
    
    private Stich(Card karte1, Card karte2, Card karte3, Player winner) {
        this.karte1 = karte1;
        this.karte2 = karte2;
        this.karte3 = karte3;
        this.game = null;
        this.winner = winner;
        this.readOnly = true;
    }
    
    public Optional<Stich> cardPlayed(Player spieler, Card card) throws EcSkatException {
        if (readOnly) {
            return Optional.empty();
        }
        checkCard(spieler, card);
        spieler.playCard(card);
        if (spieler1 == null) {
            spieler1 = spieler;
            karte1 = card;
            return Optional.empty();
        } else if (spieler2 == null) {
            spieler2 = spieler;
            karte2 = card;
            return Optional.empty();
        } else {
            spieler3 = spieler;
            karte3 = card;
            Comparator<Card> comp = Card.getComparator(game);
            if (comp.compare(karte1, karte2) > 0) {
                winner = (comp.compare(karte1, karte3) > 0) ? spieler1 : spieler3;
            } else {
                winner = (comp.compare(karte2, karte3) > 0) ? spieler2 : spieler3;
            }
            Stich letzterStich = new Stich(karte1, karte2, karte3, winner);
            clear();
            winner.receiveStich(letzterStich, game);
            return Optional.of(letzterStich);
        }
    }
    
    private void checkCard(Player spieler, Card card) throws EcSkatException {
        if (karte1 != null && !karte1.isGleicheFarbe(card, game) && spieler.hasCardWithSameColor(karte1, game)) {
            throw new EcSkatException(KARTE_NOT_ALLOWED);
        }
    }
    
    private void clear() {
        karte1 = null;
        karte2 = null;
        karte3 = null;
        spieler1 = null;
        spieler2 = null;
        spieler3 = null;
        winner = null;
    }
    
    public Player getWinner() {
        return winner;
    }
    
    public Card getKarte1() {
        return karte1;
    }
    
    public Card getKarte2() {
        return karte2;
    }
    
    public Card getKarte3() {
        return karte3;
    }
    
}
