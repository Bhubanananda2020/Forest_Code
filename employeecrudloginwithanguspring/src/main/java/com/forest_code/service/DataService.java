package com.forest_code.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.forest_code.entity.DataEntity;
import com.forest_code.entity.EmployeeEntity;

@Service
public interface DataService {

	public DataEntity saveAndUpdateAllEmployee(DataEntity lstee);

	public List<DataEntity> getAllEmployee();
	
	public void deleteEmployeeById(int id);

	
}
