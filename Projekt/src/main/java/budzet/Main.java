package budzet;

import budzet.obserwatorzy.UsługaPowiadomień;
import budzet.prognozy.*;
import budzet.raporty.FabrykaRaportów;
import budzet.raporty.Raport;
import budzet.rdzen.*;
import java.util.Date;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== APLIKACJA BUDŻET DOMOWY ===\n");
        
        // 1. Inicjalizacja menedżera (Singleton)
        MenedżerBudżetu menedżer = MenedżerBudżetu.pobierzInstancję();
        
        // 2. Dodanie obserwatora
        UsługaPowiadomień usługaPowiadomień = new UsługaPowiadomień();
        menedżer.dodajObserwatora(usługaPowiadomień);
        
        // 3. Utworzenie kategorii
        Kategoria jedzenie = new Kategoria("Jedzenie");
        Kategoria transport = new Kategoria("Transport");
        Kategoria rozrywka = new Kategoria("Rozrywka");
        Kategoria rachunki = new Kategoria("Rachunki");
        
        // 4. Utworzenie budżetów
        Budżet budżetJedzenie = new Budżet(jedzenie, 800.0, "2024-05");
        Budżet budżetTransport = new Budżet(transport, 300.0, "2024-05");
        
        menedżer.dodajBudżet(budżetJedzenie);
        menedżer.dodajBudżet(budżetTransport);
        
        // 5. Dodanie przykładowych transakcji
        System.out.println("Dodawanie transakcji...");
        
        // Przychody
        menedżer.dodajTransakcję(new Transakcja(1, new Date(), 4500.0, 
            new Kategoria("Wynagrodzenie"), TypTransakcji.PRZYCHOD));
        
        // Wydatki - jedzenie
        menedżer.dodajTransakcję(new Transakcja(2, new Date(), 150.0, jedzenie, 
            TypTransakcji.WYDATEK));
        menedżer.dodajTransakcję(new Transakcja(3, new Date(), 200.0, jedzenie, 
            TypTransakcji.WYDATEK));
        menedżer.dodajTransakcję(new Transakcja(4, new Date(), 300.0, jedzenie, 
            TypTransakcji.WYDATEK));
        menedżer.dodajTransakcję(new Transakcja(5, new Date(), 250.0, jedzenie, 
            TypTransakcji.WYDATEK));
        
        // Wydatki - transport
        menedżer.dodajTransakcję(new Transakcja(6, new Date(), 100.0, transport, 
            TypTransakcji.WYDATEK));
        menedżer.dodajTransakcję(new Transakcja(7, new Date(), 250.0, transport, 
            TypTransakcji.WYDATEK));
        
        // Wydatki - inne
        menedżer.dodajTransakcję(new Transakcja(8, new Date(), 500.0, rachunki, 
            TypTransakcji.WYDATEK));
        menedżer.dodajTransakcję(new Transakcja(9, new Date(), 200.0, rozrywka, 
            TypTransakcji.WYDATEK));
        
        System.out.println("Dodano " + menedżer.getListaTransakcji().size() + " transakcji.\n");
        
        // 6. Testowanie strategii prognozowania
        System.out.println("=== PROGNOZOWANIE WYDATKÓW ===\n");
        
        StrategiaPrognozy strategia1 = new ŚredniaProstaPrognoza();
        StrategiaPrognozy strategia2 = new PrognozaTrendowa();
        
        double prognoza1 = strategia1.prognozuj(menedżer.getListaTransakcji());
        double prognoza2 = strategia2.prognozuj(menedżer.getListaTransakcji());
        
        System.out.println("Strategia: " + strategia1.getNazwaStrategii());
        System.out.println("Prognoza średniego wydatku: " + prognoza1 + " PLN\n");
        
        System.out.println("Strategia: " + strategia2.getNazwaStrategii());
        System.out.println("Prognoza wydatków z trendem: " + prognoza2 + " PLN\n");
        
        // 7. Generowanie raportów (Factory Method)
        System.out.println("=== GENEROWANIE RAPORTÓW ===\n");
        
        FabrykaRaportów fabryka = new FabrykaRaportów(menedżer);
        
        // Raport miesięczny
        Raport raportMiesięczny = fabryka.utwórzRaport("miesięczny");
        raportMiesięczny.generuj();
        
        // Raport kategorii
        Raport raportJedzenie = fabryka.utwórzRaport("kategorii", "Jedzenie");
        raportJedzenie.generuj();
        
        Raport raportTransport = fabryka.utwórzRaport("kategorii", "Transport");
        raportTransport.generuj();
        
        // 8. Podsumowanie
        System.out.println("=== PODSUMOWANIE ===");
        System.out.println("Liczba transakcji: " + menedżer.getListaTransakcji().size());
        System.out.println("Liczba budżetów: " + menedżer.getListaBudżetów().size());
        System.out.println("Aplikacja gotowa do działania!");
    }
}