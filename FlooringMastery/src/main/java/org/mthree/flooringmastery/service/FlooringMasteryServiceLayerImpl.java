//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.mthree.flooringmastery.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import org.mthree.flooringmastery.dao.AuditDao;
import org.mthree.flooringmastery.dao.ExportDao;
import org.mthree.flooringmastery.dao.FlooringMasteryPersistenceException;
import org.mthree.flooringmastery.dao.OrderDao;
import org.mthree.flooringmastery.dao.ProductDao;
import org.mthree.flooringmastery.dao.TaxDao;
import org.mthree.flooringmastery.model.Order;
import org.mthree.flooringmastery.model.Product;
import org.mthree.flooringmastery.model.Tax;

public class FlooringMasteryServiceLayerImpl implements FlooringMasteryServiceLayer {
    private OrderDao orderDao;
    private TaxDao taxDao;
    private ProductDao productDao;
    private AuditDao auditDao;
    private ExportDao exportDao;

    public FlooringMasteryServiceLayerImpl(OrderDao orderDao, TaxDao taxDao, ProductDao productDao, AuditDao auditDao, ExportDao exportDao) {
        this.orderDao = orderDao;
        this.taxDao = taxDao;
        this.productDao = productDao;
        this.auditDao = auditDao;
        this.exportDao = exportDao;
    }

    public int getNextOrderNumber() {
        return this.orderDao.getNextOrderNumber();
    }

    public Order addOrder(Order orderNumber) {
        this.calculateOrderTotals(orderNumber);
        Order added = this.orderDao.addOrder(orderNumber);
        this.writeAudit("ADD order #" + added.getOrderNumber());
        return added;
    }

    public Order getOrder(LocalDate date, int orderNumber) {
        return this.orderDao.getOrder(date, orderNumber);
    }

    public Order editOrder(LocalDate date, int orderNumber) {
        Order existing = this.orderDao.getOrder(date, orderNumber);
        if (existing != null) {
            this.calculateOrderTotals(existing);
            this.orderDao.editOrder(date, orderNumber);
            this.writeAudit("EDIT order #" + orderNumber);
        }

        return existing;
    }

    public List<Order> getOrdersForDate(LocalDate date) {
        return this.orderDao.getOrdersForDate(date);
    }

    public Order removeOrder(LocalDate date, int orderNumber) {
        Order removed = this.orderDao.removeOrder(date, orderNumber);
        if (removed != null) {
            this.writeAudit("REMOVE order #" + orderNumber);
        }

        return removed;
    }

    public void exportData() {
        this.exportDao.exportData(this.orderDao.getAllOrders());
        this.writeAudit("EXPORT all data");
    }

    public List<Tax> getTaxes() {
        try {
            return this.taxDao.getAllTaxes();
        } catch (FlooringMasteryPersistenceException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Product> getProducts() {
        try {
            return this.productDao.getAllProducts();
        } catch (FlooringMasteryPersistenceException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeAudit(String message) {
        try {
            this.auditDao.writeAuditEntry(message);
        } catch (Exception var3) {
            System.out.println("Could not write to audit log.");
        }

    }

    private void calculateOrderTotals(Order order) {
        // Look up Product by type
        final Product product;
        try {
            product = productDao.getAllProducts().stream()
                    .filter(p -> p.getProductType().equalsIgnoreCase(order.getProductType()))
                    .findFirst()
                    .orElse(null);
        } catch (FlooringMasteryPersistenceException e) {
            throw new RuntimeException(e);
        }

        // Look up Tax by state
        final Tax tax;
        try {
            tax = taxDao.getAllTaxes().stream()
                    .filter(t -> t.getStateAbbreviation().equalsIgnoreCase(order.getState()))
                    .findFirst()
                    .orElse(null);
        } catch (FlooringMasteryPersistenceException e) {
            throw new RuntimeException(e);
        }

        if (product == null || tax == null) {
            // Nothing to calculate if we can't resolve product/tax
            return;
        }

        // Fill derived fields
        order.setCostPerSquareFoot(product.getCostPerSquareFoot());
        order.setLaborCostPerSquareFoot(product.getLaborCostPerSquareFoot());
        order.setTaxRate(tax.getTaxRate());

        BigDecimal area = order.getArea();

        BigDecimal materialCost = area.multiply(order.getCostPerSquareFoot())
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal laborCost = area.multiply(order.getLaborCostPerSquareFoot())
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal subTotal = materialCost.add(laborCost);

        // taxRate is a whole percent (e.g., 6.25). Convert to decimal then multiply.
        BigDecimal taxRateDecimal = order.getTaxRate().movePointLeft(2); // divide by 100
        BigDecimal taxCost = subTotal.multiply(taxRateDecimal)
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal total = subTotal.add(taxCost)
                .setScale(2, RoundingMode.HALF_UP);

        order.setMaterialCost(materialCost);
        order.setLaborCost(laborCost);
        order.setTax(taxCost);
        order.setTotal(total);
    }

}
