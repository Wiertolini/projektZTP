package budzet.rdzen;

import budzet.obserwatorzy.ObserwatorBudżetu;
import java.util.ArrayList;
import java.util.List;

public class MenedżerBudżetu {
    private static MenedżerBudżetu instancja;
    private List<Transakcja> listaTransakcji;
    private List<Budżet> listaBudżetów;
    private List<ObserwatorBudżetu> obserwatorzy;
    
    private MenedżerBudżetu() {
        listaTransakcji = new ArrayList<>();
        listaBudżetów = new ArrayList<>();
        obserwatorzy = new ArrayList<>();
    }
    
    public static synchronized MenedżerBudżetu pobierzInstancję() {
        if (instancja == null) {
            instancja = new MenedżerBudżetu();
        }
        return instancja;
    }
    
    public void dodajTransakcję(Transakcja transakcja) {
        listaTransakcji.add(transakcja);
        sprawdźBudżety(); // Automatyczne powiadomienie po zmianie stanu
    }
    
    public void dodajBudżet(Budżet budżet) {
        listaBudżetów.add(budżet);
        aktualizujTransakcjeDlaBudżetu(budżet);
    }
    
    public void dodajObserwatora(ObserwatorBudżetu obserwator) {
        obserwatorzy.add(obserwator);
    }
    
    private void sprawdźBudżety() {
        for (Budżet budżet : listaBudżetów) {
            aktualizujTransakcjeDlaBudżetu(budżet);
            if (budżet.czyPrzekroczony()) {
                powiadomObserwatorów(budżet);
            }
        }
    }
    
    private void aktualizujTransakcjeDlaBudżetu(Budżet budżet) {
        List<Transakcja> transakcjeKategorii = new ArrayList<>();
        for (Transakcja t : listaTransakcji) {
            if (t.getKategoria().getNazwa().equals(budżet.getKategoria().getNazwa())) {
                transakcjeKategorii.add(t);
            }
        }
        budżet.setTransakcje(transakcjeKategorii);
    }
    
    private void powiadomObserwatorów(Budżet budżet) {
        for (ObserwatorBudżetu obserwator : obserwatorzy) {
            obserwator.aktualizuj(budżet);
        }
    }
    
    public List<Transakcja> getListaTransakcji() { return listaTransakcji; }
    public List<Budżet> getListaBudżetów() { return listaBudżetów; }
}