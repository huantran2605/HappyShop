package com.happyshop.setting;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.happyshop.common.entity.Setting;
import com.happyshop.common.entity.SettingCategory;

@Service
public class SettingServiceImpl implements SettingService{
    @Autowired
    SettingRepository settingRepository;

    public List<Setting> getGeneralSettings() {
        return settingRepository.findBySomeCategory(SettingCategory.GENERAL, SettingCategory.CURRENCY);
    }

   
  
    
    
    
}
