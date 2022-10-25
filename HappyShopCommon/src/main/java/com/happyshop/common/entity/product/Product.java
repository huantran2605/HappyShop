package com.happyshop.common.entity.product;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.happyshop.common.entity.Brand;
import com.happyshop.common.entity.Category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(unique = true, length = 255, nullable = false)
    private String name;
    
    @Column(unique = true, length = 255, nullable = false)
    private String alias;
    
    @Column(length = 500, nullable = false, name = "short_description")
    private String shortDescription;
    
    @Column(length = 5000, nullable = false, name = "full_description")
    private String fullDescription;
    
    @Column(updatable = false, length = 255, nullable = false, name = "created_time")
    private Date createdTime;
    
    @Column(name="updated_time")
    private String updatedTime;
    
    private boolean enable;
    
    @Column(name="in_stock")
    private boolean inStock;
    
    private float cost;
    private float price;
    
    @Column(name="discount_percent")
    private float discountPercent;
    
    
    private float length;
    private float width;
    private float height;
    private float weight;
    
    @Column(name="main_image")
    private String mainImage;
    
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    
    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;
    
//    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
//    private Set<ProductImage> images = new HashSet<>();
//    
//    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<ProductDetail> details = new ArrayList<>();
//    
 
    
    @Transient
    public String getBrandName() {
        return this.brand.getName();
    }
    
    @Transient
    public String getCategoryName() {
        return this.category.getName();
    }
    
    

    
    
    
}
