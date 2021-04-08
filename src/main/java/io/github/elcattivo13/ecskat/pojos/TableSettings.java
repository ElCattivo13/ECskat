package io.github.elcattivo13.ecskat.pojos;


public class TableSettings{
    
    private boolean withRamsch = true;
    private boolean ramschUnterDrueckenVerboten = true;
    private boolean zuViertOk = true;
    private int zusatzPunkteGewonnen = 50;
    private int zusatzPunkteVerloren = 40;
    private boolean ramschSkatWeiterreichen;
    private boolean ramschAbrechnungAlle;

    
    
    public void setWithRamsch(boolean withRamsch) {
        this.withRamsch = withRamsch;
    }
    
    public boolean isWithRamsch() {
        return withRamsch;
    }
    
    public void setRamschUnterDrueckenVerboten(boolean ramschUnterDrueckenVerboten) {
        this.ramschUnterDrueckenVerboten = ramschUnterDrueckenVerboten;
    }
    
    public boolean isRamschUnterDrueckenVerboten() {
        return ramschUnterDrueckenVerboten;
    }
    
    public void setZuViertOk(boolean zuViertOk) {
        this.zuViertOk = zuViertOk;
    }
    
    public boolean isZuViertOk() {
        return zuViertOk;
    }
    
    public void setZusatzPunkteGewonnen(int zusatzPunkteGewonnen) {
        this.zusatzPunkteGewonnen = zusatzPunkteGewonnen;
    }
    
    public int getZusatzPunkteGewonnen() {
        return zusatzPunkteGewonnen;
    }
    
    public void setZusatzPunkteVerloren(int zusatzPunkteVerloren) {
        this.zusatzPunkteVerloren = zusatzPunkteVerloren;
    }
    
    public int getZusatzPunkteVerloren(){
        return zusatzPunkteVerloren;
    }
    
    public void setRamschSkatWeiterreichen(boolean ramschSkatWeiterreichen){
        this.ramschSkatWeiterreichen = ramschSkatWeiterreichen;
    }
    
    public boolean isRamschSkatWeiterreichen(){
        return this.ramschSkatWeiterreichen;
    }
    
    public void setRamschAbrechnungAlle(boolean ramschAbrechnungAlle){
        this.ramschAbrechnungAlle = ramschAbrechnungAlle;
    }
    
    public boolean isRamschAbrechnungAlle(){
        return this.ramschAbrechnungAlle;
    }

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TableSettings [withRamsch=").append(withRamsch).append(", ramschUnterDrueckenVerboten=")
				.append(ramschUnterDrueckenVerboten).append(", zuViertOk=").append(zuViertOk)
				.append(", zusatzPunkteGewonnen=").append(zusatzPunkteGewonnen).append(", zusatzPunkteVerloren=")
				.append(zusatzPunkteVerloren).append(", ramschSkatWeiterreichen=").append(ramschSkatWeiterreichen)
				.append(", ramschAbrechnungAlle=").append(ramschAbrechnungAlle).append("]");
		return builder.toString();
	}
    
    
}
