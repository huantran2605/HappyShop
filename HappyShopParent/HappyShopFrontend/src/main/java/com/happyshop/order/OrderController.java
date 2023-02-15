package com.happyshop.order;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.happyshop.Utility;
import com.happyshop.common.entity.Customer;
import com.happyshop.common.entity.order.Order;
import com.happyshop.common.entity.order.OrderDetail;
import com.happyshop.customer.CustomerService;

@Controller
@RequestMapping("/order")
public class OrderController {
    @Autowired
    OrderService orderService;
    @Autowired
    CustomerService customerService;
    
    private String defaultRedirectURL = "redirect:/order/page/1?sortField=orderTime&sortDir=des&keyWord=";
    
    @GetMapping("")
    private String viewFirstPageOrder(Model model,
            RedirectAttributes re, HttpServletRequest request) {
        return defaultRedirectURL;
    }
    @GetMapping("/page/{pageNum}")
    private String orderPage (@PathVariable ("pageNum") Integer pageNum,
            @Param("sortField") String sortField,
            @Param("sortDir") String sortDir,
            @Param("keyWord") String keyWord,          
            Model model, HttpServletRequest request) {
        
        Customer customer = getAuthenticationCustomer(request);
        Integer customerId = customer.getId();
        //sort
        Sort sort = Sort.by(sortField);           
        
        if(sortDir.equalsIgnoreCase("asc"))
            sort = sort.ascending();  
        else  sort = sort.descending();
        
        Pageable pageable = PageRequest.of(pageNum - 1,
                OrderService.SIZE_PAGE_ORDER, sort);
        
        Page<Order> pageOrder = orderService.findAll(keyWord, customerId, pageable); 
        List<Order> listOrder = pageOrder.getContent();
        //list order     
        long startCount = (pageNum - 1) * OrderService.SIZE_PAGE_ORDER + 1;
        long endCount = startCount + OrderService.SIZE_PAGE_ORDER - 1;
        if(endCount > pageOrder.getTotalElements() )
            endCount = pageOrder.getTotalElements();
        
        String reserveDir = sortDir.equalsIgnoreCase("asc") ? "des"  : "asc";  
        model.addAttribute("reserveDir", reserveDir);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("totalPage", pageOrder.getTotalPages()); 
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        
        if(endCount >  pageOrder.getTotalElements()) {
            endCount = pageOrder.getTotalElements();
        }
        
        model.addAttribute("totalElement", pageOrder.getTotalElements());
        
        model.addAttribute("orders", listOrder);
        
        model.addAttribute("elementsCurrentPerPage", pageOrder.getNumberOfElements());
        model.addAttribute("elementsPerPage", OrderService.SIZE_PAGE_ORDER);
        model.addAttribute("keyWord", keyWord);
        model.addAttribute("moduleURL", "/order");        
      
        return"order/order_customer";
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
    private String detailOrder(@PathVariable("id") Integer orderId,
            Model model, RedirectAttributes re, HttpServletRequest request) {
        Customer customer = getAuthenticationCustomer(request);
        Order order = orderService.findByOrderIdAndCustomer(orderId, customer.getId());

        model.addAttribute("order", order);
        Set<OrderDetail> orderDetails = order.getOrderDetails();
        model.addAttribute("orderDetails", orderDetails);

        return "order/order_detail_modal";
    }
    
    
}
