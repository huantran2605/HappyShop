package com.happyshop.shippingRate;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.happyshop.common.entity.Customer;
import com.happyshop.common.entity.ShippingRate;

@Service
public class ShippingRateServiceImpl implements ShippingRateService {
    @Autowired
    ShippingRateRepository repo;

    @Override
    public List<ShippingRate> findAll() { 
        return repo.findAll();
    }
    
    @Override   
    public Page<ShippingRate> findAll(Pageable pageable, String keyWord) {       
        if (!keyWord.isBlank()) {
            return repo.findAllByKeyword(keyWord, pageable);  
        }
        return repo.findAll(pageable);
    }
    
    
    
}
