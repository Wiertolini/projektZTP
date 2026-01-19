package budzet.eksport;

import budzet.rdzen.Transakcja;
import java.util.List;

public interface InterfejsEksportuCSV {
    void eksportuj(List<Transakcja> transakcje);
}