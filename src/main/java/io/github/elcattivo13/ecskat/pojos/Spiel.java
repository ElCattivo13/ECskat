package io.github.elcattivo13.ecskat.pojos;

import static io.github.elcattivo13.ecskat.errorhandling.EcSkatException.Reason.HANDSPIEL_NOT_ALLOWED;
import static io.github.elcattivo13.ecskat.errorhandling.EcSkatException.Reason.ILLEGAL_ACTION;
import static io.github.elcattivo13.ecskat.errorhandling.EcSkatException.Reason.ILLEGAL_REIZWERT;
import static io.github.elcattivo13.ecskat.errorhandling.EcSkatException.Reason.INVALID_SKAT_SIZE;
import static io.github.elcattivo13.ecskat.errorhandling.EcSkatException.Reason.NOT_YOUR_TURN;
import static io.github.elcattivo13.ecskat.errorhandling.EcSkatException.Reason.NULLSPIEL_UEBERREIZT;
import static io.github.elcattivo13.ecskat.errorhandling.EcSkatException.Reason.RAMSCH_UNTER_DRUECKEN_VERBOTEN;
import static io.github.elcattivo13.ecskat.errorhandling.EcSkatException.Reason.UNEXPECTED_PLAYER;
import static io.github.elcattivo13.ecskat.websocket.SkatMessage.Key.AKTUELLER_REIZWERT;
import static io.github.elcattivo13.ecskat.websocket.SkatMessage.Key.ANSAGE_ERHOEHEN;
import static io.github.elcattivo13.ecskat.websocket.SkatMessage.Key.KONTRA_GESAGT;
import static io.github.elcattivo13.ecskat.websocket.SkatMessage.Key.RE_GESAGT;
import static io.github.elcattivo13.ecskat.websocket.SkatMessage.Key.SKAT_AUFGENOMMEN;
import static io.github.elcattivo13.ecskat.websocket.SkatMessage.Key.SPIELANSAGE;
import static io.github.elcattivo13.ecskat.websocket.SkatMessage.Key.WER_IST_DRAN;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.github.elcattivo13.ecskat.errorhandling.EcSkatException;
import io.github.elcattivo13.ecskat.websocket.SkatMessage;

public class Spiel extends BaseObject {

	private static final long serialVersionUID = -1699579178249838583L;

	private static final Logger log = LoggerFactory.getLogger(Spiel.class);
    
    private static final List<Integer> reizwerte = Arrays.asList(0, 18, 20, 22, 23, 24, 27, 30, 33, 35, 36, 40, 44, 45, 46, 48, 50, 54, 55, 59, 60, 63, 66, 70, 72, 77, 80, 81, 84, 88, 90, 96, 99, 100);
    
    private final Table table;
    private final Player vorhand;
    private final Player mittelhand;
    private final Player hinterhand;
    private final Player watcher;
    private LinkedList<Card> skat = new LinkedList<>();
    private Player werIstDran;
    private Action naechsteAktion;
    //private Set<AbstractMap.SimpleEntry<Player, Action>> expectedNextActions = new HashSet<>();
    private AbstractMap.SimpleEntry<Player, Integer> aktuellerReizwert = new AbstractMap.SimpleEntry<>(null, 17);
    private Game game;
    private GameLevel gameLevel;
    private Stich aktuellerStich;
    private Stich letzterStich;
    private int sticheGespielt = 0;
    private boolean kontra = false;
    private boolean re = false;
    private SpielResult sr = new SpielResult();
    
    public Spiel(Table table, Player vorhand, Player mittelhand, Player hinterhand, Player watcher, CutPosition cutPosition) throws EcSkatException {
        super();
        this.table = table;
        this.vorhand = vorhand;
        this.mittelhand = mittelhand;
        this.hinterhand = hinterhand;
        this.watcher = watcher;
        
        kartenAusteilen(cutPosition);
        
        next(mittelhand, Action.REIZEN_SAGEN);
    }
    
