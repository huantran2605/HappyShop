package com.happyshop.category;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.happyshop.common.entity.Category;


@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    @Query("select c from Category c where c.enable = true order by c.name asc")
    public List<Category> findAllEnabled(); 
    
    @Query("select c from Category c where c.enable = true and c.alias = ?1")
    public Category findByAliasEnabled(String alias);
}
