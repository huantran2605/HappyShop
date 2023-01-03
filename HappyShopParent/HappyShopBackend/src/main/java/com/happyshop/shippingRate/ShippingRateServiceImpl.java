package com.happyshop.shippingRate;



import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.happyshop.common.entity.Brand;
import com.happyshop.common.entity.Customer;
import com.happyshop.common.entity.ShippingRate;
import com.happyshop.common.entity.product.Product;
import com.happyshop.product.ProductRepository;

@Service
public class ShippingRateServiceImpl implements ShippingRateService {
    @Autowired
    ShippingRateRepository shipRepo;
    @Autowired
    ProductRepository productRepo;
    
    private static final int DIM_DIVISOR = 319;

    @Override
    public List<ShippingRate> findAll() { 
        return shipRepo.findAll();
    }
    
    @Override   
    public Page<ShippingRate> findAll(Pageable pageable, String keyWord) {       
        if (!keyWord.isBlank()) {
            return shipRepo.findAllByKeyword(keyWord, pageable);  
        }
        return shipRepo.findAll(pageable);
    }

    public void updateCODSupport(Integer id, boolean enabled) {
        shipRepo.updateCODSupport(id, enabled);
    }

    public Optional<ShippingRate> findById(Integer id) {
        return shipRepo.findById(id);
    }

    public <S extends ShippingRate> S save(S entity) {
        return shipRepo.save(entity);
    }

    public ShippingRate findByCountryAndState(Integer countryId, String state) {
        return shipRepo.findByCountryAndState(countryId, state);
    }
    
    
    
    public void deleteById(Integer id) {
        shipRepo.deleteById(id);
    }

    @Override
    public String isRateUnique (ShippingRate sr) {
        
        ShippingRate rateInDb = shipRepo.findByCountryAndState(sr.getCountry().getId(),
                sr.getState());         
        if(sr.getId() == null) { //create new 
            if(rateInDb != null) {
                return "duplicated";
            } 
            else {
                return "ok";
            }
        }
        else { // update
            if(rateInDb != null) {
                if(rateInDb.getId() ==  sr.getId()) {
                    return "ok";
                } 
                else {
                    return "duplicated";
                }
            }
            else {
                return "ok";
            }                     
        }
    }  
    
    public float calculateShippingCost(Integer productId, Integer countryId, String state) 
            throws ShippingRateNotFoundException {
        ShippingRate shippingRate = shipRepo.findByCountryAndState(countryId, state);
        
        if (shippingRate == null) {
            throw new ShippingRateNotFoundException("No shipping rate found for the given "
                    + "destination. You have to enter shipping cost manually.");
        }
        
        Product product = productRepo.findById(productId).get();
        
        float dimWeight = (product.getLength() * product.getWidth() * product.getHeight()) / DIM_DIVISOR;
        float finalWeight = product.getWeight() > dimWeight ? product.getWeight() : dimWeight;
                
        return finalWeight * shippingRate.getRate();
    }
    
}
