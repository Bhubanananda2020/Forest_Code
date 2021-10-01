package com.forest_code.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.forest_code.model.User;
import com.forest_code.repo.UserRepo;

@Service
public class UserService {

	@Autowired
	private UserRepo userRepo;

	// get all user
	public List<User> getAllUser() {
		return this.userRepo.findAll();
	}

	// Get single user
	public User getUser(String uname) {
		return this.userRepo.findAll().stream().filter((user) -> user.getUsername().equals(uname)).findAny()
				.orElse(null);
	}

	// add new user
	public User addUser(User u) {
		this.userRepo.save(u);
		return u;
	}

}
