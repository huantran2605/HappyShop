package com.happyshop.setting;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.happyshop.common.entity.setting.Currency;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Integer>{

}
