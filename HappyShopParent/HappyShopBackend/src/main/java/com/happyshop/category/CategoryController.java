package com.happyshop.category;

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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
import com.happyshop.common.entity.Category;

import antlr.StringUtils;

@Controller
@RequestMapping("category")
public class CategoryController {
    
        @Autowired
        CategoryService categoryService;
        
        @GetMapping("/listCategory")
        private String viewFirstPageCategory(Model model) {
            return categoryPage(1, "id", "asc", "", model);
        }
        
        @GetMapping("/page/{pageNum}") 
        private String categoryPage (@PathVariable ("pageNum") Integer pageNum,
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
                    CategoryService.SIZE_PAGE_CATEGORY, sort);
            
            Page<Category> pageCategory = categoryService.findAll(pageable,keyWord); 
            List<Category> listCategory = pageCategory.getContent();
            //list cat 
            
            long startCount = (pageNum - 1) * CategoryService.SIZE_PAGE_CATEGORY + 1;
            long endCount = startCount + CategoryService.SIZE_PAGE_CATEGORY - 1 ;
            if(endCount > pageCategory.getTotalElements() )
                endCount = pageCategory.getTotalElements();
            
            String reserveDir = sortDir.equalsIgnoreCase("asc") ? "des"  : "asc";
            model.addAttribute("reserveDir", reserveDir);
            model.addAttribute("sortField", sortField);
            model.addAttribute("sortDir", sortDir);
            model.addAttribute("pageNum", pageNum);
            model.addAttribute("totalPage", pageCategory.getTotalPages()); 
            model.addAttribute("currentPage", pageNum);
            model.addAttribute("startCount", startCount);
            model.addAttribute("endCount", endCount);
            
            if(endCount >  pageCategory.getTotalElements()) {
                endCount = pageCategory.getTotalElements();
            }
            
            model.addAttribute("totalElement", pageCategory.getTotalElements());
            
            model.addAttribute("categories", listCategory);
            
            model.addAttribute("elementsCurrentPerPage", pageCategory.getNumberOfElements());
            model.addAttribute("elementsPerPage", CategoryService.SIZE_PAGE_CATEGORY);
            
            model.addAttribute("keyWord", keyWord);
            model.addAttribute("moduleURL", "/category");
            
            return"category/listCategory";
        }
        
        @GetMapping("/new")
        private String form_Category(Category category,Model model) {
            category.setEnable(true);
            model.addAttribute("category", category);  
            model.addAttribute("listCategory", categoryService.showListCategory());
            model.addAttribute("titlePage", "Create new category");
            return "category/form_category";
        }
           
        @PostMapping("/saveOrUpdate")
        private String saveCategory(Category category, @RequestParam("imageFile") MultipartFile mutipartFile,
                @RequestParam("id") Optional<Integer> id,
                RedirectAttributes re,
                Model model) throws IOException {
       
            // upload photo
            if (!mutipartFile.isEmpty()) {
                String fileName = org.springframework.util.StringUtils.cleanPath(mutipartFile.getOriginalFilename());
                category.setImage(fileName);
                Category savedCate = categoryService.save(category);
                String fileDir = "../category-images/" + savedCate.getId();
                // delete old photos if have  
                FileUploadUtil.cleanDir(fileDir);
                FileUploadUtil.saveFile(mutipartFile, fileName, fileDir);
            } else {
                if(!id.isEmpty()) {
                    Optional<Category> oldCategory = categoryService.findById(id.get());
                    category.setImage(oldCategory.get().getImage()); 
                    BeanUtils.copyProperties(category, oldCategory.get());
                    categoryService.save(oldCategory.get());
                    re.addFlashAttribute("message", "Updated Category successfully!");
                }
                else {
                    category.setImage(null);
                    categoryService.save(category);                  
                    re.addFlashAttribute("message", "Added new Category successfully!");
                }
            }
            String nameSearch = category.getName().replace(" ", "_");
            return "redirect:/category/page/1?sortField=id&sortDir=asc&keyWord=" + nameSearch;
        }

        @GetMapping("update/{id}")
        private String updateCategory(@PathVariable("id") Integer  id,            
                Model model) {
            Optional<Category> cat = categoryService.findById(id);
            if (cat.isEmpty()) {
                model.addAttribute("message", "The category is not exist!");
                return "redirect:/category/listCategory";
            } else {  
                model.addAttribute("listCategory", categoryService.showListCategory());
                model.addAttribute("category", cat.get());
                model.addAttribute("id", id);
                model.addAttribute("update", "Update Category");
                model.addAttribute("titlePage", "Update category");
            }
            return "category/form_category";

        }
        
        @GetMapping("updateEnabled/{id}")
        private String updateEnableStatus(@PathVariable("id") Integer id,
                RedirectAttributes re,Model model){
            Optional<Category> category =  categoryService.findById(id);
            String status = "";
            if (category.isEmpty()) {
                re.addFlashAttribute("message", "The category is not exist!");
                return "redirect:/user/listUser";
            }
            else {
                status = categoryService.updateEnabledStatus(category.get());
                re.addFlashAttribute("message", status);    
            }
            String nameSearch = category.get().getName().replace(" ", "_");
            return "redirect:/category/page/1?sortField=id&sortDir=asc&keyWord=" + nameSearch;
        }      
        
        @GetMapping("/delete/{id}")
        private String deleteCategory(@PathVariable("id") Integer id,
                RedirectAttributes re,Model model) throws IOException {
            Optional<Category> category =  categoryService.findById(id);
            if (category.isEmpty()) {
                re.addFlashAttribute("message", "The category is not exist!");
                return "redirect:/category/listCategory";
            }
            else {
                categoryService.deleteById(id); 
              //delete folder contains images
                String dir = "../category-images/" + id;
                FileUtils.deleteDirectory(new File(dir));
                
                re.addFlashAttribute("message","Delete category id: "+ id + " successfully!");           
                return "redirect:/category/listCategory";
            }
        
        }
        
        @GetMapping("/export/csv")
        public void exportCsv(HttpServletResponse response) throws IOException {
            List<Category> listCategory = categoryService.findAll();
            CategoryCsvExporter categoryCsvExporter = new CategoryCsvExporter();
            categoryCsvExporter.export(listCategory, response);

        }
        
}
