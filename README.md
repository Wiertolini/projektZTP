# 📊 Budżet Domowy - Aplikacja do zarządzania finansami



### ✨ Kluczowe funkcje

- 📝 **Zarządzanie transakcjami** - dodawanie, edycja, usuwanie i wyszukiwanie
- 📂 **Kategorie wydatków** - pełna kontrola nad własnymi kategoriami
- 💰 **Planowanie budżetów** - ustawianie limitów miesięcznych
- 📊 **Statystyki i raporty** - szczegółowe analizy finansowe
- 🔮 **Prognozowanie** - inteligentne przewidywanie przyszłych wydatków
- 🔔 **System powiadomień** - ostrzeżenia o przekroczeniu budżetu
- 💾 **Eksport danych** - możliwość zapisu raportów

## 🛠️ Wymagania techniczne

- **Java Development Kit (JDK)** w wersji 8 lub nowszej
- System operacyjny: Windows, Linux, macOS
- Minimalna ilość pamięci RAM: 512 MB
- Konsola/terminal do uruchomienia aplikacji

## 🚀 Jak uruchomić aplikację

### Użycie IDE (np. IntelliJ IDEA, Eclipse)

1. Otwórz projekt w swoim IDE
2. Upewnij się, że ścieżka do JDK jest poprawnie skonfigurowana
3. Znajdź plik `InterfejsKonsolowy.java` w pakiecie `budzet`
4. Uruchom klasę jako aplikację Java

projektZTP2/
├── README.md
├── src/
│   └── main/
│       └── java/
│           └── budzet/
│               ├── InterfejsKonsolowy.java     # Główna klasa aplikacji
│               ├── rdzen/                       # Rdzeń systemu (Singleton)
│               │   ├── MenedżerBudżetu.java
│               │   ├── Transakcja.java
│               │   ├── Budżet.java
│               │   ├── Kategoria.java
│               │   └── TypTransakcji.java
│               ├── obserwatorzy/                # Wzorzec Observer
│               │   ├── ObserwatorBudżetu.java
│               │   └── UsługaPowiadomień.java
│               ├── prognozy/                    # Wzorzec Strategy
│               │   ├── StrategiaPrognozy.java
│               │   ├── ŚredniaProstaPrognoza.java
│               │   └── PrognozaTrendowa.java
│               └── raporty/                     # Wzorzec Factory Method
│                   ├── Raport.java
│                   ├── RaportMiesięczny.java
│                   ├── RaportKategorii.java
│                   └── FabrykaRaportów.java
🎯 Wzorce projektowe
Aplikacja demonstruje praktyczne zastosowanie czterech kluczowych wzorców projektowych:

1. Singleton (MenedżerBudżetu)
Cel: Zapewnienie jednej, globalnej instancji zarządzającej całym systemem

Zastosowanie: Centralne zarządzanie transakcjami, budżetami i obserwatorami

Korzyści: Spójność danych, zapobieganie konfliktom

2. Observer (ObserwatorBudżetu)
Cel: Powiadamianie o przekroczeniu budżetu

Zastosowanie: System powiadomień w UsługaPowiadomień

Korzyści: Luźne powiązania, rozszerzalność

3. Strategy (StrategiaPrognozy)
Cel: Różne algorytmy prognozowania wydatków

Zastosowanie: ŚredniaProstaPrognoza i PrognozaTrendowa

Korzyści: Łatwa wymiana algorytmów, rozszerzalność

4. Factory Method (FabrykaRaportów)
Cel: Tworzenie różnych typów raportów

Zastosowanie: RaportMiesięczny i RaportKategorii

Korzyści: Enkapsulacja tworzenia obiektów, rozszerzalność

📋 Instrukcja użytkowania
Pierwsze uruchomienie
Po uruchomieniu aplikacji zobaczysz:

Powitalny ekran z logo aplikacji

Przykładowe dane zostaną automatycznie załadowane

Dostępne będzie menu główne z 9 opcjami

