package com.spring.javawspring;

import java.util.List;
import java.util.UUID;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

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
	
	@Autowired
	JavaMailSender mailSender;
	
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
	public String memberJoinPost(MultipartFile fName, MemberVO vo) {
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
		
		// 체크가 완료되면 파일 업로드 후 vo에 담긴 자료를 DB에 저장시켜준다 (회원가입) - 서비스 객체에서 수행처리
		int res = memberService.setMemberJoinOk(fName, vo);
		
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
	
	// 비밀번호를 찾기 위한 임시 비밀번호 발급 폼.
	@RequestMapping(value="/memberPwdSearch", method=RequestMethod.GET)
	public String memberPwdSearchGet() {
		return "member/memberPwdSearch";
	}
	// 비밀번호를 찾기 위한 임시 비밀번호 발급 처리
	@RequestMapping(value="/memberPwdSearch", method=RequestMethod.POST)
	public String memberPwdSearchPost(String mid, String toMail) {
		MemberVO vo = memberService.getMemberIdCheck(mid);
		
		if(vo.getEmail().equals(toMail)) {
			// 회원 정보가 맞다면 임시 비밀번호를 발급받는다.
			UUID uid = UUID.randomUUID();
			String pwd = uid.toString().substring(0, 8);
			
			// 발급받은 임시 비밀번호를 암호화 처리 시켜, DB에 저장한다.
			memberService.setMemberPwdUpdate(mid, passwordEncoder.encode(pwd));
			
			// 임시 비밀번호를 메일로 전송처리한다.
			String res = mailSend(toMail, pwd);
			
			if(res.equals("1")) return "redirect:/msg/memberImsiPwdOk";
			else return "redirect:/msg/memberImsiPwdNo";	
		}
		return "redirect:/msg/memberImsiPwdNo";
	}
	// 비밀번호 수정 폼.
	@RequestMapping(value="/memberPwdUpdate", method=RequestMethod.GET)
	public String memberPwdUpdateGet() {
		return "member/memberPwdUpdate";
	}
	
	// 비밀번호 수정 폼.
	@RequestMapping(value="/memberPwdUpdate", method=RequestMethod.POST)
	public String memberPwdUpdatePost(HttpSession session, String newPwd, String oldPwd) {
		String mid = session.getAttribute("sMid") == null ? "" : (String) session.getAttribute("sMid");
		MemberVO vo = memberService.getMemberIdCheck(mid);
		String pwd = passwordEncoder.encode(newPwd);
		memberService.setMemberPwdUpdate(mid, pwd);
		session.invalidate();
		return "redirect:/msg/memberPwdUpdateOk?mid="+mid;
	}

	// 임시 비밀번호 메일 전송
	private String mailSend(String toMail, String pwd) {
		try {
			String title = "임시 비밀번호가 발급되었습니다.";
			String content = "";
			
			// 메일을 전송하기 위한 객체 : MimeMessage(), MimeMessageHelper() 
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8"); // 보관함 개념
			
			// 메일 보관함에 회원이 보내온 메세지들을 모두 저장시킨다.
			messageHelper.setTo(toMail); // 받는이
			messageHelper.setSubject(title); // 제목
			messageHelper.setText(content); // 내용
			
			/*
			 * content += "<br><hr><h3>- HommeElegant</h3><hr><br>"; content +=
			 * "<p><img src=\"cid:hommeElegantMain.jpg\" width='500px'/></p>"; content +=
			 * "<p>방문하기 : <a href='http://49.142.157.251:9090/green2209J_09/main.mem'>HommeElegant</a></p>";
			 */			
			content += "<br><hr><h3>- 정광맨</h3><hr><br>";
			content += "<br><hr><h3>- 임시 비밀번호는 <font color='blue'>"+pwd+"</font>입니다.</h3><hr><br>";
			content += "<p>임시 비밀번호는 비밀번호 수정 후 이용해주시기 바랍니다.<p><br>";
			content += "<p><img src=\"cid:main.jpg\" width='500px'/></p>";
			content += "<p>방문하기 : <a href='http://49.142.157.251:9090/green2209J_08/'>바로가기</a></p>";
			messageHelper.setText(content, true); // 앞에있는 내용에 추가되어 해당 내용이 다시 들어간다는 의미로 true
			
			// 본문에 기재된 그림파일의 경로를 따로 표시시켜준다. 그리고, 보관함에 다시 저장시켜준다.
			//FileSystemResource file = new FileSystemResource("D:\\JavaWorkspace\\springframework\\works\\javawspring\\src\\main\\webapp\\resources\\images\\hommeElegantMain.JPG");
			//messageHelper.addInline("hommeElegantMain.jpg", file);
			FileSystemResource file = new FileSystemResource("D:\\JavaWorkspace\\springframework\\works\\javawspring\\src\\main\\webapp\\resources\\images\\main.JPG");
			messageHelper.addInline("main.jpg", file);
			
			// 메일 전송하기
			mailSender.send(message);
			return "1";
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		return "0";
	}
}
