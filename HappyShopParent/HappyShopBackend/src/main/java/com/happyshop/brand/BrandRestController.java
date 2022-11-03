package com.happyshop.brand;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.happyshop.common.entity.Brand;
import com.happyshop.common.entity.Category;

@RestController
public class BrandRestController {
    @Autowired
    BrandService brandService;
    
    @PostMapping("/brands/check_name")
    private String check_name(Integer id, String name) {
        String status =  brandService.isNameUnique(id, name);
        return status;
    }
    
    @GetMapping("/brands/{id}/categories")
    private List<CategoryDTO> getCategories(@PathVariable ("id") Integer id) throws BrandNotFoundException {
        List<CategoryDTO> listCategories = new ArrayList<>();
        
        Brand brand = brandService.getById(id);
        for (Category category : brand.getCategories()) {
            CategoryDTO categoryDTO = new CategoryDTO(category.getId(), category.getName());
            listCategories.add(categoryDTO);
        }
        
        return listCategories;
    }
}
