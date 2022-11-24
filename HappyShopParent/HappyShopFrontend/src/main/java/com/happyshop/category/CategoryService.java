package com.happyshop.category;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.happyshop.common.entity.Category;
import com.happyshop.common.entity.User;

public interface CategoryService {
   
    List<Category> listNoChildrenCategories();

    Category getCategory (String alias);
    
    List<Category> getListParentCategory (Category category);
}
