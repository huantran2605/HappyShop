package com.happyshop.common.entity.setting;

import java.util.Set;

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
@Table(name = "states")
@AllArgsConstructor
@Data
@NoArgsConstructor
public class State {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(nullable = false, length = 45)
    private String name;
    
    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;

  
    @Override
    public String toString() {
        return "State [id=" + id + ", name=" + name + "]";
    }


    public State(String name, Country country) {
        this.name = name;
        this.country = country;
    }


    public State(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
    
    
}
