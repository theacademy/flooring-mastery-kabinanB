//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.mthree.flooringmastery.dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.mthree.flooringmastery.model.Order;

public class OrderDaoFileImpl implements OrderDao {
    private Map<LocalDate, Map<Integer, Order>> orders = new LinkedHashMap();
    private static final String HEADER = "OrderNumber,CustomerName,State,TaxRate,ProductType,Area,CostPerSquareFoot,LaborCostPerSquareFoot,MaterialCost,LaborCost,Tax,Total";
    private final String ORDER_FOLDER;
    private static final String DELIMITER = ",";
    private int largestOrderNumber = 0;
    private static final DateTimeFormatter FILE_DATE = DateTimeFormatter.ofPattern("MMddyyyy");
    private static final String CSV_SPLIT_REGEX = ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";

    public OrderDaoFileImpl(String orderFolder) {
        this.ORDER_FOLDER = orderFolder;
        this.ensureFolder();
        this.loadFromFile();
    }

    public OrderDaoFileImpl() {
        this.ORDER_FOLDER = "resources/SampleFileData/Orders";
        this.ensureFolder();
        this.loadFromFile();
    }

    private void ensureFolder() {
        try {
            Files.createDirectories(Path.of(this.ORDER_FOLDER));
        } catch (IOException e) {
            throw new RuntimeException("Could not create/access Orders folder: " + this.ORDER_FOLDER, e);
        }
    }

    private void writeToFile() {
        for(LocalDate date : this.orders.keySet()) {
            this.writeOneDate(date);
        }

    }

