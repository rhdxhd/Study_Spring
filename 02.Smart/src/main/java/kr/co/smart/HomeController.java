package kr.co.smart;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import kr.co.smart.member.MemberService;
import kr.co.smart.member.MemberVO;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@Autowired  private MemberService member;
	@Autowired  private BCryptPasswordEncoder pwEncoder;
	
	

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(HttpSession session, Model model) {
		
		//테스트하는 동안 사용할 수 있도록 임시 로그인처리-----------------------------
		String user_id="hanul201", user_pw="78c14800d373";
		MemberVO vo = member.member_info(user_id);
		if( pwEncoder.matches(user_pw, vo.getUser_pw()) ) {
			session.setAttribute("loginInfo", vo);
		}
		
		//-------------------------------------------
				
		
		session.removeAttribute("category");
		//session.setAttribute("category", "");
		return "home";
	}
	
}