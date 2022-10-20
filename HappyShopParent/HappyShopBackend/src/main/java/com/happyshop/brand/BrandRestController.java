package com.happyshop.brand;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BrandRestController {
    @Autowired
    BrandService brandService;
    
    @PostMapping("brands/check_name")
    private String check_name(Integer id, String name) {
        String status =  brandService.isNameUnique(id, name);
        return status;
    }
    
}
