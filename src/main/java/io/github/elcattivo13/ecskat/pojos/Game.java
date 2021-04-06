package io.github.elcattivo13.ecskat.pojos;

enum Game {
    GRAND(24),
    EICHEL(12),
    GRUEN(11),
    HERZ(10),
    SCHELL(9),
    NULL(23),
    NULL_HAND(35),
    NULL_OUVERT(46),
    NULL_OUVERT_HAND(59),
    RAMSCH(0);
    
    public final int grundwert;
    
    private Game(int grundwert) {
        this.grundwert = grundwert;
    }
    
    public boolean isNullSpiel() {
        return this == NULL || this == NULL_HAND || this == NULL_OUVERT || this == NULL_OUVERT_HAND;
    }
    
    public boolean isHandSpiel(GameLevel gameLevel) {
        return this == NULL_HAND || this == NULL_OUVERT_HAND || gameLevel != GameLevel.NORMAL;
    }
}