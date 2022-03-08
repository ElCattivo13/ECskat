package io.github.elcattivo13.pojos;

import static io.github.elcattivo13.ecskat.pojos.Blatt.*;
import static io.github.elcattivo13.ecskat.pojos.Farbe.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.elcattivo13.ecskat.errorhandling.EcSkatException;
import io.github.elcattivo13.ecskat.pojos.Card;
import io.github.elcattivo13.ecskat.pojos.Game;
import io.github.elcattivo13.ecskat.pojos.Player;
import io.github.elcattivo13.ecskat.pojos.Stich;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class StichTest {
    
    private static final Logger log = LoggerFactory.getLogger(StichTest.class);
    
    private Player player1;
    private Player player2;
    private Player player3;
    
    @BeforeEach
    public void setup() throws EcSkatException {
        List<Player> players = createPlayers();
        player1 = players.get(0);
        player2 = players.get(1);
        player3 = players.get(2);
    }
    
    @Test
    public void testCheckCard1() throws EcSkatException{
        
        Stich stich = new Stich(Game.GRUEN);
        
        // erste Karte kann immer gespielt werden
        stich.checkCard(player1, Card.of(EICHEL, OBER));
        stich.checkCard(player1, Card.of(GRUEN, UNTER));
        stich.checkCard(player1, Card.of(HERZ, ASS));
        stich.checkCard(player1, Card.of(SCHELL, SIEBEN));
        
        //stich.cardPlayed(player1, )
        
        // zweite Karte, zugeben
        
        
        // zweite Karte, abwerfen bzw. stechen
    }
    
    @Test
        public void testCheckCard2() throws EcSkatException{
            
            Stich stich = new Stich(Game.GRUEN);
            
            // erste Karte kann immer gespielt werden
            stich.checkCard(player1, Card.of(EICHEL, OBER));
            stich.checkCard(player1, Card.of(GRUEN, UNTER));
            stich.checkCard(player1, Card.of(HERZ, ASS));
            stich.checkCard(player1, Card.of(SCHELL, SIEBEN));
            
            //stich.cardPlayed(player1, )
            
            // zweite Karte, zugeben
            
            
            // zweite Karte, abwerfen bzw. stechen
        }
    
    @Test
    public void testCheckCard3() throws EcSkatException{
         
         Stich stich = new Stich(Game.GRUEN);
         
         // erste Karte kann immer gespielt werden
         stich.checkCard(player1, Card.of(EICHEL, OBER));
         stich.checkCard(player1, Card.of(GRUEN, UNTER));
         stich.checkCard(player1, Card.of(HERZ, ASS));
         stich.checkCard(player1, Card.of(SCHELL, SIEBEN));
     }
    
    private List<Player> createPlayers() throws EcSkatException {
        Player p1 = new Player("P1");
        p1.receiveCards(false, 
            Card.of(GRUEN, SIEBEN)
        );
        
        Player p2 = new Player("P2");

        Player p3 = new Player("P3");

        return Arrays.asList(p1, p2, p3);
    }
    
}