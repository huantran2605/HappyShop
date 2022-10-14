package com.seafoodshop.category;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.seafoodshop.common.entity.Category;
import com.seafoodshop.common.entity.User;
import com.seafoodshop.common.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    @Query("SELECT u FROM Category u WHERE CONCAT(u.id,' ',u.name,' ', u.alias ) LIKE %?1%")
    public Page<Category> searchCategory (String keyWord, Pageable pageable);  
    
    @Query("SELECT u FROM Category u WHERE u.name = :name")
    public Category findByName( @Param("name") String name); 
    
    @Query("SELECT u FROM Category u WHERE u.alias = :alias")
    public Category findByAlias( @Param("alias") String alias); 

    
}
