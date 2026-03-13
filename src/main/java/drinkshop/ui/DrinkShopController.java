package drinkshop.ui;

import drinkshop.domain.*;
import drinkshop.service.DrinkShopService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class DrinkShopController {

    private DrinkShopService service;

    // obiecte speciale DOAR pentru UI / filtrare
    private final Categorie CATEGORIE_ALL = new Categorie(-1, "ALL");
    private final Tip TIP_ALL = new Tip(-1, "ALL");

    // ---------- PRODUCT ----------
    @FXML private TableView<Product> productTable;
    @FXML private TableColumn<Product, Integer> colProdId;
    @FXML private TableColumn<Product, String> colProdName;
    @FXML private TableColumn<Product, Double> colProdPrice;
    @FXML private TableColumn<Product, String> colProdCategorie;
    @FXML private TableColumn<Product, String> colProdTip;
    @FXML private TextField txtProdName, txtProdPrice;
    @FXML private ComboBox<Categorie> comboProdCategorie;
    @FXML private ComboBox<Tip> comboProdTip;

    // ---------- RETETE ----------
    @FXML private TableView<Reteta> retetaTable;
    @FXML private TableColumn<Reteta, Integer> colRetetaId;
    @FXML private TableColumn<Reteta, String> colRetetaDesc;

    @FXML private TableView<IngredientReteta> newRetetaTable;
    @FXML private TableColumn<IngredientReteta, String> colNewIngredName;
    @FXML private TableColumn<IngredientReteta, Double> colNewIngredCant;
    @FXML private TextField txtNewIngredName, txtNewIngredCant;

    // ---------- ORDER (CURRENT) ----------
    @FXML private TableView<OrderItem> currentOrderTable;
    @FXML private TableColumn<OrderItem, String> colOrderProdName;
    @FXML private TableColumn<OrderItem, Integer> colOrderQty;

    // ---------- STOC ----------
    @FXML private TableView<Stoc> stocTable;
    @FXML private TableColumn<Stoc, String> colStocIngredient;
    @FXML private TableColumn<Stoc, Double> colStocCantitate;
    @FXML private TableColumn<Stoc, Double> colStocMinim;

    // ---------- CATEGORII ----------
    @FXML private TableView<Categorie> categorieTable;
    @FXML private TableColumn<Categorie, Integer> colCatId;
    @FXML private TableColumn<Categorie, String> colCatName;
    @FXML private TextField txtCatName;

    // ---------- TIPURI ----------
    @FXML private TableView<Tip> tipTable;
    @FXML private TableColumn<Tip, Integer> colTipId;
    @FXML private TableColumn<Tip, String> colTipName;
    @FXML private TextField txtTipName;

    @FXML private ComboBox<Integer> comboQty;
    @FXML private Label lblOrderTotal;
    @FXML private TextArea txtReceipt;
    @FXML private Label lblTotalRevenue;

    private final ObservableList<Product> productList = FXCollections.observableArrayList();
    private final ObservableList<Reteta> retetaList = FXCollections.observableArrayList();
    private final ObservableList<IngredientReteta> newRetetaList = FXCollections.observableArrayList();
    private final ObservableList<OrderItem> currentOrderItems = FXCollections.observableArrayList();
    private final ObservableList<Stoc> stocList = FXCollections.observableArrayList();
    private final ObservableList<Categorie> categorieList = FXCollections.observableArrayList();
    private final ObservableList<Tip> tipList = FXCollections.observableArrayList();

    private Order currentOrder = new Order(1);

    public void setService(DrinkShopService service) {
        this.service = service;
        initData();
    }

    @FXML
    private void initialize() {

        // ---------- PRODUCTS ----------
        colProdId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colProdName.setCellValueFactory(new PropertyValueFactory<>("nume"));
        colProdPrice.setCellValueFactory(new PropertyValueFactory<>("pret"));
        colProdCategorie.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().getCategorie() != null
                                ? data.getValue().getCategorie().getNume()
                                : ""
                )
        );
        colProdTip.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().getTip() != null
                                ? data.getValue().getTip().getNume()
                                : ""
                )
        );
        productTable.setItems(productList);

        comboProdCategorie.setConverter(new StringConverter<>() {
            @Override
            public String toString(Categorie categorie) {
                return categorie == null ? "" : categorie.getNume();
            }

            @Override
            public Categorie fromString(String string) {
                return null;
            }
        });

        comboProdTip.setConverter(new StringConverter<>() {
            @Override
            public String toString(Tip tip) {
                return tip == null ? "" : tip.getNume();
            }

            @Override
            public Tip fromString(String string) {
                return null;
            }
        });

        productTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                txtProdName.setText(newVal.getNume());
                txtProdPrice.setText(String.valueOf(newVal.getPret()));
                comboProdCategorie.setValue(newVal.getCategorie());
                comboProdTip.setValue(newVal.getTip());
            }
        });

        // ---------- RETETE ----------
        colRetetaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colRetetaDesc.setCellValueFactory(data -> {
            Reteta r = data.getValue();
            String desc = r.getIngrediente().stream()
                    .map(i -> i.getDenumire() + " (" + i.getCantitate() + ")")
                    .collect(Collectors.joining(", "));
            return new SimpleStringProperty(desc);
        });
        retetaTable.setItems(retetaList);

        colNewIngredName.setCellValueFactory(new PropertyValueFactory<>("denumire"));
        colNewIngredCant.setCellValueFactory(new PropertyValueFactory<>("cantitate"));
        newRetetaTable.setItems(newRetetaList);

        // ---------- CURRENT ORDER ----------
        colOrderProdName.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getProduct().getNume()));
        colOrderQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        currentOrderTable.setItems(currentOrderItems);

        comboQty.setItems(FXCollections.observableArrayList(1,2,3,4,5,6,7,8,9,10));

        // ---------- STOC ----------
        colStocIngredient.setCellValueFactory(new PropertyValueFactory<>("ingredient"));
        colStocCantitate.setCellValueFactory(new PropertyValueFactory<>("cantitate"));
        colStocMinim.setCellValueFactory(new PropertyValueFactory<>("stocMinim"));
        stocTable.setItems(stocList);

        // ---------- CATEGORII ----------
        colCatId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCatName.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getNume()));
        categorieTable.setItems(categorieList);

        categorieTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                txtCatName.setText(newVal.getNume());
            }
        });

        // ---------- TIPURI ----------
        colTipId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTipName.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getNume()));
        tipTable.setItems(tipList);

        tipTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                txtTipName.setText(newVal.getNume());
            }
        });
    }

    private void initData() {
        productList.setAll(service.getAllProducts());
        retetaList.setAll(service.getAllRetete());
        stocList.setAll(service.getAllStocuri());
        categorieList.setAll(service.getAllCategorii());
        tipList.setAll(service.getAllTipuri());

        reloadCategorieCombo();
        reloadTipCombo();

        lblTotalRevenue.setText(String.format("Daily Revenue: %.2f", service.getDailyRevenue()));
        updateOrderTotal();
    }

    // ---------- HELPERS ----------
    private boolean esteCategorieAll(Categorie categorie) {
        return categorie != null && "ALL".equalsIgnoreCase(categorie.getNume());
    }

    private boolean esteTipAll(Tip tip) {
        return tip != null && "ALL".equalsIgnoreCase(tip.getNume());
    }

    private void clearProductFields() {
        txtProdName.clear();
        txtProdPrice.clear();
        comboProdCategorie.setValue(null);
        comboProdTip.setValue(null);
    }

    private void reloadCategorieCombo() {
        comboProdCategorie.getItems().clear();
        comboProdCategorie.getItems().add(CATEGORIE_ALL);
        comboProdCategorie.getItems().addAll(service.getAllCategorii());
    }

    private void reloadTipCombo() {
        comboProdTip.getItems().clear();
        comboProdTip.getItems().add(TIP_ALL);
        comboProdTip.getItems().addAll(service.getAllTipuri());
    }

    // ---------- PRODUCT ----------
    @FXML
    private void onAddProduct() {
        Reteta r = retetaTable.getSelectionModel().getSelectedItem();

        if (r == null) {
            showError("Selectati o reteta pentru produs.");
            return;
        }

        Categorie categorie = comboProdCategorie.getValue();
        if (categorie == null) {
            showError("Selectati categoria produsului.");
            return;
        }
        if (esteCategorieAll(categorie)) {
            showError("Categoria ALL este disponibila doar pentru filtrare.");
            return;
        }

        Tip tip = comboProdTip.getValue();
        if (tip == null) {
            showError("Selectati tipul produsului.");
            return;
        }
        if (esteTipAll(tip)) {
            showError("Tipul ALL este disponibil doar pentru filtrare.");
            return;
        }

        try {
            double price = Double.parseDouble(txtProdPrice.getText());

            Product p = new Product(
                    r.getId(),
                    txtProdName.getText(),
                    price,
                    categorie,
                    tip
            );

            service.addProduct(p);
            clearProductFields();
            initData();

        } catch (NumberFormatException e) {
            showError("Pretul trebuie sa fie numar.");
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void onUpdateProduct() {
        Product selected = productTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showError("Selectati un produs.");
            return;
        }

        Categorie categorie = comboProdCategorie.getValue();
        if (categorie == null) {
            showError("Selectati categoria produsului.");
            return;
        }
        if (esteCategorieAll(categorie)) {
            showError("Categoria ALL este disponibila doar pentru filtrare.");
            return;
        }

        Tip tip = comboProdTip.getValue();
        if (tip == null) {
            showError("Selectati tipul produsului.");
            return;
        }
        if (esteTipAll(tip)) {
            showError("Tipul ALL este disponibil doar pentru filtrare.");
            return;
        }

        try {
            double price = Double.parseDouble(txtProdPrice.getText());

            service.updateProduct(
                    selected.getId(),
                    txtProdName.getText(),
                    price,
                    categorie,
                    tip
            );

            clearProductFields();
            initData();

        } catch (NumberFormatException e) {
            showError("Pretul trebuie sa fie numar.");
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void onDeleteProduct() {
        Product selected = productTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showError("Selectati un produs.");
            return;
        }

        try {
            service.deleteProduct(selected.getId());
            clearProductFields();
            initData();
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void onFilterCategorie() {
        Categorie categorie = comboProdCategorie.getValue();

        if (categorie == null) {
            showError("Selectati o categorie pentru filtrare.");
            return;
        }

        if (esteCategorieAll(categorie)) {
            productList.setAll(service.getAllProducts());
            return;
        }

        productList.setAll(service.filtreazaDupaCategorie(categorie));
    }

    @FXML
    private void onFilterTip() {
        Tip tip = comboProdTip.getValue();

        if (tip == null) {
            showError("Selectati un tip pentru filtrare.");
            return;
        }

        if (esteTipAll(tip)) {
            productList.setAll(service.getAllProducts());
            return;
        }

        productList.setAll(service.filtreazaDupaTip(tip));
    }

    // ---------- RETETA NOUA ----------
    @FXML
    private void onAddNewIngred() {
        String ingredient = txtNewIngredName.getText();

        if (ingredient == null || ingredient.isBlank()) {
            showError("Introduceti numele ingredientului.");
            return;
        }

        if (!service.ingredientExistaInStoc(ingredient)) {
            showError("Ingredientul nu exista in stoc.");
            return;
        }

        try {
            double cantitate = Double.parseDouble(txtNewIngredCant.getText());

            newRetetaList.add(new IngredientReteta(ingredient, cantitate));

            txtNewIngredName.clear();
            txtNewIngredCant.clear();

        } catch (NumberFormatException e) {
            showError("Cantitatea trebuie sa fie numar.");
        }
    }

    @FXML
    private void onDeleteNewIngred() {
        IngredientReteta sel = newRetetaTable.getSelectionModel().getSelectedItem();

        if (sel == null) {
            showError("Selectati un ingredient din reteta.");
            return;
        }

        newRetetaList.remove(sel);
    }

    @FXML
    private void onAddNewReteta() {
        if (newRetetaList.isEmpty()) {
            showError("Reteta trebuie sa contina cel putin un ingredient.");
            return;
        }

        try {
            Reteta r = new Reteta(
                    service.getAllRetete().size() + 1,
                    new ArrayList<>(newRetetaList)
            );

            service.addReteta(r);

            newRetetaList.clear();
            initData();

        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void onClearNewRetetaIngredients() {
        newRetetaList.clear();
        txtNewIngredName.clear();
        txtNewIngredCant.clear();
    }

    // ---------- CURRENT ORDER ----------
    @FXML
    private void onAddOrderItem() {
        Product selected = productTable.getSelectionModel().getSelectedItem();
        Integer qty = comboQty.getValue();

        if (selected == null) {
            showError("Selectati un produs din lista.");
            return;
        }

        if (qty == null) {
            showError("Selectati cantitatea.");
            return;
        }

        currentOrderItems.add(new OrderItem(selected, qty));
        updateOrderTotal();
    }

    @FXML
    private void onDeleteOrderItem() {
        OrderItem sel = currentOrderTable.getSelectionModel().getSelectedItem();

        if (sel == null) {
            showError("Selectati un produs din comanda.");
            return;
        }

        currentOrderItems.remove(sel);
        updateOrderTotal();
    }

    @FXML
    private void onFinalizeOrder() {
        currentOrder.getItems().clear();
        currentOrder.getItems().addAll(currentOrderItems);

        try {
            for (OrderItem item : currentOrder.getItems()) {
                for (int i = 0; i < item.getQuantity(); i++) {
                    service.comandaProdus(item.getProduct());
                }
            }

            double total = service.computeTotal(currentOrder);
            currentOrder.setTotalPrice(total);

            service.addOrder(currentOrder);

            txtReceipt.setText(service.generateReceipt(currentOrder));

            currentOrderItems.clear();
            currentOrder = new Order(currentOrder.getId() + 1);

            initData();
            updateOrderTotal();

        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    private void updateOrderTotal() {
        currentOrder.getItems().clear();
        currentOrder.getItems().addAll(currentOrderItems);

        if (currentOrderItems.isEmpty()) {
            lblOrderTotal.setText("Total: 0.00");
            return;
        }

        double total = service.computeTotal(currentOrder);
        lblOrderTotal.setText(String.format("Total: %.2f", total));
    }

    // ---------- EXPORT + REVENUE ----------
    @FXML
    private void onExportOrdersCsv() {
        try {
            service.exportCsv("orders.csv");
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void onDailyRevenue() {
        lblTotalRevenue.setText(String.format("Daily Revenue: %.2f", service.getDailyRevenue()));
    }

    // ---------- CATEGORII ----------
    @FXML
    private void onAddCategorie() {
        String nume = txtCatName.getText();

        if (nume == null || nume.isBlank()) {
            showError("Introduceti numele categoriei.");
            return;
        }

        if ("ALL".equalsIgnoreCase(nume.trim())) {
            showError("Categoria ALL este disponibila doar pentru filtrare.");
            return;
        }

        try {
            int newId = service.getAllCategorii().stream()
                    .mapToInt(Categorie::getId)
                    .max()
                    .orElse(0) + 1;

            Categorie categorie = new Categorie(newId, nume.trim());
            service.addCategorie(categorie);

            txtCatName.clear();
            initData();

        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void onUpdateCategorie() {
        Categorie selected = categorieTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showError("Selectati o categorie.");
            return;
        }

        String nume = txtCatName.getText();

        if (nume == null || nume.isBlank()) {
            showError("Introduceti numele categoriei.");
            return;
        }

        if ("ALL".equalsIgnoreCase(nume.trim())) {
            showError("Categoria ALL este disponibila doar pentru filtrare.");
            return;
        }

        try {
            Categorie categorieNoua = new Categorie(selected.getId(), nume.trim());
            service.updateCategorie(categorieNoua);

            txtCatName.clear();
            initData();

        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void onDeleteCategorie() {
        Categorie selected = categorieTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showError("Selectati o categorie.");
            return;
        }

        try {
            service.deleteCategorie(selected.getId());
            txtCatName.clear();
            initData();
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    // ---------- TIPURI ----------
    @FXML
    private void onAddTip() {
        String nume = txtTipName.getText();

        if (nume == null || nume.isBlank()) {
            showError("Introduceti numele tipului.");
            return;
        }

        if ("ALL".equalsIgnoreCase(nume.trim())) {
            showError("Tipul ALL este disponibil doar pentru filtrare.");
            return;
        }

        try {
            int newId = service.getAllTipuri().stream()
                    .mapToInt(Tip::getId)
                    .max()
                    .orElse(0) + 1;

            Tip tip = new Tip(newId, nume.trim());
            service.addTip(tip);

            txtTipName.clear();
            initData();

        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void onUpdateTip() {
        Tip selected = tipTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showError("Selectati un tip.");
            return;
        }

        String nume = txtTipName.getText();

        if (nume == null || nume.isBlank()) {
            showError("Introduceti numele tipului.");
            return;
        }

        if ("ALL".equalsIgnoreCase(nume.trim())) {
            showError("Tipul ALL este disponibil doar pentru filtrare.");
            return;
        }

        try {
            Tip tipNou = new Tip(selected.getId(), nume.trim());
            service.updateTip(tipNou);

            txtTipName.clear();
            initData();

        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void onDeleteTip() {
        Tip selected = tipTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showError("Selectati un tip.");
            return;
        }

        try {
            service.deleteTip(selected.getId());
            txtTipName.clear();
            initData();
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        alert.showAndWait();
    }
}