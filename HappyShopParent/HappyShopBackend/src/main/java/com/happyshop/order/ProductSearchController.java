package com.happyshop.order;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.happyshop.common.entity.product.Product;
import com.happyshop.product.ProductService;

@Controller
public class ProductSearchController {
    @Autowired
    ProductService productService;

    @GetMapping("/order/search_product")
    public String showSearchProductPage() {
        return "redirect:/order/search_product/page/1?sortField=name&sortDir=asc&keyWord=";
    }

    @PostMapping("/order/search_product")
    public String searchProducts(@Param("keyWord") String keyWord) {
        return "redirect:/order/search_product/page/1?sortField=name&sortDir=asc&keyWord=" + keyWord;
    }

    @GetMapping("/order/search_product/page/{pageNum}")
    public String productSearch(@PathVariable("pageNum") Integer pageNum,
            Model model, @Param("keyWord") String keyWord) {
        Sort sort = Sort.by("name").ascending();
        Pageable pageable = PageRequest.of(pageNum - 1, ProductService.SIZE_PAGE_PRODUCT, sort);
        Page<Product> pageProduct = productService.findAll(pageable, keyWord, 0);
        List<Product> listProduct = pageProduct.getContent();
        model.addAttribute("products", listProduct);
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("totalPage", pageProduct.getTotalPages());
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("sortField", "name");
        model.addAttribute("sortDir", "asc");
        
        long startCount = (pageNum - 1) * ProductService.SIZE_PAGE_PRODUCT + 1;
        long endCount = startCount + ProductService.SIZE_PAGE_PRODUCT - 1;
        if (endCount > pageProduct.getTotalElements())
            endCount = pageProduct.getTotalElements();
        
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("totalElement", pageProduct.getTotalElements());
        model.addAttribute("elementsCurrentPerPage", pageProduct.getNumberOfElements());
        model.addAttribute("elementsPerPage", ProductService.SIZE_PAGE_PRODUCT);
        model.addAttribute("keyWord", keyWord);
        model.addAttribute("moduleURL", "/order/search_product");
        return "order/search_product";
    }

}
