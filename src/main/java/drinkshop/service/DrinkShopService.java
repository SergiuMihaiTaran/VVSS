package drinkshop.service;

import drinkshop.domain.*;
import drinkshop.export.CsvExporter;
import drinkshop.receipt.ReceiptGenerator;
import drinkshop.repository.IRepository;
import drinkshop.service.validator.*;

import java.util.ArrayList;
import java.util.List;

public class DrinkShopService {

    private final ProductService productService;
    private final OrderService orderService;
    private final RetetaService retetaService;
    private final StocService stocService;
    private final CategorieService categorieService;
    private final TipService tipService;
    private final DailyReportService report;

    private final List<Order> ordersDinSesiune = new ArrayList<>();

    public DrinkShopService(
            IRepository<Integer, Product> productRepo,
            IRepository<Integer, Order> orderRepo,
            IRepository<Integer, Reteta> retetaRepo,
            IRepository<Integer, Stoc> stocRepo,
            IRepository<Integer, Categorie> categorieRepo,
            IRepository<Integer, Tip> tipRepo
    ) {
        this.productService = new ProductService(productRepo, new ProductValidator());

        this.orderService = new OrderService(
                orderRepo,
                productRepo,
                new OrderValidator(),
                new OrderItemValidator()
        );

        this.retetaService = new RetetaService(retetaRepo, new RetetaValidator());
        this.stocService = new StocService(stocRepo, new StocValidator(), new RetetaValidator());

        this.categorieService = new CategorieService(
                categorieRepo,
                productRepo,
                new CategorieValidator()
        );

        this.tipService = new TipService(
                tipRepo,
                productRepo,
                new TipValidator()
        );

        this.report = new DailyReportService();
    }

    // ---------- PRODUCT ----------
    public void addProduct(Product p) {
        productService.addProduct(p);
    }

    public void updateProduct(int id, String name, double price, Categorie categorie, Tip tip) {
        productService.updateProduct(id, name, price, categorie, tip);
    }

    public void deleteProduct(int id) {
        productService.deleteProduct(id);
    }

    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    public List<Product> filtreazaDupaCategorie(Categorie categorie) {
        return productService.filterByCategorie(categorie);
    }

    public List<Product> filtreazaDupaTip(Tip tip) {
        return productService.filterByTip(tip);
    }

    // ---------- ORDER ----------
    public void addOrder(Order o) {
        orderService.addOrder(o);
        ordersDinSesiune.add(o);
    }

    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    public double computeTotal(Order o) {
        return orderService.computeTotal(o);
    }

    public String generateReceipt(Order o) {
        return ReceiptGenerator.generate(o, productService.getAllProducts());
    }

    public double getDailyRevenue() {
        return report.getTotalRevenue(ordersDinSesiune);
    }

    public void exportCsv(String path) {
        CsvExporter.exportOrders(productService.getAllProducts(), orderService.getAllOrders(), path);
    }

    // ---------- STOCK + RECIPE ----------
    public void comandaProdus(Product produs) {
        if (produs == null) {
            throw new IllegalArgumentException("Produsul nu poate fi null.");
        }

        Reteta reteta = retetaService.findById(produs.getId());

        if (reteta == null) {
            throw new IllegalStateException("Nu exista reteta pentru produsul: " + produs.getNume());
        }

        if (!stocService.areSuficient(reteta)) {
            throw new IllegalStateException("Stoc insuficient pentru produsul: " + produs.getNume());
        }

        stocService.consuma(reteta);
    }

    public List<Reteta> getAllRetete() {
        return retetaService.getAll();
    }

    public void addReteta(Reteta r) {
        retetaService.addReteta(r);
    }

    public void updateReteta(Reteta r) {
        retetaService.updateReteta(r);
    }

    public void deleteReteta(int id) {
        retetaService.deleteReteta(id);
    }

    public List<Stoc> getAllStocuri() {
        return stocService.getAll();
    }

    public boolean ingredientExistaInStoc(String ingredient) {
        return stocService.getAll().stream()
                .anyMatch(s -> s.getIngredient().equalsIgnoreCase(ingredient));
    }

    // ---------- CATEGORII ----------
    public void addCategorie(Categorie categorie) {
        categorieService.addCategorie(categorie);
    }

    public void updateCategorie(Categorie categorie) {
        categorieService.updateCategorie(categorie);
    }

    public void deleteCategorie(int id) {
        categorieService.deleteCategorie(id);
    }

    public List<Categorie> getAllCategorii() {
        return categorieService.getAllCategorii();
    }

    public Categorie findCategorieById(int id) {
        return categorieService.findById(id);
    }

    // ---------- TIPURI ----------
    public void addTip(Tip tip) {
        tipService.addTip(tip);
    }

    public void updateTip(Tip tip) {
        tipService.updateTip(tip);
    }

    public void deleteTip(int id) {
        tipService.deleteTip(id);
    }

    public List<Tip> getAllTipuri() {
        return tipService.getAllTipuri();
    }

    public Tip findTipById(int id) {
        return tipService.findById(id);
    }
}