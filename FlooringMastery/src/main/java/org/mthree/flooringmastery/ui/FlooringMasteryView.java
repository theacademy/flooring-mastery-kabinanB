//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.mthree.flooringmastery.ui;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.mthree.flooringmastery.model.Order;
import org.mthree.flooringmastery.model.Product;
import org.mthree.flooringmastery.model.Tax;

public class FlooringMasteryView {
    private UserIO io;

    public FlooringMasteryView(UserIO io) {
        this.io = io;
    }

    public int printMenuAndGetSelection() {
        this.io.print("* * * * * * * * * * * * * * * * * * * * * * * * *");
        this.io.print("* <<Flooring Program>>");
        this.io.print("* 1. Display Orders");
        this.io.print("* 2. Add an Order ");
        this.io.print("* 3. Edit an Order");
        this.io.print("* 4. Remove an Order");
        this.io.print("* 5. Export All Data");
        this.io.print("* 6. Quit");
        this.io.print("*");
        this.io.print("* * * * * * * * * * * * * * * * * * * * * * * * *");
        return this.io.readInt("Please select from the above choices", 1, 6);
    }

    public void displayOrders(List<Order> orders) {
        for(Order currentOrder : orders) {
            String orderInfo = String.format("#%s : %s %s", currentOrder.getOrderNumber(), currentOrder.getCustomerName(), currentOrder.getState());
            this.io.print(orderInfo);
        }

        this.io.readString("Please hit enter to continue.");
    }

    public void displayOrderInfo(Order order) {
        String orderInfo = String.format("#%s : %s %s", order.getOrderNumber(), order.getCustomerName(), order.getState());
        this.io.print(orderInfo);
        this.io.readString("Please hit enter to continue.");
    }

    public void displayAddOrderBanner() {
        this.io.print("=== Add Order ===");
    }

    public Order getAddOrderInput(List<Tax> taxes, List<Product> products) {
        LocalDate date = this.promptFutureDate("Enter an Order Date. It must be in the future and in the format MM-dd-yyyy");
        String customerName = this.validateNameInput("Enter a valid Customer Name [a-z][A-Z][0-9] as well as periods and commas are accepted.");
        Tax state = this.validateStateInput("Enter a valid State or the Abbravetion", taxes);
        Product product = this.validateProductInput("Choose from these products listed", products);
        BigDecimal area = this.validateAreaInput("Enter a valid Area. Requirements: minimum 100 sq feet");
        Order order = new Order();
        order.setOrderDate(date);
        order.setCustomerName(customerName);
        order.setState(state.getStateName());
        order.setTaxRate(state.getTaxRate());
        order.setProductType(product.getProductType());
        order.setArea(area);
        order.setCostPerSquareFoot(product.getCostPerSquareFoot());
        return order;
    }

    public void displayAddOrderSuccess() {
        this.io.print("=== You have successfully added an order ===");
    }

    public void displayEditOrderBanner() {
        this.io.print("=== Edit Order ===");
    }

    public int getOrderNumberInput(List<Order> ordersForDate) {
        for(Order o : ordersForDate) {
            UserIO var10000 = this.io;
            int var10001 = o.getOrderNumber();
            var10000.print("Order #" + var10001 + " - " + o.getCustomerName() + " (" + o.getProductType() + ", $" + String.valueOf(o.getTotal()) + ")");
        }

        int orderNumber = 0;
        boolean valid = false;

        while(!valid) {
            orderNumber = this.io.readInt("Enter an Order Number from the list above: ");
            int finalOrderNumber = orderNumber;
            boolean exists = ordersForDate.stream().anyMatch((ox) -> ox.getOrderNumber() == finalOrderNumber);
            if (!exists) {
                this.io.print("Invalid order number. Please choose one from the list.");
            } else {
                valid = true;
            }
        }

        return orderNumber;
    }

    public Order getEditOrderInput(LocalDate date,
                                   List<Order> orders,
                                   List<Tax> taxes,
                                   List<Product> products) {

        int orderNumber = this.getOrderNumberInput(orders);

        List<Order> sameDate = orders.stream()
                .filter(o -> o.getOrderDate().isEqual(date))
                .collect(java.util.stream.Collectors.toList());

        if (sameDate.isEmpty()) {
            this.io.print("No orders found for this date.");
            return null;
        }

        Order original = sameDate.stream()
                .filter(o -> o.getOrderNumber() == orderNumber)
                .findFirst()
                .orElse(null);

        if (original == null) {
            this.io.print("Order not found.");
            return null;
        }

        String nameIn  = this.io.readString("Enter customer name (" + original.getCustomerName() + "): ").trim();
        String stateIn = this.io.readString("Enter state (" + original.getState() + "): ").trim();
        String prodIn  = this.io.readString("Enter product type (" + original.getProductType() + "): ").trim();
        String areaIn  = this.io.readString("Enter area (" + String.valueOf(original.getArea()) + "): ").trim();

        // Name: keep entered value or original (no interactive validation)
        String newName = nameIn.isEmpty() ? original.getCustomerName() : nameIn;

        // State: if provided, lookup by abbrev or full name; else keep original state/tax
        Tax newState;
        if (stateIn.isEmpty()) {
            newState = taxes.stream()
                    .filter(t -> t.getStateAbbreviation().equalsIgnoreCase(original.getState())
                            || t.getStateName().equalsIgnoreCase(original.getState()))
                    .findFirst().orElse(null);
            if (newState == null) {
                this.io.print("State not found.");
                return null;
            }
        } else {
            newState = taxes.stream()
                    .filter(t -> t.getStateAbbreviation().equalsIgnoreCase(stateIn)
                            || t.getStateName().equalsIgnoreCase(stateIn))
                    .findFirst().orElse(null);
            if (newState == null) {
                this.io.print("Invalid state.");
                return null;
            }
        }

        // Product: if provided, lookup; else keep original product
        Product newProduct;
        if (prodIn.isEmpty()) {
            newProduct = products.stream()
                    .filter(p -> p.getProductType().equalsIgnoreCase(original.getProductType()))
                    .findFirst().orElse(null);
            if (newProduct == null) {
                this.io.print("Product not found.");
                return null;
            }
        } else {
            newProduct = products.stream()
                    .filter(p -> p.getProductType().equalsIgnoreCase(prodIn))
                    .findFirst().orElse(null);
            if (newProduct == null) {
                this.io.print("Invalid product type.");
                return null;
            }
        }

        // Area: if provided, parse; else keep original
        BigDecimal newArea;
        if (areaIn.isEmpty()) {
            newArea = original.getArea();
        } else {
            try {
                newArea = new BigDecimal(areaIn);
            } catch (NumberFormatException ex) {
                this.io.print("Invalid area.");
                return null;
            }
        }

        Order edited = new Order();
        edited.setOrderDate(original.getOrderDate());
        edited.setOrderNumber(original.getOrderNumber());
        edited.setCustomerName(newName);
        edited.setState(newState.getStateAbbreviation());
        edited.setTaxRate(newState.getTaxRate());
        edited.setProductType(newProduct.getProductType());
        edited.setCostPerSquareFoot(newProduct.getCostPerSquareFoot());
        edited.setLaborCostPerSquareFoot(newProduct.getLaborCostPerSquareFoot());
        edited.setArea(newArea);
        return edited;
    }



