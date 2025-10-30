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


    /**
     * The main brain, syntax inspired by ClassRoster
     */
    public void run() {
        boolean keepGoing = true;

        try {

            while(keepGoing) {
                int selection = this.view.printMenuAndGetSelection();
                switch (selection) {
                    case 1:
                        displayOrders();
                        break;
                    case 2:
                        addOrder();
                        break;
                    case 3:
                        editOrder();
                        break;
                    case 4:
                        removeOrder();
                        break;
                    case 5:
                        exportAllData();
                        break;
                    case 6:
                        keepGoing = false;
                        break;
                    default:
                        unknownCommand();
                }
            }

            this.exitMessage();
        } catch (FlooringMasteryPersistenceException e) {
            this.view.displayErrorMessage(e.getMessage());
        }

    }


    /*
    Display any orders given the date
    if not found or invalid input the view handles this error
     */
    private void displayOrders() throws FlooringMasteryPersistenceException {
        LocalDate date = this.view.promptAnyDate("Enter an order date (MM-dd-yyyy): ");

        try {
            List<Order> orders = service.getOrdersForDate(date);
            if (!orders.isEmpty()) {
                this.view.displayOrders(orders);
            }
            else
            {
                view.displayErrorMessage("No orders found for that date.");


            }

        } catch (NullPointerException var3) {
            this.view.displayErrorMessage("No orders found for that date.");
        }
    }

    /*
    Adding an order given previous orders.
    Looks up into file for infos about Products and Taxes

     */
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


    /**
     * Edits any order in the folder
     * Current Implementation is not great
     * @throws FlooringMasteryPersistenceException
     */
    private void editOrder() throws FlooringMasteryPersistenceException {
        this.view.displayEditOrderBanner();
        List<Tax> taxes = this.service.getTaxes();
        List<Product> products = this.service.getProducts();
        LocalDate date = this.view.promptAnyDate("Enter the date of the order to edit (MM-dd-yyyy): ");
        List<Order> ordersForDate = this.service.getOrdersForDate(date);
        Order edited = view.getEditOrderInput(date, ordersForDate, taxes, products);
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

    /*
    Remove order given date an order number
     */

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

    /*
    Optional
     */
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
