package io.github.elcattivo13.pojos;

import static io.github.elcattivo13.pojos.Farbe.*;
import static io.github.elcattivo13.pojos.Blatt.*;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class CardTest {
    
    private static final Logger log = LoggerFactory.getLogger(CardTest.class);
    
    @Test
    public void testEquals() {
        Card c1 = Card.of(EICHEL, UNTER);
        Card c2 = Card.of(EICHEL, OBER);
        Card c3 = Card.of(GRUEN, UNTER);
        Card c4 = Card.of(EICHEL, UNTER);
        
        assertTrue(c1.equals(c1));
        assertFalse(c1.equals(c2));
        assertFalse(c1.equals(c3));
        assertTrue(c1.equals(c4));
        assertFalse(c1.equals(null));
    }
    
    @Test
    public void testGetDeck() {
        
        List<Card> deck = Card.getDeck();
        Set<Card> deckSet = new HashSet<>(deck);
        
        assert(32, deck.size());
        assert(32, deckSet.size());
        
        deckSet.addAll(deck);
        assert(32, deckSet.size());
    }
    
    @Test
    public void testPunkte() {
        
        assert(0, Card.of(EICHEL, SIEBEN).punkte());
        assert(0, Card.of(GRUEN, NEUN).punkte());
        assert(2, Card.of(EICHEL, UNTER).punkte());
        assert(3, Card.of(SCHELL, OBER).punkte());
        assert(4, Card.of(HERZ, KOENIG).punkte());
        assert(10, Card.of(SCHELL, ZEHN).punkte());
        assert(11, Card.of(GRUEN, ASS).punkte());
        
        assert(120, Card.getDeck().stream().mapToInt(Card::punkte).sum());
    }
    
    @Test
    public void testIsTrumpf() {
        
        List<Card> deck = Card.getDeck();
        
        for(Game g : Arrays.asList(Game.RAMSCH, Game.GRAND, Game.EICHEL, Game.GRUEN, Game.HERZ, Game.SCHELL)) {
            assertTrue(Card.of(EICHEL, UNTER).isTrumpf(g));
            assertTrue(Card.of(GRUEN, UNTER).isTrumpf(g));
            assertTrue(Card.of(HERZ, UNTER).isTrumpf(g));
            assertTrue(Card.of(SCHELL, UNTER).isTrumpf(g));
        }
        
        assertTrue(Card.of(EICHEL, KOENIG).isTrumpf(Game.EICHEL));
        assertTrue(Card.of(GRUEN, SIEBEN).isTrumpf(Game.GRUEN));
        assertTrue(Card.of(HERZ, ASS).isTrumpf(Game.HERZ));
        assertTrue(Card.of(SCHELL, NEUN).isTrumpf(Game.SCHELL));
        
        assert(0, deck.stream().filter(c -> c.isTrumpf(Game.NULL)).size());
        assert(0, deck.stream().filter(c -> c.isTrumpf(Game.NULL_HAND)).size());
        assert(0, deck.stream().filter(c -> c.isTrumpf(Game.NULL_OUVERT)).size());
        assert(0, deck.stream().filter(c -> c.isTrumpf(Game.NULL_OUVERT_HAND)).size());
        assert(4, deck.stream().filter(c -> c.isTrumpf(Game.GRAND)).size());
        assert(4, deck.stream().filter(c -> c.isTrumpf(Game.RAMSCH)).size());
        assert(11, deck.stream().filter(c -> c.isTrumpf(Game.HERZ)).size());
        
    }
    
    @Test
    public void testIsGleicheFarbe() {
        
        for(Game g : Game.values()) {
            assertTrue(Card.of(EICHEL, ACHT).isGleicheFarbe(Card.of(EICHEL, KOENIG), g));
        }
        
        assertTrue(Card.of(EICHEL, ACHT).isGleicheFarbe(Card.of(EICHEL, UNTER), Game.EICHEL));
        assertTrue(Card.of(EICHEL, ACHT).isGleicheFarbe(Card.of(EICHEL, UNTER), Game.NULL);
        assertFalse(Card.of(EICHEL, ACHT).isGleicheFarbe(Card.of(EICHEL, UNTER), Game.HERZ));
        
        assert(11, Card.getDeck().stream().filter(c -> c.isGleicheFarbe(Card.of(GRUEN, SIEBEN), Game.GRUEN)).size());
        assert(7, Card.getDeck().stream().filter(c -> c.isGleicheFarbe(Card.of(GRUEN, SIEBEN), Game.HERZ)).size());
        
    }
    
    @Test
    public void testGetComparator() {
        
        Comparator<Card> cGrand = Card.getComparator(Game.GRAND);
        Comparator<Card> cHerz = Card.getComparator(Game.HERZ);
        Comparator<Card> cNull = Card.getComparator(Game.NULL);
        
        assertTrue(cGrand.compare(Card.of(EICHEL, UNTER), Card.of(HERZ, ASS)) > 0);
        assertTrue(cGrand.compare(Card.of(HERZ, ASS), Card.of(EICHEL, UNTER)) < 0);
        
        assertTrue(cGrand.compare(Card.of(EICHEL, ACHT), Card.of(HERZ, ASS)) > 0);
        assertTrue(cGrand.compare(Card.of(HERZ, ASS), Card.of(EICHEL, ACHT)) > 0);
        
        assertTrue(cGrand.compare(Card.of(EICHEL, OBER), Card.of(EICHEL, ZEHN)) < 0);
        assertTrue(cGrand.compare(Card.of(EICHEL, ZEHN), Card.of(EICHEL, OBER)) > 0);
        
        assertTrue(cNull.compare(Card.of(EICHEL, OBER), Card.of(EICHEL, ZEHN)) > 0);
        assertTrue(cNull.compare(Card.of(EICHEL, ZEHN), Card.of(EICHEL, OBER)) < 0);
        assertTrue(cNull.compare(Card.of(EICHEL, OBER), Card.of(EICHEL, UNTER)) > 0);
        
        assertTrue(cHerz.compare(Catd.of(SCHELL, ASS), Card.of(GRUEN, ZEHN)) > 0);
        assertTrue(cHerz.compare(Catd.of(GRUEN, ZEHN), Card.of(SCHELL, ASS)) > 0);
        assertTrue(cHerz.compare(Catd.of(SCHELL, ASS), Card.of(HERZ, ACHT)) < 0);
        assertTrue(cHerz.compare(Catd.of(SCHELL, UNTER), Card.of(HERZ, ACHT)) > 0);
        assertTrue(cHerz.compare(Catd.of(HERZ, ACHT), Card.of(SCHELL, UNTER)) < 0);
        
    }
    
    @Test
    public void testShuffleDeck() {
        List<Card> deck = Card.getDeck();
        Card c0 = Card.of(deck.get(0).farbe, deck.get(0).blatt);
        Card c1 = Card.of(deck.get(1).farbe, deck.get(1).blatt);
        Card c2 = Card.of(deck.get(2).farbe, deck.get(2).blatt);
        Card c3 = Card.of(deck.get(3).farbe, deck.get(3).blatt);
        
        deck.shuffleDeck();
        
        assert(32, deck.size());
        assertFalse(
            deck.get(0).equals(c0) &&
            deck.get(1).equals(c1) &&
            deck.get(2).equals(c2) &&
            deck.get(3).equals(c3));
    }
    
    @Test
    public void testCutDeck() {
        List<Card> deck = Card.getDeck();
        deck.shuffleDeck();
        List<Integer> deckInts = deck.stream().map(CardTest::toInt).collect(Collectors.toList());
        
        Card.cutDeck(deck, CutPosition.TOP);
        
        List<Integer> deckInts2 = deck.stream().map(CardTest::toInt).collect(Collectors.toList());
        
        log.info("before cut: {}", deckInts);
        log.info(" after cut: {}", deckInts2);
        
        // TODO meaningful assertions
        
    }
    
    private static int toInt(Card card) {
        return 8 * card.farbe.ordinal() + blatt.ordinal();
    }
}