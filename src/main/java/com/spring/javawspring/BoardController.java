package com.spring.javawspring;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.spring.javawspring.pagination.PageProcess;
import com.spring.javawspring.pagination.PageVO;
import com.spring.javawspring.service.BoardService;
import com.spring.javawspring.service.MemberService;
import com.spring.javawspring.vo.BoardVO;
import com.spring.javawspring.vo.GoodVO;
import com.spring.javawspring.vo.MemberVO;

@Controller
@RequestMapping("/board")
public class BoardController {
	
	@Autowired
	BoardService boardService;
	
	@Autowired
	MemberService memberService;
	
	@Autowired
	PageProcess pageProcess;
	
	
	@RequestMapping(value="/boardList",method=RequestMethod.GET)
	public String boardListGet(Model model,
			@RequestParam(name="pag", defaultValue="1", required = false) int pag,
			@RequestParam(name="pageSize", defaultValue="5", required = false) int pageSize) {
		PageVO pageVO = pageProcess.totRecCnt(pag, pageSize, "board", "", "");
		
		List<BoardVO> vos = boardService.getBoardList(pageVO.getStartIndexNo(),pageSize);
		
		model.addAttribute("vos",vos);
		model.addAttribute("pageVO",pageVO);
		return "board/boardList";
	}
	
	@RequestMapping(value="/boardInput",method=RequestMethod.GET)
	public String boardInputGet(Model model,HttpSession session, String pag, String pageSize) {
		String mid = session.getAttribute("sMid") == null ? "" : (String) session.getAttribute("sMid");
		String sNickName = session.getAttribute("sNickName") == null ? "" : (String) session.getAttribute("sNickName");
		MemberVO vo = memberService.getMemberIdCheck(mid);
		model.addAttribute("pag",pag);
		model.addAttribute("pageSize",pageSize);
		model.addAttribute("sNickName",sNickName);
		model.addAttribute("homePage",vo.getHomePage());
		model.addAttribute("email",vo.getEmail());
		return "board/boardInput";
	}
	
	@RequestMapping(value="/boardInput",method=RequestMethod.POST)
	public String boardInputPost(BoardVO vo) {
		// content에 이미지가 저장되어 있다면, 저장된 이미지만 골라서 /resources/data/board 폴더에 저장시켜준다.
		boardService.imgCheck(vo.getContent());
		
		// 이미지 복사작업이 끝나면, board폴더에 실제로 저장된 파일명을 DB에 저장시켜준다. (/resources/data/ckeditor/ == >> /resources/data/board/)
		vo.setContent(vo.getContent().replace("/data/ckeditor/", "/data/board/"));
		
		int res = boardService.setBoardInput(vo);
		if(res == 1) return "redirect:/msg/boardInputOk";
		else return "board/boardInputNo"; 
	}
	
	@RequestMapping(value="/boardContent",method=RequestMethod.GET)
	public String boardContentGet(Model model, int idx, String pag, String pageSize, HttpServletRequest request, HttpSession session) {
		
		// 글 조회수 1회 증가시키기.(조회수 중복방지처리 - 세션 사용 : 'board+고유번호'를 객체배열에 추가시킨다.)
		ArrayList<String> contentIdx = (ArrayList) session.getAttribute("sContentIdx");
		if(contentIdx == null) contentIdx = new ArrayList<String>();
		String imsiContentIdx = "board" + idx;
		if(!contentIdx.contains(imsiContentIdx)) {
			boardService.setBoardReadNum(idx);
			contentIdx.add(imsiContentIdx);
		}
		session.setAttribute("sContentIdx", contentIdx);
		
		// 해당글에 좋아요 버튼을 클릭하였었다면 '좋아요세션'에 아이디를 저장시켜두었기에 찾아서 있다면 sSw값을 1로 보내어 하트색을 빨강색으로 변경유지하게한다.
		ArrayList<String> goodIdx = (ArrayList) session.getAttribute("sGoodIdx");
		if(goodIdx == null) {
			goodIdx = new ArrayList<String>();
		}
		String imsiGoodIdx = "boardGood" + idx;
		if(goodIdx.contains(imsiGoodIdx)) {
			session.setAttribute("sSw", "1"); // 로그인 사용자가 이미 좋아요를 클릭한 게시글이라면 빨간색으로 표시하기 위해 sSw에 1을 전송하고 있다.
		}
		else {
			session.setAttribute("sSw", "0");
		}
		
		// DB에서 현재 게시글에 '좋아요'가 체크되어있는지를 알아보자.
		String mid = session.getAttribute("sMid") == null ? "" : (String) session.getAttribute("sMid");
		GoodVO goodVo = boardService.getBoardGoodCheck(idx, "board", mid);
		model.addAttribute("goodVo",goodVo);
		
		// 이전글 / 다음글 가져오기
		ArrayList<BoardVO> pnVos = boardService.getPrevNext(idx);
		model.addAttribute("pnVos",pnVos);
		
		BoardVO vo = boardService.getBoardContent(idx);
		model.addAttribute("vo",vo);
		model.addAttribute("pag",pag);
		model.addAttribute("pageSize",pageSize);
		return "board/boardContent";
	}
	
