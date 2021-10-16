package com.forest_code.jwtautho.springwithjwtautho.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Home {

	@RequestMapping("/welcome")
	public String welcom() {
		String text = "this is private page";
		text += " this page is not allowed to unauthenticated user";
		return text;
	}

	@RequestMapping("/getuser")
	public String getuser() {
		return "{\"name\":\"ForestCode\"}";
	}
}
