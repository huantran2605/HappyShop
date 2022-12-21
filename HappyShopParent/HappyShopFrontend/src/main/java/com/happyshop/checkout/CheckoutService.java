package com.happyshop.checkout;

import java.util.List;

import com.happyshop.common.entity.CartItem;
import com.happyshop.common.entity.ShippingRate;

public interface CheckoutService {
    CheckoutInfo prepareCheckout(List<CartItem> cartItems, ShippingRate sr);
         
    
}
