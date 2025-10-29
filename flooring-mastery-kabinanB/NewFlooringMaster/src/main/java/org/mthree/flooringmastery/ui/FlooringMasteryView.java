package org.mthree.flooringmastery.ui;

import org.mthree.flooringmastery.model.Order;
import org.mthree.flooringmastery.model.Product;
import org.mthree.flooringmastery.model.Tax;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


public class FlooringMasteryView {
    private UserIO io;


    public FlooringMasteryView(UserIO io) {
        this.io = io;
    }

    public int printMenuAndGetSelection()
    {
        io.print("* * * * * * * * * * * * * * * * * * * * * * * * *");
        io.print("* <<Flooring Program>>");
        io.print("* 1. Display Orders");
        io.print("* 2. Add an Order ");
        io.print("* 3. Edit an Order");
        io.print("* 4. Remove an Order");
        io.print("* 5. Export All Data");
        io.print("* 6. Quit");
        io.print("*");
        io.print("* * * * * * * * * * * * * * * * * * * * * * * * *");

        return io.readInt("Please select from the above choices", 1, 6);
    }

    public void displayOrders(List<Order> orders)
    {
        //Listing each order
        for(Order currentOrder : orders)
        {
            String orderInfo = String.format("#%s : %s %s",
                    currentOrder.getOrderNumber(),
                    currentOrder.getCustomerName(),
                    currentOrder.getState());
            io.print(orderInfo);
        }

        io.readString("Please hit enter to continue.");
    }

    public void displayOrderInfo(Order order)
    {
        //Listing one order
        String orderInfo = String.format("#%s : %s %s",
                order.getOrderNumber(),
                order.getCustomerName(),
                order.getState());

        io.print(orderInfo);
        io.readString("Please hit enter to continue.");

    }

    public void displayAddOrderBanner()
    {
        io.print("=== Add Order ===");
    }



    public Order getAddOrderInput(List<Tax> taxes, List<Product> products)
    {
        LocalDate date = promptFutureDate("Enter an Order Date. It must be in the future and in the format MM-dd-yyyy");
        String customerName = validateNameInput("Enter a valid Customer Name [a-z][A-Z][0-9] as well as periods and commas are accepted.");

        //Both these functions require reading from taxes.txt and products.txt so we let the dao package to handle it?
        //REQUIRES READING
        Tax state = validateStateInput("Enter a valid State or the Abbravetion", taxes);
        Product product = validateProductInput("Choose from these products listed", products);

        BigDecimal area = validateAreaInput("Enter a valid Area. Requirements: minimum 100 sq feet");

        Order order = new Order();


        order.setOrderDate(date);
        order.setCustomerName(customerName);
        order.setState(state.getStateName());
        order.setTaxRate(state.getTaxRate());
        order.setProductType(product.getProductType());
        order.setArea(area);
        order.setCostPerSquareFoot(product.getCostPerSquareFoot());


        //read that partcular date look at the orders if empty is 1, else max+1
        //REQUIRES READING
        //order.setOrderNumber();

        //

        return order;

    }

    public void displayAddOrderSuccess()
    {
        io.print("=== You have successfully added an order ===");
    }


//    public int getOrderNumberInput()
//    {
//        /**
//         *         int maxValue = 0;
//         *         for(Order o: orders )
//         *         {
//         *             if(maxValue < o.getOrderNumber())
//         *             {
//         *                 maxValue = o.getOrderNumber();
//         *             }
//         *         }
//         *
//         *         return maxValue + 1;
//         *
//         *
//         *
//         */
//
//        //We look into the object and see the order numbers
//        //We then select only these numbers. If selected outside throw and catch
//
//
//
//    }

    public void displayEditOrderBanner()
    {
        io.print("=== Edit Order ===");
    }

