package com.happyshop.brand;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.happyshop.common.entity.Brand;
import com.happyshop.common.entity.Category;
@Repository
public interface BrandRepository extends JpaRepository<Brand, Integer> {
    
    @Query("SELECT u FROM Brand u WHERE CONCAT(u.id,' ',u.name) LIKE %?1%")
    public Page<Brand> searchBrand (String keyWord, Pageable pageable);  
    
    @Query("SELECT u FROM Brand u WHERE u.name = :name")
    public Brand findByName( @Param("name") String name); 
}
