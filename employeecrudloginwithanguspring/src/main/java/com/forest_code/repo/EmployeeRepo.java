package com.forest_code.repo;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.forest_code.entity.EmployeeEntity;

public interface EmployeeRepo extends JpaRepository<EmployeeEntity, Integer> {

	@Query(value = "select * from employee_entity where eusername = :username OR eemail = :username ", nativeQuery = true)
	public EmployeeEntity getByEusername(@Param("username") String username);
	
	@Transactional
	public int deleteByEusername(String eusername);


	/*
	 * update employee_entity set eemail='test90@test.com', ename = 'test90',
	 * epassword='test90', eposition='test90', eroll='test90', esalary=5, is_enabled
	 * = 1 where eusername = "test90"
	 */
	
}
