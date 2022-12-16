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

        
    public List<Setting> getGeneralSettings() {
        return settingRepository.findBySomeCategory(SettingCategory.GENERAL, SettingCategory.CURRENCY);
    }

    public EmailSettingBag getEmailSetting(){
        List<Setting> list = new ArrayList<>();
        list = settingRepository.findByCategory(SettingCategory.MAIL_SERVER);
        for (Setting setting : settingRepository.findByCategory(SettingCategory.MAIL_TEMPLATES)) {
            list.add(setting);
        }
        return new EmailSettingBag(list);
    }
  
    
    
    
}
