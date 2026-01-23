package budzet.prognozy;

import budzet.rdzen.Transakcja;
import java.util.List;

/**
@brief Interfejs strategii prognozowania wydatków.

@details
Element wzorca Strategia (@ref wzorzec_strategy). Pozwala podmieniać algorytmy prognozy bez zmiany kodu wywołującego.
*/

public interface StrategiaPrognozy {
    /**
    @brief Wylicza prognozowaną wartość wydatków na podstawie listy transakcji.
    @param transakcje lista transakcji historycznych.
    @return prognozowana kwota wydatków.
    */
    double prognozuj(List<Transakcja> transakcje);

    /**
    @brief Zwraca nazwę strategii widoczną dla użytkownika.
    @return nazwa strategii.
    */
    String getNazwaStrategii();
}
