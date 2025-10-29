package org.mthree.flooringmastery.dao;

import org.junit.jupiter.api.Test;
import org.mthree.flooringmastery.dao.OrderDao;
import org.mthree.flooringmastery.dao.OrderDaoFileImpl;
import org.mthree.flooringmastery.model.Order;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class OrderDaoStubImplTest {

    @Test
    void add_get_edit_remove_and_nextNumber() {
        OrderDao dao = new OrderDaoFileImpl();

        assertEquals(1, dao.getNextOrderNumber());

        LocalDate d = LocalDate.now().plusDays(1);
        Order o = new Order();
        o.setOrderNumber(1);
        o.setOrderDate(d);
        o.setCustomerName("A");
        o.setState("TX");
        o.setProductType("Tile");
        o.setArea(new BigDecimal("100"));

        dao.addOrder(o);
        assertEquals(2, dao.getNextOrderNumber());

        Order fetched = dao.getOrder(d, 1);
        assertNotNull(fetched);

        // mutate and "edit"
        fetched.setArea(new BigDecimal("120"));
        dao.editOrder(d, 1);
        assertEquals(new BigDecimal("120"), dao.getOrder(d, 1).getArea());

        // remove
        Order removed = dao.removeOrder(d, 1);
        assertNotNull(removed);
        assertNull(dao.getOrder(d, 1));
    }
}
