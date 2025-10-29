//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.mthree.flooringmastery.dao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.mthree.flooringmastery.model.Product;

public class ProductDaoFileImpl implements ProductDao {
    private final Map<String, Product> allProducts = new LinkedHashMap();
    private final String CLASSPATH = "SampleFileData/Data/Products.txt";
    private final String FS_FALLBACK = "resources/SampleFileData/Data/Products.txt";
    private static final String DELIM = ",";

    private Product parse(String line) {
        String[] t = line.split(",");
        Product p = new Product();
        p.setProductType(t[0].trim());
        p.setCostPerSquareFoot(new BigDecimal(t[1].trim()));
        p.setLaborCostPerSquareFoot(new BigDecimal(t[2].trim()));
        return p;
    }

    private void loadFile() throws FlooringMasteryPersistenceException {
        this.allProducts.clear();
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("SampleFileData/Data/Products.txt");
        if (in == null) {
            try {
                in = Files.newInputStream(Path.of("resources/SampleFileData/Data/Products.txt"));
            } catch (IOException var6) {
            }
        }

        if (in == null) {
            throw new FlooringMasteryPersistenceException("Could not load product data (missing Products.txt).");
        } else {
            try {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
                    String line = br.readLine();
                    if (line != null && !line.toLowerCase().startsWith("producttype")) {
                        Product first = this.parse(line);
                        this.allProducts.put(first.getProductType(), first);
                    }

                    while((line = br.readLine()) != null) {
                        if (!line.isBlank()) {
                            Product p = this.parse(line);
                            this.allProducts.put(p.getProductType(), p);
                        }
                    }
                }

            } catch (IOException e) {
                throw new FlooringMasteryPersistenceException("Could not read product file.", e);
            }
        }
    }

    public List<Product> getAllProducts() throws FlooringMasteryPersistenceException {
        this.loadFile();
        return new ArrayList(this.allProducts.values());
    }
}
