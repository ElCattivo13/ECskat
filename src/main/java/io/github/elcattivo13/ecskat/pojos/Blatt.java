package io.github.elcattivo13.ecskat.pojos;

import static io.github.elcattivo13.ecskat.errorhandling.EcSkatException.Reason.UNKNOWN_BLATT;

import io.github.elcattivo13.ecskat.errorhandling.EcSkatException;

enum Blatt {
    SIEBEN(1,1,0),
    ACHT(2,2,0),
    NEUN(3,3,0),
    ZEHN(6,4,10),
    UNTER(8,5,2),
    OBER(4,6,3),
    KOENIG(5,7,4),
    ASS(7,8,11);
    
    private final int rang;
    private final int nullRang;
    public final int wert;
    
    private Blatt(int rang, int nullRang, int wert) {
        this.rang = rang;
        this.nullRang = nullRang;
        this.wert = wert;
    }
    public int rang(Game game) {
        return game.isNullSpiel() ? nullRang : rang;
    }
    
    public static Blatt of(String blatt) throws EcSkatException {
        try {
            return Blatt.valueOf(blatt);
        } catch(IllegalArgumentException e) {
            throw new EcSkatException(UNKNOWN_BLATT);
        }
    }
}
