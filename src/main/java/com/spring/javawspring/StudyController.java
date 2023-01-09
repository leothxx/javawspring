package com.spring.javawspring;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.spring.javawspring.common.ARIAUtil;
import com.spring.javawspring.common.SecurityUtil;
import com.spring.javawspring.service.MemberService;
import com.spring.javawspring.service.StudyService;
import com.spring.javawspring.vo.GuestVO;
import com.spring.javawspring.vo.MailVO;
import com.spring.javawspring.vo.MemberVO;

@Controller
@RequestMapping("/study")
public class StudyController {
	@Autowired
	StudyService studyService;
	
	@Autowired
	MemberService memberService;
	
	@Autowired
	BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	JavaMailSender mailSender; // autowired를 걸땐 impl class가 아닌 더 위에 있는 interface로 선언해준다.
	
	@RequestMapping(value="/ajax/ajaxMenu",method=RequestMethod.GET)
	public String ajaxMenuGet() {
		return "study/ajax/ajaxMenu";
	}
	
	// 일반 String 값의 전달 1(숫자/영문자)
	@ResponseBody // 스트링 형태의 자료를 넘겨줄때 필요한 어노테이션.
	@RequestMapping(value="/ajax/ajaxTest1_1",method=RequestMethod.POST)
	public String ajaxTest1_1Post(int idx) {
		idx = (int)(Math.random()*idx)+1;
		String res = idx + " : Have a good time!!";
		return res;
	}
	
	// 일반 String 값의 전달 2(숫자/영문자/한글)
	@ResponseBody // 스트링 형태의 자료를 넘겨줄때 필요한 어노테이션.
	@RequestMapping(value="/ajax/ajaxTest1_2",produces="application/text;charset=UTF-8",method=RequestMethod.POST)
	public String ajaxTest1_2Post(int idx) {
		idx = (int)(Math.random()*idx)+1;
		String res = idx + " : 행복한 하루 되세요!!";
		return res;
	}
	
	// 일반 배열값의 전달 폼
	@RequestMapping(value="/ajax/ajaxTest2_1",method=RequestMethod.GET)
	public String ajaxTest2_1Get() {
		return "study/ajax/ajaxTest2_1";
	}
	
	// 일반 배열값의 전달
	@ResponseBody // 스트링 형태의 자료를 넘겨줄때 필요한 어노테이션.
	@RequestMapping(value="/ajax/ajaxTest2_1",method=RequestMethod.POST)
	public String[] ajaxTest2_1Post(String dodo) {
		//String[] strArr = new String[100];
		//strArr = studyService.getCityStringArr(dodo);
		//return strArr;
		return studyService.getCityStringArr(dodo);
	}
	
	// 일반 배열값의 전달 폼
	@RequestMapping(value="/ajax/ajaxTest2_2",method=RequestMethod.GET)
	public String ajaxTest2_2Get() {
		return "study/ajax/ajaxTest2_2";
	}
	
	// 객체 배열값의 전달
	@ResponseBody // 스트링 형태의 자료를 넘겨줄때 필요한 어노테이션.
	@RequestMapping(value="/ajax/ajaxTest2_2",method=RequestMethod.POST)
	public ArrayList<String> ajaxTest2_2Post(String dodo) {
		return studyService.getCityArrayListArr(dodo);
	}
	
	// Map(HashMap<k,v>)값의 전달 폼
	@RequestMapping(value="/ajax/ajaxTest2_3",method=RequestMethod.GET)
	public String ajaxTest2_3Get() {
		return "study/ajax/ajaxTest2_3";
	}
	
	// Map(HashMap<k,v>)값의 전달
	@ResponseBody // 스트링 형태의 자료를 넘겨줄때 필요한 어노테이션.
	@RequestMapping(value="/ajax/ajaxTest2_3",method=RequestMethod.POST)
	public HashMap<Object, Object> ajaxTest2_3Post(String dodo) {
		ArrayList<String> vos = new ArrayList<String>();
		vos = studyService.getCityArrayListArr(dodo);
		HashMap<Object, Object> map = new HashMap<Object, Object>();
		map.put("city", vos);
		return map;
	}
	
