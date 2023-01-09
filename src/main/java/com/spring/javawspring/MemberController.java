package com.spring.javawspring;

import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.spring.javawspring.pagination.PageProcess;
import com.spring.javawspring.pagination.PageVO;
import com.spring.javawspring.service.MemberService;
import com.spring.javawspring.vo.MemberVO;

@Controller
@RequestMapping("/member")
public class MemberController {
	
	@Autowired
	MemberService memberService;
	
	@Autowired
	BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	PageProcess pageProcess;
	
	@RequestMapping(value="/memberLogin",method=RequestMethod.GET)
	public String memberLoginGet(HttpServletRequest request) {
		// 로그인 폼 호출시에 기존에 저장된 쿠키가 있다면, 불러와서 mid에 담아서 넘겨준다.
		Cookie[] cookies = request.getCookies();
		for(int i=0; i<cookies.length; i++) {
			if(cookies[i].getName().equals("cMid")) {
				request.setAttribute("mid", cookies[i].getValue());
				break;
			}
		}
		return "member/memberLogin";
	}
	
	@RequestMapping(value="/memberLogin",method=RequestMethod.POST)
	public String memberLoginPost(HttpServletRequest request, HttpServletResponse response ,HttpSession session,
			@RequestParam(name="mid",defaultValue = "", required = false) String mid,
			@RequestParam(name="pwd",defaultValue = "", required = false) String pwd,
			@RequestParam(name="idCheck",defaultValue = "", required = false) String idCheck) {
		
		MemberVO vo = memberService.getMemberIdCheck(mid);
		if(vo != null && passwordEncoder.matches(pwd, vo.getPwd()) && vo.getUserDel().equals("NO")) {
			// 회원 인증 처리된 경우 수행할 내용 ? 0.strLevel처리, 1.주요필드를 세션에 저장, 2.오늘방문횟수처리, 3.방문수와 방문포인트증가, 4.쿠키에 아이디저장유무
			// 0.
			String strLevel = "";
			if(vo.getLevel() == 0) strLevel = "관리자";
			else if(vo.getLevel() == 1) strLevel = "운영자";
			else if(vo.getLevel() == 2) strLevel = "우수회원";
			else if(vo.getLevel() == 3) strLevel = "정회원";
			else if(vo.getLevel() == 4) strLevel = "준회원";
			// 1.
			session.setAttribute("sLevel", vo.getLevel());
			session.setAttribute("sStrLevel", strLevel);
			session.setAttribute("sMid", vo.getMid());
			session.setAttribute("sNickName", vo.getNickName());
			session.setAttribute("sKey", 0);
			// 2~3. 로그인한 사용자의 방문횟수(포인트) 누적
			memberService.setMemberVisitProcess(vo);
			// 4.
			if(idCheck.equals("on")) {
				Cookie cookie = new Cookie("cMid", mid);
				cookie.setMaxAge(60*60*24*7);
				response.addCookie(cookie);
			}
			else {
				Cookie[] cookies = request.getCookies();
				for(int i=0; i<cookies.length; i++) {
					if(cookies[i].getName().equals("cMid")) {
						cookies[i].setMaxAge(0);
						response.addCookie(cookies[i]);
					}
				}
			}
		
		//session.setAttribute("sAMid", "admin");
		return "redirect:/msg/memberLoginOk?mid="+mid;
		}
		else {
			return "redirect:/msg/memberLoginNo";
		}
	}
	
	@RequestMapping(value="/memberMain",method=RequestMethod.GET)
	public String memberMainGet(Model model, HttpSession session) {
		String mid = session.getAttribute("sMid") == null ? "" : (String) session.getAttribute("sMid");
		MemberVO vo = memberService.getMemberIdCheck(mid);
		model.addAttribute("vo", vo);
		return "member/memberMain";
	}
	
	@RequestMapping(value="/memberLogout", method=RequestMethod.GET)
	public String memberLogoutGet(HttpSession session) {
		String mid = session.getAttribute("sMid") == null ? "" : (String) session.getAttribute("sMid");
		session.invalidate();
		return "redirect:/msg/memberLogout?mid="+mid;
	}
	
