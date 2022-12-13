package com.happyshop.shippingRate;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.apache.commons.io.FileUtils;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.happyshop.common.entity.Brand;
import com.happyshop.common.entity.Country;
import com.happyshop.common.entity.ShippingRate;
import com.happyshop.common.entity.User;
import com.happyshop.setting.country.CountryService;

@Controller
@RequestMapping("/shippingRate")
public class ShippingRateController {
    @Autowired
    ShippingRateService shippingRateService;  
    @Autowired
    CountryService countryService;
    
    @GetMapping("/listShippingRate")
    private String viewFirstPageProduct(Model model,
            RedirectAttributes re) {
        return "redirect:/shippingRate/page/1?sortField=id&sortDir=asc&keyWord=";
    }
    
    @GetMapping("/page/{pageNum}") 
    private String shippingRatePage (@PathVariable ("pageNum") Integer pageNum,
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
                ShippingRateService.SIZE_PAGE_SHIPPING_RATE, sort);
        
        Page<ShippingRate> pageShippingRate = shippingRateService.findAll(pageable,keyWord); 
        List<ShippingRate> listShippingRate = pageShippingRate.getContent();
  
        long startCount = (pageNum - 1) * ShippingRateService.SIZE_PAGE_SHIPPING_RATE + 1;
        long endCount = startCount + ShippingRateService.SIZE_PAGE_SHIPPING_RATE - 1;
        if(endCount > pageShippingRate.getTotalElements() )
            endCount = pageShippingRate.getTotalElements();
        
        String reserveDir = sortDir.equalsIgnoreCase("asc") ? "des"  : "asc";
        model.addAttribute("reserveDir", reserveDir);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("totalPage", pageShippingRate.getTotalPages()); 
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        
        if(endCount >  pageShippingRate.getTotalElements()) {
            endCount = pageShippingRate.getTotalElements();
        }
        
        model.addAttribute("totalElement", pageShippingRate.getTotalElements());
        
        model.addAttribute("shippingRates", listShippingRate);
        
        model.addAttribute("elementsCurrentPerPage", pageShippingRate.getNumberOfElements());
        model.addAttribute("elementsPerPage", ShippingRateService.SIZE_PAGE_SHIPPING_RATE);
        model.addAttribute("keyWord", keyWord);
        model.addAttribute("moduleURL", "/shippingRate");
        return"shippingRate/listShippingRate";
    }
    
    @GetMapping("/updateCodSupported/{id}")
    public String updateCodeSupported(@PathVariable("id") Integer id, Model model,
            RedirectAttributes ra) {
        Optional<ShippingRate> sr =  shippingRateService.findById(id);
        if(sr.isEmpty()) {
            ra.addFlashAttribute("message", "Could not find the shipping rate.");
            return "redirect:/shippingRate/listShippingRate";
        }
        else {
            if(sr.get().isCodSupported()) {
                shippingRateService.updateCODSupport(id, false);
            }
            else {
                shippingRateService.updateCODSupport(id, true);
            }
        }
        ra.addFlashAttribute("message", "Update COD Supported Successfully!");
        String nameSearch = sr.get().getCountry().getName()+" "+ sr.get().getState();
        return "redirect:/shippingRate/page/1?sortField=id&sortDir=asc&keyWord=" + nameSearch;
    }
    
    @GetMapping("/new")
    private String form_ShippingRate(ShippingRate shippingRate,Model model) {
        model.addAttribute("shippingRate", shippingRate);   
        model.addAttribute("titlePage", "Create new shippingRate");
        List<Country> listCountry = countryService.findAllByOrderByNameAsc();
        model.addAttribute("listCountry", listCountry);   
        return "shippingRate/shippingRate_form";
    }
    
    @PostMapping("/saveOrUpdate")
    private String saveUser(ShippingRate shippingRate,  
            RedirectAttributes re) {
        String status = shippingRateService.isRateUnique(shippingRate);
        if(status.equals("ok")) {
            re.addFlashAttribute("message", "Create Shipping Rate successfully!");
            shippingRateService.save(shippingRate);
        }
        else {
            re.addFlashAttribute("message", "There's already rate for this destination: "+ shippingRate.getCountry().getName() 
                  + "-"+shippingRate.getState() );
        }
        return "redirect:/shippingRate/page/1?sortField=id&sortDir=asc&keyWord=";
               
    } 
    
    @GetMapping("update/{id}")
    private String updateShippingRate(@PathVariable("id") Integer  id,            
            Model model) {
        Optional<ShippingRate> shippingRate = shippingRateService.findById(id);
        if (shippingRate.isEmpty()) {
            model.addAttribute("message", "The shipping rate is not existed!");
            return "redirect:/shippingRate/listShippingRate";
        } else {  
            List<Country> listCountry = countryService.findAllByOrderByNameAsc();
            model.addAttribute("listCountry", listCountry);   
            model.addAttribute("shippingRate", shippingRate.get());
            model.addAttribute("update", "Update Shipping Rate");
            model.addAttribute("titlePage", "Update shipping Rate");
        }
        return "shippingRate/shippingRate_form";
    }
    
    @GetMapping("/delete/{id}")
    private String deleteShippingRate(@PathVariable("id") Integer id,
            RedirectAttributes re,Model model) throws IOException {
        Optional<ShippingRate> shippingRate =  shippingRateService.findById(id);
        if (shippingRate.isEmpty()) {
            re.addFlashAttribute("message", "The shippingRate is not existed!");
            return "redirect:/shippingRate/listShippingRate";
        }
        else {
            shippingRateService.deleteById(id);          
            re.addFlashAttribute("message","Delete shippingRate id: "+ id + " successfully!");           
        }
        return "redirect:/shippingRate/listShippingRate";
    
    }
    
}
