package com.happyshop;

import static org.assertj.core.api.Assertions.assertThat;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.happyshop.common.entity.Customer;
import com.happyshop.common.entity.order.Order;
import com.happyshop.common.entity.order.OrderDetail;
import com.happyshop.common.entity.order.OrderStatus;
import com.happyshop.common.entity.order.OrderTrack;
import com.happyshop.common.entity.order.PaymentMethod;
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
        
        order.setPaymentMethod(PaymentMethod.CREDIT_CARD);
        
        Order savedOrder = repo.save(order);
        
        assertThat(savedOrder.getPaymentMethod()).isEqualTo(PaymentMethod.COD);      
        
    }
    @Test
    public void deleteOrder() {
        
        repo.deleteById(8);
        
    }
    
    @Test
    public void updateOrderTracks() {
        Order order = repo.findById(8).get();
        OrderTrack orderTrack1 = new OrderTrack();
        orderTrack1.setUpdatedTime(new Date());
        orderTrack1.setNote(OrderStatus.PACKAGED.defaultDescription());
        orderTrack1.setOrder(order);
        orderTrack1.setStatus(OrderStatus.PACKAGED);
        
        OrderTrack orderTrack2 = new OrderTrack();
        orderTrack2.setUpdatedTime(new Date());
        orderTrack2.setNote(OrderStatus.DELIVERED.defaultDescription());
        orderTrack2.setOrder(order);
        orderTrack2.setStatus(OrderStatus.DELIVERED);
        
        OrderTrack orderTrack3 = new OrderTrack();
        orderTrack3.setUpdatedTime(new Date());
        orderTrack3.setNote(OrderStatus.PICKED.defaultDescription());
        orderTrack3.setOrder(order);
        orderTrack3.setStatus(OrderStatus.PICKED);
        
        List<OrderTrack> list = order.getOrderTracks();
        list.add(orderTrack1);
        list.add(orderTrack2);
        list.add(orderTrack3);
        
        order.setOrderTracks(list);
       
        assertThat( repo.save(order).getOrderTracks()).hasSizeGreaterThan(0);
    }
    
    
    @Test
    public void findByOrderTimeBetween() throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date timeStart = dateFormat.parse("2020-10-1");
        Date timeEnd = dateFormat.parse("2020-10-31");
        
        List<Order> listOrder = repo.findByOrderTimeBetween(timeStart, timeEnd);
        assertThat(listOrder.size()).isGreaterThan(0);
        
        for (Order order : listOrder) {
            System.out.printf("%s | %s | %.2f \n", order.getId(), order.getProductCost(), order.getTotal() );
            
        }
    }
    
}
