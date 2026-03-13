package drinkshop.service;

import drinkshop.domain.Categorie;
import drinkshop.domain.Product;
import drinkshop.repository.IRepository;
import drinkshop.service.validator.Validator;

import java.util.List;

public class CategorieService {

    private final IRepository<Integer, Categorie> categorieRepo;
    private final IRepository<Integer, Product> productRepo;
    private final Validator<Categorie> validator;

    public CategorieService(IRepository<Integer, Categorie> categorieRepo,
                            IRepository<Integer, Product> productRepo,
                            Validator<Categorie> validator) {
        this.categorieRepo = categorieRepo;
        this.productRepo = productRepo;
        this.validator = validator;
    }

    public void addCategorie(Categorie categorie) {
        validator.validate(categorie);
        categorieRepo.save(categorie);
    }

    public void updateCategorie(Categorie categorieNoua) {
        validator.validate(categorieNoua);

        Categorie categorieVeche = categorieRepo.findOne(categorieNoua.getId());
        if (categorieVeche == null) {
            throw new IllegalArgumentException("Categoria nu exista.");
        }

        categorieRepo.update(categorieNoua);

        List<Product> produse = productRepo.findAll();
        for (Product p : produse) {
            if (p.getCategorie() != null && p.getCategorie().getId() == categorieNoua.getId()) {
                Product actualizat = new Product(
                        p.getId(),
                        p.getNume(),
                        p.getPret(),
                        categorieNoua,
                        p.getTip()
                );
                productRepo.update(actualizat);
            }
        }
    }

    public void deleteCategorie(int id) {
        Categorie categorie = categorieRepo.findOne(id);
        if (categorie == null) {
            throw new IllegalArgumentException("Categoria nu exista.");
        }

        boolean folosita = productRepo.findAll().stream()
                .anyMatch(p -> p.getCategorie() != null && p.getCategorie().getId() == id);

        if (folosita) {
            throw new IllegalStateException("Categoria nu poate fi stearsa deoarece exista produse care apartin acestei categorii.");
        }

        categorieRepo.delete(id);
    }

    public List<Categorie> getAllCategorii() {
        return categorieRepo.findAll();
    }

    public Categorie findById(int id) {
        return categorieRepo.findOne(id);
    }
}