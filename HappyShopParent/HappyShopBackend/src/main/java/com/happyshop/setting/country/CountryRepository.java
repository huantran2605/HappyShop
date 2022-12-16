package com.happyshop.setting.country;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.happyshop.common.entity.setting.Country;


@Repository
public interface CountryRepository extends JpaRepository<Country, Integer> {
    public List<Country> findAllByOrderByNameAsc();
}
