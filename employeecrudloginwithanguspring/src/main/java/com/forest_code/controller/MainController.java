package com.forest_code.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.forest_code.dto.EmployeeDTO;
import com.forest_code.dto.EmployeeEntityDtoConv;
import com.forest_code.entity.EmployeeEntity;
import com.forest_code.service.EmployeeService;

@RestController
@RequestMapping("/employee")
public class MainController {

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private EmployeeEntityDtoConv employeeEntityDtoConv;

	@GetMapping("/home")
	public ResponseEntity<String> home() {
		return new ResponseEntity<String>("Welcome or Dashboardpage", HttpStatus.OK);
	}

	@PostMapping("/")
	public ResponseEntity<EmployeeDTO> createEmployee(@RequestBody EmployeeEntity ee) {
		EmployeeDTO employeeDTO = new EmployeeDTO();
		System.out.println(ee);
		ee.setEpassword(passwordEncoder.encode(ee.getEpassword()));
		EmployeeEntity createEmployee = this.employeeService.createEmployee(ee);
		if (createEmployee != null) {
			employeeDTO = employeeEntityDtoConv.entityToDto(createEmployee);
			return new ResponseEntity<EmployeeDTO>(employeeDTO, HttpStatus.OK);
		} else {
			return new ResponseEntity<EmployeeDTO>(employeeDTO, HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/")
	public ResponseEntity<List<EmployeeDTO>> getAllEmployee() {
		List<EmployeeDTO> lisEmpDto = new ArrayList<EmployeeDTO>();
		List<EmployeeEntity> getAllEmpLst = this.employeeService.getAllEmployee();
		lisEmpDto = employeeEntityDtoConv.entityToDto(getAllEmpLst);
		return new ResponseEntity<List<EmployeeDTO>>(lisEmpDto, HttpStatus.OK);
	}

	@GetMapping("/{eusername}")
	public ResponseEntity<EmployeeDTO> getEmployeeByUserName(@PathVariable("eusername") String eusername) {
		EmployeeDTO empDto = new EmployeeDTO();
		EmployeeEntity singleEmployee = this.employeeService.getEmployeeByUserName(eusername);
		System.out.println(singleEmployee);
		empDto = employeeEntityDtoConv.entityToDto(singleEmployee);
		return new ResponseEntity<EmployeeDTO>(empDto, HttpStatus.OK);
	}

	@PutMapping("/")
	public ResponseEntity<EmployeeDTO> updateEmployee(@RequestBody EmployeeEntity ee) {
		EmployeeDTO empDto = new EmployeeDTO();
		EmployeeEntity updateEmployee = this.employeeService.updateEmployeeByUserName(ee);
		empDto = employeeEntityDtoConv.entityToDto(updateEmployee);
		return new ResponseEntity<EmployeeDTO>(empDto, HttpStatus.OK);
	}

	@DeleteMapping("/{eusername}")
	public ResponseEntity<String> getAllEmployewe(@PathVariable("eusername") String eusername) {
		int i = this.employeeService.deleteEmployeeByUserName(eusername);
		if (i > 0) {
			return new ResponseEntity<String>("User Delete Successfully", HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("User not Found", HttpStatus.NOT_FOUND);
		}
	}

}
