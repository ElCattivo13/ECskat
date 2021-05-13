package io.github.elcattivo13.ecskat.pojos;

import java.util.Random;

/**
 * CutPosition.toInt() = n bedeutet, dass beim Abheben die obersten
 * n Karten abgehoben werden. Also muss n zwischen 4 und 28 (inklusive)
 * liegen.
 */
public enum CutPosition {
    TOP(4), // 4-14
    MIDDLE(11), // 11-21
    BOTTOM(18); // 18-28
    
    private int base;
    
    private CutPosition(int base) {
        this.base = base;
    }
    
    public int toInt() {
        Random random = new Random();
        int i = random.nextInt(11); // zufallszahlen von 0 bis 10
        return base + i;
    }
}