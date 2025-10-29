package org.mthree.flooringmastery.service;

import java.time.LocalDate;
import java.util.List;

import org.mthree.flooringmastery.model.Order;
import org.mthree.flooringmastery.model.Tax;
import org.mthree.flooringmastery.model.Product;

public interface FlooringMasteryServiceLayer {

    public int getNextOrderNumber();

    public Order addOrder(Order orderNumber);

    public Order getOrder(LocalDate date, int orderNumber);

    public Order editOrder(LocalDate date, int orderNumber);

    public List<Order> getOrdersForDate(LocalDate date);

    public Order removeOrder(LocalDate date, int orderNumber);

    public void exportData();

    public List<Tax> getTaxes();

    public List<Product> getProducts();

}
