package com.happyshop.question;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.happyshop.CustomerUtility;
import com.happyshop.common.entity.Customer;
import com.happyshop.common.entity.product.Product;
import com.happyshop.common.entity.question.Question;
import com.happyshop.common.exception.ProductNotFoundException;
import com.happyshop.product.ProductService;
import com.happyshop.question.QuestionService;

@Controller
public class QuestionController {
    
    @Autowired
    ProductService productService;
    @Autowired
    QuestionService questionService;
    @Autowired
    CustomerUtility customerUtility;
    
    
    @GetMapping("all-questions/page/{pageNum}")
    private String viewAllQuestionsOfProduct(@Param("productId") Integer  productId,
            @Param("sortField") String sortField, 
            RedirectAttributes re, Model model,HttpServletRequest request,
            @PathVariable ("pageNum") Integer pageNum) {
        Product product = productService.findById(productId).get();
       
        Sort sort = Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(pageNum - 1,  
                QuestionService.SIZE_PAGE_QUESTION, sort);
        
        Page<Question> pageQuestion = questionService.findByProductAndApprovalStatus(product, pageable);
        List<Question> listQuestion = pageQuestion.getContent();
        model.addAttribute("listQuestion", listQuestion);
        
        long startCount = (pageNum - 1) * QuestionService.SIZE_PAGE_QUESTION + 1;
        long endCount = startCount + QuestionService.SIZE_PAGE_QUESTION - 1 ;
        if(endCount > pageQuestion.getTotalElements() )
            endCount = pageQuestion.getTotalElements();
        
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("totalPage", pageQuestion.getTotalPages()); 
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("totalElement", pageQuestion.getTotalElements());       
        model.addAttribute("elementsCurrentPerPage", pageQuestion.getNumberOfElements());
        model.addAttribute("elementsPerPage", QuestionService.SIZE_PAGE_QUESTION);
        model.addAttribute("moduleURL", "/all-questions");
        
        model.addAttribute("product", product);
        model.addAttribute("productId", productId);
        model.addAttribute("sortField", sortField);
        
        Customer customer =  customerUtility.getAuthenticationCustomer(request);
        
        if(customer == null) {
            model.addAttribute("customerAuthentication", false);           
        }
        else {
            model.addAttribute("customerAuthentication", true);
            model.addAttribute("customer", customer);             
        }
                
               
        return "question/all_questions_of_product";
    }
}