	// DB를 활용한 값의 전달 폼
	@RequestMapping(value="/ajax/ajaxTest3",method=RequestMethod.GET)
	public String ajaxTest3Get() {
		return "study/ajax/ajaxTest3";
	}
	
	// DB를 활용한 값의 전달1 (vo)
	@ResponseBody
	@RequestMapping(value="/ajax/ajaxTest3_1",method=RequestMethod.POST)
	public GuestVO ajaxTest3_1Post(String mid) {
		//GuestVO vo = studyService.getGuestMid(mid);
		//return vo;
		return studyService.getGuestMid(mid);
	}
	
	// DB를 활용한 값의 전달2 (vos)
	@ResponseBody
	@RequestMapping(value="/ajax/ajaxTest3_2",method=RequestMethod.POST)
	public ArrayList<GuestVO> ajaxTest3_2Post(String mid,String category) {
		//ArrayList<GuestVO> vos = studyService.getGuestNames(mid);
		//return vos;
		return studyService.getGuestNames(mid,category);
	}
	
	// 암호화연습(sha-256)
	@RequestMapping(value="/password/sha256",method=RequestMethod.GET)
	public String sha256Get() {
		return "study/password/sha256";
	}
	
	// 암호화연습(sha-256) 결과 처리
	@ResponseBody
	@RequestMapping(value="/password/sha256",produces="application/text;charset=UTF-8",method=RequestMethod.POST)
	public String sha256Post(String pwd) {
		String res = SecurityUtil.encryptSHA256(pwd);
		pwd = "원본 비밀번호 : " + pwd + " / 암호화된 비밀번호 : " + res + "<br/>";
		return pwd;
	}
	
	// 암호화연습(aria)
	@RequestMapping(value="/password/aria",method=RequestMethod.GET)
	public String ariaGet() {
		return "study/password/aria";
	}
	
