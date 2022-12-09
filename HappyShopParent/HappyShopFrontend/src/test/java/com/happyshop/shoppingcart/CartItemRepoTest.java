package com.happyshop.shoppingcart;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.happyshop.common.entity.CartItem;
import com.happyshop.common.entity.Customer;
import com.happyshop.common.entity.product.Product;
import com.happyshop.shoppingCart.CartItemRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CartItemRepoTest {
    @Autowired
    CartItemRepository cartItemRepo;
    @Autowired
    EntityManager entityManager;
    
//    @Test
//    public void saveCartItem() {
//        Customer customer = entityManager.find(Customer.class, 5);
//        Product product = entityManager.find(Product.class, 5);
//        CartItem cartItem = new CartItem(customer, product, 3);
//        CartItem savedCartItem = cartItemRepo.save(cartItem);
//        assertThat(savedCartItem.getId()).isEqualTo(1);
//    }
    
//    @Test
//    public void findByCustomerTest() {
//        Customer customer = entityManager.find(Customer.class, 1);
//        CartItem cartItem = cartItemRepo.findByCustomer(customer);
//        
//        assertThat(cartItem.getCustomer().getId()).isEqualTo(customer.getId());
//        System.out.println(cartItem.getId());
//        assertThat(cartItem.getId()).isGreaterThan(0);
//    }
    
//    @Test
//    public void findByCustomerAndProduct () {
//        Customer customer = entityManager.find(Customer.class, 3);
//        CartItem cartItem = cartItemRepo.findByCustomerAndProduct(customer, 5);
//        assertThat(cartItem.getCustomer().getId()).isEqualTo(customer.getId());
//        assertThat(cartItem.getId()).isEqualTo(2);
//        System.out.println(cartItem);
//    }
    
//    @Test
//    public void updateQuantityTest () {
//        cartItemRepo.updateQuantity(12, 3, 9);
//        Optional<CartItem> cartItem = cartItemRepo.findById(4);
//        assertThat(cartItem.get().getQuantity()).isEqualTo(12);
//    }
    
    @Test
    public void deleteByCustomerAndProduct () {
        cartItemRepo.deleteByCustomerAndProduct(1, 1);
        Optional<CartItem> cartItem = cartItemRepo.findById(1);
        assertThat(cartItem.get()).isNull();
    }
}
