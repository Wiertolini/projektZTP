package budzet.raporty;

import budzet.rdzen.MenedżerBudżetu;
import budzet.rdzen.TypTransakcji;

/**
@brief Raport sumaryczny wydatków (miesięczny).

@details
ConcreteProduct w fabryce raportów (@ref wzorzec_factory).
*/

public class RaportMiesięczny extends Raport {
    public RaportMiesięczny(MenedżerBudżetu menedżer) { super(menedżer); }
    
    @Override
    public void generuj() {
        System.out.println("\n=== RAPORT MIESIĘCZNY ===");
        double wydatki = menedżer.getListaTransakcji().stream()
            .filter(t -> t.getTyp() == TypTransakcji.WYDATEK)
            .mapToDouble(t -> t.getKwota()).sum();
        System.out.println("Całkowite wydatki: " + wydatki + " PLN");
    }
}