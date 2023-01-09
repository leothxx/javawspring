package com.spring.javawspring.service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.filefilter.CanWriteFileFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.condition.RequestCondition;

import com.spring.javawspring.dao.StudyDAO;
import com.spring.javawspring.vo.GuestVO;

@Service
public class StudyServiceImpl implements StudyService {
	@Autowired
	StudyDAO studyDAO;

	@Override
	public String[] getCityStringArr(String dodo) {
		String[] strArr = new String[100];
		if(dodo.equals("서울특별시")) {
			strArr[0] = "강남구";
			strArr[1] = "서초구";
			strArr[2] = "동대문구";
			strArr[3] = "성북구";
			strArr[4] = "마포구";
			strArr[5] = "광진구";
			strArr[6] = "관악구";
			strArr[7] = "중구";
			strArr[8] = "서구";
			strArr[9] = "송파구";
		}
		else if(dodo.equals("경기도")) {
			strArr[0] = "수원시";
			strArr[1] = "이천시";
			strArr[2] = "일산시";
			strArr[3] = "시흥시";
			strArr[4] = "용인시";
			strArr[5] = "광명시";
			strArr[6] = "의정부시";
			strArr[7] = "평택시";
			strArr[8] = "안성시";
			strArr[9] = "광주시";
		}
		else if(dodo.equals("충청북도")) {
			strArr[0] = "청주시";
			strArr[1] = "충주시";
			strArr[2] = "괴산군";
			strArr[3] = "진천군";
			strArr[4] = "제천시";
			strArr[5] = "음성군";
			strArr[6] = "옥천군";
			strArr[7] = "영동군";
			strArr[8] = "단양군";
			strArr[9] = "증평군";
		}
		else if(dodo.equals("충청남도")) {
			strArr[0] = "천안시";
			strArr[1] = "병천시";
			strArr[2] = "옥산군";
			strArr[3] = "아산시";
			strArr[4] = "공주시";
			strArr[5] = "당진군";
			strArr[6] = "보령시";
			strArr[7] = "계룡시";
			strArr[8] = "논산시";
			strArr[9] = "예산군";
		}
		return strArr;
	}

	@Override
	public ArrayList<String> getCityArrayListArr(String dodo) {
		ArrayList<String> vos = new ArrayList<String>();
		if(dodo.equals("서울특별시")) {
			vos.add("강남구");
			vos.add("서초구");
			vos.add("동대문구");
			vos.add("성북구");
			vos.add("마포구");
			vos.add("광진구");
			vos.add("관악구");
			vos.add("중구");
			vos.add("서구");
			vos.add("송파구");
		}
		else if(dodo.equals("경기도")) {
			vos.add("수원시");
			vos.add("이천시");
			vos.add("일산시");
			vos.add("시흥시");
			vos.add("용인시");
			vos.add("광명시");
			vos.add("의정부시");
			vos.add("평택시");
			vos.add("안성시");
			vos.add("광주시");
		}
		else if(dodo.equals("충청북도")) {
			vos.add("청주시");
			vos.add("충주시");
			vos.add("괴산군");
			vos.add("진천군");
			vos.add("제천시");
			vos.add("음성군");
			vos.add("옥천군");
			vos.add("영동군");
			vos.add("단양군");
			vos.add("증평군");
		}
		else if(dodo.equals("충청남도")) {
			vos.add("천안시");
			vos.add("병천시");
			vos.add("옥산군");
			vos.add("아산시");
			vos.add("공주시");
			vos.add("당진군");
			vos.add("보령시");
			vos.add("계룡시");
			vos.add("논산시");
			vos.add("예산군");
		}
		return vos;
	}

	@Override
	public GuestVO getGuestMid(String mid) {
		return studyDAO.getGuestMid(mid);
	}

	@Override
	public ArrayList<GuestVO> getGuestNames(String mid, String category) {
		return studyDAO.getGuestNames(mid,category);
	}

	@Override
	public int fileUpload(MultipartFile fName) {
		int res = 0;
		try {
			UUID uuid = UUID.randomUUID();
			String oFileName = fName.getOriginalFilename();
			String saveFileName = uuid + "_" + oFileName; // 랜덤된 이름과 파일명의 구분을 '_'로 준다.
			
			writeFile(fName, saveFileName);
			res = 1;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return res;
	}

	public void writeFile(MultipartFile fName, String saveFileName) throws IOException {
		byte[] data = fName.getBytes();
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		//request.getRealPath("/resources/pds/temp/");
		String realPath = request.getSession().getServletContext().getRealPath("/resources/pds/temp/");
		
		FileOutputStream fos = new FileOutputStream(realPath + saveFileName);
		fos.write(data);
		fos.close();
	}
	
}
