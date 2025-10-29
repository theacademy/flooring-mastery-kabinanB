package org.mthree.flooringmastery.dao;

import org.mthree.flooringmastery.model.Order;

import java.time.LocalDate;
import java.util.Map;

public class ExportDaoStubImpl implements ExportDao {
    public boolean called = false;
    public Map<LocalDate, Map<Integer, Order>> snapshot;

    @Override
    public void exportData(Map<LocalDate, Map<Integer, Order>> allOrdersMap) {
        called = true;
        snapshot = allOrdersMap;
    }
}
