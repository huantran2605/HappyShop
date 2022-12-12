package com.happyshop.common.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "shipping_rates")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ShippingRate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private float rate;
    private int days;
    
    @Column(name = "cod_supported")
    private boolean codSupported;
    
    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;  
    
    @Column(nullable = false, length = 45)
    private String state;

    public ShippingRate(float rate, int days, boolean codSupported, Country country, String state) {
        this.rate = rate;
        this.days = days;
        this.codSupported = codSupported;
        this.country = country;
        this.state = state;
    }
    
    
}
