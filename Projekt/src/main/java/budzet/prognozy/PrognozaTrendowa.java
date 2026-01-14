package budzet.prognozy;

import budzet.rdzen.Transakcja;
import java.util.List;

public class PrognozaTrendowa implements StrategiaPrognozy {
    @Override
    public double prognozuj(List<Transakcja> transakcje) {
        if (transakcje == null || transakcje.size() < 2) {
            return 0;
        }
        
        // Wyodrębniamy tylko wydatki
        List<Transakcja> wydatki = transakcje.stream()
            .filter(t -> t.getTyp() == budzet.rdzen.TypTransakcji.WYDATEK)
            .toList();
        
        if (wydatki.size() < 2) {
            return 0;
        }
        
        // Obliczamy średnią z ostatnich 3 transakcji
        int n = Math.min(3, wydatki.size());
        double suma = 0;
        
        for (int i = wydatki.size() - n; i < wydatki.size(); i++) {
            suma += wydatki.get(i).getKwota();
        }
        
        // Prognoza: średnia z ostatnich n transakcji * współczynnik wzrostu (jeśli jest trend)
        double średnia = suma / n;
        
        // Jeśli mamy więcej niż 2 transakcje, obliczamy trend
        if (wydatki.size() >= 3) {
            Transakcja ostatnia = wydatki.get(wydatki.size() - 1);
            Transakcja przedostatnia = wydatki.get(wydatki.size() - 2);
            double trend = (ostatnia.getKwota() - przedostatnia.getKwota()) / przedostatnia.getKwota();
            
            // Prognoza uwzględniająca trend (maksymalnie +20%)
            return średnia * (1 + Math.min(trend, 0.2));
        }
        
        return średnia;
    }
    
    @Override
    public String getNazwaStrategii() {
        return "Prognoza trendowa wydatków";
    }
}