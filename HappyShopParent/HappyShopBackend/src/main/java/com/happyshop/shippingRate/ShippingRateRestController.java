package com.happyshop.shippingRate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShippingRateRestController {
    @Autowired
    ShippingRateService shipService;
    
    @PostMapping("/get_shipping_cost")
    public String getShippingCost(Integer productId, Integer countryId, String state)throws ShippingRateNotFoundException {
        float shipCost = shipService.calculateShippingCost(productId, countryId, state);
        return String.valueOf(shipCost);
    }
}
