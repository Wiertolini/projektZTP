package budzet;

import budzet.obserwatorzy.*;
import budzet.prognozy.*;
import budzet.raporty.*;
import budzet.rdzen.*;
import java.text.SimpleDateFormat;
import java.util.*;
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
        kategorie.add(new Kategoria("Jedzenie"));
        kategorie.add(new Kategoria("Transport"));
        kategorie.add(new Kategoria("Rozrywka"));
        kategorie.add(new Kategoria("Rachunki"));
        kategorie.add(new Kategoria("Zdrowie"));
        kategorie.add(new Kategoria("Ubrania"));
        kategorie.add(new Kategoria("Inne"));
        
        menedżer.dodajObserwatora(new UsługaPowiadomień());
        
        if (menedżer.getListaTransakcji().isEmpty()) {
            utwórzPrzykładoweDane();
        }
    }
    
    private static void utwórzPrzykładoweDane() {
        Random rand = new Random();
        Date data = new Date();
        
        for (int i = 1; i <= 15; i++) {
            Kategoria kat = kategorie.get(rand.nextInt(kategorie.size()));
            TypTransakcji typ = (i % 5 == 0) ? TypTransakcji.PRZYCHOD : TypTransakcji.WYDATEK;
            double kwota = typ == TypTransakcji.PRZYCHOD ? 
                rand.nextDouble() * 3000 + 2000 : rand.nextDouble() * 400 + 50;
            
            menedżer.dodajTransakcję(new Transakcja(i, data, kwota, kat, typ));
        }
        
        menedżer.dodajBudżet(new Budżet(kategorie.get(0), 800.0, "2024-05"));
        menedżer.dodajBudżet(new Budżet(kategorie.get(1), 300.0, "2024-05"));
        menedżer.dodajBudżet(new Budżet(kategorie.get(2), 500.0, "2024-05"));
    }
    
    private static void wyświetlNagłówek() {
        System.out.println("╔══════════════════════════════════════════════════════╗");
        System.out.println("║            APLIKACJA BUDŻET DOMOWY                  ║");
        System.out.println("║            v1.0 - Interfejs Konsolowy               ║");
        System.out.println("╚══════════════════════════════════════════════════════╝\n");
    }
    
    private static void wyświetlMenuGłówne() {
        System.out.println("""
            \n════════════════════ MENU GŁÓWNE ════════════════════
            1. Zarządzaj transakcjami
            2. Zarządzaj kategoriami
            3. Zarządzaj budżetami
            4. Wyświetl statystyki
            5. Generuj raporty
            6. Wykonaj prognozy
            7. Zarządzaj obserwatorami
            8. Eksportuj dane
            9. Wyjście
            ═══════════════════════════════════════════════════════""");
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
        while (true) {
            try {
                System.out.print("Podaj kwotę: ");
                double kwota = Double.parseDouble(scanner.nextLine());
                if (kwota > 0) return kwota;
                System.out.println("Kwota musi być większa od 0!");
            } catch (NumberFormatException e) {
                System.out.println("Niepoprawna kwota!");
            }
        }
    }
    
    // ========== MENU 1: ZARZĄDZANIE TRANSAKCJAMI ==========
    private static void zarządzajTransakcjami() {
        while (true) {
            System.out.println("""
                \n══════════ ZARZĄDZANIE TRANSAKCJAMI ══════════
                1. Dodaj nową transakcję
                2. Wyświetl wszystkie transakcje
                3. Wyszukaj transakcje
                4. Edytuj transakcję
                5. Usuń transakcję
                6. Powrót do menu głównego""");
            System.out.print("Wybierz opcję (1-6): ");
            
            int wybór = pobierzWybór(1, 6);
            if (wybór == 6) break;
            
            switch (wybór) {
                case 1 -> dodajTransakcję();
                case 2 -> wyświetlWszystkieTransakcje();
                case 3 -> wyszukajTransakcje();
                case 4 -> edytujTransakcję();
                case 5 -> usuńTransakcję();
            }
        }
    }
    
    private static void dodajTransakcję() {
        System.out.println("\n══════════ DODAWANIE NOWEJ TRANSAKCJI ══════════");
        
        System.out.println("Wybierz typ transakcji:");
        System.out.println("1. Przychód");
        System.out.println("2. Wydatek");
        System.out.print("Wybierz (1-2): ");
        TypTransakcji typ = pobierzWybór(1, 2) == 1 ? TypTransakcji.PRZYCHOD : TypTransakcji.WYDATEK;
        
        double kwota = pobierzKwotę();
        Kategoria kategoria = wybierzKategorię();
        if (kategoria == null) return;
        
        int noweId = menedżer.getListaTransakcji().size() + 1;
        Transakcja transakcja = new Transakcja(noweId, new Date(), kwota, kategoria, typ);
        menedżer.dodajTransakcję(transakcja);
        
        System.out.println("Transakcja została dodana pomyślnie!");
        
        menedżer.getListaBudżetów().stream()
            .filter(b -> b.getKategoria().equals(kategoria) && b.czyPrzekroczony())
            .forEach(b -> System.out.println("Uwaga: Przekroczono budżet dla kategorii: " + kategoria.getNazwa()));
    }
    
    private static void wyświetlWszystkieTransakcje() {
        List<Transakcja> transakcje = menedżer.getListaTransakcji();
        if (transakcje.isEmpty()) {
            System.out.println("Brak transakcji.");
            return;
        }
        
        System.out.println("\n════════════ LISTA WSZYSTKICH TRANSAKCJI ════════════");
        System.out.printf("%-4s %-12s %-15s %-10s %-10s\n", "ID", "Data", "Kategoria", "Typ", "Kwota");
        System.out.println("--------------------------------------------------------");
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        double sumaPrzychodów = 0, sumaWydatków = 0;
        
        for (Transakcja t : transakcje) {
            String typStr = t.getTyp() == TypTransakcji.PRZYCHOD ? "Przychód" : "Wydatek";
            String znak = t.getTyp() == TypTransakcji.PRZYCHOD ? "+" : "-";
            System.out.printf("%-4d %-12s %-15s %-10s %s%.2f PLN\n",
                t.getId(), sdf.format(t.getData()), t.getKategoria().getNazwa(), typStr, znak, t.getKwota());
            
            if (t.getTyp() == TypTransakcji.PRZYCHOD) sumaPrzychodów += t.getKwota();
            else sumaWydatków += t.getKwota();
        }
        
        System.out.println("--------------------------------------------------------");
        System.out.printf("Łącznie przychodów:  +%.2f PLN\n", sumaPrzychodów);
        System.out.printf("Łącznie wydatków:    -%.2f PLN\n", sumaWydatków);
        System.out.printf("Bilans:              %.2f PLN\n", sumaPrzychodów - sumaWydatków);
    }
    
    private static void wyszukajTransakcje() {
        System.out.println("""
            \n════════════ WYSZUKIWANIE TRANSAKCJI ════════════
            1. Po kategorii
            2. Po dacie
            3. Po kwocie (powyżej wartości)
            4. Po typie (przychód/wydatek)""");
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
                scanner.nextLine();
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
                System.out.println("Wybierz typ:\n1. Przychód\n2. Wydatek");
                TypTransakcji typ = pobierzWybór(1, 2) == 1 ? TypTransakcji.PRZYCHOD : TypTransakcji.WYDATEK;
                wynik = menedżer.getListaTransakcji().stream()
                    .filter(t -> t.getTyp() == typ)
                    .collect(Collectors.toList());
            }
        }
        
        if (wynik.isEmpty()) {
            System.out.println("Nie znaleziono transakcji spełniających kryteria.");
        } else {
            System.out.println("\nZnaleziono " + wynik.size() + " transakcji:");
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
            wynik.forEach(t -> System.out.printf("ID: %d | %s | %s | %s | %.2f PLN\n",
                t.getId(), sdf.format(t.getData()), t.getKategoria().getNazwa(),
                t.getTyp() == TypTransakcji.PRZYCHOD ? "Przychód" : "Wydatek", t.getKwota()));
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
            .findFirst().orElse(null);
        
        if (doEdycji == null) {
            System.out.println("Nie znaleziono transakcji o podanym ID.");
            return;
        }
        
        System.out.println("\nEdytujesz transakcję:");
        System.out.println("1. Kwota: " + doEdycji.getKwota());
        System.out.println("2. Kategoria: " + doEdycji.getKategoria().getNazwa());
        System.out.println("3. Typ: " + (doEdycji.getTyp() == TypTransakcji.PRZYCHOD ? "Przychód" : "Wydatek"));
        System.out.print("Co chcesz edytować? (1-3): ");
        
        switch (pobierzWybór(1, 3)) {
            case 1 -> {
                System.out.print("Podaj nową kwotę: ");
                doEdycji.setKwota(pobierzKwotę());
            }
            case 2 -> {
                Kategoria nowaKat = wybierzKategorię();
                if (nowaKat != null) doEdycji.setKategoria(nowaKat);
            }
            case 3 -> {
                System.out.println("Wybierz nowy typ:\n1. Przychód\n2. Wydatek");
                doEdycji.setTyp(pobierzWybór(1, 2) == 1 ? TypTransakcji.PRZYCHOD : TypTransakcji.WYDATEK);
            }
        }
        System.out.println("Transakcja została zaktualizowana!");
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
            .findFirst().orElse(null);
        
        if (doUsunięcia == null) {
            System.out.println("Nie znaleziono transakcji o podanym ID.");
            return;
        }
        
        System.out.print("Czy na pewno chcesz usunąć tę transakcję? (T/N): ");
        if (scanner.nextLine().equalsIgnoreCase("T")) {
            menedżer.usuńTransakcję(doUsunięcia);
            System.out.println("Transakcja została usunięta!");
        } else {
            System.out.println("Anulowano usuwanie.");
        }
    }
    
    // ========== MENU 2: ZARZĄDZANIE KATEGORIAMI ==========
    private static void zarządzajKategoriami() {
        while (true) {
            System.out.println("""
                \n══════════ ZARZĄDZANIE KATEGORIAMI ══════════
                1. Dodaj nową kategorię
                2. Wyświetl wszystkie kategorie
                3. Edytuj kategorię
                4. Usuń kategorię
                5. Powrót do menu głównego""");
            System.out.print("Wybierz opcję (1-5): ");
            
            int wybór = pobierzWybór(1, 5);
            if (wybór == 5) break;
            
            switch (wybór) {
                case 1 -> dodajKategorię();
                case 2 -> wyświetlKategorie();
                case 3 -> edytujKategorię();
                case 4 -> usuńKategorię();
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
        
        if (kategorie.stream().anyMatch(k -> k.getNazwa().equalsIgnoreCase(nazwa))) {
            System.out.println("Kategoria o tej nazwie już istnieje!");
            return;
        }
        
        kategorie.add(new Kategoria(nazwa));
        System.out.println("Kategoria \"" + nazwa + "\" została dodana!");
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
                .filter(t -> t.getKategoria().equals(k)).count();
            
            double sumaWydatków = menedżer.getListaTransakcji().stream()
                .filter(t -> t.getKategoria().equals(k) && t.getTyp() == TypTransakcji.WYDATEK)
                .mapToDouble(Transakcja::getKwota).sum();
            
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
        System.out.println("Kategoria została zaktualizowana!");
    }
    
    private static void usuńKategorię() {
        wyświetlKategorie();
        if (kategorie.size() <= 1) {
            System.out.println("Nie można usunąć wszystkich kategorii!");
            return;
        }
        
        System.out.print("\nWybierz numer kategorii do usunięcia: ");
        int nr = pobierzWybór(1, kategorie.size());
        Kategoria kat = kategorie.get(nr - 1);
        
        boolean używana = menedżer.getListaTransakcji().stream()
            .anyMatch(t -> t.getKategoria().equals(kat));
        
        if (używana) {
            System.out.print("Uwaga: Ta kategoria jest używana w transakcjach! Czy na pewno chcesz ją usunąć? (T/N): ");
            if (!scanner.nextLine().equalsIgnoreCase("T")) {
                System.out.println("Anulowano usuwanie.");
                return;
            }
        }
        
        kategorie.remove(kat);
        System.out.println("Kategoria \"" + kat.getNazwa() + "\" została usunięta!");
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
        return kategorie.get(pobierzWybór(1, kategorie.size()) - 1);
    }
    
    // ========== MENU 3: ZARZĄDZANIE BUDŻETAMI ==========
    private static void zarządzajBudżetami() {
        while (true) {
            System.out.println("""
                \n══════════ ZARZĄDZANIE BUDŻETAMI ══════════
                1. Dodaj nowy budżet
                2. Wyświetl wszystkie budżety
                3. Sprawdź przekroczenia
                4. Edytuj budżet
                5. Usuń budżet
                6. Powrót do menu głównego""");
            System.out.print("Wybierz opcję (1-6): ");
            
            int wybór = pobierzWybór(1, 6);
            if (wybór == 6) break;
            
            switch (wybór) {
                case 1 -> dodajBudżet();
                case 2 -> wyświetlBudżety();
                case 3 -> sprawdźPrzekroczenia();
                case 4 -> edytujBudżet();
                case 5 -> usuńBudżet();
            }
        }
    }
    
    private static void dodajBudżet() {
        System.out.println("\n══════════ DODAWANIE NOWEGO BUDŻETU ══════════");
        Kategoria kategoria = wybierzKategorię();
        if (kategoria == null) return;
        
        if (menedżer.getListaBudżetów().stream().anyMatch(b -> b.getKategoria().equals(kategoria))) {
            System.out.println("Budżet dla tej kategorii już istnieje!");
            return;
        }
        
        System.out.print("Podaj miesięczny limit wydatków: ");
        double limit = pobierzKwotę();
        System.out.print("Podaj okres (np. 2024-05): ");
        String okres = scanner.nextLine().trim();
        
        menedżer.dodajBudżet(new Budżet(kategoria, limit, okres));
        System.out.println("Budżet został dodany!");
        System.out.println("Kategoria: " + kategoria.getNazwa() + "\nLimit: " + limit + " PLN\nOkres: " + okres);
    }
    
    private static void wyświetlBudżety() {
        List<Budżet> budżety = menedżer.getListaBudżetów();
        System.out.println("\n════════════ LISTA BUDŻETÓW ════════════");
        if (budżety.isEmpty()) {
            System.out.println("Brak zdefiniowanych budżetów.");
            return;
        }
        
        System.out.printf("%-15s %-10s %-15s %-10s %-10s\n", "Kategoria", "Okres", "Limit", "Wydatki", "Status");
        System.out.println("------------------------------------------------------------");
        
        budżety.forEach(b -> {
            double wydatki = b.getAktualneWydatki();
            double procent = (wydatki / b.getLimit()) * 100;
            String status = procent >= 100 ? "PRZEKROCZONY" : procent >= 80 ? "BLISKO LIMITU" : "W NORMIE";
            System.out.printf("%-15s %-10s %-10.2f PLN %-10.2f PLN %-15s\n",
                b.getKategoria().getNazwa(), b.getOkres(), b.getLimit(), wydatki, status);
        });
    }
    
    private static void sprawdźPrzekroczenia() {
        System.out.println("\n══════════ SPRAWDZENIE PRZEKROCZEŃ ══════════");
        boolean znaleziono = false;
        
        for (Budżet b : menedżer.getListaBudżetów()) {
            if (b.czyPrzekroczony()) {
                znaleziono = true;
                System.out.println("PRZEKROCZENIE BUDŻETU:");
                System.out.println("   Kategoria: " + b.getKategoria().getNazwa());
                System.out.println("   Okres: " + b.getOkres());
                System.out.println("   Limit: " + b.getLimit() + " PLN");
                System.out.println("   Wydatki: " + b.getAktualneWydatki() + " PLN");
                System.out.println("   Przekroczenie: " + (b.getAktualneWydatki() - b.getLimit()) + " PLN\n");
            }
        }
        
        if (!znaleziono) System.out.println("Żaden budżet nie został przekroczony!");
    }
    
    private static void edytujBudżet() {
        wyświetlBudżety();
        List<Budżet> budżety = menedżer.getListaBudżetów();
        if (budżety.isEmpty()) {
            System.out.println("Brak budżetów do edycji.");
            return;
        }
        
        System.out.print("\nWybierz numer budżetu do edycji: ");
        int nr = pobierzWybór(1, budżety.size());
        Budżet budżet = budżety.get(nr - 1);
        
        System.out.println("Edytujesz budżet dla kategorii: " + budżet.getKategoria().getNazwa());
        System.out.println("1. Limit: " + budżet.getLimit() + " PLN");
        System.out.println("2. Okres: " + budżet.getOkres());
        System.out.print("Co chcesz edytować? (1-2): ");
        
        if (pobierzWybór(1, 2) == 1) {
            System.out.print("Podaj nowy limit: ");
            budżet.setLimit(pobierzKwotę());
            System.out.println("Limit został zaktualizowany!");
        } else {
            System.out.print("Podaj nowy okres (np. 2024-06): ");
            budżet.setOkres(scanner.nextLine().trim());
            System.out.println("Okres został zaktualizowany!");
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
        
        System.out.print("Czy na pewno chcesz usunąć budżet dla kategorii \"" + budżet.getKategoria().getNazwa() + "\"? (T/N): ");
        if (scanner.nextLine().equalsIgnoreCase("T")) {
            menedżer.usuńBudżet(budżet);
            System.out.println("Budżet został usunięty!");
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
        
        double sumaPrzychodów = transakcje.stream()
            .filter(t -> t.getTyp() == TypTransakcji.PRZYCHOD)
            .mapToDouble(t -> t.getKwota()).sum();
        double sumaWydatków = transakcje.stream()
            .filter(t -> t.getTyp() == TypTransakcji.WYDATEK)
            .mapToDouble(t -> t.getKwota()).sum();
        double bilans = sumaPrzychodów - sumaWydatków;
        
        Map<Kategoria, Double> wydatkiPoKategoriach = transakcje.stream()
            .filter(t -> t.getTyp() == TypTransakcji.WYDATEK)
            .collect(Collectors.groupingBy(t -> t.getKategoria(), Collectors.summingDouble(t -> t.getKwota())));
        
        System.out.println("PODSUMOWANIE:");
        System.out.printf("Łączna liczba transakcji: %d\n", transakcje.size());
        System.out.printf("Przychody:  +%.2f PLN\n", sumaPrzychodów);
        System.out.printf("Wydatki:    -%.2f PLN\n", sumaWydatków);
        System.out.printf("Bilans:     %.2f PLN\n", bilans);
        System.out.printf("Procent oszczędności: %.1f%%\n", sumaPrzychodów > 0 ? (bilans / sumaPrzychodów * 100) : 0);
        
        System.out.println("\nWYDATKI WEDŁUG KATEGORII:");
        if (wydatkiPoKategoriach.isEmpty()) {
            System.out.println("Brak wydatków.");
        } else {
            final double sumaWydatkówFinal = sumaWydatków;
            wydatkiPoKategoriach.entrySet().stream()
                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                .forEach(entry -> System.out.printf("  %-15s: %8.2f PLN (%5.1f%%)\n",
                    entry.getKey().getNazwa(), entry.getValue(), (entry.getValue() / sumaWydatkówFinal) * 100));
        }
        
        System.out.println("\nPODSUMOWANIE BUDŻETÓW:");
        List<Budżet> budżety = menedżer.getListaBudżetów();
        if (budżety.isEmpty()) {
            System.out.println("Brak zdefiniowanych budżetów.");
        } else {
            long przekroczone = budżety.stream().filter(b -> b.czyPrzekroczony()).count();
            System.out.printf("Zdefiniowanych budżetów: %d\n", budżety.size());
            System.out.printf("Przekroczonych budżetów: %d\n", przekroczone);
            System.out.printf("Budżety w normie: %d\n", budżety.size() - przekroczone);
        }
    }
    
    // ========== MENU 5-8: POZOSTAŁE FUNKCJE (SKRÓCONE) ==========
    private static void generujRaporty() {
        while (true) {
            System.out.println("""
                \n══════════ GENEROWANIE RAPORTÓW ══════════
                1. Raport miesięczny
                2. Raport kategorii
                3. Lista wszystkich raportów
                4. Powrót do menu głównego""");
            System.out.print("Wybierz opcję (1-4): ");
            
            int wybór = pobierzWybór(1, 4);
            if (wybór == 4) break;
            
            switch (wybór) {
                case 1 -> fabrykaRaportów.utwórzRaport("miesięczny").generuj();
                case 2 -> {
                    Kategoria kat = wybierzKategorię();
                    if (kat != null) fabrykaRaportów.utwórzRaport("kategorii", kat.getNazwa()).generuj();
                }
                case 3 -> {
                    System.out.println("""
                        \n══════════ DOSTĘPNE RAPORTY ══════════
                        1. Raport miesięczny
                        2. Raport kategorii
                        3. Raport budżetów
                        4. Raport przekroczeń""");
                    System.out.print("Wybierz raport (1-4): ");
                    
                    switch (pobierzWybór(1, 4)) {
                        case 1 -> fabrykaRaportów.utwórzRaport("miesięczny").generuj();
                        case 2 -> {
                            Kategoria kat = wybierzKategorię();
                            if (kat != null) fabrykaRaportów.utwórzRaport("kategorii", kat.getNazwa()).generuj();
                        }
                        case 3 -> wyświetlBudżety();
                        case 4 -> sprawdźPrzekroczenia();
                    }
                }
            }
        }
    }
    
    private static void wykonajPrognozy() {
        System.out.println("""
            \n══════════ PROGNOZOWANIE WYDATKÓW ══════════
            Wybierz algorytm prognozowania:
            1. Średnia prosta
            2. Prognoza trendowa
            3. Porównaj oba algorytmy
            4. Powrót do menu głównego""");
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
                System.out.printf("%-24s| %12.2f PLN | %10.2f PLN\n", strategia1.getNazwaStrategii(), prognoza1, prognoza1 * 30);
                System.out.printf("%-24s| %12.2f PLN | %10.2f PLN\n", strategia2.getNazwaStrategii(), prognoza2, prognoza2 * 15);
                System.out.println("\nInterpretacja:");
                if (prognoza2 > prognoza1) System.out.println("Algorytm trendowy wykrył wzrost wydatków.");
                else if (prognoza2 < prognoza1) System.out.println("Algorytm trendowy wykrył spadek wydatków.");
                else System.out.println("Brak wyraźnego trendu w wydatkach.");
            }
        }
    }
    
    private static void zarządzajObserwatorami() {
        System.out.println("""
            \n══════════ ZARZĄDZANIE OBSERWATORAMI ══════════
            1. Wyświetl aktywnych obserwatorów
            2. Dodaj nowy typ obserwatora
            3. Testuj powiadomienia
            4. Powrót do menu głównego""");
        System.out.print("Wybierz opcję (1-4): ");
        
        switch (pobierzWybór(1, 4)) {
            case 1 -> System.out.println("\nAktywni obserwatorzy:\n1. UsługaPowiadomień - powiadamia o przekroczeniu budżetu");
            case 2 -> System.out.println("\nDostępne typy obserwatorów:\n1. Powiadomienia konsolowe (już dodane)\n2. Powiadomienia plikowe (w przyszłości)\n3. Powiadomienia email (w przyszłości)\n\nFunkcja w trakcie rozwoju...");
            case 3 -> {
                System.out.println("\n══════════ TEST POWIADOMIEŃ ══════════");
                sprawdźPrzekroczenia();
                System.out.println("Jeśli są przekroczone budżety, powiadomienia zostały wysłane.");
            }
        }
    }
    
    private static void eksportujDane() {
        System.out.println("""
            \n══════════ EKSPORT DANYCH ══════════
            1. Eksport transakcji do CSV
            2. Eksport raportu do pliku tekstowego
            3. Zapisz stan aplikacji
            4. Powrót do menu głównego""");
        System.out.print("Wybierz opcję (1-4): ");
        
        switch (pobierzWybór(1, 4)) {
            case 1 -> eksportujDoCSV();
            case 2 -> {
                System.out.println("\n══════════ EKSPORT RAPORTU ══════════");
                System.out.println("1. Raport miesięczny\n2. Raport kategorii");
                System.out.print("Wybierz typ raportu (1-2): ");
                if (pobierzWybór(1, 2) == 1) {
                    System.out.println("\n=== RAPORT MIESIĘCZNY ===");
                    fabrykaRaportów.utwórzRaport("miesięczny").generuj();
                } else {
                    Kategoria kat = wybierzKategorię();
                    if (kat != null) {
                        System.out.println("\n=== RAPORT KATEGORII: " + kat.getNazwa() + " ===");
                        fabrykaRaportów.utwórzRaport("kategorii", kat.getNazwa()).generuj();
                    }
                }
                System.out.println("\nRaport został wygenerowany. Aby zapisać, przekopiuj powyższe dane.");
            }
            case 3 -> {
                System.out.println("\n══════════ ZAPISYWANIE STANU ══════════");
                System.out.println("Liczba transakcji: " + menedżer.getListaTransakcji().size());
                System.out.println("Liczba budżetów: " + menedżer.getListaBudżetów().size());
                System.out.println("Liczba kategorii: " + kategorie.size());
                System.out.println("\nStan aplikacji został zachowany w pamięci.");
                System.out.println("Uwaga: W obecnej wersji dane są przechowywane tylko w pamięci.");
                System.out.println("Po zamknięciu aplikacji dane zostaną utracone.");
                System.out.println("W przyszłości planowana jest obsługa zapisu do pliku.");
            }
        }
    }
    
    private static void eksportujDoCSV() {
        System.out.println("\n══════════ EKSPORT DO CSV ══════════");
        List<Transakcja> transakcje = menedżer.getListaTransakcji();
        if (transakcje.isEmpty()) {
            System.out.println("Brak transakcji do eksportu.");
            return;
        }
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        System.out.println("ID;Data;Kategoria;Typ;Kwota");
        transakcje.forEach(t -> System.out.printf("%d;%s;%s;%s;%.2f\n",
            t.getId(), sdf.format(t.getData()), t.getKategoria().getNazwa(),
            t.getTyp() == TypTransakcji.PRZYCHOD ? "Przychód" : "Wydatek", t.getKwota()));
        
        System.out.println("\nDane zostały przygotowane do eksportu.");
        System.out.println("Aby zapisać do pliku, przekopiuj powyższe dane.");
    }
}