	@ResponseBody
	@RequestMapping(value="/boardGood",method=RequestMethod.POST)
	public String boardGoodPost(HttpSession session,int idx) {
		// 좋아요수 증가처리하기
		String sw = "0"; // 처음 0으로 셋팅하고, 처음 좋아요 버튼을 누르면 '1'을 돌려준다., 이미 '좋아요'를 한번 눌렀으면 '0'으로 sw값을 보내준다.
		ArrayList<String> goodIdx = (ArrayList) session.getAttribute("sGoodIdx");
		if(goodIdx == null) {
			goodIdx = new ArrayList<String>();
		}
		String imsiGoodIdx = "boardGood" + idx;
		if(!goodIdx.contains(imsiGoodIdx)) {
			boardService.setBoardGood(idx);
			goodIdx.add(imsiGoodIdx);
			sw = "1";	// 처음으로 좋아요 버튼을 클릭하였기에 '1'을 반환
		}
		session.setAttribute("sGoodIdx", goodIdx);
		
		return sw; 
	}
	
	// 게시글 삭제하기
	@RequestMapping(value="/boardDelete",method=RequestMethod.GET)
	public String boardDeleteGet(int idx, int pag, int pageSize, String mid, Model model, HttpSession session) {
		// 게시글에 사진이 존재한다면 서버에 있는 사진파일을 먼저 삭제한다.
		BoardVO vo = boardService.getBoardContent(idx);
		String sMid = (String) session.getAttribute("sMid");
		if(!vo.getMid().equals(mid) || !sMid.equals("admin")) {
			return "redirect:/msg/boardDeleteNo";
		}
		if(vo.getContent().indexOf("src=\"/") != -1) boardService.imgDelete(vo.getContent());
		
		// DB에서 실제로 존재하는 게시글을 삭제처리한다.
		boardService.setBoardDeleteOk(idx);
		
		model.addAttribute("flag","?pag="+pag+"&pageSize="+pageSize);
		
		return "redirect:/msg/boardDeleteOk";
	}
	
	// 게시글 수정하기 폼
	@RequestMapping(value="boardUpdate",method=RequestMethod.GET)
	public String boardUpdateGet(Model model,int idx, int pag, int pageSize) {
		// 수정창으로 이동시에는 먼저 원본 파일의 그림 파일이 있다면, 현재 폴더(board)의 그림파일을 ckeditor 폴더로 복사시켜준다.
		BoardVO vo = boardService.getBoardContent(idx);
		if(vo.getContent().indexOf("src=\"/") != -1) boardService.imgCheckUpdate(vo.getContent());
		model.addAttribute("vo",vo);
		model.addAttribute("pag",pag);
		model.addAttribute("pageSize",pageSize);
		return "board/boardUpdate";
	}
	
	// 변경된 게시글 수정처리 (그림포함)
	@RequestMapping(value="boardUpdate",method=RequestMethod.POST)
	public String boardUpdatePost(Model model,BoardVO vo, int pag, int pageSize) {
		// 수정된 자료가 원본자료와 완전히 동일하다면 수정처리하지 않는다.
		BoardVO origVO = boardService.getBoardContent(vo.getIdx());
		// content의 내용이 조금이라도 변경된 것이 있다면 아래 내용을 수행처리 시킨다. 
		if(!origVO.getContent().equals(vo.getContent())) {
			// 실제로 수정하기 버튼을 클릭하게 되면 기존의 board폴더에 저장된 현재 content의 그림파일 모두를 삭제시킨다.
			if(origVO.getContent().indexOf("src=\"/") != -1) boardService.imgDelete(origVO.getContent());
			
			//vo.getContent()에 들어있는 파일의 경로를 'board'경로를 'ckeditor'경로로 바꿔줘야한다.
			vo.setContent(vo.getContent().replace("/data/board/", "/data/ckeditor/"));
			
			// 앞의 모든 준비가 끝나면, 파일을 처음 업로드한 것과 같은 작업을 처리한다.
			// 이 작업은 처음 게시글을 올릴때의 파일 복사 작업과 동일한 작업입니다.
			boardService.imgCheck(vo.getContent());
			
			// 파일 업로드가 끝나면 다시 경로를 수정한다. 'ckeditor' 경로를 'board' 경로로 변경시켜줘야한다. (즉, 변경된 vo.getContent()를  vo.setContent() 처리한다.)
			vo.setContent(vo.getContent().replace("/data/ckeditor/", "/data/board/"));
		}
		// 잘 정비된 vo를 DB에 Update 시켜준다.
		boardService.setBoardUpdateOk(vo);
		
		model.addAttribute("flag","?pag="+pag+"&pageSize="+pageSize);
		
		if(vo.getContent().indexOf("src=\"/") != -1) boardService.imgCheckUpdate(vo.getContent());
		model.addAttribute("vo",vo);
		model.addAttribute("pag",pag);
		model.addAttribute("pageSize",pageSize);
		return "redirect:/msg/boardUpdateOk";
	}
	
}
