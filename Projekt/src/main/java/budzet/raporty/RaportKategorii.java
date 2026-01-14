package budzet.raporty;

import budzet.rdzen.MenedżerBudżetu;
import budzet.rdzen.Budżet;
import budzet.rdzen.Transakcja;
import budzet.rdzen.TypTransakcji;
import java.util.List;

public class RaportKategorii extends Raport {
    private String nazwaKategorii;
    
    public RaportKategorii(MenedżerBudżetu menedżer, String nazwaKategorii) {
        super(menedżer);
        this.nazwaKategorii = nazwaKategorii;
    }
    
    @Override
    public void generuj() {
        System.out.println("\n📈 RAPORT KATEGORII: " + nazwaKategorii);
        System.out.println("========================================");
        
        // Filtruj transakcje z danej kategorii
        List<Transakcja> transakcjeKategorii = menedżer.getListaTransakcji().stream()
            .filter(t -> t.getKategoria().getNazwa().equals(nazwaKategorii))
            .toList();
        
        if (transakcjeKategorii.isEmpty()) {
            System.out.println("Brak transakcji w kategorii: " + nazwaKategorii);
            return;
        }
        
        double sumaWydatków = transakcjeKategorii.stream()
            .filter(t -> t.getTyp() == TypTransakcji.WYDATEK)
            .mapToDouble(t -> t.getKwota())
            .sum();
        
        double sumaPrzychodów = transakcjeKategorii.stream()
            .filter(t -> t.getTyp() == TypTransakcji.PRZYCHOD)
            .mapToDouble(t -> t.getKwota())
            .sum();
        
        System.out.println("Statystyki kategorii:");
        System.out.println("  Liczba transakcji: " + transakcjeKategorii.size());
        System.out.println("  Suma wydatków:    " + sumaWydatków + " PLN");
        System.out.println("  Suma przychodów:  " + sumaPrzychodów + " PLN");
        System.out.println("  Saldo kategorii:  " + (sumaPrzychodów - sumaWydatków) + " PLN");
        System.out.println();
        
        // Sprawdź czy istnieje budżet dla tej kategorii
        for (Budżet budżet : menedżer.getListaBudżetów()) {
            if (budżet.getKategoria().getNazwa().equals(nazwaKategorii)) {
                System.out.println("Informacje o budżecie:");
                System.out.println("  Limit miesięczny: " + budżet.getLimit() + " PLN");
                System.out.println("  Wydatki aktualne: " + budżet.getAktualneWydatki() + " PLN");
                System.out.println("  Procent wykorzystania: " + 
                    (budżet.getAktualneWydatki() / budżet.getLimit() * 100) + "%");
                System.out.println("  Status: " + 
                    (budżet.czyPrzekroczony() ? "PRZEKROCZONY ⚠️" : "w normie ✅"));
                break;
            }
        }
        
        System.out.println("========================================\n");
    }
}