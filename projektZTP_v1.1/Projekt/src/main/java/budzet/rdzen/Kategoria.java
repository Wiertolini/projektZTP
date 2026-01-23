package budzet.rdzen;

/**
@brief Kategoria transakcji (np. jedzenie, transport, rachunki).
*/

public class Kategoria {
    private String nazwa;
    
    public Kategoria(String nazwa) {
        this.nazwa = nazwa;
    }
    
    public String getNazwa() { return nazwa; }
    public void setNazwa(String nazwa) { this.nazwa = nazwa; }
}