package kr.co.smart;

import java.util.HashMap;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import kr.co.smart.common.CommonUtility;
import kr.co.smart.member.MemberService;
import kr.co.smart.member.MemberVO;

@Controller @RequestMapping("/member")
@PropertySource( "classpath:info.properties" )
public class MemberController {
	@Autowired private CommonUtility common;
	@Autowired private MemberService service;
	@Autowired private BCryptPasswordEncoder pwEncoder;
	
	
	//새비밀번호로 변경저장 처리 요청
	@RequestMapping("/updatePassword")
	public boolean updatePassword(String user_pw, HttpSession session) {
		//세션의 로그인정보 꺼내기
		MemberVO vo = (MemberVO)session.getAttribute("loginInfo");
		//입력 새 비번을 암호화하기
		vo.setUser_pw ( pwEncoder.encode(user_pw) );

		return service.member_resetPassword(vo) == 1 ? true : false;
		
	}
	
	
	
	
	
	//현재비밀번호 확인처리 요청
	@ResponseBody @RequestMapping("/confirmPassword")
	public int confirmPassword(String user_pw, HttpSession session) {
		//세션의 로그인정보 꺼내기
		MemberVO vo = (MemberVO)session.getAttribute("loginInfo");
		if ( vo==null ) {
			//로그인이 안되어 있다면 로그인 화면으로 연결되도록
			return -1;
		}else {
			//입력한 현재 비번이 DB의 비번과 일치하는지
			return pwEncoder.matches(user_pw, vo.getUser_pw()) ? 0 : 1;
			
		}
		
	}
	

	 
	
	
	
	//비밀번호변경화면 요청
	@RequestMapping("/changePassword")
	public String changePassword() {
		return "member/password";
	}
	
	
	//임시비밀번호 발급처리 요청
	
	@ResponseBody @RequestMapping(value="/resetPassword", produces="text/html; charset=utf-8")
	public String resetPassword(MemberVO vo) {
		
		vo = service.member_userid_email(vo);
		
		StringBuffer msg = new StringBuffer("<script>");
		
		if ( vo == null ) {
			msg.append( "alert('아이디나 이메일이 맞지 않습니다. \\n확인하세요!'); " );
			msg.append( "location='findPassword'" );
		}else {
		
			//화면에서 입력한 아이디와 이메일이 일치하는 회원에게
			//임시비밀번호를 발급해 이메일로 보내기
			String pw = UUID.randomUUID().toString(); //afdh1324_dfsfs548ef_ahlh421er
			pw = pw.substring( pw.lastIndexOf("-")+1 );    //ahlh421er
			vo.setUser_pw(pwEncoder.encode(pw) );
			
			if( service.member_resetPassword(vo)==1 && common.sendPassword(vo, pw) ) {
				msg.append( "alert('임시 비밀번호가 발급되었습니다. \\n이메일을 확인하세요.'); " );
				msg.append( "location='login' " );
			
			}else {
				msg.append( "alert('임시 비밀번호 발급 실패ㅠㅠ'); ");
				msg.append( "history.go(-1) ");
			}
			
		}
		msg.append("</script>");
		return msg.toString();
	}
	
	
	//비밀번호찾기 화면 요청
	@RequestMapping("/findPassword")
	public String findPassword(HttpSession session) {
		session.setAttribute("category", "find");
		return "default/member/find";
	}
	
	
	
	//회원가입화면 요청
	@RequestMapping("/join")
	public String join(HttpSession session) {
		session.setAttribute("category", "join");
		return "member/join";
	}
	
	
	//아이디 중복확인 요청
	@RequestMapping("/idCheck")
	public boolean idCheck(String user_id) {
		//아이디사용가능true : 아이디사용중false
		return service.member_info(user_id)== null ? true : false;
	}
	
	
	
	//회원가입처리 요청
	@ResponseBody @RequestMapping( value= "/register", produces = "text/html charset=utf-8")
	public String join(MemberVO vo, HttpServletRequest request, MultipartFile file) {
		//프로필이미지 파일 첨부한 경우
		if( ! file.isEmpty() )  {
			//common
			
		}
		
		
		//화면에서 입력한 정보로 DB에 신규회원정보를 저장 -> 웰컴페이지로 연결
		vo.setUser_pw( pwEncoder.encode(vo.getUser_pw()) );
		
		StringBuffer msg = new StringBuffer("<script>");
		if ( service.member_join(vo) == 1 ) {
			 msg.append( "alert('회원가입을 축하합니다 ^^'); location='")
				.append( request.getContextPath() ).append("' ");
			
		}else {
			msg.append( "alert('회원가입 실패 ㅠㅠ'); history.go(-1); ");
		}
		
		msg.append("<script>");
		return msg.toString();
	}
	
	
	
	
	
	
	
