package com.happyshop.address;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.happyshop.Utility;
import com.happyshop.common.entity.Address;
import com.happyshop.common.entity.Customer;
import com.happyshop.common.entity.setting.Country;
import com.happyshop.customer.CustomerService;
import com.happyshop.setting.country.CountryService;

@Controller
@RequestMapping("/address_book")
public class AddressController {
    
    @Autowired
    AddressService addressService;
    @Autowired
    CountryService countryService;
    @Autowired
    CustomerService customerService;
    
    
    @GetMapping("")
    public String showAllAddressBooks(HttpServletRequest request, Model model) {
        Customer customer = addressService.getAuthenticationCustomer(request);
        List<Address> listAddress = addressService.findByCustomer(customer);
        boolean usePrimaryAddressAsDefault = true;
        for (Address address : listAddress) {
            if(address.isDefaultForShipping()) {
                usePrimaryAddressAsDefault = false;
                break;
            }
        }
         
        model.addAttribute("usePrimaryAddressAsDefault", usePrimaryAddressAsDefault);
        model.addAttribute("listAddress", listAddress);
        model.addAttribute("customer", customer);
        
        return "address_book/address_books";
    }
    
    @GetMapping("/new")
    public String addressAddNewForm(Address address, Model model) {
        
        List<Country> listCountry = countryService.findAllByOrderByNameAsc();
        model.addAttribute("listCountry", listCountry);      
        model.addAttribute("titlePage", "Add new address");      
        model.addAttribute("address", address);      
        return "address_book/address_form";
    }
    
    @PostMapping("/saveOrUpdate")
    public String saveOrUpdate(Address address
            ,HttpServletRequest request,RedirectAttributes ra) {
        Customer customer = customerService.getAuthenticationCustomer(request);
        address.setCustomer(customer);
        if(address.getId() == null) {
            ra.addFlashAttribute("message","Added new address successfully!");            
        }
        else {
            ra.addFlashAttribute("message","Updated address successfully!");                    
        }
        
        addressService.save(address);
        
        String redirectOption =  request.getParameter("redirect");
        String selectedProduct =  request.getParameter("selectedProduct");
        if(redirectOption != null){
            if(redirectOption.equals("cart")) {
                return "redirect:/address_book?redirect=cart";           
            }
            else if(redirectOption.equals("checkout")) {
                return "redirect:/address_book?redirect=checkout&selectedProduct=" +selectedProduct;   
            }
        }
        return "redirect:/address_book";
    }
    
    @GetMapping("/update/{idAddress}")
    public String addressAddNewForm(@PathVariable("idAddress") Integer idAddress,
            HttpServletRequest request, Model model) {
        Customer customer = customerService.getAuthenticationCustomer(request);
        List<Country> listCountry = countryService.findAllByOrderByNameAsc();
        model.addAttribute("listCountry", listCountry);      
        
        Address address = addressService.findByIdAndCustomer(idAddress, customer.getId());
        model.addAttribute("titlePage", "Update address");   
        model.addAttribute("update", "Update address");   
        model.addAttribute("address", address);    
        return "address_book/address_form";
    }
    
    @GetMapping("/delete/{idAddress}")
    public String addressAddNewForm(@PathVariable("idAddress") Integer idAddress,
            HttpServletRequest request, Model model, RedirectAttributes ra) {
        Customer customer = customerService.getAuthenticationCustomer(request);
        addressService.deleteByIdAndCustomer(idAddress, customer.getId());
        
        ra.addFlashAttribute("message", "Deleted address successfully!");
        return "redirect:/address_book";
    }
    
    @GetMapping("/update_default_address/{idAddress}")
    public String updateDefaultAddress(@PathVariable("idAddress") Integer idAddress,
            HttpServletRequest request,RedirectAttributes ra) {
        Customer customer = addressService.getAuthenticationCustomer(request);
        addressService.setDefaultAddress(idAddress, customer.getId());     
        
        String redirectOption =  request.getParameter("redirect");
        String selectedProduct =  request.getParameter("selectedProduct");
        if(redirectOption != null){
            if(redirectOption.equals("cart")) {
                return "redirect:/cart";               
            }
            else if(redirectOption.equals("checkout")) {
                return "redirect:/checkout?selectedProduct=" +selectedProduct;   
            }
        }
            
        
        ra.addFlashAttribute("message", "Set As Default Address Successfully!");       
        return "redirect:/address_book"; 
    }
    
}
