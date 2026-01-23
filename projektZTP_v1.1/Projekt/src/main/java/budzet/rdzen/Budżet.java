package budzet.rdzen;

import java.util.List;

/**
@brief Model budżetu dla danej kategorii i okresu.

Przechowuje limit oraz listę transakcji przypisanych do kategorii; umożliwia sprawdzenie, czy limit został przekroczony.
Wzorzec Obserwator przekazuje instancję budżetu do obserwatorów jako „stan” (@ref wzorzec_observer).
*/

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