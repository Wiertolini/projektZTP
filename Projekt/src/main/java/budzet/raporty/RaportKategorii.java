package budzet.raporty;

import budzet.rdzen.MenedżerBudżetu;
import budzet.rdzen.TypTransakcji;

public class RaportKategorii extends Raport {
    private String kategoria;
    
    public RaportKategorii(MenedżerBudżetu menedżer, String kategoria) {
        super(menedżer);
        this.kategoria = kategoria;
    }
    
    @Override
    public void generuj() {
        System.out.println("\n=== RAPORT KATEGORII: " + kategoria + " ===");
        double suma = menedżer.getListaTransakcji().stream()
            .filter(t -> t.getKategoria().getNazwa().equals(kategoria))
            .filter(t -> t.getTyp() == TypTransakcji.WYDATEK)
            .mapToDouble(t -> t.getKwota()).sum();
        System.out.println("Wydano w tej kategorii: " + suma + " PLN");
    }
}