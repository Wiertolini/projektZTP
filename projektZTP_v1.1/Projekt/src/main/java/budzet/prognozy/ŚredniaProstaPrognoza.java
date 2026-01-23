package budzet.prognozy;

import budzet.rdzen.Transakcja;
import budzet.rdzen.TypTransakcji;
import java.util.List;

/**
@brief Strategia prognozowania oparta o średnią arytmetyczną wydatków.

@details
ConcreteStrategy we wzorcu Strategia (@ref wzorzec_strategy).
*/

public class ŚredniaProstaPrognoza implements StrategiaPrognozy {
    @Override
    public double prognozuj(List<Transakcja> transakcje) {
        if (transakcje == null || transakcje.isEmpty()) return 0;
        
        double sumaWydatków = 0;
        int licznik = 0;
        
        for (Transakcja t : transakcje) {
            if (t.getTyp() == TypTransakcji.WYDATEK) {
                sumaWydatków += t.getKwota();
                licznik++;
            }
        }
        return licznik > 0 ? sumaWydatków / licznik : 0;
    }
    
    @Override
    public String getNazwaStrategii() { return "Średnia arytmetyczna"; }
}