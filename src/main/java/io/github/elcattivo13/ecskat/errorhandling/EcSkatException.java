package io.github.elcattivo13.ecskat.errorhandling;



public abstract class EcSkatException extends Exception {

	private static final long serialVersionUID = 1864579569964531608L;
	private final Reason reason;
    
    public EcSkatException(Reason reason) {
        this.reason = reason;
    }
    
    public Reason getReason(){
        return this.reason;
    }
    
    protected enum Reason {
        UNKNOWN_PLAYER,
        UNKNOWN_TABLE,
        UNKNOWN_FARBE,
        UNKNOWN_BLATT,
        PLAYER_NOT_AT_TABLE,
        PLAYER_ALREADY_AT_TABLE,
        TABLE_IS_FULL,
        SPIEL_ALREADY_STARTED,
        KARTE_NOT_ALLOWED,
        xxx
    }
}


//
//
//public class KarteNotAllowedException