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
import org.mthree.flooringmastery.model.Tax;

public class TaxDaoFileImpl implements TaxDao {
    private final Map<String, Tax> allTaxes = new LinkedHashMap();
    private final String CLASSPATH = "SampleFileData/Data/Taxes.txt";
    private final String FS_FALLBACK = "resources/SampleFileData/Data/Taxes.txt";
    private static final String DELIM = ",";

    private Tax parse(String line) {
        String[] t = line.split(",");
        Tax tax = new Tax();
        tax.setStateAbbreviation(t[0].trim());
        tax.setStateName(t[1].trim());
        tax.setTaxRate(new BigDecimal(t[2].trim()));
        return tax;
    }

    private void loadFile() throws FlooringMasteryPersistenceException {
        this.allTaxes.clear();
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("SampleFileData/Data/Taxes.txt");
        if (in == null) {
            try {
                in = Files.newInputStream(Path.of("resources/SampleFileData/Data/Taxes.txt"));
            } catch (IOException var6) {
            }
        }

        if (in == null) {
            throw new FlooringMasteryPersistenceException("-_- Could not load tax data into memory (missing Taxes.txt). Looked for classpath 'SampleFileData/Data/Taxes.txt' and file 'resources/SampleFileData/Data/Taxes.txt'.");
        } else {
            try {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
                    String line = br.readLine();
                    if (line != null && !line.toLowerCase().startsWith("state")) {
                        Tax first = this.parse(line);
                        this.allTaxes.put(first.getStateAbbreviation(), first);
                    }

                    while((line = br.readLine()) != null) {
                        if (!line.isBlank()) {
                            Tax tax = this.parse(line);
                            this.allTaxes.put(tax.getStateAbbreviation(), tax);
                        }
                    }
                }

            } catch (IOException e) {
                throw new FlooringMasteryPersistenceException("Could not read tax file.", e);
            }
        }
    }

    public List<Tax> getAllTaxes() throws FlooringMasteryPersistenceException {
        this.loadFile();
        return new ArrayList(this.allTaxes.values());
    }
}
