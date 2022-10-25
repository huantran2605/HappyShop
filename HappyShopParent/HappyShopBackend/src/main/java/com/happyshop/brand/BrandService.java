package com.happyshop.brand;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.happyshop.common.entity.Brand;

public interface BrandService {
    
    int SIZE_PAGE_BRAND = 7;

    <S extends Brand> List<S> findAll(Example<S> example, Sort sort);

    Brand getById(Integer id);

    void deleteAll();

    void delete(Brand entity);

    <S extends Brand> Page<S> findAll(Example<S> example, Pageable pageable);

    Optional<Brand> findById(Integer id);

    List<Brand> findAllById(Iterable<Integer> ids);

    List<Brand> findAll(Sort sort);

    Page<Brand> findAll(Pageable pageable);

    List<Brand> findAll();

    <S extends Brand> S save(S entity);

    String isNameUnique(Integer id, String name);

    Page<Brand> findAll(Pageable pageable, String keyword);

    void deleteById(Integer id);  

}
