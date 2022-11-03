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

import org.apache.commons.io.FileUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
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
import com.happyshop.brand.BrandService;
import com.happyshop.common.entity.Category;
import com.happyshop.common.entity.product.Product;
import com.happyshop.common.entity.product.ProductImage;

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
        model.addAttribute("elementsPerPage", ProductService.SIZE_PAGE_PRODUCT);
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
        model.addAttribute("titlePage", "Create new product");
        return "product/form_product";
    }
    
    @PostMapping("/saveOrUpdate")
    private String saveProduct(Product product,
            @RequestParam("detailIDs") String[] detailIDs,
            @RequestParam("imageFile") MultipartFile mainImageMultipartFile,
            @RequestParam("extraImage") MultipartFile[] extraImageMultipartFile,
            @RequestParam(name = "exsitingExtraImage", required = false) MultipartFile[] exsitingExtraImageMultipartFile,
            @RequestParam("productNameDetails") String[] nameDetails,
            @RequestParam("productValueDetails") String[] valueDetails,
            @RequestParam(name = "imageIDs", required = false) String[] imageIDs,
            @RequestParam(name = "imageNames", required = false) String[] imageNames,
            RedirectAttributes re,
            Model model) throws IOException {
        
            if(product.getId() != null) {    
                re.addAttribute("message", "Updated Product successfully!");
            } else {
                re.addAttribute("message", "Added new Product successfully!");
            }
            setDetailsProduct(detailIDs, nameDetails, valueDetails, product);
            
            setMainImageName(mainImageMultipartFile, product);    
            if(imageIDs != null && imageNames != null) {
                setExistingExtraImageName(imageIDs, imageNames, product);           
            }
            setNewExtraImageName(extraImageMultipartFile, product);
            
            Product savedProduct = productService.save(product);
            createImageFileDir(savedProduct);
            
            if(exsitingExtraImageMultipartFile != null) {
                saveUploadedNewExsitingExtraImages(exsitingExtraImageMultipartFile, savedProduct);            
            }
            saveUploadedImages(mainImageMultipartFile,extraImageMultipartFile,
                     savedProduct);
            
            deleteExtraImagesWeredRemovedOnForm(product);
            
            productService.save(product);
      
            return "redirect:/product/listProduct";
    }
    
    private void saveUploadedNewExsitingExtraImages(MultipartFile[] exsitingExtraImageMultipartFile,
            Product savedProduct) throws IOException {
        if (exsitingExtraImageMultipartFile.length > 0) {
            String fileDir = "product-images/" + savedProduct.getId() + "/extras";
            for (MultipartFile multipartFile : exsitingExtraImageMultipartFile) {
                if (!multipartFile.isEmpty()) {
                    String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

                    FileUploadUtil.saveFile(multipartFile, fileName, fileDir);
                }
            }
        }

    }

    private void createImageFileDir(Product savedProduct) throws IOException {
        String mainImageFileDirr = "product-images/" + savedProduct.getId();
        Path mainImageUploadPath = Paths.get(mainImageFileDirr);
        if (!Files.exists(mainImageUploadPath)) {
            Files.createDirectory(mainImageUploadPath);
        }
        
        String extraImageFileDirr = "product-images/" + savedProduct.getId() + "/extras";
        Path ExtraImageUploadPath = Paths.get(extraImageFileDirr);
        if (!Files.exists(ExtraImageUploadPath)) {
            Files.createDirectory(ExtraImageUploadPath);
        }
    }

    private void deleteExtraImagesWeredRemovedOnForm(Product product) throws IOException {
        String extraImageDir = "product-images/" + product.getId() + "/extras";
        Path dirPath = Paths.get(extraImageDir);
        
        Files.list(dirPath).forEach(file -> {
            String fileName = file.toFile().getName();
            if(!product.containsImageName(fileName)) {
                try {
                    Files.delete(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setExistingExtraImageName(String[] imageIDs,
            String[] imageNames, Product product) {
        
        Set<ProductImage> set = new HashSet<>();
        if(imageIDs.length > 0 && imageNames.length > 0) {
            for (int j = 0; j < imageNames.length; j++) {
                int idImage = Integer.parseInt(imageIDs[j]);      
                String nameImage = imageNames[j];
                set.add(new ProductImage(idImage, nameImage, product));
            }
        }
        product.setImages(set);     
    }

    private void setDetailsProduct(String[] detailIDs, String[] nameDetails, String[] valueDetails, Product product) {
        if(nameDetails.length > 0 && valueDetails.length > 0) {
            for (int i = 0; i < nameDetails.length; i++) {
                if(!nameDetails[i].isEmpty() && !valueDetails[i].isEmpty()) {
                    if(detailIDs.length > 0) {
                        int idDetail = Integer.parseInt(detailIDs[i]);
                        product.addDetail(idDetail, nameDetails[i] , valueDetails[i]);   
                    }
                    else {
                        product.addDetail(nameDetails[i], valueDetails[i]);            
                    }            
                }
            }
        }
    }

    private void setNewExtraImageName(MultipartFile[] extraImageMultipartFile, Product product) {
        if (extraImageMultipartFile.length > 0) {
            for (MultipartFile multipartFile : extraImageMultipartFile) {
                if (!multipartFile.isEmpty()) {
                    String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
                    if(!product.containsImageName(fileName)) {
                        product.addExtraImage(fileName);          
                    }
                }
            }
        }  
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
    
    private void saveUploadedImages (MultipartFile mainImageMultipartFile,
            MultipartFile[] extraImageMultipartFile,
            Product savedProduct) throws IOException {
 
        if (!mainImageMultipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(mainImageMultipartFile.getOriginalFilename());
            String fileDir = "product-images/" + savedProduct.getId();
            // delete old photos if have
            FileUploadUtil.cleanDir(fileDir);
            FileUploadUtil.saveFile(mainImageMultipartFile, fileName, fileDir);               
        }

        if(extraImageMultipartFile.length > 0) {
            String fileDir = "product-images/" + savedProduct.getId() + "/extras";
            for (MultipartFile multipartFile : extraImageMultipartFile) {
                if(!multipartFile.isEmpty()) {
                    String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
                    
                    FileUploadUtil.saveFile(multipartFile, fileName, fileDir);
                }
            }
        }
        
        
    }
    
    @GetMapping("updateEnabled/{id}")
    private String updateEnableStatus(@PathVariable("id") Integer id,
            RedirectAttributes re){
        Optional<Product> product =  productService.findById(id);
        String status = "";
        if (product.isEmpty()) {
            re.addAttribute("message", "The product is not existed!");
            return "redirect:/user/listUser";
        }
        else {
            status = productService.updateEnabledStatus(product.get());
            re.addAttribute("message", status);     
        }
        return "redirect:/product/listProduct";
    }    
    
    @GetMapping("/delete/{id}")
    private String deleteProduct(@PathVariable("id") Integer id,
            RedirectAttributes re,Model model) throws IOException {
        Optional<Product> product =  productService.findById(id);
        
        if (product.isEmpty()) {
            re.addAttribute("message", "The product is not exist!");
            return "redirect:/product/listProduct";
        }
        else {    
            productService.deleteById(id); 
          //delete folder contains images
            String dir = "product-images/" + id;
            FileUtils.deleteDirectory(new File(dir));
            
            re.addAttribute("message","Delete product id: "+ id + " successfully!");           
            return "redirect:/product/listProduct";
        }
    
    }
    
    @GetMapping("update/{id}")
    private String updateProduct(@PathVariable("id") Integer  id,            
            Model model) {
        Optional<Product> product = productService.findById(id);
        if (product.isEmpty()) {
            model.addAttribute("message", "The product is not exist!");
            return "redirect:/product/listProduct";
        } else {  
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
    
}
