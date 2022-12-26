package com.happyshop.setting;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.happyshop.common.entity.setting.Setting;
import com.happyshop.common.entity.setting.SettingCategory;

public interface SettingRepository extends JpaRepository<Setting, String>{
    
    public List<Setting> findByCategory(SettingCategory category);
    
    @Query("select p from Setting p where p.category = ?1 or p.category = ?2")
    public List<Setting> findBySomeCategory(SettingCategory catOne, SettingCategory catTwo);
    
    public Setting findByKey(String key);
}
