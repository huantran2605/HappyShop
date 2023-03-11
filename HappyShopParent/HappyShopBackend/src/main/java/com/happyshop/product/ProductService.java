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
import com.happyshop.common.entity.product.Product;

public interface ProductService {
    
    int SIZE_PAGE_PRODUCT = 5;

    Product getById(Integer id);

    <S extends Product, R> R findBy(Example<S> example, Function<FetchableFluentQuery<S>, R> queryFunction);

    void delete(Product entity);

    void deleteById(Integer id);

    <S extends Product> boolean exists(Example<S> example);

    boolean existsById(Integer id);

    <S extends Product> List<S> saveAll(Iterable<S> entities);

    Optional<Product> findById(Integer id);

    List<Product> findAllById(Iterable<Integer> ids);

    List<Product> findAll(Sort sort);

    <S extends Product> S save(S entity);

    List<Product> findAll();

    Page<Product> findAll(Pageable pageable, String keyword, Integer categoryID);

    String isNameUnique(Integer id, String name);

    String updateEnabledStatus(Product product);
    
    void saveProductPriceAndQuantity(Product productInForm);
    
    public void setAvarageRatingAndReviewCount(Product product);

}
