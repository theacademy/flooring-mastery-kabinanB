package org.mthree.flooringmastery.dao;

import org.mthree.flooringmastery.model.Product;

import java.util.List;

public interface ProductDao {
    List<Product> getAllProducts() throws FlooringMasteryPersistenceException;
}
