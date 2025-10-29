package org.mthree.flooringmastery.dao;

import org.mthree.flooringmastery.model.Product;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;
//
//public class ProductDaoFileImpl implements ProductDao{
//    //Data Structure to store products given Type of the product as key and value as the object itself
//    private Map<String, Product> allProducts = new HashMap<>();
//
//    private final String PRODUCT_FILE;
//    private static final String DELIMITER = ",";
//
//
//    public ProductDaoFileImpl()
//    {
//        PRODUCT_FILE = "resources/SampleFileData/Data/Products.txt";
//    }
//    public ProductDaoFileImpl(String PRODUCT_FILE) {
//        this.PRODUCT_FILE = PRODUCT_FILE;
//    }
//
//
//    private Product unmarshallProduct(String productAsText)
//    {
//        //This reads the file and allocates each property to Product
//        String[] productTokens = productAsText.split(DELIMITER);
//
//        Product productFromFile = new Product();
//
//        productFromFile.setProductType(productTokens[0]);
//
//        String productTokenOneString = productTokens[1];
//        BigDecimal productTokenOne = new BigDecimal(productTokenOneString);
//        productFromFile.setCostPerSquareFoot(productTokenOne);
//
//        String productTokenTwoString = productTokens[2];
//        BigDecimal productTokenTwo = new BigDecimal(productTokenTwoString);
//        productFromFile.setLaborCostPerSquareFoot(productTokenTwo);
//
//        return productFromFile;
//
//    }
//
//    private void loadFile() throws FlooringMasteryPersistenceException
//    {
//        Scanner scanner;
//
//        //File opens?
//        try
//        {
//            scanner = new Scanner(new BufferedReader((new FileReader(PRODUCT_FILE))));
//        } catch (FileNotFoundException e)
//        {
//            throw new FlooringMasteryPersistenceException("-_- Could not load product data into memory.\"", e );
//        }
//
//        String currentLine;
//
//        Product currentProduct;
//
//        while(scanner.hasNextLine())
//        {
//            currentLine = scanner.nextLine();
//
//            currentProduct = unmarshallProduct(currentLine);
//
//            //Skip the titles
//            if(currentProduct.getProductType().equalsIgnoreCase("ProductType"))
//            {
//                continue;
//            }
//
//            allProducts.put(currentProduct.getProductType(), currentProduct );
//        }
//
//    }
//
//
//    @Override
//    public List<Product> getAllProducts() throws FlooringMasteryPersistenceException {
//        loadFile();
//        return new ArrayList<>(allProducts.values());
//    }
//}

public class ProductDaoFileImpl implements ProductDao {
    private final Map<String, Product> allProducts = new LinkedHashMap<>();
    private final String CLASSPATH = "SampleFileData/Data/Products.txt";
    private final String FS_FALLBACK = "resources/SampleFileData/Data/Products.txt";
    private static final String DELIM = ",";

    private Product parse(String line){
        String[] t = line.split(DELIM);
        Product p = new Product();
        p.setProductType(t[0].trim());
        p.setCostPerSquareFoot(new BigDecimal(t[1].trim()));
        p.setLaborCostPerSquareFoot(new BigDecimal(t[2].trim()));
        return p;
    }

    private void loadFile() throws FlooringMasteryPersistenceException {
        allProducts.clear();
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(CLASSPATH);
        if (in == null) {
            try { in = java.nio.file.Files.newInputStream(java.nio.file.Path.of(FS_FALLBACK)); }
            catch (IOException ignore) {}
        }
        if (in == null) throw new FlooringMasteryPersistenceException("Could not load product data (missing Products.txt).");

        try (BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
            String line = br.readLine(); // header or first row
            if (line != null && !line.toLowerCase().startsWith("producttype")) {
                Product first = parse(line); allProducts.put(first.getProductType(), first);
            }
            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;
                Product p = parse(line);
                allProducts.put(p.getProductType(), p);
            }
        } catch (IOException e) {
            throw new FlooringMasteryPersistenceException("Could not read product file.", e);
        }
    }

    @Override
    public List<Product> getAllProducts() throws FlooringMasteryPersistenceException {
        loadFile();
        return new ArrayList<>(allProducts.values());
    }
}

