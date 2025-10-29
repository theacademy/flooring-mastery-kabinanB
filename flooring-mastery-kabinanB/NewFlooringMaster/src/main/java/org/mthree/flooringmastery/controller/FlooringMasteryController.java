package org.mthree.flooringmastery.controller;

import org.mthree.flooringmastery.dao.FlooringMasteryPersistenceException;
import org.mthree.flooringmastery.model.Order;
import org.mthree.flooringmastery.model.Product;
import org.mthree.flooringmastery.model.Tax;
import org.mthree.flooringmastery.service.FlooringMasteryServiceLayer;
import org.mthree.flooringmastery.ui.FlooringMasteryView;

import java.time.LocalDate;
import java.util.List;

public class FlooringMasteryController {

    private final FlooringMasteryView view;
    private final FlooringMasteryServiceLayer service;

    public FlooringMasteryController(FlooringMasteryView view, FlooringMasteryServiceLayer service) {
        this.view = view;
        this.service = service;
    }

    public void run() {
        boolean keepGoing = true;

        try {
            while (keepGoing) {
                int selection = view.printMenuAndGetSelection();
                switch (selection) {
                    case 1 -> displayOrders();
                    case 2 -> addOrder();
                    case 3 -> editOrder();
                    case 4 -> removeOrder();
                    case 5 -> exportAllData();
                    case 6 -> keepGoing = false;
                    default -> unknownCommand();
                }
            }
            exitMessage();
        } catch (FlooringMasteryPersistenceException e) {
            view.displayErrorMessage(e.getMessage());
        }
    }

    // ========== Case 1: Display ==========
    private void displayOrders() throws FlooringMasteryPersistenceException {
        LocalDate date = view.promptAnyDate("Enter an order date (MM-dd-yyyy): ");
        try
        {
            var orders = service.getOrdersForDate(date);
            if(orders!=null || !orders.isEmpty())
            {

                view.displayOrders(orders);
            }

        }catch(NullPointerException e)
        {
            view.displayErrorMessage("No orders found for that date.");
            return;
        }
    }

    // ========== Case 2: Add ==========
    private void addOrder() throws FlooringMasteryPersistenceException {
        view.displayAddOrderBanner();

        List<Tax> taxes = service.getTaxes();
        List<Product> products = service.getProducts();

        // Let the view collect + validate inputs
        Order draft = view.getAddOrderInput(taxes, products);

        // Assign next order number before sending to service
        draft.setOrderNumber(service.getNextOrderNumber());

        // Optional confirmation before persisting
        if (!view.getConfirmation()) {
            view.displayErrorMessage("Order was not placed.");
            return;
        }

        Order saved = service.addOrder(draft);   // service will calculate material/labor/tax/total
        view.displayOrderInfo(saved);
        view.displayAddOrderSuccess();
    }

    // ========== Case 3: Edit ==========
    private void editOrder() throws FlooringMasteryPersistenceException {
        view.displayEditOrderBanner();

        // Get lists needed for validation prompts
        List<Tax> taxes = service.getTaxes();
        List<Product> products = service.getProducts();

        // First, pick the date to narrow the list shown to the user
        LocalDate date = view.promptAnyDate("Enter the date of the order to edit (MM-dd-yyyy): ");
        List<Order> ordersForDate = service.getOrdersForDate(date);

        // View will display the (date-filtered) orders, ask for number,
        // and return a NEW 'edited' Order carrying the user’s changes.
        Order edited = view.getEditOrderInput(ordersForDate, taxes, products);
        if (edited == null) {
            // View already printed a message (no orders for that date / not found).
            return;
        }

        // Fetch the original from DAO via service
        Order original = service.getOrder(date, edited.getOrderNumber());
        if (original == null) {
            view.displayErrorMessage("Order not found.");
            return;
        }

        // Copy user-mutated fields onto the original; service will re-price on edit()
        original.setCustomerName(edited.getCustomerName());
        original.setState(edited.getState());
        original.setTaxRate(edited.getTaxRate());
        original.setProductType(edited.getProductType());
        original.setCostPerSquareFoot(edited.getCostPerSquareFoot());
        original.setLaborCostPerSquareFoot(edited.getLaborCostPerSquareFoot());
        original.setArea(edited.getArea());

        // Optional confirmation
        if (!view.getConfirmation()) {
            view.displayErrorMessage("Edit cancelled.");
            return;
        }

        // Persist + re-calc
        Order after = service.editOrder(date, original.getOrderNumber());
        view.displayOrderInfo(after);
        view.displayEditOrderSuccess();
    }

    // ========== Case 4: Remove ==========
    private void removeOrder() throws FlooringMasteryPersistenceException {
        view.displayRemoveOrderBanner();

        LocalDate date = view.promptAnyDate("Enter the date of the order to remove (MM-dd-yyyy): ");
        List<Order> ordersForDate = service.getOrdersForDate(date);
        if (ordersForDate.isEmpty()) {
            view.displayErrorMessage("No orders found for that date.");
            return;
        }

        int orderNumber = view.getOrderNumberInput(ordersForDate);

        if (!view.getConfirmation()) {
            view.displayErrorMessage("Removal cancelled.");
            return;
        }

        Order removed = service.removeOrder(date, orderNumber);
        if (removed == null) {
            view.displayErrorMessage("Order not found.");
            return;
        }

        view.displayOrderInfo(removed);
        view.displayRemoveOrderSuccess();
    }

    // ========== Case 5: Export ==========
    private void exportAllData() throws FlooringMasteryPersistenceException {
        if (!view.getConfirmation()) {
            view.displayErrorMessage("Export cancelled.");
            return;
        }
        service.exportData();
        view.displayExportDataSuccess();
    }

    // ========== Misc ==========
    private void unknownCommand() {
        view.displayUnknownCommandMessage();
    }

    private void exitMessage() {
        view.displayExitMessage();
    }
}
