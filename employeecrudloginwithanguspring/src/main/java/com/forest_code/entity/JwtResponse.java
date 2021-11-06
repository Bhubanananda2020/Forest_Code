package com.forest_code.entity;

import java.util.ArrayList;

import org.springframework.http.HttpStatus;

public class JwtResponse {
	private String token;
	private HttpStatus status;
	private String message;
	private ArrayList<Object> obj;
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public HttpStatus getStatus() {
		return status;
	}
	public void setStatus(HttpStatus status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public ArrayList<Object> getObj() {
		return obj;
	}
	public void setObj(ArrayList<Object> obj) {
		this.obj = obj;
	}
	@Override
	public String toString() {
		return "JwtResponse [token=" + token + ", status=" + status + ", message=" + message + ", obj=" + obj + "]";
	}
	public JwtResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
	public JwtResponse(String token, HttpStatus status, String message, ArrayList<Object> obj) {
		super();
		this.token = token;
		this.status = status;
		this.message = message;
		this.obj = obj;
	}
	
	
	
	
	
	
	

}
