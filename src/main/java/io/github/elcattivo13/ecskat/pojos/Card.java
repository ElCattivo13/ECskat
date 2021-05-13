package io.github.elcattivo13.ecskat.pojos;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class Card {
    
    public final Blatt blatt;
    public final Farbe farbe;
    
    public Card(Farbe farbe, Blatt blatt)  {
        this.farbe = farbe;
        this.blatt = blatt;
    }
    
    public static Card of(Farbe farbe, Blatt blatt) {
        return new Card(farbe, blatt);
    }
    
    public int punkte() {
        return blatt.wert;
    }
    
    public boolean isTrumpf(Game game) {
        switch (game) {
            case GRAND:
            case RAMSCH:
                return blatt == Blatt.UNTER;
            case EICHEL:
                return blatt == Blatt.UNTER || farbe == Farbe.EICHEL;
            case GRUEN:
                return blatt == Blatt.UNTER || farbe == Farbe.GRUEN;
            case HERZ:
                return blatt == Blatt.UNTER || farbe == Farbe.HERZ;
            case SCHELL:
                return blatt == Blatt.UNTER || farbe == Farbe.SCHELL;
            default:
                return false;
        }
    }
    
    public boolean isGleicheFarbe(Card that, Game game) {
        return (this.isTrumpf(game) && that.isTrumpf(game)) ||
            (!this.isTrumpf(game) && !that.isTrumpf(game) &&
            this.farbe.equals(that.farbe));
    }
    
    public static Comparator<Card> getComparator(Game game) {
        
        return (a,b) -> {
            if (a.isTrumpf(game)) {
                if (b.isTrumpf(game)) {
                    return a.blatt.rang(game) - b.blatt.rang(game) == 0 ? a.farbe.wert - b.farbe.wert : a.blatt.rang(game) - b.blatt.rang(game);
                } else {
                    return 1;
                }
            } else {
                if (b.isTrumpf(game)) {
                    return -1;
                } else {
                    if (a.farbe == b.farbe) {
                        return a.blatt.rang(game) - b.blatt.rang(game);
                    } else {
                        return 1;
                    }
                }
            }
        };
    }
    
    public static List<Card> getDeck() {
        List<Card> deck = new ArrayList<>(32);
        for (Farbe farbe : Farbe.values()) {
            for (Blatt blatt : Blatt.values()) {
                deck.add(new Card(farbe, blatt));
            }
        }
        return deck;
    }
    
    public static void shuffleDeck(List<Card> deck) {
        Collections.shuffle(deck);
    }
    
    public static int cutDeck(List<Card> deck, CutPosition pos) {
        Consumer<Integer> eineKarteAbheben = (i) -> deck.add(deck.remove(0));
        int position = pos.toInt();
        Collections.nCopies(position, 42).forEach(eineKarteAbheben);
        return position;
    }

	@Override
	public int hashCode() {
		return Objects.hash(blatt, farbe);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Card other = (Card) obj;
		return blatt == other.blatt && farbe == other.farbe;
	}
    
    
}
