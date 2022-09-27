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
		Path fileDir = Paths.get("users-photo");
		String filePath = fileDir.toFile().getAbsolutePath();
		
		registry.addResourceHandler("/users-photo/**")
			.addResourceLocations("file:/" + filePath+"/");
	}
	

}
