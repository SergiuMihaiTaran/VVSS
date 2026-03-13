package drinkshop.ui;

import drinkshop.domain.*;
import drinkshop.repository.Repository;
import drinkshop.repository.file.*;
import drinkshop.service.DrinkShopService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class DrinkShopApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        Repository<Integer, Categorie> categorieRepo = new FileCategorieRepository("data/categorii.txt");
        Repository<Integer, Tip> tipRepo = new FileTipRepository("data/tipuri.txt");

        Repository<Integer, Product> productRepo = new FileProductRepository("data/products.txt", categorieRepo, tipRepo);
        Repository<Integer, Order> orderRepo = new FileOrderRepository("data/orders.txt", productRepo);
        Repository<Integer, Reteta> retetaRepo = new FileRetetaRepository("data/retete.txt");
        Repository<Integer, Stoc> stocRepo = new FileStocRepository("data/stocuri.txt");


        DrinkShopService service = new DrinkShopService(
                productRepo,
                orderRepo,
                retetaRepo,
                stocRepo,
                categorieRepo,
                tipRepo
        );

        FXMLLoader loader = new FXMLLoader(getClass().getResource("drinkshop.fxml"));
        Scene scene = new Scene(loader.load());

        DrinkShopController controller = loader.getController();
        controller.setService(service);

        stage.setTitle("Coffee Shop Management");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}