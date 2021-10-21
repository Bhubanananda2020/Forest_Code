package com.forest_code.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.forest_code.config.CustomUserDetails;
import com.forest_code.entity.JwtRequest;
import com.forest_code.entity.JwtResponse;
import com.forest_code.helper.JwtUtilHelper;
import com.forest_code.serviceimpl.CustomUserDetailsService;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class JwtController {

	@Autowired
	private CustomUserDetailsService customUserDetailsService;

	@Autowired
	private JwtUtilHelper jwtUtilHelper;

	@Autowired
	private AuthenticationManager authenticationManager;

	@RequestMapping(value = "/token", method = RequestMethod.POST)
	public ResponseEntity<JwtResponse> generateToken(@RequestBody JwtRequest jwtRequest) throws Exception {
		System.out.println(jwtRequest);
		JwtResponse jr = new JwtResponse();
		try {
			System.out.println("===============>");
			Authentication authenticate = this.authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(), jwtRequest.getPassword()));
			System.out.println(authenticate.getPrincipal().toString());
			
			UserDetails userDetails = this.customUserDetailsService.loadUserByUsername(jwtRequest.getUsername());
			String token = this.jwtUtilHelper.generateToken(userDetails);
			jr.setToken(token);
			
			
			return new ResponseEntity<JwtResponse>(jr, HttpStatus.OK);
			
			
			
			
		} catch (UsernameNotFoundException e) {
			e.printStackTrace();
			throw new Exception("UsernameNotFound");
		} catch (BadCredentialsException e) {
			e.printStackTrace();
			throw new Exception("Bad Credential");
		}
	}
}