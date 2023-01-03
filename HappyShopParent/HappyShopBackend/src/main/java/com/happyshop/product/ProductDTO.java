package com.happyshop.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class ProductDTO {
    private String name;
    private String shortName;
    private String imagePath;
    private float price;
    private float cost;
    private int quantity;
    
}
