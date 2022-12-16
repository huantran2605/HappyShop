package com.happyshop.setting.state;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.happyshop.common.entity.setting.Country;
import com.happyshop.common.entity.setting.State;

@Service
public class StateServiceImpl implements StateService {
    @Autowired
    StateRepository stateRepository;

    public List<State> findByCountryOrderByNameAsc(Country country) {
        return stateRepository.findByCountryOrderByNameAsc(country);
    }

    public <S extends State> S save(S entity) {
        return stateRepository.save(entity);
    }

    public void deleteById(Integer id) {
        stateRepository.deleteById(id);
    }

    

    
}
