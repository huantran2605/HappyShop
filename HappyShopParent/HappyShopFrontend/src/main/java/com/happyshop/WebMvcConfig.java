package com.happyshop;


import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer{

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
	    addModuleResourceHandlers("../category-images", registry);
	    addModuleResourceHandlers("../brand-logos", registry);
	    addModuleResourceHandlers("../product-images", registry);
	    

	}
	
	public void addModuleResourceHandlers (String pattern, ResourceHandlerRegistry registry ) {
	    Path fileDir = Paths.get(pattern);
        String filePath = fileDir.toFile().getAbsolutePath();
        
        String logicalPath = pattern.replace("../", "");
        registry.addResourceHandler("/"+ logicalPath + "/**")
            .addResourceLocations("file:/" + filePath+"/");
	}
	

}
