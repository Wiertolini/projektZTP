package budzet.obserwatorzy;

import budzet.rdzen.Budżet;

public class UsługaPowiadomień implements ObserwatorBudżetu {
    @Override
    public void aktualizuj(Budżet budżet) {
        System.out.println("[ALARM] Przekroczono budżet w kategorii: " + 
            budżet.getKategoria().getNazwa() + 
            " | Limit: " + budżet.getLimit() + 
            " | Wydano: " + budżet.getAktualneWydatki());
    }
}