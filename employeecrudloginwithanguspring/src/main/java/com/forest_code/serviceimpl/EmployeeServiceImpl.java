package com.forest_code.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.forest_code.entity.EmployeeEntity;
import com.forest_code.repo.EmployeeRepo;
import com.forest_code.service.EmployeeService;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	private EmployeeRepo employeeRepo;

	@Override
	public EmployeeEntity createEmployee(EmployeeEntity ee) {
		return this.employeeRepo.save(ee);
	}

	@Override
	public List<EmployeeEntity> getAllEmployee() {
		return this.employeeRepo.findAll();
	}

	@Override
	public EmployeeEntity getEmployeeByUserName(String eusername) {
		return this.employeeRepo.getByEusername(eusername);
	}

	@Override
	public EmployeeEntity updateEmployeeByUserName(EmployeeEntity ee) {
		return this.employeeRepo.save(ee);
	}

	@Override
	public int deleteEmployeeByUserName(String eusername) {
		int i = this.employeeRepo.deleteByEusername(eusername);
		return i;
	}

}
