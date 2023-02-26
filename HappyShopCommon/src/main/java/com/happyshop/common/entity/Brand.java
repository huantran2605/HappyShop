package com.happyshop.common.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.happyshop.common.Constants;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table (name="brands")
public class Brand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false, length = 45, unique = true)
    private String name;
    @Column(length = 128)
    private String logo;
    
    @ManyToMany
    @JoinTable(
            name = "brands_categories",
            joinColumns = @JoinColumn(name="brand_id"),
            inverseJoinColumns = @JoinColumn(name="category_id")
            )   
    private Set<Category> categories = new HashSet<>();
    @Transient
    public String getBrandLogoPath () {
        return Constants.S3_BASE_URI+ "/brand-logos/"+ this.id+"/" + this.logo; 
    }
    
}
