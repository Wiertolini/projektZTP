package budzet.obserwatorzy;
import budzet.rdzen.Budżet;

public interface ObserwatorBudżetu {
    void aktualizuj(Budżet budżet);
}