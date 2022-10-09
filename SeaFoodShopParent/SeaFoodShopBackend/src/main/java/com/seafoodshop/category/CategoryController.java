package com.seafoodshop.category;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

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

import com.seafoodshop.FileUploadUtil;
import com.seafoodshop.common.entity.Category;

import antlr.StringUtils;

@Controller
@RequestMapping("category")
public class CategoryController {
    
        @Autowired
        CategoryService categoryService;
        
        @GetMapping("/listCategory")
        private String viewFirstPageCategory(Model model,
                RedirectAttributes re,
                @RequestParam("message")  Optional<String> message) {
            List<Category> list = categoryService.findAll();
            model.addAttribute("categories", list);
            
            re.addAttribute("message", message.orElse(null));
            return "redirect:/category/page/1?sortField=id&sortDir=asc&keyWord=";
        }
        
        @GetMapping("/page/{pageNum}") 
        private String categoryPage (@PathVariable ("pageNum") Integer pageNum,
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
                    categoryService.SIZE_PAGE_CATEGORY, sort);
            
            Page<Category> pageCategory = categoryService.showListCategoryPage(pageable, keyWord.get()); 
            List<Category> listCategory = pageCategory.getContent();
            //list user 
            
            long startCount = (pageNum - 1) * categoryService.SIZE_PAGE_CATEGORY + 1;
            long endCount = startCount + categoryService.SIZE_PAGE_CATEGORY - 1 ;
            if(endCount > pageCategory.getTotalElements() )
                endCount = pageCategory.getTotalElements();
            
            String reserveDir = sortDir.equalsIgnoreCase("asc") ? "des"  : "asc";
            model.addAttribute("reserveDir", reserveDir);
            model.addAttribute("sortField", sortField);
            model.addAttribute("sortDir", sortDir);
            model.addAttribute("totalPage", pageCategory.getTotalPages()); 
            model.addAttribute("currentPage", pageNum);
            model.addAttribute("startCount", startCount);
            model.addAttribute("endCount", endCount);
            
            if(endCount >  pageCategory.getTotalElements()) {
                endCount = pageCategory.getTotalElements();
            }
            
            model.addAttribute("totalElement", pageCategory.getTotalElements());
            
            model.addAttribute("categories", listCategory);
            
            model.addAttribute("categoriesCurrentPerPage", pageCategory.getNumberOfElements());
            model.addAttribute("categoriesPerPage", categoryService.SIZE_PAGE_CATEGORY);
            model.addAttribute("message", message.orElse(null));
            
            model.addAttribute("keyWord", keyWord.get());
            
            return"category/listCategory";
        }
        
        @GetMapping("/new")
        private String form_Category(Category category,Model model) {
            category.setEnable(true);
            model.addAttribute("category", category);  
            model.addAttribute("listCategory", categoryService.showListCategoryForm());
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
                String fileDir = "category-images/" + savedCate.getId();
                // delete old photos if have  
                FileUploadUtil.cleanDir(fileDir);
                FileUploadUtil.saveFile(mutipartFile, fileName, fileDir);
            } else {
                if(!id.isEmpty()) {
                    Optional<Category> oldCategory = categoryService.findById(id.get());
                    category.setImage(oldCategory.get().getImage()); 
                    BeanUtils.copyProperties(category, oldCategory.get());
                    categoryService.save(oldCategory.get());
                    re.addAttribute("message", "Updated Category successfully!");
                }
                else {
                    category.setImage(null);
                    categoryService.save(category);                  
                    re.addAttribute("message", "Added new Category successfully!");
                }
            }
            
            return "redirect:/category/listCategory";
        }

        @GetMapping("update/{id}")
        private String updateCategory(@PathVariable("id") Integer  id,            
                Model model) {
            Optional<Category> cat = categoryService.findById(id);
            if (cat.isEmpty()) {
                model.addAttribute("message", "The category is not exist!");
                return "redirect:/category/listCategory";
            } else {  
                model.addAttribute("listCategory", categoryService.showListCategoryForm());
                model.addAttribute("category", cat.get());
                model.addAttribute("id", id);
                model.addAttribute("update", "Update Category");
            }
            return "category/form_category";

        }

//        
//
//        
//        
//        
//        @GetMapping("/delete/{id}")
//        private String deleteUser(@PathVariable("id") Long id,
//                RedirectAttributes re,Model model) throws IOException {
//            Optional<User> user =  userService.findById(id);
//            if (user.isEmpty()) {
//                re.addAttribute("message", "The user is not exist!");
//                return "redirect:/user/listUser";
//            }
//            else {
//                userService.deleteById(id); 
//                re.addAttribute("message","Delete User has id: "+ id + " successfully!");           
//                return "redirect:/user/listUser";
//            }
//        
//        }
//        
//        @GetMapping("updateEnabled/{id}")
//        private String updateEnableStatus(@PathVariable("id") Long id,
//                RedirectAttributes re){
//            Optional<User> user =  userService.findById(id);
//            String status = "";
//            if (user.isEmpty()) {
//                re.addAttribute("message", "The user is not exist!");
//                return "redirect:/user/listUser";
//            }
//            else {
//                status = userService.updateEnabledStatus(user.get());
//                re.addAttribute("message", status);     
//            }
//            return "redirect:/user/listUser";
//        }
//
//        @GetMapping("/export/csv")
//        public void exportCsv(HttpServletResponse response) throws IOException {
//            List<User> listUser = userService.findAll();
//            UserCsvExporter userCsvExporter = new UserCsvExporter();
//            userCsvExporter.export(listUser, response);
//
//        }
//        @GetMapping("/export/excel")
//        public void exportExcel(HttpServletResponse response) throws IOException {
//            List<User> listUser = userService.findAll();
//            UserExcelExporter userExcelExporter = new UserExcelExporter();
//            userExcelExporter.export(listUser, response);
//            
//        }
//        @GetMapping("/export/pdf")
//        public void exporPdf(HttpServletResponse response) throws IOException {
//            List<User> listUser = userService.findAll();
//            UserPdfExporter userPdfExporter = new UserPdfExporter();
//            userPdfExporter.export(listUser, response);
//            
//        }
//        
//    }
//
//    
}
