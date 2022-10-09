package com.seafoodshop;


import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer{

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		Path fileDir1 = Paths.get("users-photo");
		String filePath1 = fileDir1.toFile().getAbsolutePath();
		
		Path fileDir2 = Paths.get("category-images");
        String filePath2 = fileDir2.toFile().getAbsolutePath();
        
		registry.addResourceHandler("/users-photo/**")
			.addResourceLocations("file:/" + filePath1+"/");
		
		registry.addResourceHandler("/category-images/**")
        .addResourceLocations("file:/" + filePath2+"/");
		
	}
	

}
