package com.seafoodshop;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
	@GetMapping("")
	private String viewHomePage() {
		return "index";
	}
	@GetMapping("test")
	private String viewHomePa() {
		return "test";
	}
}
