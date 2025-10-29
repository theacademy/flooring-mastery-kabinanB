//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.mthree.flooringmastery.model;

import java.math.BigDecimal;
import java.util.Objects;

public class Tax {
    private String stateAbbreviation;
    private String stateName;
    private BigDecimal taxRate;

    public String getStateAbbreviation() {
        return this.stateAbbreviation;
    }

    public void setStateAbbreviation(String stateAbbreviation) {
        this.stateAbbreviation = stateAbbreviation;
    }

    public String getStateName() {
        return this.stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public BigDecimal getTaxRate() {
        return this.taxRate;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

    public boolean equals(Object o) {
        if (o != null && this.getClass() == o.getClass()) {
            Tax tax = (Tax)o;
            return Objects.equals(this.stateAbbreviation, tax.stateAbbreviation) && Objects.equals(this.stateName, tax.stateName) && Objects.equals(this.taxRate, tax.taxRate);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.stateAbbreviation, this.stateName, this.taxRate});
    }

    public String toString() {
        String var10000 = this.stateAbbreviation;
        return "Taxes{stateAbbreviation='" + var10000 + "', stateName='" + this.stateName + "', taxRate=" + String.valueOf(this.taxRate) + "}";
    }
}
