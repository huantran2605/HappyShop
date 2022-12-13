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

    public void updateCODSupport(Integer id, boolean enabled) {
        repo.updateCODSupport(id, enabled);
    }

    public Optional<ShippingRate> findById(Integer id) {
        return repo.findById(id);
    }

    public <S extends ShippingRate> S save(S entity) {
        return repo.save(entity);
    }

    public ShippingRate findByCountryAndState(Integer countryId, String state) {
        return repo.findByCountryAndState(countryId, state);
    }
    
    
    
    public void deleteById(Integer id) {
        repo.deleteById(id);
    }

    @Override
    public String isRateUnique (ShippingRate sr) {
        
        ShippingRate rateInDb = repo.findByCountryAndState(sr.getCountry().getId(),
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
    
    
}
