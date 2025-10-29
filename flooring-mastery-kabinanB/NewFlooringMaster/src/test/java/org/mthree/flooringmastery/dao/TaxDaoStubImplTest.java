package org.mthree.flooringmastery.dao;

import org.junit.jupiter.api.Test;
import org.mthree.flooringmastery.dao.TaxDao;
import org.mthree.flooringmastery.dao.TaxDaoFileImpl;

import static org.junit.jupiter.api.Assertions.*;

class TaxDaoStubImplTest {

    @Test
    void getAllTaxes_containsTX_withRate445() {
        TaxDao dao = new TaxDaoFileImpl();
        assertFalse(dao.getAllTaxes().isEmpty());
        var tx = dao.getAllTaxes().get(0);
        assertEquals("TX", tx.getStateAbbreviation());
        // 4.45% stored as "4.45"
        assertEquals("4.45", tx.getTaxRate().toPlainString());
    }

}