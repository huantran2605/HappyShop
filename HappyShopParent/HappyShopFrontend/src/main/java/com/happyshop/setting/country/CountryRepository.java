package com.happyshop.setting.country;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.happyshop.common.entity.Country;


@Repository
public interface CountryRepository extends JpaRepository<Country, Integer> {
    public List<Country> findAllByOrderByNameAsc();
    
//    @Query("select c from Country c where c.code = ?1")
    public Country findByCode(String code);
}
