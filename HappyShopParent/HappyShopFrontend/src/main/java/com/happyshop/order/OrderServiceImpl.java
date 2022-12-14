package com.happyshop.order;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.happyshop.checkout.CheckoutInfo;
import com.happyshop.common.entity.Address;
import com.happyshop.common.entity.CartItem;
import com.happyshop.common.entity.Customer;
import com.happyshop.common.entity.order.Order;
import com.happyshop.common.entity.order.OrderDetail;
import com.happyshop.common.entity.order.OrderStatus;
import com.happyshop.common.entity.order.OrderTrack;
import com.happyshop.common.entity.order.PaymentMethod;
import com.happyshop.common.entity.product.Product;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    OrderRepository repo;
    
    public Order createOrder(Customer customer, Address address, List<CartItem> cartItems,
            PaymentMethod paymentMethod, CheckoutInfo checkoutInfo) {
        Order newOrder  = new Order();  
        
        newOrder.setCustomer(customer);
        if(address == null) {
            newOrder.copyAddressFromCustomer();
        }
        else {  
            newOrder.copyShippingAddress(address);
        }
        newOrder.setOrderTime(new Date()); 
        if(paymentMethod.equals(PaymentMethod.PAYPAL)) {
            newOrder.setStatus(OrderStatus.PAID);
        }
        else{
            newOrder.setStatus(OrderStatus.NEW);
        }
        
        newOrder.setShippingCost(checkoutInfo.getShippingCostTotal());
        newOrder.setProductCost(checkoutInfo.getProductCost());
        newOrder.setSubtotal(checkoutInfo.getProductTotal());
        newOrder.setTotal(checkoutInfo.getPaymentTotal());
        newOrder.setTax(0);
        newOrder.setPaymentMethod(paymentMethod);
        newOrder.setDeliverDays(checkoutInfo.getDeliverDays());
        newOrder.setDeliverDate(checkoutInfo.getDeliverDate());
        
        Set<OrderDetail> orderDetails = newOrder.getOrderDetails();
        
        for (CartItem cartItem : cartItems) {
            OrderDetail od = new OrderDetail();
            Product product = cartItem.getProduct();
            od.setProduct(product);
            od.setOrder(newOrder);
            od.setQuantity(cartItem.getQuantity());
            od.setProductCost(product.getCost() * product.getQuantity());
            od.setShippingCost(cartItem.getShippingCost());
            od.setUnitPrice(product.getDiscountPrice());
            od.setSubtotal(cartItem.getSubTotal());
            
            orderDetails.add(od);
        }
        
        List<OrderTrack> orderTracks = newOrder.getOrderTracks();
        OrderTrack ot = new OrderTrack();
        ot.setUpdatedTime(new Date());
        ot.setStatus(OrderStatus.NEW);
        ot.setOrder(newOrder);
        ot.setNote(OrderStatus.NEW.defaultDescription());
        orderTracks.add(ot);
        newOrder.setOrderTracks(orderTracks);
        
        return repo.save(newOrder);
    }
    
}
