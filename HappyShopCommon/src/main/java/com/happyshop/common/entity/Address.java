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
@Table(name = "addresses")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "first_name", nullable = false, length = 45)
    private String firstName;
    
    @Column(name = "last_name", nullable = false, length = 45)
    private String lastName;
    
    @Column(name = "phone_number", nullable = false, length = 15)
    private String phoneNumber;
    
    @Column(name = "address_line_1", nullable = false, length = 64)
    private String addressLine1;
    
    @Column(name = "address_line_2", length = 64)
    private String addressLine2;
    
    @Column(nullable = false, length = 45)
    private String city;
    
    @Column(nullable = false, length = 45)
    private String state;
    
    @Column(name = "postal_code", nullable = false, length = 10)
    private String postalCode;
    
    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;
    
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
    
    @Column(name = "default_address")
    private boolean defaultForShipping;

    @Override
    public String toString() {
        String address = "Full Name: " + firstName;
        
        if(!lastName.isEmpty() && lastName != null) address += " " + lastName ;
        if(!addressLine1.isEmpty() && addressLine1 != null) address += ". Address: " + addressLine1;
        if(addressLine2 != null && !addressLine2.isEmpty() ) address += ", " + addressLine2;
        if(!city.isEmpty() && city != null) address += ", " + city;
        if(!state.isEmpty() && state != null) address += ", " + state;
        
        address += ", " + country.getName();
        if(!postalCode.isEmpty() && postalCode != null) address += ". Postal Code: " + postalCode;
        if(!phoneNumber.isEmpty() && phoneNumber != null) address += ". Phone number: " + phoneNumber;
           
        return address;
    }

    
    
}
