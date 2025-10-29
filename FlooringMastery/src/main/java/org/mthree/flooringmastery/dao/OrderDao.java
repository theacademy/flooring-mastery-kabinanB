//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.mthree.flooringmastery.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.mthree.flooringmastery.model.Order;

public interface OrderDao {
    int getNextOrderNumber();

    Order addOrder(Order var1);

    Order getOrder(LocalDate var1, int var2);

    Order editOrder(LocalDate var1, int var2);

    List<Order> getOrdersForDate(LocalDate var1);

    Map<LocalDate, Map<Integer, Order>> getAllOrders();

    Order removeOrder(LocalDate var1, int var2);
}
