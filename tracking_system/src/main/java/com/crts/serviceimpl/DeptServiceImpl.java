package com.crts.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crts.entity.DeptEntity;
import com.crts.repo.DeptRepo;
import com.crts.service.DeptService;

@Service
public class DeptServiceImpl implements DeptService {

	@Autowired
	private DeptRepo deptRepo;

	/* ============ SAVE DEPARTMENT ============ */
	public boolean saveDepartment(String dcode, String dname, String dpcode, int createby, String diact) {
		boolean saveDepartmentFlag = false;
		try {
			long millis = System.currentTimeMillis();
			java.sql.Date date = new java.sql.Date(millis);
			Boolean deptstatus = Boolean.valueOf(diact);
			DeptEntity de = new DeptEntity();
			de.setDecode(dcode);
			de.setDename(dname);
			de.setDepcode(dpcode);
			de.setDecdate(date);
			de.setDecreatedby(createby);
			de.setDeisactive(deptstatus);
			DeptEntity save = this.deptRepo.save(de);
			if (save != null) {
				saveDepartmentFlag = true;
			} else {
				saveDepartmentFlag = false;
			}
		} catch (Exception e) {
			saveDepartmentFlag = false;
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

	/* ============ GET ALL DEPARTMENT ============ */
	public List<DeptEntity> getAllDept() {
		return this.deptRepo.findAll();
	}

	/* ============ GET DEPARTMENT BY DEPARTMENT ID ============ */	
	public DeptEntity getBydeid(int deid) {
		return this.deptRepo.getById(deid);
	}

}
