package com.happyshop;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.happyshop.common.entity.Customer;
import com.happyshop.common.entity.Review;
import com.happyshop.common.entity.Review_Like;
import com.happyshop.review.ReviewRepository;
import com.happyshop.review.review_like.ReviewLikeRepository;
import com.happyshop.review.review_like.ReviewLikeService;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class ReviewLikeRepositoryTests {
    @Autowired
    ReviewLikeRepository repo;
    
    @Autowired
    private ReviewLikeService service;
    
    
    @Test
    public void addReviewLikeTest() {
        Review_Like rL = new Review_Like(new Customer(1), new Review(1));
        
        repo.save(rL);
    }
    @Test
    public void getReviewLikeByCustomerAndReviewTest() {
        Review_Like rL = repo.findByCustomerAndReview(new Customer(1), new Review(1));
        
        assertThat(rL).isNotNull();
    }
    
    
    
    
}
