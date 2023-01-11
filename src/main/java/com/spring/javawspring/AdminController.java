package com.spring.javawspring;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.spring.javawspring.pagination.PageProcess;
import com.spring.javawspring.pagination.PageVO;
import com.spring.javawspring.service.AdminService;
import com.spring.javawspring.service.BoardService;
import com.spring.javawspring.service.MemberService;
import com.spring.javawspring.vo.MemberVO;

@Controller
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired
	AdminService adminService;
	
	@Autowired
	MemberService memberService;
	
	@Autowired
	BoardService boardService;
	
	@Autowired
	PageProcess pageProcess;
	
	@Autowired
	JavaMailSender mainSender;
	
	@RequestMapping(value="/adminMain",method=RequestMethod.GET)
	public String adminMainGet() {
		return "admin/adminMain";
	}
	
	@RequestMapping(value="/adminLeft",method=RequestMethod.GET)
	public String adminLeftGet() {
		return "admin/adminLeft";
	}
	
	@RequestMapping(value="/adminContent",method=RequestMethod.GET)
	public String adminContentGet() {
		return "admin/adminContent";
	}
	
	@RequestMapping(value = "/member/adminMemberList", method = RequestMethod.GET)
	public String adminMemberListGet(Model model,
			@RequestParam(name="mid", defaultValue = "", required = false) String mid,
			@RequestParam(name="pag", defaultValue = "1", required = false) int pag,
			@RequestParam(name="pageSize", defaultValue = "3", required = false) int pageSize) {
		
		PageVO pageVo = pageProcess.totRecCnt(pag, pageSize, "member", "", mid);
		
		List<MemberVO> vos = memberService.getMemberList(pageVo.getStartIndexNo(), pageSize, mid);
		
		model.addAttribute("vos", vos);
		model.addAttribute("pageVo", pageVo);
		
		model.addAttribute("mid", mid);
		
		return "admin/member/adminMemberList";
	}
	
	// 회원 등급 변경하기
	@ResponseBody
	@RequestMapping(value="/member/adminMemberLevel",method=RequestMethod.POST)
	public String adminMemberLevelPost(int idx, int level) {
		int res = adminService.setMemberLevelUpdate(idx,level);
		return res+"";
	}
	
	@RequestMapping(value = "/adminMemberSearch", method = RequestMethod.GET)
	public String adminMemberSearchGet(Model model,
			@RequestParam(name="mid", defaultValue = "", required = false) String mid,
			@RequestParam(name="pag", defaultValue = "1", required = false) int pag,
			@RequestParam(name="pageSize", defaultValue = "3", required = false) int pageSize) {
		
		PageVO pageVo = pageProcess.totRecCnt(pag, pageSize, "member", "", mid);
		
		List<MemberVO> vos = memberService.getMemberList(pageVo.getStartIndexNo(), pageSize, mid);
		
		model.addAttribute("vos", vos);
		model.addAttribute("pageVo", pageVo);
		
		model.addAttribute("mid", mid);
		
		return "admin/member/adminMemberList";
	}
	
	@RequestMapping(value="/adminMemberDelete",method=RequestMethod.GET)
	public String adminMemberDeleteGet(int idx, int pag) {
		int res = adminService.adminMemberDelete(idx);
		if(res == 1) return "redirect:/msg/adminMemberDeleteOk?pag="+pag;
		else return "redirect:/msg/adminMemberDeleteNo?pag="+pag;	
	}
	
	// ckeditor 폴더의 파일 리스트 보여주기
	@RequestMapping(value="/file/fileList", method = RequestMethod.GET)
	public String fileListGet(HttpServletRequest request, Model model) {
		String realPath = request.getRealPath("/resources/data/ckeditor/");
		String[] files = new File(realPath).list();
		model.addAttribute("files",files);
		return "admin/file/fileList";
	}
	
	// 선택된 파일 삭제하기
	@RequestMapping(value="/file/fileDelete", method = RequestMethod.GET)
	public String fileDeleteGet(String file, HttpServletRequest request) {
		String files[] = file.split("/");
		String deletePath = request.getRealPath("/resources/data/ckeditor/");
		for(int i=0; i<files.length; i++) {
			System.out.println(files[i]);
			fileDelete(deletePath + files[i]);
		}
		System.out.println("=============================");
		return "redirect:/msg/fileDeleteOk";
	}

	// 파일 삭제하기.
	private void fileDelete(String origFilePath) {
		File delFile = new File(origFilePath);
		if(delFile.exists()) delFile.delete(); // 파일이 있다면 삭제!
	}
}
