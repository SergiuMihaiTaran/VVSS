package drinkshop.service;

import drinkshop.domain.Categorie;
import drinkshop.domain.Product;
import drinkshop.domain.Tip;
import drinkshop.repository.file.FileCategorieRepository;
import drinkshop.repository.file.FileProductRepository;
import drinkshop.repository.file.FileTipRepository;
import drinkshop.service.validator.ProductValidator;
import drinkshop.service.validator.Validator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class ProductServiceTest {
    private FileProductRepository fileProductRepository;
    private ProductService productService;
    private FileCategorieRepository fileCategorieRepository;
    private FileTipRepository fileTipRepository;
    private ProductValidator validator;
    private Categorie testCat = new Categorie(1, "Bauturi");
    private Tip testTip = new Tip(1, "Alcoolic");
    @BeforeEach
    void setUp() {

        fileCategorieRepository=new FileCategorieRepository("C:\\Users\\sergi\\Desktop\\MyDrinkShop\\src\\test\\java\\drinkshop\\service\\testCategorie.txt");
        fileTipRepository=new FileTipRepository("C:\\Users\\sergi\\Desktop\\MyDrinkShop\\src\\test\\java\\drinkshop\\service\\testTip.txt");
        fileTipRepository.save(new Tip(1,"tip"));
        fileCategorieRepository.save(new Categorie(1,"ceva"));
        fileProductRepository=new FileProductRepository("C:\\Users\\sergi\\Desktop\\MyDrinkShop\\src\\test\\java\\drinkshop\\service\\testProduct.txt",fileCategorieRepository,fileTipRepository);
        validator=new ProductValidator();
        productService=new ProductService(fileProductRepository,validator);

        productService.addProduct(new Product(1,"nume",101.1f,new Categorie(1,"ceva"),new Tip(1,"tip")));
    }
    @Test
    @DisplayName("P237-6: ECP_Valid_Update_Produs")
    void testUpdateProduct_ECP_Valid() {
        String numeNou = "NumeValid";
        float pretValid = 25.5f;
        productService.updateProduct(1, numeNou, pretValid, testCat, testTip);
        assertEquals(pretValid, productService.findById(1).getPret());
        assertEquals(numeNou, productService.findById(1).getNume());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    @DisplayName("P237-7: ECP_Invalid_Name")
    void testUpdateProduct_ECP_Invalid_Name(String invalidName) {
        assertThrows(RuntimeException.class, () ->
                productService.updateProduct(1, invalidName, 10.0f, testCat, testTip)
        );
    }
    @Test
    @DisplayName("P237-8: BVA_Valid_Price")
    void testUpdateProduct_BVA_Valid_Price() {
        float pretMinimValid = 0.01f;
        productService.updateProduct(1, "Nume", pretMinimValid, testCat, testTip);
        assertEquals(pretMinimValid, productService.findById(1).getPret());
    }

    @Test
    @DisplayName("P237-9: BVA_Invalid_Price")
    void testUpdateProduct_BVA_Invalid_Price() {
        float pretInvalid = 0.0f;
        assertThrows(RuntimeException.class, () ->
                productService.updateProduct(1, "Nume", pretInvalid, testCat, testTip)
        );

    }
    @Test
    @DisplayName("P237-8: BVA_Valid_Price")
    void testUpdateProduct_BVA_Valid_Nume() {

        productService.updateProduct(1, "1", 100f, testCat, testTip);
        assertEquals(100f, productService.findById(1).getPret());
    }

    @Test
    @DisplayName("P237-9: BVA_Invalid_Price")
    void testUpdateProduct_BVA_Invalid_Nume() {

        assertThrows(RuntimeException.class, () ->
                productService.updateProduct(1, " ", 100f, testCat, testTip)
        );

    }

}