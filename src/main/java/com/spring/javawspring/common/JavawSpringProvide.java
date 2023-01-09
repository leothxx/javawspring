package com.spring.javawspring.common;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

public class JavawSpringProvide {
	public int fileUpload(MultipartFile fName, String flag) {
		int res = 0;
		try {
			UUID uuid = UUID.randomUUID();
			String oFileName = fName.getOriginalFilename();
			String saveFileName = uuid + "_" + oFileName; // 랜덤된 이름과 파일명의 구분을 '_'로 준다.
			
			writeFile(fName, saveFileName, flag);
			res = 1;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return res;
	}

	public void writeFile(MultipartFile fName, String saveFileName, String flag) throws IOException {
		byte[] data = fName.getBytes();
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		//request.getRealPath("/resources/pds/temp/");
		String realPath = request.getSession().getServletContext().getRealPath("/resources/"+flag+"/"); // 무조건 폴더명 맞게 넘겨줘야한다.
		
		FileOutputStream fos = new FileOutputStream(realPath + saveFileName);
		fos.write(data);
		fos.close();
	}
}
