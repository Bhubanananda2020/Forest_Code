package com.crts.response;

import java.util.ArrayList;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Apiresponse {
	private HttpStatus status;
	private String message;
	private ArrayList<Object> obj;
}