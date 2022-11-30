package com.happyshop.setting;

import java.util.List;

import com.happyshop.common.entity.Setting;
import com.happyshop.common.entity.SettingCategory;

public interface SettingService {
    List<Setting> findAll();
    
    <S extends Setting> List<S> saveAll(Iterable<S> entities);
        
    GeneralSettingBag getGeneralSettings();
}
