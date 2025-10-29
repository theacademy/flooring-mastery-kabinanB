package org.mthree.flooringmastery.dao;

import org.mthree.flooringmastery.model.Order;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExportDaoFileImpl implements ExportDao {
    private final String EXPORT_FILE;
    public static final String DELIMITER = ","; // (optional) not used below

    public ExportDaoFileImpl() {
        this.EXPORT_FILE = "resources/SampleFileData/Backup/DataExport.txt";
    }

    public ExportDaoFileImpl(String exportFile) {
        this.EXPORT_FILE = exportFile;
    }

    @Override
    public void exportData(Map<LocalDate, Map<Integer, Order>> allOrdersMap) {
        List<Order> allOrders = new ArrayList<>();

        for (Map.Entry<LocalDate, Map<Integer, Order>> entry : allOrdersMap.entrySet()) {
            LocalDate date = entry.getKey();
            Map<Integer, Order> ordersForDate = entry.getValue();

            for (Order o : ordersForDate.values()) {
                o.setOrderDate(date);
                allOrders.add(o);
            }
        }

        writeToFile(allOrders);
    }

    private String marshallExportData(Order order) {
        return order.getOrderNumber() + DELIMITER
                + order.getCustomerName() + DELIMITER
                + order.getState() + DELIMITER
                + String.valueOf(order.getTaxRate()) + DELIMITER
                + order.getProductType() + DELIMITER
                + String.valueOf(order.getArea()) + DELIMITER
                + String.valueOf(order.getCostPerSquareFoot()) + DELIMITER
                + String.valueOf(order.getLaborCostPerSquareFoot()) + DELIMITER
                + String.valueOf(order.getMaterialCost()) + DELIMITER
                + String.valueOf(order.getLaborCost()) + DELIMITER
                + String.valueOf(order.getTax()) + DELIMITER
                + String.valueOf(order.getTotal()) + DELIMITER
                + String.valueOf(order.getOrderDate());
    }

    private void writeToFile(List<Order> allOrders) {
        try (PrintWriter out = new PrintWriter(new FileWriter(this.EXPORT_FILE))) {
            out.println("OrderNumber,CustomerName,State,TaxRate,ProductType,Area,CostPerSquareFoot,"
                    + "LaborCostPerSquareFoot,MaterialCost,LaborCost,Tax,Total,OrderDate");

            for (Order order : allOrders) {
                out.println(marshallExportData(order));
            }
        } catch (IOException ex) {
            System.out.println("ERROR: Could not write export data to file.");
        }
    }
}
