package com.happyshop.checkout.paypal;

public interface PaypalService {
    boolean validateOrder(String orderId) throws PayPalApiException;
}
