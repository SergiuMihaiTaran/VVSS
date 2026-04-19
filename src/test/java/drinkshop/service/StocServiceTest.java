package drinkshop.service;

import drinkshop.domain.IngredientReteta;
import drinkshop.domain.Reteta;
import drinkshop.domain.Stoc;
import drinkshop.repository.file.FileStocRepository;
import drinkshop.service.validator.RetetaValidator;
import drinkshop.service.validator.StocValidator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StocServiceTest {

    private FileStocRepository fileStocRepository;
    private StocService stocService;

    private final String filePath = "src/test/java/drinkshop/service/testStoc.txt";

    @BeforeEach
    void setUp() throws IOException {
        FileWriter writer = new FileWriter(filePath, false);
        writer.write("1;Zahar;10;2\n");
        writer.write("2;Lapte;20;5\n");
        writer.write("3;Cafea;15;3\n");
        writer.close();

        fileStocRepository = new FileStocRepository(filePath);
        stocService = new StocService(fileStocRepository, new StocValidator(), new RetetaValidator());
    }

    @AfterEach
    void tearDown() throws IOException {
        FileWriter writer = new FileWriter(filePath, false);
        writer.write("");
        writer.close();
    }

    @Test
    @DisplayName("F02_TC01: reteta null")
    void testConsuma_NullReteta() {
        assertThrows(IllegalArgumentException.class,
                () -> stocService.consuma(null));
    }

    @Test
    @DisplayName("F02_TC02: lista ingrediente goala")
    void testConsuma_EmptyIngredients() {
        Reteta reteta = new Reteta(1, new ArrayList<>());

        assertThrows(IllegalArgumentException.class,
                () -> stocService.consuma(reteta));
    }

    @Test
    @DisplayName("F02_TC03: stoc insuficient")
    void testConsuma_StocInsuficient() {
        List<IngredientReteta> ingrediente = new ArrayList<>();
        ingrediente.add(new IngredientReteta("Zahar", 20));

        Reteta reteta = new Reteta(1, ingrediente);

        assertThrows(IllegalStateException.class,
                () -> stocService.consuma(reteta));
    }

    @Test
    @DisplayName("F02_TC04: poateConsuma este false")
    void testConsuma_PoateConsumaFalse() {
        List<IngredientReteta> ingrediente = new ArrayList<>();
        ingrediente.add(new IngredientReteta("Zahar", 9));

        Reteta reteta = new Reteta(1, ingrediente);

        assertThrows(IllegalStateException.class,
                () -> stocService.consuma(reteta));
    }

    @Test
    @DisplayName("F02_TC05: cantitate negativa")
    void testConsuma_NegativeQuantity() {
        List<IngredientReteta> ingrediente = new ArrayList<>();
        ingrediente.add(new IngredientReteta("Zahar", -1));

        Reteta reteta = new Reteta(1, ingrediente);

        assertThrows(IllegalArgumentException.class,
                () -> stocService.consuma(reteta));
    }

    @Test
    @DisplayName("F02_TC06: nume ingredient null")
    void testConsuma_NullIngredientName() {
        List<IngredientReteta> ingrediente = new ArrayList<>();
        ingrediente.add(new IngredientReteta(null, 5));

        Reteta reteta = new Reteta(1, ingrediente);

        assertThrows(IllegalArgumentException.class,
                () -> stocService.consuma(reteta));
    }

    @Test
    @DisplayName("F02_TC07: nume ingredient blank")
    void testConsuma_BlankIngredientName() {
        List<IngredientReteta> ingrediente = new ArrayList<>();
        ingrediente.add(new IngredientReteta(" ", 5));

        Reteta reteta = new Reteta(1, ingrediente);

        assertThrows(IllegalArgumentException.class,
                () -> stocService.consuma(reteta));
    }

    @Test
    @DisplayName("F02_TC08: succes")
    void testConsuma_Success() {
        List<IngredientReteta> ingrediente = new ArrayList<>();
        ingrediente.add(new IngredientReteta("Zahar", 2));

        Reteta reteta = new Reteta(1, ingrediente);

        assertDoesNotThrow(() -> stocService.consuma(reteta));

        Stoc zahar = fileStocRepository.findOne(1);
        assertEquals(8.0, zahar.getCantitate(), 0.001);
    }
}