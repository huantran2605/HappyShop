package com.seafoodshop.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CategoryRestController {
    @Autowired
    CategoryService cateService;

    @PostMapping("categories/check_name_alias")
    private int check_name_alias(Integer id, String name, String alias) {
       return cateService.IsNameOrAliasUnique(id, name, alias);
    }
}
