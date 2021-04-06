package io.github.elcattivo13.ecskat.pojos;

import io.github.elcattivo13.ecskat.errorhandling.UnknownFarbeException;

enum Farbe { 
    EICHEL(12),
    GRUEN(11),
    HERZ(10),
    SCHELL(9);
    
    public final int wert;
    
    private Farbe(int wert) {
        this.wert = wert;
    }
    
    public static Farbe of(String farbe) throws UnknownFarbeException {
        try {
            return Farbe.valueOf(farbe);
        } catch(IllegalArgumentException e) {
            throw new UnknownFarbeException();
        }
    }
}