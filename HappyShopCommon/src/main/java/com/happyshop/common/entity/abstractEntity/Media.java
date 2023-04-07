package com.happyshop.common.entity.abstractEntity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Media {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;
    
    @Column(nullable = false)
    protected String name;
    
    @Column(length = 200)
    protected String description;
    
    @Column(nullable = false, name = "created_time")
    protected Date createdTime;

    public Media(String name, String description, Date createdTime) {
        super();
        this.name = name;
        this.description = description;
        this.createdTime = createdTime;
    }
    
    
    
}
