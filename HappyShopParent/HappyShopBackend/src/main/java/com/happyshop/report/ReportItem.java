package com.happyshop.report;

import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportItem {
    
    private String identifier;
    private float grossSales;
    private float netSales;
    private int ordersCount;
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ReportItem other = (ReportItem) obj;
        return Objects.equals(identifier, other.identifier);
    }
    @Override
    public int hashCode() {
        return Objects.hash(identifier);
    }
    public ReportItem(String identifier) {
        this.identifier = identifier;
    }
    
    public void  addGrowwSales(float amount) {
        this.grossSales += amount;
    }
    public void  addNetSales(float amount) {
        this.netSales += amount;
    }
    public void  plusCountOrder() {
        this.ordersCount++;
    }
    
    
}
