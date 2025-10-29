package org.mthree.flooringmastery.dao;

import org.mthree.flooringmastery.model.Order;

import java.time.LocalDate;
import java.util.Map;

public interface ExportDao {
    public void exportData(Map<LocalDate, Map<Integer, Order>> allOrdersMap);
}
