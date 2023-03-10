package com.happyshop.review;

import java.util.List;

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

import com.happyshop.Utility;
import com.happyshop.common.entity.Customer;
import com.happyshop.common.entity.Review;
import com.happyshop.common.exception.ReviewNotFoundException;
import com.happyshop.customer.CustomerService;

@Controller
@RequestMapping("/review")
public class ReviewController {
    @Autowired 
    ReviewService reviewService;
    @Autowired
    CustomerService customerService;
    
    String defaultUrl = "redirect:/review/page/1?sortField=reviewTime&sortDir=des&keyWord=";
    @GetMapping("/listReview")
    public String viewFirstReviewPage() {
       
        return defaultUrl;
    }
    
    @GetMapping("/page/{pageNum}")
    private String reviewPage (@PathVariable ("pageNum") Integer pageNum,
            @Param("sortField") String sortField,
            @Param("sortDir") String sortDir,
            @Param("keyWord") String keyWord, 
            HttpServletRequest request,
            Model model) {
        
        Customer customer = getAuthenticationCustomer(request);
        
        //sort
        Sort sort = Sort.by(sortField);
        if(sortDir.equalsIgnoreCase("asc"))  
            sort = Sort.by(sortField).ascending();
        else  sort = Sort.by(sortField).descending();  
        
        Pageable pageable = PageRequest.of(pageNum - 1,  
                ReviewService.SIZE_PAGE_PRODUCT, sort);
        
        Page<Review> pageReview = reviewService.findAll(keyWord, customer, pageable); 
        List<Review> listReview = pageReview.getContent();
        
        long startCount = (pageNum - 1) * ReviewService.SIZE_PAGE_PRODUCT + 1;
        long endCount = startCount + ReviewService.SIZE_PAGE_PRODUCT - 1 ;
        if(endCount > pageReview.getTotalElements() )
            endCount = pageReview.getTotalElements();
        
        String reserveDir = sortDir.equalsIgnoreCase("asc") ? "des"  : "asc";
        model.addAttribute("reserveDir", reserveDir);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("totalPage", pageReview.getTotalPages()); 
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
    
        
        model.addAttribute("reviews", listReview);
        model.addAttribute("totalElement", pageReview.getTotalElements());
        
        model.addAttribute("elementsCurrentPerPage", pageReview.getNumberOfElements());
        model.addAttribute("elementsPerPage", ReviewService.SIZE_PAGE_PRODUCT);
        
        model.addAttribute("keyWord", keyWord);
        model.addAttribute("moduleURL", "/review");
        
        return"review/listReview";
    }
    
    public Customer getAuthenticationCustomer(HttpServletRequest request) {
        String email = Utility.getEmailAuthenticationCustomer(request);
        if(email == null) {
            return null;
        }
        Customer customer = customerService.findByEmail(email);
        return customer;
    }
    
    @GetMapping("detail/{id}")
    private String detailReview(@PathVariable("id") Integer  id,  
            RedirectAttributes re,
            Model model) {
        try {
            Review review = reviewService.findById(id);      
            model.addAttribute("review", review);
            return "review/review_detail_modal";
        } catch (ReviewNotFoundException e) {
            re.addFlashAttribute("message", e.getMessage());
            return defaultUrl;
        }
       
    }
}
