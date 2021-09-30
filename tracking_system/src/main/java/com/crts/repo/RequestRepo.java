package com.crts.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.crts.entity.CommentsHistory;
import com.crts.entity.RequestEntity;

@Repository
public interface RequestRepo extends JpaRepository<RequestEntity, Integer> {

	/* ======== Get Last Request Code ======== */
	@Query("select max(re.reqcode) from RequestEntity re where re.reqdeptcode = :deptcode")
	public String getLastRequestNumber(@Param("deptcode") String deptcode);

	/* ======== Get Request By Request Code ======== */
	public RequestEntity getByreqcode(String reqcode);

	/* ===== Get All comments By Request Id and user id ===== */
	@Query(value = "SELECT re.request_title, ue.user_first_name, ce.created_date, ce.comments_desc\r\n"
			+ "FROM requests_entity re INNER JOIN comments_entity ce ON ce.request_number = re.request_id \r\n"
			+ "inner join user_entity ue on ue.user_id = ce.created_by WHERE re.request_number = :reqnum order by ce.created_date desc;", nativeQuery = true)
	public List<Object[]> getAllCommentByReqId(@Param("reqnum") String reqnum);

	
	
	/* ========= No of raised Request for Department=============== */
	@Query(value = "SELECT count(re.request_number)\r\n"
			+ "FROM requests_entity re inner join status_entity se on  se.request_number = re.request_id\r\n"
			+ "inner join user_entity ue on ue.user_id = re.assigned_to\r\n"
			+ "inner join dept_entity de on de.dept_code = re.request_dept\r\n"
			+ "where se.status_id in(SELECT max(status_id) FROM requesttrackingsystemdb.status_entity\r\n"
			+ "group by request_number) and re.created_by = :uid and se.status_desc != 'CLOSE REQUEST';", nativeQuery = true)
	public int noOfRaisedRequestInDepartment(@Param("uid") int uid);

	

	
	
	
	/* ========= No of Assigned Request for Department=============== */
	@Query(value = "SELECT count(re.request_number)\r\n"
			+ "FROM requests_entity re inner join status_entity se on  se.request_number = re.request_id\r\n"
			+ "inner join user_entity ue on ue.user_id = re.assigned_to\r\n"
			+ "inner join dept_entity de on de.dept_code = re.request_dept\r\n"
			+ "where se.status_id in(SELECT max(status_id) FROM requesttrackingsystemdb.status_entity\r\n"
			+ "group by request_number) and re.assigned_to = :uid and se.status_desc != 'CLOSE REQUEST';", nativeQuery = true)
	public int noOfAssignedRequestInDepartment(@Param("uid") int uid);

}








