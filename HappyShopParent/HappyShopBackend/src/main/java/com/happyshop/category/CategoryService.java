package com.happyshop.category;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.happyshop.common.entity.Category;
import com.happyshop.common.entity.User;

public interface CategoryService {
    int SIZE_PAGE_CATEGORY = 5;

    String updateEnabledStatus(Category category);

    List<Category> findAll();

    Category getById(Integer id);

    void deleteAll();

    void delete(Category entity);

    void deleteById(Integer id);

    long count();

    <S extends Category> List<S> saveAll(Iterable<S> entities);

    Optional<Category> findById(Integer id);

    List<Category> findAllById(Iterable<Integer> ids);

    List<Category> findAll(Sort sort);

    Page<Category> findAll(Pageable pageable);

    <S extends Category> S save(S entity);

    Page<Category> findAll(Pageable pageable, String keyword);

    void printSubCategory(List<Category> listInForm, Category parent, int level);

    List<Category> showListCategory();


    int IsNameOrAliasUnique(Integer id, String name, String alias);

    

}
