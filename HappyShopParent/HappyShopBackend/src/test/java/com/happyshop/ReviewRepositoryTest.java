package com.happyshop;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.happyshop.common.entity.Customer;
import com.happyshop.common.entity.Review;
import com.happyshop.common.entity.product.Product;
import com.happyshop.review.ReviewRepository;

@DataJpaTest  
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class ReviewRepositoryTest {
    @Autowired
    private ReviewRepository reviewRepo;
    
    @Test
    public  void testCreateReview() {
        Product p = new Product(2);
        Customer c = new Customer(7);
        Review r = new Review("fghdgh....", "is gfdsfghood ok ", 1, new Date() , c, p);
        assertThat(r).isNotNull();
        
        reviewRepo.save(r);
    }
    
    
}
