package com.seafoodshop.brand;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.seafoodshop.common.entity.Brand;

@Service
public class BrandServiceImpl implements BrandService{
    
    @Autowired
    BrandRepository brandRepo;

    @Override
    public <S extends Brand> S save(S entity) {
        return brandRepo.save(entity);
    }

    @Override
    public List<Brand> findAll() {
        return brandRepo.findAll();
    }

    @Override
    public Page<Brand> findAll(Pageable pageable) {
        return brandRepo.findAll(pageable);
    }

    @Override
    public List<Brand> findAll(Sort sort) {
        return brandRepo.findAll(sort);
    }

    @Override
    public List<Brand> findAllById(Iterable<Integer> ids) {
        return brandRepo.findAllById(ids);
    }

    @Override
    public Optional<Brand> findById(Integer id) {
        return brandRepo.findById(id);
    }

    @Override
    public <S extends Brand> Page<S> findAll(Example<S> example, Pageable pageable) {
        return brandRepo.findAll(example, pageable);
    }

    @Override
    public void delete(Brand entity) {
        brandRepo.delete(entity);
    }

    @Override
    public void deleteAll() {
        brandRepo.deleteAll();
    }

    @Override
    public Brand getById(Integer id) {
        return brandRepo.getById(id);
    }

    @Override
    public <S extends Brand> List<S> findAll(Example<S> example, Sort sort) {
        return brandRepo.findAll(example, sort);
    }
    
    
}
