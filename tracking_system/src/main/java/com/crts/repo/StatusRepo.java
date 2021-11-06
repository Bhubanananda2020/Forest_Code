package com.crts.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.crts.entity.StatusEntity;

@Repository
public interface StatusRepo extends JpaRepository<StatusEntity, Integer> {

	/* ==== Get Latest Status By requestNumber ==== */
	@Query(value = "SELECT * FROM status_entity se where se.request_number = :reqnumber ORDER BY status_id DESC limit 1", nativeQuery = true)
	public StatusEntity getStatusByRequestNumber(@Param("reqnumber") int reqnumber);

	
	
	/* ==================================== User request work ============================================ */
	
	
	/* ==== Get Status By CREATBY ==== */
	@Query(value = "SELECT * FROM status_entity se WHERE se.created_by = :reqcreateby ORDER BY created_date DESC ;", nativeQuery = true)
	public List<StatusEntity> getStatusByReqcreateby(@Param("reqcreateby") int reqcreateby);

	/* ======== Get ALL ARRISED LATEST Request By LOGIN USER ID ======== */
	@Query(value = "SELECT re.request_number,re.request_title, se.status_desc, re.assigned_to, se.created_date, re.severity, re.piority,  DATEDIFF(se.created_date,re.assigned_date),de.department_name,ue.user_first_name\r\n"
			+ "FROM requests_entity re inner join status_entity se on  se.request_number = re.request_id\r\n"
			+ "inner join user_entity ue on ue.user_id = re.assigned_to \r\n"
			+ "inner join dept_entity de on de.dept_code = re.request_dept\r\n"
			+ "where se.status_id in(SELECT max(status_id) FROM requesttrackingsystemdb.status_entity\r\n"
			+ "group by request_number) and re.created_by = :uid and se.status_desc != 'CLOSE REQUEST';", nativeQuery = true)
	public List<Object[]> getAllArrisedLastUpdateRequest(@Param("uid") int uid);

	/* ======== Get ALL ASSIGN LATEST Request By LOGIN USER ID ======== */
	@Query(value = "SELECT re.request_number,re.request_title, se.status_desc, re.assigned_to, se.created_date, re.severity, re.piority,  DATEDIFF(se.created_date,re.assigned_date),de.department_name,ue.user_first_name\r\n"
			+ "FROM requests_entity re inner join status_entity se on  se.request_number = re.request_id\r\n"
			+ "inner join user_entity ue on ue.user_id = re.assigned_to \r\n"
			+ "inner join dept_entity de on de.dept_code = re.request_dept\r\n"
			+ "where se.status_id in(SELECT max(status_id) FROM requesttrackingsystemdb.status_entity\r\n"
			+ "group by request_number) and re.assigned_to = :uid and se.status_desc != 'CLOSE REQUEST' ", nativeQuery = true)
	public List<Object[]> getAllAssignLastUpdateRequest(@Param("uid") int uid);

	/* ======== Get ALL ARRISED CLOSED Request By LOGIN USER ID ======== */
	@Query(value = "SELECT re.request_number,re.request_title, se.status_desc, re.assigned_to, se.created_date, re.severity, re.piority,  DATEDIFF(se.created_date,re.assigned_date),de.department_name,ue.user_first_name\r\n"
			+ "FROM requests_entity re inner join status_entity se on  se.request_number = re.request_id\r\n"
			+ "inner join user_entity ue on ue.user_id = re.assigned_to \r\n"
			+ "inner join dept_entity de on de.dept_code = re.request_dept\r\n"
			+ "where se.status_id in(SELECT max(status_id) FROM requesttrackingsystemdb.status_entity\r\n"
			+ "group by request_number) and re.created_by = :uid and se.status_desc = 'CLOSE REQUEST' ", nativeQuery = true)
	public List<Object[]> getAllArrisedClosedRequest(@Param("uid") int uid);

	
	
	
	/* ==================================== Admin request view work ============================================ */
	
	

