package com.happyshop.brand;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.happyshop.FileUploadUtil;
import com.happyshop.category.CategoryService;
import com.happyshop.common.entity.Brand;
import com.happyshop.common.entity.Category;

@Controller
@RequestMapping("brand")
public class BrandController {
    @Autowired
    BrandService brandService;
    @Autowired
    CategoryService categoryService;
    
    @GetMapping("/listBrand")
    private String viewFirstPageBrand(
            Model model,  
            RedirectAttributes re,
            @RequestParam("message")  Optional<String> message) {
        
        List<Brand> list = brandService.findAll();
        model.addAttribute("brands", list);       
        re.addAttribute("message", message.orElse(null));
        return "redirect:/brand/page/1?sortField=id&sortDir=asc&keyWord=";      
    }
    
    @GetMapping("/new")
    private String form_Brand(Brand brand,Model model) {
        model.addAttribute("brand", brand);   
        model.addAttribute("listCategory", categoryService.showListCategory());
        return "brand/form_brand";
    }
    
    @PostMapping("/saveOrUpdate")
    private String saveBrand(Brand brand, @RequestParam("logoFile") MultipartFile mutipartFile,
            @RequestParam("id") Optional<Integer> id,
            RedirectAttributes re,
            Model model) throws IOException {
   
        // upload photo
        if (!mutipartFile.isEmpty()) {
            String fileName = org.springframework.util.StringUtils.cleanPath(mutipartFile.getOriginalFilename());
            brand.setLogo(fileName);
            Brand savedCate = brandService.save(brand);
            String fileDir = "brand-logos/" + savedCate.getId();
            // delete old photos if have  
            FileUploadUtil.cleanDir(fileDir);  
            FileUploadUtil.saveFile(mutipartFile, fileName, fileDir);
        } else {
            if(!id.isEmpty()) {
                Optional<Brand> oldBrand = brandService.findById(id.get());
                brand.setLogo(oldBrand.get().getLogo()); 
                BeanUtils.copyProperties(brand, oldBrand.get());
                brandService.save(oldBrand.get());
                re.addAttribute("message", "Updated Brand successfully!");
            }
            else {
                brand.setLogo(null);
                brandService.save(brand);                  
                re.addAttribute("message", "Added new Brand successfully!");
            }
        }
        
        return "redirect:/brand/listBrand";
    }
    
    @GetMapping("update/{id}")
    private String updateBrand(@PathVariable("id") Integer  id,            
            Model model) {
        Optional<Brand> brand = brandService.findById(id);
        if (brand.isEmpty()) {
            model.addAttribute("message", "The brand is not exist!");
            return "redirect:/brand/listBrand";
        } else {  
            model.addAttribute("listCategory", categoryService.showListCategory());
            model.addAttribute("brand", brand.get());
            model.addAttribute("id", id);
            model.addAttribute("update", "Update Brand");
        }
        return "brand/form_brand";

    }
    
    @GetMapping("/page/{pageNum}") 
    private String brandPage (@PathVariable ("pageNum") Integer pageNum,
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
                BrandService.SIZE_PAGE_BRAND, sort);
        
        Page<Brand> pageBrand = brandService.findAll(pageable,keyWord.get()); 
        List<Brand> listBrand = pageBrand.getContent();
        //list brand 
        
        long startCount = (pageNum - 1) * BrandService.SIZE_PAGE_BRAND + 1;
        long endCount = startCount + BrandService.SIZE_PAGE_BRAND - 1 ;
        if(endCount > pageBrand.getTotalElements() )
            endCount = pageBrand.getTotalElements();
        
        String reserveDir = sortDir.equalsIgnoreCase("asc") ? "des"  : "asc";
        model.addAttribute("reserveDir", reserveDir);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("totalPage", pageBrand.getTotalPages()); 
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        
        if(endCount >  pageBrand.getTotalElements()) {
            endCount = pageBrand.getTotalElements();
        }
        
        model.addAttribute("totalElement", pageBrand.getTotalElements());
        
        model.addAttribute("brands", listBrand);
        
        model.addAttribute("brandsCurrentPerPage", pageBrand.getNumberOfElements());
        model.addAttribute("brandsPerPage", BrandService.SIZE_PAGE_BRAND);
        model.addAttribute("message", message.orElse(null));
        
        model.addAttribute("keyWord", keyWord.get());
        
        return"brand/listBrand";
    }
    
    @GetMapping("/delete/{id}")
    private String deleteBrand(@PathVariable("id") Integer id,
            RedirectAttributes re,Model model) throws IOException {
        Optional<Brand> brand =  brandService.findById(id);
        if (brand.isEmpty()) {
            re.addAttribute("message", "The brand is not exist!");
            return "redirect:/brand/listBrand";
        }
        else {
            brandService.deleteById(id); 
          //delete folder contains logos
            String dir = "brand-logos/" + id;
            FileUtils.deleteDirectory(new File(dir));
            
            re.addAttribute("message","Delete brand id: "+ id + " successfully!");           
            return "redirect:/brand/listBrand";
        }
    
    }
}
