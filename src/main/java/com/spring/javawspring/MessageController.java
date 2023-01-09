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
	public String msgGet(@PathVariable String msgFlag, Model model, @RequestParam(value="mid",defaultValue="",required = false) String mid) {
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
		return "include/message";
	}
}
