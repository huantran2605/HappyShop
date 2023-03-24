package com.happyshop;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import com.happyshop.common.entity.Customer;
import com.happyshop.common.entity.Question;
import com.happyshop.common.entity.Review;
import com.happyshop.common.entity.product.Product;
import com.happyshop.question.QuestionRepository;
import com.happyshop.review.ReviewRepository;

@DataJpaTest  
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class QuestionRepositoryTest {
    @Autowired
    private QuestionRepository repo;
    
    @Test
    public  void testCreateReview() {
        Pageable p = PageRequest.of(0, 3);
        Page<Question> page = repo.findAllNotApprovedAndNotAnswered("is",p); 
        List<Question> list= page.getContent();
        assertThat(list.size()).isGreaterThan(0);
        
        for (Question question : list) {
            System.out.println(question.getQuestion_content());
        }
    }
    
    
    
}
