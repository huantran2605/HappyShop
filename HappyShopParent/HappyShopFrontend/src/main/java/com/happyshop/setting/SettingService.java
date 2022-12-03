package com.happyshop.setting;

import java.util.List;

import com.happyshop.common.entity.Setting;
import com.happyshop.common.entity.SettingCategory;

public interface SettingService {
    List<Setting> getGeneralSettings();
    
    EmailSettingBag getEmailSetting();
}
