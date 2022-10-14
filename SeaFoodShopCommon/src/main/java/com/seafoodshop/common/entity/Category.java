package com.seafoodshop.common.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(length = 128, nullable = false, unique = true)
    private String name;
    @Column(length = 64, nullable = false, unique = true)
    private String alias;
    @Column(length = 128)
    private String image;
    @Column(nullable = false)
    private boolean enable;
    
    @OneToOne
    @JoinColumn(name = "parent_id")
    private Category parent;
    @OneToMany(mappedBy = "parent")
    private Set<Category> children = new HashSet<>();
    
    
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getAlias() {
        return alias;
    }
    public void setAlias(String alias) {
        this.alias = alias;
    }
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public boolean isEnable() {
        return enable;
    }
    public void setEnable(boolean enable) {
        this.enable = enable;
    }
    public Category getParent() {
      return parent;               
    }
    public void setParent(Category parent) {
        this.parent = parent;
    }
    public Set<Category> getChildren() {
        return children;
    }
    public void setChildren(Set<Category> children) {
        this.children = children;
    }
    
    public Category(String name) {
        this.name = name;
        this.alias = name;
        this.enable = true;
        this.image = null;
    }
    public Category () {
        
    }
    
    
    public Category(Integer id, String name) {
        this.id = id;
        this.name = name;
        this.alias = name;
        this.enable = true;
    }
    public Category(Integer id, String name, String alias, String image, boolean enable, Category parent,
            Set<Category> children) {
        this.id = id;
        this.name = name;
        this.alias = alias;
        this.image = image;
        this.enable = enable;
        this.parent = parent;
        this.children = children;
    }
    
    @Transient
    public String getCategoryImagePath () {
        return "/category-images/"+ this.id+"/" + this.image; 
    }
    
    @Transient
    public String getCategoryNameParent() {
        if(this.parent != null) {
            String name =  this.getParent().getName();        
            return name;                 
        }
        return "No";
    }
    
    @Transient
    public int getNumberchild() {
       int num = this.children.size();       
       return num;       
    }
    
}
