package com.happyshop.setting.country;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.happyshop.common.entity.setting.Country;

@Service
public class CountryServiceImpl implements CountryService {
    @Autowired
    CountryRepository countryRepository;

    public List<Country> findAllByOrderByNameAsc() {
        return countryRepository.findAllByOrderByNameAsc();
    }

    public <S extends Country> S save(S entity) {
        return countryRepository.save(entity);
    }

    public void deleteById(Integer id) {
        countryRepository.deleteById(id);
    }

    public Optional<Country> findById(Integer id) {
        return countryRepository.findById(id);
    }

    
    
    
    
    
}
