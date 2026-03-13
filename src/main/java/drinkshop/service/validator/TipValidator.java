package drinkshop.service.validator;

import drinkshop.domain.Tip;

public class TipValidator implements Validator<Tip> {

    @Override
    public void validate(Tip tip) {
        String errors = "";

        if (tip == null) {
            errors += "Tipul nu poate fi null!\n";
        } else {
            if (tip.getId() <= 0) {
                errors += "ID tip invalid!\n";
            }

            if (tip.getNume() == null || tip.getNume().isBlank()) {
                errors += "Numele tipului nu poate fi gol!\n";
            }
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }
}