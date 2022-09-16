package com.seafoodshop.common.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="roles")
public class Role {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(length = 40, nullable = false, unique = true)
	private String name;
	
	@Column(length = 150, nullable = false)
	private String description;

	public Role(String name) {
		this.name = name;
	}

	public Role(String string, String string2) {
		this.name=  string;
		this.description=  string2;
		
	}

	@Override
	public String toString() {
		return this.name;
	}
	
}