	/* ======== Get ALL ARRISED LATEST Request By LOGIN Admin ID ======== */
	@Query(value = "SELECT re.created_By,re.request_number,re.request_title, se.status_desc,\r\n"
			+ "de.department_name,ue.user_first_name, re.assigned_to, \r\n"
			+ "se.created_date, re.severity, re.piority, DATEDIFF(se.created_date,re.assigned_date) FROM requests_entity re inner join status_entity se on  se.request_number = re.request_id\r\n"
			+ "inner join user_entity ue on ue.user_id = re.assigned_to inner join dept_entity de on de.dept_code = re.request_dept\r\n"
			+ "where se.status_id in(SELECT max(status_id) FROM requesttrackingsystemdb.status_entity\r\n"
			+ "group by request_number) and re.created_by in ((SELECT ud.user_id FROM user_dept ud\r\n"
			+ "where ud.Dept_id in (SELECT ud.Dept_id FROM user_dept ud\r\n"
			+ "where ud.user_id = :uid and ud.user_role = 'admin') and ud.user_role != 'no permission'\r\n"
			+ "group by (ud.user_id))) and re.created_by in(((SELECT ud.user_id FROM user_dept ud\r\n"
			+ "where ud.Dept_id in (SELECT ud.Dept_id FROM user_dept ud\r\n"
			+ "where ud.user_id = :uid and ud.user_role = 'admin') and ud.user_role != 'no permission'\r\n"
			+ "group by (ud.user_id)))) and se.status_desc != 'CLOSE REQUEST';", nativeQuery = true)
	public List<Object[]> getAllRaisedLastUpdateRequestforadmin(@Param("uid") int uid);

	
	/* ======== Get ALL ASSIGN LATEST Request By LOGIN Admin ID ======== */
	@Query(value = "SELECT re.created_By,re.request_number,re.request_title, se.status_desc,\r\n"
			+ "de.department_name,ue.user_first_name, re.assigned_to, \r\n"
			+ "se.created_date, re.severity, re.piority, DATEDIFF(se.created_date,re.assigned_date)\r\n"
			+ "FROM requests_entity re inner join status_entity se on  se.request_number = re.request_id\r\n"
			+ "inner join user_entity ue on ue.user_id = re.assigned_to inner join dept_entity de on de.dept_code = re.request_dept\r\n"
			+ "where se.status_id in(SELECT max(status_id) FROM requesttrackingsystemdb.status_entity\r\n"
			+ "group by request_number) and re.created_by in ((SELECT ud.user_id FROM user_dept ud\r\n"
			+ "where ud.Dept_id in (SELECT ud.Dept_id FROM user_dept ud\r\n"
			+ "where ud.user_id = :uid and ud.user_role = 'admin') and ud.user_role != 'no permission'\r\n"
			+ "group by (ud.user_id))) and  re.assigned_to in (((SELECT ud.user_id FROM user_dept ud\r\n"
			+ "where ud.Dept_id in (SELECT ud.Dept_id FROM user_dept ud\r\n"
			+ "where ud.user_id = :uid and ud.user_role = 'admin') and ud.user_role != 'no permission'\r\n"
			+ "group by (ud.user_id)))) and se.status_desc != 'CLOSE REQUEST';", nativeQuery = true)
	public List<Object[]> getAllAssignLastUpdateRequestforadmin(@Param("uid") int uid);
	
	
	/* ======== Get ALL ARRISED CLOSED Request By LOGIN Admin ID ======== */
	@Query(value = "SELECT re.created_By,re.request_number,re.request_title, se.status_desc,\r\n"
			+ "de.department_name,ue.user_first_name, re.assigned_to, \r\n"
			+ "se.created_date, re.severity, re.piority, DATEDIFF(se.created_date,re.assigned_date) FROM requests_entity re inner join status_entity se on  se.request_number = re.request_id\r\n"
			+ "inner join user_entity ue on ue.user_id = re.assigned_to inner join dept_entity de on de.dept_code = re.request_dept\r\n"
			+ "where se.status_id in(SELECT max(status_id) FROM requesttrackingsystemdb.status_entity\r\n"
			+ "group by request_number) and re.created_by in ((SELECT ud.user_id FROM user_dept ud\r\n"
			+ "where ud.Dept_id in (SELECT ud.Dept_id FROM user_dept ud\r\n"
			+ "where ud.user_id = :uid and ud.user_role = 'admin') and ud.user_role != 'no permission'\r\n"
			+ "group by (ud.user_id))) and re.created_by in(((SELECT ud.user_id FROM user_dept ud\r\n"
			+ "where ud.Dept_id in (SELECT ud.Dept_id FROM user_dept ud\r\n"
			+ "where ud.user_id = :uid and ud.user_role = 'admin') and ud.user_role != 'no permission'\r\n"
			+ "group by (ud.user_id)))) and se.status_desc = 'CLOSE REQUEST';", nativeQuery = true)
	public List<Object[]> getAllRaisedClosedRequestforadmin(@Param("uid") int uid);

	
	
	
	/* ==================================== Super Admin request view work ============================================ */
		
	

