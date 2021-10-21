package com.forest_code.dto;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.forest_code.entity.EmployeeEntity;

@Component
public class EmployeeEntityDtoConv {

	public EmployeeDTO entityToDto(EmployeeEntity ee) {
		EmployeeDTO employeeDTO = new EmployeeDTO();
		employeeDTO.setEname(ee.getEname());
		employeeDTO.setEemail(ee.getEemail());
		employeeDTO.setEposition(ee.getEposition());
		employeeDTO.setEsalary(ee.getEsalary());
		employeeDTO.setEusername(ee.getEusername());
		employeeDTO.setEpassword(ee.getEpassword());
		employeeDTO.setEroll(ee.getEroll());
		employeeDTO.setEnabled(ee.isEnabled());
		return employeeDTO;
	}

	public List<EmployeeDTO> entityToDto(List<EmployeeEntity> ee) {
		return ee.stream().map(x -> entityToDto(x)).collect(Collectors.toList());
	}

	public EmployeeEntity dtoToEntity(EmployeeDTO ed) {
		EmployeeEntity employeeEntity = new EmployeeEntity();
		employeeEntity.setEname(ed.getEname());
		employeeEntity.setEemail(ed.getEemail());
		employeeEntity.setEposition(ed.getEposition());
		employeeEntity.setEsalary(ed.getEsalary());
		employeeEntity.setEusername(ed.getEusername());
		employeeEntity.setEpassword(ed.getEpassword());
		employeeEntity.setEroll(ed.getEroll());
		employeeEntity.setEnabled(ed.isEnabled());
		return employeeEntity;
	}

	public List<EmployeeEntity> dtoToEntity(List<EmployeeDTO> ed) {
		return ed.stream().map(x -> dtoToEntity(x)).collect(Collectors.toList());
	}
	
	
	

}