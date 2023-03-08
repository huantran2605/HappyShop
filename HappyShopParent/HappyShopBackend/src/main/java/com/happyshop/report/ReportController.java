package com.happyshop.report;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.happyshop.common.entity.setting.Setting;
import com.happyshop.common.entity.setting.SettingCategory;
import com.happyshop.setting.SettingService;

@Controller
@RequestMapping("/report")
public class ReportController {
    
    @Autowired SettingService settingService;
    
    public void loadCurrencySetting( HttpServletRequest  request) {
        List<Setting> list = settingService.findByCategory(SettingCategory.CURRENCY);
        
        for (Setting setting : list) {
            request.setAttribute(setting.getKey(), setting.getValue());
        }     
    }
    @GetMapping("/reports")
    public String viewReportDateHome(HttpServletRequest  request) {
        loadCurrencySetting(request);
        
        return "report/reports";
    }
}
