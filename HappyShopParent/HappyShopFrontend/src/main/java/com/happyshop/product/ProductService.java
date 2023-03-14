package com.happyshop.product;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;

import com.happyshop.common.entity.Category;
import com.happyshop.common.entity.Customer;
import com.happyshop.common.entity.product.Product;

public interface ProductService {
    
    int SIZE_PAGE_PRODUCT = 10;
    int SIZE_RESULT_PAGE_PRODUCT = 10;
    
    Page<Product> findByCategory(Integer categoryId,Pageable pageable);
    
    Product findByAlias(String alias);
    
    Page<Product> searchProduct(String keyword, Pageable pageable);
    
    void updateQuantity(Integer newQuantity, Integer productId);
    
    Optional<Product> findById(Integer id);
    
    public void setAvarageRatingAndReviewCount(Product product);
    
    public boolean checkProductNeedReview(Customer customer, Integer productId);
}
