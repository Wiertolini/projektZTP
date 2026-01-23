package budzet.obserwatorzy;
import budzet.rdzen.Budżet;

/**
@brief Interfejs obserwatora budżetu.

@details
Element wzorca Obserwator (@ref wzorzec_observer). Implementacje są powiadamiane, gdy budżet w danej kategorii
zostanie przekroczony.
*/

public interface ObserwatorBudżetu {
    void aktualizuj(Budżet budżet);
}