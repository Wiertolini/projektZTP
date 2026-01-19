package budzet.raporty;

import budzet.rdzen.MenedżerBudżetu;
import budzet.rdzen.Transakcja;
import budzet.rdzen.TypTransakcji;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RaportMiesięczny extends Raport {
    
    public RaportMiesięczny(MenedżerBudżetu menedżer) {
        super(menedżer);
    }
    
    @Override
    public void generuj() {
        System.out.println("\n RAPORT MIESIĘCZNY");
        System.out.println("========================================");
        
        List<Transakcja> transakcje = menedżer.getListaTransakcji();
        
        double sumaPrzychodów = obliczSumęPrzychodów();
        double sumaWydatków = obliczSumęWydatków();
        double bilans = sumaPrzychodów - sumaWydatków;
        
        System.out.println("Podsumowanie finansowe:");
        System.out.println("  Przychody: " + sumaPrzychodów + " PLN");
        System.out.println("  Wydatki:   " + sumaWydatków + " PLN");
        System.out.println("  Bilans:    " + bilans + " PLN");
        System.out.println();
        
        // Grupowanie wydatków według kategorii
        Map<String, Double> wydatkiWedługKategorii = transakcje.stream()
            .filter(t -> t.getTyp() == TypTransakcji.WYDATEK)
            .collect(Collectors.groupingBy(
                t -> t.getKategoria().getNazwa(),
                Collectors.summingDouble(t -> t.getKwota())
            ));
        
        if (!wydatkiWedługKategorii.isEmpty()) {
            System.out.println("Wydatki według kategorii:");
            wydatkiWedługKategorii.forEach((kategoria, kwota) -> {
                double procent = (kwota / sumaWydatków) * 100;
                System.out.printf("  %-15s: %8.2f PLN (%5.1f%%)\n", 
                    kategoria, kwota, procent);
            });
        }
        
        System.out.println("========================================\n");
    }
}