package com.happyshop.common.entity;



import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@Data
@NoArgsConstructor
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(nullable = false, unique = true, length = 45)
    private String email;

    @Column(nullable = false, length = 64)
    private String password;
    
    @Column(name = "first_name", nullable = false, length = 45)
    private String firstName;
    
    @Column(name = "last_name", nullable = false, length = 45)
    private String lastName;
    
    @Column(name = "phone_number", nullable = false, length = 15)
    private String phoneNumber;
    
    @Column(nullable = false, length = 64)
    private String addressLine1;
    
    @Column(name = "address_line_2", length = 64)
    private String addressLine2;
    

    @Column(nullable = false, length = 45)
    private String city;
    
    @Column(nullable = false, length = 45)
    private String state;
    
    @Column(name = "postal_code", nullable = false, length = 10)
    private String postalCode;  
    
    @Column(name = "verification_code", length = 64)
    private String verificationCode;    
    
    private boolean enabled;
    
    @Column(name = "created_time")
    private Date createdTime;
    
    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "authentication_type", length = 10)
    private AuthenticationType authenticationType;
    
    @Column(name="reset_password_token", length = 30)
    private String resetPasswordToken;
    
    @Transient
    public String getFullName() {
        return this.getFirstName() + " " + this.getLastName();
    }


    
    
}
