package com.happyshop;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.happyshop.common.entity.Customer;
import com.happyshop.common.entity.Review;
import com.happyshop.review.ReviewRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class ReviewRepositoryTests {
    @Autowired
    ReviewRepository repo;
    
    
    @Test
    public void getReviewsByCustomerTest() {
//        Customer customer = new Customer(1);
//        List<Review> r =repo.findAll(customer);
//        assertThat(r.size()).isGreaterThan(0);
//
//        for (Review review : r) {
//            System.out.println(review.getHeadline() +"  "+review.getComment()+"  "+review.getRating());
//        }
    }
}
