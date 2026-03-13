package drinkshop.reports;

import drinkshop.domain.Order;

import java.util.List;

public class DailyReportService {

    public double getTotalRevenue(List<Order> ordersDinSesiune) {
        if (ordersDinSesiune == null || ordersDinSesiune.isEmpty()) {
            return 0.0;
        }

        return ordersDinSesiune.stream()
                .mapToDouble(Order::getTotalPrice)
                .sum();
    }
}