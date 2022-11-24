package com.happyshop.product;

import java.util.Date;
import java.util.List;
import java.util.Optional;
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
import com.happyshop.common.entity.product.Product;

@Service
public class ProductServiceImpl implements ProductService{
    
    @Autowired
    ProductRepository productRepo;

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
    
    
    

    
}
