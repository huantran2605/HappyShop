package com.happyshop;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.happyshop.common.entity.Category;
import com.happyshop.common.entity.product.Product;
import com.happyshop.product.ProductRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class ProductRepoTest {
    @Autowired
    ProductRepository productRepo;
    
    @Test
    public void updateQuantityTest() {
        productRepo.updateQuantity(44, 1);
        assertThat(productRepo.findById(1).get().getQuantity()).isEqualTo(44);
    }
    
}
