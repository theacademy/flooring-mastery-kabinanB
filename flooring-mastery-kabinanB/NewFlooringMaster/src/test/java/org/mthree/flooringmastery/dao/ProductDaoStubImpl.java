package org.mthree.flooringmastery.dao;

import org.mthree.flooringmastery.model.Product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProductDaoStubImpl implements ProductDao {
    private final List<Product> products = new ArrayList<>();

    public ProductDaoStubImpl() {
        products.add(make("Tile", new BigDecimal("3.50"), new BigDecimal("4.15")));
        products.add(make("Wood", new BigDecimal("5.15"), new BigDecimal("4.75")));
        products.add(make("Carpet", new BigDecimal("2.25"), new BigDecimal("2.10")));
        products.add(make("Laminate", new BigDecimal("1.75"), new BigDecimal("2.10")));
    }

    private Product make(String type, BigDecimal cpsf, BigDecimal lpsf) {
        Product p = new Product();
        p.setProductType(type);
        p.setCostPerSquareFoot(cpsf);
        p.setLaborCostPerSquareFoot(lpsf);
        return p;
    }

    @Override
    public List<Product> getAllProducts() { return products; }
}
