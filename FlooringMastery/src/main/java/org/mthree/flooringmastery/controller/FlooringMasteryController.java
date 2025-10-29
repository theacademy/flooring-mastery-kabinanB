//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.mthree.flooringmastery.controller;

import java.time.LocalDate;
import java.util.List;
import org.mthree.flooringmastery.dao.FlooringMasteryPersistenceException;
import org.mthree.flooringmastery.model.Order;
import org.mthree.flooringmastery.model.Product;
import org.mthree.flooringmastery.model.Tax;
import org.mthree.flooringmastery.service.FlooringMasteryServiceLayer;
import org.mthree.flooringmastery.ui.FlooringMasteryView;

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
            while(keepGoing) {
                int selection = this.view.printMenuAndGetSelection();
                switch (selection) {
                    case 1:
                        this.displayOrders();
                        break;
                    case 2:
                        this.addOrder();
                        break;
                    case 3:
                        this.editOrder();
                        break;
                    case 4:
                        this.removeOrder();
                        break;
                    case 5:
                        this.exportAllData();
                        break;
                    case 6:
                        keepGoing = false;
                        break;
                    default:
                        this.unknownCommand();
                }
            }

            this.exitMessage();
        } catch (FlooringMasteryPersistenceException e) {
            this.view.displayErrorMessage(e.getMessage());
        }

    }

    private void displayOrders() throws FlooringMasteryPersistenceException {
        LocalDate date = this.view.promptAnyDate("Enter an order date (MM-dd-yyyy): ");

        try {
            List<Order> orders = this.service.getOrdersForDate(date);
            if (orders != null || !orders.isEmpty()) {
                this.view.displayOrders(orders);
            }

        } catch (NullPointerException var3) {
            this.view.displayErrorMessage("No orders found for that date.");
        }
    }

    private void addOrder() throws FlooringMasteryPersistenceException {
        this.view.displayAddOrderBanner();
        List<Tax> taxes = this.service.getTaxes();
        List<Product> products = this.service.getProducts();
        Order draft = this.view.getAddOrderInput(taxes, products);
        draft.setOrderNumber(this.service.getNextOrderNumber());
        if (!this.view.getConfirmation()) {
            this.view.displayErrorMessage("Order was not placed.");
        } else {
            Order saved = this.service.addOrder(draft);
            this.view.displayOrderInfo(saved);
            this.view.displayAddOrderSuccess();
        }
    }

    private void editOrder() throws FlooringMasteryPersistenceException {
        this.view.displayEditOrderBanner();
        List<Tax> taxes = this.service.getTaxes();
        List<Product> products = this.service.getProducts();
        LocalDate date = this.view.promptAnyDate("Enter the date of the order to edit (MM-dd-yyyy): ");
        List<Order> ordersForDate = this.service.getOrdersForDate(date);
        Order edited = this.view.getEditOrderInput(ordersForDate, taxes, products);
        if (edited != null) {
            Order original = this.service.getOrder(date, edited.getOrderNumber());
            if (original == null) {
                this.view.displayErrorMessage("Order not found.");
            } else {
                original.setCustomerName(edited.getCustomerName());
                original.setState(edited.getState());
                original.setTaxRate(edited.getTaxRate());
                original.setProductType(edited.getProductType());
                original.setCostPerSquareFoot(edited.getCostPerSquareFoot());
                original.setLaborCostPerSquareFoot(edited.getLaborCostPerSquareFoot());
                original.setArea(edited.getArea());
                if (!this.view.getConfirmation()) {
                    this.view.displayErrorMessage("Edit cancelled.");
                } else {
                    Order after = this.service.editOrder(date, original.getOrderNumber());
                    this.view.displayOrderInfo(after);
                    this.view.displayEditOrderSuccess();
                }
            }
        }
    }

    private void removeOrder() throws FlooringMasteryPersistenceException {
        this.view.displayRemoveOrderBanner();
        LocalDate date = this.view.promptAnyDate("Enter the date of the order to remove (MM-dd-yyyy): ");
        List<Order> ordersForDate = this.service.getOrdersForDate(date);
        if (ordersForDate.isEmpty()) {
            this.view.displayErrorMessage("No orders found for that date.");
        } else {
            int orderNumber = this.view.getOrderNumberInput(ordersForDate);
            if (!this.view.getConfirmation()) {
                this.view.displayErrorMessage("Removal cancelled.");
            } else {
                Order removed = this.service.removeOrder(date, orderNumber);
                if (removed == null) {
                    this.view.displayErrorMessage("Order not found.");
                } else {
                    this.view.displayOrderInfo(removed);
                    this.view.displayRemoveOrderSuccess();
                }
            }
        }
    }

    private void exportAllData() throws FlooringMasteryPersistenceException {
        if (!this.view.getConfirmation()) {
            this.view.displayErrorMessage("Export cancelled.");
        } else {
            this.service.exportData();
            this.view.displayExportDataSuccess();
        }
    }

    private void unknownCommand() {
        this.view.displayUnknownCommandMessage();
    }

    private void exitMessage() {
        this.view.displayExitMessage();
    }
}
