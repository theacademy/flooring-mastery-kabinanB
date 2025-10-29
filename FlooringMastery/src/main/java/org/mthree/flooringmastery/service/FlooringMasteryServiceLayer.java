//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.mthree.flooringmastery.service;

import java.time.LocalDate;
import java.util.List;
import org.mthree.flooringmastery.model.Order;
import org.mthree.flooringmastery.model.Product;
import org.mthree.flooringmastery.model.Tax;

public interface FlooringMasteryServiceLayer {
    int getNextOrderNumber();

    Order addOrder(Order var1);

    Order getOrder(LocalDate var1, int var2);

    Order editOrder(LocalDate var1, int var2);

    List<Order> getOrdersForDate(LocalDate var1);

    Order removeOrder(LocalDate var1, int var2);

    void exportData();

    List<Tax> getTaxes();

    List<Product> getProducts();
}
