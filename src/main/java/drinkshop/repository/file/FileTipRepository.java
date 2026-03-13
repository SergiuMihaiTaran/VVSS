package drinkshop.repository.file;

import drinkshop.domain.Tip;

public class FileTipRepository
        extends FileAbstractRepository<Integer, Tip> {

    public FileTipRepository(String fileName) {
        super(fileName);
        loadFromFile();
    }

    @Override
    protected Integer getId(Tip entity) {
        return entity.getId();
    }

    @Override
    protected Tip extractEntity(String line) {
        String[] elems = line.split(",");

        int id = Integer.parseInt(elems[0]);
        String nume = elems[1];

        return new Tip(id, nume);
    }

    @Override
    protected String createEntityAsString(Tip entity) {
        return entity.getId() + "," + entity.getNume();
    }
}