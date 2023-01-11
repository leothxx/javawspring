package com.spring.javawspring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.javawspring.dao.AdminDAO;

@Service
public class AdminServiceImpl implements AdminService {
	
	@Autowired
	AdminDAO adminDAO;

	@Override
	public int setMemberLevelUpdate(int idx, int level) {
		return adminDAO.setMemberLevelUpdate(idx,level);
	}

	@Override
	public int adminMemberDelete(int idx) {
		return adminDAO.adminMemberDelete(idx);
	}
}
