

package org.mthree.flooringmastery.service;

import org.junit.jupiter.api.*;
import org.mthree.flooringmastery.dao.*;
import org.mthree.flooringmastery.model.Order;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FlooringMasteryServiceLayerImplTest {

    private FlooringMasteryServiceLayer service;

    // Pull real stub beans so we can assert on their internal state
    private OrderDaoStubImpl orderDao;
    private AuditDaoStubImpl auditDao;
    private ExportDaoStubImpl exportDao;

    @BeforeEach
    void initSpring() {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        service   = ctx.getBean("serviceLayer", FlooringMasteryServiceLayer.class);
        orderDao  = (OrderDaoStubImpl)  ctx.getBean("orderDaoStub");
        auditDao  = (AuditDaoStubImpl)  ctx.getBean("auditDaoStub");
        exportDao = (ExportDaoStubImpl) ctx.getBean("exportDaoStub");
    }

    // ===== helper =====
    private Order makeOrder(LocalDate date, int number, String name,
                            String stateAbbrev, String productType, String areaStr) {
        Order o = new Order();
        o.setOrderDate(date);
        o.setOrderNumber(number);
        o.setCustomerName(name);
        o.setState(stateAbbrev);        // service resolves tax rate from Tax DAO
        o.setProductType(productType);  // service resolves costs from Product DAO
        o.setArea(new BigDecimal(areaStr));
        return o;
    }

    // ===== tests =====

    @Test
    void getNextOrderNumber_delegatesToDao() {
        assertEquals(1, service.getNextOrderNumber()); // empty -> 1
        service.addOrder(makeOrder(LocalDate.now().plusDays(1), 1, "A", "TX", "Tile", "100"));
        assertEquals(2, service.getNextOrderNumber());
    }

    @Test
    void addOrder_calculatesAndPersists_andAudits() {
        LocalDate d = LocalDate.now().plusDays(1);
        int next = service.getNextOrderNumber();

        Order saved = service.addOrder(makeOrder(d, next, "Alice", "TX", "Tile", "150"));

        // Material = 150 * 3.50 = 525.00
        assertEquals(new BigDecimal("525.00"), saved.getMaterialCost());
        // Labor = 150 * 4.15 = 622.50
        assertEquals(new BigDecimal("622.50"), saved.getLaborCost());
        // Tax = (525 + 622.50) * 0.0445 = 51.12
        assertEquals(new BigDecimal("51.06"), saved.getTax());
        // Total = 1198.62
        assertEquals(new BigDecimal("1198.56"), saved.getTotal());

        // persisted
        assertNotNull(orderDao.getOrder(d, next));
        // audited
        assertFalse(auditDao.entries.isEmpty());
        assertTrue(auditDao.entries.get(0).toUpperCase().contains("ADD"));
    }

    @Test
    void editOrder_recalculatesOnChange_andAudits() {
        LocalDate d = LocalDate.now().plusDays(2);
        service.addOrder(makeOrder(d, 1, "Bob", "TX", "Tile", "150"));

        // simulate view-edited object (change area)
        Order toEdit = orderDao.getOrder(d, 1);
        toEdit.setArea(new BigDecimal("200"));

        Order after = service.editOrder(d, 1);

        // Material = 200 * 3.50 = 700.00
        assertEquals(new BigDecimal("700.00"), after.getMaterialCost());
        // Labor = 200 * 4.15 = 830.00
        assertEquals(new BigDecimal("830.00"), after.getLaborCost());
        // Tax = (700 + 830) * 0.0445 = 68.06
        assertEquals(new BigDecimal("68.09"), after.getTax());
        // Total = 1598.06
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
        assertEquals(4, service.getTaxes().size());
        assertEquals("TX", service.getTaxes().get(0).getStateAbbreviation());
        assertEquals(4, service.getProducts().size());
        assertEquals("Tile", service.getProducts().get(0).getProductType());
    }
}
