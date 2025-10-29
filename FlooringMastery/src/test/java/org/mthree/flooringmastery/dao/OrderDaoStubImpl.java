package org.mthree.flooringmastery.dao;

import org.mthree.flooringmastery.model.Order;
import java.time.LocalDate;
import java.util.*;

public class OrderDaoStubImpl implements OrderDao {
    private final Map<LocalDate, Map<Integer, Order>> orders = new HashMap<>();

    @Override
    public int getNextOrderNumber() {
        return orders.values().stream()
                .flatMap(m -> m.keySet().stream())
                .max(Integer::compareTo)
                .orElse(0) + 1;
    }

    @Override
    public Order addOrder(Order order) {
        orders.computeIfAbsent(order.getOrderDate(), d -> new HashMap<>())
                .put(order.getOrderNumber(), order);
        return order;
    }

    @Override
    public Order getOrder(LocalDate date, int orderNumber) {
        return orders.getOrDefault(date, Collections.emptyMap()).get(orderNumber);
    }

    @Override
    public Order editOrder(LocalDate date, int orderNumber) {
        return getOrder(date, orderNumber);
    }

    @Override
    public List<Order> getOrdersForDate(LocalDate date) {
        return new ArrayList<>(orders.getOrDefault(date, Collections.emptyMap()).values());
    }

    @Override
    public Map<LocalDate, Map<Integer, Order>> getAllOrders() {
        return orders;
    }

    @Override
    public Order removeOrder(LocalDate date, int orderNumber) {
        return orders.getOrDefault(date, Collections.emptyMap()).remove(orderNumber);
    }
}
