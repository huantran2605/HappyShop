package com.happyshop.category;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.happyshop.common.entity.Category;
import com.happyshop.common.entity.User;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryRepository categoryRepo;

   public List<Category> listNoChildrenCategories(){
       List<Category> listNoChildren = new ArrayList<>();
       
       List<Category> listAllEnabled = categoryRepo.findAllEnabled();
       for (Category category : listAllEnabled) {
           if(category.getChildren() == null || category.getChildren().size() == 0) {
               listNoChildren.add(category);
           }
       } 
       return listNoChildren;
   }
   
   public Category getCategory (String alias) {
       return categoryRepo.findByAliasEnabled(alias);
   }
   
   public List<Category> getListParentCategory (Category category) {
       List<Category> listParentCategory = new ArrayList<>();
       Category parent = category.getParent();
       while(parent != null) {
           listParentCategory.add(0, parent);
           parent = parent.getParent();
       }
       listParentCategory.add(category);
       return listParentCategory;
   }
} 
