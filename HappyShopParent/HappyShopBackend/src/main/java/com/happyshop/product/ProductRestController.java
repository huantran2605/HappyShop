package com.happyshop.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductRestController {
    
    @Autowired
    ProductService productService;
    
    @PostMapping("/products/check_name")
    private String checkNameUnique (Integer id, String name) {
        String status =  productService.isNameUnique(id, name);
        return status;
    }
}
