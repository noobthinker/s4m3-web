package com.xkorey.myibatis.service.impl;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xkorey.myibatis.dao.UserMapper;
import com.xkorey.myibatis.models.User;
import com.xkorey.myibatis.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserMapper mapper;
	
	public User getUser(Integer id) {
		return mapper.selectByPrimaryKey(id);
	}

}
