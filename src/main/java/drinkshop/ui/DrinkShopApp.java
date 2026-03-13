package drinkshop.ui;

import drinkshop.domain.*;
import drinkshop.repository.IRepository;
import drinkshop.repository.file.*;
import drinkshop.service.DrinkShopService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class DrinkShopApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        IRepository<Integer, Categorie> categorieRepo = new FileCategorieRepository("data/categorii.txt");
        IRepository<Integer, Tip> tipRepo = new FileTipRepository("data/tipuri.txt");

        IRepository<Integer, Product> productRepo = new FileProductRepository("data/products.txt", categorieRepo, tipRepo);
        IRepository<Integer, Order> orderRepo = new FileOrderRepository("data/orders.txt", productRepo);
        IRepository<Integer, Reteta> retetaRepo = new FileRetetaRepository("data/retete.txt");
        IRepository<Integer, Stoc> stocRepo = new FileStocRepository("data/stocuri.txt");


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