package budzet.raporty;

import budzet.rdzen.MenedżerBudżetu;

public class FabrykaRaportów {
    private MenedżerBudżetu menedżer;
    
    public FabrykaRaportów(MenedżerBudżetu menedżer) {
        this.menedżer = menedżer;
    }
    
    public Raport utwórzRaport(String typ, String... parametry) {
        switch (typ.toLowerCase()) {
            case "miesięczny":
                return new RaportMiesięczny(menedżer);
                
            case "kategorii":
                if (parametry.length < 1) {
                    throw new IllegalArgumentException("Brak nazwy kategorii dla raportu");
                }
                return new RaportKategorii(menedżer, parametry[0]);
                
            default:
                throw new IllegalArgumentException("Nieznany typ raportu: " + typ);
        }
    }
}