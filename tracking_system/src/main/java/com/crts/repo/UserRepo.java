package com.crts.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.crts.entity.UserEntity;

@Repository
public interface UserRepo extends JpaRepository<UserEntity, Integer> {

	/* ===== Get User Data By username or email and password ===== */
	@Query("select ue from UserEntity ue where ue.uEmail = :username or ue.uName = :username and ue.uPassword = :password")
	public UserEntity getUserEntityByUserNameEmailPassword(@Param("username") String username,
			@Param("password") String password);

	/* ===== Get User Data By username or email ===== */
	@Query("select ue from UserEntity ue where ue.uEmail = :username or ue.uName = :username")
	public UserEntity getUserEntityByUserNameEmail(@Param("username") String username);

	
	
	/* ===== Get Email Id By Firstname ===== */
	@Query(value = "SELECT ue.user_email FROM user_entity ue where ue.user_first_name = :username", nativeQuery = true)
	public String getEmailIdByFirstName(@Param("username") String username);

	/* ===== Get User By UserId ===== */
	public UserEntity getByuId(int uid);

	
	
}
