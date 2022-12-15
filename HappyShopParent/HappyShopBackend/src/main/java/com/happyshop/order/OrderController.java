package com.happyshop.order;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import com.happyshop.common.entity.Currency;
import com.happyshop.common.entity.Customer;
import com.happyshop.common.entity.Order;
import com.happyshop.common.entity.OrderDetail;
import com.happyshop.common.entity.Setting;
import com.happyshop.common.entity.SettingCategory;
import com.happyshop.common.entity.product.Product;
import com.happyshop.order.OrderService;
import com.happyshop.setting.CurrencyService;
import com.happyshop.setting.SettingService;

@Controller
@RequestMapping("/order")
public class OrderController {
    @Autowired
    OrderService orderService;
    @Autowired
    SettingService settingService;
    
    private String defaultRedirectURL = "redirect:/order/page/1?sortField=orderTime&sortDir=des&keyWord=";
    
    @GetMapping("/listOrder")
    private String viewFirstPageProduct(Model model,
            RedirectAttributes re, HttpServletRequest request) {
        return defaultRedirectURL;
    }
    
    @GetMapping("/page/{pageNum}") 
    private String orderPage (@PathVariable ("pageNum") Integer pageNum,
            @Param("sortField") String sortField,
            @Param("sortDir") String sortDir,
            @Param("keyWord") String keyWord,          
            Model model, HttpServletRequest request) {
        //sort
        Sort sort = null;
        if(sortField.equals("destination")) {
            sort = Sort.by("country").and(Sort.by("state").and(Sort.by("city")));
        }
        else {
            sort = Sort.by(sortField);           
        }
        if(sortDir.equalsIgnoreCase("asc"))
            sort = sort.ascending();  
        else  sort = sort.descending();
        
        Pageable pageable = PageRequest.of(pageNum - 1,
                OrderService.SIZE_PAGE_ORDER, sort);
        
        Page<Order> pageOrder = orderService.findAll(keyWord, pageable); 
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
        
        loadCurrencySetting(request);
        return"order/listOrder";
    }
    
    public void loadCurrencySetting( HttpServletRequest  request) {
        List<Setting> list = settingService.findByCategory(SettingCategory.CURRENCY);
        
        for (Setting setting : list) {
            request.setAttribute(setting.getKey(), setting.getValue());
        }     
    }
    
    @GetMapping("detail/{id}")
    private String detailOrder(@PathVariable("id") Integer  id,            
            Model model, RedirectAttributes re, HttpServletRequest request) {
        Optional<Order> order = orderService.findById(id);
        if (order.isEmpty()) {
            re.addFlashAttribute("message", "The order is not existed!");
            return defaultRedirectURL;
        } else {
            model.addAttribute("order", order.get()); 
            Set<OrderDetail> orderDetails = order.get().getOrderDetails();
            model.addAttribute("orderDetails", orderDetails); 
            loadCurrencySetting(request);
        }
        return "order/order_detail_modal";
    }
    
    @GetMapping("/delete/{id}")
    private String deleteOrder(@PathVariable("id") Integer id,
            RedirectAttributes re,Model model) throws IOException {
        Optional<Order> order =  orderService.findById(id);
        
        if (order.isEmpty()) {
            re.addFlashAttribute("message", "The order is not existed!");
            return defaultRedirectURL;
        }
        else {    
            orderService.deleteById(id); 
            re.addFlashAttribute("message","Delete an order id: "+ id + " successfully!");           
            return defaultRedirectURL;
        }
    }
     
}
