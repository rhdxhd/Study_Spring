package kr.co.middle;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;

import kr.co.middle.common.CommonService;
import kr.co.middle.member.MemberService;
import kr.co.middle.member.MemberVO;

@RestController @RequestMapping(value="/member", produces = "text/plain; charset=utf-8")  
public class MemberController {
	@Autowired private MemberService service;
	@Autowired private BCryptPasswordEncoder pwEncoder; 
	private CommonService common;
	
	//org.springframework.web.multipart.commons.CommonsMultipartResolver
	
	
	
	@RequestMapping("/join")
	public String join(String vo, MultipartFile andFile, HttpServletRequest request) {
//		MultipartRequest part = (MultipartRequest)request;
//		MultipartFile andFile = part.getFile("andFile");
		
		MemberVO member = new Gson().fromJson(vo, MemberVO.class);
		member.setUser_pw( pwEncoder.encode(member.getUser_pw()) );
		
		common.fileUpload("profile", andFile, request);
		
		
		if( ! andFile.isEmpty() ) {
			// http://localhost:80/file  /profile/2024/01/03/abc.jpg
		member.setProfile( common.fileUpload("profile", andFile, request)) ;
	
		}
		
		return String.valueOf( service.member_join(member) );
		
		
	}
	
	@RequestMapping("/login")
	public String login(String user_id, String user_pw, String social) {
		MemberVO vo = service.member_info(user_id);
		boolean match = false;
		if( vo != null ) {
			match = pwEncoder.matches(user_pw, vo.getUser_pw());
		}
		return new Gson().toJson(match ? vo : null);
	}

}
