package org.mthree.flooringmastery.dao;

import org.mthree.flooringmastery.model.Product;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

public class ProductDaoStubImpl implements ProductDao {
    @Override
    public List<Product> getAllProducts() {
        Product tile = new Product();
        tile.setProductType("Tile");
        tile.setCostPerSquareFoot(new BigDecimal("3.50"));
        tile.setLaborCostPerSquareFoot(new BigDecimal("4.15"));
        return Collections.singletonList(tile);
    }
}
