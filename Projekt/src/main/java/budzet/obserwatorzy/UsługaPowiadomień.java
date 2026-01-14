package budzet.obserwatorzy;

import budzet.rdzen.Budżet;

public class UsługaPowiadomień implements ObserwatorBudżetu {
    @Override
    public void aktualizuj(Budżet budżet) {
        double aktualneWydatki = budżet.getAktualneWydatki();
        double limit = budżet.getLimit();
        double przekroczenie = aktualneWydatki - limit;
        
        System.out.println("🚨 POWIADOMIENIE: Budżet przekroczony!");
        System.out.println("Kategoria: " + budżet.getKategoria().getNazwa());
        System.out.println("Okres: " + budżet.getOkres());
        System.out.println("Limit: " + limit + " PLN");
        System.out.println("Wydatki: " + aktualneWydatki + " PLN");
        System.out.println("Przekroczenie: " + przekroczenie + " PLN");
        System.out.println("----------------------------------------");
    }
}