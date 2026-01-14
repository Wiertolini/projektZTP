package budzet.raporty;

import budzet.rdzen.MenedżerBudżetu;
import budzet.rdzen.Transakcja;
import java.util.List;

public abstract class Raport {
    protected MenedżerBudżetu menedżer;
    
    public Raport(MenedżerBudżetu menedżer) {
        this.menedżer = menedżer;
    }
    
    public abstract void generuj();
    
    protected double obliczSumęWydatków() {
        List<Transakcja> transakcje = menedżer.getListaTransakcji();
        return transakcje.stream()
            .filter(t -> t.getTyp() == budzet.rdzen.TypTransakcji.WYDATEK)
            .mapToDouble(t -> t.getKwota())
            .sum();
    }
    
    protected double obliczSumęPrzychodów() {
        List<Transakcja> transakcje = menedżer.getListaTransakcji();
        return transakcje.stream()
            .filter(t -> t.getTyp() == budzet.rdzen.TypTransakcji.PRZYCHOD)
            .mapToDouble(t -> t.getKwota())
            .sum();
    }
}