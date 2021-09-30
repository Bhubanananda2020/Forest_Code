package com.crts.service;

import org.springframework.stereotype.Service;

import com.crts.entity.CommentsEntity;

@Service
public interface CommentsService {

	/* Save comments */	
	public boolean saveComments(CommentsEntity  ce);

}
