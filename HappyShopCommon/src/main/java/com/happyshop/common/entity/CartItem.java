package com.happyshop.common.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.happyshop.common.entity.product.Product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@Data
@NoArgsConstructor
@Table(name="cart_items")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name="customer_id")
    private Customer customer;
    
    @ManyToOne
    @JoinColumn(name="product_id")
    private Product product;
    
    private int quantity;

    public CartItem(Customer customer, Product product, int quantity) {
        super();
        this.customer = customer;
        this.product = product;
        this.quantity = quantity;
    }
    
    @Transient
    public float getSubTotal() {
        return this.product.getDiscountPrice() * this.quantity;
    }
    
    
}
