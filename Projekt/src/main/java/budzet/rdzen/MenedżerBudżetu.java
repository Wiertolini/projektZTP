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
        sprawdźBudżety();
    }
    
    public void usuńTransakcję(Transakcja transakcja) {
        listaTransakcji.remove(transakcja);
        sprawdźBudżety();
    }
    
    public void dodajBudżet(Budżet budżet) {
        listaBudżetów.add(budżet);
        aktualizujTransakcjeDlaBudżetu(budżet);
    }
    
    public void usuńBudżet(Budżet budżet) {
        listaBudżetów.remove(budżet);
    }
    
    public void dodajObserwatora(ObserwatorBudżetu obserwator) {
        obserwatorzy.add(obserwator);
    }
    
    public void usuńObserwatora(ObserwatorBudżetu obserwator) {
        obserwatorzy.remove(obserwator);
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
            if (t.getKategoria().equals(budżet.getKategoria())) {
                transakcjeKategorii.add(t);
            }
        }
        budżet.setTransakcje(transakcjeKategorii);
    }
    
    public void powiadomObserwatorów(Budżet budżet) {
        for (ObserwatorBudżetu obserwator : obserwatorzy) {
            obserwator.aktualizuj(budżet);
        }
    }
    
    // Gettery
    public List<Transakcja> getListaTransakcji() { return listaTransakcji; }
    public List<Budżet> getListaBudżetów() { return listaBudżetów; }
}