package com.seafoodshop.category;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.seafoodshop.common.entity.Category;
import com.seafoodshop.common.entity.User;


@Service
public class CategoryServiceImpl implements CategoryService{
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
    public String updateEnabledStatus(Category category){
        String status= "";
        if(category.isEnable() == true) {
            category.setEnable(false);
            status = "Disable the category has id: " + category.getId()+ " successfully!";
        }
        else {
            category.setEnable(true);
            status = "Enable the category has id: " + category.getId()+ " successfully!";
        } 
        categoryRepo.save(category);
        return status;
    }
    @Override
    public Page <Category> findAll (Pageable pageable, String keyword) {
        if(!keyword.isBlank()) {
            return categoryRepo.searchCategory(keyword, pageable);
        }  
        return categoryRepo.findAll(pageable);
    }

    
        
}