    public void displayEditOrderSuccess() {
        this.io.print("=== You have successfully edited an order ===");
    }

    public void displayRemoveOrderBanner() {
        this.io.print("=== Remove an Order ===");
    }

    public boolean getConfirmation() {
        String answer;
        do {
            answer = this.io.readString("Do you want to place this order? (Yes/No)");
            if (!answer.equalsIgnoreCase("Yes") && !answer.equalsIgnoreCase("No")) {
                this.io.print("Please enter either Yes or No.");
            }
        } while(!answer.equalsIgnoreCase("Yes") && !answer.equalsIgnoreCase("No"));

        return answer.equalsIgnoreCase("Yes");
    }

    public void displayRemoveOrderSuccess() {
        this.io.print("=== You have successfully removed an order === ");
    }

    public void displayExportDataSuccess() {
        this.io.print("=== You have successfully exported data ===");
    }

    public void displayUnknownCommandMessage() {
        this.io.print("Unknown Command!!!");
    }

    public void displayExitMessage() {
        this.io.print("Good Bye!!!");
    }

    public void displayErrorMessage(String errorMsg) {
        this.io.print("=== ERROR ===");
        this.io.print(errorMsg);
        this.io.readString("Please hit enter to continue.");
    }

    public LocalDate promptAnyDate(String prompt) {
        return this.io.readLocalDate(prompt);
    }

    public LocalDate promptFutureDate(String prompt) {
        while(true) {
            LocalDate d = this.io.readLocalDate(prompt);
            if (d.isAfter(LocalDate.now())) {
                return d;
            }

            this.io.print("The date must be in the future.");
        }
    }

    private String validateNameInput(String prompt) {
        while(true) {
            String name = this.io.readString(prompt);
            Pattern p = Pattern.compile("[^a-zA-Z0-9., ]");
            if (name.isEmpty()) {
                this.io.print("Name may not be blank.");
            } else {
                if (!p.matcher(name).find()) {
                    return name;
                }

                this.io.print("Invalid characters. Allowed: letters, numbers, spaces, period, comma.");
            }
        }
    }

    private Tax validateStateInput(String prompt, List<Tax> taxes) {
        Tax tax = null;

        while(tax == null) {
            String input = this.io.readString(prompt).trim();

            for(Tax t : taxes) {
                if (t.getStateName().equalsIgnoreCase(input) || t.getStateAbbreviation().equalsIgnoreCase(input)) {
                    tax = t;
                    break;
                }
            }

            if (tax == null) {
                this.io.print("Invalid state. Valid options are:");

                for(Tax t : taxes) {
                    UserIO var10000 = this.io;
                    String var10001 = t.getStateAbbreviation();
                    var10000.print(var10001 + " - " + t.getStateName());
                }
            }
        }

        return tax;
    }

    private Product validateProductInput(String prompt, List<Product> products) {
        Product product = null;

        while(product == null) {
            this.io.print("=== Available Products ===");
            StringBuilder sb = new StringBuilder();

            for(Product p : products) {
                sb.append(String.format("%s : Cost $%s | Labor $%s%n", p.getProductType(), p.getCostPerSquareFoot(), p.getLaborCostPerSquareFoot()));
            }

            this.io.print(sb.toString());
            String input = this.io.readString(prompt);
            Optional<Product> match = products.stream().filter((px) -> px.getProductType().equalsIgnoreCase(input)).findFirst();
            if (match.isPresent()) {
                product = (Product)match.get();
            } else {
                this.io.print("Invalid product type. Please choose one from the list above.");
            }
        }

        return product;
    }

    private BigDecimal validateAreaInput(String prompt) {
        boolean inputValue = true;
        BigDecimal area = null;

        while(inputValue) {
            try {
                String areaString = this.io.readString(prompt);
                int areaInteger = Integer.parseInt(areaString);
                if (areaInteger >= 100) {
                    area = new BigDecimal(areaString);
                    inputValue = false;
                }
            } catch (NumberFormatException e) {
                this.io.print("Please input a valid number where the area is minimum 100" + e.getMessage());
            }
        }

        return area;
    }
}
