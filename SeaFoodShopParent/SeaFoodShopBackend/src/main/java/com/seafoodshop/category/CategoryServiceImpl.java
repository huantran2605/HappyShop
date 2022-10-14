package com.seafoodshop.category;

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

import com.seafoodshop.common.entity.Category;
import com.seafoodshop.common.entity.User;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryRepository categoryRepo;

    @Override
    public <S extends Category> S save(S entity) {
        return categoryRepo.save(entity);
    }

    @Override
    public Page<Category> findAll(Pageable pageable) {
        return categoryRepo.findAll(pageable);
    }

    @Override
    public List<Category> findAll(Sort sort) {
        return categoryRepo.findAll(sort);
    }

    @Override
    public List<Category> findAllById(Iterable<Integer> ids) {
        return categoryRepo.findAllById(ids);
    }

    @Override
    public Optional<Category> findById(Integer id) {
        return categoryRepo.findById(id);
    }

    @Override
    public <S extends Category> List<S> saveAll(Iterable<S> entities) {
        return categoryRepo.saveAll(entities);
    }

    @Override
    public long count() {
        return categoryRepo.count();
    }

    @Override
    public void deleteById(Integer id) {
        categoryRepo.deleteById(id);
    }

    @Override
    public void delete(Category entity) {
        categoryRepo.delete(entity);
    }

    @Override
    public void deleteAll() {
        categoryRepo.deleteAll();
    }

    @Override
    public Category getById(Integer id) {
        return categoryRepo.getById(id);
    }

    @Override
    public List<Category> findAll() {
        return categoryRepo.findAll();
    }

    @Override
    public String updateEnabledStatus(Category category) {
        String status = "";
        if (category.isEnable() == true) {
            category.setEnable(false);
            status = "Disable the category id: " + category.getId() + " successfully!";
        } else {
            category.setEnable(true);
            status = "Enable the category id: " + category.getId() + " successfully!";
        }
        categoryRepo.save(category);
        return status;
    }

    @Override
    public Page<Category> findAll(Pageable pageable, String keyword) {
        if (!keyword.isBlank()) {
            return categoryRepo.searchCategory(keyword, pageable);
        }
        return categoryRepo.findAll(pageable);
    }


    @Override
       public List<Category> showListCategory() {
           List<Category> listInForm = new ArrayList<>();
           List<Category> listRoot =  categoryRepo.findAll(); 
           for (Category category : listRoot) {
               if(category.getParent() == null) {
                   listInForm.add(new Category(category.getId(),category.getName()
                           ,category.getAlias(),category.getImage(),
                           category.isEnable(),category.getParent(),category.getChildren()));
                   printSubCategory(listInForm, category, 1);
               }
           }
           return listInForm;
       }
       @Override
       public void printSubCategory(List<Category> listInForm,Category parent, int level) {
           int nextLevel = level + 1;
           Set<Category> children = parent.getChildren();
           for (Category subCategory : children) {
               String name="";
               for (int i = 0; i < level; i++) {
                   name += "--";
               }
               name += subCategory.getName();
               listInForm.add(new Category(subCategory.getId(),name
                       ,subCategory.getAlias(),subCategory.getImage(),
                       subCategory.isEnable(),subCategory.getParent(),subCategory.getChildren()));
               printSubCategory(listInForm,subCategory, nextLevel);
           }
       }
       
       @Override
    public int IsNameOrAliasUnique(Integer id, String name, String alias) {
           Category category1 = categoryRepo.findByName(name);
           Category category2 = categoryRepo.findByAlias(alias);
           if(id == null) {//create
               if(category1 != null && category2 != null) {
                   return 1;//both name and alias are duplicated                  
               }
               else if(category1 != null && category2 == null) {
                   return 2;//name is duplicated
               }
               else if(category1 == null && category2 != null) {
                   return 3;//alias is duplicated
               }
               else
                   return 0;// unique            
           }
           else {//update 
               if(category1 == null && category2 == null) {
                   return 0;
               }
               if(category1 == null || category2 == null) {
                   if(category1 == null && category2.getId() == id) {
                       return 0;
                   }
                   else if(category1.getId() == id && category2 == null) {
                       return 0;
                   }                 
               }
               if(category1 != null || category2 != null){
                   if(category1.getId() != id && category2.getId() != id) {
                       return 1;
                   }
                   else if(category1.getId() != id && category2.getId() == id || category1.getId() != id && category2 == null) {
                       return 2;
                   }
                   else if(category1.getId() == id && category2.getId() != id || category1 == null && category2.getId() != id) {
                       return 3;
                   }
                   else if(category1.getId() == id && category2.getId() == id) {
                       return 0;
                   } 
               }     
           return 1;
       }
}
}
