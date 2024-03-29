package com.happyshop.review;



import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.happyshop.common.entity.Customer;
import com.happyshop.common.entity.order.Order;
import com.happyshop.common.entity.product.Product;
import com.happyshop.common.entity.review.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
   
    @Query("SELECT r FROM Review r WHERE r.customer = ?2 AND"
            + " CONCAT(r.headline,' ',r.comment,' ', r.product.name,' ', 'id', r.id) LIKE %?1% ")          
    public Page<Review> findAll(String keyword, Customer customer, Pageable pageable);
    
    @Query("SELECT r FROM Review r WHERE r.customer = ?1 ")          
    public Page<Review> findAll(Customer customer, Pageable pageable);
    
    @Query("SELECT r FROM Review r WHERE r.product = ?1  ORDER BY r.reviewTime DESC")
    public List<Review> findByProduct(Product product);
    
    @Query("SELECT r FROM Review r WHERE r.product = ?1  ")
    public Page<Review> findByProduct(Product product, Pageable pageable);
    
    @Query("SELECT r FROM Review r WHERE r.customer = ?1 AND r.product = ?2")
    public Review findByProductAndCustomer(Customer customer, Product product);
    
    
}
