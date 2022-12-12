package com.happyshop.shippingRate;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.happyshop.common.entity.Customer;
import com.happyshop.common.entity.ShippingRate;

public interface ShippingRateService {
    int SIZE_PAGE_SHIPPING_RATE = 10;
    
    Page<ShippingRate> findAll(Pageable pageable, String keyword);
    
    List<ShippingRate> findAll();
}
