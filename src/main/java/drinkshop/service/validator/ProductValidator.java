package drinkshop.service.validator;

import drinkshop.domain.Product;

public class ProductValidator implements Validator<Product> {

    @Override
    public void validate(Product product) {

        String errors = "";

        if (product == null) {
            errors += "Produsul nu poate fi null!\n";
        } else {
            if (product.getId() <= 0)
                errors += "ID invalid!\n";

            if (product.getNume() == null || product.getNume().isBlank())
                errors += "Numele nu poate fi gol!\n";

            if (product.getPret() <= 0)
                errors += "Pret invalid!\n";

            if (product.getCategorie() == null)
                errors += "Categoria nu poate fi vida!\n";
            else if (product.getCategorie().getNume() == null || product.getCategorie().getNume().isBlank())
                errors += "Categoria este invalida!\n";
            else if ("ALL".equalsIgnoreCase(product.getCategorie().getNume()))
                errors += "Categoria ALL este folosita doar pentru filtrare!\n";

            if (product.getTip() == null)
                errors += "Tipul nu poate fi vid!\n";
            else if (product.getTip().getNume() == null || product.getTip().getNume().isBlank())
                errors += "Tipul este invalid!\n";
            else if ("ALL".equalsIgnoreCase(product.getTip().getNume()))
                errors += "Tipul ALL este folosit doar pentru filtrare!\n";
        }

        if (!errors.isEmpty())
            throw new ValidationException(errors);
    }
}