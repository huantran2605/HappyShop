package com.happyshop.product;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import com.happyshop.common.entity.product.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>{
    @Query("SELECT u FROM Product u WHERE u.enable = true AND (u.category.id = ?1 OR "
            + "u.category.allParentIDs LIKE %?2%) order by u.name asc")
    public Page<Product> findByCategory (Integer categoryId,String categoryIdMatch, Pageable pageable); 
    
    public Product findByAlias(String alias);
    
    @Query(value = "SELECT * FROM products WHERE enable = true AND "
            + "MATCH (name, short_description, full_description) AGAINST (?1)",
            nativeQuery = true)
    public Page<Product> searchProduct (String keyword, Pageable pageable); 
    
    @Modifying
    @Query("UPDATE Product p SET p.quantity = ?1 where p.id = ?2")
    @Transactional
    public void updateQuantity(Integer newQuantity, Integer productId);

}
