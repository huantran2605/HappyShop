package com.happyshop.setting.state;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.happyshop.common.entity.Country;
import com.happyshop.common.entity.State;
import com.happyshop.setting.country.CountryService;

@RestController
public class StateRestController {
    @Autowired
    StateService stateService;
    @Autowired
    CountryService countryService;
    
    @GetMapping("/states/list_by_country/{idCountry}")
    public List<StateDTO> getAllByCountryOrderByAsc(@PathVariable("idCountry") int idCountry){
        Optional<Country> country = countryService.findById(idCountry);
        List<State> listState =   stateService.findByCountryOrderByNameAsc(country.get());
        List<StateDTO> result = new ArrayList<>();
        for (State state : listState) {
            result.add(new StateDTO(state.getId(), state.getName()));
        }
        return result;
    }
    
    @PostMapping("/states/save")
    public String saveState(@RequestBody State state){
        State savedCountry = stateService.save(state);
        return String.valueOf(savedCountry.getId());
    }
    
    @DeleteMapping("/states/delete/{id}")
    public void deleteCountry(@PathVariable("id") Integer id){
        stateService.deleteById(id);
    }
    
}
