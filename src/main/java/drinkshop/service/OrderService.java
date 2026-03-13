package drinkshop.service;

import drinkshop.domain.Order;
import drinkshop.domain.OrderItem;
import drinkshop.domain.Product;
import drinkshop.repository.Repository;
import drinkshop.service.validator.Validator;

import java.util.List;

public class OrderService {

    private final Repository<Integer, Order> orderRepo;
    private final Repository<Integer, Product> productRepo;
    private final Validator<Order> orderValidator;
    private final Validator<OrderItem> orderItemValidator;

    public OrderService(Repository<Integer, Order> orderRepo,
                        Repository<Integer, Product> productRepo,
                        Validator<Order> orderValidator,
                        Validator<OrderItem> orderItemValidator) {
        this.orderRepo = orderRepo;
        this.productRepo = productRepo;
        this.orderValidator = orderValidator;
        this.orderItemValidator = orderItemValidator;
    }

    public void addOrder(Order o) {
        orderValidator.validate(o);
        orderRepo.save(o);
    }

    public void updateOrder(Order o) {
        orderValidator.validate(o);
        orderRepo.update(o);
    }

    public void deleteOrder(int id) {
        orderRepo.delete(id);
    }

    public List<Order> getAllOrders() {
        return orderRepo.findAll();
    }

    public Order findById(int id) {
        return orderRepo.findOne(id);
    }

    public double computeTotal(Order o) {
        if (o == null) {
            throw new IllegalArgumentException("Comanda nu poate fi null.");
        }

        if (o.getItems() == null || o.getItems().isEmpty()) {
            return 0.0;
        }

        return o.getItems().stream()
                .mapToDouble(i -> {
                    orderItemValidator.validate(i);

                    Product p = productRepo.findOne(i.getProduct().getId());
                    if (p == null) {
                        throw new IllegalArgumentException("Produs inexistent pentru ID: " + i.getProduct().getId());
                    }

                    return p.getPret() * i.getQuantity();
                })
                .sum();
    }

    public void addItem(Order o, OrderItem item) {
        orderItemValidator.validate(item);
        o.getItems().add(item);
        orderValidator.validate(o);
        orderRepo.update(o);
    }

    public void removeItem(Order o, OrderItem item) {
        orderItemValidator.validate(item);
        o.getItems().remove(item);
        orderValidator.validate(o);
        orderRepo.update(o);
    }
}