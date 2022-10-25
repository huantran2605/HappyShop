package com.happyshop.product;

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

import com.happyshop.common.entity.Category;
import com.happyshop.common.entity.product.Product;

@Service
public class ProductServiceImpl implements ProductService{
    
    @Autowired
    ProductRepository productRepo;

    @Override
    public <S extends Product> S save(S entity) {
        return productRepo.save(entity);
    }

    @Override
    public Page<Product> findAll(Pageable pageable, String keyword) {
        if (!keyword.isBlank()) {
            return productRepo.searchProduct(keyword, pageable);
        }
        return productRepo.findAll(pageable);
    }

    @Override
    public List<Product> findAll(Sort sort) {
        return productRepo.findAll(sort);
    }
    
    @Override
    public List<Product> findAll() {
        return productRepo.findAll();
    }
    
    @Override
    public List<Product> findAllById(Iterable<Integer> ids) {
        return productRepo.findAllById(ids);
    }

    @Override
    public Optional<Product> findById(Integer id) {
        return productRepo.findById(id);
    }

    @Override
    public <S extends Product> List<S> saveAll(Iterable<S> entities) {
        return productRepo.saveAll(entities);
    }

    @Override
    public boolean existsById(Integer id) {
        return productRepo.existsById(id);
    }

    @Override
    public <S extends Product> boolean exists(Example<S> example) {
        return productRepo.exists(example);
    }

    @Override
    public void deleteById(Integer id) {
        productRepo.deleteById(id);
    }

    @Override
    public void delete(Product entity) {
        productRepo.delete(entity);
    }

    @Override
    public <S extends Product, R> R findBy(Example<S> example, Function<FetchableFluentQuery<S>, R> queryFunction) {
        return productRepo.findBy(example, queryFunction);
    }

    @Override
    public Product getById(Integer id) {
        return productRepo.getById(id);
    }
    
    
}
