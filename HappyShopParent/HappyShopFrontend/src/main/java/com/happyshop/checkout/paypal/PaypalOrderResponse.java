package com.happyshop.checkout.paypal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaypalOrderResponse {
    private String id;
    private String status;
    
    public  boolean validate(String id) {
        return id.equals(id) &&  status.equals("COMPLETED");
    }
    
}