    private void kartenAusteilen(CutPosition cutPos) throws EcSkatException {
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
        
        sr.setSkat(new ArrayList<>(skat));
        sr.putAusgeteilteKarten(vorhand, new ArrayList<>(vorhand.getCards()));
        sr.putAusgeteilteKarten(mittelhand, new ArrayList<>(mittelhand.getCards()));
        sr.putAusgeteilteKarten(hinterhand, new ArrayList<>(hinterhand.getCards()));
    }
    
    private void next(Player spieler, Action action) throws EcSkatException {
        werIstDran = spieler;
        naechsteAktion = action;
        
        SkatMessage msg = SkatMessage.of(WER_IST_DRAN)
            .setSubject(werIstDran)
            .setNaechsteAktion(naechsteAktion);
        sendToTable(msg);
    }
    
    private void checkAction(Player spieler, Action action) throws EcSkatException {
        if (!werIstDran.equals(spieler)) {
            throw new EcSkatException(NOT_YOUR_TURN);
        }
        if (naechsteAktion != action) {
            throw new EcSkatException(ILLEGAL_ACTION);
        }
    }
    
    private void sendToTable(SkatMessage msg) throws EcSkatException {
        getWebsocket().sendToPlayers(msg, table.getSpieler());
    }
    
    public Optional<SpielResult> reizenSagen(Player spieler, int reizwert) throws EcSkatException {
        checkAction(spieler, Action.REIZEN_SAGEN);
        if (!reizwerte.contains(reizwert)) {
            throw new EcSkatException(ILLEGAL_REIZWERT);
        }
        if (reizwert > aktuellerReizwert.getValue()) {
            spieler.setAchtzehnGesagt(true);
            next(
                (aktuellerReizwert.getKey() == null) ? vorhand : aktuellerReizwert.getKey(),
                (!spieler.equals(vorhand)) ? Action.REIZEN_HOEREN : Action.SPIEL_ANSAGEN);
            aktuellerReizwert = new AbstractMap.SimpleEntry<>(spieler, reizwert);
            sendToTable(SkatMessage.of(AKTUELLER_REIZWERT).setAktuellerReizwert(reizwert).setSubject(spieler));
        } else {
            // Spieler hat weggesagt
            if (spieler.equals(vorhand)) {
                if (table.getSettings().isWithRamsch()) {
                    game = Game.RAMSCH;
                    gameLevel = GameLevel.NORMAL;
                    sendToTable(SkatMessage.of(SPIELANSAGE).setGame(Game.RAMSCH).setGameLevel(GameLevel.NORMAL));
                    if (table.getSettings().isRamschSkatWeiterreichen()) {
                        vorhand.receiveCards(true, skat.poll(),skat.poll());
                        next(vorhand, Action.RAMSCH_SKAT_WEITERREICHEN);
                    } else {
                        next(vorhand, Action.KARTE_SPIELEN);
                    }
                } else {
                    SpielResult res = sr;
                    res.setEingemischt(true);
                    return Optional.of(res);
                }
            } else if (spieler.equals(mittelhand)) {
                next(hinterhand, Action.REIZEN_SAGEN);
            } else if (spieler.equals(hinterhand)) {
                if (aktuellerReizwert.getKey() != null) {
                    next(aktuellerReizwert.getKey(), Action.SPIEL_ANSAGEN);
                } else {
                    next(vorhand, Action.REIZEN_SAGEN);
                }
            } else {
                throw new EcSkatException(UNEXPECTED_PLAYER);
            }
        }
        return Optional.empty();
    }
    
    public void ramschSkatWeiterreichen(Player spieler, List<Card> karten) throws EcSkatException {
        checkAction(spieler, Action.RAMSCH_SKAT_WEITERREICHEN);
        if (karten.size() != 2) {
            throw new EcSkatException(INVALID_SKAT_SIZE);
        }
        if (table.getSettings().isRamschUnterDrueckenVerboten() && karten.stream().filter(c -> c.blatt == Blatt.UNTER).findAny().isPresent()) {
            throw new EcSkatException(RAMSCH_UNTER_DRUECKEN_VERBOTEN);
        }
        
        if (spieler.equals(vorhand)) {
            spieler.skatDruecken(karten, true);
            mittelhand.receiveCards(true, karten.toArray(new Card[1]));
            next(mittelhand, Action.RAMSCH_SKAT_WEITERREICHEN);
        } else if (spieler.equals(mittelhand)) {
            spieler.skatDruecken(karten, true);
            hinterhand.receiveCards(true, karten.toArray(new Card[1]));
            next(hinterhand, Action.RAMSCH_SKAT_WEITERREICHEN);
        } else if (spieler.equals(hinterhand)) {
        	for (Card card : karten) {
				spieler.playCard(card);
			}
            skat.addAll(karten);
            next(vorhand, Action.KARTE_SPIELEN);
        } else {
            throw new EcSkatException(UNEXPECTED_PLAYER);
        }
    }
    
