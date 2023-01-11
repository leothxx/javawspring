package com.spring.javawspring;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MessageController {
	@RequestMapping(value="/msg/{msgFlag}",method=RequestMethod.GET)
	public String msgGet(@PathVariable String msgFlag, Model model, @RequestParam(value="mid",defaultValue="",required = false) String mid, 
			@RequestParam(value="pag",defaultValue="1", required = false) int pag,
			@RequestParam(value="flag",defaultValue="1", required = false) String flag) {
		if(msgFlag.equals("memberLoginOk")) {
			model.addAttribute("msg",mid+"님 어서오세요~");
			model.addAttribute("url","member/memberMain");
		}
		else if(msgFlag.equals("memberLoginNo")) {
			model.addAttribute("msg","아이디 혹은 비밀번호가 일치하지 않습니다.");
			model.addAttribute("url","member/memberLogin");
		}
		else if(msgFlag.equals("guestInputOk")) {
			model.addAttribute("msg","방명록이 작성되었습니다.");
			model.addAttribute("url","guest/guestList");
		}
		else if(msgFlag.equals("guestInputOk")) {
			model.addAttribute("msg","방명록이 작성되었습니다.");
			model.addAttribute("url","guest/guestList");
		}
		else if(msgFlag.equals("memberLogout")) {
			model.addAttribute("msg",mid+"님 로그아웃 되셨습니다.");
			model.addAttribute("url","member/memberLogin");
		}
		else if(msgFlag.equals("guestDeleteOk")) {
			model.addAttribute("msg","방명록이 삭제되었습니다.");
			model.addAttribute("url","guest/guestList");
		}
		else if(msgFlag.equals("memberJoinOk")) {
			model.addAttribute("msg","회원가입이 완료되었습니다.");
			model.addAttribute("url","member/memberLogin");
		}
		else if(msgFlag.equals("memberIdCheckNo")) {
			model.addAttribute("msg","중복된 아이디가 존재합니다.");
			model.addAttribute("url","member/memberJoin");
		}
		else if(msgFlag.equals("memberNickNameCheckNo")) {
			model.addAttribute("msg","중복된 닉네임이 존재합니다.");
			model.addAttribute("url","member/memberJoin");
		}
		else if(msgFlag.equals("adminNo")) {
			model.addAttribute("msg","관리자만 입장 가능합니다.");
			model.addAttribute("url","member/memberMain");
		}
		else if(msgFlag.equals("memberNo")) {
			model.addAttribute("msg","로그인 후 이용 가능합니다.");
			model.addAttribute("url","member/memberLogin");
		}
		else if(msgFlag.equals("levelCheckNo")) {
			model.addAttribute("msg","회원 등급 확인 후 이용하세요!");
			model.addAttribute("url","member/memberMain");
		}
		else if(msgFlag.equals("mailSendOk")) {
			model.addAttribute("msg","메일이 전송되었습니다!");
			model.addAttribute("url","study/mail/mailForm");
		}
		else if(msgFlag.equals("memberImsiPwdOk")) {
			model.addAttribute("msg","계정의 이메일로 임시 비밀번호를 발송하였습니다.");
			model.addAttribute("url","member/memberLogin");
		}
		else if(msgFlag.equals("memberImsiPwdNo")) {
			model.addAttribute("msg","아이디와 이메일 주소를 확인해주세요!");
			model.addAttribute("url","member/memberPwdSearch");
		}
		else if(msgFlag.equals("memberPwdUpdateOk")) {
			model.addAttribute("msg",mid+"님 비밀번호가 변경되었습니다!");
			model.addAttribute("url","member/memberLogin");
		}
		else if(msgFlag.equals("fileUploadOk")) {
			model.addAttribute("msg","파일이 업로드 되었습니다!");
			model.addAttribute("url","study/fileUpload/fileUploadForm");
		}
		else if(msgFlag.equals("fileUploadNo")) {
			model.addAttribute("msg","파일 업로드 실패!");
			model.addAttribute("url","study/fileUpload/fileUploadForm");
		}
		else if(msgFlag.equals("memberDeleteOk")) {
			model.addAttribute("msg","회원 탈퇴가 정상적으로 처리되었습니다.");
			model.addAttribute("url","member/memberLogin");
		}
		else if(msgFlag.equals("memberDeleteNo")) {
			model.addAttribute("msg","입력하신 정보를 다시 확인해주세요.");
			model.addAttribute("url","member/memberDelete");
		}
		else if(msgFlag.equals("boardInputOk")) {
			model.addAttribute("msg","게시물이 등록되었습니다.");
			model.addAttribute("url","board/boardList");
		}
		else if(msgFlag.equals("boardInputNo")) {
			model.addAttribute("msg","게시물이 등록되지 않았습니다.");
			model.addAttribute("url","board/boardInput");
		}
		else if(msgFlag.equals("adminMemberDeleteOk")) {
			model.addAttribute("msg","회원 탈퇴 처리 되었습니다.");
			model.addAttribute("url","admin/member/adminMemberList?pag="+pag);
		}
		else if(msgFlag.equals("adminMemberDeleteNo")) {
			model.addAttribute("msg","회원 탈퇴 처리에 실패하였습니다.");
			model.addAttribute("url","admin/member/adminMemberList?pag="+pag);
		}
		else if(msgFlag.equals("boardDeleteOk")) {
			model.addAttribute("msg","게시글이 삭제되었습니다.");
			model.addAttribute("url","board/boardList"+flag);
		}
		else if(msgFlag.equals("boardDeleteNo")) {
			model.addAttribute("msg","게시글이 삭제할 권한이 없습니다.");
			model.addAttribute("url","board/boardList"+flag);
		}
		else if(msgFlag.equals("boardUpdateOk")) {
			model.addAttribute("msg","게시글이 수정되었습니다.");
			model.addAttribute("url","board/boardList"+flag);
		}
		else if(msgFlag.equals("fileDeleteOk")) {
			model.addAttribute("msg","파일이 삭제되었습니다.");
			model.addAttribute("url","admin/file/fileList");
		}
		return "include/message";
	}
}
