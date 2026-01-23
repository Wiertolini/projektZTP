package budzet.eksport;

import java.io.FileWriter;
import java.io.IOException;

/**
@brief Prosty komponent zapisujący tekst do pliku.

@details
Pełni rolę @b Adaptee we wzorcu Adapter (@ref wzorzec_adapter).
Posiada własny interfejs (metoda @c zapiszNaDysk), który jest dopasowywany przez @c AdapterEksportuCSV.
*/

public class BibliotekaZapisujaca {
    public void zapiszNaDysk(String nazwaPliku, String zawartosc) {
        try (FileWriter writer = new FileWriter(nazwaPliku)) {
            writer.write(zawartosc);
            System.out.println("✓ Dane zostały zapisane do pliku: " + nazwaPliku);
            System.out.println("✓ Ścieżka: " + new java.io.File(nazwaPliku).getAbsolutePath());
        } catch (IOException e) {
            System.err.println(" Błąd podczas zapisywania do pliku: " + e.getMessage());
        }
    }
}