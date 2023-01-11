package com.spring.javawspring.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.spring.javawspring.vo.BoardVO;
import com.spring.javawspring.vo.GoodVO;

public interface BoardDAO {

	public List<BoardVO> getBoardList(@Param("startIndexNo") int startIndexNo, @Param("pageSize") int pageSize);

	public int totRecCnt();

	public int setBoardInput(@Param("vo") BoardVO vo);

	public BoardVO getBoardContent(@Param("idx") int idx);

	public void setBoardReadNum(@Param("idx") int idx);

	public void setBoardGood(@Param("idx") int idx);

	public GoodVO getBoardGoodCheck(@Param("partIdx") int partIdx, @Param("part") String part, @Param("mid") String mid);

	public ArrayList<BoardVO> getPrevNext(@Param("idx") int idx);

	public void setBoardDeleteOk(@Param("idx") int idx);

	public void setBoardUpdateOk(@Param("vo") BoardVO vo);
	
}
