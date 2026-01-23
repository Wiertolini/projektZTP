# 📊 Projekt ZTP – System Zarządzania Budżetem Domowym



## 🎯 Cel projektu

Celem projektu jest stworzenie konsolowej aplikacji do zarządzania budżetem domowym,
która umożliwia:
- ewidencjonowanie przychodów i wydatków,
- analizę danych finansowych,
- prognozowanie przyszłych kosztów,
- generowanie raportów,
- eksport danych do plików CSV,
- automatyczne reagowanie na zmiany stanu budżetu.

---

## 🧩 Zastosowane wzorce projektowe

W projekcie wykorzystano następujące wzorce:

### 🔹 Obserwator
- **Cel:** automatyczne powiadamianie o zmianach stanu budżetu  
- **Przykład:** informowanie o przekroczeniu limitu wydatków  
- **Pakiet:** `obserwatorzy`

### 🔹 Strategia
- **Cel:** możliwość wyboru algorytmu prognozowania w czasie działania programu  
- **Pakiet:** `prognozy`

### 🔹 Fabryka
- **Cel:** centralizacja procesu tworzenia raportów  
- **Pakiet:** `raporty`

### 🔹 Adapter
- **Cel:** integracja zewnętrznej biblioteki zapisu CSV z interfejsem systemu  
- **Pakiet:** `eksport`

---

## 🗂 Struktura projektu

```
projekt/
├── rdzen/              # logika domenowa (budżet, transakcje)
├── obserwatorzy/       # wzorzec Obserwator
├── prognozy/           # wzorzec Strategia
├── raporty/            # wzorzec Fabryka
├── eksport/            # wzorzec Adapter
├── docs/               # dokumentacja projektu
└── InterfejsKonsolowy.java
```

---

## ▶️ Uruchomienie projektu

### Wymagania
- Java JDK **17** lub nowsza

### Kompilacja
```bash
javac *.java
```

### Uruchomienie
```bash
java InterfejsKonsolowy
```

Projekt **nie wymaga zewnętrznych bibliotek**.

---

## 👤 Instrukcja użytkownika (skrót)

Aplikacja działa w trybie konsolowym i oferuje menu tekstowe umożliwiające:
- dodawanie transakcji,
- przypisywanie kategorii,
- generowanie raportów,
- wykonywanie prognoz,
- eksport danych do CSV.

Obsługa programu odbywa się poprzez wybór odpowiednich opcji menu.

---

## 👥 Podział pracy w zespole

- **Karol Ziemak**- Rdzeń Systemu
- **Jakub Wierciszewski** - eksport + dokumentacja
- **Michał Szwabowicz**- Prognozy
- **Szymon Duchnowski**- Raporty + obserwatorzy + dokumentacja


---

## 📄 Dokumentacja

Szczegółowa dokumentacja projektu znajduje się w katalogu `docs/` i obejmuje:
- opis wzorców projektowych,
- diagramy UML,
- instrukcję użytkownika i instalacji.
