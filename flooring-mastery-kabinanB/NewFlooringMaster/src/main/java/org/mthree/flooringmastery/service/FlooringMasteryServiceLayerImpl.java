package org.mthree.flooringmastery.service;

import org.mthree.flooringmastery.dao.*;
import org.mthree.flooringmastery.model.Order;
import org.mthree.flooringmastery.model.Product;
import org.mthree.flooringmastery.model.Tax;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

public class FlooringMasteryServiceLayerImpl implements FlooringMasteryServiceLayer{

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

    @Override
    public int getNextOrderNumber() {
        return orderDao.getNextOrderNumber();

    }

    @Override
    public Order addOrder(Order orderNumber) {
        // Attach product/tax info and calculate totals
        calculateOrderTotals(orderNumber);
        Order added = orderDao.addOrder(orderNumber);
        writeAudit("ADD order #" + added.getOrderNumber());
        return added;
    }

    @Override
    public Order getOrder(LocalDate date, int orderNumber) {
        return orderDao.getOrder(date, orderNumber);

    }

    @Override
    public Order editOrder(LocalDate date, int orderNumber) {
        Order existing = orderDao.getOrder(date, orderNumber);
        if (existing != null) {
            calculateOrderTotals(existing);
            orderDao.editOrder(date, orderNumber);
            writeAudit("EDIT order #" + orderNumber);
        }
        return existing;
    }

    @Override
    public List<Order> getOrdersForDate(LocalDate date) {
        return orderDao.getOrdersForDate(date);
    }

    @Override
    public Order removeOrder(LocalDate date, int orderNumber) {
        Order removed = orderDao.removeOrder(date, orderNumber);
        if (removed != null) {
            writeAudit("REMOVE order #" + orderNumber);
        }
        return removed;
    }

    @Override
    public void exportData() {
        exportDao.exportData(orderDao.getAllOrders());
        writeAudit("EXPORT all data");

    }

    @Override
    public List<Tax> getTaxes() {
        try {
            return taxDao.getAllTaxes();
        } catch (FlooringMasteryPersistenceException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Product> getProducts() {
        try {
            return productDao.getAllProducts();
        } catch (FlooringMasteryPersistenceException e) {
            throw new RuntimeException(e);
        }
    }
    //--- helper function ---

    private void writeAudit(String message) {
        try {
            auditDao.writeAuditEntry(message);
        } catch (Exception e) {
            System.out.println("Could not write to audit log.");
        }
    }

    private void calculateOrderTotals(Order order) {
        // Look up product and tax info
        Product product = productDao.getAllProducts().stream()
                .filter(p -> p.getProductType().equalsIgnoreCase(order.getProductType()))
                .findFirst().orElse(null);

        Tax tax = taxDao.getAllTaxes().stream()
                .filter(t -> t.getStateAbbreviation().equalsIgnoreCase(order.getState()))
                .findFirst().orElse(null);

        if (product == null || tax == null) return;

        order.setCostPerSquareFoot(product.getCostPerSquareFoot());
        order.setLaborCostPerSquareFoot(product.getLaborCostPerSquareFoot());
        order.setTaxRate(tax.getTaxRate());

        BigDecimal area = order.getArea();
        BigDecimal materialCost = area.multiply(order.getCostPerSquareFoot()).setScale(2, RoundingMode.HALF_UP);
        BigDecimal laborCost = area.multiply(order.getLaborCostPerSquareFoot()).setScale(2, RoundingMode.HALF_UP);
        BigDecimal subTotal = materialCost.add(laborCost);
        BigDecimal taxCost = subTotal.multiply(order.getTaxRate().divide(new BigDecimal("100"), 5, RoundingMode.HALF_UP))
                .setScale(2, RoundingMode.HALF_UP);
        BigDecimal total = subTotal.add(taxCost).setScale(2, RoundingMode.HALF_UP);

        order.setMaterialCost(materialCost);
        order.setLaborCost(laborCost);
        order.setTax(taxCost);
        order.setTotal(total);
    }

}
