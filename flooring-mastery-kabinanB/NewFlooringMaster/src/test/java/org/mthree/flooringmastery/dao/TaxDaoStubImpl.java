package org.mthree.flooringmastery.dao;

import org.mthree.flooringmastery.model.Tax;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TaxDaoStubImpl implements TaxDao {
    private final List<Tax> taxes = new ArrayList<>();

    public TaxDaoStubImpl() {
        taxes.add(make("TX", "Texas", "4.45"));
        taxes.add(make("OH", "Ohio",  "6.25"));
        taxes.add(make("PA", "Pennsylvania", "6.75"));
        taxes.add(make("MI", "Michigan", "5.75"));
    }

    private Tax make(String abbr, String name, String rate) {
        Tax t = new Tax();
        t.setStateAbbreviation(abbr);
        t.setStateName(name);
        t.setTaxRate(new BigDecimal(rate)); // as percent, e.g. 4.45
        return t;
    }

    @Override
    public List<Tax> getAllTaxes() { return taxes; }
}
