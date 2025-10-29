package org.mthree.flooringmastery.dao;

import org.junit.jupiter.api.Test;
import org.mthree.flooringmastery.model.Order;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ExportDaoStubImplTest {

    @Test
    void exportData_capturesSnapshot_and_setsFlag() {
        ExportDaoStubImpl dao = new ExportDaoStubImpl();

        Map<LocalDate, Map<Integer, Order>> data = new LinkedHashMap<>();
        dao.exportData(data);

        assertTrue(dao.wasCalled());
        assertNotNull(dao.getSnapshot());
    }
}


