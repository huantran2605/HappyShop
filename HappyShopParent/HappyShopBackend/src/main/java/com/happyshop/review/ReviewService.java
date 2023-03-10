package com.happyshop.review;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.happyshop.common.entity.Review;
import com.happyshop.common.exception.ReviewNotFoundException;

public interface ReviewService {
    int SIZE_PAGE_PRODUCT = 5;
    
    public Page<Review> findAll(String keyword, Pageable pageable);
    
    public Review findById(Integer id) throws ReviewNotFoundException;
    
    public void deleteById(Integer id) throws ReviewNotFoundException;
    
    public <S extends Review> S save(S entity);
}
