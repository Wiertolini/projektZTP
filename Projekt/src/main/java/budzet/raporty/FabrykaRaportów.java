package budzet.raporty;

import budzet.rdzen.MenedżerBudżetu;

public class FabrykaRaportów {
    private MenedżerBudżetu menedżer;
    public FabrykaRaportów(MenedżerBudżetu menedżer) { this.menedżer = menedżer; }
    
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