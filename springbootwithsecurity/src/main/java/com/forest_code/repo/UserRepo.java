package com.forest_code.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.forest_code.model.User;

@Repository
public interface UserRepo extends JpaRepository<User, String> {

	public User findByUsername(String username);

}
