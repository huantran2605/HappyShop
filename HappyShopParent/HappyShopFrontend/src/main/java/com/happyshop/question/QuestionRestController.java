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
import com.happyshop.common.entity.product.Product;
import com.happyshop.common.entity.question.Question;
import com.happyshop.common.entity.question.QuestionVisitor;
import com.happyshop.product.ProductService;
import com.happyshop.question.visitor.QuestionVisitorService;

@RestController
public class QuestionRestController {
    @Autowired
    CustomerUtility customerUtility;
    @Autowired
    ProductService productService;
    @Autowired
    QuestionService questionService;
    @Autowired
    QuestionVisitorService questionVisitorService;
    
    @PostMapping("/question/save")
    private String saveQuestion(
            @RequestParam("productId") Integer productId,
            @RequestParam("content") String content,
            @RequestParam(name = "fullName", required = false) String fullName,
            @RequestParam(name = "phoneNumber", required = false) String phoneNumber,
            @RequestParam(name = "email", required = false) String email,
            
            HttpServletRequest request) {
        
        Customer customer = customerUtility.getAuthenticationCustomer(request);        
        
        Product product = productService.findById(productId).get();
        
        Question q = new Question();
        q.setContent(content);
        q.setCustomer(customer);
        q.setProduct(product);
        q.setAskTime(new Date());
        q.setAnswerStatus(false);
        q.setLikes(0);
        q.setApprovalStatus(false);
        
        if(customer == null) {
            QuestionVisitor visitor = new QuestionVisitor(fullName, phoneNumber, email);     
            questionVisitorService.save(visitor);           
            q.setVisitor(visitor);
        }
        questionService.save(q); 
        
        return "ok";
    }
}
