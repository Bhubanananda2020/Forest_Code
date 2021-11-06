package com.forest_code.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
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
import com.forest_code.entity.DataEntity;
import com.forest_code.entity.EmployeeEntity;
import com.forest_code.service.DataService;
import com.forest_code.service.EmployeeService;

@RestController
@RequestMapping("/employee")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class MainController {

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private EmployeeEntityDtoConv employeeEntityDtoConv;

	@Autowired
	private DataService dataService;

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

	/*
	 * @PostMapping("/savealldata") public ResponseEntity<List<EmployeeDTO>>
	 * createEmployee(@RequestBody List<EmployeeEntity> lstee) { List<EmployeeDTO>
	 * employeeDTO = new ArrayList<EmployeeDTO>(); List<EmployeeEntity> list = new
	 * ArrayList<EmployeeEntity>();
	 * 
	 * EmployeeEntity entity2 = new EmployeeEntity(); EmployeeEntity entity1 = new
	 * EmployeeEntity();
	 * 
	 * for (EmployeeEntity lst : lstee) {
	 * lst.setEpassword(passwordEncoder.encode(lst.getEpassword()));
	 * System.out.println("user Id Is => "+lst.getEid()); if (lst.getEid() != 0) {
	 * System.out.println("Data Update"); System.out.println("=========>" + lst);
	 * entity1 = this.employeeService.createEmployee(lst); list.add(entity1);
	 * System.out.println("--------->" + entity1); } else {
	 * System.out.println("=========>" + lst); System.out.println("Data Save");
	 * entity2 = this.employeeService.createEmployee(lst); list.add(entity2);
	 * System.out.println("--------->" + entity2); } }
	 * 
	 * employeeDTO = employeeEntityDtoConv.entityToDto(list); return new
	 * ResponseEntity<List<EmployeeDTO>>(employeeDTO, HttpStatus.OK); }
	 */

	@PostMapping("/savealldata")
	public ResponseEntity<List<DataEntity>> createEmployee(@RequestBody List<DataEntity> lsteee) {
		System.out.println();
		System.out.println(lsteee);
		System.out.println();
		List<DataEntity> list = new ArrayList<DataEntity>();

		DataEntity entity2 = new DataEntity();
		DataEntity entity1 = new DataEntity();
		for (DataEntity lst : lsteee) {
			System.out.println("user Id Is => " + lst.getId());
			if (lst.getId() != 0) {
				System.out.println("Data Update");
				System.out.println("=========>" + lst);
				entity1 = this.dataService.saveAndUpdateAllEmployee(lst);
				list.add(entity1);
				System.out.println("--------->" + entity1);
			} else {
				System.out.println("=========>" + lst);
				System.out.println("Data Save");
				entity2 = this.dataService.saveAndUpdateAllEmployee(lst);
				list.add(entity2);
				System.out.println("--------->" + entity2);
			}
		}
		return new ResponseEntity<List<DataEntity>>(list, HttpStatus.OK);
	}

	@GetMapping("/getalllist")
	public ResponseEntity<List<DataEntity>> getAllEmployeeList() {
		return new ResponseEntity<List<DataEntity>>(this.dataService.getAllEmployee(), HttpStatus.OK);
	}

	@PostMapping("/deleteuser/{id}")
	public ResponseEntity<String> getAllEmployewe(@PathVariable("id") int id) {
		this.dataService.deleteEmployeeById(id);
		return new ResponseEntity<String>("User Delete Successfully", HttpStatus.OK);

	}

}