	@RequestMapping(value="/adminLogout",method=RequestMethod.GET)
	public String adminLogoutGet(HttpSession session) {
		String mid = (String) session.getAttribute("sAMid");
		session.invalidate();
		return "redirect:/msg/memberLogout?mid="+mid;
	}
	
	// 회원 가입 폼
	@RequestMapping(value="/memberJoin",method=RequestMethod.GET)
	public String memberJoinGet() {
		return "member/memberJoin";
	}
	
	// 회원 가입 처리
	@RequestMapping(value="/memberJoin",method=RequestMethod.POST)
	public String memberJoinPost(MemberVO vo) {
		//System.out.println("memberVO : " + vo);
		
		//아이디 체크
		if(memberService.getMemberIdCheck(vo.getMid()) != null) {
			return "redirect:/msg/memberIdCheckNo";
		}
		//닉네임 체크
		if(memberService.getMemberNickNameCheck(vo.getNickName()) != null) {
			return "redirect:/msg/memberNickNameCheckNo";
		}
		
		// 비밀번호 암호화(BCryptPasswordEncoder)
		vo.setPwd(passwordEncoder.encode(vo.getPwd()));
		
		// 체크가 완료되면 vo에 담긴 자료를 DB에 저장시켜준다 (회원가입)
		int res = memberService.setMemberJoinOk(vo);
		
		if(res == 1) return "redirect:/msg/memberJoinOk";
		else return "redirect:/msg/memberJoinNo";
	}
	
	@ResponseBody
	@RequestMapping(value="/memberIdCheck",method=RequestMethod.POST)
	public String memberIdCheckPost(String mid) {
		String res = "0";
		MemberVO vo = memberService.getMemberIdCheck(mid);
		if(vo != null) res = "1";
		return res;
	}
	
	@ResponseBody
	@RequestMapping(value="/memberNickNameCheck",method=RequestMethod.POST)
	public String memberNickNameCheckPost(String nickName) {
		String res = "0";
		MemberVO vo = memberService.getMemberNickNameCheck(nickName);
		if(vo != null) res = "1";
		return res;
	}
	
