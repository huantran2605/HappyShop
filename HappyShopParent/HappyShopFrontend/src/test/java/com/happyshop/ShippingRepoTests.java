package com.happyshop;

import static org.assertj.core.api.Assertions.assertThat;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.happyshop.common.entity.ShippingRate;
import com.happyshop.common.entity.setting.Country;
import com.happyshop.shipping.ShippingRateRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class ShippingRepoTests {
    @Autowired
    ShippingRateRepository repo;
    
    @Autowired
    EntityManager em;
    
    @Test
    public void getShippingRate() {
        Country country = em.find(Country.class, 234);
        
        
        ShippingRate sr = repo.findByCountryAndState(country, "New York");
        
        System.out.println(sr);
        assertThat(sr.getId()).isEqualTo(2);
    }
}
