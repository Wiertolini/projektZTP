# Budżet Domowy - Aplikacja do zarządzania finansami

## Opis projektu
Aplikacja wspomagająca zarządzanie domowym budżetem z wykorzystaniem wzorców projektowych.

## Wzorce projektowe
1. **Singleton** - MenedżerBudżetu (rdzeń systemu)
2. **Observer** - system powiadomień o przekroczeniu budżetu
3. **Strategy** - różne algorytmy prognozowania wydatków
4. **Factory Method** - generowanie różnych typów raportów

## Struktura projektu
- `rdzen/` - rdzeń systemu z encjami i Singletonem
- `obserwatorzy/` - implementacja wzorca Observer
- `prognozy/` - strategie prognozowania wydatków
- `raporty/` - generowanie raportów (Factory Method)

## Uruchomienie
1. Skompiluj wszystkie pliki Java
2. Uruchom klasę `Main.java`

## Autorzy
Projekt zespołowy - 4 osoby z podziałem na moduły zgodnie ze wzorcami projektowymi.