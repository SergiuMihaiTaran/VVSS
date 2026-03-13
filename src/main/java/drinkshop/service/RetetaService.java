package drinkshop.service;

import drinkshop.domain.Reteta;
import drinkshop.repository.IRepository;
import drinkshop.service.validator.Validator;

import java.util.List;

public class RetetaService {

    private final IRepository<Integer, Reteta> retetaRepo;
    private final Validator<Reteta> retetaValidator;

    public RetetaService(IRepository<Integer, Reteta> retetaRepo,
                         Validator<Reteta> retetaValidator) {
        this.retetaRepo = retetaRepo;
        this.retetaValidator = retetaValidator;
    }

    public void addReteta(Reteta r) {
        retetaValidator.validate(r);
        retetaRepo.save(r);
    }

    public void updateReteta(Reteta r) {
        retetaValidator.validate(r);
        retetaRepo.update(r);
    }

    public void deleteReteta(int id) {
        retetaRepo.delete(id);
    }

    public Reteta findById(int id) {
        return retetaRepo.findOne(id);
    }

    public List<Reteta> getAll() {
        return retetaRepo.findAll();
    }
}