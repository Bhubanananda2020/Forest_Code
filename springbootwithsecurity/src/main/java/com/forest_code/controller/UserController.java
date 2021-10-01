package com.forest_code.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.forest_code.model.User;
import com.forest_code.services.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserService userService;

	// get all user
	@GetMapping("/")
	public List<User> getAllUser() {
		return this.userService.getAllUser();
	}

	// get Single user
	@GetMapping("/{uname}")
	public User getSingleUser(@PathVariable("uname") String uname) {
		return this.userService.getUser(uname);
	}

	// Add user
	@PostMapping("/")
	public User addNewUser(@RequestBody User u) {
		return this.userService.addUser(u);
	}

}
