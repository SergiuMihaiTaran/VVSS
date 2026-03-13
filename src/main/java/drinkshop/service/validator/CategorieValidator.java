package drinkshop.service.validator;

import drinkshop.domain.Categorie;

public class CategorieValidator implements Validator<Categorie> {

    @Override
    public void validate(Categorie categorie) {
        String errors = "";

        if (categorie == null) {
            errors += "Categoria nu poate fi null!\n";
        } else {
            if (categorie.getId() <= 0) {
                errors += "ID categorie invalid!\n";
            }

            if (categorie.getNume() == null || categorie.getNume().isBlank()) {
                errors += "Numele categoriei nu poate fi gol!\n";
            }
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }
}
