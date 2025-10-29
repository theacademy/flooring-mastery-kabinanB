package org.mthree.flooringmastery.dao;

import org.junit.jupiter.api.Test;
import org.mthree.flooringmastery.dao.OrderDao;
import org.mthree.flooringmastery.model.Order;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class OrderDaoTest {
    OrderDao orderTestDao;


    LocalDate d1 = LocalDate.of(2025, 10, 30);
    LocalDate d2 = LocalDate.of(2025, 11, 1);
    @Test
    public void testAddGetNextOrder() throws Exception
    {
        Order order = new Order();

        int start = orderTestDao.getNextOrderNumber();

        getOrder("Kabinan Balakrishnan", start);

        orderTestDao.addOrder(order);

        int afterFirst = orderTestDao.getNextOrderNumber();
        assertEquals(start+1, afterFirst);

        Order order2 = getOrder("Micheal Jackson", afterFirst);

        orderTestDao.addOrder(order2);
        int afterSecond = orderTestDao.getNextOrderNumber();
        assertEquals(start+2, afterSecond);


    }


    private static Order getOrder(String name, int start) {
        Order order = new Order();

        order.setOrderNumber(start);
        order.setCustomerName(name);
        order.setState("TX");
        order.setTaxRate(new BigDecimal("4.45"));
        order.setProductType("Tile");
        order.setArea(new BigDecimal("150"));
        order.setCostPerSquareFoot(new BigDecimal("3.50"));
        order.setLaborCostPerSquareFoot(new BigDecimal("4.15"));
        // If DAO persists calculated fields as-is, set them here (service normally calculates):
        order.setMaterialCost(new BigDecimal("525.00"));
        order.setLaborCost(new BigDecimal("622.50"));
        order.setTax(new BigDecimal("51.06"));
        order.setTotal(new BigDecimal("1198.56"));
        return order;
    }

    @Test
    public void testAddGetOrder() throws Exception
    {
        int num = orderTestDao.getNextOrderNumber();
        Order newOrder = getOrder("Nicola Barella", num);

        orderTestDao.addOrder(newOrder);

        Order anotherOrder = orderTestDao.getOrder(d1,num);
        assertEquals("Nicola Barella", anotherOrder.getCustomerName());
        assertEquals(new BigDecimal("1198.56"), anotherOrder.getTotal());
    }

    @Test
    public void testAddEditOrder() throws Exception
    {
        int num = orderTestDao.getNextOrderNumber();
        Order in = getOrder("Dan Vitchev", num);
        orderTestDao.addOrder(in);

        Order edited = getOrder("Daniel Shaw", num); // change a field
        orderTestDao.editOrder(d1,edited.getOrderNumber());

        Order out = orderTestDao.getOrder(d1, num);
        assertEquals("Daniel Shaw", out.getCustomerName());


    }

    @Test
    public void testAddGetOrderdForDate() throws Exception
    {
        int n1 = orderTestDao.getNextOrderNumber();
        orderTestDao.addOrder(getOrder("Eve Morrisons", n1));

        int n2 = orderTestDao.getNextOrderNumber();
        orderTestDao.addOrder(getOrder( "Frank Gemini", n2));

        List<Order> list = orderTestDao.getOrdersForDate(d1);
        assertEquals(2, list.size());
        assertTrue(list.stream().anyMatch(o -> o.getCustomerName().equals("Eve Morrisons")));
        assertTrue(list.stream().anyMatch(o -> o.getCustomerName().equals("Frank Gemini")));

    }

    @Test
    public void testAddGetAllOrders()
    {
        int n1 = orderTestDao.getNextOrderNumber();
        orderTestDao.addOrder(getOrder("Gina Topper", n1));

        int n2 = orderTestDao.getNextOrderNumber();
        orderTestDao.addOrder(getOrder("Hank Wilson", n2));

        Map<LocalDate, Map<Integer, Order>> all = orderTestDao.getAllOrders();
        assertEquals(2, all.size());
        assertTrue(all.containsKey(d1));
        assertTrue(all.containsKey(d2));
        assertEquals(1, all.get(d1).size());
        assertEquals(1, all.get(d2).size());


    }

    @Test
    public void testAddRemoveOrder()
    {
        int num = orderTestDao.getNextOrderNumber();
        orderTestDao.addOrder(getOrder( "Ivy Night", num));

        Order removed = orderTestDao.removeOrder(d1, num);
        assertNotNull(removed);

        assertNull(orderTestDao.getOrder(d1, num));
        List<Order> list = orderTestDao.getOrdersForDate(d1);
        assertTrue(list.isEmpty());

    }


}
