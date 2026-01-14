package budzet;

import budzet.rdzen.*;
import budzet.obserwatorzy.*;
import budzet.prognozy.*;
import budzet.raporty.*;
import java.util.*;
import java.text.SimpleDateFormat;
import java.util.stream.Collectors;

public class InterfejsKonsolowy {
    private static Scanner scanner = new Scanner(System.in);
    private static MenedżerBudżetu menedżer = MenedżerBudżetu.pobierzInstancję();
    private static List<Kategoria> kategorie = new ArrayList<>();
    private static FabrykaRaportów fabrykaRaportów = new FabrykaRaportów(menedżer);
    
    public static void main(String[] args) {
        inicjalizujDane();
        wyświetlNagłówek();
        
        boolean kontynuuj = true;
        while (kontynuuj) {
            wyświetlMenuGłówne();
            int wybór = pobierzWybór(1, 9);
            
            switch (wybór) {
                case 1 -> zarządzajTransakcjami();
                case 2 -> zarządzajKategoriami();
                case 3 -> zarządzajBudżetami();
                case 4 -> wyświetlStatystyki();
                case 5 -> generujRaporty();
                case 6 -> wykonajPrognozy();
                case 7 -> zarządzajObserwatorami();
                case 8 -> eksportujDane();
                case 9 -> kontynuuj = false;
            }
        }
        
        System.out.println("\nDziękujemy za korzystanie z aplikacji Budżet Domowy!");
        scanner.close();
    }
    
    private static void inicjalizujDane() {
        // Domyślne kategorie
        kategorie.add(new Kategoria("Jedzenie"));
        kategorie.add(new Kategoria("Transport"));
        kategorie.add(new Kategoria("Rozrywka"));
        kategorie.add(new Kategoria("Rachunki"));
        kategorie.add(new Kategoria("Zdrowie"));
        kategorie.add(new Kategoria("Ubrania"));
        kategorie.add(new Kategoria("Inne"));
        
        // Domyślny obserwator
        UsługaPowiadomień usługa = new UsługaPowiadomień();
        menedżer.dodajObserwatora(usługa);
        
        // Przykładowe dane startowe
        if (menedżer.getListaTransakcji().isEmpty()) {
            System.out.println("Tworzenie przykładowych danych...");
            utwórzPrzykładoweDane();
        }
    }
    
    private static void utwórzPrzykładoweDane() {
        // Przykładowe transakcje
        Random rand = new Random();
        Date data = new Date();
        
        for (int i = 1; i <= 15; i++) {
            Kategoria kat = kategorie.get(rand.nextInt(kategorie.size()));
            TypTransakcji typ = (i % 5 == 0) ? TypTransakcji.PRZYCHOD : TypTransakcji.WYDATEK;
            double kwota = typ == TypTransakcji.PRZYCHOD ? 
                rand.nextDouble() * 3000 + 2000 : // Przychody 2000-5000
                rand.nextDouble() * 400 + 50;      // Wydatki 50-450
            
            Transakcja t = new Transakcja(i, data, kwota, kat, typ);
            menedżer.dodajTransakcję(t);
        }
        
        // Przykładowe budżety
        menedżer.dodajBudżet(new Budżet(kategorie.get(0), 800.0, "2024-05"));
        menedżer.dodajBudżet(new Budżet(kategorie.get(1), 300.0, "2024-05"));
        menedżer.dodajBudżet(new Budżet(kategorie.get(2), 500.0, "2024-05"));
    }
    
    private static void wyświetlNagłówek() {
        System.out.println("╔══════════════════════════════════════════════════════╗");
        System.out.println("║            APLIKACJA BUDŻET DOMOWY                  ║");
        System.out.println("║            v1.0 - Interfejs Konsolowy               ║");
        System.out.println("╚══════════════════════════════════════════════════════╝");
        System.out.println();
    }
    
    private static void wyświetlMenuGłówne() {
        System.out.println("\n════════════════════ MENU GŁÓWNE ════════════════════");
        System.out.println("1. 🏷️  Zarządzaj transakcjami");
        System.out.println("2. 📂 Zarządzaj kategoriami");
        System.out.println("3. 💰 Zarządzaj budżetami");
        System.out.println("4. 📊 Wyświetl statystyki");
        System.out.println("5. 📈 Generuj raporty");
        System.out.println("6. 🔮 Wykonaj prognozy");
        System.out.println("7. 🔔 Zarządzaj obserwatorami");
        System.out.println("8. 💾 Eksportuj dane");
        System.out.println("9. ❌ Wyjście");
        System.out.println("═══════════════════════════════════════════════════════");
        System.out.print("Wybierz opcję (1-9): ");
    }
    
    private static int pobierzWybór(int min, int max) {
        int wybór = -1;
        while (wybór < min || wybór > max) {
            try {
                wybór = Integer.parseInt(scanner.nextLine());
                if (wybór < min || wybór > max) {
                    System.out.print("Niepoprawny wybór. Podaj liczbę od " + min + " do " + max + ": ");
                }
            } catch (NumberFormatException e) {
                System.out.print("To nie jest liczba. Podaj liczbę od " + min + " do " + max + ": ");
            }
        }
        return wybór;
    }
    
