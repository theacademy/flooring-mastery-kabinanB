package org.mthree.flooringmastery.dao;

import org.mthree.flooringmastery.model.Order;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class OrderDaoFileImpl implements OrderDao{

    private Map<LocalDate, Map<Integer, Order>> orders = new LinkedHashMap<>();

    private static final String HEADER =
            "OrderNumber,CustomerName,State,TaxRate,ProductType,Area,CostPerSquareFoot," +
                    "LaborCostPerSquareFoot,MaterialCost,LaborCost,Tax,Total";

    // UML fields
    private final String ORDER_FOLDER;

    private static final String DELIMITER = ",";

    private int largestOrderNumber = 0;

    private static final DateTimeFormatter FILE_DATE = DateTimeFormatter.ofPattern("MMddyyyy");

    public OrderDaoFileImpl(String orderFolder) {
        ORDER_FOLDER = orderFolder;
        ensureFolder();
        loadFromFile();
    }

    public OrderDaoFileImpl() {
        this.ORDER_FOLDER = "resources/SampleFileData/Orders";
        ensureFolder();
        loadFromFile();
    }

    private void ensureFolder() {
        try {
            Files.createDirectories(Path.of(ORDER_FOLDER));
        } catch (IOException e) {
            throw new RuntimeException("Could not create/access Orders folder: " + ORDER_FOLDER, e);
        }
    }

    // ---------- UML methods ----------

    /** Writes ALL dates currently in memory back to their respective files. */
    private void writeToFile() {
        for (LocalDate date : orders.keySet()) {
            writeOneDate(date);
        }
    }

    /** Reads ALL Orders_*.txt files from ORDER_FOLDER into memory. */
    private void loadFromFile() {
        orders.clear();
        largestOrderNumber = 0;

        try (DirectoryStream<Path> stream =
                     Files.newDirectoryStream(Path.of(ORDER_FOLDER), "Orders_*.txt")) {

            for (Path p : stream) {
                LocalDate date = extractDateFromFileName(p.getFileName().toString());
                Map<Integer, Order> map = new LinkedHashMap<>();

                try (BufferedReader br = Files.newBufferedReader(p)) {
                    String line = br.readLine(); // header
                    while ((line = br.readLine()) != null) {
                        if (line.isBlank()) continue;

                        // ✅ split with CSV regex (handles commas inside quotes)
                        String[] tokens = line.split(CSV_SPLIT_REGEX, -1);

                        Order o = unmarshalOrder(tokens);  // your updated unmarshal
                        o.setOrderDate(date);

                        map.put(o.getOrderNumber(), o);
                        largestOrderNumber = Math.max(largestOrderNumber, o.getOrderNumber());
                    }
                }
                orders.put(date, map);
            }

        } catch (NoSuchFileException ignore) {
            // Folder empty on first run — that’s fine
        } catch (IOException e) {
            throw new RuntimeException("Could not load orders from folder " + ORDER_FOLDER, e);
        }
    }


    @Override
    public int getNextOrderNumber() {
//        int largestOrderNumber = 0;
//        for(Order o:  )
//        {
//            if(largestOrderNumber < o.getOrderNumber())
//            {
//                largestOrderNumber = o.getOrderNumber();
//            }
//        }
//        largestOrderNumber += 1;

        return largestOrderNumber + 1;

    }

    @Override
    public Order addOrder(Order order) {
        LocalDate date = order.getOrderDate();
        orders.computeIfAbsent(date, d -> new HashMap<>())
                .put(order.getOrderNumber(), order);

        largestOrderNumber = Math.max(largestOrderNumber, order.getOrderNumber());
        writeOneDate(date); // persist only that date’s file
        return order;
    }

    @Override
    public Order getOrder(LocalDate date, int orderNumber) {
        Map<Integer, Order> m = orders.get(date);
        return (m == null) ? null : m.get(orderNumber);
    }

    @Override
    public Order editOrder(LocalDate date, int orderNumber) {
        Order current = getOrder(date, orderNumber);
        if (current != null) writeOneDate(date);
        return current;
    }

    @Override
    public List<Order> getOrdersForDate(LocalDate date) {
        Map<Integer, Order> m = orders.get(date);
        if (m == null) return Collections.emptyList();
        List<Order> list = new ArrayList<>(m.values());
        list.sort(Comparator.comparingInt(Order::getOrderNumber));
        return list;
    }

    @Override
    public Map<LocalDate, Map<Integer, Order>> getAllOrders() {
        Map<LocalDate, Map<Integer, Order>> copy = new HashMap<>();
        for (var e : orders.entrySet()) {
            copy.put(e.getKey(), new HashMap<>(e.getValue()));
        }
        return copy;
    }

    @Override
    public Order removeOrder(LocalDate date, int orderNumber) {
        Map<Integer, Order> m = orders.get(date);
        if (m == null) return null;
        Order removed = m.remove(orderNumber);
        writeOneDate(date); // persist date’s file after removal
        return removed;
    }

    // --- Helpers ---

    private void writeOneDate(LocalDate date) {
        Path file = Path.of(ORDER_FOLDER, "Orders_" + FILE_DATE.format(date) + ".txt");
        Map<Integer, Order> m = orders.getOrDefault(date, Collections.emptyMap());

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file.toFile())))) {
            out.println(HEADER);
            for (Order o : m.values()) {
                out.println(marshalOrder(o));
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not write orders for " + date, e);
        }
    }

    private LocalDate extractDateFromFileName(String fileName) {
        // "Orders_MMddyyyy.txt"
        String mmddyyyy = fileName.substring("Orders_".length(), fileName.length() - 4);
        return LocalDate.parse(mmddyyyy, FILE_DATE);
    }