	/* ======== Get ALL ARRISED LATEST Request FOR SUPER Admin ======== */
	@Query(value = "SELECT re.created_By,re.request_number,re.request_title, se.status_desc,\r\n"
			+ "de.department_name,ue.user_first_name, re.assigned_to,\r\n"
			+ "se.created_date, re.severity, re.piority, DATEDIFF(se.created_date,re.assigned_date) FROM requests_entity re inner join status_entity se on  se.request_number = re.request_id\r\n"
			+ "inner join user_entity ue on ue.user_id = re.assigned_to inner join dept_entity de on de.dept_code = re.request_dept\r\n"
			+ "where se.status_id in(SELECT max(status_id) FROM requesttrackingsystemdb.status_entity\r\n"
			+ "group by request_number)  and se.status_desc != 'CLOSE REQUEST';", nativeQuery = true)
	public List<Object[]> getAllRaisedLastUpdateRequestforsuperadmin();

	
	/* ======== Get ALL ASSIGN LATEST Request FOR SUPER Admin ======== */
	@Query(value = "SELECT re.created_By,re.request_number,re.request_title, se.status_desc,\r\n"
			+ "de.department_name,ue.user_first_name, re.assigned_to,\r\n"
			+ "se.created_date, re.severity, re.piority, DATEDIFF(se.created_date,re.assigned_date) FROM requests_entity re inner join status_entity se on  se.request_number = re.request_id\r\n"
			+ "inner join user_entity ue on ue.user_id = re.assigned_to inner join dept_entity de on de.dept_code = re.request_dept\r\n"
			+ "where se.status_id in(SELECT max(status_id) FROM requesttrackingsystemdb.status_entity\r\n"
			+ "group by request_number)  and se.status_desc != 'CLOSE REQUEST';", nativeQuery = true)
	public List<Object[]> getAllAssignLastUpdateRequestforsuperadmin();
	
	
	/* ======== Get ALL ARRISED CLOSED Request FOR SUPER Admin ======== */
	@Query(value = "SELECT re.created_By,re.request_number,re.request_title, se.status_desc,\r\n"
			+ "de.department_name,ue.user_first_name, re.assigned_to,\r\n"
			+ "se.created_date, re.severity, re.piority, DATEDIFF(se.created_date,re.assigned_date) FROM requests_entity re inner join status_entity se on  se.request_number = re.request_id\r\n"
			+ "inner join user_entity ue on ue.user_id = re.assigned_to inner join dept_entity de on de.dept_code = re.request_dept\r\n"
			+ "where se.status_id in(SELECT max(status_id) FROM requesttrackingsystemdb.status_entity\r\n"
			+ "group by request_number)  and se.status_desc = 'CLOSE REQUEST';", nativeQuery = true)
	public List<Object[]> getAllRaisedClosedRequestforsuperadmin();

	
	
	
}
