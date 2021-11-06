package com.forest_code.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.forest_code.entity.DataEntity;
import com.forest_code.entity.EmployeeEntity;
import com.forest_code.repo.DataRepo;
import com.forest_code.service.DataService;

@Service
public class DataServiceImpl implements DataService {

	@Autowired
	private DataRepo dataRepo;
		
	@Override
	public DataEntity saveAndUpdateAllEmployee(DataEntity lstee) {
		// TODO Auto-generated method stub
		return this.dataRepo.saveAndFlush(lstee);
	}

	@Override
	public List<DataEntity> getAllEmployee() {
		return this.dataRepo.findAll();
	}

	@Override
	public void deleteEmployeeById(int id) {
		this.dataRepo.deleteById(id);		
	}
	

}
