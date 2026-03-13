package drinkshop.service;

import drinkshop.domain.IngredientReteta;
import drinkshop.domain.Reteta;
import drinkshop.domain.Stoc;
import drinkshop.repository.IRepository;
import drinkshop.service.validator.Validator;

import java.util.List;

public class StocService {

    private final IRepository<Integer, Stoc> stocRepo;
    private final Validator<Stoc> stocValidator;
    private final Validator<Reteta> retetaValidator;

    public StocService(IRepository<Integer, Stoc> stocRepo,
                       Validator<Stoc> stocValidator,
                       Validator<Reteta> retetaValidator) {
        this.stocRepo = stocRepo;
        this.stocValidator = stocValidator;
        this.retetaValidator = retetaValidator;
    }

    public List<Stoc> getAll() {
        return stocRepo.findAll();
    }

    public void add(Stoc s) {
        stocValidator.validate(s);
        stocRepo.save(s);
    }

    public void update(Stoc s) {
        stocValidator.validate(s);
        stocRepo.update(s);
    }

    public void delete(int id) {
        stocRepo.delete(id);
    }

    public boolean areSuficient(Reteta reteta) {
        retetaValidator.validate(reteta);

        List<IngredientReteta> ingredienteNecesare = reteta.getIngrediente();

        for (IngredientReteta e : ingredienteNecesare) {
            String ingredient = e.getDenumire();
            double necesar = e.getCantitate();

            double disponibil = stocRepo.findAll().stream()
                    .filter(s -> s.getIngredient().equalsIgnoreCase(ingredient))
                    .mapToDouble(Stoc::getCantitate)
                    .sum();

            if (disponibil < necesar) {
                return false;
            }
        }
        return true;
    }

    public void consuma(Reteta reteta) {
        retetaValidator.validate(reteta);

        if (!poateConsumaFaraSaScadaSubMinim(reteta)) {
            throw new IllegalStateException("Nu se poate consuma: stocul ar scadea sub stocul minim.");
        }

        for (IngredientReteta e : reteta.getIngrediente()) {
            String ingredient = e.getDenumire();
            double necesar = e.getCantitate();

            List<Stoc> ingredienteStoc = stocRepo.findAll().stream()
                    .filter(s -> s.getIngredient().equalsIgnoreCase(ingredient))
                    .toList();

            double ramas = necesar;

            for (Stoc s : ingredienteStoc) {
                if (ramas <= 0) break;

                double maximDeConsum = s.getCantitate() - s.getStocMinim();
                if (maximDeConsum <= 0) {
                    continue;
                }

                double deScazut = Math.min(maximDeConsum, ramas);
                s.setCantitate(s.getCantitate() - deScazut);
                ramas -= deScazut;

                stocValidator.validate(s);
                stocRepo.update(s);
            }

            if (ramas > 0) {
                throw new IllegalStateException("Nu exista suficient stoc disponibil fara a cobori sub minim pentru ingredientul: " + ingredient);
            }
        }
    }

    public boolean poateConsumaFaraSaScadaSubMinim(Reteta reteta) {
        retetaValidator.validate(reteta);

        for (IngredientReteta e : reteta.getIngrediente()) {
            String ingredient = e.getDenumire();
            double necesar = e.getCantitate();

            List<Stoc> stocuriIngredient = stocRepo.findAll().stream()
                    .filter(s -> s.getIngredient().equalsIgnoreCase(ingredient))
                    .toList();

            double disponibil = stocuriIngredient.stream()
                    .mapToDouble(Stoc::getCantitate)
                    .sum();

            double stocMinimTotal = stocuriIngredient.stream()
                    .mapToDouble(Stoc::getStocMinim)
                    .sum();

            if (disponibil - necesar < stocMinimTotal) {
                return false;
            }
        }

        return true;
    }
}