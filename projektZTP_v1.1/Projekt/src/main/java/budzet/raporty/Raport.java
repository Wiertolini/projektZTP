package budzet.raporty;
import budzet.rdzen.MenedżerBudżetu;

/**
@brief Abstrakcyjna baza dla raportów.

@details
Pełni rolę @b Product w fabryce raportów (@ref wzorzec_factory). Konkretne raporty implementują metodę @c generuj().
*/

public abstract class Raport {
    protected MenedżerBudżetu menedżer;
    public Raport(MenedżerBudżetu menedżer) { this.menedżer = menedżer; }
    public abstract void generuj();
}