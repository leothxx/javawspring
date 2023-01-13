package com.spring.javawspring.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.spring.javawspring.vo.ScheduleVO;

public interface ScheduleDAO {

	public List<ScheduleVO> getScheduleList(@Param("mid") String mid, @Param("ym") String ym);

	public List<ScheduleVO> getScheduleMenu(@Param("mid") String mid, @Param("ymd") String ymd);

	public int setScheduleInputOk(@Param("vo") ScheduleVO vo);

	public int setScheduleUpdateOk(@Param("vo") ScheduleVO vo);

	public int setScheduleDeleteOk(@Param("idx") int idx);

}
