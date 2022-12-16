package com.happyshop.shipping;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.happyshop.common.entity.ShippingRate;
import com.happyshop.common.entity.setting.Country;

@Repository
public interface ShippingRateRepository extends JpaRepository<ShippingRate, Integer> {
    
    public ShippingRate findByCountryAndState(Country country, String state);
}
