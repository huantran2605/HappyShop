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
import com.happyshop.common.entity.Like;
import com.happyshop.like.LikeRepository;
import com.happyshop.like.LikeService;
import com.happyshop.review.ReviewRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class LikeRepositoryTests {
    @Autowired
    LikeRepository repo;
    
    @Autowired
    private LikeService service;
    
    
    @Test
    public void addReviewLikeTest() {
        Like rL = new Like(new Customer(1), new Review(1));
        
        repo.save(rL);
    }
    @Test
    public void getReviewLikeByCustomerAndReviewTest() {
        Like rL = repo.findByCustomerAndReview(new Customer(1), new Review(1));
        
        assertThat(rL).isNotNull();
    }
    
    
    
    
}
