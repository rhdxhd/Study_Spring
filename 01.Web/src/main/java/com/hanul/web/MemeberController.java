package com.hanul.web;

import java.lang.reflect.Member;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import member.MemberVO;


@Controller
public class MemeberController {
	
	//Controller -> Parameter 접근해 DTO에 담아야 한다.
	//DTO dto = new DTO();
	//dto.setName( name )
	// ....
	// DB에 저장처리메소드 호출시 파라메터로 객체의 주소만 보낸다. 
	
	@RequestMapping("/joinData")
	public String join(Model model, MemberVO vo) { 
		model.addAttribute("vo", vo);
		model.addAttribute("method", "데이터객체 방식");
		return "member/info";
	}
	
	
	
	//화면을 통해 서브밋된 파라미터를 @RequestParam로 접근하기
	@RequestMapping("/joinParam")
	public String join(Model model, int age, @RequestParam("email") String mail, String gender, @RequestParam String name ) {
		model.addAttribute("age", age);
		model.addAttribute("email", mail);
		model.addAttribute("gender", gender);
		model.addAttribute("name", name);
		model.addAttribute("method", "@RequestParam방식");
		return "member/info";
	}
	
	
	
	//화면을 통해 서브밋된 파라미터를 HttpServletRequest로 접근하기
	@RequestMapping("/joinRequest")
	public String join(HttpServletRequest request, Model model) {
	
		String name = request.getParameter("name");
		model.addAttribute("name", name);
		model.addAttribute("gender", request.getParameter("gender"));
		model.addAttribute("email", request.getParameter("eamil"));
		
		//String -> int -> Integer.parserInt: int
		//String -> int -> Integer.valueOf: Integer
		//Integer  no1 = new Integer(10);
		//int no2 = no1; // Integer -> int: Auto Unboxing	
		
		model.addAttribute("age", Integer.valueOf(request.getParameter("age")) );
		model.addAttribute("method", "HttpServletRequest 방식");
		return "member/info";
	}
	
	
	@RequestMapping("/member")
	public String member() { 
		return "member/join"; 
	}
}
