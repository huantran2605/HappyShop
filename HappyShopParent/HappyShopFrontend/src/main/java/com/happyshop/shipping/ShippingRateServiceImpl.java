package com.happyshop.shipping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.happyshop.common.entity.Address;
import com.happyshop.common.entity.Customer;
import com.happyshop.common.entity.ShippingRate;
import com.happyshop.common.entity.setting.Country;

@Service
public class ShippingRateServiceImpl implements ShippingRateService {
    @Autowired
    ShippingRateRepository repo;
    
    
    public ShippingRate findByCustomer(Customer customer) {
        String state = customer.getState();
        if(state == null || state.isEmpty()) {
            state = customer.getCity();
        }
        Country country = customer.getCountry();
        return repo.findByCountryAndState(country, state);
    }
    
    public ShippingRate findByAddress(Address address) {
        String state = address.getState();
        if(state == null || state.isEmpty()) {
            state = address.getCity();
        }
        Country country = address.getCountry();
        return repo.findByCountryAndState(country, state);
    }
}   
