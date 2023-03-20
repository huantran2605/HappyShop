package com.happyshop.review;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.happyshop.common.entity.Review;
import com.happyshop.common.exception.ReviewNotFoundException;

@Service
public class ReviewServiceImpl implements ReviewService {
    @Autowired
    ReviewRepository repo;

    public Page<Review> findAll(String keyword, Pageable pageable) {
        if(!keyword.isBlank() || !keyword.isEmpty()) {
            return repo.findAll(keyword, pageable);                      
        }
        return repo.findAll(pageable);
    }

    public Review findById(Integer id) throws ReviewNotFoundException {  
        try {
            return repo.findById(id).get();                       
        } catch (Exception e) {
            throw new ReviewNotFoundException("The review is not existed!");
        }
    }

    public void deleteById(Integer id) throws ReviewNotFoundException {
        try {
            repo.deleteById(id);                     
        } catch (Exception e) {
            throw new ReviewNotFoundException("The review is not existed!");
        }
    }

    public <S extends Review> S save(S entity) {
        return repo.save(entity);
    }
    
    
    
    
    
    
    
}
