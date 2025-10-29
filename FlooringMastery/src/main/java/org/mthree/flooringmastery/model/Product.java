//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.mthree.flooringmastery.model;

import java.math.BigDecimal;
import java.util.Objects;

public class Product {
    private String productType;
    private BigDecimal costPerSquareFoot;
    private BigDecimal laborCostPerSquareFoot;

    public String getProductType() {
        return this.productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public BigDecimal getCostPerSquareFoot() {
        return this.costPerSquareFoot;
    }

    public void setCostPerSquareFoot(BigDecimal costPerSquareFoot) {
        this.costPerSquareFoot = costPerSquareFoot;
    }

    public BigDecimal getLaborCostPerSquareFoot() {
        return this.laborCostPerSquareFoot;
    }

    public void setLaborCostPerSquareFoot(BigDecimal laborCostPerSquareFoot) {
        this.laborCostPerSquareFoot = laborCostPerSquareFoot;
    }

    public boolean equals(Object o) {
        if (o != null && this.getClass() == o.getClass()) {
            Product product = (Product)o;
            return Objects.equals(this.productType, product.productType) && Objects.equals(this.costPerSquareFoot, product.costPerSquareFoot) && Objects.equals(this.laborCostPerSquareFoot, product.laborCostPerSquareFoot);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.productType, this.costPerSquareFoot, this.laborCostPerSquareFoot});
    }

    public String toString() {
        String var10000 = this.productType;
        return "Products{productType='" + var10000 + "', costPerSquareFoot=" + String.valueOf(this.costPerSquareFoot) + ", laborCostPerSquareFoot=" + String.valueOf(this.laborCostPerSquareFoot) + "}";
    }
}
