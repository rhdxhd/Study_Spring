package kr.co.smart;

import java.util.Scanner;

import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import kr.co.smart.member.MemberVO;



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations= {
      "file:src/main/webapp/WEB-INF/spring/root-context.xml"})
public class DBTest {

   @Autowired @Qualifier("hanul") private SqlSession sql;
   private BCryptPasswordEncoder pwEncoder=new BCryptPasswordEncoder();
   
   
   
   @Test
   public void reset() {
	   MemberVO vo = new MemberVO();
	   Scanner sc = new Scanner(System.in);
	   System.out.println("아이디: ");
	   
	   vo.setUser_id(sc.next());
	   
	   System.out.println("비번: ");
	   vo.setUser_pw( pwEncoder.encode( sc.next()));
	   
	   sql.update("member.resetPassword", vo);
	   
	   sc.close();
   }
   

   
   
   @Test
   public void login() {
      Scanner sc=new Scanner(System.in);
      System.out.println("아이디: ");
      String user_id=sc.next();
      
      System.out.println("비번: ");
      String userpw=sc.next();
      
      System.out.printf("입력비번: %s--> 암호화비번 :%s",userpw,pwEncoder.encode(userpw));
      //문자열을 암호화 할때마다 매번 다른 결과가 나온다
      //입력문자와 암호화된 문자의 일치여부를 확인해야한다.
      MemberVO vo=sql.selectOne("member.info", user_id);
      if(vo==null) {
         System.out.println("아이디 불일치");
      }else {
         
          boolean match=pwEncoder.matches(userpw, vo.getUser_pw());
          
         if(match) {
            System.out.println(vo.getName()+"회원으로 로그인됨");
         }else {
            System.out.println("비번 불일치");
         }
      }
      
      sc.close();
   }
   
   
   @Test
   public void join() {
      //키보드로 회원가입에 필요한 정보를 입력받아 처리하기
      Scanner sc=new Scanner(System.in);
      
      MemberVO vo=new MemberVO();
     
      System.out.println("이름:");
      vo.setName(sc.next());
      
      System.out.println("아이디:");
      vo.setUser_id(sc.next());
      
      System.out.println("비번:");
      vo.setUser_pw(pwEncoder.encode(sc.next()));
      
      System.out.println("이메일:");
      vo.setEmail(sc.next());
      
      System.out.println("관리자?(Y/N)");
      vo.setRole( sc.next().toLowerCase().equals("y") ? "ADMIN" : "USER" );
      
      
      sc.close();
      int dml= sql.insert("member.joinTest",vo);
      System.out.println(dml==1?"가입성공":"가입실패");
   }
   
   @Test
   public void query_test(){
      String today=sql.selectOne("member.today"); 
      System.out.println("오늘:"+today);
      
   }
}