package com.happyshop.shippingRate;

import java.util.List;

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

import com.happyshop.common.entity.ShippingRate;

@Controller
@RequestMapping("/shippingRate")
public class ShippingRateController {
    @Autowired
    ShippingRateService shippingRateService;
    
    
    @GetMapping("/listShippingRate")
    private String viewFirstPageProduct(Model model,
            RedirectAttributes re) {
        return shippingRatePage(1, "id", "asc", "", model);
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
    
    
}
