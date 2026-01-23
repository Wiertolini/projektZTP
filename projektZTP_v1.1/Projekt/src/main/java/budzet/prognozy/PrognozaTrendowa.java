package budzet.prognozy;

import budzet.rdzen.Transakcja;
import budzet.rdzen.TypTransakcji;
import java.util.List;

/**
@brief Strategia prognozowania wydatków uwzględniająca trend z ostatnich transakcji.

@details
ConcreteStrategy we wzorcu Strategia (@ref wzorzec_strategy).
*/

public class PrognozaTrendowa implements StrategiaPrognozy {
    private static final int OKNO_ANALIZY = 3;
    private static final double MAX_WZROST = 0.2; // 20%

    @Override
    public double prognozuj(List<Transakcja> transakcje) {
        if (transakcje == null) return 0;
        
        List<Transakcja> wydatki = transakcje.stream()
            .filter(t -> t.getTyp() == TypTransakcji.WYDATEK)
            .toList();
        
        if (wydatki.size() < 2) return 0;
        
        int n = Math.min(OKNO_ANALIZY, wydatki.size());
        double suma = 0;
        for (int i = wydatki.size() - n; i < wydatki.size(); i++) {
            suma += wydatki.get(i).getKwota();
        }
        double srednia = suma / n;
        
        if (wydatki.size() >= 3) {
            double ost = wydatki.get(wydatki.size() - 1).getKwota();
            double przedOst = wydatki.get(wydatki.size() - 2).getKwota();
            
            if (przedOst == 0) return srednia; // Zabezpieczenie przed NaN
            
            double trend = (ost - przedOst) / przedOst;
            return srednia * (1 + Math.min(trend, MAX_WZROST));
        }
        return srednia;
    }
    
    @Override
    public String getNazwaStrategii() { return "Prognoza trendowa (ostatnie 3)"; }
}