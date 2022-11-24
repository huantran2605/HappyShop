package com.happyshop;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.happyshop.category.CategoryService;
import com.happyshop.common.entity.Category;

@Controller
public class MainController {
    @Autowired
    CategoryService categoryService;
    
	@GetMapping("")
	private String viewHomePage(Model model) {
	    List<Category> listCategory = categoryService.listNoChildrenCategories();
	    model.addAttribute("listCategory", listCategory);
		return "index";
	}
}
