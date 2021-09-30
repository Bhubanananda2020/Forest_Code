package com.crts.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.crts.entity.DeptEntity;

@Repository
public interface DeptRepo extends JpaRepository<DeptEntity, Integer> {

	/* ============ GET ALL PARENT DEPARTMENT CODE ============ */
	@Query("select DISTINCT depcode from DeptEntity")
	public List<String> getAllDeptEntityParentCode();

	
	
	
	
	
	
	
	
	/* ============ GET ALL PARENT DEPARTMENT CODE By UserId ============ */
	@Query(value = "SELECT parent_department_code FROM dept_entity\r\n"
			+ "where Dept_id = (SELECT Dept_id FROM user_dept where user_id = :uId and user_role != 'nouser' );", nativeQuery = true)
	public List<String> getAllDeptEntityParentCodeAndUid(@Param("uId") int uId);

	/* ============ GET ALL DEPARTMENT CODE ============ */
	@Query("select DISTINCT decode from DeptEntity")
	public List<String> getAllDeptEntityCode();

	/* ============ GET DEPARTMENT CODE by code ============ */
	@Query("select de from DeptEntity de where de.decode = :deptcode")
	public List<DeptEntity> getAllDeptEntityByCode(@Param("deptcode") String deptcode);

	/* ============ GET ALL CREATE USER BY DEPTCODE ============ */
	@Query("select de from DeptEntity de where de.decode = :deptcode")
	public List<DeptEntity> getAllDeptEntityUser(@Param("deptcode") String deptcode);

	/* ============ GET DEPARTMENT ID BY DEPTCODE ============ */
	@Query("select de.deid from DeptEntity de where de.decode = :deptcode")
	public int getDeptIDByDeptCode(@Param("deptcode") String deptcode);

	/* ============ GET DEPARTMENT BY DEPARTMENT ID ============ */	
	public DeptEntity getBydeid(int deid);
	
	
	
	
}
