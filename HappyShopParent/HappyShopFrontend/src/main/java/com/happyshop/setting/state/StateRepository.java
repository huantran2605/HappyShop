package com.happyshop.setting.state;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.happyshop.common.entity.setting.Country;
import com.happyshop.common.entity.setting.State;

@Repository
public interface StateRepository extends JpaRepository<State, Integer> {
    public List<State> findByCountryOrderByNameAsc(Country country);
}
