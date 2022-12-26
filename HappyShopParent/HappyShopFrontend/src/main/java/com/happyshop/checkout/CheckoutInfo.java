package com.happyshop.checkout;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class CheckoutInfo {
    private float productCost;
    private float productTotal;
    private float shippingCostTotal;
    private float paymentTotal;
    private int deliverDays;
    private boolean codSupported;

    public Date getDeliverDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, deliverDays);
        
        return calendar.getTime();
    }
    
    public String getPaymentTotal4PayPal() {
        DecimalFormat formatter = new DecimalFormat("###,###.##");
        return formatter.format(paymentTotal);
    }

}

