package io.github.elcattivo13.ecskat.pojos;

import static io.github.elcattivo13.ecskat.errorhandling.EcSkatException.Reason.UNKNOWN_FARBE;

import io.github.elcattivo13.ecskat.errorhandling.EcSkatException;

enum Farbe { 
    EICHEL(12),
    GRUEN(11),
    HERZ(10),
    SCHELL(9);
    
    public final int wert;
    
    private Farbe(int wert) {
        this.wert = wert;
    }
    
    public static Farbe of(String farbe) throws EcSkatException {
        try {
            return Farbe.valueOf(farbe);
        } catch(IllegalArgumentException e) {
            throw new EcSkatException(UNKNOWN_FARBE);
        }
    }
}