    public void reizenHoeren(Player spieler, boolean ja) throws EcSkatException {
        checkAction(spieler, Action.REIZEN_HOEREN);
        if (ja) {
            spieler.setAchtzehnGesagt(true);
            next(aktuellerReizwert.getKey(), Action.REIZEN_SAGEN);
            aktuellerReizwert = new AbstractMap.SimpleEntry<>(spieler, aktuellerReizwert.getValue());
            sendToTable(SkatMessage.of(AKTUELLER_REIZWERT).setAktuellerReizwert(aktuellerReizwert.getValue()).setSubject(spieler));
        } else {
            // Spieler hat weggesagt
            next(hinterhand, (aktuellerReizwert.getKey() == mittelhand) ? Action.REIZEN_SAGEN : Action.SPIEL_ANSAGEN);
        }
    }
    
    public void skatAufnehmen(Player spieler) throws EcSkatException {
        checkAction(spieler, Action.SPIEL_ANSAGEN);
        spieler.receiveCards(true, skat.poll(), skat.poll());
        sendToTable(SkatMessage.of(SKAT_AUFGENOMMEN).setSubject(spieler));
    }
    
    public void spielAnsagen(Player spieler, Game game, GameLevel gameLevel, List<Card> gedrueckteKarten) throws EcSkatException {
        checkAction(spieler, Action.SPIEL_ANSAGEN);
        if (skat.isEmpty() && gameLevel != GameLevel.NORMAL) {
            throw new EcSkatException(HANDSPIEL_NOT_ALLOWED);
        }
        if (game.isNullSpiel() && game.grundwert < aktuellerReizwert.getValue()) {
            throw new EcSkatException(NULLSPIEL_UEBERREIZT);
        }
        this.game = game;
        this.gameLevel = gameLevel;
        this.aktuellerReizwert = new AbstractMap.SimpleEntry<>(spieler, aktuellerReizwert.getValue());
        if (!game.isNullSpiel()) {
            spieler.calcSpitzen(game);
        }
        spieler.skatDruecken(gedrueckteKarten);
        this.aktuellerStich = new Stich(game);
        next(vorhand, Action.KARTE_SPIELEN);
        sendToTable(SkatMessage.of(SPIELANSAGE).setGame(game).setGameLevel(gameLevel));
    }
    
    public Optional<SpielResult> karteSpielen(Player spieler, Card karte) throws EcSkatException {
        checkAction(spieler, Action.KARTE_SPIELEN);
        Optional<Stich> result = aktuellerStich.cardPlayed(spieler, karte);
        if (!result.isPresent()) {
            next(vorhand.equals(spieler) ? mittelhand : (mittelhand.equals(spieler) ? hinterhand : vorhand), Action.KARTE_SPIELEN);
        } else {
            letzterStich = result.get();
            sticheGespielt++;
            if ((game.isNullSpiel() && aktuellerReizwert.getKey().isStichErhalten()) ||
                    (sticheGespielt == 10)) {
                return Optional.of(spielAuswerten());
            } else {
                next(letzterStich.getWinner(), Action.KARTE_SPIELEN);
            }
        }
        return Optional.empty();
    }
    
