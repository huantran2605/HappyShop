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

import com.happyshop.FileUploadUtil;
import com.happyshop.admin.AmazonS3Util;
import com.happyshop.brand.BrandService;
import com.happyshop.category.CategoryCsvExporter;
import com.happyshop.category.CategoryService;
import com.happyshop.common.entity.Category;
import com.happyshop.common.entity.product.Product;
import com.happyshop.common.entity.product.ProductImage;
import com.happyshop.security.UserDetailsClass;

@Controller
@RequestMapping("product")  
public class ProductController {
    @Autowired
    ProductService productService;
    @Autowired
    BrandService brandService; 
    @Autowired
    CategoryService categoryService;
    
    @GetMapping("/listProduct")
    private String viewFirstPageProduct(Model model,
            RedirectAttributes re) {
        return productPage(1, "id", "asc", "", 0, model);
    }
        
    @GetMapping("/page/{pageNum}") 
    private String productPage (@PathVariable ("pageNum") Integer pageNum,
            @Param("sortField") String sortField,
            @Param("sortDir") String sortDir,
            @Param("keyWord") String keyWord, 
            @Param("categoryID") Integer categoryID, 
            
            Model model) {
        //sort
        Sort sort = Sort.by(sortField);
        if(sortDir.equalsIgnoreCase("asc"))  
            sort = Sort.by(sortField).ascending();
        else  sort = Sort.by(sortField).descending();  
        
        Pageable pageable = PageRequest.of(pageNum - 1,  
                ProductService.SIZE_PAGE_PRODUCT, sort);
        
        Page<Product> pageProduct = productService.findAll(pageable,keyWord,categoryID); 
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
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("totalPage", pageProduct.getTotalPages()); 
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        if(categoryID != null) {
            model.addAttribute("categoryID", categoryID);  
        }
        model.addAttribute("listCategories", categoryService.showListCategory());
        
        
        model.addAttribute("products", listProduct);
        model.addAttribute("totalElement", pageProduct.getTotalElements());
        
        model.addAttribute("elementsCurrentPerPage", pageProduct.getNumberOfElements());
        model.addAttribute("elementsPerPage", ProductService.SIZE_PAGE_PRODUCT);
        
        model.addAttribute("keyWord", keyWord);
        model.addAttribute("moduleURL", "/product");
        
        return"product/listProduct";
    }
    
    
    @GetMapping("/new")
    private String form_Product(Product product,Model model) {
        product.setEnable(true);
        product.setInStock(true);
        model.addAttribute("product", product); 
        model.addAttribute("brands", brandService.findAll(Sort.by("name")));
        model.addAttribute("titlePage", "Create new product");
        return "product/form_product";
    }
    
    @PostMapping("/saveOrUpdate")
    private String saveProduct(Product product,
            @RequestParam(name = "detailIDs", required = false) String[] detailIDs,
            @RequestParam(value = "imageFile", required = false) MultipartFile mainImageMultipartFile,
            @RequestParam(value = "extraImage", required = false) MultipartFile[] extraImageMultipartFile,
            @RequestParam(name = "exsitingExtraImage", required = false) MultipartFile[] exsitingExtraImageMultipartFile,
            @RequestParam(name = "productNameDetails", required = false) String[] nameDetails,
            @RequestParam(name = "productValueDetails", required = false) String[] valueDetails,
            @RequestParam(name = "imageIDs", required = false) String[] imageIDs,
            @RequestParam(name = "imageNames", required = false) String[] imageNames,
            RedirectAttributes re,
            @AuthenticationPrincipal UserDetailsClass loggedUser,
            Model model) throws IOException {
            
            if(!loggedUser.hasRole("Admin") && !loggedUser.hasRole("Editor")) {
                if(loggedUser.hasRole("Salesperson")) {
                    productService.saveProductPriceAndQuantity(product);
                    re.addFlashAttribute("message", "Updated Product successfully!");
                    return "redirect:/product/listProduct";
                }
            }
            
            if(product.getId() != null) {    
                re.addFlashAttribute("message", "Updated Product successfully!");
            } else {
                re.addFlashAttribute("message", "Added new Product successfully!");
            }
            ProductSaveHelper.setDetailsProduct(detailIDs, nameDetails, valueDetails, product);
            
            setMainImageName(mainImageMultipartFile, product);    
            if(imageIDs != null && imageNames != null) {
                ProductSaveHelper.setExistingExtraImageName(imageIDs, imageNames, product);           
            }
            ProductSaveHelper.setNewExtraImageName(extraImageMultipartFile, product);
            
            Product savedProduct = productService.save(product);
            
            if(exsitingExtraImageMultipartFile != null) {
                ProductSaveHelper.saveUploadedNewExsitingExtraImages(exsitingExtraImageMultipartFile, savedProduct);            
            }
            ProductSaveHelper.saveUploadedImages(mainImageMultipartFile,extraImageMultipartFile,
                     savedProduct);
            
            ProductSaveHelper.deleteExtraImagesWeredRemovedOnForm(product);
            
            productService.save(product);
      
            String nameSerach = product.getName();
            return "redirect:/product/page/1?sortField=id&sortDir=asc&categoryID=0&keyWord=" + nameSerach;
    }
    
