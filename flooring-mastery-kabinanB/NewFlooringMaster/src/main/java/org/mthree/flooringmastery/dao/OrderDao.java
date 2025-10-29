package org.mthree.flooringmastery.dao;

import org.mthree.flooringmastery.model.Order;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface OrderDao {

    public int getNextOrderNumber();

    public Order addOrder(Order order);

    public Order getOrder(LocalDate date, int orderNumber);

    public Order editOrder(LocalDate date, int orderNumber);

    public List<Order> getOrdersForDate(LocalDate date);

    public Map<LocalDate, Map<Integer, Order>> getAllOrders();

    public Order removeOrder(LocalDate date, int orderNumber);




}
