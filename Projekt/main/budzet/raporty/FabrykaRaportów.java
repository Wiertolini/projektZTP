package Projekt.main.budzet.raporty;

public class FabrykaRaportów {

    public Raport utwórzRaport(String typ) {

        if (typ == null) {
            throw new IllegalArgumentException("Typ raportu nie może być null");
        }

        switch (typ.toLowerCase()) {
            case "miesięczny":
                return new RaportMiesięczny();

            case "kategorie":
                return new RaportKategorii();

            default:
                throw new IllegalArgumentException("Nieznany typ raportu: " + typ);
        }
    }
}
