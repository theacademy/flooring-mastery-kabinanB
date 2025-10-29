//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.mthree.flooringmastery.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class Order {
    private int OrderNumber;
    private String CustomerName;
    private String State;
    private LocalDate orderDate;
    private BigDecimal TaxRate;
    private String ProductType;
    private BigDecimal Area;
    private BigDecimal CostPerSquareFoot;
    private BigDecimal LaborCostPerSquareFoot;
    private BigDecimal MaterialCost;
    private BigDecimal LaborCost;
    private BigDecimal Tax;
    private BigDecimal Total;

    public int getOrderNumber() {
        return this.OrderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.OrderNumber = orderNumber;
    }

    public String getCustomerName() {
        return this.CustomerName;
    }

    public void setCustomerName(String customerName) {
        this.CustomerName = customerName;
    }

    public String getState() {
        return this.State;
    }

    public void setState(String state) {
        this.State = state;
    }

    public BigDecimal getTaxRate() {
        return this.TaxRate;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.TaxRate = taxRate;
    }

    public String getProductType() {
        return this.ProductType;
    }

    public void setProductType(String productType) {
        this.ProductType = productType;
    }

    public BigDecimal getArea() {
        return this.Area;
    }

    public void setArea(BigDecimal area) {
        this.Area = area;
    }

    public BigDecimal getCostPerSquareFoot() {
        return this.CostPerSquareFoot;
    }

    public void setCostPerSquareFoot(BigDecimal costPerSquareFoot) {
        this.CostPerSquareFoot = costPerSquareFoot;
    }

    public BigDecimal getLaborCostPerSquareFoot() {
        return this.LaborCostPerSquareFoot;
    }

    public void setLaborCostPerSquareFoot(BigDecimal laborCostPerSquareFoot) {
        this.LaborCostPerSquareFoot = laborCostPerSquareFoot;
    }

    public BigDecimal getMaterialCost() {
        return this.MaterialCost;
    }

    public void setMaterialCost(BigDecimal materialCost) {
        this.MaterialCost = materialCost;
    }

    public BigDecimal getLaborCost() {
        return this.LaborCost;
    }

    public void setLaborCost(BigDecimal laborCost) {
        this.LaborCost = laborCost;
    }

    public BigDecimal getTax() {
        return this.Tax;
    }

    public void setTax(BigDecimal tax) {
        this.Tax = tax;
    }

    public BigDecimal getTotal() {
        return this.Total;
    }

    public void setTotal(BigDecimal total) {
        this.Total = total;
    }

    public LocalDate getOrderDate() {
        return this.orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public boolean equals(Object o) {
        if (o != null && this.getClass() == o.getClass()) {
            Order order = (Order)o;
            return this.OrderNumber == order.OrderNumber && Objects.equals(this.CustomerName, order.CustomerName) && Objects.equals(this.State, order.State) && Objects.equals(this.orderDate, order.orderDate) && Objects.equals(this.TaxRate, order.TaxRate) && Objects.equals(this.ProductType, order.ProductType) && Objects.equals(this.Area, order.Area) && Objects.equals(this.CostPerSquareFoot, order.CostPerSquareFoot) && Objects.equals(this.LaborCostPerSquareFoot, order.LaborCostPerSquareFoot) && Objects.equals(this.MaterialCost, order.MaterialCost) && Objects.equals(this.LaborCost, order.LaborCost) && Objects.equals(this.Tax, order.Tax) && Objects.equals(this.Total, order.Total);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.OrderNumber, this.CustomerName, this.State, this.orderDate, this.TaxRate, this.ProductType, this.Area, this.CostPerSquareFoot, this.LaborCostPerSquareFoot, this.MaterialCost, this.LaborCost, this.Tax, this.Total});
    }

    public String toString() {
        int var10000 = this.OrderNumber;
        return "Order{OrderNumber=" + var10000 + ", CustomerName='" + this.CustomerName + "', State='" + this.State + "', orderDate=" + String.valueOf(this.orderDate) + ", TaxRate=" + String.valueOf(this.TaxRate) + ", ProductType='" + this.ProductType + "', Area=" + String.valueOf(this.Area) + ", CostPerSquareFoot=" + String.valueOf(this.CostPerSquareFoot) + ", LaborCostPerSquareFoot=" + String.valueOf(this.LaborCostPerSquareFoot) + ", MaterialCost=" + String.valueOf(this.MaterialCost) + ", LaborCost=" + String.valueOf(this.LaborCost) + ", Tax=" + String.valueOf(this.Tax) + ", Total=" + String.valueOf(this.Total) + "}";
    }
}
