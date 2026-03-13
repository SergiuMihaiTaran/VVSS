package drinkshop.service;

import drinkshop.domain.Categorie;
import drinkshop.domain.Product;
import drinkshop.domain.Tip;
import drinkshop.repository.Repository;
import drinkshop.service.validator.Validator;

import java.util.List;
import java.util.stream.Collectors;

public class ProductService {

    private final Repository<Integer, Product> productRepo;
    private final Validator<Product> validator;

    public ProductService(Repository<Integer, Product> productRepo,
                          Validator<Product> validator) {
        this.productRepo = productRepo;
        this.validator = validator;
    }

    public void addProduct(Product p) {
        validator.validate(p);
        productRepo.save(p);
    }

    public void updateProduct(int id, String name, double price,
                              Categorie categorie, Tip tip) {

        Product existing = productRepo.findOne(id);
        if (existing == null) {
            throw new IllegalArgumentException("Produsul cu ID " + id + " nu exista.");
        }

        Product updated = new Product(id, name, price, categorie, tip);
        validator.validate(updated);
        productRepo.update(updated);
    }

    public void deleteProduct(int id) {
        Product existing = productRepo.findOne(id);
        if (existing == null) {
            throw new IllegalArgumentException("Produsul cu ID " + id + " nu exista.");
        }
        productRepo.delete(id);
    }

    public List<Product> getAllProducts() {
        return productRepo.findAll();
    }

    public Product findById(int id) {
        return productRepo.findOne(id);
    }

    public List<Product> filterByCategorie(Categorie categorie) {
        if (categorie == null || "ALL".equalsIgnoreCase(categorie.getNume())) {
            return getAllProducts();
        }

        return getAllProducts().stream()
                .filter(p -> p.getCategorie() != null && p.getCategorie().getId() == categorie.getId())
                .collect(Collectors.toList());
    }

    public List<Product> filterByTip(Tip tip) {
        if (tip == null || "ALL".equalsIgnoreCase(tip.getNume())) {
            return getAllProducts();
        }

        return getAllProducts().stream()
                .filter(p -> p.getTip() != null && p.getTip().getId() == tip.getId())
                .collect(Collectors.toList());
    }
}