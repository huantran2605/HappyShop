package com.happyshop.setting.country;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.happyshop.common.entity.Country;

@RestController
public class CountryRestController {
    @Autowired
    CountryService countryService;
    
    @GetMapping("/countries/listAll")
    public List<Country> getAll(){
        return countryService.findAllByOrderByNameAsc();
    }
    
    @PostMapping("/countries/save")
    public String saveCountry(@RequestBody Country country){
        Country savedCountry = countryService.save(country);
        return String.valueOf(savedCountry.getId());
    }
    
    @DeleteMapping("/countries/delete/{id}")
    public void deleteCountry(@PathVariable("id") Integer id){
        countryService.deleteById(id);
    }
    
}
