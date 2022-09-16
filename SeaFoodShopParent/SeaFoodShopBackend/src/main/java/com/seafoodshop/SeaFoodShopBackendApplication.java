package com.seafoodshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@EntityScan({"com.seafoodshop.common.entity", "com.seafoodshop.admin.user"})
public class SeaFoodShopBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(SeaFoodShopBackendApplication.class, args);
	}

}
