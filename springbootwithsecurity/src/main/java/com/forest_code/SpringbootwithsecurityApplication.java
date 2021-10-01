package com.forest_code;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.forest_code.model.User;
import com.forest_code.repo.UserRepo;

@SpringBootApplication
public class SpringbootwithsecurityApplication implements CommandLineRunner {

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(SpringbootwithsecurityApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		User user1 = new User();
		user1.setUsername("Honey1");
		user1.setEmail("Honey1@test.com");
		user1.setPassword(this.bCryptPasswordEncoder.encode("web1"));
		user1.setRole("ROLE_NORMAL");
		this.userRepo.save(user1);

		User user2 = new User();
		user2.setUsername("Honey2");
		user2.setEmail("Honey2@test.com");
		user2.setPassword(this.bCryptPasswordEncoder.encode("web2"));
		user2.setRole("ROLE_ADMIN");
		this.userRepo.save(user2);

	}

}
