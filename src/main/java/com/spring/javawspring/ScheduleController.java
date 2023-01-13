package com.spring.javawspring;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.spring.javawspring.service.ScheduleService;
import com.spring.javawspring.vo.ScheduleVO;

@Controller
@RequestMapping("/schedule")
public class ScheduleController {
	
	@Autowired
	ScheduleService scheduleService;
	
	
	@RequestMapping(value="/schedule",method=RequestMethod.GET)
	public String scheduleGet() {
		scheduleService.getSchedule();
		return "schedule/schedule";
	}
	
	@RequestMapping(value="/scheduleMenu",method=RequestMethod.GET)
	public String scheduleMenuGet(HttpSession session, String ymd, Model model) {
		String mid = (String) session.getAttribute("sMid");
		List<ScheduleVO> vos = scheduleService.getScheduleMenu(mid,ymd);
		model.addAttribute("vos", vos);
		model.addAttribute("ymd", ymd);
		model.addAttribute("scheduleCnt", vos.size());
		return "schedule/scheduleMenu";
	}
	
	@ResponseBody
	@RequestMapping(value="/scheduleInputOk",method=RequestMethod.POST)
	public String scheduleInputOkPost(ScheduleVO vo) {
		int res = 0;
		res = scheduleService.setScheduleInputOk(vo);
		return res+"";
	}
	
	@ResponseBody
	@RequestMapping(value="/scheduleUpdateOk",method=RequestMethod.POST)
	public String scheduleUpdateOkPost(ScheduleVO vo) {
		int res = 0;
		res = scheduleService.setScheduleUpdateOk(vo);
		return res+"";
	}
	
	@ResponseBody
	@RequestMapping(value="/scheduleDeleteOk",method=RequestMethod.POST)
	public String scheduleDeleteOkPost(int idx) {
		int res = 0;
		res = scheduleService.setScheduleDeleteOk(idx);
		return res+"";
	}
}