    public int getOrderNumberInput(List<Order> ordersForDate) {
        for (Order o : ordersForDate) {
            io.print("Order #" + o.getOrderNumber() + " - " + o.getCustomerName()
                    + " (" + o.getProductType() + ", $" + o.getTotal() + ")");
        }

        int orderNumber = 0;
        boolean valid = false;

        while (!valid) {
            orderNumber = io.readInt("Enter an Order Number from the list above: ");
            int finalOrderNumber = orderNumber;
            boolean exists = ordersForDate.stream()
                    .anyMatch(o -> o.getOrderNumber() == finalOrderNumber);

            if (!exists) {
                io.print("Invalid order number. Please choose one from the list.");
            } else {
                valid = true;
            }
        }

        return orderNumber;
    }

    public Order getEditOrderInput(List<Order> orders, List<Tax> taxes, List<Product> products)
    {

        LocalDate date = promptAnyDate("Please enter the date of the order");
        int orderNumber = getOrderNumberInput(orders);

//        for(Order o: orders)
//        {
//
//            if (o.getOrderNumber() == orderNumber && o.getOrderDate().isEqual(date))
//            {
//                String newName = validateNameInput("Enter customer name (" + o.getCustomerName() + "): ");
//                Tax newState = validateStateInput("Enter state name (" + o.getState() + "): ", taxes);
//                Product newProduct = validateProductInput("Enter product type (" + o.getProductType() + "): ", products);
//                BigDecimal newArea = validateAreaInput("Enter the area (" + o.getArea() + "): ");
//                return o;
//            }
//        }
//        return null;

        List<Order> sameDate = orders.stream().filter((o)->o.getOrderDate().isEqual(date)).collect(Collectors.toList());

        if(sameDate.isEmpty())
        {
            io.print("No orders found for this date.");
            return null;
        }

        Order original = sameDate.stream().filter((o)->o.getOrderNumber() == orderNumber).findFirst().orElse(null);

        if (original == null) {
            io.print("Order not found.");
            return null;
        }

        String nameIn   = io.readString("Enter customer name (" + original.getCustomerName() + "): ").trim();
        String stateIn  = io.readString("Enter state (" + original.getState() + "): ").trim();         // abbr or name
        String prodIn   = io.readString("Enter product type (" + original.getProductType() + "): ").trim();
        String areaIn   = io.readString("Enter area (" + original.getArea() + "): ").trim();

        /**
         * Tertiary operator
         */
        // resolve name (allow blank to keep)
        String newName = nameIn.isEmpty() ? original.getCustomerName()
                : validateNameInput("");// reuse your validation; ignore prompt arg if needed

        // resolve state (blank -> keep). Accept abbr or full name.
        Tax newState = stateIn.isEmpty()
                ? taxes.stream().filter(t ->
                        t.getStateAbbreviation().equalsIgnoreCase(original.getState()) ||
                                t.getStateName().equalsIgnoreCase(original.getState()))
                .findFirst().orElseThrow(null)
                : validateStateInput(stateIn, taxes);

        // resolve product (blank -> keep)
        Product newProduct = prodIn.isEmpty()
                ? products.stream().filter(p -> p.getProductType().equalsIgnoreCase(original.getProductType()))
                .findFirst().orElseThrow(null)
                : validateProductInput(prodIn, products);

        // resolve area (blank -> keep)
        BigDecimal newArea = areaIn.isEmpty()
                ? original.getArea()
                : validateAreaInput(areaIn);

        // ---- Build edited order for service to re-price ----
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

        // Do NOT compute material/labor/tax/total here. Service will recalc.
        return edited;
    }

    public void displayEditOrderSuccess()
    {
        io.print("=== You have successfully edited an order ===");
    }

    public void displayRemoveOrderBanner()
    {
        io.print("=== Remove an Order ===");
    }


    public boolean getConfirmation()
    {
        String answer;

        do {
            answer = io.readString("Do you want to place this order? (Yes/No)");
            if(!(answer.equalsIgnoreCase("Yes") || answer.equalsIgnoreCase("No")))
            {
                io.print("Please enter either Yes or No.");
            }
        }while(!(answer.equalsIgnoreCase("Yes") || answer.equalsIgnoreCase("No")));

        return answer.equalsIgnoreCase("Yes");

    }