    private void setMainImageName(MultipartFile mainImageMultipartFile, Product product) {
        if (!mainImageMultipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(mainImageMultipartFile.getOriginalFilename());
            product.setMainImage(fileName);
        }
        else {
            if (product.getId() != null) {               
                Optional<Product> oldProduct = productService.findById(product.getId());
                product.setMainImage(oldProduct.get().getMainImage());
            } else {
                product.setMainImage(null);
            }
        }
        
    } 
    
    @GetMapping("updateEnabled/{id}")
    private String updateEnableStatus(@PathVariable("id") Integer id,
            RedirectAttributes re){
        Optional<Product> product =  productService.findById(id);
        String status = "";
        if (product.isEmpty()) {
            re.addFlashAttribute("message", "The product is not existed!");
            return "redirect:/user/listUser";
        }
        else {
            status = productService.updateEnabledStatus(product.get());
            re.addFlashAttribute("message", status);     
        }
        String nameSerach = product.get().getName();
        return "redirect:/product/page/1?categoryID=0&sortField=id&sortDir=asc&keyWord=" + nameSerach;
    }    
    
    @GetMapping("delete/{id}")
    private String deleteProduct(@PathVariable("id") Integer id,
            RedirectAttributes re,Model model) throws IOException {
        Optional<Product> product =  productService.findById(id);
        
        if (product.isEmpty()) {
            re.addFlashAttribute("message", "The product is not exist!");
            return "redirect:/product/listProduct";
        }
        else {    
            productService.deleteById(id); 
          //delete folder contains images
            String dir = "product-images/" + id;
            AmazonS3Util.removeFolder(dir);
            
            re.addFlashAttribute("message","Delete product id: "+ id + " successfully!");           
            return "redirect:/product/listProduct";
        }
    
    }
    
    @GetMapping("update/{id}")
    private String updateProduct(@PathVariable("id") Integer  id,            
            Model model,@AuthenticationPrincipal UserDetailsClass loggedUser) {
        Optional<Product> product = productService.findById(id);
        if (product.isEmpty()) {
            model.addAttribute("message", "The product is not exist!");
            return "redirect:/product/listProduct";
        } else { 
            Boolean readOnlyForSalesperson = false;
            if(!loggedUser.hasRole("Admin") && !loggedUser.hasRole("Editor")) {
                if(loggedUser.hasRole("Salesperson")) {
                    readOnlyForSalesperson = true;
                }
            }
            model.addAttribute("readOnlyForSalesperson", readOnlyForSalesperson);
            model.addAttribute("product", product.get());
            model.addAttribute("brands", brandService.findAll(Sort.by("name")));       
            model.addAttribute("listCategories", product.get().getBrand().getCategories());
            
            int totalExtraImagesProduct =  product.get().getImages().size();
            model.addAttribute("totalExtraImagesProduct", totalExtraImagesProduct);
            
            model.addAttribute("id", id);
            model.addAttribute("update", "Update Product");
            model.addAttribute("titlePage", "Update product");
        }
        return "product/form_product";

    }
    
    @GetMapping("detail/{id}")
    private String detailProduct(@PathVariable("id") Integer  id,            
            Model model) {
        Optional<Product> product = productService.findById(id);
        if (product.isEmpty()) {
            model.addAttribute("message", "The product is not exist!");
            return "redirect:/product/listProduct";
        } else {
            model.addAttribute("product", product.get());
            
        }
        return "product/product_detail_modal";
    }
    
    @GetMapping("/export/csv")
    public void exportCsv(HttpServletResponse response) throws IOException {
        List<Product> listProduct = productService.findAll();
        ProductCsvExporter productCsvExporter = new ProductCsvExporter();
        productCsvExporter.export(listProduct, response);

    }
}
