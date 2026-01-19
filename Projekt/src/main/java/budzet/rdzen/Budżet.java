package budzet.rdzen;

import java.util.List;

public class Budżet {
    private Kategoria kategoria;
    private double limit;
    private String okres;
    private List<Transakcja> transakcje;
    
    public Budżet(Kategoria kategoria, double limit, String okres) {
        this.kategoria = kategoria;
        this.limit = limit;
        this.okres = okres;
    }
    
    public boolean czyPrzekroczony() {
        // Używamy własnej metody, by nie powielać kodu sumowania
        return getAktualneWydatki() > limit;
    }
    
    public double getAktualneWydatki() {
        double suma = 0;
        if (transakcje != null) {
            for (Transakcja t : transakcje) {
                if (t.getTyp() == TypTransakcji.WYDATEK) {
                    suma += t.getKwota();
                }
            }
        }
        return suma;
    }
    
    public Kategoria getKategoria() { return kategoria; }
    public double getLimit() { return limit; }
    public String getOkres() { return okres; }
    public void setTransakcje(List<Transakcja> transakcje) { this.transakcje = transakcje; }
}