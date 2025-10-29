package org.mthree.flooringmastery.dao;

import org.mthree.flooringmastery.model.Order;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExportDaoFileImpl implements ExportDao{
    private List<Order> allOrders;

    private final String EXPORT_FILE;
    public static final String DELIMITER = ",";

    public ExportDaoFileImpl() {
        EXPORT_FILE = "resources/SampleFileData/Backup/DataExport.txt";

    }

    public ExportDaoFileImpl(String EXPORT_FILE) {
        this.EXPORT_FILE = EXPORT_FILE;
    }



    @Override
    public void exportData(Map<LocalDate, Map<Integer, Order>> allOrdersMap) {
        List<Order> allOrders = new ArrayList<>(); //
        for (var e : allOrdersMap.entrySet()) {
            LocalDate date = e.getKey();
            for (Order o : e.getValue().values()) {
                o.setOrderDate(date);
                allOrders.add(o);
            }
        }
        writeToFile(allOrders);
    }

    private String marshallExportData(Order order) {
        return order.getOrderNumber() + DELIMITER +
                order.getCustomerName() + DELIMITER +
                order.getState() + DELIMITER +
                order.getTaxRate() + DELIMITER +
                order.getProductType() + DELIMITER +
                order.getArea() + DELIMITER +
                order.getCostPerSquareFoot() + DELIMITER +
                order.getLaborCostPerSquareFoot() + DELIMITER +
                order.getMaterialCost() + DELIMITER +
                order.getLaborCost() + DELIMITER +
                order.getTax() + DELIMITER +
                order.getTotal() + DELIMITER +
                order.getOrderDate();
    }

    private void writeToFile(List<Order> allOrders) {
        try (PrintWriter out = new PrintWriter(new FileWriter(EXPORT_FILE))) { // overwrites file
            out.println("OrderNumber,CustomerName,State,TaxRate,ProductType,Area,CostPerSquareFoot,"
                    + "LaborCostPerSquareFoot,MaterialCost,LaborCost,Tax,Total,OrderDate");

            for (Order order : allOrders) {
                out.println(marshallExportData(order));
            }

        } catch (IOException e) {
            System.out.println("ERROR: Could not write export data to file.");
        }
    }
}
