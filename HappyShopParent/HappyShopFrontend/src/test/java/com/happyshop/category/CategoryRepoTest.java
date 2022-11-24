package com.happyshop.category;

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

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CategoryRepoTest {
    @Autowired
    CategoryRepository categoryRepo;
    
    @Test
    public void listNoChildrenCate() {
        
        List<Category> listAllEnabled = categoryRepo.findAllEnabled();
        for (Category category : listAllEnabled) {
            if(category.getChildren() == null || category.getChildren().size() == 0) {
                System.out.println(category.getName() + " " + "("+category.isEnable()+")");
            }
        } 
    }
    
    @Test
    public void testFindByAliasEnabled() {
        String alias = "camera";
        Category cat =  categoryRepo.findByAliasEnabled(alias);
        assertThat(cat).isNotNull();
    }
}
