package budzet.obserwatorzy;

import budzet.rdzen.Budżet;

/**
@brief Implementacja powiadomień o przekroczeniu limitu budżetu.

@details
ConcreteObserver we wzorcu Obserwator (@ref wzorzec_observer). W tej implementacji alarm jest wypisywany w konsoli.
*/

public class UsługaPowiadomień implements ObserwatorBudżetu {
    @Override
    public void aktualizuj(Budżet budżet) {
        System.out.println("[ALARM] Przekroczono budżet w kategorii: " + 
            budżet.getKategoria().getNazwa() + 
            " | Limit: " + budżet.getLimit() + 
            " | Wydano: " + budżet.getAktualneWydatki());
    }
}