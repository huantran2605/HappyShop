package com.happyshop.product;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.happyshop.brand.BrandService;
import com.happyshop.common.entity.Category;
import com.happyshop.common.entity.product.Product;

@Controller
@RequestMapping("/product")
public class ProductController {
    @Autowired
    ProductService productService;
    @Autowired
    BrandService brandService;
    
    @GetMapping("/listProduct")
    private String viewFirstPageProduct(Model model,
            RedirectAttributes re,
            @RequestParam("message")  Optional<String> message) {
        List<Product> list = productService.findAll();
        model.addAttribute("products", list);
        
        re.addAttribute("message", message.orElse(null));
        return "redirect:/product/page/1?sortField=id&sortDir=asc&keyWord=";
    }
        
    @GetMapping("/page/{pageNum}") 
    private String productPage (@PathVariable ("pageNum") Integer pageNum,
            @Param("sortField") String sortField,
            @Param("sortDir") String sortDir,
            @Param("keyWord") Optional<String> keyWord, 
            
            Model model,@RequestParam("message")  Optional<String> message) {
        //sort
        Sort sort = Sort.by(sortField);
        if(sortDir.equalsIgnoreCase("asc"))
            sort = Sort.by(sortField).ascending();
        else  sort = Sort.by(sortField).descending();
        
        org.springframework.data.domain.Pageable pageable = PageRequest.of(pageNum - 1,
                ProductService.SIZE_PAGE_PRODUCT, sort);
        
        Page<Product> pageProduct = productService.findAll(pageable,keyWord.get()); 
        List<Product> listProduct = pageProduct.getContent();
        //list cat 
        
        long startCount = (pageNum - 1) * ProductService.SIZE_PAGE_PRODUCT + 1;
        long endCount = startCount + ProductService.SIZE_PAGE_PRODUCT - 1 ;
        if(endCount > pageProduct.getTotalElements() )
            endCount = pageProduct.getTotalElements();
        
        String reserveDir = sortDir.equalsIgnoreCase("asc") ? "des"  : "asc";
        model.addAttribute("reserveDir", reserveDir);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("totalPage", pageProduct.getTotalPages()); 
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        
        if(endCount >  pageProduct.getTotalElements()) {
            endCount = pageProduct.getTotalElements();
        }
        
        model.addAttribute("totalElement", pageProduct.getTotalElements());
        
        model.addAttribute("products", listProduct);
        
        model.addAttribute("elementsCurrentPerPage", pageProduct.getNumberOfElements());
        model.addAttribute("elementsPerPage", productService.SIZE_PAGE_PRODUCT);
        model.addAttribute("message", message.orElse(null));
        
        model.addAttribute("keyWord", keyWord.get());
        
        return"product/listProduct";
    }
    
    
    @GetMapping("/new")
    private String form_Product(Product product,Model model) {
        product.setEnable(true);
        product.setInStock(true);
        model.addAttribute("product", product); 
        model.addAttribute("brands", brandService.findAll(Sort.by("name")));
        return "product/form_product";
    }
}
