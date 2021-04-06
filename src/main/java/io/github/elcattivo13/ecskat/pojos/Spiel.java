package io.github.elcattivo13.ecskat.pojo

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Spiel extends BaseObject {
    
    private static final Logger log = LoggerFactory.getLogger(Spiel.class);
    
    private static final List<Integer> reizwerte = Arrays.asList(0, 18, 20, 22, 23, 24, 27, 30, 33, 35, 36, 40, 44, 45, 46, 48, 50, 54, 55, 59, 60, 63, 66, 70, 72, 77, 80, 81, 84, 88, 90, 96, 99, 100);
    
    private final Table table;
    private final Player vorhand;
    private final Player mittelhand;
    private final Player hinterhand;
    private LinkedList<Card> skat = new LinkedList<>();
    private Player werIstDran;
    private Action naechsteAktion;
    //private Set<AbstractMap.SimpleEntry<Player, Action>> expectedNextActions = new HashSet<>();
    private AbstractMap.SimpleEntry<Player, Integer> aktuellerReizwert = new AbstractMap.SimpleEntry<>(null, 17);
    private Game spiel;
    private GameLevel spielLevel;
    private Stich aktuellerStich;
    private Stich letzterStich;
    private int sticheGespielt = 0;
    
    public Spiel(Table table, Player vorhand, Player mittelhand, Player hinterhand, CutPosition cutPosition) {
        super();
        this.table = table;
        this.vorhand = vorhand;
        this.mittelhand = mittelhand;
        this.hinterhand = hinterhand;
        
        kartenAusteilen(cutPosition);
        
        next(mittelhand, Action.REIZEN_SAGEN);
    }
    
    private void kartenAusteilen(CutPosition cutPos) {
        LinkedList<Card> deck = new LinkedList<>(Card.getDeck());
        Card.shuffleDeck(deck);
        Card.cutDeck(deck, cutPos);
        
        vorhand.receiveCards(false, deck.poll(), deck.poll(), deck.poll());
        mittelhand.receiveCards(false, deck.poll(), deck.poll(), deck.poll());
        hinterhand.receiveCards(false, deck.poll(), deck.poll(), deck.poll());
        
        skat.add(deck.poll());
        skat.add(deck.poll());
        
        vorhand.receiveCards(false, deck.poll(), deck.poll(), deck.poll(), deck.poll());
        mittelhand.receiveCards(false, deck.poll(), deck.poll(), deck.poll(), deck.poll());
        hinterhand.receiveCards(false, deck.poll(), deck.poll(), deck.poll(), deck.poll());
        
        vorhand.receiveCards(true, deck.poll(), deck.poll(), deck.poll());
        mittelhand.receiveCards(true, deck.poll(), deck.poll(), deck.poll());
        hinterhand.receiveCards(true, deck.poll(), deck.poll(), deck.poll());
    }
    
    private void next(Player spieler, Action action) {
        werIstDran = spieler;
        naechsteAktion = action;
        // TODO notification via WebSocket
    }
    
    private void checkAction(Player spieler, Action action) throws NotYourTurnException, WrongActionException {
        if (!werIstDran.equals(spieler)) {
            throw new NotYourTurnException();
        }
        if (naechsteAktion != action) {
            throw new WrongActionException();
        }
    }
    
    public Optional<SpielResult> reizenSagen(Player spieler, int reizwert) {
        checkAction(spieler, Action.REIZEN_SAGEN);
        if (!reizwerte.contains(reizwert)) {
            throw new IllegalReizwertException();
        }
        if (reizwert > aktuellerReizwert.getValue()) {
            next((aktuellerReizwert.getKey() != null) ? aktuellerReizwert.getKey() : vorhand, (!spieler.equals(vorhand)) ? Action.REIZEN_HOEREN : Action.SPIEL_ANSAGEN);
            aktuellerReizwert = new AbstractMap.SimpleEntry<>(spieler, reizwert);
        } else {
            // Spieler hat weggesagt
            if (spieler.equals(vorhand)) {
                if (table.getSettings().isWithRamsch()) {
                    game = Game.RAMSCH;
                    vorhand.receiveCards(true, skat.poll(),skat.poll());
                    next(vorhand, Action.RAMSCH_SKAT_WEITERREICHEN);
                } else {
                    SpielResult res = new SpielResult();
                    res.setEingemischt(true);
                    return Optional.of(res);
                }
            } else if (spieler.equals(mittelhand)) {
                werIstDran = hinterhand;
                naechsteAktion = Action.REIZEN_SAGEN;
            } else if (spieler.equals(hinterhand)) {
                if (aktuellerReizwert.getKey() != null) {
                    naechsteAktion = Action.SPIEL_ANSAGEN;
                    werIstDran = aktuellerReizwert.getKey();
                } else {
                    naechsteAktion = Action.REIZEN_SAGEN;
                    werIstDran = vorhand;
                }
            } else {
                throw new UnexpectedPlayerException();
            }
        }
        return Optional.empty();
    }
    
    public void ramschSkatWeiterreichen(Player spieler, List<Card> karten) {
        checkAction(spieler, Action.RAMSCH_SKAT_WEITERREICHEN);
        if (karten.size() != 2) {
            throw new EcSkatException();
        }
        if (table.getSettings().isRamschUnterDrueckenVerboten() && karten.stream().filter(c -> c.value == Value.UNTER).findAny().isPresent()) {
            throw new EcSkatException();
        }
        
        if (spieler.equals(vorhand)) {
            spieler.skatDruecken(karten, true);
            mittelhand.receiveCards(karten);
            next(mittelhand, Action.RAMSCH_SKAT_WEITERREICHEN)
        } else if (spieler.equals(mittelhand)) {
            spieler.skatDruecken(karten, true);
            hinterhand.receiveCards(karten);
            next(hinterhand, Action.RAMSCH_SKAT_WEITERREICHENk);
        } else if (spieler.equals(hinterhand)) {
            karten.forEach(spieler::playCard);
            skat.addAll(karten);
            next(vorhand. Action.KARTE_SPIELEN;
        } else {
            throw new EcSkatException();
        }
    }
    
    public void reizenHoeren(Player spieler, boolean ja) {
        checkAction(spieler, Action.REIZEN_HOEREN);
        if (ja) {
            next(aktuellerReizwert.getKey(), Action.REIZEN_SAGEN);
            aktuellerReizwert = new AbstractMap.SimpleEntry<>(spieler, aktuellerReizwert.getValue());
        } else {
            // Spieler hat weggesagt
            next(hinterhand, (aktuellerReizwert.getKey() == mittelhand) ? Action.REIZEN_SAGEN : Action.SPIEL_ANSAGEN;
        }
    }
    
    public void skatAufnehmen(Player spieler) {
        checkAction(spieler, Action.SPIEL_ANSAGEN);
        spieler.receiveCards(skat.poll(), skat.poll());
    }
    
    public void spielAnsagen(Player spieler, Game spiel, GameLevel gameLevel, List<Card> gedrueckteKarten) {
        checkAction(spieler, Action.SPIEL_ANSAGEN);
        if (skat.isEmpty() && gameLevel != GameLevel.NORMAL) {
            throw new HandSpielNotAllowedException();
        }
        this.game = game;
        this.gameLevel = gameLevel;
        this.aktuellerReizwert = new AbstractMap.SimpleEntry<>(spieler, aktuellerReizwert.getValue());
        if (!game.isNullSpiel()) {
            spieler.calcSpitzen(game);
        }
        spieler.skatDruecken(gedrueckteKarten);
        this.aktuellerStich = new Stich(game);
        this.werIstDran = vorhand;
        this.naechsteAktion = Action.KARTE_SPIELEN;
    }
    
    public Optional<SpielResult> karteSpielen(Player spieler, Card karte) throws EcSkatException {
        checkAction(spieler, Action.KARTE_SPIELEN);
        Optional<Stich> result = aktuellerStich.cardPlayed(spieler, karte);
        if (!result.isPresent()) {
            next(vorhand.equals(spieler) ? mittelhand : (mittelhand.equals(spieler) ? hinterhand : vorhand), Action.KARTE_SPIELEN);
        } else {
            letzterStich = result.get();
            sticheGespielt++;
            if (sticheGespielt < 10) {
                next(letzterStich.getWinner(), Action.KARTE_SPIELEN);
            } else {
                return Optional.of(spielAuswerten());
            }
        }
        return Optional.empty();
    }
    
    private SpielResult spielAuswerten() {
        SpielResult res = new SpielResult();
        res.setGame(game);
        res.setGameLevel(gameLevel);
        
        if (game == Game.RAMSCH) {
            final int punkteVorhand = vorhand.sumPoints();
            final int punkteMittelhand = mittelhand.sumPoints();
            final int punkteHinterhand = hinterhand.sumPoints();
            
            final boolean schwarzVorhand = !vorhand.isStichErhalten();
            final boolean schwarzMittelhand = !mittelhand.isStichErhalten();
            final boolean schwarzHinterhand = !hinterhand.isStichErhalten();
            
            final boolean durchmarschVorhand = schwarzMittelhand && schwarzHinterhand;
            final boolean durchmarschMittelhand = schwarzVorhand && schwarzMittelhand;
            final boolean durchmarschHinterhand = schwarzVorhand && schwarzMittelhand;
            
            final int wertungVorhand = (durchmarschVorhand ? 1 :
                (table.getSettings().isRamschAbrechnungAlle() ||
                (punkteVorhand >= punkteMittelhand && punkteVorhand >= punkteHinterhand) ? -2 : 0));
            final int wertungMittelhand = (durchmarschMittelhand ? 1 :
                (table.getSettings().isRamschAbrechnungAlle() ||
                (punkteMittelhand >= punkteVorhand && punkteMittelhand >= punkteHinterhand) ? -2 : 0));
            final int wertungHinterhand = (durchmarschHinterhand ? 1 :
                (table.getSettings().isRamschAbrechnungAlle() ||
                (punkteHinterhand >= punkteVorhand && punkteHinterhand >= punkteMittelhand) ? -2 : 0));
            
            res.putWertung(vorhand, wertungVorhand * punkteVorhand);
            res.putWertung(mittelhand, wertungMittelhand * punkteMittelhand);
            res.putWertung(hinterhand, wertungHinterhand * punkteHinterhand);
            
            return res;
        }
        
        final int zusatzPunkteGewonnen = table.getSettings().getZusatzPunkteGewonnen();
        final int zusatzPunkteVerloren = table.getSettings().getZusatzPunkteVerloren();
        
        Player alleinspieler = aktuellerReizwert.getKey();
        List<Player> gegenspieler = Stream.of(vorhand, mittelhand, hinterhand)
            .filter(p -> !p.equals(alleinspieler
            .collect(Collectors.toList());
        final int punkteAlleinspieler = .sumPoints();
        final int punkteGegenspieler = gegenspieler.stream().mapToInt(Player::sumPoints).sum();
        final int faktorVerloren = game.isHandSpiel(gameLevel) ? -1 : -2;
        final boolean schneider = punkteGegenspieler <= 30;
        final boolean schwarz = !gegenspieler.stream()
            .map(Player::isStichErhalten)
            .reduce(false, (a,b) -> a || b);
        final boolean alleinspielerSchneider = punkteAlleinspieler <= 30;
        final boolean alleinspielerSchwarz = !alleinspieler.isStichErhalten()
        
        res.setGespaltenes(punkteAlleinspieler == punkteGegenspieler);
        
        if (game.isNullSpiel()) {
            if (alleinspielerSchwarz) {
                res.putWertung(alleinspieler, game.grundwert + table.getSettings().getZusatzPunkteGewonnen());
                gegenspieler.forEach(p -> res.putWertung(p, 0));
            } else {
                res.putWertung(alleinspieler, faktorVerloren * game.grundwert);
                gegenspieler.forEach(p -> res.putWertung(p, table.getSettings().getZusatzPunkteVerloren()));
            }
        } else {
            // Farbspiele oder Grand
            if ((punkteAlleinspieler <= 60) ||
                (gameLevel == GameLevel.SCHNEIDER_ANGESAGT && !schneider) ||
                (gameLevel == GameLevel.SCHWARZ_ANGESAGT && !schwarz) ||
                (gameLevel == GameLevel.OUVERT && !schwarz)) {
                // verloren
                int fall = gameLevel.gewinnStufeVerloren;
                if (gameLevel.ordinal() <= GameLevel.HAND.ordinal() && alleinspielerSchneider) {
                    fall++;
                }
                if (gameLevel.ordinal() <= GameLevel.SCHNEIDER_ANGESAGT.ordinal() && alleinspielerSchwarz) {
                    fall++;
                }
                res.putWertung(alleinspieler, faktorVerloren * game.grundwert * (alleinspieler.getSpitzen() + fall));
                gegenspieler.forEach(p -> res.putWertung(p, table.getSettings().getZusatzPunkteVerloren()));
            } else {
                // gewonnen
                res.putWertung(alleinspieler, 
                    game.grundwert * (
                        alleinspieler.getSpitzen() + 
                        gameLevel.gewinnStufe + 
                        (schneider ? 1 : 0) + 
                        (schwarz ? 1 : 0)) + 
                        table.getSettings().getZusatzPunkteGewonnen());
                gegenspieler.forEach(p -> res.putWertung(p, 0));
            }
        }
        
        return res;
    }
    
}