	// 회원 리스트 폼
	/*
	@RequestMapping(value="/memberList",method=RequestMethod.GET)
	public String memberListGet(Model model,
			@RequestParam(name="pag", defaultValue="1", required = false) int pag,
			@RequestParam(name="pagSize", defaultValue="3", required = false) int pageSize) {
		
		int totRecCnt = memberService.totRecCnt(); // 3. 총 레코드 건수를 구한다.
		
		// 4. 총 페이지 건수를 구한다.
		int totPage = (totRecCnt % pageSize)==0 ? totRecCnt / pageSize : (totRecCnt / pageSize) + 1;
		
		// 5. 현재페이지의 시작 인덱스번호를 구한다.
		int startIndexNo = (pag - 1) * pageSize;
		
		// 6. 현재 화면에 보여주는 시작번호를 구한다.
		int curScrStartNo = totRecCnt - startIndexNo;
		
		// 블록페이징처리.....(3단계) -> 블록의 시작번호를 0번부터 처리했다.
		// 1. 블록의 크기를 결정한다.(여기선 3으로 지정)
		int blockSize = 3;
		
		// 2. 현재페이지가 위치하고 있는 블록 번호를 구한다.(예:1페이지는 0블록, 3페이지는 0블록, 5페이지는 1블록)
		// int curBlock = (pag % blockSize)==0 ? (pag / blockSize) - 1 : (pag / blockSize);
		int curBlock = (pag - 1) / blockSize;
		
		// 3. 마지막블록을 구한다.
		// int lastBlock = (totPage % blockSize)==0 ? (totPage / blockSize) - 1 : (totPage / blockSize);
		int lastBlock = (totPage - 1) / blockSize;
		
		// ArrayList<GuestVO> vos = dao.getGuestList();
		ArrayList<MemberVO> vos = memberService.getMemberList(startIndexNo, pageSize);
		
		model.addAttribute("vos", vos);
		model.addAttribute("pag", pag);
		model.addAttribute("totPage", totPage);
		model.addAttribute("curScrStartNo", curScrStartNo);
		model.addAttribute("blockSize", blockSize);
		model.addAttribute("curBlock", curBlock);
		model.addAttribute("lastBlock", lastBlock);
	
		model.addAttribute("vos",vos);
		return "member/memberList";
	}
	*/
	// 전체 리스트와 검색 리스트를 하나의 메소드로 처리.. --> 동적 쿼리
	/*
	@RequestMapping(value="/memberList",method=RequestMethod.GET)
	public String memberListGet(Model model,
			@RequestParam(name="mid", defaultValue="", required = false) String mid,
			@RequestParam(name="pag", defaultValue="1", required = false) int pag,
			@RequestParam(name="pagSize", defaultValue="3", required = false) int pageSize) {
		
		int totRecCnt = memberService.totTermRecCnt(mid); // 3. 총 레코드 건수를 구한다.
		
		// 4. 총 페이지 건수를 구한다.
		int totPage = (totRecCnt % pageSize)==0 ? totRecCnt / pageSize : (totRecCnt / pageSize) + 1;
		
		// 5. 현재페이지의 시작 인덱스번호를 구한다.
		int startIndexNo = (pag - 1) * pageSize;
		
		// 6. 현재 화면에 보여주는 시작번호를 구한다.
		int curScrStartNo = totRecCnt - startIndexNo;
		
		// 블록페이징처리.....(3단계) -> 블록의 시작번호를 0번부터 처리했다.
		// 1. 블록의 크기를 결정한다.(여기선 3으로 지정)
		int blockSize = 3;
		
		// 2. 현재페이지가 위치하고 있는 블록 번호를 구한다.(예:1페이지는 0블록, 3페이지는 0블록, 5페이지는 1블록)
		// int curBlock = (pag % blockSize)==0 ? (pag / blockSize) - 1 : (pag / blockSize);
		int curBlock = (pag - 1) / blockSize;
		
		// 3. 마지막블록을 구한다.
		// int lastBlock = (totPage % blockSize)==0 ? (totPage / blockSize) - 1 : (totPage / blockSize);
		int lastBlock = (totPage - 1) / blockSize;
		
		// ArrayList<GuestVO> vos = dao.getGuestList();
		ArrayList<MemberVO> vos = memberService.getTermMemberList(startIndexNo, pageSize,mid);
		
		model.addAttribute("vos", vos);
		model.addAttribute("pag", pag);
		model.addAttribute("totPage", totPage);
		model.addAttribute("curScrStartNo", curScrStartNo);
		model.addAttribute("blockSize", blockSize);
		model.addAttribute("curBlock", curBlock);
		model.addAttribute("lastBlock", lastBlock);
		model.addAttribute("mid", mid);
		model.addAttribute("totRecCnt", totRecCnt);
		
		model.addAttribute("vos",vos);
		return "member/memberList";
	}
	*/
	// Pagination 이용하기.
	@RequestMapping(value="/memberList",method=RequestMethod.GET)
	public String memberListGet(Model model,
			@RequestParam(name="mid", defaultValue="", required = false) String mid,
			@RequestParam(name="pag", defaultValue="1", required = false) int pag,
			@RequestParam(name="pagSize", defaultValue="3", required = false) int pageSize) {
		
		PageVO pageVO = pageProcess.totRecCnt(pag, pageSize, "member", mid, ""); // 3. 총 레코드 건수를 구한다.
		
		List<MemberVO> vos = memberService.getMemberList(pageVO.getStartIndexNo(), pageVO.getPageSize());
		
		model.addAttribute("vos", vos);
		model.addAttribute("pageVO", pageVO);
		
		model.addAttribute("mid", mid);
		
		return "member/memberList";
	}
}
