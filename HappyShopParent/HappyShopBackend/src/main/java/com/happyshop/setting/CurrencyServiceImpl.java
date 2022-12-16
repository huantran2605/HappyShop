package com.happyshop.setting;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.happyshop.common.entity.setting.Currency;
import com.happyshop.common.entity.setting.Setting;

@Service
public class CurrencyServiceImpl implements CurrencyService{
    @Autowired
    CurrencyRepository currencyRepository;

    public List<Currency> findAll() {
        return currencyRepository.findAll();
    }

    public Optional<Currency> findById(Integer id) {
        return currencyRepository.findById(id);
    }
    
    
    
    
}
