package com.happyshop.admin.product;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.happyshop.common.entity.product.Product;
import com.happyshop.product.ProductRepository;



@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class ProductRepositoryTests {
    @Autowired
    private ProductRepository repo;
    
    @Test
    public void testDeleteProduct() {
        Integer id = 16;
        repo.deleteById(id);

        Optional<Product> result = repo.findById(id);
        
        assertThat(!result.isPresent());
    }

    
    @Test
    public void saveProductwithImages() {
        Product product = repo.getReferenceById(2);
        
        product.setMainImage("main-image.png");
        product.addExtraImage("extra-image-1");;
        product.addExtraImage("extra-image-2");;
        Product productSaved = repo.save(product);
        assertThat(productSaved.getImages().size()).isEqualTo(2);
    }
    @Test
    public void addDetaiProduct() {
        Product product = repo.getReferenceById(8);
        
        product.addDetail("d1","v1");
        product.addDetail("d2","v2");
        product.addDetail("d3","v3");
        
        Product productSaved = repo.save(product);
        assertThat(productSaved.getDetails().size()).isEqualTo(3);
        
    }
}
