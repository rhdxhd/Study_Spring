package kr.co.smart.common;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.mail.HtmlEmail;
import org.springframework.stereotype.Service;

import kr.co.smart.member.MemberVO;

@Service
public class CommonUtility {
	
	private String EMAIL = "c@naver.com"; //관리자의 이메일주소
	
	private void connectMailServer( HtmlEmail mail ) {
		mail.setDebug(true);
		mail.setCharset("utf-8");
		
		mail.setHostName( "smtp.naver.com" );
		mail.setAuthentication(EMAIL, "실제비밀번호"); //관리자의 이메일주소, 해당 이메일의 비번
		mail.setSSLOnConnect(true); //로그인버튼 클릭
	}
	
	
	// 임시 비밀번호를 이메일로 보내기
	public boolean sendPassword(MemberVO vo, String pw) {
		boolean send = true;
		
		HtmlEmail mail = new HtmlEmail();
		connectMailServer( mail ); //메일서버에 연결하기(메일 아이디/비번 입력후 로그인)
		
		try {
			mail.setFrom( EMAIL, "스마트 IoT 관리자" ); //메일 보내는이 정보
			mail.addTo(vo.getEmail(), vo.getName() );  // 메일 수신인 정보
			
			//메일 제목
			mail.setSubject("스마트 IoT 로그인 임시비밀번호 발급");
			//메일 내용
			StringBuffer content = new StringBuffer();
			content.append("<h3>") .append(vo.getName()) .append("임시비밀번호가 발급되었습니다</h3>");
			content.append("<div>아이디: ") .append( vo.getUser_id() ) .append("</div>");
			content.append("<div>임시 비밀번호: <strong>") .append(pw) .append("</strong> </div>");
			content.append("<div>발급된 임시 비밀번호로 로그인 후 비밀번호를 변경하세요</div>");
			mail.setHtmlMsg( content.toString());
			
			mail.send(); //메일 보내기 버튼 클릭
			
		
		}catch(Exception e) {
			System.out.println(e.getMessage());
			send = false;
		}
		
		return send;
 	}

	// 웹브라우저 요청의 루트반환
	public String appURL(HttpServletRequest request) {
		StringBuffer url = new StringBuffer("http://");
		url.append( request.getServerName()).append(":");   // http://localhost:, http://127.0.0.1:
		url.append( request.getServerPort());				// http://localhost:80, http://127.0.0.1:8080
		url.append( request.getContextPath());				// http://localhost:80/samrt, http://127.0.0.1:8080/web
		
		return url.toString();
	}
	
}
