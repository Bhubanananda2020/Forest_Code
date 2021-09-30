package com.crts.serviceimpl;

import org.springframework.stereotype.Service;

import com.crts.entity.CommentsEntity;
import com.crts.service.CommentsService;

@Service
public class CommentsServiceImpl implements CommentsService{


	public boolean saveComments(CommentsEntity ce) {
		return this.saveComments(ce);
	}

}
