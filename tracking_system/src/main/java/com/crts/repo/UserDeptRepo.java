package com.crts.repo;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.crts.entity.UserDeptEntity;

public interface UserDeptRepo extends JpaRepository<UserDeptEntity, Integer> {

	@Query(value = "SELECT ue.user_id,ue.user_first_name,de.user_role FROM user_entity ue inner join user_dept de on ue.user_id = de.user_id where de.Dept_id = :deptid", nativeQuery = true)
	public List<Object[]> getAllUserByDeptid(int deptid);

	
	/* ========= No of user role by user id===============*/
	@Query(value = "SELECT ud.user_role FROM user_dept ud where ud.user_id = :userid and ud.user_role != 'no permission'", nativeQuery = true)
	public List<String> IsUserAdmin(int userid);


	
	
	/* ========= DELETE user role by user id===============*/
	@Modifying
	@Transactional
	@Query(value = "DELETE FROM user_dept ud WHERE ud.user_id=:uId", nativeQuery = true)	
	public int DeleteUserDeptAccess(int uId);

	
	/* ========= Get user role by user id===============*/
	@Query(value = "SELECT * FROM user_dept ud where ud.user_id = :userid", nativeQuery = true)
	public List<UserDeptEntity> getAllRollByUserId(int userid);
	
	
	/* ============ GET ALL DEPARTMENT ============ */
	@Query(value = "SELECT ud.user_role FROM user_dept ud where ud.user_id = :uid and ud.Dept_id = :deptid", nativeQuery = true)	
	public String getAllDeptpermission(int uid, int deptid);
	
	
	
	
	/* ========= Get All admin department by user id===============*/	
	@Query(value = "SELECT de.dept_code FROM user_dept ud \r\n"
			+ "inner join dept_entity de on de.Dept_id = ud.Dept_id\r\n"
			+ "where ud.user_id = :userid  and ud.user_role = 'admin';", nativeQuery = true)
	public List<String> AllAdminDepartment(int userid);

	
	
	
	/* ========= Get All super admin department by user id===============*/	
	@Query(value = "SELECT DE.dept_code FROM dept_entity de", nativeQuery = true)
	public List<String> AllSupAdminDepartment();
	
	
	
	
	
	/* ========= No of Sub Department===============*/
	@Query(value = "SELECT count(Dept_id) FROM dept_entity\r\n"
			+ "where parent_department_code in (SELECT parent_department_code FROM dept_entity\r\n"
			+ "where Dept_id in (SELECT Dept_id FROM user_dept where user_id = :uid and user_role != 'no permission' )) and parent_department_code != dept_code;", nativeQuery = true)
	public int noOfDepartment (@Param("uid") int uid);
	
	
	
	/* ========= No of User in Same Department===============*/
	@Query(value = "SELECT count(ud.user_role) FROM requesttrackingsystemdb.user_dept ud\r\n"
			+ "where ud.Dept_id  in (SELECT Dept_id FROM requesttrackingsystemdb.user_dept\r\n"
			+ "where user_id = :uid and user_role != 'no permission') and ud.user_role != 'no permission'", nativeQuery = true)	
	public int noOfUserInDepartment(@Param("uid") int uid);
}