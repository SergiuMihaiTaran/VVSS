package drinkshop.receipt;

import drinkshop.domain.Order;
import drinkshop.domain.OrderItem;
import drinkshop.domain.Product;

import java.util.List;

public class ReceiptGenerator {
    public static String generate(Order o, List<Product> products) {
        StringBuilder sb = new StringBuilder();
        sb.append("===== BON FISCAL =====\n")
                .append("Comanda #")
                .append(o.getId())
                .append("\n");

        for (OrderItem i : o.getItems()) {
            Product p = products.stream()
                    .filter(p1 -> i.getProduct().getId() == p1.getId())
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException(
                            "Produs inexistent pentru ID: " + i.getProduct().getId()
                    ));

            double subtotal = p.getPret() * i.getQuantity();

            sb.append(p.getNume())
                    .append(": ")
                    .append(String.format("%.2f", p.getPret()))
                    .append(" x ")
                    .append(i.getQuantity())
                    .append(" = ")
                    .append(String.format("%.2f", subtotal))
                    .append(" RON\n");
        }

        sb.append("---------------------\n")
                .append("TOTAL: ")
                .append(String.format("%.2f", o.getTotalPrice()))
                .append(" RON\n")
                .append("=====================\n");

        return sb.toString();
    }
}