	//로그아웃 처리 요청
	@RequestMapping("/logout")
	public String logout(HttpSession session, HttpServletRequest request) {
		
		//https://kauth.kakao.com/oauth/logout
		//client_id=${YOUR_REST_API_KEY}
		//&logout_redirect_uri=${YOUR_LOGOUT_REDIRECT_URI}"
		MemberVO vo = (MemberVO)session.getAttribute("loginInfo");
		//세션에 있는 로그인정보 삭제후 웰컴페이지로 연결
		session.removeAttribute("loginInfo");
		
		String social = vo.getSocial();
		//if(social != null && social.equal("K") ) {
		
		if ( "K".equals(social)) {	
			StringBuffer url = new StringBuffer( "https://kauth.kakao.com/oauth/logout") ;
			url.append("client_id=").append( KAKAO_CLIENT_ID );
			url.append("&logout_redirect_uri=").append(common.appURL(request));
			return "redirect:"	+ url.toString();
		}else
			return "redirect:/";
	}
		
	
	
	
	
	
	
	
	
	
	@Value("${KAKAO_CLIENT_ID}") private String KAKAO_CLIENT_ID;
	@Value("${NAVER_CLIENT_ID}") private String NAVER_CLIENT_ID;
	@Value("${NAVER_SECRET}") private String NAVER_SECRET;


	
	
	
	
	
	
	
	//네이버로그인요청
	@RequestMapping("/naverLogin")
	public String naverlogin(HttpSession session, HttpServletRequest request) {
		// 네이버 로그인 연동 URL 생성하기
		// https://nid.naver.com/oauth2.0/authorize?response_type=code
		// &client_id=CLIENT_ID
		// &state=STATE_STRING
		// &redirect_uri=CALLBACK_URL
		String state = UUID.randomUUID().toString();
		session.setAttribute("state", state);
	
	
	StringBuffer url = new StringBuffer("https://nid.naver.com/oauth2.0/authorize?response_type=code");
	url.append("&client_id=").append( NAVER_CLIENT_ID);
	url.append("&state=").append( state );
	url.append("&redirect_uri=").append(common.appURL(request)).append("/member/naverCallback");  //  http://localhost:80/smart/member/naverCallback
	return "redirect:" + url.toString();
	
}
	
	
	
	
	
	
	
	
	
	
	//카카오로그인요청
		@RequestMapping("/kakaoLogin")
		public String kakaologin(HttpSession session, HttpServletRequest request) {
			
			//인가 코드 받기
			// https://kauth.kakao.com/oauth/authorize?response_type=code
			// &client_id=${REST_API_KEY}
			// &redirect_uri=${REDIRECT_URI}
			
		StringBuffer url = new StringBuffer("https://kauth.kakao.com/oauth/authorize?response_type=code");
		url.append("&client_id=").append( KAKAO_CLIENT_ID);
		url.append("&redirect_uri=").append(common.appURL(request)).append("/member/kakaoCallback");  
		return "redirect:" + url.toString();
		
	}
		
		
		
		
	
		
	
	
	
	
	@RequestMapping("/naverCallback")
	public String naverCallback(String state, String code, HttpSession session) {
		if( code == null ) return "redirect:/";
		
//      if( code != null) {
//		}
		
		//https://nid.naver.com/oauth2.0/token?grant_type=authorization_code
		//&client_id=jyvqXeaVOVmV
		//&client_secret=527300A0_COq1_XV33cf
		//&code=EIc5bFrl4RibFls1
		//&state=9kgsGTfH4j7IyAkg  
		
		
		//접근 토큰 발급 요청
		StringBuffer url = new StringBuffer ( "https://nid.naver.com/oauth2.0/token?grant_type=authorization_code");
		url.append("&client_id=").append( NAVER_CLIENT_ID );
		url.append("&client_secret=").append( NAVER_SECRET );
		url.append("&code=").append( code );
		url.append("&state=").append( state );
		String response = common.requestAPI(url.toString());

		
		HashMap<String, String> map
			= new Gson().fromJson( response, new TypeToken<HashMap<String, String>>(){}.getType() );
		String token = map.get("access_token");
		String type = map.get("token_type");
		
//		{
//		    "access_token": "AAAAQosjWDJieBiQZc3to9YQp6HDLvrmyKC+6+iZ3gq7qrkqf50ljZC+Lgoqrg",
//		    "token_type": "bearer",
//		}


		//접근 토큰을 이용하여 프로필 API 호출하기
		response = common.requestAPI("https://openapi.naver.com/v1/nid/me", type + " " +token);
	
		JSONObject json = new JSONObject ( response );
		
		if( json.getString("resultcode").equals("00") ) {
			json = json.getJSONObject( "response" );
		
			String email = json.getString("email");
			String gender = json.getString("gender").equals("F") ? "여" : "남";  // F/M -> 여/남
			//female  / male
			String id = json.getString("id");
			String name = json.getString("name");
			String profile = json.getString("profile_image");
			
			MemberVO member = new MemberVO();
			member.setSocial("N"); //N:네이버, K:카카오
			member.setUser_id(id);
			member.setName(name);
			member.setEmail(email);
			member.setGender(gender);
			member.setProfile(profile);
			member.setPhone(json.getString("mobile"));

				
			//네이버로그인이 처음인 경우: insert, 아니면 update
			if( service.member_info(id) == null ) {
				service.member_join(member);
			}else {
				service.member_update(member);
			}
			session.setAttribute("loginInfo", member);
		}
		
		
//		{
//			  "resultcode": "00",
//			  "message": "success",
//			  "response": {
//			    "email": "openapi@naver.com",
//			    "nickname": "OpenAPI",
//			    "profile_image": "https://ssl.pstatic.net/static/pwe/address/nodata_33x33.gif",
//			    "age": "40-49",
//			    "gender": "F",
//			    "id": "32742776",
//			    "name": "오픈 API",
//			    "birthday": "10-01"
//			  }
//			}
		
		
		
		
		return "redirect:/";
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@RequestMapping("/kakaoCallback")
	public String kakaoCallback( String code, HttpSession session) {
		if( code == null ) return "redirect:/";
		
		
//		curl -v -X POST "https://kauth.kakao.com/oauth/token" \
//		 -H "Content-Type: application/x-www-form-urlencoded" \
//		 -d "grant_type=authorization_code" \
//		 -d "client_id=${REST_API_KEY}" \
//		 --data-urlencode "redirect_uri=${REDIRECT_URI}" \
//		 -d "code=${AUTHORIZE_CODE}"
		
	
		
		//https://kauth.kakao.com/oauth/token?grant_type=authorization_code
		//&client_id=${REST_API_KEY}
		//&code=${AUTHORIZE_CODE}
	
	
		StringBuffer url = new StringBuffer ( "https://kauth.kakao.com/oauth/token?grant_type=authorization_code");
		url.append("&client_id=").append( KAKAO_CLIENT_ID );
		url.append("&code=").append( code );
		
		String response = common.requestAPI(url.toString());
		JSONObject json = new JSONObject(response);
		String token_type = json.getString("token_type");
		String access_token = json.getString("access_token");
		
//		{
//		    "access_token": "AAAAQosjWDJieBiQZc3to9YQp6HDLvrmyKC+6+iZ3gq7qrkqf50ljZC+Lgoqrg",
//		    "token_type": "bearer",
//		}

		
		
		
		
//		{
//	    "id":123456789,
//	    "connected_at": "2022-04-11T01:45:28Z",
//	    "kakao_account": { 
//	        // 프로필 또는 닉네임 동의항목 필요
//	        "profile_nickname_needs_agreement	": false,
//	        // 프로필 또는 프로필 사진 동의항목 필요
//	        "profile_image_needs_agreement	": false,
//	        "profile": {
//	            // 프로필 또는 닉네임 동의항목 필요
//	            "nickname": "홍길동",
//	            // 프로필 또는 프로필 사진 동의항목 필요
//	            "thumbnail_image_url": "http://yyy.kakao.com/.../img_110x110.jpg",
//	            "profile_image_url": "http://yyy.kakao.com/dn/.../img_640x640.jpg",
//	            "is_default_image":false
//	        },
//	        // 이름 동의항목 필요
//	        "name_needs_agreement":false, 
//	        "name":"홍길동",
//	        // 카카오계정(이메일) 동의항목 필요
//	        "email_needs_agreement":false, 
//	        "is_email_valid": true,   
//	        "is_email_verified": true,
//	        "email": "sample@sample.com",
//	        // 연령대 동의항목 필요
//	        "age_range_needs_agreement":false,
//	        "age_range":"20~29",
//	        // 출생 연도 동의항목 필요
//	        "birthyear_needs_agreement": false,
//	        "birthyear": "2002",
//	        // 생일 동의항목 필요
//	        "birthday_needs_agreement":false,
//	        "birthday":"1130",
//	        "birthday_type":"SOLAR",
//	        // 성별 동의항목 필요
//	        "gender_needs_agreement":false,
//	        "gender":"female",
//	        // 카카오계정(전화번호) 동의항목 필요
//	        "phone_number_needs_agreement": false,
//	        "phone_number": "+82 010-1234-5678",   
//	        // CI(연계정보) 동의항목 필요
//	        "ci_needs_agreement": false,
//	        "ci": "${CI}",
//	        "ci_authenticated_at": "2019-03-11T11:25:22Z",
//	    },
//	    "properties":{
//	        "${CUSTOM_PROPERTY_KEY}": "${CUSTOM_PROPERTY_VALUE}",
//	        ...
//	    },
//	    "for_partner": {
//	        "uuid": "${UUID}"
//	    }
//	}
		
		response = common.requestAPI("https://kapi.kakao.com/v2/user/me", token_type + " " +access_token);
		json = new JSONObject(response);
		if( ! json.isEmpty() ) {
			String user_id = String.valueOf( json.getLong( "id" ));
			json = json.getJSONObject( "kakao_account" );
			//String name = json.has( "name" ) ? json.getString( "name" ) : "";
			String name = haskey(json, "name");
			String email = haskey(json, "email");
			String gender = haskey(json, "gender", "남");
			String phone_number = haskey(json, "phone_number");
		
			
			json = json.getJSONObject( "profile" );
			String profile = haskey(json, "profile_image_url" );
			if ( name.isEmpty() ) {
				name = haskey( json, "nickname", "무명씨" );
			}
			
			MemberVO vo = new MemberVO();
			vo.setSocial("K");
			vo.setEmail(email);
			vo.setGender(gender);
			vo.setName(name);
			vo.setPhone(phone_number);
			vo.setProfile(profile);
			vo.setUser_id(user_id);
			
		
			//카카오로그인이 처음이면 insert 아니면 update
			if(service.member_info( user_id ) == null ) {
				service.member_join(vo);
			}else 
				service.member_update(vo);
			
			session.setAttribute("loginInfo", vo);
		}
		
		return "redirect:/";	
	}
	
	
	
	private String haskey( JSONObject json, String key) {
		return json.has(key) ? json.getString(key) : "";
	}
	
	private String haskey( JSONObject json, String key, String defaultValue ) {
		return json.has(key) ? json.getString(key) : defaultValue;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//로그인 처리 요청
	@ResponseBody @RequestMapping(value="/smartLogin", produces= "text/html; charset=utf-8")
	public String login(HttpServletRequest request, String user_id, String user_pw, HttpSession session) {
		//화면에서 입력한 아이디/비번이 일치하는 회원정보를 조회
		MemberVO vo = service.member_info(user_id);
		
		StringBuffer msg = new StringBuffer("<script>");
		boolean match = false;
		if( vo != null ) 
			match = pwEncoder.matches(user_pw, vo.getUser_pw());
		
				
		if( match ) {
			session.setAttribute("loginInfo", vo); //세션에 로그인정보 담기
			msg.append("location='"). append( common.appURL(request) ) .append("'");
		}else {
			msg.append("alert('아이디 비밀번호가 일치하지 않습니다'); history.go(-1)");
		}
		
			
		/*
		if( vo == null ) { 
			로그인 되지 않은 경우
  			msg.append("alert('아이디 비밀번호가 일치하지 않습니다'); history.go(-1)");
   		}else {	
			//입력문자와 암호화된 문자의 일치여부를 확인
			boolean match = pwEncoder.matches(user_pw, vo.getUser_pw());
	    	if( match ) {			
    	    	로그인 된 경우 - 웰컴페이지로 연결
		        msg.append("location='"). append( common.appURL(request) ) .append("'"); //location=''
			}else{
		        msg.append("alert('아이디 비밀번호가 일치하지 않습니다'); history.go(-1)");
			}
		}
		*/
				
		msg.append("</script>");
		
		return msg.toString();
		
	}
	
	
	//로그인 화면 요청
	@RequestMapping("/login")
	public String login(HttpSession session) {
		session.setAttribute("category", "login");
		return "default/member/login";
	}
	
}