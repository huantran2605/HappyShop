package com.happyshop.review;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.happyshop.common.entity.Customer;
import com.happyshop.common.entity.Review;
import com.happyshop.common.entity.order.Order;
import com.happyshop.common.entity.product.Product;
import com.happyshop.common.exception.ReviewNotFoundException;

public interface ReviewService {
    int SIZE_PAGE_PRODUCT = 5;
    
    public Page<Review> findAll(String keyword,Customer customer, Pageable pageable);
    
    public Review findById(Integer id) throws ReviewNotFoundException;
    
    public List<Review> getMostRecentReviewOfProduct(Product product);
    
    public List<Review> findByProduct(Product product);
    
    public Page<Review> findByProduct(Product product, Pageable pageable);
    
    public <S extends Review> S save(S entity);
    
    public int checkCustomerHasReviewForProduct(Customer customer, Product product);
    
    
}
