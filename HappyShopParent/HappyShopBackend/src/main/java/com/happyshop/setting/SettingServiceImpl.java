package com.happyshop.setting;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.happyshop.common.entity.setting.Setting;
import com.happyshop.common.entity.setting.SettingCategory;

@Service
public class SettingServiceImpl implements SettingService{
    @Autowired
    SettingRepository settingRepository;

    public List<Setting> findAll() {
        return settingRepository.findAll();
    }

    public GeneralSettingBag getGeneralSettings() {
        List<Setting> settings = new ArrayList<>();
        
        List<Setting> generalSettings = settingRepository.findByCategory(SettingCategory.GENERAL);
        List<Setting> currencySettings = settingRepository.findByCategory(SettingCategory.CURRENCY);
        
        settings.addAll(generalSettings);   
        settings.addAll(currencySettings);
        
        return new GeneralSettingBag(settings);
    }

    public <S extends Setting> List<S> saveAll(Iterable<S> entities) {
        return settingRepository.saveAll(entities);
    }
    
    public List<Setting> getMailServerSetting() {      
        return settingRepository.findByCategory(SettingCategory.MAIL_SERVER);
    }
    
    public List<Setting> getMailTemplatesSetting() {      
        return settingRepository.findByCategory(SettingCategory.MAIL_TEMPLATES);
    }

    public List<Setting> findByCategory(SettingCategory category) {
        return settingRepository.findByCategory(category);
    }
    
    
}
