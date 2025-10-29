package org.mthree.flooringmastery.dao;

import org.mthree.flooringmastery.model.Tax;

import java.util.List;

public interface TaxDao {
    List<Tax> getAllTaxes() throws FlooringMasteryPersistenceException;
}
