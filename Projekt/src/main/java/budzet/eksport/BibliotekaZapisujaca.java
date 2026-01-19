package budzet.eksport;

import java.io.FileWriter;
import java.io.IOException;

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