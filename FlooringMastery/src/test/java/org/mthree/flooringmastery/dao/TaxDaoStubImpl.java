package org.mthree.flooringmastery.dao;

import org.mthree.flooringmastery.model.Tax;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

public class TaxDaoStubImpl implements TaxDao {
    @Override
    public List<Tax> getAllTaxes() {
        Tax tx = new Tax();
        tx.setStateAbbreviation("TX");
        tx.setStateName("Texas");
        tx.setTaxRate(new BigDecimal("4.45"));
        return Collections.singletonList(tx);
    }
}
