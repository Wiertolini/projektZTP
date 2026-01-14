package budzet.prognozy;

import budzet.rdzen.Transakcja;
import java.util.List;

public class ŚredniaProstaPrognoza implements StrategiaPrognozy {
    @Override
    public double prognozuj(List<Transakcja> transakcje) {
        if (transakcje == null || transakcje.isEmpty()) {
            return 0;
        }
        
        double sumaWydatków = 0;
        int liczbaWydatków = 0;
        
        for (Transakcja t : transakcje) {
            if (t.getTyp() == budzet.rdzen.TypTransakcji.WYDATEK) {
                sumaWydatków += t.getKwota();
                liczbaWydatków++;
            }
        }
        
        return liczbaWydatków > 0 ? sumaWydatków / liczbaWydatków : 0;
    }
    
    @Override
    public String getNazwaStrategii() {
        return "Średnia prosta wydatków";
    }
}