//package org.mthree.flooringmastery.dao;
//
//import org.mthree.flooringmastery.model.Product;
//import org.mthree.flooringmastery.model.Tax;
//
//import java.io.BufferedReader;
//import java.io.FileNotFoundException;
//import java.io.FileReader;
//import java.math.BigDecimal;
//import java.util.*;
//
//public class TaxDaoFileImpl implements TaxDao{
//
//    private Map<String, Tax> allTaxes = new HashMap<>();
//
//    private final String TAX_FILE;
//    private static final String DELIMITER = ",";
//
//    public TaxDaoFileImpl(String TAX_FILE) {
//        this.TAX_FILE = TAX_FILE;
//    }
//
//    public TaxDaoFileImpl()
//    {
//        TAX_FILE = "resources/SampleFileData/Data/Taxes.txt";
//    }
//
//    private Tax unmarshallTax(String taxAsText)
//    {
//        //This reads the file and allocates each property to Product
//        String[] taxTokens = taxAsText.split(DELIMITER);
//
//        Tax taxFromFile = new Tax();
//
//        taxFromFile.setStateAbbreviation(taxTokens[0]);
//
//
//        taxFromFile.setStateName(taxTokens[1]);
//
//        String taxTokenTwoString = taxTokens[2];
//        BigDecimal taxTokenTwo = new BigDecimal(taxTokenTwoString);
//        taxFromFile.setTaxRate(taxTokenTwo);
//
//        return taxFromFile;
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
//            scanner = new Scanner(new BufferedReader((new FileReader(TAX_FILE))));
//        } catch (FileNotFoundException e)
//        {
//            throw new FlooringMasteryPersistenceException("-_- Could not load product data into memory.", e );
//        }
//
//        String currentLine;
//
//        Tax currentTax;
//
//        while(scanner.hasNextLine())
//        {
//            currentLine = scanner.nextLine();
//
//            currentTax = unmarshallTax(currentLine);
//
//            //Skip the titles
//            if(currentTax.getStateAbbreviation().equalsIgnoreCase("State"))
//            {
//                continue;
//            }
//
//            allTaxes.put(currentTax.getStateAbbreviation(), currentTax );
//        }
//
//    }
//
//    @Override
//    public List<Tax> getAllTaxes() throws FlooringMasteryPersistenceException {
//        loadFile();
//        return new ArrayList<>(allTaxes.values());
//    }
//}

package org.mthree.flooringmastery.dao;

import org.mthree.flooringmastery.model.Tax;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class TaxDaoFileImpl implements TaxDao {

    private final Map<String, Tax> allTaxes = new LinkedHashMap<>();

    // Classpath resource (recommended) and filesystem fallback
    private final String CLASSPATH = "SampleFileData/Data/Taxes.txt";
    private final String FS_FALLBACK = "resources/SampleFileData/Data/Taxes.txt";

    private static final String DELIM = ",";

    private Tax parse(String line) {
        String[] t = line.split(DELIM);
        Tax tax = new Tax();
        tax.setStateAbbreviation(t[0].trim());
        tax.setStateName(t[1].trim());
        tax.setTaxRate(new BigDecimal(t[2].trim())); // e.g. 4.45
        return tax;
    }

    private void loadFile() throws FlooringMasteryPersistenceException {
        allTaxes.clear();

        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(CLASSPATH);
        if (in == null) {
            // filesystem fallback (useful when running without resources on classpath)
            try { in = Files.newInputStream(Path.of(FS_FALLBACK)); }
            catch (IOException ignore) {}
        }

        if (in == null) {
            throw new FlooringMasteryPersistenceException(
                    "-_- Could not load tax data into memory (missing Taxes.txt). " +
                            "Looked for classpath '" + CLASSPATH + "' and file '" + FS_FALLBACK + "'."
            );
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
            String line = br.readLine(); // header or first row

            // If the first line isn’t a header, treat it as data
            if (line != null && !line.toLowerCase().startsWith("state")) {
                Tax first = parse(line);
                allTaxes.put(first.getStateAbbreviation(), first);
            }

            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;
                Tax tax = parse(line);
                allTaxes.put(tax.getStateAbbreviation(), tax);
            }
        } catch (IOException e) {
            throw new FlooringMasteryPersistenceException("Could not read tax file.", e);
        }
    }

    @Override
    public List<Tax> getAllTaxes() throws FlooringMasteryPersistenceException {
        loadFile();
        return new ArrayList<>(allTaxes.values());
    }
}

