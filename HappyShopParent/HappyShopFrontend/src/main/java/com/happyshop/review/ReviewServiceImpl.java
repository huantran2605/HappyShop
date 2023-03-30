package com.happyshop.review;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.happyshop.common.entity.Customer;
import com.happyshop.common.entity.order.Order;
import com.happyshop.common.entity.product.Product;
import com.happyshop.common.entity.review.Review;
import com.happyshop.common.exception.ReviewNotFoundException;

@Service
public class ReviewServiceImpl implements ReviewService {
    @Autowired
    ReviewRepository repo;

    public Page<Review> findAll(String keyword, Customer customer, Pageable pageable) {
        if(!keyword.isBlank() || !keyword.isEmpty()) {
            return repo.findAll(keyword, customer, pageable);                      
        }
        return repo.findAll(customer, pageable);
    }

    public Review findById(Integer id) throws ReviewNotFoundException {
        try {
            return repo.findById(id).get();                       
        } catch (Exception e) {
            throw new ReviewNotFoundException("The review is not existed!");
        }
    }

    public List<Review> getMostRecentReviewOfProduct(Product product) {
        List<Review> reviews = repo.findByProduct(product);
        
        List<Review> recentReviews = new ArrayList<>();
        
        int size = reviews.size();
        int index = 0;
        while(size > 0) {
            recentReviews.add(reviews.get(index));
            index++;
            if(index == 3) {
                break;
            }
            size--;
        }
        
        return recentReviews;
    }

    public List<Review> findByProduct(Product product) {
        return repo.findByProduct(product);
    }

    public Page<Review> findByProduct(Product product, Pageable pageable) {
        return repo.findByProduct(product, pageable);
    }

    public <S extends Review> S save(S entity) {
        return repo.save(entity);
    }

    public int checkCustomerHasReviewForProduct(Customer customer, Product product) {
        Review r = repo.findByProductAndCustomer(customer, product);
        if(r != null) {
            return r.getId();
        }
        return -1;
    }


    
    
}
