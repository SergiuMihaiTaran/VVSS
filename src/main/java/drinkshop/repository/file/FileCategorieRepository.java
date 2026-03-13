package drinkshop.repository.file;

import drinkshop.domain.Categorie;

public class FileCategorieRepository
        extends FileAbstractRepository<Integer, Categorie> {

    public FileCategorieRepository(String fileName) {
        super(fileName);
        loadFromFile();
    }

    @Override
    protected Integer getId(Categorie entity) {
        return entity.getId();
    }

    @Override
    protected Categorie extractEntity(String line) {
        String[] elems = line.split(",");

        int id = Integer.parseInt(elems[0]);
        String nume = elems[1];

        return new Categorie(id, nume);
    }

    @Override
    protected String createEntityAsString(Categorie entity) {
        return entity.getId() + "," + entity.getNume();
    }
}