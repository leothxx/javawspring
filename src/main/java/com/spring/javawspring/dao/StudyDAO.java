package com.spring.javawspring.dao;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Param;

import com.spring.javawspring.vo.GuestVO;

public interface StudyDAO {

	public GuestVO getGuestMid(@Param("mid") String mid);

	public ArrayList<GuestVO> getGuestNames(@Param("mid") String mid, @Param("category") String category);
	
}
