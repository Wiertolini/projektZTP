package budzet.eksport;

import budzet.rdzen.Transakcja;
import java.text.SimpleDateFormat;
import java.util.List;

/**
@brief Adapter eksportu transakcji do formatu CSV.

@details
Realizuje wzorzec Adapter (@ref wzorzec_adapter):
- Target: @c InterfejsEksportuCSV
- Adapter: @c AdapterEksportuCSV
- Adaptee: @c BibliotekaZapisujaca

Adapter buduje zawartość CSV na podstawie listy transakcji i deleguje zapis do @c BibliotekaZapisujaca.
*/

public class AdapterEksportuCSV implements InterfejsEksportuCSV {
    private BibliotekaZapisujaca zewnetrznySystem;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

    public AdapterEksportuCSV() {
        this.zewnetrznySystem = new BibliotekaZapisujaca();
    }

    @Override
        /**
    @brief Eksportuje listę transakcji do pliku CSV.
    @param transakcje lista transakcji do zapisania.
    */
public void eksportuj(List<Transakcja> transakcje) {
        if (transakcje == null || transakcje.isEmpty()) {
            System.out.println(" Brak transakcji do eksportu.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        // Nagłówek CSV
        sb.append("ID;Data;Kategoria;Typ;Kwota (PLN)\n");

        for (Transakcja t : transakcje) {
            sb.append(t.getId()).append(";")
              .append(sdf.format(t.getData())).append(";")
              .append(t.getKategoria().getNazwa()).append(";")
              .append(t.getTyp().toString()).append(";")
              .append(String.format("%.2f", t.getKwota())).append("\n");
        }

        // Adapter przekształca obiekty domenowe na format zrozumiały dla biblioteki
        String nazwaPliku = "budzet_domowy_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date()) + ".csv";
        zewnetrznySystem.zapiszNaDysk(nazwaPliku, sb.toString());
        
        // Dodatkowe informacje dla użytkownika
        System.out.println(" Statystyki eksportu:");
        System.out.println("   • Liczba transakcji: " + transakcje.size());
        System.out.println("   • Format: CSV (separator średnik)");
        System.out.println("   • Kodowanie: UTF-8");
        
        // Podsumowanie kwot
        double sumaPrzychodów = transakcje.stream()
            .filter(t -> t.getTyp() == budzet.rdzen.TypTransakcji.PRZYCHOD)
            .mapToDouble(t -> t.getKwota())
            .sum();
        double sumaWydatków = transakcje.stream()
            .filter(t -> t.getTyp() == budzet.rdzen.TypTransakcji.WYDATEK)
            .mapToDouble(t -> t.getKwota())
            .sum();
        
        System.out.println("   • Suma przychodów: " + String.format("%.2f", sumaPrzychodów) + " PLN");
        System.out.println("   • Suma wydatków: " + String.format("%.2f", sumaWydatków) + " PLN");
        System.out.println("   • Bilans: " + String.format("%.2f", (sumaPrzychodów - sumaWydatków)) + " PLN");
    }
}