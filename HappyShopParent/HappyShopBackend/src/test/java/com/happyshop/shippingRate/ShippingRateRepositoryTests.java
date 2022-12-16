package com.happyshop.shippingRate;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.happyshop.common.entity.ShippingRate;
import com.happyshop.common.entity.product.Product;
import com.happyshop.common.entity.setting.Country;
import com.happyshop.product.ProductRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class ShippingRateRepositoryTests {
    @Autowired
    private ShippingRateRepository repo;
    @Autowired
    EntityManager entityManager;
    
    @Test
    public void testAddShippingRate() {
        Country country = entityManager.find(Country.class, 1);
       ShippingRate s1 = new ShippingRate(2, 3, true, country, "nam dinh");
       assertThat(s1.getDays()).isEqualTo(3);
    }

    
    
}
