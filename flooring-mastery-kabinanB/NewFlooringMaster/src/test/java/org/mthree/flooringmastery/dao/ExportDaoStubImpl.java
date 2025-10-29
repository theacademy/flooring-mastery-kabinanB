package org.mthree.flooringmastery.dao;

import org.mthree.flooringmastery.model.Order;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

public class ExportDaoStubImpl implements ExportDao {
    public boolean called = false;
    public Map<LocalDate, Map<Integer, Order>> snapshot;

    @Override
    public void exportData(Map<LocalDate, Map<Integer, Order>> allOrders) {
        called = true;
        snapshot = new LinkedHashMap<>(allOrders);
    }

    public boolean wasCalled() {   // <-- add this
        return called;
    }

    public Map<LocalDate, Map<Integer, Order>> getSnapshot()
    {
        return snapshot;
    }
}
