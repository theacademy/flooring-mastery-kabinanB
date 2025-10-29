//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.mthree.flooringmastery.dao;

import java.util.List;
import org.mthree.flooringmastery.model.Tax;

public interface TaxDao {
    List<Tax> getAllTaxes() throws FlooringMasteryPersistenceException;
}
