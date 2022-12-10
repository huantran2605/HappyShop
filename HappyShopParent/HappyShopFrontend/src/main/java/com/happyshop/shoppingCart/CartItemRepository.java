package com.happyshop.shoppingCart;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.happyshop.common.entity.CartItem;
import com.happyshop.common.entity.Customer;
import com.happyshop.common.entity.product.Product;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
    
    public List<CartItem> findByCustomer(Customer customer);
    
    @Query("select c from CartItem c where c.customer = ?1 and c.product.id = ?2")
    public CartItem findByCustomerAndProduct(Customer customer, Integer productId);
    
    @Query("UPDATE CartItem c SET c.quantity = ?1 WHERE c.customer = ?2 and c.product.id = ?3")
    @Modifying
    @Transactional
    public void updateQuantity(Integer quantity, Customer customer, Integer productId);
    
    @Query("DELETE FROM CartItem c WHERE c.customer.id = ?1 and c.product.id = ?2")
    @Modifying
    public void deleteByCustomerAndProduct(Integer cutomerId, Integer productId);
    
}
