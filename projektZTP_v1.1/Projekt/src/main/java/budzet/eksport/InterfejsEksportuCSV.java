package budzet.eksport;

import budzet.rdzen.Transakcja;
import java.util.List;

/**
@brief Interfejs eksportu transakcji do CSV.

@details
Pełni rolę @b Target we wzorcu Adapter (@ref wzorzec_adapter).
*/

public interface InterfejsEksportuCSV {
    void eksportuj(List<Transakcja> transakcje);
}