    private SpielResult spielAuswerten() throws EcSkatException {
        SpielResult res = sr;
        res.setGame(game);
        res.setGameLevel(gameLevel);
        
        if (game == Game.RAMSCH) {
            return spielAuswertenRamsch(res);
        }
        
        final int zusatzPunkteGewonnen = table.getSettings().getZusatzPunkteGewonnen();
        final int zusatzPunkteVerloren = table.getSettings().getZusatzPunkteVerloren();
        
        Player alleinspieler = aktuellerReizwert.getKey();
        List<Player> gegenspieler = Stream.of(vorhand, mittelhand, hinterhand)
            .filter(p -> !p.equals(alleinspieler))
            .collect(Collectors.toList());
        final int punkteAlleinspieler = alleinspieler.sumPoints() + skat.stream().mapToInt(Card::punkte).sum();
        log.info("Punkte Alleinspieler: {}", punkteAlleinspieler);
        final int punkteGegenspieler = gegenspieler.stream().mapToInt(Player::sumPoints).sum();
        log.info("Punkte Gegenspieler: {}", punkteGegenspieler);
        final int faktorKontraRe = (kontra ? 2 : 1)*(re ? 2 : 1);
        final int faktorVerloren = game.isHandSpiel(gameLevel) ? -1 : -2;
        final boolean schneider = punkteGegenspieler <= 30;
        final boolean schwarz = !gegenspieler.stream()
            .map(Player::isStichErhalten)
            .reduce(false, (a,b) -> a || b);
        final boolean alleinspielerSchneider = punkteAlleinspieler <= 30;
        final boolean alleinspielerSchwarz = !alleinspieler.isStichErhalten();
        
        res.setGespaltenes(punkteAlleinspieler == punkteGegenspieler);
        
        if (game.isNullSpiel()) {
            // überreizen ist bereits bei Spielansage abgefangen
            if (alleinspielerSchwarz) {
                res.putWertung(alleinspieler, faktorKontraRe * game.grundwert + zusatzPunkteGewonnen);
                gegenspieler.forEach(p -> res.putWertung(p, 0));
            } else {
                res.putWertung(alleinspieler, faktorKontraRe * faktorVerloren * game.grundwert);
                gegenspieler.forEach(p -> res.putWertung(p, zusatzPunkteVerloren));
            }
            return res;
        } else {
            // Farbspiele oder Grand
            int fallGewonnen = alleinspieler.getSpitzen() + gameLevel.gewinnStufe + (schneider ? 1 : 0) + (schwarz ? 1 : 0);
            double reizwert = aktuellerReizwert.getValue();
            if ((fallGewonnen * game.grundwert < reizwert ||
                (punkteAlleinspieler <= 60) ||
                (gameLevel == GameLevel.SCHNEIDER_ANGESAGT && !schneider) ||
                (gameLevel == GameLevel.SCHWARZ_ANGESAGT && !schwarz) ||
                (gameLevel == GameLevel.OUVERT && !schwarz))) {
                // verloren
                int fallVerloren = gameLevel.gewinnStufeVerloren + alleinspieler.getSpitzen();
                if (gameLevel.ordinal() <= GameLevel.HAND.ordinal() && alleinspielerSchneider) {
                    fallVerloren++;
                }
                if (gameLevel.ordinal() <= GameLevel.SCHNEIDER_ANGESAGT.ordinal() && alleinspielerSchwarz) {
                    fallVerloren++;
                }
                fallVerloren = (int) Math.max(fallVerloren, Math.ceil(reizwert / game.grundwert));
                //
                res.putWertung(alleinspieler, faktorKontraRe * faktorVerloren * game.grundwert * fallVerloren);
                gegenspieler.forEach(p -> res.putWertung(p, zusatzPunkteVerloren));
            } else {
                // gewonnen
                res.putWertung(alleinspieler, 
                    faktorKontraRe * game.grundwert * fallGewonnen + zusatzPunkteGewonnen);
                gegenspieler.forEach(p -> res.putWertung(p, 0));
            }
        }
        
        return res;
    }
    
