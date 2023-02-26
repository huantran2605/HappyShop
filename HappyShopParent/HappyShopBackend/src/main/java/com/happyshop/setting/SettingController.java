package com.happyshop.setting;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.happyshop.FileUploadUtil;
import com.happyshop.admin.AmazonS3Util;
import com.happyshop.common.Constants;
import com.happyshop.common.entity.setting.Currency;
import com.happyshop.common.entity.setting.Setting;



@Controller
public class SettingController {
    @Autowired
    SettingService settingService;
    
    @Autowired
    CurrencyService currencyService;
    @GetMapping("/setting")
    private String listAll(Model model) {
        List<Setting> listSetting = settingService.findAll();
        List<Currency> listCurrency = currencyService.findAll();
        
        model.addAttribute("listCurrency", listCurrency);
        for (Setting setting : listSetting) {
            model.addAttribute(setting.getKey(), setting.getValue());
        }
        
        model.addAttribute("S3_BASE_URI", Constants.S3_BASE_URI);
        
        return "setting/settings";
    }
    
    @PostMapping("/setting/save_general")
    private String save_generalSetting(Model model,  
            HttpServletRequest request, RedirectAttributes ra,
            @RequestParam("logoFile") MultipartFile multipartFile 
            ) throws IOException{
        GeneralSettingBag settingBag = settingService.getGeneralSettings();
        saveSiteLogo(multipartFile, settingBag);
        saveCurrencySymbol(request, settingBag);
        updateSettingValuesFromForm(request, settingBag.list());
        
        
        ra.addFlashAttribute("message", "Updated General Setting successfully!");
        return "redirect:/setting";
    }
    
    private void saveSiteLogo(MultipartFile multipartFile, GeneralSettingBag settings ) throws IOException {
        if (!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            settings.updateSiteLogo(fileName);
            String uploadDir = "site-logo";
            
            AmazonS3Util.removeFolder(uploadDir);
            AmazonS3Util.uploadFile(uploadDir, fileName, multipartFile.getInputStream());
        }
    }
    
    
    private void saveCurrencySymbol(HttpServletRequest request, GeneralSettingBag settingBag){
        Integer currencyId = Integer.parseInt(request.getParameter("CURRENCY_ID"));

        Optional<Currency> findByIdResult = currencyService.findById(currencyId);

        if (findByIdResult.isPresent()) {
            Currency currency = findByIdResult.get();
            settingBag.updateCurrencySymbol(currency.getSymbol());
        }
    }
    
    private void updateSettingValuesFromForm(HttpServletRequest request, List<Setting> listSettings) {
        for (Setting setting : listSettings) {
            String value = request.getParameter(setting.getKey());
            if (value != null) {
                setting.setValue(value);
            }
        }
        
        settingService.saveAll(listSettings);
    }
    
    @PostMapping("/setting/save_mail_server")
    private String saveMailServerSetting(
            HttpServletRequest request, RedirectAttributes ra
            ) throws IOException{
        List<Setting> list = settingService.getMailServerSetting();
        
        updateSettingValuesFromForm(request, list);
           
        ra.addFlashAttribute("message", "Updated Mail Server Setting successfully!");
        return "redirect:/setting";
    }
    
    @PostMapping("/setting/save_mail_templates")  
    private String saveMailTemplatesSetting(
            HttpServletRequest request, RedirectAttributes ra
            ) throws IOException{
        List<Setting> list = settingService.getMailTemplatesSetting();
        
        updateSettingValuesFromForm(request, list);
           
        ra.addFlashAttribute("message", "Updated Mail Templates Setting successfully!");
        return "redirect:/setting";
    }
    
    @PostMapping("/setting/save_payment")  
    private String savePaymentSetting(
            HttpServletRequest request, RedirectAttributes ra
            ) throws IOException{
        List<Setting> list = settingService.getPaymentSetting();
        
        updateSettingValuesFromForm(request, list);
           
        ra.addFlashAttribute("message", "Updated Payment Setting successfully!");
        return "redirect:/setting";
    }
}
