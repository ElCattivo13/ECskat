package io.github.elcattivo13.ecskat.pojos;



public enum GameLevel {
    NORMAL(1,1),
    HAND (2,2),
    SCHNEIDER_ANGESAGT(3,4),
    SCHWARZ_ANGESAGT(4,6),
    OUVERT(5,7);
    
    public final int gewinnStufe;
    public final int gewinnStufeVerloren;
    
    private GameLevel(int gewinnStufe, int gewinnStufeVerloren) {
        this.gewinnStufe = gewinnStufe;
        this.gewinnStufeVerloren = gewinnStufeVerloren;
    }
}