    private SpielResult spielAuswertenRamsch(SpielResult res) throws EcSkatException {
        
        letzterStich.getWinner().receiveCards(false, skat.get(0), skat.get(1));
        letzterStich.getWinner().skatDruecken(this.skat);
        
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
    
    public void kontraSagen(Player spieler) throws EcSkatException {
        if (!vorhand.equals(spieler) && !mittelhand.equals(spieler) && !hinterhand.equals(spieler)) {
            throw new EcSkatException(UNEXPECTED_PLAYER);
        } else if (naechsteAktion != Action.KARTE_SPIELEN ||
                aktuellerReizwert.getKey() == null ||
                aktuellerReizwert.getKey().equals(spieler) ||
                spieler.karteGespielt() ||
                !spieler.isAchtzehnGesagt() ||
                kontra) {
            throw new EcSkatException(ILLEGAL_ACTION);
        }
        kontra = true;
        sendToTable(SkatMessage.of(KONTRA_GESAGT).setSubject(spieler));
    }
    
    public void reSagen(Player spieler) throws EcSkatException {
        if (!vorhand.equals(spieler) && !mittelhand.equals(spieler) && !hinterhand.equals(spieler)) {
           throw new EcSkatException(UNEXPECTED_PLAYER);
        } else if (naechsteAktion != Action.KARTE_SPIELEN ||
                aktuellerReizwert.getKey() == null ||
                !aktuellerReizwert.getKey().equals(spieler) ||
                spieler.karteGespielt() ||
                !kontra ||
                re) {
            throw new EcSkatException(ILLEGAL_ACTION);
        }
        re = true;
        sendToTable(SkatMessage.of(RE_GESAGT).setSubject(spieler));
    }
    
    public void ansageErhoehen(Player spieler, GameLevel gameLevel) throws EcSkatException {
        if (!vorhand.equals(spieler) && !mittelhand.equals(spieler) && !hinterhand.equals(spieler)) {
           throw new EcSkatException(UNEXPECTED_PLAYER);
        } else if (naechsteAktion != Action.KARTE_SPIELEN ||
                aktuellerReizwert.getKey() == null ||
                !aktuellerReizwert.getKey().equals(spieler) ||
                spieler.karteGespielt() ||
                skat.isEmpty() ||
                this.gameLevel.gewinnStufe >= gameLevel.gewinnStufe) {
            throw new EcSkatException(ILLEGAL_ACTION);
        }
        this.gameLevel = gameLevel;
        sendToTable(SkatMessage.of(ANSAGE_ERHOEHEN).setGameLevel(gameLevel));
    }
    
    
    
    
    
    
    
	public LinkedList<Card> getSkat() {
		return skat;
	}

	public void setSkat(LinkedList<Card> skat) {
		this.skat = skat;
	}

	public Player getWerIstDran() {
		return werIstDran;
	}

	public void setWerIstDran(Player werIstDran) {
		this.werIstDran = werIstDran;
	}

	public Action getNaechsteAktion() {
		return naechsteAktion;
	}

	public void setNaechsteAktion(Action naechsteAktion) {
		this.naechsteAktion = naechsteAktion;
	}

	public AbstractMap.SimpleEntry<Player, Integer> getAktuellerReizwert() {
		return aktuellerReizwert;
	}

	public void setAktuellerReizwert(AbstractMap.SimpleEntry<Player, Integer> aktuellerReizwert) {
		this.aktuellerReizwert = aktuellerReizwert;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public GameLevel getGameLevel() {
		return gameLevel;
	}

	public void setGameLevel(GameLevel gameLevel) {
		this.gameLevel = gameLevel;
	}

	public Stich getAktuellerStich() {
		return aktuellerStich;
	}

	public void setAktuellerStich(Stich aktuellerStich) {
		this.aktuellerStich = aktuellerStich;
	}

	public Stich getLetzterStich() {
		return letzterStich;
	}

	public void setLetzterStich(Stich letzterStich) {
		this.letzterStich = letzterStich;
	}

	public int getSticheGespielt() {
		return sticheGespielt;
	}

	public void setSticheGespielt(int sticheGespielt) {
		this.sticheGespielt = sticheGespielt;
	}

	@JsonIgnore
	public Table getTable() {
		return table;
	}

	public Player getVorhand() {
		return vorhand;
	}

	public Player getMittelhand() {
		return mittelhand;
	}

	public Player getHinterhand() {
		return hinterhand;
	}
    
	public boolean isKontra(){
		return this.kontra;
	}

	public Player getWatcher() {
		return watcher;
	}
	
}

