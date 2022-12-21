package com.happyshop.setting;

import java.util.List;

import com.happyshop.common.entity.setting.Setting;
import com.happyshop.common.entity.setting.SettingCategory;

public interface SettingService {
    List<Setting> findAll();
    
    <S extends Setting> List<S> saveAll(Iterable<S> entities);
        
    GeneralSettingBag getGeneralSettings();
    
    List<Setting> getMailServerSetting();
    
    List<Setting> getMailTemplatesSetting();
    
    List<Setting> findByCategory(SettingCategory category);

    List<Setting> getPaymentSetting();
}
