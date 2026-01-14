package budzet.rdzen;

import java.util.List;

public class Budżet {
    private Kategoria kategoria;
    private double limit;
    private String okres; // np. "2024-05"
    private List<Transakcja> transakcje; // transakcje w tej kategorii i okresie
    
    public Budżet(Kategoria kategoria, double limit, String okres) {
        this.kategoria = kategoria;
        this.limit = limit;
        this.okres = okres;
    }
    
    public boolean czyPrzekroczony() {
        double sumaWydatków = 0;
        if (transakcje != null) {
            for (Transakcja t : transakcje) {
                if (t.getTyp() == TypTransakcji.WYDATEK) {
                    sumaWydatków += t.getKwota();
                }
            }
        }
        return sumaWydatków > limit;
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
    
    // Gettery i settery
    public Kategoria getKategoria() { return kategoria; }
    public double getLimit() { return limit; }
    public String getOkres() { return okres; }
    public List<Transakcja> getTransakcje() { return transakcje; }
    
    public void setKategoria(Kategoria kategoria) { this.kategoria = kategoria; }
    public void setLimit(double limit) { this.limit = limit; }
    public void setOkres(String okres) { this.okres = okres; }
    public void setTransakcje(List<Transakcja> transakcje) { this.transakcje = transakcje; }
}