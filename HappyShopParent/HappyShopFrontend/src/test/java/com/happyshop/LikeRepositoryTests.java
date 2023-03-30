//package com.happyshop;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//import java.util.List;
//
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.test.annotation.Rollback;
//
//import com.happyshop.common.entity.Customer;
//import com.happyshop.common.entity.question.QuestionLike;
//import com.happyshop.common.entity.review.Review;
//import com.happyshop.like.LikeRepository;
//import com.happyshop.like.LikeService;
//import com.happyshop.review.ReviewRepository;
//
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = Replace.NONE)
//@Rollback(false)
//public class LikeRepositoryTests {
//    @Autowired
//    QuestionLikeRepository repo;
//    
//    @Autowired
//    private QuestionLikeService service;
//    
//    
//    @Test
//    public void addReviewLikeTest() {
//        QuestionLike rL = new QuestionLike(new Customer(1), new Review(1));
//        
//        repo.save(rL);
//    }
//    @Test
//    public void getReviewLikeByCustomerAndReviewTest() {
//        QuestionLike rL = repo.findByCustomerAndReview(new Customer(1), new Review(1));
//        
//        assertThat(rL).isNotNull();
//    }
//    
//    
//    
//    
//}
