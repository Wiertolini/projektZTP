package budzet;

import budzet.eksport.*;
import budzet.obserwatorzy.*;
import budzet.prognozy.*;
import budzet.raporty.*;
import budzet.rdzen.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class InterfejsKonsolowy {
    private static final Scanner scanner = new Scanner(System.in);
    private static final MenedżerBudżetu menedżer = MenedżerBudżetu.pobierzInstancję();
    private static final List<Kategoria> kategorie = new ArrayList<>();
    private static final FabrykaRaportów fabryka = new FabrykaRaportów(menedżer);
    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

    record Opcja(String opis, Runnable akcja) {}

    public static void main(String[] args) {
        inicjalizujDane();
        wyświetlNagłówek();
        
        runMenu("MENU GŁÓWNE", List.of(
            new Opcja("Transakcje", InterfejsKonsolowy::menuTransakcje),
            new Opcja("Kategorie", InterfejsKonsolowy::menuKategorie),
            new Opcja("Budżety", InterfejsKonsolowy::menuBudzety),
            new Opcja("Raporty", InterfejsKonsolowy::menuRaporty),
            new Opcja("Prognozy", InterfejsKonsolowy::menuPrognozy),
            new Opcja("obserwator budzetu", InterfejsKonsolowy::menuObserwatory),
            new Opcja("Eksport", InterfejsKonsolowy::menuEksport),
            new Opcja("Podsumowanie systemu", InterfejsKonsolowy::pokazPodsumowanie)
        ));
        
        System.out.println("\nDziękujemy za korzystanie z aplikacji!");
        scanner.close();
    }

    private static void wyświetlNagłówek() {
        System.out.println("\n═══════════════════════════════════════════════");
        System.out.println("          APLIKACJA BUDŻET DOMOWY");
        System.out.println("═══════════════════════════════════════════════\n");
    }

    private static void runMenu(String tytul, List<Opcja> opcje) {
        while (true) {
            System.out.println("\n══════════ " + tytul + " ══════════");
            for (int i = 0; i < opcje.size(); i++) 
                System.out.println((i + 1) + ". " + opcje.get(i).opis);
            System.out.println((opcje.size() + 1) + ". Powrót");
            System.out.print("Wybierz opcję: ");
            
            int w = pobierzLiczbe(1, opcje.size() + 1);
            if (w == opcje.size() + 1) return;
            
            System.out.println();
            opcje.get(w - 1).akcja.run();
        }
    }

    private static int pobierzLiczbe(int min, int max) {
        while (true) {
            try {
                int liczba = Integer.parseInt(scanner.nextLine());
                if (liczba >= min && liczba <= max) return liczba;
                System.out.print("Podaj liczbę od " + min + " do " + max + ": ");
            } catch (Exception e) {
                System.out.print("Niepoprawny format. Spróbuj ponownie: ");
            }
        }
    }
    
    private static double pobierzKwotę() {
        while (true) {
            try {
                System.out.print("Podaj kwotę: ");
                double kwota = Double.parseDouble(scanner.nextLine());
                if (kwota > 0) return kwota;
                System.out.println("Kwota musi być większa od 0!");
            } catch (Exception e) {
                System.out.println("Niepoprawna kwota!");
            }
        }
    }
    
    private static Kategoria wybierzKategorię() {
        if (kategorie.isEmpty()) {
            System.out.println("Brak kategorii. Najpierw dodaj kategorie.");
            return null;
        }
        
        System.out.println("\nDostępne kategorie:");
        for (int i = 0; i < kategorie.size(); i++) {
            System.out.println((i + 1) + ". " + kategorie.get(i).getNazwa());
        }
        System.out.print("Wybierz kategorię: ");
        int wybór = pobierzLiczbe(1, kategorie.size());
        return kategorie.get(wybór - 1);
    }
    
    // --- MENU TRANSAKCJI ---
    private static void menuTransakcje() {
        runMenu("TRANSAKCJE", List.of(
            new Opcja("Wyświetl wszystkie", InterfejsKonsolowy::wyświetlTransakcje),
            new Opcja("Dodaj transakcję", InterfejsKonsolowy::dodajTransakcję),
            new Opcja("Podsumowanie", InterfejsKonsolowy::podsumowanieTransakcji)
        ));
    }
    
    private static void wyświetlTransakcje() {
        List<Transakcja> transakcje = menedżer.getListaTransakcji();
        
        if (transakcje.isEmpty()) {
            System.out.println("Brak transakcji.");
            return;
        }
        
        System.out.println("LISTA TRANSAKCJI:");
        System.out.println("---------------------------------------------------");
        
        for (Transakcja t : transakcje) {
            String typ = t.getTyp() == TypTransakcji.PRZYCHOD ? "Przychód" : "Wydatek";
            String znak = t.getTyp() == TypTransakcji.PRZYCHOD ? "+" : "-";
            System.out.printf("%d | %s | %-15s | %-8s | %s%.2f PLN\n",
                t.getId(), sdf.format(t.getData()), t.getKategoria().getNazwa(), typ, znak, t.getKwota());
        }
        
        System.out.println("---------------------------------------------------");
        System.out.println("Łącznie: " + transakcje.size() + " transakcji");
    }
    
    private static void dodajTransakcję() {
        System.out.println("DODAWANIE TRANSAKCJI");
        
        System.out.println("Typ transakcji:");
        System.out.println("1. Przychód");
        System.out.println("2. Wydatek");
        System.out.print("Wybierz: ");
        TypTransakcji typ = pobierzLiczbe(1, 2) == 1 ? TypTransakcji.PRZYCHOD : TypTransakcji.WYDATEK;
        
        double kwota = pobierzKwotę();
        Kategoria kategoria = wybierzKategorię();
        if (kategoria == null) return;
        
        int noweId = menedżer.getListaTransakcji().stream()
            .mapToInt(Transakcja::getId)
            .max().orElse(0) + 1;
        
        Transakcja transakcja = new Transakcja(noweId, new Date(), kwota, kategoria, typ);
        menedżer.dodajTransakcję(transakcja);
        
        System.out.println("\nDodano transakcję:");
        System.out.println("ID: " + noweId + " | " + sdf.format(new Date()));
        System.out.println("Kategoria: " + kategoria.getNazwa());
        System.out.println("Typ: " + (typ == TypTransakcji.PRZYCHOD ? "Przychód" : "Wydatek"));
        System.out.println("Kwota: " + kwota + " PLN");
        
        // Sprawdź przekroczenie budżetu (Observer)
        menedżer.getListaBudżetów().stream()
            .filter(b -> b.getKategoria().equals(kategoria) && b.czyPrzekroczony())
            .forEach(b -> System.out.println("UWAGA: Przekroczono budżet dla " + kategoria.getNazwa()));
    }
    
    private static void podsumowanieTransakcji() {
        List<Transakcja> transakcje = menedżer.getListaTransakcji();
        
        double przychody = transakcje.stream()
            .filter(t -> t.getTyp() == TypTransakcji.PRZYCHOD)
            .mapToDouble(Transakcja::getKwota).sum();
        double wydatki = transakcje.stream()
            .filter(t -> t.getTyp() == TypTransakcji.WYDATEK)
            .mapToDouble(Transakcja::getKwota).sum();
        
        System.out.println("PODSUMOWANIE FINANSOWE:");
        System.out.println("Przychody:  +" + przychody + " PLN");
        System.out.println("Wydatki:    -" + wydatki + " PLN");
        System.out.println("Bilans:     " + (przychody - wydatki) + " PLN");
        System.out.println("Transakcji: " + transakcje.size());
    }
    
    // --- MENU KATEGORII ---
    private static void menuKategorie() {
        runMenu("KATEGORIE", List.of(
            new Opcja("Lista kategorii", InterfejsKonsolowy::wyświetlKategorie),
            new Opcja("Dodaj kategorię", InterfejsKonsolowy::dodajKategorię),
            new Opcja("Statystyki kategorii", InterfejsKonsolowy::statystykiKategorii)
        ));
    }
    
    private static void wyświetlKategorie() {
        System.out.println("KATEGORIE:");
        for (int i = 0; i < kategorie.size(); i++) {
            Kategoria k = kategorie.get(i);
            long liczba = menedżer.getListaTransakcji().stream()
                .filter(t -> t.getKategoria().equals(k)).count();
            System.out.println((i + 1) + ". " + k.getNazwa() + " (" + liczba + " transakcji)");
        }
    }
    
    private static void dodajKategorię() {
        System.out.print("Nazwa nowej kategorii: ");
        String nazwa = scanner.nextLine().trim();
        
        if (nazwa.isEmpty()) {
            System.out.println("Nazwa nie może być pusta!");
            return;
        }
        
        if (kategorie.stream().anyMatch(k -> k.getNazwa().equalsIgnoreCase(nazwa))) {
            System.out.println("Kategoria już istnieje!");
            return;
        }
        
        kategorie.add(new Kategoria(nazwa));
        System.out.println("Dodano kategorię: " + nazwa);
    }
    
    private static void statystykiKategorii() {
        System.out.println("STATYSTYKI KATEGORII:");
        for (Kategoria kat : kategorie) {
            List<Transakcja> transakcje = menedżer.getListaTransakcji().stream()
                .filter(t -> t.getKategoria().equals(kat)).toList();
            
            double sumaWydatków = transakcje.stream()
                .filter(t -> t.getTyp() == TypTransakcji.WYDATEK)
                .mapToDouble(Transakcja::getKwota).sum();
            
            if (transakcje.size() > 0) {
                System.out.printf("%-15s: %d transakcji, wydatki: %.2f PLN\n",
                    kat.getNazwa(), transakcje.size(), sumaWydatków);
            }
        }
    }
    
    // --- MENU BUDŻETÓW ---
    private static void menuBudzety() {
        runMenu("BUDŻETY", List.of(
            new Opcja("Lista budżetów", InterfejsKonsolowy::wyświetlBudżety),
            new Opcja("Dodaj budżet", InterfejsKonsolowy::dodajBudżet),
            new Opcja("Sprawdź przekroczenia", InterfejsKonsolowy::sprawdźPrzekroczenia)
        ));
    }
    
    private static void wyświetlBudżety() {
        List<Budżet> budżety = menedżer.getListaBudżetów();
        
        if (budżety.isEmpty()) {
            System.out.println("Brak budżetów.");
            return;
        }
        
        System.out.println("BUDŻETY:");
        System.out.println("---------------------------------------------------");
        
        for (Budżet b : budżety) {
            double wydatki = b.getAktualneWydatki();
            double procent = (wydatki / b.getLimit()) * 100;
            String status = procent >= 100 ? "PRZEKROCZONY" : procent >= 80 ? "BLISKO LIMITU" : "OK";
            
            System.out.printf("%-15s | Limit: %.2f | Wydatki: %.2f (%.1f%%) | %s\n",
                b.getKategoria().getNazwa(), b.getLimit(), wydatki, procent, status);
        }
    }
    
    private static void dodajBudżet() {
        System.out.println("DODAWANIE BUDŻETU");
        Kategoria kat = wybierzKategorię();
        if (kat == null) return;
        
        System.out.print("Miesięczny limit: ");
        double limit = pobierzKwotę();
        
        System.out.print("Okres (np. 2024-05): ");
        String okres = scanner.nextLine().trim();
        
        menedżer.dodajBudżet(new Budżet(kat, limit, okres));
        System.out.println("Dodano budżet dla " + kat.getNazwa() + " (limit: " + limit + " PLN)");
    }
    
    private static void sprawdźPrzekroczenia() {
        System.out.println("SPRAWDZANIE PRZEKROCZEŃ:");
        boolean znaleziono = false;
        
        for (Budżet b : menedżer.getListaBudżetów()) {
            if (b.czyPrzekroczony()) {
                znaleziono = true;
                System.out.println("PRZEKROCZENIE: " + b.getKategoria().getNazwa());
                System.out.println("  Limit: " + b.getLimit() + " PLN");
                System.out.println("  Wydatki: " + b.getAktualneWydatki() + " PLN");
                System.out.println("  Przekroczenie: " + (b.getAktualneWydatki() - b.getLimit()) + " PLN\n");
            }
        }
        
        if (!znaleziono) System.out.println("Brak przekroczeń budżetu.");
    }
    
    // --- MENU RAPORTÓW (FACTORY METHOD) ---
    private static void menuRaporty() {
        runMenu("RAPORTY", List.of(
            new Opcja("Raport miesięczny", () -> fabryka.utwórzRaport("miesięczny").generuj()),
            new Opcja("Raport kategorii", () -> {
                Kategoria kat = wybierzKategorię();
                if (kat != null) fabryka.utwórzRaport("kategorii", kat.getNazwa()).generuj();
            }),
            new Opcja("Porównanie raportów", InterfejsKonsolowy::porównajRaporty)
        ));
    }
    
    private static void porównajRaporty() {
        System.out.println("PORÓWNANIE RAPORTÓW:");
        System.out.println("\n1. Raport miesięczny:");
        fabryka.utwórzRaport("miesięczny").generuj();
        
        if (!kategorie.isEmpty()) {
            System.out.println("\n2. Raport dla kategorii " + kategorie.get(0).getNazwa() + ":");
            fabryka.utwórzRaport("kategorii", kategorie.get(0).getNazwa()).generuj();
        }
    }
    
    // --- MENU PROGNOZ (STRATEGY) ---
    private static void menuPrognozy() {
        runMenu("PROGNOZY ", List.of(
            new Opcja("Średnia prosta", () -> wykonajPrognozę(new ŚredniaProstaPrognoza())),
            new Opcja("Trendowa", () -> wykonajPrognozę(new PrognozaTrendowa())),
            new Opcja("Porównanie strategii", InterfejsKonsolowy::porównajStrategie)
        ));
    }
    
    private static void wykonajPrognozę(StrategiaPrognozy strategia) {
        double wynik = strategia.prognozuj(menedżer.getListaTransakcji());
        System.out.println("Strategia: " + strategia.getNazwaStrategii());
        System.out.println("Prognoza: " + wynik + " PLN");
        System.out.println("Prognoza miesięczna: " + (wynik * 30) + " PLN");
    }
    
    private static void porównajStrategie() {
        StrategiaPrognozy s1 = new ŚredniaProstaPrognoza();
        StrategiaPrognozy s2 = new PrognozaTrendowa();
        
        double p1 = s1.prognozuj(menedżer.getListaTransakcji());
        double p2 = s2.prognozuj(menedżer.getListaTransakcji());
        
        System.out.println("PORÓWNANIE STRATEGII PROGNOZOWANIA:");
        System.out.printf("%-20s: %.2f PLN\n", s1.getNazwaStrategii(), p1);
        System.out.printf("%-20s: %.2f PLN\n", s2.getNazwaStrategii(), p2);
        System.out.printf("Różnica: %.2f PLN\n", Math.abs(p1 - p2));
        
        if (p2 > p1) System.out.println("Trend wskazuje na wzrost wydatków.");
        else if (p2 < p1) System.out.println("Trend wskazuje na spadek wydatków.");
        else System.out.println("Brak wyraźnego trendu.");
    }
    
    // --- MENU OBSERWATORÓW ---
    private static void menuObserwatory() {
        runMenu("MENU OBSERWATORÓW", List.of(
            new Opcja("Test powiadomień", () -> {
                System.out.println("Testowanie powiadomień...");
                // Dodajemy transakcję, która może przekroczyć budżet
                if (!kategorie.isEmpty()) {
                    menedżer.dodajTransakcję(new Transakcja(
                        999, new Date(), 250, kategorie.get(0), TypTransakcji.WYDATEK
                    ));
                    System.out.println("Dodano testową transakcję.");
                    sprawdźPrzekroczenia();
                }
            }),
            new Opcja("Informacje o systemie", () -> {
                System.out.println("SYSTEM OBSERWATORÓW:");
                System.out.println("Menedżer ma " + menedżer.getListaBudżetów().size() + " budżetów.");
                System.out.println("Każde przekroczenie budżetu wywołuje powiadomienia.");
            })
        ));
    }
    
    // --- MENU EKSPORTU (ADAPTER) ---
    private static void menuEksport() {
        runMenu("EKSPORT", List.of(
            new Opcja("Eksport do CSV", () -> {
                InterfejsEksportuCSV eksporter = new AdapterEksportuCSV();
                eksporter.eksportuj(menedżer.getListaTransakcji());
                System.out.println("Dane wyeksportowane do formatu CSV.");
            }),
            new Opcja("Podgląd danych do eksportu", InterfejsKonsolowy::podglądDanych)
        ));
    }
    
    private static void podglądDanych() {
        System.out.println("DANE DO EKSPORTU (CSV):");
        System.out.println("ID;Data;Kategoria;Typ;Kwota");
        for (Transakcja t : menedżer.getListaTransakcji()) {
            System.out.printf("%d;%s;%s;%s;%.2f\n",
                t.getId(), sdf.format(t.getData()), t.getKategoria().getNazwa(),
                t.getTyp() == TypTransakcji.PRZYCHOD ? "Przychód" : "Wydatek", t.getKwota());
        }
    }
    
    // --- PODSUMOWANIE SYSTEMU ---
    private static void pokazPodsumowanie() {
        
        System.out.println("\nSTATYSTYKI SYSTEMU:");
        System.out.println("Transakcji: " + menedżer.getListaTransakcji().size());
        System.out.println("Kategorii: " + kategorie.size());
        System.out.println("Budżetów: " + menedżer.getListaBudżetów().size());
        
        double bilans = menedżer.getListaTransakcji().stream()
            .mapToDouble(t -> t.getTyp() == TypTransakcji.PRZYCHOD ? t.getKwota() : -t.getKwota())
            .sum();
        System.out.println("Bilans finansowy: " + bilans + " PLN");
    }
    
    // --- INICJALIZACJA DANYCH ---
    private static void inicjalizujDane() {
        // Dodajemy domyślne kategorie
        kategorie.add(new Kategoria("Jedzenie"));
        kategorie.add(new Kategoria("Transport"));
        kategorie.add(new Kategoria("Rozrywka"));
        kategorie.add(new Kategoria("Rachunki"));
        
        // Rejestrujemy obserwatora
        menedżer.dodajObserwatora(new UsługaPowiadomień());
        
        // Dodajemy przykładowe budżety
        if (kategorie.size() >= 2) {
            menedżer.dodajBudżet(new Budżet(kategorie.get(0), 800.0, "2024-05"));
            menedżer.dodajBudżet(new Budżet(kategorie.get(1), 300.0, "2024-05"));
        }
        
        // Dodajemy przykładowe transakcje (jeśli system jest pusty)
        if (menedżer.getListaTransakcji().isEmpty()) {
            menedżer.dodajTransakcję(new Transakcja(1, new Date(), 150.0, kategorie.get(0), TypTransakcji.WYDATEK));
            menedżer.dodajTransakcję(new Transakcja(2, new Date(), 200.0, kategorie.get(0), TypTransakcji.WYDATEK));
            menedżer.dodajTransakcję(new Transakcja(3, new Date(), 100.0, kategorie.get(1), TypTransakcji.WYDATEK));
            menedżer.dodajTransakcję(new Transakcja(4, new Date(), 2500.0, kategorie.get(0), TypTransakcji.PRZYCHOD));
            System.out.println("Zainicjalizowano przykładowe dane.");
        }
    }
}