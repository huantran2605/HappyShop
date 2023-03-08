package com.happyshop.common.entity.product;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
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

import com.happyshop.common.Constants;
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
    
    @Column(name = "created_time")
    private Date createdTime;
    
    @Column(name="updated_time")
    private Date updatedTime;

    @Column(nullable = false)
    private boolean enable;
    
    @Column(name="in_stock")
    private boolean inStock;
    
    private float cost;
    private float price;
    
    @Column(name="discount_percent")
    private float discountPercent;
    
    @Column
    private float length;
    @Column
    private float width;
    @Column
    private float height;
    @Column
    private float weight;
    
    @Column(nullable = false)
    private int quantity;
    
    @Column(name="main_image", length = 128)
    private String mainImage;
    
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    
    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;
    
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProductImage> images = new HashSet<>();
   
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductDetail> details = new ArrayList<>();
//    
    
    
    @Transient
    public String getBrandName() {
        return this.brand.getName();
    }
    
    @Transient
    public String getCategoryName() {
        return this.category.getName();
    }
    
    public void addExtraImage(String name) {
        this.images.add(new ProductImage(name, this));
    }
    
    public void addDetail(String name, String value) {
        this.details.add(new ProductDetail(name, value, this));
    }
    
    public void addDetail(Integer id,String name, String value) {
        this.details.add(new ProductDetail(id, name, value, this));
    }  
    
    @Transient
    public String getProductMainImagePath () {
        return Constants.S3_BASE_URI+ "/product-images/"+ this.id+"/" + this.mainImage; 
    }

    public boolean containsImageName(String fileName) {
        Iterator<ProductImage> image = images.iterator();  
        while(image.hasNext()) {
            ProductImage p = image.next();
            if(p.getName().equals(fileName)) {
                return true;
            }
        }
        return false;
    }
    
    @Transient
    public String shortName() {
        if(this.name.length() > 70) {
            this.name = this.name.substring(0, 70) + "...";
        }
        return this.name;
    }
    @Transient
    public float getDiscountPrice() {
        float discountprice = price - (price * discountPercent) / 100;
        return discountprice;
    }

    public Product(Integer id) {
        this.id = id;
    }

    public Product(String name) {
        this.name = name;
    }

    
}
