package com.happyshop.question;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.happyshop.CustomerUtility;
import com.happyshop.common.entity.Customer;
import com.happyshop.common.entity.Question;
import com.happyshop.common.entity.Question_Asker;
import com.happyshop.common.entity.product.Product;
import com.happyshop.product.ProductService;

@RestController
public class QuestionRestController {
    @Autowired
    CustomerUtility customerUtility;
    @Autowired
    ProductService productService;
    @Autowired
    QuestionService questionService;
    @Autowired
    Question_AskerService question_AskerService;
    
    @PostMapping("/question/save")
    private String saveQuestion(
            @RequestParam("productId") Integer productId,
            @RequestParam("question_content") String question_content,
            @RequestParam(name = "fullName", required = false) String fullName,
            @RequestParam(name = "phoneNumber", required = false) String phoneNumber,
            @RequestParam(name = "email", required = false) String email,
            
            HttpServletRequest request) {
        
        Customer customer = customerUtility.getAuthenticationCustomer(request);        
        
        Product product = productService.findById(productId).get();
        
        Question q = new Question();
        q.setQuestion_content(question_content);
        q.setCustomer(customer);
        q.setProduct(product);
        q.setAskTime(new Date());
        q.setAnswerStatus(false);
        q.setLikes(0);
        q.setApprovalStatus(false);
        
        if(customer == null) {
            Question_Asker asker = new Question_Asker(fullName, phoneNumber, email);     
            question_AskerService.save(asker);           
            q.setAsker(asker);
        }
        questionService.save(q); 
        
        return "ok";
    }
}
