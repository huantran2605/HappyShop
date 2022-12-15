package com.happyshop;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.happyshop.common.entity.Customer;
import com.happyshop.common.entity.Order;
import com.happyshop.common.entity.OrderDetail;
import com.happyshop.common.entity.OrderStatus;
import com.happyshop.common.entity.PaymentMethod;
import com.happyshop.common.entity.product.Product;
import com.happyshop.order.OrderRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class OrderRepositoryTests {
    @Autowired private OrderRepository repo;
    @Autowired private TestEntityManager entityManager;
    
    @Test
    public void testCreateNewOrderWithSingleProduct() {
        Customer customer = entityManager.find(Customer.class, 3);
        Product product = entityManager.find(Product.class, 2);
        
        Order mainOrder = new Order();
        mainOrder.setOrderTime(new Date());
        mainOrder.setCustomer(customer);
        mainOrder.copyAddressFromCustomer();
        
        mainOrder.setShippingCost(10);
        mainOrder.setProductCost(product.getCost());
        mainOrder.setTax(0);
        mainOrder.setSubtotal(product.getPrice());
        mainOrder.setTotal(product.getPrice() + 10); 
        
        mainOrder.setPaymentMethod(PaymentMethod.CREDIT_CARD);
        mainOrder.setStatus(OrderStatus.NEW);
        mainOrder.setDeliverDate(new Date());
        mainOrder.setDeliverDays(1);
        
        OrderDetail orderDetail1 = new OrderDetail(); 
        orderDetail1.setProduct(product);
        orderDetail1.setOrder(mainOrder);
        orderDetail1.setProductCost(product.getCost());
        orderDetail1.setShippingCost(10);
        orderDetail1.setQuantity(6);
        orderDetail1.setSubtotal(product.getPrice() * 6);
        orderDetail1.setUnitPrice(product.getPrice());
        
        Product product2 = entityManager.find(Product.class, 7);
        OrderDetail orderDetail2 = new OrderDetail(); 
        orderDetail2.setProduct(product2);
        orderDetail2.setOrder(mainOrder);
        orderDetail2.setProductCost(product.getCost());
        orderDetail2.setShippingCost(10);
        orderDetail2.setQuantity(2);
        orderDetail2.setSubtotal(product.getPrice() * 2);
        orderDetail2.setUnitPrice(product.getPrice());
        
        Set<OrderDetail> set = mainOrder.getOrderDetails();
        
        set.add(orderDetail1);
        set.add(orderDetail2);
        mainOrder.setOrderDetails(set);
        
        Order savedOrder = repo.save(mainOrder);
        
        assertThat(savedOrder.getId()).isGreaterThan(0);        
    }
    
    @Test
    public void updateOrder() {
        Order order = repo.findById(1).get();
        
        order.setPaymentMethod(PaymentMethod.COD);
        
        Order savedOrder = repo.save(order);
        
        assertThat(savedOrder.getPaymentMethod()).isEqualTo(PaymentMethod.COD);      
        
    }
    
    
}
