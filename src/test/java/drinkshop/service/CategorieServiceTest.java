package drinkshop.service;

import drinkshop.domain.Categorie;
import drinkshop.domain.Product;
import drinkshop.domain.Tip;
import drinkshop.repository.IRepository;
import drinkshop.repository.file.FileCategorieRepository;
import drinkshop.repository.file.FileProductRepository;
import drinkshop.service.validator.CategorieValidator;
import drinkshop.service.validator.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mockito;
import java.nio.file.Files;
import java.io.IOException;

import java.nio.file.Path;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategorieServiceTest {

    private IRepository<Integer, Categorie> categorieRepo;
    private IRepository<Integer, Product> productRepo;
    private Validator<Categorie> validator;
    private CategorieService categorieService;

    @BeforeEach
    void setUp() {
        categorieRepo = mock(IRepository.class);
        productRepo = mock(IRepository.class);
        validator = mock(Validator.class);
        categorieService = new CategorieService(categorieRepo, productRepo, validator);
    }

    @Test
    void testAddCategorie_Valid() {
        Categorie cat = new Categorie(1, "Sucuri");
        categorieService.addCategorie(cat);
        verify(validator, times(1)).validate(cat);
        verify(categorieRepo, times(1)).save(cat);
    }

    @Test
    void testDeleteCategorie_ThrowsExceptionWhenProductsExist() {
        int idCatarogii = 1;
        Categorie cat = new Categorie(idCatarogii, "Bere");
        Product p = new Product(10, "Heineken", 10.0, cat, new Tip(1,"Alcool"));

        when(categorieRepo.findOne(idCatarogii)).thenReturn(cat);
        when(productRepo.findAll()).thenReturn(Collections.singletonList(p));
        assertThrows(IllegalStateException.class, () -> {
            categorieService.deleteCategorie(idCatarogii);
        });
        verify(categorieRepo, never()).delete(anyInt());
    }
}
class CategorieServiceIntegrationVTest {
    private IRepository<Integer, Categorie> categorieRepo;
    private IRepository<Integer, Product> productRepo;
    private Validator<Categorie> validator;
    private CategorieService service;

    @BeforeEach
    void setUp() {
        categorieRepo = mock(IRepository.class);
        productRepo = mock(IRepository.class);
        validator = new CategorieValidator();
        service = new CategorieService(categorieRepo, productRepo, validator);
    }

    @Test
    void testAdd_IntegrationWithRealValidator_Success() {
        Categorie cat = new Categorie(1, "Valid");
        service.addCategorie(cat);
        verify(categorieRepo).save(cat);
    }

    @Test
    void testAdd_IntegrationWithRealValidator_InvalidName() {
        Categorie catInvalida = new Categorie(1, "");
        assertThrows(RuntimeException.class, () -> service.addCategorie(catInvalida));
        verify(categorieRepo, never()).save(any());
    }
}


class CategorieServiceIntegrationRTest{
    @TempDir
    Path tempDir;

    private FileCategorieRepository categorieRepo;
    private FileProductRepository productRepo;
    private IRepository<Integer, Tip> tipRepo;
    private CategorieService service;

    @BeforeEach
    void setUp() throws IOException {
        Path catPath = tempDir.resolve("categorii.txt");
        Path prodPath = tempDir.resolve("produse.txt");

        Files.createFile(catPath);
        Files.createFile(prodPath);

        tipRepo = mock(IRepository.class);

        categorieRepo = new FileCategorieRepository(catPath.toString());
        productRepo = new FileProductRepository(prodPath.toString(), categorieRepo, tipRepo);

        Validator<Categorie> validator = new CategorieValidator();
        service = new CategorieService(categorieRepo, productRepo, validator);
    }

    @Test
    void testAdd_FullIntegration_Persistence() {
        Categorie cat = new Categorie(1, "Sucuri");
        service.addCategorie(cat);
        Categorie gasit = service.findById(1);
        assertNotNull(gasit);
        assertEquals("Sucuri", gasit.getNume());
    }

    @Test
    void testDelete_FullIntegration_FailureWhenHasProducts() {
        Categorie cat = new Categorie(1, "Alcool");
        categorieRepo.save(cat);
        Tip mockTip = mock(Tip.class);
        when(mockTip.getId()).thenReturn(1);
        when(tipRepo.findOne(1)).thenReturn(mockTip);
        Product p = new Product(10, "Bere", 5.0, cat, mockTip);
        productRepo.save(p);
        assertThrows(IllegalStateException.class, () -> service.deleteCategorie(1));
    }
}