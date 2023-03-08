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
import com.happyshop.order.OrderDetailRepository;
import com.happyshop.order.OrderRepository;

@DataJpaTest  
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class OrderDetailRepositoryTests {
    @Autowired private OrderDetailRepository repo;
    
    @Test
    public void testFindWithCategoryByTimeBetween() throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date timeStart = dateFormat.parse("2020-10-1");
        Date timeEnd = dateFormat.parse("2020-10-31");
        
        List<OrderDetail> listOrderDetail = repo.findWithCategoryAndTimeBetween(timeStart, timeEnd);
        assertThat(listOrderDetail.size()).isGreaterThan(0);
        
        for (OrderDetail d : listOrderDetail) {
            System.out.printf("%s | %d | %.2f | %.2f | %.2f \n",d.getProduct().getCategory().getName(), d.getProductCost(), d.getShippingCost(), d.getSubtotal());
            
        }
    }
    
    
    @Test
    public void testFindWithProductByTimeBetween() throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date timeStart = dateFormat.parse("2020-10-1");
        Date timeEnd = dateFormat.parse("2020-10-31");
        
        List<OrderDetail> listOrderDetail = repo.findWithProductAndTimeBetween(timeStart, timeEnd);
        assertThat(listOrderDetail.size()).isGreaterThan(0);
        
        for (OrderDetail d : listOrderDetail) {
            System.out.printf("%s | %d |  %.2f | %.2f | %.2f \n",d.getProduct().getName(), d.getQuantity(), d.getProductCost(), d.getShippingCost(), d.getSubtotal());
            
        }
    }
    
}
