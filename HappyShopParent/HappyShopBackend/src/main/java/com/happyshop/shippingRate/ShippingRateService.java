package com.happyshop.shippingRate;


import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.happyshop.common.entity.ShippingRate;

public interface ShippingRateService {
    int SIZE_PAGE_SHIPPING_RATE = 10;
    
    Page<ShippingRate> findAll(Pageable pageable, String keyword);
    
    List<ShippingRate> findAll();
    
    void updateCODSupport(Integer id, boolean enabled);
    
    Optional<ShippingRate> findById(Integer id);
    
    <S extends ShippingRate> S save(S entity);
    
    ShippingRate findByCountryAndState(Integer countryId, String state);
    
    String isRateUnique (ShippingRate sr);
    
    void deleteById(Integer id) ;
}