//    private String marshalOrder(Order o) {
//        return o.getOrderNumber() + DELIMITER +
//                o.getCustomerName() + DELIMITER +
//                o.getState() + DELIMITER +
//                o.getTaxRate() + DELIMITER +
//                o.getProductType() + DELIMITER +
//                o.getArea() + DELIMITER +
//                o.getCostPerSquareFoot() + DELIMITER +
//                o.getLaborCostPerSquareFoot() + DELIMITER +
//                o.getMaterialCost() + DELIMITER +
//                o.getLaborCost() + DELIMITER +
//                o.getTax() + DELIMITER +
//                o.getTotal();
//    }

    private static String q(String s) {
        if (s == null) return "";
        boolean needQuotes = s.contains(",") || s.contains("\"") || s.contains("\n") || s.contains("\r");
        if (!needQuotes) return s;
        return "\"" + s.replace("\"", "\"\"") + "\"";
    }

    private String marshalOrder(Order o) {
        return o.getOrderNumber() + DELIMITER +
                q(o.getCustomerName()) + DELIMITER +
                o.getState() + DELIMITER +
                o.getTaxRate() + DELIMITER +
                o.getProductType() + DELIMITER +
                o.getArea() + DELIMITER +
                o.getCostPerSquareFoot() + DELIMITER +
                o.getLaborCostPerSquareFoot() + DELIMITER +
                o.getMaterialCost() + DELIMITER +
                o.getLaborCost() + DELIMITER +
                o.getTax() + DELIMITER +
                o.getTotal();
    }


    private static final String CSV_SPLIT_REGEX = ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";

    private static String unquote(String s) {
        s = s.trim();
        if (s.length() >= 2 && s.startsWith("\"") && s.endsWith("\"")) {
            s = s.substring(1, s.length() - 1).replace("\"\"", "\"");
        }
        return s;
    }

    // Replace your toBD with this
    private static BigDecimal toBD(String s) {
        s = unquote(s).trim();
        if (s.isEmpty() || s.equalsIgnoreCase("null")) return BigDecimal.ZERO;
        return new BigDecimal(s);
    }

    // Replace your unmarshalOrder with this
    private Order unmarshalOrder(String[] raw) {
        // If caller passed a raw line, split with CSV-safe regex already used in loadFromFile()
        String[] c = (raw.length == 1) ? raw[0].split(CSV_SPLIT_REGEX, -1) : raw;

        // Unquote all fields
        for (int i = 0; i < c.length; i++) c[i] = unquote(c[i]);

        // We support:
        //  - 12 columns:  [Order#, Name, State, TaxRate, Product, Area, CPSF, LPSF, Material, Labor, Tax, Total]
        //  - 11 columns (legacy, no TaxRate): [Order#, Name, State, Product, Area, CPSF, LPSF, Material, Labor, Tax, Total]
        //  - >=12 columns when Name had unquoted commas: join extra middle tokens back into Name
        if (c.length < 11) {
            throw new RuntimeException("Orders row has " + c.length + " columns, expected >= 11. Row: " + String.join("|", c));
        }

        boolean legacyNoTaxRate;
        String orderNumStr, name, state;
        int startIdx; // index in c where State (or Product for legacy) begins

        if (c.length == 11) {
            // legacy layout, no TaxRate
            legacyNoTaxRate = true;
            // Name spans indices [1 .. c.length - 10]
            int nameEnd = c.length - 10; // inclusive
            orderNumStr = c[0].trim();
            name = String.join(",", Arrays.copyOfRange(c, 1, nameEnd + 1)).trim();
            startIdx = nameEnd + 1;     // points to State
            state = c[startIdx++].trim(); // State
        } else {
            // have TaxRate present (12 or more columns if name had extra commas)
            legacyNoTaxRate = false;
            int nameEnd = c.length - 11; // inclusive
            orderNumStr = c[0].trim();
            name = String.join(",", Arrays.copyOfRange(c, 1, nameEnd + 1)).trim();
            startIdx = nameEnd + 1;     // points to State
            state = c[startIdx++].trim();       // State
        }

        Order o = new Order();
        o.setOrderNumber(Integer.parseInt(orderNumStr));
        o.setCustomerName(name);
        o.setState(state);

        if (legacyNoTaxRate) {
            o.setTaxRate(BigDecimal.ZERO);          // will be re-derived by service on edits/adds
            o.setProductType(c[startIdx++].trim()); // Product
        } else {
            o.setTaxRate(toBD(c[startIdx++]));      // TaxRate
            o.setProductType(c[startIdx++].trim()); // Product
        }

        o.setArea(toBD(c[startIdx++]));                    // Area
        o.setCostPerSquareFoot(toBD(c[startIdx++]));       // CPSF
        o.setLaborCostPerSquareFoot(toBD(c[startIdx++]));  // LPSF
        o.setMaterialCost(toBD(c[startIdx++]));            // Material
        o.setLaborCost(toBD(c[startIdx++]));               // Labor
        o.setTax(toBD(c[startIdx++]));                     // Tax
        o.setTotal(toBD(c[startIdx++]));                   // Total

        return o;
    }

}
