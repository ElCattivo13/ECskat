package io.github.elcattivo13.pojos;

import static io.github.elcattivo13.pojos.Farbe.*;
import static io.github.elcattivo13.pojos.Blatt.*;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class StichTest {
    
    private static final Logger log = LoggerFactory.getLogger(StichTest.class);
    
    private Player player1;
    private Player player2;
    private Player player3;
    
    @Before
    public void setup() {
        List<Player> players = createPlayers();
        player1 = players.get(0);
        player2 = players.get(1);
        player3 = players.get(2);
    }
    
    @Test
    public void testCheckCard1() throws EcSkatExceptipn{
        
        Stich stich = new Stich(Game.GRUEN);
        
        // erste Karte kann immer gespielt werden
        stich.checkCard(player1, Card.of(EICHEL, OBER));
        stich.checkCard(player1, Card.of(GRUEN, UNTER));
        stich.checkCard(player1, Card.of(HERZ, ASS));
        stich.checkCard(player1, Card.of(SCHELL, SIEBEN));
        
        stich.cardPlayed(player1, )
        
        // zweite Karte, zugeben
        
        
        // zweite Karte, abwerfen bzw. stechen
    }
    
    @Test
        public void testCheckCard1() throws EcSkatExceptipn{
            
            Stich stich = new Stich(Game.GRUEN);
            
            // erste Karte kann immer gespielt werden
            stich.checkCard(player1, Card.of(EICHEL, OBER));
            stich.checkCard(player1, Card.of(GRUEN, UNTER));
            stich.checkCard(player1, Card.of(HERZ, ASS));
            stich.checkCard(player1, Card.of(SCHELL, SIEBEN));
            
            stich.cardPlayed(player1, )
            
            // zweite Karte, zugeben
            
            
            // zweite Karte, abwerfen bzw. stechen
        }
    
    @Test
    public void testCheckCard1() throws EcSkatExceptipn{
         
         Stich stich = new Stich(Game.GRUEN);
         
         // erste Karte kann immer gespielt werden
         stich.checkCard(player1, Card.of(EICHEL, OBER));
         stich.checkCard(player1, Card.of(GRUEN, UNTER));
         stich.checkCard(player1, Card.of(HERZ, ASS));
         stich.checkCard(player1, Card.of(SCHELL, SIEBEN));@@@@@
     }
    
    private List<Player> createPlayers() {
        Player p1 = new Player("P1");
        p1.receiveCards(false, 
            Card.of(GRUEN, SIEBEN)
        );
        
        
        return Arrays.asList(p1);
    }
    
}