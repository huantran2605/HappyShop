package com.happyshop.shipping;

import com.happyshop.common.entity.Address;
import com.happyshop.common.entity.Customer;
import com.happyshop.common.entity.ShippingRate;

public interface ShippingRateService {
    ShippingRate findByCustomer(Customer customer);
    
    ShippingRate findByAddress(Address address);
    
}
