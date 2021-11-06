package com.forest_code.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.forest_code.entity.DataEntity;

public interface DataRepo extends JpaRepository<DataEntity, Integer> {

}
