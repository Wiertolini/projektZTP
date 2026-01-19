package budzet.raporty;
import budzet.rdzen.MenedżerBudżetu;

public abstract class Raport {
    protected MenedżerBudżetu menedżer;
    public Raport(MenedżerBudżetu menedżer) { this.menedżer = menedżer; }
    public abstract void generuj();
}