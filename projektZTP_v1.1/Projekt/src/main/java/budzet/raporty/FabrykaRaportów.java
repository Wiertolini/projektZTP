package budzet.raporty;

import budzet.rdzen.MenedżerBudżetu;

/**
@brief Fabryka tworząca obiekty raportów na podstawie typu żądanego przez użytkownika.

@details
Realizuje wzorzec Fabryka (@ref wzorzec_factory). Metoda @c utwórzRaport(...) enkapsuluje dobór klasy raportu
oraz przekazanie zależności (@c MenedżerBudżetu).
*/

public class FabrykaRaportów {
    private MenedżerBudżetu menedżer;
    public FabrykaRaportów(MenedżerBudżetu menedżer) { this.menedżer = menedżer; }
    
        /**
    @brief Tworzy raport na podstawie typu podanego przez użytkownika.
    @param typ nazwa typu raportu (np. "miesięczny", "kategorii").
    @param parametry dodatkowe parametry zależne od typu (np. nazwa kategorii).
    @return gotowy obiekt raportu.
    @throws IllegalArgumentException gdy podano nieznany typ lub brakuje parametrów.
    */
public Raport utwórzRaport(String typ, String... parametry) {
        switch (typ.toLowerCase()) {
            case "miesięczny": return new RaportMiesięczny(menedżer);
            case "kategorii": 
                if (parametry.length > 0) return new RaportKategorii(menedżer, parametry[0]);
                throw new IllegalArgumentException("Wymagana nazwa kategorii!");
            default: throw new IllegalArgumentException("Nieznany typ raportu");
        }
    }
}