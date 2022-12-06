package com.happyshop.setting.country;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Example;

import com.happyshop.common.entity.Country;

public interface CountryService {
    List<Country> findAllByOrderByNameAsc();
    
    <S extends Country> S save(S entity);
    
    void deleteById(Integer id);
    
    Optional<Country> findById(Integer id);
    
    Country findByCode(String code);
}
