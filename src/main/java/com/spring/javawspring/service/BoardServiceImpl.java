package com.spring.javawspring.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.spring.javawspring.dao.BoardDAO;
import com.spring.javawspring.vo.BoardReplyVO;
import com.spring.javawspring.vo.BoardVO;
import com.spring.javawspring.vo.GoodVO;

@Service
public class BoardServiceImpl implements BoardService {
	@Autowired
	BoardDAO boardDAO;

	@Override
	public List<BoardVO> getBoardList(int startIndexNo, int pageSize) {
		return boardDAO.getBoardList(startIndexNo, pageSize);
	}

	@Override
	public int setBoardInput(BoardVO vo) {
		return boardDAO.setBoardInput(vo);
	}

	@Override
	public BoardVO getBoardContent(int idx) {
		return boardDAO.getBoardContent(idx);
	}

	@Override
	public void setBoardReadNum(int idx) {
		boardDAO.setBoardReadNum(idx);
	}

	@Override
	public void setBoardGood(int idx) {
		boardDAO.setBoardGood(idx);
	}

	@Override
	public GoodVO getBoardGoodCheck(int partIdx, String part, String mid) {
		return boardDAO.getBoardGoodCheck(partIdx,part,mid);
	}

	@Override
	public ArrayList<BoardVO> getPrevNext(int idx) {
		return boardDAO.getPrevNext(idx);
	}

	@Override
	public void imgCheck(String content) {
		//      0123456789012345678901234567890123456789012345678901234567890
		// <img src="/javawspring/data/ckeditor/230111121332_주디.jfif" style="height:330px; width:152px" />
		// content안에 그림파일이 존재할때만 작업을 수행 할 수 있도록 한다.
		if(content.indexOf("src=\"/") == -1) return;
		
		// request 객체를 가져다가 사용하는 방법
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		String uploadPath = request.getSession().getServletContext().getRealPath("/resources/data/ckeditor/");
		
		int position = 32;
		String nextImg = content.substring(content.indexOf("src=\"/") + position);
		boolean sw = true;
		
		while(sw) {
			String imgFile = nextImg.substring(0, nextImg.indexOf("\""));
			String origFilePath = uploadPath + imgFile;
			String copyFilePath = request.getSession().getServletContext().getRealPath("/resources/data/board/"+ imgFile);
			
			fileCopyCheck(origFilePath, copyFilePath); // board폴더에 파일을 복사하고자 한다.
			if(nextImg.indexOf("src=\"/") == -1) sw = false; // src가 더이상 없다면 반복문 탈출
		
			else nextImg = nextImg.substring(nextImg.indexOf("src=\"/") + position); // 더 있다면 다음 src위치로 지정
		}
	}

