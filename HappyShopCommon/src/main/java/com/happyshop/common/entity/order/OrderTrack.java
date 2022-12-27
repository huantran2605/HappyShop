package com.happyshop.common.entity.order;


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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name="order_track")
public class OrderTrack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    private Date updatedTime;   
    
    @Column(length = 45, nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    
    @Column(length = 256)  
    private String note;
    
    @ManyToOne
    @JoinColumn(name="order_id")
    private Order order;
}