    private static double pobierzKwotę() {
        double kwota = -1;
        while (kwota <= 0) {
            try {
                System.out.print("Podaj kwotę: ");
                kwota = Double.parseDouble(scanner.nextLine());
                if (kwota <= 0) {
                    System.out.println("Kwota musi być większa od 0!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Niepoprawna kwota!");
            }
        }
        return kwota;
    }
    
    // ========== MENU 1: ZARZĄDZANIE TRANSAKCJAMI ==========
    private static void zarządzajTransakcjami() {
        boolean powrót = false;
        while (!powrót) {
            System.out.println("\n══════════ ZARZĄDZANIE TRANSAKCJAMI ══════════");
            System.out.println("1. ➕ Dodaj nową transakcję");
            System.out.println("2. 👁️  Wyświetl wszystkie transakcje");
            System.out.println("3. 🔍 Wyszukaj transakcje");
            System.out.println("4. ✏️  Edytuj transakcję");
            System.out.println("5. ❌ Usuń transakcję");
            System.out.println("6. ↩️  Powrót do menu głównego");
            System.out.print("Wybierz opcję (1-6): ");
            
            int wybór = pobierzWybór(1, 6);
            
            switch (wybór) {
                case 1 -> dodajTransakcję();
                case 2 -> wyświetlWszystkieTransakcje();
                case 3 -> wyszukajTransakcje();
                case 4 -> edytujTransakcję();
                case 5 -> usuńTransakcję();
                case 6 -> powrót = true;
            }
        }
    }
    
    private static void dodajTransakcję() {
        System.out.println("\n══════════ DODAWANIE NOWEJ TRANSAKCJI ══════════");
        
        // Wybór typu transakcji
        System.out.println("Wybierz typ transakcji:");
        System.out.println("1. 📈 Przychód");
        System.out.println("2. 📉 Wydatek");
        System.out.print("Wybierz (1-2): ");
        int typWybór = pobierzWybór(1, 2);
        TypTransakcji typ = (typWybór == 1) ? TypTransakcji.PRZYCHOD : TypTransakcji.WYDATEK;
        
        // Kwota
        double kwota = pobierzKwotę();
        
        // Wybór kategorii
        Kategoria kategoria = wybierzKategorię();
        if (kategoria == null) return;
        
        // Data (domyślnie teraz)
        Date data = new Date();
        
        // Utworzenie i dodanie transakcji
        int noweId = menedżer.getListaTransakcji().size() + 1;
        Transakcja transakcja = new Transakcja(noweId, data, kwota, kategoria, typ);
        menedżer.dodajTransakcję(transakcja);
        
        System.out.println("✅ Transakcja została dodana pomyślnie!");
        
        // Sprawdzenie czy przekroczono budżet
        for (Budżet b : menedżer.getListaBudżetów()) {
            if (b.getKategoria().equals(kategoria) && b.czyPrzekroczony()) {
                System.out.println("⚠️  Uwaga: Przekroczono budżet dla kategorii: " + kategoria.getNazwa());
            }
        }
    }
    
    private static void wyświetlWszystkieTransakcje() {
        List<Transakcja> transakcje = menedżer.getListaTransakcji();
        
        if (transakcje.isEmpty()) {
            System.out.println("Brak transakcji.");
            return;
        }
        
        System.out.println("\n════════════ LISTA WSZYSTKICH TRANSAKCJI ════════════");
        System.out.printf("%-4s %-12s %-15s %-10s %-10s\n", 
            "ID", "Data", "Kategoria", "Typ", "Kwota");
        System.out.println("--------------------------------------------------------");
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        double sumaWydatków = 0;
        double sumaPrzychodów = 0;
        
        for (Transakcja t : transakcje) {
            String typStr = (t.getTyp() == TypTransakcji.PRZYCHOD) ? "Przychód" : "Wydatek";
            String znak = (t.getTyp() == TypTransakcji.PRZYCHOD) ? "+" : "-";
            
            System.out.printf("%-4d %-12s %-15s %-10s %s%.2f PLN\n",
                t.getId(),
                sdf.format(t.getData()),
                t.getKategoria().getNazwa(),
                typStr,
                znak,
                t.getKwota());
            
            if (t.getTyp() == TypTransakcji.PRZYCHOD) {
                sumaPrzychodów += t.getKwota();
            } else {
                sumaWydatków += t.getKwota();
            }
        }
        
        System.out.println("--------------------------------------------------------");
        System.out.printf("Łącznie przychodów:  +%.2f PLN\n", sumaPrzychodów);
        System.out.printf("Łącznie wydatków:    -%.2f PLN\n", sumaWydatków);
        System.out.printf("Bilans:              %.2f PLN\n", sumaPrzychodów - sumaWydatków);
    }
    
    private static void wyszukajTransakcje() {
        System.out.println("\n════════════ WYSZUKIWANIE TRANSAKCJI ════════════");
        System.out.println("1. 🔍 Po kategorii");
        System.out.println("2. 📅 Po dacie");
        System.out.println("3. 💰 Po kwocie (powyżej wartości)");
        System.out.println("4. 📊 Po typie (przychód/wydatek)");
        System.out.print("Wybierz kryterium (1-4): ");
        
        int kryterium = pobierzWybór(1, 4);
        List<Transakcja> wynik = new ArrayList<>();
        
        switch (kryterium) {
            case 1 -> {
                Kategoria kat = wybierzKategorię();
                if (kat != null) {
                    wynik = menedżer.getListaTransakcji().stream()
                        .filter(t -> t.getKategoria().equals(kat))
                        .collect(Collectors.toList());
                }
            }
            case 2 -> {
                System.out.print("Podaj datę (dd.MM.yyyy): ");
                String dataStr = scanner.nextLine();
                // Uproszczone wyszukiwanie - w rzeczywistości należałoby parsować datę
                wynik = menedżer.getListaTransakcji();
            }
            case 3 -> {
                System.out.print("Podaj minimalną kwotę: ");
                double minKwota = pobierzKwotę();
                wynik = menedżer.getListaTransakcji().stream()
                    .filter(t -> t.getKwota() >= minKwota)
                    .collect(Collectors.toList());
            }
            case 4 -> {
                System.out.println("Wybierz typ:");
                System.out.println("1. Przychód");
                System.out.println("2. Wydatek");
                int typWybór = pobierzWybór(1, 2);
                TypTransakcji typ = (typWybór == 1) ? TypTransakcji.PRZYCHOD : TypTransakcji.WYDATEK;
                wynik = menedżer.getListaTransakcji().stream()
                    .filter(t -> t.getTyp() == typ)
                    .collect(Collectors.toList());
            }
        }
        
        // Wyświetlanie wyników
        if (wynik.isEmpty()) {
            System.out.println("Nie znaleziono transakcji spełniających kryteria.");
        } else {
            System.out.println("\nZnaleziono " + wynik.size() + " transakcji:");
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
            for (Transakcja t : wynik) {
                String typ = (t.getTyp() == TypTransakcji.PRZYCHOD) ? "Przychód" : "Wydatek";
                System.out.printf("ID: %d | %s | %s | %s | %.2f PLN\n",
                    t.getId(), sdf.format(t.getData()), t.getKategoria().getNazwa(), typ, t.getKwota());
            }
        }
    }
    
    private static void edytujTransakcję() {
        wyświetlWszystkieTransakcje();
        List<Transakcja> transakcje = menedżer.getListaTransakcji();
        
        if (transakcje.isEmpty()) {
            System.out.println("Brak transakcji do edycji.");
            return;
        }
        
        System.out.print("\nPodaj ID transakcji do edycji: ");
        int id = pobierzWybór(1, Integer.MAX_VALUE);
        
        Transakcja doEdycji = transakcje.stream()
            .filter(t -> t.getId() == id)
            .findFirst()
            .orElse(null);
        
        if (doEdycji == null) {
            System.out.println("Nie znaleziono transakcji o podanym ID.");
            return;
        }
        
        System.out.println("\nEdytujesz transakcję:");
        System.out.println("1. Kwota: " + doEdycji.getKwota());
        System.out.println("2. Kategoria: " + doEdycji.getKategoria().getNazwa());
        System.out.println("3. Typ: " + (doEdycji.getTyp() == TypTransakcji.PRZYCHOD ? "Przychód" : "Wydatek"));
        System.out.print("Co chcesz edytować? (1-3): ");
        
        int coEdytować = pobierzWybór(1, 3);
        
        switch (coEdytować) {
            case 1 -> {
                System.out.print("Podaj nową kwotę: ");
                double nowaKwota = pobierzKwotę();
                doEdycji.setKwota(nowaKwota);
            }
            case 2 -> {
                Kategoria nowaKat = wybierzKategorię();
                if (nowaKat != null) {
                    doEdycji.setKategoria(nowaKat);
                }
            }
            case 3 -> {
                System.out.println("Wybierz nowy typ:");
                System.out.println("1. Przychód");
                System.out.println("2. Wydatek");
                int typWybór = pobierzWybór(1, 2);
                TypTransakcji nowyTyp = (typWybór == 1) ? TypTransakcji.PRZYCHOD : TypTransakcji.WYDATEK;
                doEdycji.setTyp(nowyTyp);
            }
        }
        
        System.out.println("✅ Transakcja została zaktualizowana!");
    }
    
    private static void usuńTransakcję() {
        wyświetlWszystkieTransakcje();
        List<Transakcja> transakcje = menedżer.getListaTransakcji();
        
        if (transakcje.isEmpty()) {
            System.out.println("Brak transakcji do usunięcia.");
            return;
        }
        
        System.out.print("\nPodaj ID transakcji do usunięcia: ");
        int id = pobierzWybór(1, Integer.MAX_VALUE);
        
        Transakcja doUsunięcia = transakcje.stream()
            .filter(t -> t.getId() == id)
            .findFirst()
            .orElse(null);
        
        if (doUsunięcia == null) {
            System.out.println("Nie znaleziono transakcji o podanym ID.");
            return;
        }
        
        System.out.print("Czy na pewno chcesz usunąć tę transakcję? (T/N): ");
        String potwierdzenie = scanner.nextLine().toUpperCase();
        
        if (potwierdzenie.equals("T")) {
            menedżer.usuńTransakcję(doUsunięcia);
            System.out.println("✅ Transakcja została usunięta!");
        } else {
            System.out.println("Anulowano usuwanie.");
        }
    }
    
    // ========== MENU 2: ZARZĄDZANIE KATEGORIAMI ==========
    private static void zarządzajKategoriami() {
        boolean powrót = false;
        while (!powrót) {
            System.out.println("\n══════════ ZARZĄDZANIE KATEGORIAMI ══════════");
            System.out.println("1. ➕ Dodaj nową kategorię");
            System.out.println("2. 👁️  Wyświetl wszystkie kategorie");
            System.out.println("3. ✏️  Edytuj kategorię");
            System.out.println("4. ❌ Usuń kategorię");
            System.out.println("5. ↩️  Powrót do menu głównego");
            System.out.print("Wybierz opcję (1-5): ");
            
            int wybór = pobierzWybór(1, 5);
            
            switch (wybór) {
                case 1 -> dodajKategorię();
                case 2 -> wyświetlKategorie();
                case 3 -> edytujKategorię();
                case 4 -> usuńKategorię();
                case 5 -> powrót = true;
            }
        }
    }
    
    private static void dodajKategorię() {
        System.out.print("\nPodaj nazwę nowej kategorii: ");
        String nazwa = scanner.nextLine().trim();
        
        if (nazwa.isEmpty()) {
            System.out.println("Nazwa kategorii nie może być pusta!");
            return;
        }
        
        // Sprawdź czy kategoria już istnieje
        boolean istnieje = kategorie.stream()
            .anyMatch(k -> k.getNazwa().equalsIgnoreCase(nazwa));
        
        if (istnieje) {
            System.out.println("Kategoria o tej nazwie już istnieje!");
            return;
        }
        
        Kategoria nowaKategoria = new Kategoria(nazwa);
        kategorie.add(nowaKategoria);
        System.out.println("✅ Kategoria \"" + nazwa + "\" została dodana!");
    }
    
    private static void wyświetlKategorie() {
        System.out.println("\n════════════ LISTA KATEGORII ════════════");
        if (kategorie.isEmpty()) {
            System.out.println("Brak kategorii.");
            return;
        }
        
        for (int i = 0; i < kategorie.size(); i++) {
            Kategoria k = kategorie.get(i);
            long liczbaTransakcji = menedżer.getListaTransakcji().stream()
                .filter(t -> t.getKategoria().equals(k))
                .count();
            
            double sumaWydatków = menedżer.getListaTransakcji().stream()
                .filter(t -> t.getKategoria().equals(k) && t.getTyp() == TypTransakcji.WYDATEK)
                .mapToDouble(Transakcja::getKwota)
                .sum();
            
            System.out.printf("%d. %-15s (transakcji: %d, wydatki: %.2f PLN)\n",
                i + 1, k.getNazwa(), liczbaTransakcji, sumaWydatków);
        }
    }
    
    private static void edytujKategorię() {
        wyświetlKategorie();
        if (kategorie.isEmpty()) return;
        
        System.out.print("\nWybierz numer kategorii do edycji: ");
        int nr = pobierzWybór(1, kategorie.size());
        
        Kategoria kat = kategorie.get(nr - 1);
        System.out.print("Podaj nową nazwę dla kategorii \"" + kat.getNazwa() + "\": ");
        String nowaNazwa = scanner.nextLine().trim();
        
        if (nowaNazwa.isEmpty()) {
            System.out.println("Nazwa nie może być pusta!");
            return;
        }
        
        kat.setNazwa(nowaNazwa);
        System.out.println("✅ Kategoria została zaktualizowana!");
    }
    
    private static void usuńKategorię() {
        wyświetlKategorie();
        if (kategorie.size() <= 1) { // Zawsze zostaw przynajmniej jedną kategorię
            System.out.println("Nie można usunąć wszystkich kategorii!");
            return;
        }
        
        System.out.print("\nWybierz numer kategorii do usunięcia: ");
        int nr = pobierzWybór(1, kategorie.size());
        
        Kategoria kat = kategorie.get(nr - 1);
        
        // Sprawdź czy kategoria jest używana w transakcjach
        boolean używana = menedżer.getListaTransakcji().stream()
            .anyMatch(t -> t.getKategoria().equals(kat));
        
        if (używana) {
            System.out.println("⚠️  Uwaga: Ta kategoria jest używana w transakcjach!");
            System.out.print("Czy na pewno chcesz ją usunąć? (T/N): ");
            String potwierdzenie = scanner.nextLine().toUpperCase();
            
            if (!potwierdzenie.equals("T")) {
                System.out.println("Anulowano usuwanie.");
                return;
            }
        }
        
        kategorie.remove(kat);
        System.out.println("✅ Kategoria \"" + kat.getNazwa() + "\" została usunięta!");
    }
    
    private static Kategoria wybierzKategorię() {
        if (kategorie.isEmpty()) {
            System.out.println("Brak kategorii. Najpierw dodaj kategorie.");
            return null;
        }
        
        System.out.println("\nWybierz kategorię:");
        for (int i = 0; i < kategorie.size(); i++) {
            System.out.println((i + 1) + ". " + kategorie.get(i).getNazwa());
        }
        System.out.print("Wybierz numer (1-" + kategorie.size() + "): ");
        
        int wybór = pobierzWybór(1, kategorie.size());
        return kategorie.get(wybór - 1);
    }
    
    // ========== MENU 3: ZARZĄDZANIE BUDŻETAMI ==========
    private static void zarządzajBudżetami() {
        boolean powrót = false;
        while (!powrót) {
            System.out.println("\n══════════ ZARZĄDZANIE BUDŻETAMI ══════════");
            System.out.println("1. ➕ Dodaj nowy budżet");
            System.out.println("2. 👁️  Wyświetl wszystkie budżety");
            System.out.println("3. 🔍 Sprawdź przekroczenia");
            System.out.println("4. ✏️  Edytuj budżet");
            System.out.println("5. ❌ Usuń budżet");
            System.out.println("6. ↩️  Powrót do menu głównego");
            System.out.print("Wybierz opcję (1-6): ");
            
            int wybór = pobierzWybór(1, 6);
            
            switch (wybór) {
                case 1 -> dodajBudżet();
                case 2 -> wyświetlBudżety();
                case 3 -> sprawdźPrzekroczenia();
                case 4 -> edytujBudżet();
                case 5 -> usuńBudżet();
                case 6 -> powrót = true;
            }
        }
    }
    
    private static void dodajBudżet() {
        System.out.println("\n══════════ DODAWANIE NOWEGO BUDŻETU ══════════");
        
        // Wybór kategorii
        Kategoria kategoria = wybierzKategorię();
        if (kategoria == null) return;
        
        // Sprawdź czy budżet już istnieje
        boolean istnieje = menedżer.getListaBudżetów().stream()
            .anyMatch(b -> b.getKategoria().equals(kategoria));
        
        if (istnieje) {
            System.out.println("⚠️  Budżet dla tej kategorii już istnieje!");
            return;
        }
        
        // Kwota limitu
        System.out.print("Podaj miesięczny limit wydatków: ");
        double limit = pobierzKwotę();
        
        // Okres (np. "2024-05")
        System.out.print("Podaj okres (np. 2024-05): ");
        String okres = scanner.nextLine().trim();
        
        Budżet budżet = new Budżet(kategoria, limit, okres);
        menedżer.dodajBudżet(budżet);
        
        System.out.println("✅ Budżet został dodany!");
        System.out.println("Kategoria: " + kategoria.getNazwa());
        System.out.println("Limit: " + limit + " PLN");
        System.out.println("Okres: " + okres);
    }
    
    private static void wyświetlBudżety() {
        List<Budżet> budżety = menedżer.getListaBudżetów();
        
        System.out.println("\n════════════ LISTA BUDŻETÓW ════════════");
        if (budżety.isEmpty()) {
            System.out.println("Brak zdefiniowanych budżetów.");
            return;
        }
        
        System.out.printf("%-15s %-10s %-15s %-10s %-10s\n", 
            "Kategoria", "Okres", "Limit", "Wydatki", "Status");
        System.out.println("------------------------------------------------------------");
        
        for (Budżet b : budżety) {
            double wydatki = b.getAktualneWydatki();
            double procent = (wydatki / b.getLimit()) * 100;
            String status;
            
            if (procent >= 100) {
                status = "PRZEKROCZONY ⚠️";
            } else if (procent >= 80) {
                status = "BLISKO LIMITU ⚠️";
            } else {
                status = "W NORMIE ✅";
            }
            
            System.out.printf("%-15s %-10s %-10.2f PLN %-10.2f PLN %-15s\n",
                b.getKategoria().getNazwa(),
                b.getOkres(),
                b.getLimit(),
                wydatki,
                status);
        }
    }
    
    private static void sprawdźPrzekroczenia() {
        List<Budżet> budżety = menedżer.getListaBudżetów();
        
        System.out.println("\n══════════ SPRAWDZENIE PRZEKROCZEŃ ══════════");
        
        boolean znalezionoPrzekroczenia = false;
        for (Budżet b : budżety) {
            if (b.czyPrzekroczony()) {
                znalezionoPrzekroczenia = true;
                System.out.println("🚨 PRZEKROCZENIE BUDŻETU:");
                System.out.println("   Kategoria: " + b.getKategoria().getNazwa());
                System.out.println("   Okres: " + b.getOkres());
                System.out.println("   Limit: " + b.getLimit() + " PLN");
                System.out.println("   Wydatki: " + b.getAktualneWydatki() + " PLN");
                System.out.println("   Przekroczenie: " + (b.getAktualneWydatki() - b.getLimit()) + " PLN");
                System.out.println();
            }
        }
        
        if (!znalezionoPrzekroczenia) {
            System.out.println("✅ Żaden budżet nie został przekroczony!");
        }
    }
    
    private static void edytujBudżet() {
        wyświetlBudżety();
        List<Budżet> budżety = menedżer.getListaBudżetów();
        
        if (budżety.isEmpty()) {
            System.out.println("Brak budżetów do edycji.");
            return;
        }
        
        System.out.print("\nWybierz numer budżetu do edycji (według kolejności na liście): ");
        int nr = pobierzWybór(1, budżety.size());
        
        Budżet budżet = budżety.get(nr - 1);
        
        System.out.println("Edytujesz budżet dla kategorii: " + budżet.getKategoria().getNazwa());
        System.out.println("1. Limit: " + budżet.getLimit() + " PLN");
        System.out.println("2. Okres: " + budżet.getOkres());
        System.out.print("Co chcesz edytować? (1-2): ");
        
        int coEdytować = pobierzWybór(1, 2);
        
        switch (coEdytować) {
            case 1 -> {
                System.out.print("Podaj nowy limit: ");
                double nowyLimit = pobierzKwotę();
                budżet.setLimit(nowyLimit);
                System.out.println("✅ Limit został zaktualizowany!");
            }
            case 2 -> {
                System.out.print("Podaj nowy okres (np. 2024-06): ");
                String nowyOkres = scanner.nextLine().trim();
                budżet.setOkres(nowyOkres);
                System.out.println("✅ Okres został zaktualizowany!");
            }
        }
    }
    
    private static void usuńBudżet() {
        wyświetlBudżety();
        List<Budżet> budżety = menedżer.getListaBudżetów();
        
        if (budżety.isEmpty()) {
            System.out.println("Brak budżetów do usunięcia.");
            return;
        }
        
        System.out.print("\nWybierz numer budżetu do usunięcia: ");
        int nr = pobierzWybór(1, budżety.size());
        
        Budżet budżet = budżety.get(nr - 1);
        
        System.out.print("Czy na pewno chcesz usunąć budżet dla kategorii \"" + 
            budżet.getKategoria().getNazwa() + "\"? (T/N): ");
        String potwierdzenie = scanner.nextLine().toUpperCase();
        
        if (potwierdzenie.equals("T")) {
            menedżer.usuńBudżet(budżet);
            System.out.println("✅ Budżet został usunięty!");
        } else {
            System.out.println("Anulowano usuwanie.");
        }
    }
    
    // ========== MENU 4: STATYSTYKI ==========
    private static void wyświetlStatystyki() {
        System.out.println("\n════════════ STATYSTYKI FINANSOWE ════════════");
        
        List<Transakcja> transakcje = menedżer.getListaTransakcji();
        
        if (transakcje.isEmpty()) {
            System.out.println("Brak danych do wyświetlenia.");
            return;
        }
        
        // Oblicz sumy za pomocą strumieni (streams) zamiast pętli for
        double sumaPrzychodów = transakcje.stream()
            .filter(t -> t.getTyp() == TypTransakcji.PRZYCHOD)
            .mapToDouble(t -> t.getKwota())
            .sum();
        
        double sumaWydatków = transakcje.stream()
            .filter(t -> t.getTyp() == TypTransakcji.WYDATEK)
            .mapToDouble(t -> t.getKwota())
            .sum();
        
        // Użyj strumienia do zgrupowania wydatków według kategorii
        Map<Kategoria, Double> wydatkiPoKategoriach = transakcje.stream()
            .filter(t -> t.getTyp() == TypTransakcji.WYDATEK)
            .collect(Collectors.groupingBy(
                t -> t.getKategoria(),
                Collectors.summingDouble(t -> t.getKwota())
            ));
        
        double bilans = sumaPrzychodów - sumaWydatków;
        
        System.out.println("📊 PODSUMOWANIE:");
        System.out.printf("Łączna liczba transakcji: %d\n", transakcje.size());
        System.out.printf("Przychody:  +%.2f PLN\n", sumaPrzychodów);
        System.out.printf("Wydatki:    -%.2f PLN\n", sumaWydatków);
        System.out.printf("Bilans:     %.2f PLN\n", bilans);
        System.out.printf("Procent oszczędności: %.1f%%\n", 
            (sumaPrzychodów > 0 ? (bilans / sumaPrzychodów * 100) : 0));
        
        System.out.println("\n📈 WYDATKI WEDŁUG KATEGORII:");
        if (wydatkiPoKategoriach.isEmpty()) {
            System.out.println("Brak wydatków.");
        } else {
            // Użyj finalnej kopii zmiennej dla lambdy
            final double sumaWydatkówFinal = sumaWydatków;
            wydatkiPoKategoriach.entrySet().stream()
                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                .forEach(entry -> {
                    double procent = (entry.getValue() / sumaWydatkówFinal) * 100;
                    System.out.printf("  %-15s: %8.2f PLN (%5.1f%%)\n",
                        entry.getKey().getNazwa(), entry.getValue(), procent);
                });
        }
        
        System.out.println("\n💰 PODSUMOWANIE BUDŻETÓW:");
        List<Budżet> budżety = menedżer.getListaBudżetów();
        if (budżety.isEmpty()) {
            System.out.println("Brak zdefiniowanych budżetów.");
        } else {
            // Użyj strumienia do zliczenia przekroczonych budżetów
            long przekroczone = budżety.stream()
                .filter(b -> b.czyPrzekroczony())
                .count();
            
            System.out.printf("Zdefiniowanych budżetów: %d\n", budżety.size());
            System.out.printf("Przekroczonych budżetów: %d\n", przekroczone);
            System.out.printf("Budżety w normie: %d\n", budżety.size() - przekroczone);
        }
    }
    
    // ========== MENU 5: RAPORTY ==========
    private static void generujRaporty() {
        boolean powrót = false;
        while (!powrót) {
            System.out.println("\n══════════ GENEROWANIE RAPORTÓW ══════════");
            System.out.println("1. 📊 Raport miesięczny");
            System.out.println("2. 📈 Raport kategorii");
            System.out.println("3. 📋 Lista wszystkich raportów");
            System.out.println("4. ↩️  Powrót do menu głównego");
            System.out.print("Wybierz opcję (1-4): ");
            
            int wybór = pobierzWybór(1, 4);
            
            switch (wybór) {
                case 1 -> {
                    Raport raport = fabrykaRaportów.utwórzRaport("miesięczny");
                    raport.generuj();
                }
                case 2 -> {
                    Kategoria kat = wybierzKategorię();
                    if (kat != null) {
                        Raport raport = fabrykaRaportów.utwórzRaport("kategorii", kat.getNazwa());
                        raport.generuj();
                    }
                }
                case 3 -> listaRaportów();
                case 4 -> powrót = true;
            }
        }
    }
    
    private static void listaRaportów() {
        System.out.println("\n══════════ DOSTĘPNE RAPORTY ══════════");
        System.out.println("1. Raport miesięczny - podsumowanie wszystkich transakcji");
        System.out.println("2. Raport kategorii - szczegółowe dane dla wybranej kategorii");
        System.out.println("3. Raport budżetów - stan wszystkich budżetów");
        System.out.println("4. Raport przekroczeń - lista przekroczonych budżetów");
        System.out.print("\nWybierz raport do wygenerowania (1-4): ");
        
        int wybór = pobierzWybór(1, 4);
        
        switch (wybór) {
            case 1 -> fabrykaRaportów.utwórzRaport("miesięczny").generuj();
            case 2 -> {
                Kategoria kat = wybierzKategorię();
                if (kat != null) {
                    fabrykaRaportów.utwórzRaport("kategorii", kat.getNazwa()).generuj();
                }
            }
            case 3 -> {
                System.out.println("\n══════════ RAPORT BUDŻETÓW ══════════");
                wyświetlBudżety();
            }
            case 4 -> {
                System.out.println("\n══════════ RAPORT PRZEKROCZEŃ ══════════");
                sprawdźPrzekroczenia();
            }
        }
    }
    
    // ========== MENU 6: PROGNOZY ==========
    private static void wykonajPrognozy() {
        System.out.println("\n══════════ PROGNOZOWANIE WYDATKÓW ══════════");
        System.out.println("Wybierz algorytm prognozowania:");
        System.out.println("1. 📊 Średnia prosta (średnia z wszystkich wydatków)");
        System.out.println("2. 📈 Prognoza trendowa (uwzględnia trendy)");
        System.out.println("3. 🔄 Porównaj oba algorytmy");
        System.out.println("4. ↩️  Powrót do menu głównego");
        System.out.print("Wybierz opcję (1-4): ");
        
        int wybór = pobierzWybór(1, 4);
        
        if (wybór == 4) return;
        
        StrategiaPrognozy strategia1 = new ŚredniaProstaPrognoza();
        StrategiaPrognozy strategia2 = new PrognozaTrendowa();
        
        switch (wybór) {
            case 1 -> {
                double prognoza = strategia1.prognozuj(menedżer.getListaTransakcji());
                System.out.println("\n══════════ PROGNOZA - ŚREDNIA PROSTA ══════════");
                System.out.printf("Prognozowany średni wydatek: %.2f PLN\n", prognoza);
                System.out.printf("Prognozowane wydatki miesięczne: %.2f PLN\n", prognoza * 30);
            }
            case 2 -> {
                double prognoza = strategia2.prognozuj(menedżer.getListaTransakcji());
                System.out.println("\n══════════ PROGNOZA - TRENDOWA ══════════");
                System.out.printf("Prognozowany następny wydatek: %.2f PLN\n", prognoza);
                System.out.printf("Prognozowane wydatki miesięczne: %.2f PLN\n", prognoza * 15);
            }
            case 3 -> {
                double prognoza1 = strategia1.prognozuj(menedżer.getListaTransakcji());
                double prognoza2 = strategia2.prognozuj(menedżer.getListaTransakcji());
                
                System.out.println("\n══════════ PORÓWNANIE PROGNOZ ══════════");
                System.out.println("Algorytm                 | Prognoza       | Miesięcznie");
                System.out.println("-------------------------|----------------|------------");
                System.out.printf("%-24s| %12.2f PLN | %10.2f PLN\n",
                    strategia1.getNazwaStrategii(), prognoza1, prognoza1 * 30);
                System.out.printf("%-24s| %12.2f PLN | %10.2f PLN\n",
                    strategia2.getNazwaStrategii(), prognoza2, prognoza2 * 15);
                
                System.out.println("\n💡 Interpretacja:");
                if (prognoza2 > prognoza1) {
                    System.out.println("Algorytm trendowy wykrył wzrost wydatków.");
                } else if (prognoza2 < prognoza1) {
                    System.out.println("Algorytm trendowy wykrył spadek wydatków.");
                } else {
                    System.out.println("Brak wyraźnego trendu w wydatkach.");
                }
            }
        }
    }
    
    // ========== MENU 7: OBSERWATORZY ==========
    private static void zarządzajObserwatorami() {
        System.out.println("\n══════════ ZARZĄDZANIE OBSERWATORAMI ══════════");
        System.out.println("1. 👁️  Wyświetl aktywnych obserwatorów");
        System.out.println("2. ➕ Dodaj nowy typ obserwatora");
        System.out.println("3. 🔔 Testuj powiadomienia");
        System.out.println("4. ↩️  Powrót do menu głównego");
        System.out.print("Wybierz opcję (1-4): ");
        
        int wybór = pobierzWybór(1, 4);
        
        switch (wybór) {
            case 1 -> {
                System.out.println("\nAktywni obserwatorzy:");
                // W obecnej wersji mamy tylko jeden stały obserwator
                System.out.println("1. UsługaPowiadomień - powiadamia o przekroczeniu budżetu");
            }
            case 2 -> {
                System.out.println("\nDostępne typy obserwatorów:");
                System.out.println("1. Powiadomienia konsolowe (już dodane)");
                System.out.println("2. Powiadomienia plikowe (w przyszłości)");
                System.out.println("3. Powiadomienia email (w przyszłości)");
                System.out.println("\nFunkcja w trakcie rozwoju...");
            }
            case 3 -> {
                System.out.println("\n══════════ TEST POWIADOMIEŃ ══════════");
                // Sprawdzamy przekroczenia, które automatycznie wywołają powiadomienia
                sprawdźPrzekroczenia();
                System.out.println("Jeśli są przekroczone budżety, powiadomienia zostały wysłane.");
            }
        }
    }
    
    // ========== MENU 8: EKSPORT DANYCH ==========
    private static void eksportujDane() {
        System.out.println("\n══════════ EKSPORT DANYCH ══════════");
        System.out.println("1. 📄 Eksport transakcji do CSV");
        System.out.println("2. 📊 Eksport raportu do pliku tekstowego");
        System.out.println("3. 💾 Zapisz stan aplikacji");
        System.out.println("4. ↩️  Powrót do menu głównego");
        System.out.print("Wybierz opcję (1-4): ");
        
        int wybór = pobierzWybór(1, 4);
        
        switch (wybór) {
            case 1 -> eksportujDoCSV();
            case 2 -> eksportujRaport();
            case 3 -> zapiszStan();
            case 4 -> { return; }
        }
    }
    
    private static void eksportujDoCSV() {
        System.out.println("\n══════════ EKSPORT DO CSV ══════════");
        System.out.println("Funkcja eksportu do CSV - symulacja");
        
        List<Transakcja> transakcje = menedżer.getListaTransakcji();
        if (transakcje.isEmpty()) {
            System.out.println("Brak transakcji do eksportu.");
            return;
        }
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        
        System.out.println("ID;Data;Kategoria;Typ;Kwota");
        for (Transakcja t : transakcje) {
            String typ = (t.getTyp() == TypTransakcji.PRZYCHOD) ? "Przychód" : "Wydatek";
            System.out.printf("%d;%s;%s;%s;%.2f\n",
                t.getId(),
                sdf.format(t.getData()),
                t.getKategoria().getNazwa(),
                typ,
                t.getKwota());
        }
        
        System.out.println("\n✅ Dane zostały przygotowane do eksportu.");
        System.out.println("Aby zapisać do pliku, przekopiuj powyższe dane.");
    }
    
    private static void eksportujRaport() {
        System.out.println("\n══════════ EKSPORT RAPORTU ══════════");
        System.out.println("1. Raport miesięczny");
        System.out.println("2. Raport kategorii");
        System.out.print("Wybierz typ raportu (1-2): ");
        
        int typ = pobierzWybór(1, 2);
        
        if (typ == 1) {
            System.out.println("\n=== RAPORT MIESIĘCZNY ===");
            fabrykaRaportów.utwórzRaport("miesięczny").generuj();
        } else {
            Kategoria kat = wybierzKategorię();
            if (kat != null) {
                System.out.println("\n=== RAPORT KATEGORII: " + kat.getNazwa() + " ===");
                fabrykaRaportów.utwórzRaport("kategorii", kat.getNazwa()).generuj();
            }
        }
        
        System.out.println("\n✅ Raport został wygenerowany. Aby zapisać, przekopiuj powyższe dane.");
    }
    
    private static void zapiszStan() {
        System.out.println("\n══════════ ZAPISYWANIE STANU ══════════");
        System.out.println("Liczba transakcji: " + menedżer.getListaTransakcji().size());
        System.out.println("Liczba budżetów: " + menedżer.getListaBudżetów().size());
        System.out.println("Liczba kategorii: " + kategorie.size());
        
        System.out.println("\n✅ Stan aplikacji został zachowany w pamięci.");
        System.out.println("Uwaga: W obecnej wersji dane są przechowywane tylko w pamięci.");
        System.out.println("Po zamknięciu aplikacji dane zostaną utracone.");
        System.out.println("W przyszłości planowana jest obsługa zapisu do pliku.");
    }
}