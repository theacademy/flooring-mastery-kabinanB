package org.mthree.flooringmastery.dao;


import org.mthree.flooringmastery.model.Order;

import java.time.LocalDate;
import java.util.*;

public class OrderDaoStubImpl implements OrderDao {
    private final Map<LocalDate, Map<Integer, Order>> store = new LinkedHashMap<>();
    private int max = 0;

    @Override
    public int getNextOrderNumber() { return max + 1; }

    @Override
    public Order addOrder(Order order) {
        store.computeIfAbsent(order.getOrderDate(), d -> new LinkedHashMap<>())
                .put(order.getOrderNumber(), order);
        max = Math.max(max, order.getOrderNumber());
        return order;
    }

    @Override
    public Order getOrder(LocalDate date, int orderNumber) {
        return store.getOrDefault(date, Map.of()).get(orderNumber);
    }

    @Override
    public Order editOrder(LocalDate date, int orderNumber) {
        return getOrder(date, orderNumber); // already in-memory; service mutates object
    }

    @Override
    public List<Order> getOrdersForDate(LocalDate date) {
        return new ArrayList<>(store.getOrDefault(date, Map.of()).values());
    }

    @Override
    public Map<LocalDate, Map<Integer, Order>> getAllOrders() {
        return store;
    }

    @Override
    public Order removeOrder(LocalDate date, int orderNumber) {
        Map<Integer, Order> m = store.get(date);
        if (m == null) return null;
        return m.remove(orderNumber);
    }
}

