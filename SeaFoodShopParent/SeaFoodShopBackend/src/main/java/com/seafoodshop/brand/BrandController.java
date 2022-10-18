package com.seafoodshop.brand;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.seafoodshop.category.CategoryService;
import com.seafoodshop.common.entity.Brand;
import com.seafoodshop.common.entity.Category;

@Controller
@RequestMapping("brand")
public class BrandController {
    @Autowired
    BrandService brandService;
    @Autowired
    CategoryService categoryService;
    
    @GetMapping("/listBrand")
    private String listBrand(
            Model model,  
            RedirectAttributes re,
            @RequestParam("message")  Optional<String> message) {
        
        List<Brand> list = brandService.findAll();
        model.addAttribute("brands", list);       
        re.addAttribute("message", message.orElse(null));
        return "brand/listBrand";       
    }
    
    @GetMapping("/new")
    private String form_Brand(Brand brand,Model model) {
        model.addAttribute("brand", brand);   
        model.addAttribute("listCategory", categoryService.showListCategory());
        return "brand/form_brand";
    }
}
