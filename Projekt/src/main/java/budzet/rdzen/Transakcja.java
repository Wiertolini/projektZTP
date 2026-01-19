package budzet.rdzen;

import java.util.Date;

public class Transakcja {
    private int id;
    private Date data;
    private double kwota;
    private Kategoria kategoria;
    private TypTransakcji typ;
    
    public Transakcja(int id, Date data, double kwota, Kategoria kategoria, TypTransakcji typ) {
        this.id = id;
        this.data = data;
        this.kwota = kwota;
        this.kategoria = kategoria;
        this.typ = typ;
    }
    
    public int getId() { return id; }
    public Date getData() { return data; }
    public double getKwota() { return kwota; }
    public Kategoria getKategoria() { return kategoria; }
    public TypTransakcji getTyp() { return typ; }
    
    public void setId(int id) { this.id = id; } // Setter potrzebny np. przy edycji
}