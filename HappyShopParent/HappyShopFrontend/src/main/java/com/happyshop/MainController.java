package com.happyshop;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
	
	@GetMapping("/login")
    private String viewLoginPage() throws IOException {
    
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth == null || auth instanceof AnonymousAuthenticationToken) {
            return "login";
        }
        return "redirect:/";
    }

}
