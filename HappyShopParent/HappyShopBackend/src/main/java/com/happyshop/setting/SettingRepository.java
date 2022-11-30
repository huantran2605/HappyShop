package com.happyshop.setting;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.happyshop.common.entity.Setting;
import com.happyshop.common.entity.SettingCategory;

public interface SettingRepository extends JpaRepository<Setting, String>{
    
    public List<Setting> findByCategory(SettingCategory category);
    
}
