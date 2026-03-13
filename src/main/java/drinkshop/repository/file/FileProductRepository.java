package drinkshop.repository.file;

import drinkshop.domain.Categorie;
import drinkshop.domain.Product;
import drinkshop.domain.Tip;
import drinkshop.repository.Repository;

public class FileProductRepository
        extends FileAbstractRepository<Integer, Product> {

    private final Repository<Integer, Categorie> categorieRepo;
    private final Repository<Integer, Tip> tipRepo;

    public FileProductRepository(String fileName,
                                 Repository<Integer, Categorie> categorieRepo,
                                 Repository<Integer, Tip> tipRepo) {
        super(fileName);
        this.categorieRepo = categorieRepo;
        this.tipRepo = tipRepo;
        loadFromFile();
    }

    @Override
    protected Integer getId(Product entity) {
        return entity.getId();
    }

    @Override
    protected Product extractEntity(String line) {

        String[] elems = line.split(",");

        int id = Integer.parseInt(elems[0]);
        String name = elems[1];
        double price = Double.parseDouble(elems[2]);
        int categorieId = Integer.parseInt(elems[3]);
        int tipId = Integer.parseInt(elems[4]);

        Categorie categorie = categorieRepo.findOne(categorieId);
        Tip tip = tipRepo.findOne(tipId);

        if (categorie == null) {
            throw new IllegalStateException("Categoria cu id " + categorieId + " nu exista.");
        }

        if (tip == null) {
            throw new IllegalStateException("Tipul cu id " + tipId + " nu exista.");
        }

        return new Product(id, name, price, categorie, tip);
    }

    @Override
    protected String createEntityAsString(Product entity) {
        return entity.getId() + "," +
                entity.getNume() + "," +
                entity.getPret() + "," +
                entity.getCategorie().getId() + "," +
                entity.getTip().getId();
    }
}