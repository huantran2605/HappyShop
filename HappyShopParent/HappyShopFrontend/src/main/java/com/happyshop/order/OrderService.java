package com.happyshop.order;

import java.util.List;

import com.happyshop.checkout.CheckoutInfo;
import com.happyshop.common.entity.Address;
import com.happyshop.common.entity.CartItem;
import com.happyshop.common.entity.Customer;
import com.happyshop.common.entity.order.PaymentMethod;

public interface OrderService {
    void createOrder(Customer customer, Address address, List<CartItem> cartItems,
            PaymentMethod paymentMethod, CheckoutInfo checkoutInfo);
}
