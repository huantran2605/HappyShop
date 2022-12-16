package com.happyshop.setting;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.happyshop.common.entity.setting.Currency;

public interface CurrencyRepository extends JpaRepository<Currency, Integer>{
    
    public List<Currency> findAllByOrderByNameAsc();
}
    