	private void fileCopyCheck(String origFilePath, String copyFilePath) {
		File origFile = new File(origFilePath);
		File copyFile = new File(copyFilePath);
		
		try {
			//서버에서 서버로 옮겨야 하기에, input 스트림과 ouput 스트림 둘다 필요하다.
			FileInputStream fis = new FileInputStream(origFile);
			FileOutputStream fos = new FileOutputStream(copyFile);
			//origFile 위치에서 copyFile 위치로 파일 복사과정. 2048 2k의 속도로 복사한다.
			byte[] buffer = new byte[2048];
			int cnt = 0;
			while((cnt = fis.read(buffer)) != -1) {
				fos.write(buffer, 0, cnt);
			}
			fos.flush(); // 찌꺼기 처리
			fos.close();
			fis.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void setBoardDeleteOk(int idx) {
		boardDAO.setBoardDeleteOk(idx);
	}

	@Override
	public void imgDelete(String content) {
		//      0123456789012345678901234567890123456789012345678901234567890
		// <img src="/javawspring/data/board/230111121332_주디.jfif" style="height:330px; width:152px" />
		// content안에 그림파일이 존재할때만 작업을 수행 할 수 있도록 한다.
		if(content.indexOf("src=\"/") == -1) return;
		
		// request 객체를 가져다가 사용하는 방법
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		String uploadPath = request.getSession().getServletContext().getRealPath("/resources/data/board/");
		
		int position = 29;
		String nextImg = content.substring(content.indexOf("src=\"/") + position);
		boolean sw = true;
		
		while(sw) {
			String imgFile = nextImg.substring(0, nextImg.indexOf("\""));
			String origFilePath = uploadPath + imgFile;
			
			fileDelete(origFilePath); // board폴더에 파일을 삭제하고자 한다.
			if(nextImg.indexOf("src=\"/") == -1) sw = false; // src가 더이상 없다면 반복문 탈출
		
			else nextImg = nextImg.substring(nextImg.indexOf("src=\"/") + position); // 더 있다면 다음 src위치로 지정
		}
	}

	
	// 파일 삭제하기.
	private void fileDelete(String origFilePath) {
		File delFile = new File(origFilePath);
		if(delFile.exists()) delFile.delete(); // 파일이 있다면 삭제!
	}

	@Override
	public void imgCheckUpdate(String content) {
		//      0123456789012345678901234567890123456789012345678901234567890
		// <img src="/javawspring/data/board/230111121332_주디.jfif" style="height:330px; width:152px" />
		// <img src="/javawspring/data/ckeditor/230111121332_주디.jfif" style="height:330px; width:152px" />
		// content안에 그림파일이 존재할때만 작업을 수행 할 수 있도록 한다.
		if(content.indexOf("src=\"/") == -1) return;
		
		// request 객체를 가져다가 사용하는 방법
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		String uploadPath = request.getSession().getServletContext().getRealPath("/resources/data/board/");
		
		int position = 29;
		String nextImg = content.substring(content.indexOf("src=\"/") + position);
		boolean sw = true;
		
		while(sw) {
			String imgFile = nextImg.substring(0, nextImg.indexOf("\""));
			String origFilePath = uploadPath + imgFile;
			String copyFilePath = request.getSession().getServletContext().getRealPath("/resources/data/ckeditor/"+ imgFile);
			
			fileCopyCheck(origFilePath, copyFilePath); // ckeditor폴더에 파일을 복사하고자 한다.
			if(nextImg.indexOf("src=\"/") == -1) sw = false; // src가 더이상 없다면 반복문 탈출
		
			else nextImg = nextImg.substring(nextImg.indexOf("src=\"/") + position); // 더 있다면 다음 src위치로 지정
		}
	}

	@Override
	public void setBoardUpdateOk(BoardVO vo) {
		boardDAO.setBoardUpdateOk(vo);
	}

	@Override
	public void setBoardReplyInput(BoardReplyVO replyVo) {
		boardDAO.setBoardReplyInput(replyVo);
	}

	@Override
	public List<BoardReplyVO> getBoardReply(int idx) {
		return boardDAO.getBoardReply(idx);
	}

	@Override
	public void setBoardReplyDeleteOk(int idx) {
		boardDAO.setBoardReplyDeleteOk(idx);
	}

	@Override
	public String getMaxLevelOrder(int boardIdx) {
		return boardDAO.getMaxLevelOrder(boardIdx);
	}

	@Override
	public void setLevelOrderPlusUpdate(BoardReplyVO replyVo) {
		boardDAO.setLevelOrderPlusUpdate(replyVo);
	}

	@Override
	public void setBoardReplyInput2(BoardReplyVO replyVo) {
		boardDAO.setBoardReplyInput2(replyVo);
	}

	@Override
	public int setBoardReplyUpdateOk(int idx, String content, String hostIp) {
		return boardDAO.setBoardReplyUpdateOk(idx,content,hostIp);
	}

	@Override
	public List<BoardVO> getBoardListSearch(int startIndexNo, int pageSize, String search, String searchString) {
		return boardDAO.getBoardListSearch(startIndexNo, pageSize, search, searchString);
	}

}
