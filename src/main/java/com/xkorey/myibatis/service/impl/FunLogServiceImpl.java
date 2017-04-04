package com.xkorey.myibatis.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xkorey.myibatis.dao.FunLogMapper;
import com.xkorey.myibatis.models.FunLog;

@Service
public class FunLogServiceImpl implements com.xkorey.myibatis.service.FunLogService {

	@Autowired
	FunLogMapper mapper;
	
	public FunLog getLog(Long id) {
		return mapper.selectByPrimaryKey(id);
	}

}
