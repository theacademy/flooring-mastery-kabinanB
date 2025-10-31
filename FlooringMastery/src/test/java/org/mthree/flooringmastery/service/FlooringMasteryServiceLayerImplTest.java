package org.mthree.flooringmastery.service;

import org.junit.jupiter.api.*;
import org.mthree.flooringmastery.dao.*;
import org.mthree.flooringmastery.model.Order;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class FlooringMasteryServiceLayerImplTest {

    ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
    FlooringMasteryServiceLayer service = ctx.getBean("serviceLayer", FlooringMasteryServiceLayer.class);

    private OrderDao orderDao = ctx.getBean("orderDaoStub", OrderDao.class);
    private AuditDaoStubImpl auditDao = ctx.getBean("auditDaoStub", AuditDaoStubImpl.class);
    private ExportDaoStubImpl exportDao = ctx.getBean("exportDaoStub", ExportDaoStubImpl.class);

    @BeforeEach
    public void setup() {

    }

    @Test
    void getNextOrderNumber_delegatesToDao() {
        assertEquals(1, service.getNextOrderNumber());
        service.addOrder(makeOrder(LocalDate.now().plusDays(1), 1, "A", "TX", "Tile", "100"));
        assertEquals(2, service.getNextOrderNumber());
    }

    @Test
    void addOrder_calculatesAndPersists_andAudits() {
        LocalDate d = LocalDate.now().plusDays(1);
        int next = service.getNextOrderNumber();
        Order saved = service.addOrder(makeOrder(d, next, "Alice", "TX", "Tile", "150"));

        assertEquals(new BigDecimal("525.00"), saved.getMaterialCost());
        assertEquals(new BigDecimal("622.50"), saved.getLaborCost());
        assertEquals(new BigDecimal("51.06"), saved.getTax());
        assertEquals(new BigDecimal("1198.56"), saved.getTotal());

        assertNotNull(orderDao.getOrder(d, next));
        assertFalse(auditDao.entries.isEmpty());
        assertTrue(auditDao.entries.get(0).toUpperCase().contains("ADD"));
    }

    @Test
    void editOrder_recalculatesOnChange_andAudits() {
        LocalDate d = LocalDate.now().plusDays(2);
        service.addOrder(makeOrder(d, 1, "Bob", "TX", "Tile", "150"));

        Order toEdit = orderDao.getOrder(d, 1);
        toEdit.setArea(new BigDecimal("200"));

        Order after = service.editOrder(d, 1);
        assertEquals(new BigDecimal("700.00"), after.getMaterialCost());
        assertEquals(new BigDecimal("830.00"), after.getLaborCost());
        assertEquals(new BigDecimal("68.09"), after.getTax());
        assertEquals(new BigDecimal("1598.09"), after.getTotal());

        assertTrue(auditDao.entries.stream().anyMatch(s -> s.toUpperCase().contains("EDIT")));
    }

    @Test
    void removeOrder_deletes_andAudits() {
        LocalDate d = LocalDate.now().plusDays(3);
        service.addOrder(makeOrder(d, 1, "Cara", "TX", "Tile", "100"));

        Order removed = service.removeOrder(d, 1);
        assertNotNull(removed);
        assertNull(orderDao.getOrder(d, 1));
        assertTrue(auditDao.entries.stream().anyMatch(s -> s.toUpperCase().contains("REMOVE")));
    }

    @Test
    void exportData_delegatesToExportDao_andAudits() {
        LocalDate d = LocalDate.now().plusDays(4);
        service.addOrder(makeOrder(d, 1, "Dan", "TX", "Tile", "100"));

        service.exportData();

        assertTrue(exportDao.called);
        assertNotNull(exportDao.snapshot);
        assertFalse(exportDao.snapshot.isEmpty());
        assertTrue(auditDao.entries.stream().anyMatch(s -> s.toUpperCase().contains("EXPORT")));
    }

    @Test
    void getTaxes_and_getProducts_passThrough() {
        LocalDate d = LocalDate.now().plusDays(4);
        service.addOrder(makeOrder(d, 1, "Dan", "TX", "Tile", "100"));

        assertEquals(1, service.getTaxes().size());
        assertEquals("TX", service.getTaxes().get(0).getStateAbbreviation());
        assertEquals(1, service.getProducts().size());
        assertEquals("Tile", service.getProducts().get(0).getProductType());
    }

    private Order makeOrder(LocalDate date, int number, String name,
                            String stateAbbrev, String productType, String areaStr) {
        Order o = new Order();
        o.setOrderDate(date);
        o.setOrderNumber(number);
        o.setCustomerName(name);
        o.setState(stateAbbrev);
        o.setProductType(productType);
        o.setArea(new BigDecimal(areaStr));
        return o;
    }
}
