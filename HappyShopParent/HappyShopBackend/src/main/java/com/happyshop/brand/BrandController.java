package com.happyshop.brand;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

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
import com.happyshop.admin.AmazonS3Util;
import com.happyshop.category.CategoryCsvExporter;
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
            Model model) {

        return brandPage(1, "id", "asc", "", model);      
    }
    
    @GetMapping("/new")
    private String form_Brand(Brand brand,Model model) {
        model.addAttribute("brand", brand);   
        model.addAttribute("listCategory", categoryService.showListCategory());
        model.addAttribute("titlePage", "Create new brand");
        return "brand/form_brand";
    }
    
    @PostMapping("/saveOrUpdate")
    private String saveBrand(Brand brand, @RequestParam("logoFile") MultipartFile multipartFile,
            @RequestParam("id") Optional<Integer> id,
            RedirectAttributes re,
            Model model) throws IOException {
   
        // upload photo
        if (!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            brand.setLogo(fileName);
            Brand savedBrand = brandService.save(brand);
            String uploadDir = "brand-logos/" + savedBrand.getId();
            // delete old photos if have  
            AmazonS3Util.removeFolder(uploadDir);
            AmazonS3Util.uploadFile(uploadDir, fileName, multipartFile.getInputStream());
        } else {
            if(!id.isEmpty()) {
                Optional<Brand> oldBrand = brandService.findById(id.get());
                brand.setLogo(oldBrand.get().getLogo()); 
                BeanUtils.copyProperties(brand, oldBrand.get());
                brandService.save(oldBrand.get());
                re.addFlashAttribute("message", "Updated Brand successfully!");
            }
            else {
                brand.setLogo(null);
                brandService.save(brand);                  
                re.addFlashAttribute("message", "Added new Brand successfully!");
            }
        }
        
        String nameSerach = brand.getId()+" " + brand.getName();
        return "redirect:/brand/page/1?sortField=id&sortDir=asc&keyWord=" + nameSerach;
    }
    
    @GetMapping("update/{id}")
    private String updateBrand(@PathVariable("id") Integer  id, RedirectAttributes re,           
            Model model) {
        Optional<Brand> brand = brandService.findById(id);
        if (brand.isEmpty()) {
            re.addFlashAttribute("message", "The brand is not exist!");
            return "redirect:/brand/listBrand";
        } else {  
            model.addAttribute("listCategory", categoryService.showListCategory());
            model.addAttribute("brand", brand.get());
            model.addAttribute("id", id);
            model.addAttribute("update", "Update Brand");
            model.addAttribute("titlePage", "Update brand");
        }
        return "brand/form_brand";
    }
    
    @GetMapping("/page/{pageNum}") 
    private String brandPage (@PathVariable ("pageNum") Integer pageNum,
            @Param("sortField") String sortField,
            @Param("sortDir") String sortDir,
            @Param("keyWord") String keyWord, 
            
            Model model) {
        //sort
        Sort sort = Sort.by(sortField);
        if(sortDir.equalsIgnoreCase("asc"))
            sort = Sort.by(sortField).ascending();
        else  sort = Sort.by(sortField).descending();
        
        org.springframework.data.domain.Pageable pageable = PageRequest.of(pageNum - 1,
                BrandService.SIZE_PAGE_BRAND, sort);
        
        Page<Brand> pageBrand = brandService.findAll(pageable,keyWord); 
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
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("totalPage", pageBrand.getTotalPages()); 
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        
        if(endCount >  pageBrand.getTotalElements()) {
            endCount = pageBrand.getTotalElements();
        }
        
        model.addAttribute("totalElement", pageBrand.getTotalElements());
        
        model.addAttribute("brands", listBrand);
        
        model.addAttribute("elementsCurrentPerPage", pageBrand.getNumberOfElements());
        model.addAttribute("elementsPerPage", BrandService.SIZE_PAGE_BRAND);
        
        model.addAttribute("keyWord", keyWord);
        model.addAttribute("moduleURL", "/brand");
        
        return"brand/listBrand";
    }
    
    @GetMapping("/delete/{id}")
    private String deleteBrand(@PathVariable("id") Integer id,
            RedirectAttributes re,Model model) throws IOException {
        Optional<Brand> brand =  brandService.findById(id);
        if (brand.isEmpty()) {
            re.addFlashAttribute("message", "The brand is not exist!");
            return "redirect:/brand/listBrand";
        }
        else {
            brandService.deleteById(id); 
          //delete folder contains logos
            String dir = "brand-logos/" + id;
            AmazonS3Util.removeFolder(dir);
            
            re.addFlashAttribute("message","Delete brand id: "+ id + " successfully!");           
            return "redirect:/brand/listBrand";
        }
    
    }
    
    @GetMapping("/export/csv")
    public void exportCsv(HttpServletResponse response) throws IOException {
        List<Brand> listBrand = brandService.findAll();
        BrandCsvExporter brandCsvExporter = new BrandCsvExporter();
        brandCsvExporter.export(listBrand, response);
    }
}
