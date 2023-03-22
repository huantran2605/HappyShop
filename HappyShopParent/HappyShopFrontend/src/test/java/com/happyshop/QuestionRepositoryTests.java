package com.happyshop;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.happyshop.common.entity.Customer;
import com.happyshop.common.entity.Question;
import com.happyshop.common.entity.Reply;
import com.happyshop.common.entity.Review;
import com.happyshop.common.entity.User;
import com.happyshop.common.entity.product.Product;
import com.happyshop.question.QuestionRepository;
import com.happyshop.question.reply.ReplyRepository;
import com.happyshop.review.ReviewRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class QuestionRepositoryTests {
    @Autowired
    QuestionRepository questionRepo;
    @Autowired
    ReplyRepository replyRepo;
    
    @Test
    public void addQuestion() {
        Question q = new Question();
        q.setCustomer(new Customer(40));
        q.setQuestion_content("what price?");
        q.setAskTime(new Date());
        q.setAnswerStatus(false);
        q.setProduct(new Product(1));   
        questionRepo.save(q);
        
        assertThat(q.getId()).isGreaterThan(0);
    }
    
    @Test
    public void addReply() {
//        Reply r = new Reply(); 
//        r.setUser(new User( (long) 1));
//        r.setQuestion(new Question(3));
//        r.setAdminReplyRequired(false);
//        
//        r.setReply_content("$1000,,,,");
//        r.setReplyTime(new Date());
//        
//        replyRepo.save(r);
//        assertThat(r.getId()).isGreaterThan(0);
    }
    
    @Test
    public void viewAllReplies() {
//        Question question = questionRepo.findByProductAndCustomer(28, 40);
//        
//        List<Reply> replies = question.getReplies();
//        for (Reply r : replies) {
//            System.out.println(r.getReply_content() + " " + r.getId() + " ");
//        }
//        assertThat(replies.size()).isGreaterThan(0);
    }
    
    
   
    
    
}
