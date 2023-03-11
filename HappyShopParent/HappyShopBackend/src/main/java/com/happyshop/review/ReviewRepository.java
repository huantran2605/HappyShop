package com.happyshop.review;



import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.happyshop.common.entity.Review;
import com.happyshop.common.entity.product.Product;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
   
    @Query("SELECT r FROM Review r WHERE "
            + " CONCAT(r.headline,' ',r.comment,' ', r.customer.firstName,' ',r.customer.lastName,' ', r.product.name) LIKE %?1% ")          
    public Page<Review> findAll(String keyword, Pageable pageable);
    
    @Query("SELECT r FROM Review r WHERE r.product = ?1")
    public List<Review> findByProduct(Product product);
}
