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

    @Override
    public <S extends Product> S save(S entity) {
        if(entity.getId() == null) {
            entity.setCreatedTime(new Date());
        }
        if(entity.getAlias() == null || entity.getAlias().isEmpty()) {
            entity.setAlias(entity.getName().replace(" ", "-"));
        }
        else {
            entity.setAlias(entity.getAlias().replace(" ", "-"));
        }
        entity.setUpdatedTime(new Date());
        
        
        return productRepo.save(entity);
    }

    @Override
    public Page<Product> findAll( Pageable pageable, String keyword, Integer categoryId) {
        if (!keyword.isBlank() && keyword != null) {
            if(categoryId > 0 && categoryId != null) {
                String categoryIdMatch = "-" + String.valueOf(categoryId) + "-";
                return productRepo.searchInCategory(categoryId, categoryIdMatch, keyword, pageable);
            }
            return productRepo.searchProduct(keyword, pageable);
        }
        if(categoryId > 0 && categoryId != null) {
            String categoryIdMatch = "-" + String.valueOf(categoryId) + "-";
            return productRepo.findAllInCategory(categoryId, categoryIdMatch, pageable);
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
    
    @Override
    public String isNameUnique(Integer id, String name) {
        if(id == null) { //create new 
            Product product =  productRepo.findByName(name);           
            if(product != null) {
                return "duplicated";
            } 
            else {
                return "ok";
            }
        }
        else { // update
            Product product =  productRepo.findByName(name); 
            if(product != null) {
                if(product.getId() ==  id) {
                    return "ok";
                } 
                else {
                    return "duplicated";
                }
            }
            else {
                return "ok";
            }     
        }
    }
    
    @Override
    public String updateEnabledStatus(Product product) {
        String status = "";
        if (product.isEnable() == true) {
            product.setEnable(false);
            status = "Disable the product id: " + product.getId() + " successfully!";
        } else {
            product.setEnable(true);
            status = "Enable the product id: " + product.getId() + " successfully!";
        }
        productRepo.save(product);
        return status;
    }
    
    public void saveProductPrice(Product productInForm) {
        Product productInDB = productRepo.findById(productInForm.getId()).get();
        productInDB.setCost(productInForm.getCost());
        productInDB.setPrice(productInForm.getPrice());
        productInDB.setDiscountPercent(productInForm.getDiscountPercent());
        
        productRepo.save(productInDB);
    }
}
