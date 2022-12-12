package com.happyshop.customer;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.happyshop.common.entity.Country;
import com.happyshop.common.entity.Customer;

import com.happyshop.setting.SettingService;
import com.happyshop.setting.country.CountryService;

@Controller
@RequestMapping("/customer")
public class CustomerController {
    @Autowired
    CustomerService customerService;   
    @Autowired
    CountryService countryService;
    @Autowired
    SettingService settingService;
    
    @GetMapping("/listCustomer")
    private String viewFirstPageProduct(Model model,
            RedirectAttributes re) {
        return customerPage(1, "id", "asc", "", model);
    }
    
    @GetMapping("/page/{pageNum}") 
    private String customerPage (@PathVariable ("pageNum") Integer pageNum,
            @Param("sortField") String sortField,
            @Param("sortDir") String sortDir,
            @Param("keyWord") String keyWord,          
            Model model) {
        //sort
        Sort sort = Sort.by(sortField);
        if(sortDir.equalsIgnoreCase("asc"))
            sort = Sort.by(sortField).ascending();  
        else  sort = Sort.by(sortField).descending();
        
        Pageable pageable = PageRequest.of(pageNum - 1,
                CustomerService.SIZE_PAGE_CUSTOMER, sort);
        
        Page<Customer> pageCustomer = customerService.findAll(pageable,keyWord); 
        List<Customer> listCustomer = pageCustomer.getContent();
        //list customer     
        long startCount = (pageNum - 1) * CustomerService.SIZE_PAGE_CUSTOMER + 1;
        long endCount = startCount + CustomerService.SIZE_PAGE_CUSTOMER - 1;
        if(endCount > pageCustomer.getTotalElements() )
            endCount = pageCustomer.getTotalElements();
        
        String reserveDir = sortDir.equalsIgnoreCase("asc") ? "des"  : "asc";  
        model.addAttribute("reserveDir", reserveDir);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("totalPage", pageCustomer.getTotalPages()); 
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        
        if(endCount >  pageCustomer.getTotalElements()) {
            endCount = pageCustomer.getTotalElements();
        }
        
        model.addAttribute("totalElement", pageCustomer.getTotalElements());
        
        model.addAttribute("customers", listCustomer);
        
        model.addAttribute("elementsCurrentPerPage", pageCustomer.getNumberOfElements());
        model.addAttribute("elementsPerPage", CustomerService.SIZE_PAGE_CUSTOMER);
        model.addAttribute("keyWord", keyWord);
        model.addAttribute("moduleURL", "/customer");
        return"customer/listCustomer";
    }
    
    @GetMapping("updateEnabled/{id}")
    private String updateEnableStatus(@PathVariable("id") Integer id,
            RedirectAttributes re){
        Optional<Customer> customer =  customerService.findById(id);
        String status = "";
        if (customer.isEmpty()) {
            re.addFlashAttribute("message", "The customer is not existed!");
            return "redirect:/customer/page/1?sortField=id&sortDir=asc&keyWord=";
        }
        else {
            status = customerService.updateEnabledStatus(customer.get());
            re.addFlashAttribute("message", status);     
        }
        String emailPathSearch = customer.get().getEmail().split("@")[0];
        return "redirect:/customer/page/1?sortField=id&sortDir=asc&keyWord=" + emailPathSearch;
    }      
    
    @GetMapping("detail/{id}")
    private String detailCustomer(@PathVariable("id") Integer  id,            
            Model model, RedirectAttributes re) {
        Optional<Customer> customer = customerService.findById(id);
        if (customer.isEmpty()) {
            re.addFlashAttribute("message", "The customer is not existed!");
            return "redirect:/customer/page/1?sortField=id&sortDir=asc&keyWord=";
        } else {
            model.addAttribute("customer", customer.get());          
        }
        return "customer/customer_detail_modal";
    }
       
    @GetMapping("/delete/{id}")
    private String deleteCustomer(@PathVariable("id") Integer id,
            RedirectAttributes re,Model model) throws IOException {
        Optional<Customer> customer =  customerService.findById(id);
        
        if (customer.isEmpty()) {
            re.addFlashAttribute("message", "The customer is not existed!");
            return "redirect:/customer/page/1?sortField=id&sortDir=asc&keyWord=";
        }
        else {    
            customerService.deleteById(id); 
            re.addFlashAttribute("message","Delete a customer id: "+ id + " successfully!");           
            return "redirect:/customer/page/1?sortField=id&sortDir=asc&keyWord=";
        }
    }
    
    @GetMapping("update/{id}")
    private String updateCustomer(@PathVariable("id") Integer  id,            
            Model model, RedirectAttributes re) {
        Optional<Customer> customer = customerService.findById(id);
        if (customer.isEmpty()) {
            re.addFlashAttribute("message", "The customer is not existed!");
            return "redirect:/customer/page/1?sortField=id&sortDir=asc&keyWord=";
        } else {  
            model.addAttribute("customer", customer.get());
            List<Country> listCountry = countryService.findAllByOrderByNameAsc();
            model.addAttribute("listCountry", listCountry);
        }
        return "customer/customer_form_update";
    }
    
    
    @PostMapping("/update")
    private String saveCustomer(Customer customer, 
            RedirectAttributes re,
            Model model) throws IOException {
        customerService.updateCustomer(customer);
        re.addFlashAttribute("message", "Updated Customer successfully!");
        String emailPathSearch = customer.getEmail().split("@")[0];
        return "redirect:/customer/page/1?sortField=id&sortDir=asc&keyWord=" + emailPathSearch;
    }
    
}
