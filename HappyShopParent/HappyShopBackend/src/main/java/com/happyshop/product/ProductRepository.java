package com.happyshop.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import com.happyshop.common.entity.product.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>{
    @Query("SELECT u FROM Product u WHERE CONCAT(u.id,' ',u.name,' ', u.alias ) LIKE %?1%")
    public Page<Product> searchProduct (String keyWord, Pageable pageable); 
    
    @Query("SELECT p FROM Product p WHERE p.name = ?1")
    public Product findByName(String name);
}
