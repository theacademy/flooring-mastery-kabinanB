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
        return OrderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        OrderNumber = orderNumber;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public BigDecimal getTaxRate() {
        return TaxRate;
    }

    public void setTaxRate(BigDecimal taxRate) {
        TaxRate = taxRate;
    }

    public String getProductType() {
        return ProductType;
    }

    public void setProductType(String productType) {
        ProductType = productType;
    }

    public BigDecimal getArea() {
        return Area;
    }

    public void setArea(BigDecimal area) {
        Area = area;
    }

    public BigDecimal getCostPerSquareFoot() {
        return CostPerSquareFoot;
    }

    public void setCostPerSquareFoot(BigDecimal costPerSquareFoot) {
        CostPerSquareFoot = costPerSquareFoot;
    }

    public BigDecimal getLaborCostPerSquareFoot() {
        return LaborCostPerSquareFoot;
    }

    public void setLaborCostPerSquareFoot(BigDecimal laborCostPerSquareFoot) {
        LaborCostPerSquareFoot = laborCostPerSquareFoot;
    }

    public BigDecimal getMaterialCost() {
        return MaterialCost;
    }

    public void setMaterialCost(BigDecimal materialCost) {
        MaterialCost = materialCost;
    }

    public BigDecimal getLaborCost() {
        return LaborCost;
    }

    public void setLaborCost(BigDecimal laborCost) {
        LaborCost = laborCost;
    }

    public BigDecimal getTax() {
        return Tax;
    }

    public void setTax(BigDecimal tax) {
        Tax = tax;
    }

    public BigDecimal getTotal() {
        return Total;
    }

    public void setTotal(BigDecimal total) {
        Total = total;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return OrderNumber == order.OrderNumber && Objects.equals(CustomerName, order.CustomerName) && Objects.equals(State, order.State) && Objects.equals(orderDate, order.orderDate) && Objects.equals(TaxRate, order.TaxRate) && Objects.equals(ProductType, order.ProductType) && Objects.equals(Area, order.Area) && Objects.equals(CostPerSquareFoot, order.CostPerSquareFoot) && Objects.equals(LaborCostPerSquareFoot, order.LaborCostPerSquareFoot) && Objects.equals(MaterialCost, order.MaterialCost) && Objects.equals(LaborCost, order.LaborCost) && Objects.equals(Tax, order.Tax) && Objects.equals(Total, order.Total);
    }

    @Override
    public int hashCode() {
        return Objects.hash(OrderNumber, CustomerName, State, orderDate, TaxRate, ProductType, Area, CostPerSquareFoot, LaborCostPerSquareFoot, MaterialCost, LaborCost, Tax, Total);
    }

    @Override
    public String toString() {
        return "Order{" +
                "OrderNumber=" + OrderNumber +
                ", CustomerName='" + CustomerName + '\'' +
                ", State='" + State + '\'' +
                ", orderDate=" + orderDate +
                ", TaxRate=" + TaxRate +
                ", ProductType='" + ProductType + '\'' +
                ", Area=" + Area +
                ", CostPerSquareFoot=" + CostPerSquareFoot +
                ", LaborCostPerSquareFoot=" + LaborCostPerSquareFoot +
                ", MaterialCost=" + MaterialCost +
                ", LaborCost=" + LaborCost +
                ", Tax=" + Tax +
                ", Total=" + Total +
                '}';
    }
}
