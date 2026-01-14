package budzet.prognozy;

import budzet.rdzen.Transakcja;
import java.util.List;

public interface StrategiaPrognozy {
    double prognozuj(List<Transakcja> transakcje);
    String getNazwaStrategii();
}