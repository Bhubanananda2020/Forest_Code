package com.forest_code.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.forest_code.entity.EmployeeEntity;


public interface EmployeeService {


	public EmployeeEntity createEmployee(EmployeeEntity ee);
	
	public List<EmployeeEntity> getAllEmployee();
	
	public EmployeeEntity getEmployeeByUserName(String eusername);
	
	public EmployeeEntity updateEmployeeByUserName(EmployeeEntity ee);
	
	public int deleteEmployeeByUserName(String eusername);
	

	
}
