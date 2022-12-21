package com.happyshop.checkout;

import java.util.List;

import org.springframework.stereotype.Service;

import com.happyshop.common.entity.CartItem;
import com.happyshop.common.entity.ShippingRate;
import com.happyshop.common.entity.product.Product;

@Service
public class CheckoutServiceImpl implements CheckoutService {
    private static final int DIM_DIVISOR = 319;
    
    public CheckoutInfo prepareCheckout(List<CartItem> cartItems, ShippingRate sr) {
        CheckoutInfo c = new CheckoutInfo();
        float cost = calculateTotalProductCost(cartItems);
        float productTotal = calculateProductTotal(cartItems);
        float shippingCostTotal = calculateShippingCostTotal(cartItems, sr);
        float paymentTotal = productTotal + shippingCostTotal;
        
        c.setProductCost(cost);
        c.setProductTotal(productTotal);
        c.setShippingCostTotal(shippingCostTotal);
        c.setPaymentTotal(paymentTotal);
        c.setCodSupported(sr.isCodSupported());
        c.setDeliverDays(sr.getDays());
        
        return c;
    }

    private float calculateShippingCostTotal(List<CartItem> cartItems, ShippingRate sr) {
        float shippingCostTotal  = 0;
        
        for (CartItem item : cartItems) {
            Product product = item.getProduct();
            float dimWeight = (product.getLength() * product.getWidth() * product.getHeight()) / DIM_DIVISOR;
            float finalWeight = product.getWeight() > dimWeight ? product.getWeight() : dimWeight;
            float shippingCost = finalWeight * item.getQuantity() * sr.getRate();
            
            item.setShippingCost(shippingCost); 
                   
            shippingCostTotal += shippingCost;
        }
        
        return shippingCostTotal;
    }

    private float calculateProductTotal(List<CartItem> cartItems) {
        float total = 0;
        for (CartItem cartItem : cartItems) {
            total += cartItem.getSubTotal();
        }
        return total;
    }

    private float calculateTotalProductCost(List<CartItem> cartItems) {
        float cost = 0;
        for (CartItem cartItem : cartItems) {
            cost += cartItem.getProduct().getCost();
        }
        return cost;
    }
    
    
    
    
}
