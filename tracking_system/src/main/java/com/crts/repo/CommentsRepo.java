package com.crts.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crts.entity.CommentsEntity;

public interface CommentsRepo extends JpaRepository<CommentsEntity, Integer>{

}
