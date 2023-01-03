package com.happyshop.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.happyshop.common.entity.product.Product;

@RestController
public class ProductRestController {
    
    @Autowired
    ProductService productService;
    
    @PostMapping("/products/check_name")
    private String checkNameUnique (Integer id, String name) {
        String status =  productService.isNameUnique(id, name);
        return status;
    }
    
    @GetMapping("/products/get/{id}")
    private ProductDTO getProductInfo(@PathVariable("id") Integer productId) {
        Product product = productService.findById(productId).get();
        ProductDTO pDTO = new ProductDTO(product.getName(),product.shortName(), product.getProductMainImagePath(),
                product.getPrice(), product.getCost(), product.getQuantity());
        return pDTO;
    }
    
}
