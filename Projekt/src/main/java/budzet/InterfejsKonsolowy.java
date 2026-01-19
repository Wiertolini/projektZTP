package budzet;

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

    // Prosta struktura dla opcji menu
    record Opcja(String opis, Runnable akcja) {}

    public static void main(String[] args) {
        inicjalizujDane();
        System.out.println("=== APLIKACJA BUDŻET DOMOWY v2.0 (Compact) ===\n");

        runMenu("MENU GŁÓWNE", List.of(
            new Opcja("Zarządzaj transakcjami", InterfejsKonsolowy::menuTransakcje),
            new Opcja("Zarządzaj kategoriami", InterfejsKonsolowy::menuKategorie),
            new Opcja("Zarządzaj budżetami", InterfejsKonsolowy::menuBudzety),
            new Opcja("Statystyki", InterfejsKonsolowy::pokazStatystyki),
            new Opcja("Raporty", InterfejsKonsolowy::menuRaporty),
            new Opcja("Prognozy", InterfejsKonsolowy::menuPrognozy),
            new Opcja("Eksport", InterfejsKonsolowy::menuEksport)
        ));
        scanner.close();
    }

    // --- GENERYCZNY SILNIK MENU (Serce optymalizacji) ---
    private static void runMenu(String tytul, List<Opcja> opcje) {
        while (true) {
            System.out.println("\n--- " + tytul + " ---");
            for (int i = 0; i < opcje.size(); i++) 
                System.out.println((i + 1) + ". " + opcje.get(i).opis);
            System.out.println((opcje.size() + 1) + ". Powrót/Wyjście");

            int wybor = pobierzLiczbe("Wybierz: ", 1, opcje.size() + 1);
            if (wybor == opcje.size() + 1) return;
            opcje.get(wybor - 1).akcja.run();
        }
    }

    // --- POMOCNIKI WEJŚCIA/WYJŚCIA ---
    private static int pobierzLiczbe(String msg, int min, int max) {
        while (true) {
            System.out.print(msg);
            try {
                int w = Integer.parseInt(scanner.nextLine());
                if (w >= min && w <= max) return w;
            } catch (NumberFormatException e) {}
            System.out.println("Błąd. Podaj liczbę z zakresu " + min + "-" + max);
        }
    }

    private static double pobierzKwote() {
        while (true) {
            System.out.print("Podaj kwotę: ");
            try {
                double d = Double.parseDouble(scanner.nextLine());
                if (d > 0) return d;
            } catch (NumberFormatException e) {}
            System.out.println("Błędna kwota!");
        }
    }

    private static String pobierzTekst(String msg) {
        System.out.print(msg);
        return scanner.nextLine().trim();
    }

    private static Kategoria wybierzKategorie() {
        if (kategorie.isEmpty()) { System.out.println("Brak kategorii!"); return null; }
        System.out.println("Wybierz kategorię:");
        for (int i = 0; i < kategorie.size(); i++) 
            System.out.println((i + 1) + ". " + kategorie.get(i).getNazwa());
        return kategorie.get(pobierzLiczbe("Numer: ", 1, kategorie.size()) - 1);
    }

    // --- LOGIKA TRANSAKCJI ---
    private static void menuTransakcje() {
        runMenu("TRANSAKCJE", List.of(
            new Opcja("Dodaj", () -> {
                TypTransakcji typ = pobierzLiczbe("1. Przychód, 2. Wydatek: ", 1, 2) == 1 ? TypTransakcji.PRZYCHOD : TypTransakcji.WYDATEK;
                double kwota = pobierzKwote();
                Kategoria kat = wybierzKategorie();
                if (kat != null) {
                    menedżer.dodajTransakcję(new Transakcja(menedżer.getListaTransakcji().size() + 1, new Date(), kwota, kat, typ));
                    System.out.println("Dodano!");
                }
            }),
            new Opcja("Lista", InterfejsKonsolowy::wyswietlTransakcje),
            new Opcja("Usuń", () -> {
                wyswietlTransakcje();
                int id = pobierzLiczbe("ID do usunięcia: ", 1, Integer.MAX_VALUE);
                menedżer.getListaTransakcji().removeIf(t -> t.getId() == id);
                System.out.println("Usunięto (jeśli istniała).");
            })
        ));
    }

    private static void wyswietlTransakcje() {
        menedżer.getListaTransakcji().forEach(t -> 
            System.out.printf("ID:%d | %s | %s | %s | %.2f\n", t.getId(), sdf.format(t.getData()), t.getKategoria().getNazwa(), t.getTyp(), t.getKwota()));
    }

    // --- LOGIKA KATEGORII ---
    private static void menuKategorie() {
        runMenu("KATEGORIE", List.of(
            new Opcja("Dodaj", () -> kategorie.add(new Kategoria(pobierzTekst("Nazwa: ")))),
            new Opcja("Lista", () -> kategorie.forEach(k -> System.out.println("- " + k.getNazwa()))),
            new Opcja("Usuń", () -> {
                Kategoria k = wybierzKategorie();
                if (k != null) kategorie.remove(k);
            })
        ));
    }

    // --- LOGIKA BUDŻETÓW ---
    private static void menuBudzety() {
        runMenu("BUDŻETY", List.of(
            new Opcja("Dodaj", () -> {
                Kategoria k = wybierzKategorie();
                if (k != null) menedżer.dodajBudżet(new Budżet(k, pobierzKwote(), pobierzTekst("Okres (np. 2024-05): ")));
            }),
            new Opcja("Lista", () -> menedżer.getListaBudżetów().forEach(b -> 
                System.out.printf("%s | Limit: %.2f | Wydano: %.2f | %s\n", b.getKategoria().getNazwa(), b.getLimit(), b.getAktualneWydatki(), b.czyPrzekroczony() ? "PRZEKROCZONY" : "OK"))),
            new Opcja("Sprawdź przekroczenia", () -> menedżer.getListaBudżetów().stream().filter(Budżet::czyPrzekroczony).forEach(b -> System.out.println("ALARM: " + b.getKategoria().getNazwa())))
        ));
    }

    // --- POZOSTAŁE FUNKCJE ---
    private static void pokazStatystyki() {
        double wplywy = menedżer.getListaTransakcji().stream().filter(t -> t.getTyp() == TypTransakcji.PRZYCHOD).mapToDouble(Transakcja::getKwota).sum();
        double wydatki = menedżer.getListaTransakcji().stream().filter(t -> t.getTyp() == TypTransakcji.WYDATEK).mapToDouble(Transakcja::getKwota).sum();
        System.out.printf("Przychody: %.2f | Wydatki: %.2f | Bilans: %.2f\n", wplywy, wydatki, wplywy - wydatki);
    }

    private static void menuRaporty() {
        runMenu("RAPORTY", List.of(
            new Opcja("Miesięczny", () -> fabryka.utwórzRaport("miesięczny").generuj()),
            new Opcja("Kategorii", () -> { Kategoria k = wybierzKategorie(); if(k!=null) fabryka.utwórzRaport("kategorii", k.getNazwa()).generuj(); })
        ));
    }

    private static void menuPrognozy() {
        runMenu("PROGNOZY", List.of(
            new Opcja("Średnia", () -> prognozuj(new ŚredniaProstaPrognoza())),
            new Opcja("Trend", () -> prognozuj(new PrognozaTrendowa()))
        ));
    }

    private static void prognozuj(StrategiaPrognozy strategia) {
        System.out.printf("Prognoza (%s): %.2f PLN\n", strategia.getNazwaStrategii(), strategia.prognozuj(menedżer.getListaTransakcji()));
    }

    private static void menuEksport() {
        runMenu("EKSPORT", List.of(
            new Opcja("Do CSV", () -> menedżer.getListaTransakcji().forEach(t -> System.out.printf("%d;%s;%.2f\n", t.getId(), t.getKategoria().getNazwa(), t.getKwota())))
        ));
    }

    // --- DANE STARTOWE ---
    private static void inicjalizujDane() {
        if (!kategorie.isEmpty()) return;
        List.of("Jedzenie", "Transport", "Rozrywka", "Rachunki").forEach(n -> kategorie.add(new Kategoria(n)));
        menedżer.dodajObserwatora(new UsługaPowiadomień());
        
        Random r = new Random();
        for (int i = 0; i < 10; i++) {
            menedżer.dodajTransakcję(new Transakcja(i, new Date(), r.nextDouble() * 500, kategorie.get(r.nextInt(kategorie.size())), r.nextBoolean() ? TypTransakcji.WYDATEK : TypTransakcji.PRZYCHOD));
        }
    }
}