	// 암호화연습(aria) 결과 처리
	@ResponseBody
	@RequestMapping(value="/password/aria",produces="application/text;charset=UTF-8",method=RequestMethod.POST)
	public String ariaPost(String pwd) {
		String encPwd = "";
		String decPwd = "";
		try {
			encPwd = ARIAUtil.ariaEncrypt(pwd); // 암호화
			decPwd = ARIAUtil.ariaDecrypt(encPwd); // 복호화
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		pwd = "원본 비밀번호 : " + pwd + " / 암호화된 비밀번호 : " + encPwd + " / 복호화된 비밀번호 : "+decPwd+"<br/>";
		return pwd;
	}
	
	// 암호화연습(bCrypt)
	@RequestMapping(value="/password/bCrypt",method=RequestMethod.GET)
	public String bCryptGet() {
		return "study/password/bCrypt";
	}
	
	// 암호화연습(bCrypt) 결과 처리
	@ResponseBody
	@RequestMapping(value="/password/bCrypt",produces="application/text;charset=UTF-8",method=RequestMethod.POST)
	public String bCryptPost(String pwd) {
		String encPwd = "";
		encPwd = passwordEncoder.encode(pwd); // 암호화
		pwd = "원본 비밀번호 : " + pwd + " / 암호화된 비밀번호 : " + encPwd + "<br/>";
		return pwd;
	}
	
	// SMTP 메일 보내기
	// 메일 작성 폼
	@RequestMapping(value="/mail/mailForm",method=RequestMethod.GET)
	public String mailFormGet(Model model, String email) {
		List<MemberVO> vos = memberService.getMemberList(0,1000); // 페이징처리 임의로 천건
		model.addAttribute("vos",vos);
		model.addAttribute("email",email);
		return "study/mail/mailForm";
	}
	
	// 주소록 호출하기
	
	
	// 메일 전송하기
	@RequestMapping(value="/mail/mailForm",method=RequestMethod.POST)
	public String mailFormPost(MailVO vo, HttpServletRequest request) {
		try {
			String toMail = vo.getToMail();
			String title = vo.getTitle();
			String content = vo.getContent();
			
			// 메일을 전송하기 위한 객체 : MimeMessage(), MimeMessageHelper() 
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8"); // 보관함 개념
			
			// 메일 보관함에 회원이 보내온 메세지들을 모두 저장시킨다.
			messageHelper.setTo(toMail); // 받는이
			messageHelper.setSubject(title); // 제목
			messageHelper.setText(content); // 내용
			
			// 메세지 보관함의 내용(content)에 필요한 정보를 추가로 담아서 전송시킬 수 있도록 한다.
			content = content.replace("\n", "<br/>");
			/*
			 * content += "<br><hr><h3>- HommeElegant</h3><hr><br>"; content +=
			 * "<p><img src=\"cid:hommeElegantMain.jpg\" width='500px'/></p>"; content +=
			 * "<p>방문하기 : <a href='http://49.142.157.251:9090/green2209J_09/main.mem'>HommeElegant</a></p>";
			 */			
			content += "<br><hr><h3>- 정광맨</h3><hr><br>";
			content += "<p><img src=\"cid:main.jpg\" width='500px'/></p>";
			content += "<p>방문하기 : <a href='http://49.142.157.251:9090/green2209J_08/'>바로가기</a></p>";
			messageHelper.setText(content, true); // 앞에있는 내용에 추가되어 해당 내용이 다시 들어간다는 의미로 true
			
			// 본문에 기재된 그림파일의 경로를 따로 표시시켜준다. 그리고, 보관함에 다시 저장시켜준다.
			//FileSystemResource file = new FileSystemResource("D:\\JavaWorkspace\\springframework\\works\\javawspring\\src\\main\\webapp\\resources\\images\\hommeElegantMain.JPG");
			//messageHelper.addInline("hommeElegantMain.jpg", file);
			FileSystemResource file = new FileSystemResource("D:\\JavaWorkspace\\springframework\\works\\javawspring\\src\\main\\webapp\\resources\\images\\main.JPG");
			messageHelper.addInline("main.jpg", file);
			
			// 첨부파일 보내기(서버 파일시스템에 있는 파일)
			file = new FileSystemResource("D:\\JavaWorkspace\\springframework\\works\\javawspring\\src\\main\\webapp\\resources\\images\\la.jpg"); 
			messageHelper.addAttachment("la.jpg", file);
			
			file = new FileSystemResource("D:\\JavaWorkspace\\springframework\\works\\javawspring\\src\\main\\webapp\\resources\\images\\paris.jpg"); 
			messageHelper.addAttachment("paris.jpg", file);
			
			//file = new FileSystemResource(request.getRealPath("/resources/images/ny.jpg")); 
			file = new FileSystemResource(request.getSession().getServletContext().getRealPath("/resources/images/ny.jpg")); // 위에 대신 바꿔 쓸 수 있당. 
			messageHelper.addAttachment("ny.jpg", file);
			
			// 메일 전송하기
			mailSender.send(message);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		return "redirect:/msg/mailSendOk";
	}
	
	// UUID 입력폼
	@RequestMapping(value="/uuid/uuidForm",method=RequestMethod.GET)
	public String uuidFormGet() {
		return "study/uuid/uuidForm";
	}
	
	// UUID 처리하기
	@ResponseBody
	@RequestMapping(value="/uuid/uuidProcess",method=RequestMethod.POST)
	public String uuidProcessPost() {
		UUID uid = UUID.randomUUID(); // java util에 있는거 바로 그냥 사용이 가능함.
		return uid.toString();
	}
	
	// 파일 업로드 폼
	@RequestMapping(value="/fileUpload/fileUploadForm",method=RequestMethod.GET)
	public String fileUploadFormGet() {
		return "study/fileUpload/fileUploadForm";
	}
	
	// 파일 업로드 처리
	@RequestMapping(value="/fileUpload/fileUploadForm",method=RequestMethod.POST)
	public String fileUploadFormPost(MultipartFile fName) {
		int res = studyService.fileUpload(fName);
		if(res == 1) return "redirect:/msg/fileUploadOk";
		return "redirect:/msg/fileUploadNo";
	}
}
