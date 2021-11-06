package com.forest_code.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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
		ArrayList<Object> lstue = new ArrayList<Object>();

		HttpHeaders responseHeaders = new HttpHeaders();
		try {
			System.out.println("===============>");
			Authentication authenticate = this.authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(), jwtRequest.getPassword()));
			System.out.println(authenticate.getPrincipal().toString());

			UserDetails userDetails = this.customUserDetailsService.loadUserByUsername(jwtRequest.getUsername());
			
			lstue.add(userDetails);
			jr.setToken(this.jwtUtilHelper.generateToken(userDetails));

			responseHeaders.add("authorization", this.jwtUtilHelper.generateToken(userDetails));
			responseHeaders.add("Access-Control-Expose-Headers", "authorization");

			jr.setObj(null);
			jr.setStatus(HttpStatus.OK);
			jr.setMessage("Login Successfully");
			return ResponseEntity.ok().headers(responseHeaders).body(jr);

		} catch (UsernameNotFoundException e) {
			e.printStackTrace();
			throw new Exception("UsernameNotFound");
		} catch (BadCredentialsException e) {
			e.printStackTrace();
			throw new Exception("Bad Credential");
		}
	}
}