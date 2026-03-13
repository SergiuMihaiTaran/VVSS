package drinkshop.service;

import drinkshop.domain.Product;
import drinkshop.domain.Tip;
import drinkshop.repository.IRepository;
import drinkshop.service.validator.Validator;

import java.util.List;

public class TipService {

    private final IRepository<Integer, Tip> tipRepo;
    private final IRepository<Integer, Product> productRepo;
    private final Validator<Tip> validator;

    public TipService(IRepository<Integer, Tip> tipRepo,
                      IRepository<Integer, Product> productRepo,
                      Validator<Tip> validator) {
        this.tipRepo = tipRepo;
        this.productRepo = productRepo;
        this.validator = validator;
    }

    public void addTip(Tip tip) {
        validator.validate(tip);
        tipRepo.save(tip);
    }

    public void updateTip(Tip tipNou) {
        validator.validate(tipNou);

        Tip tipVechi = tipRepo.findOne(tipNou.getId());
        if (tipVechi == null) {
            throw new IllegalArgumentException("Tipul nu exista.");
        }

        tipRepo.update(tipNou);

        List<Product> produse = productRepo.findAll();
        for (Product p : produse) {
            if (p.getTip() != null && p.getTip().getId() == tipNou.getId()) {
                Product actualizat = new Product(
                        p.getId(),
                        p.getNume(),
                        p.getPret(),
                        p.getCategorie(),
                        tipNou
                );
                productRepo.update(actualizat);
            }
        }
    }

    public void deleteTip(int id) {
        Tip tip = tipRepo.findOne(id);
        if (tip == null) {
            throw new IllegalArgumentException("Tipul nu exista.");
        }

        boolean folosit = productRepo.findAll().stream()
                .anyMatch(p -> p.getTip() != null && p.getTip().getId() == id);

        if (folosit) {
            throw new IllegalStateException("Tipul nu poate fi sters deoarece exista produse care apartin acestui tip.");
        }

        tipRepo.delete(id);
    }

    public List<Tip> getAllTipuri() {
        return tipRepo.findAll();
    }

    public Tip findById(int id) {
        return tipRepo.findOne(id);
    }
}