package com.happyshop.product;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;
import org.springframework.stereotype.Service;

import com.happyshop.common.entity.Brand;
import com.happyshop.common.entity.Category;
import com.happyshop.common.entity.Customer;
import com.happyshop.common.entity.Review;
import com.happyshop.common.entity.order.Order;
import com.happyshop.common.entity.order.OrderDetail;
import com.happyshop.common.entity.product.Product;
import com.happyshop.order.OrderRepository;
import com.happyshop.order.OrderService;
import com.happyshop.review.ReviewRepository;
import com.happyshop.review.ReviewService;

@Service
public class ProductServiceImpl implements ProductService{
    
    @Autowired
    ProductRepository productRepo;
    @Autowired
    ReviewRepository reviewRepo;
    @Autowired
    OrderRepository orderRepo;
    @Autowired
    ReviewService reviewService;

    public Page<Product> findByCategory(Integer categoryId,Pageable pageable) {
        String categoryIdMatch = "-" + String.valueOf(categoryId) +"-";      
        return productRepo.findByCategory(categoryId,categoryIdMatch, pageable);
    }

    public Product findByAlias(String alias) {
        return productRepo.findByAlias(alias);
    }

    public Page<Product> searchProduct(String keyword, Pageable pageable) {
        return productRepo.searchProduct(keyword, pageable);
    }

    public void updateQuantity(Integer newQuantity, Integer productId) {
        productRepo.updateQuantity(newQuantity, productId);
    }

    public Optional<Product> findById(Integer id) {
        return productRepo.findById(id);
    }
    
    public void setAvarageRatingAndReviewCount(Product product) {
        List<Review> listReview = reviewRepo.findByProduct(product);
        
        int count = listReview.size();
        product.setReview_count(count);
        
        float totalRating = 0;
        for (Review review : listReview) {
            totalRating += review.getRating();
        }
        float avr_rating = (float) Math.round(totalRating / count * 100) / 100; 
        product.setAverage_rating(avr_rating);
        
        productRepo.save(product);        
    }
    
    public boolean checkProductNeedReview(Customer customer, Integer productId) {
        
        boolean productAtOrder = false;
        List<Order> listOrder = orderRepo.findAll(customer);
        for (Order order : listOrder) {
            Set<OrderDetail> listOD = order.getOrderDetails();
            for (OrderDetail oD : listOD) {
                if( oD.getProduct().getId() == productId) {
                    if(oD.getOrder().isDelivered()) {
                        productAtOrder = true;   
                        break;
                    }                   
                }
            }
            if(productAtOrder) {
                break;
            }
        }
        
        if(productAtOrder) {
            return true;
        }
               
        return false;         
    }
   
}
