package com.happyshop;  

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import org.springframework.test.annotation.Rollback;

import com.happyshop.common.entity.setting.Currency;
import com.happyshop.common.entity.setting.Setting;
import com.happyshop.common.entity.setting.SettingCategory;
import com.happyshop.setting.SettingRepository;




@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class SettingRepositoryTests {
    @Autowired
    private SettingRepository repo;
    

    
    @Test
    public void testFindBySomeCategory() {
      List<Setting> list = repo.findBySomeCategory(SettingCategory.GENERAL, SettingCategory.CURRENCY);
      for (Setting setting : list) {
        System.out.println(setting);
      }
        
    }

    
}
