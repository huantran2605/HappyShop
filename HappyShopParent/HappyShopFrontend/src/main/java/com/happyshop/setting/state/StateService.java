package com.happyshop.setting.state;

import java.util.List;

import com.happyshop.common.entity.setting.Country;
import com.happyshop.common.entity.setting.State;

public interface StateService {
    List<State> findByCountryOrderByNameAsc(Country country);
    <S extends State> S save(S entity);
    void deleteById(Integer id);
}