    private void loadFromFile() {
        this.orders.clear();
        this.largestOrderNumber = 0;

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Path.of(this.ORDER_FOLDER), "Orders_*.txt")) {
            for(Path p : stream) {
                LocalDate date = this.extractDateFromFileName(p.getFileName().toString());
                Map<Integer, Order> map = new LinkedHashMap();

                try (BufferedReader br = Files.newBufferedReader(p)) {
                    String line = br.readLine();

                    while((line = br.readLine()) != null) {
                        if (!line.isBlank()) {
                            String[] tokens = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                            Order o = this.unmarshalOrder(tokens);
                            o.setOrderDate(date);
                            map.put(o.getOrderNumber(), o);
                            this.largestOrderNumber = Math.max(this.largestOrderNumber, o.getOrderNumber());
                        }
                    }
                }

                this.orders.put(date, map);
            }
        } catch (NoSuchFileException var14) {
        } catch (IOException e) {
            throw new RuntimeException("Could not load orders from folder " + this.ORDER_FOLDER, e);
        }

    }

    public int getNextOrderNumber() {
        return this.largestOrderNumber + 1;
    }

    public Order addOrder(Order order) {
        LocalDate date = order.getOrderDate();
        ((Map)this.orders.computeIfAbsent(date, (d) -> new HashMap())).put(order.getOrderNumber(), order);
        this.largestOrderNumber = Math.max(this.largestOrderNumber, order.getOrderNumber());
        this.writeOneDate(date);
        return order;
    }

    public Order getOrder(LocalDate date, int orderNumber) {
        Map<Integer, Order> m = (Map)this.orders.get(date);
        return m == null ? null : (Order)m.get(orderNumber);
    }

    public Order editOrder(LocalDate date, int orderNumber) {
        Order current = this.getOrder(date, orderNumber);
        if (current != null) {
            this.writeOneDate(date);
        }

        return current;
    }

    public List<Order> getOrdersForDate(LocalDate date) {
        Map<Integer, Order> m = (Map)this.orders.get(date);
        if (m == null) {
            return Collections.emptyList();
        } else {
            List<Order> list = new ArrayList(m.values());
            list.sort(Comparator.comparingInt(Order::getOrderNumber));
            return list;
        }
    }

    public Map<LocalDate, Map<Integer, Order>> getAllOrders() {
        Map<LocalDate, Map<Integer, Order>> copy = new HashMap();

        for(Map.Entry<LocalDate, Map<Integer, Order>> e : this.orders.entrySet()) {
            copy.put((LocalDate)e.getKey(), new HashMap((Map)e.getValue()));
        }

        return copy;
    }

    public Order removeOrder(LocalDate date, int orderNumber) {
        Map<Integer, Order> m = (Map)this.orders.get(date);
        if (m == null) {
            return null;
        } else {
            Order removed = (Order)m.remove(orderNumber);
            this.writeOneDate(date);
            return removed;
        }
    }

    private void writeOneDate(LocalDate date) {
        Path file = Path.of(this.ORDER_FOLDER, "Orders_" + FILE_DATE.format(date) + ".txt");
        Map<Integer, Order> m = (Map)this.orders.getOrDefault(date, Collections.emptyMap());

        try {
            try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file.toFile())))) {
                out.println("OrderNumber,CustomerName,State,TaxRate,ProductType,Area,CostPerSquareFoot,LaborCostPerSquareFoot,MaterialCost,LaborCost,Tax,Total");

                for(Order o : m.values()) {
                    out.println(this.marshalOrder(o));
                }
            }

        } catch (IOException e) {
            throw new RuntimeException("Could not write orders for " + String.valueOf(date), e);
        }
    }

    private LocalDate extractDateFromFileName(String fileName) {
        String mmddyyyy = fileName.substring("Orders_".length(), fileName.length() - 4);
        return LocalDate.parse(mmddyyyy, FILE_DATE);
    }

    private static String q(String s) {
        if (s == null) {
            return "";
        } else {
            boolean needQuotes = s.contains(",") || s.contains("\"") || s.contains("\n") || s.contains("\r");
            return !needQuotes ? s : "\"" + s.replace("\"", "\"\"") + "\"";
        }
    }

    private String marshalOrder(Order o) {
        int var10000 = o.getOrderNumber();
        return var10000 + "," + q(o.getCustomerName()) + "," + o.getState() + "," + String.valueOf(o.getTaxRate()) + "," + o.getProductType() + "," + String.valueOf(o.getArea()) + "," + String.valueOf(o.getCostPerSquareFoot()) + "," + String.valueOf(o.getLaborCostPerSquareFoot()) + "," + String.valueOf(o.getMaterialCost()) + "," + String.valueOf(o.getLaborCost()) + "," + String.valueOf(o.getTax()) + "," + String.valueOf(o.getTotal());
    }

    private static String unquote(String s) {
        s = s.trim();
        if (s.length() >= 2 && s.startsWith("\"") && s.endsWith("\"")) {
            s = s.substring(1, s.length() - 1).replace("\"\"", "\"");
        }

        return s;
    }

    private static BigDecimal toBD(String s) {
        s = unquote(s).trim();
        return !s.isEmpty() && !s.equalsIgnoreCase("null") ? new BigDecimal(s) : BigDecimal.ZERO;
    }

    private Order unmarshalOrder(String[] raw) {
        String[] c = raw.length == 1 ? raw[0].split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1) : raw;

        for(int i = 0; i < c.length; ++i) {
            c[i] = unquote(c[i]);
        }

        if (c.length < 11) {
            throw new RuntimeException("Orders row has " + c.length + " columns, expected >= 11. Row: " + String.join("|", c));
        } else {
            String orderNumStr;
            String name;
            String state;
            int startIdx;
            boolean legacyNoTaxRate;
            if (c.length == 11) {
                legacyNoTaxRate = true;
                int nameEnd = c.length - 10;
                orderNumStr = c[0].trim();
                name = String.join(",", (CharSequence[])Arrays.copyOfRange(c, 1, nameEnd + 1)).trim();
                startIdx = nameEnd + 1;
                state = c[startIdx++].trim();
            } else {
                legacyNoTaxRate = false;
                int nameEnd = c.length - 11;
                orderNumStr = c[0].trim();
                name = String.join(",", (CharSequence[])Arrays.copyOfRange(c, 1, nameEnd + 1)).trim();
                startIdx = nameEnd + 1;
                state = c[startIdx++].trim();
            }

            Order o = new Order();
            o.setOrderNumber(Integer.parseInt(orderNumStr));
            o.setCustomerName(name);
            o.setState(state);
            if (legacyNoTaxRate) {
                o.setTaxRate(BigDecimal.ZERO);
                o.setProductType(c[startIdx++].trim());
            } else {
                o.setTaxRate(toBD(c[startIdx++]));
                o.setProductType(c[startIdx++].trim());
            }

            o.setArea(toBD(c[startIdx++]));
            o.setCostPerSquareFoot(toBD(c[startIdx++]));
            o.setLaborCostPerSquareFoot(toBD(c[startIdx++]));
            o.setMaterialCost(toBD(c[startIdx++]));
            o.setLaborCost(toBD(c[startIdx++]));
            o.setTax(toBD(c[startIdx++]));
            o.setTotal(toBD(c[startIdx++]));
            return o;
        }
    }
}
