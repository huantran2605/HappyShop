package com.happyshop.product;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import com.happyshop.category.CategoryService;
import com.happyshop.common.entity.Category;
import com.happyshop.common.entity.product.Product;
import com.happyshop.common.entity.product.ProductImage;


@Controller
public class ProductController {
    @Autowired
    CategoryService categoryService;
    
    @Autowired
    ProductService productService;
    @GetMapping("/c/{category_alias}")
    public String viewFristPage (@PathVariable("category_alias") String alias,
            Model model) {
        return viewCategoryByPage(alias,1, model); 
    }
    
    @GetMapping("/c/{category_alias}/page/{pageNum}")
    public String viewCategoryByPage (@PathVariable("category_alias") String alias,
            @PathVariable("pageNum") Integer pageNum,
            Model model) {
        Category cat = categoryService.getCategory(alias);
        if(cat == null) {
            return "error/404";
        }
        Pageable pageable = PageRequest.of(pageNum - 1, ProductService.SIZE_PAGE_PRODUCT);
        Page<Product> page = productService.findByCategory(cat.getId(), pageable);
        List<Product> listProduct = page.getContent();
        
        List<Category> listParentCategory = categoryService.getListParentCategory(cat);
        
        long startCount = (pageNum - 1) * ProductService.SIZE_PAGE_PRODUCT + 1;
        long endCount = startCount + ProductService.SIZE_PAGE_PRODUCT - 1 ;
        if(endCount > page.getTotalElements() ) {
            endCount = page.getTotalElements();           
        }
        model.addAttribute("totalPage", page.getTotalPages());
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("totalElement", page.getTotalElements());
        model.addAttribute("elementsCurrentPerPage", page.getNumberOfElements());
        model.addAttribute("elementsPerPage", ProductService.SIZE_PAGE_PRODUCT);
        
        model.addAttribute("category", cat);
        model.addAttribute("category_alias", alias);
        model.addAttribute("titlePage", cat.getName());
        model.addAttribute("listParentCategory", listParentCategory);
        model.addAttribute("listProduct", listProduct);
        model.addAttribute("totalChildsInCategory", cat.getChildren().size());
        return "product/products_by_category";
    }
    
    @GetMapping("/p/{product_alias}")
    public String productDetail (@PathVariable("product_alias") String alias,
            Model model) {
        Product product = productService.findByAlias(alias);
        if(product == null) {
            return "error/404";
        }
        List<Category> listParentCategory = categoryService.getListParentCategory(product.getCategory());
        model.addAttribute("listParentCategory", listParentCategory);
        model.addAttribute("product", product);
        model.addAttribute("titlePage", product.shortName());
        
        return "product/product_detail";
    }
    
    @GetMapping("/search")
    public String viewFirstPageSearchProduct (@Param("keyword") String keyword,
            Model model) {
        return searchProduct(keyword, 1, model);
    }
    
    @GetMapping("/search/page/{pageNum}")
    public String searchProduct (@Param("keyword") String keyword,
            @PathVariable("pageNum") int pageNum,
            Model model) {
        model.addAttribute("keyword", keyword);
        Pageable pageable = PageRequest.of(pageNum - 1, ProductService.SIZE_RESULT_PAGE_PRODUCT);
        Page<Product> page = productService.searchProduct(keyword, pageable);
        List<Product> listProduct = page.getContent();
        
        long startCount = (pageNum - 1) * ProductService.SIZE_PAGE_PRODUCT + 1;
        long endCount = startCount + ProductService.SIZE_PAGE_PRODUCT - 1 ;
        if(endCount > page.getTotalElements() ) {
            endCount = page.getTotalElements();           
        }
        model.addAttribute("totalPage", page.getTotalPages());
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("totalElement", page.getTotalElements());
        model.addAttribute("elementsCurrentPerPage", page.getNumberOfElements());
        model.addAttribute("elementsPerPage", ProductService.SIZE_PAGE_PRODUCT);
        
        model.addAttribute("titlePage", "Search Result '" + keyword+"'");
        model.addAttribute("listProduct", listProduct);
        
        return "product/search_result";
    }
}
