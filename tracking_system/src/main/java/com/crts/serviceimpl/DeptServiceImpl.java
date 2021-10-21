package com.crts.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crts.entity.DeptEntity;
import com.crts.repo.DeptRepo;
import com.crts.repo.UserDeptRepo;
import com.crts.service.DeptService;
import com.crts.service.UserDeptService;

@Service
public class DeptServiceImpl implements DeptService {

	@Autowired
	private DeptRepo deptRepo;

	@Autowired
	private UserDeptRepo userDeptRepo;

	/* ============ SAVE DEPARTMENT ============ */
	public DeptEntity saveDepartment(DeptEntity deptEntity) {
		DeptEntity saveDepartmentFlag = null;
		try {
			long millis = System.currentTimeMillis();
			java.sql.Date date = new java.sql.Date(millis);
			deptEntity.setDecdate(date);
			DeptEntity save = this.deptRepo.save(deptEntity);
			if (save != null) {
				saveDepartmentFlag = save;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return saveDepartmentFlag;
	}

	/* ============ GET ALL PARENT DEPARTMENT CODE By UserId ============ */
	public List<String> getAllParentDeptCodeAndUid(int uId) {
		return this.deptRepo.getAllDeptEntityParentCodeAndUid(uId);
	}

	/* ============ GET ALL PARENT DEPARTMENT CODE ============ */
	public List<String> getAllParentDeptCode() {
		return this.deptRepo.getAllDeptEntityParentCode();
	}

	/* ============ GET ALL DEPARTMENT CODE ============ */
	public List<DeptEntity> getAllDeptEntityByCode(String deptcode) {
		return this.deptRepo.getAllDeptEntityByCode(deptcode);
	}

	/* ============ GET ALL CREATE USER BY DEPTCODE ============ */
	public List<DeptEntity> getDeptUserByDeptcode(String deptcode) {
		return this.deptRepo.getAllDeptEntityUser(deptcode);
	}

	/* ============ GET ALL DEPARTMENT CODE ============ */
	public List<String> getAllDeptCode() {
		return this.deptRepo.getAllDeptEntityCode();
	}

	/* ============ GET DEPARTMENT ID By DEPT CODE ============ */
	public int getDeptIDByDeptCode(String deptcode) {
		return this.deptRepo.getDeptIDByDeptCode(deptcode);
	}

	/* ============ GET ALL DEPARTMENT FOR SUPER ADMIN ============ */
	public List<DeptEntity> getAllDept() {
		return this.deptRepo.findAll();
	}

	/* ============ GET DEPARTMENT BY DEPARTMENT ID ============ */
	public DeptEntity getBydeid(int deid) {
		return this.deptRepo.getById(deid);
	}

	/* ============ GET ALL DEPARTMENT FOR ADMIN============ */
	public List<DeptEntity> getAllDeptAdmin(int uId) {
		return this.deptRepo.getAllDeptAdmin(uId);
	}

}