    public void displayRemoveOrderSuccess()
    {
        io.print("=== You have successfully removed an order === ");
    }

    public void displayExportDataSuccess()
    {
        io.print("=== You have successfully exported data ===");
    }



    public void displayUnknownCommandMessage()
    {
        io.print("Unknown Command!!!");
    }

    public void displayExitMessage()
    {
        io.print("Good Bye!!!");
    }

    public void displayErrorMessage(String errorMsg) {
        io.print("=== ERROR ===");
        io.print(errorMsg);
    }




    //Private members

    // In FlooringMasteryView (public section)
    public LocalDate promptAnyDate(String prompt) {
        // just delegate to your io.readLocalDate without “future” validation
        return io.readLocalDate(prompt);
    }

    public LocalDate promptFutureDate(String prompt) {
        while (true) {
            LocalDate d = io.readLocalDate(prompt);
            if (!d.isAfter(LocalDate.now())) {
                io.print("The date must be in the future.");
                continue;
            }
            return d;
        }
    }


    private String validateNameInput(String prompt)
    {
        while(true)
        {
            String name = io.readString(prompt);
            /**
             * Reference from stackoverflow: https://stackoverflow.com/questions/8248277/how-to-determine-if-a-string-has-non-alphanumeric-characters
             */
            Pattern p = Pattern.compile("[^a-zA-Z0-9., ]");
            if(name.isEmpty())
            {
                io.print("Name may not be blank.");
                continue;
            }
            if(p.matcher(name).find())
            {
                io.print("Invalid characters. Allowed: letters, numbers, spaces, period, comma.");
                continue;
            }
            return name;

        }
    }

    private Tax validateStateInput(String prompt, List<Tax> taxes)
    {
        Tax tax = null;

        while(tax == null)
        {
            String input = io.readString(prompt).trim();
            for(Tax t: taxes)
            {
                if(t.getStateName().equalsIgnoreCase(input) || t.getStateAbbreviation().equalsIgnoreCase(input))
                {
                    tax = t;
                    break;
                }
            }

            if(tax == null)
            {
                io.print("Invalid state. Valid options are:");
                for(Tax t: taxes)
                {
                    io.print(t.getStateAbbreviation() + " - "+ t.getStateName());
                }
            }
        }

        return tax;

    }

    private Product validateProductInput(String prompt, List<Product> products) {
        Product product = null;

        while (product == null) {
            io.print("=== Available Products ===");

            // Build all product lines
            StringBuilder sb = new StringBuilder();
            for (Product p : products) {
                sb.append(String.format("%s : Cost $%s | Labor $%s%n",
                        p.getProductType(),
                        p.getCostPerSquareFoot(),
                        p.getLaborCostPerSquareFoot()));
            }

            io.print(sb.toString());

            String input = io.readString(prompt);

            Optional<Product> match = products.stream()
                    .filter(p -> p.getProductType().equalsIgnoreCase(input))
                    .findFirst();

            if (match.isPresent()) {
                product = match.get();
            } else {
                io.print("Invalid product type. Please choose one from the list above.");
            }
        }

        return product;
    }


    private BigDecimal validateAreaInput(String prompt)
    {
        //minimum should 100 and should be positive
        boolean inputValue = true;
        BigDecimal area = null;
        while(inputValue)
        {
            try
            {
                String areaString = io.readString(prompt);
                int areaInteger = Integer.parseInt(areaString);
                if(areaInteger >= 100)
                {
                    area = new BigDecimal(areaString);
                    inputValue = false;

                }

            }catch (NumberFormatException e)
            {
                io.print("Please input a valid number where the area is minimum 100" + e.getMessage());
            }
        }

        return area;
    }
}
