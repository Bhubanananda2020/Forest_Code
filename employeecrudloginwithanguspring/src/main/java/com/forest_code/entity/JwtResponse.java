package com.forest_code.entity;

public class JwtResponse {
	String token;

	public JwtResponse() {
		super();
	}

	public JwtResponse(String token) {
		super();
		this.token = token;
	}

	@Override
	public String toString() {
		return "JwtResponse [token=" + token + "]";
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	
	

}
