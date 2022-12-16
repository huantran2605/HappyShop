package com.happyshop.setting;

import java.util.List;
import java.util.Optional;

import com.happyshop.common.entity.setting.Currency;


public interface CurrencyService {
    List<Currency> findAll();

    Optional<Currency> findById(Integer currencyId);
}
