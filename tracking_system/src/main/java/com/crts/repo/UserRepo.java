package com.crts.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.crts.entity.UserEntity;

@Repository
public interface UserRepo extends JpaRepository<UserEntity, Integer> {

	/* ===== Get User Data By username or email ===== */
	@Query(value = "SELECT user_id, created_by, created_date, is_user_active, user_first_name, user_last_name, user_password, user_email, username \r\n"
			+ "FROM user_entity where user_email = :username  or username = :username and  is_user_active !=0;", nativeQuery = true)
	public UserEntity getUserEntityByUserNameEmail(@Param("username") String username);

	/* ===== Get Email Id By Firstname ===== */
	@Query(value = "SELECT ue.user_email FROM user_entity ue where ue.user_first_name = :username", nativeQuery = true)
	public String getEmailIdByFirstName(@Param("username") String username);

	/* ===== Get User By UserId ===== */
	public UserEntity getByuId(int uid);

	/* ===== View All User FOR ADMIN VIEW ===== */
	@Query(value = "select * from user_entity ue\r\n"
			+ "where ue.user_id in( SELECT ud.user_id FROM user_dept ud where ud.Dept_id in (\r\n"
			+ "SELECT ud.Dept_id FROM user_dept ud where ud.user_id = :uid and ud.user_role = 'admin') and ud.user_role != 'no permission');", nativeQuery = true)
	public List<UserEntity> getAllUserByuIdForAdminView(@Param("uid") int uid);

}