Menu główne
text
════════════════════ MENU GŁÓWNE ════════════════════
1. 🏷️  Zarządzaj transakcjami
2. 📂 Zarządzaj kategoriami
3. 💰 Zarządzaj budżetami
4. 📊 Wyświetl statystyki
5. 📈 Generuj raporty
6. 🔮 Wykonaj prognozy
7. 🔔 Zarządzaj obserwatorami
8. 💾 Eksportuj dane
9. ❌ Wyjście
═══════════════════════════════════════════════════════
Przykładowy przepływ pracy
Dodaj swoje pierwsze transakcje

Wybierz opcję 1 → 1 (Dodaj nową transakcję)

Wybierz typ (Przychód/Wydatek)

Podaj kwotę

Wybierz kategorię z listy

Ustaw limity budżetowe

Wybierz opcję 3 → 1 (Dodaj nowy budżet)

Wybierz kategorię

Podaj miesięczny limit

Ustaw okres (np. 2024-05)

Sprawdź swoje finanse

Wybierz opcję 4 (Statystyki) - podsumowanie finansowe

Wybierz opcję 5 (Raporty) - szczegółowe analizy

🎨 Funkcje aplikacji
📝 Zarządzanie transakcjami
Dodawanie nowych transakcji (przychody i wydatki)

Edycja i usuwanie istniejących transakcji

Wyszukiwanie według kategorii, daty, kwoty lub typu

Podgląd wszystkich transakcji w formacie tabeli

📂 Zarządzanie kategoriami
Tworzenie własnych kategorii wydatków/przychodów

Edycja nazw istniejących kategorii

Usuwanie nieużywanych kategorii (z zabezpieczeniami)

Statystyki wykorzystania kategorii

💰 Zarządzanie budżetami
Ustawianie miesięcznych limitów dla kategorii

Monitorowanie wykorzystania budżetu w czasie rzeczywistym

Ostrzeżenia o zbliżaniu się do limitu (80% wykorzystania)

Powiadomienia o przekroczeniu budżetu

📊 Statystyki i raporty
Podsumowanie finansowe (bilans, przychody, wydatki)

Analiza wydatków według kategorii (procentowo)

Raporty miesięczne z podsumowaniem

Szczegółowe raporty dla poszczególnych kategorii

🔮 Prognozowanie
Dwie strategie prognozowania:

Średnia prosta - średnia z historycznych wydatków

Prognoza trendowa - uwzględnia trendy w wydatkach

Porównanie różnych algorytmów

Estymacja przyszłych wydatków miesięcznych

🔔 System powiadomień
Automatyczne powiadomienia o przekroczeniu budżetu

Konfigurowalne poziomy ostrzeżeń

Testowanie systemu powiadomień

💾 Eksport danych
Eksport transakcji do formatu CSV

Generowanie raportów w formie tekstowej

Możliwość kopiowania danych do schowka

🔧 Rozwiązywanie problemów
Błędy kompilacji
text
❌ package budzet.rdzen does not exist
Rozwiązanie:

Upewnij się, że wszystkie pliki są w odpowiednich katalogach

Sprawdź deklaracje pakietów w plikach źródłowych

Skompiluj najpierw pakiet rdzen, potem pozostałe

Błędy wykonania
text
Exception in thread "main" java.lang.NoClassDefFoundError
Rozwiązanie:

Sprawdź czy klasa InterfejsKonsolowy jest w pakiecie budzet

Użyj poprawnej ścieżki classpath: -cp src/main/java

Upewnij się, że wszystkie pliki zostały skompilowane

Znane ograniczenia
Dane są przechowywane tylko w pamięci (brak zapisu do pliku)

Obsługa dat jest uproszczona

Brak walidacji niektórych danych wejściowych



Projekt został opracowany przez zespół 4 osób:

Jakub Wierciszewski	Moduł odpowiedzialności	Wzorzec projektowy
Karol Ziemak	Rdzeń systemu	Singleton
Szymon Duchnowski	System powiadomień	Observer
Michał Szwabowicz	Algorytmy prognozowania	Strategy
Jakub Wierciszewski	Generowanie raportów	Factory Method

