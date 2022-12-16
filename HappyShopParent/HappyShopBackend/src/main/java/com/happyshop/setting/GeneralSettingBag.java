package com.happyshop.setting;

import java.util.List;

import com.happyshop.common.entity.setting.Setting;
import com.happyshop.common.entity.setting.SettingBag;

public class GeneralSettingBag extends SettingBag {
    public GeneralSettingBag(List<Setting> listSetting) {
        super(listSetting);
    }
    
    public void updateCurrencySymbol(String value) {
        super.update("CURRENCY_SYMBOL", value);
    }
    
    public void updateSiteLogo(String value) {
        super.update("SITE_LOGO", value);
    }
    
}
