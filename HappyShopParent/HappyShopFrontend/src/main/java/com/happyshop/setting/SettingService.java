package com.happyshop.setting;

import java.util.List;

import com.happyshop.common.entity.setting.Setting;
import com.happyshop.common.entity.setting.SettingCategory;

public interface SettingService {
    List<Setting> getGeneralSettings();
    
    EmailSettingBag getEmailSetting();
}
