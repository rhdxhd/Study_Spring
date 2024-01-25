package kr.co.smart;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import kr.co.smart.common.CommonUtility;
import kr.co.smart.notice.NoticeService;
import kr.co.smart.notice.NoticeVO;

@Controller @RequestMapping("/notice")
public class NoticeController {
		@Autowired private NoticeService service;
		@Autowired private CommonUtility common;
		
		
		//공지글 변경저장처리 요청
		@RequestMapping("/update")
		public String update(NoticeVO vo, MultipartFile file, HttpServletRequest request) {
			//원래 공지글정보를 조회해두자
			NoticeVO notice = service.notice_info(vo.getId());
			// vo의 filename, filepath 에 정보는 어떨때 담기는가..
			// 화면에서 파일을 첨부하는 경우
			
			//첨부파일이 없는 경우
			if(file.isEmpty()) {
				//원래부터X -> 그대로
				//원래부터O -> 그대로 : 원래 DB의 파일정보를 담아야 한다..
				if( ! vo.getFilename().isEmpty() ) {
						vo.setFilepath( notice.getFilepath() );
				}

					
			   
			
			}else {
			//첨부파일이 있는 경우
			   //원래x -> 첨부
			   //원래O -> 바꿔서 첨부
				vo.setFilename(file.getOriginalFilename());
				vo.setFilepath( common.fileUpload("notice", file, request));
			}
				
		  //화면에서 변경입력한 정보로 DB에 변경저장하기 -> 정보화면연결
		  if ( service.notice_update(vo) ==1) {
			  //원래O -> 첨부파일을 없애는 경우(삭제)
			  if( file.isEmpty() ) {
				  if( vo.getFilename().isEmpty() ) {
					  //DB에 있으면 삭제 
				  }
			  }else {
			  //원래O -> 바꿔서 첨부: 원래 첨부되어 있던 물리적파일 삭제
				  //DB에 있으면 삭제
				  common.fileDelete(notice.getFilepath(), request);
			  }
		  } 
		  return "redirect:info?id=" + vo.getId();
		}
		
		
		
		//공지글 수정화면 요청
		@RequestMapping("/modify")
		public String modify(int id, Model model) {
			//해당 공지글 정보를 DB에서 조회해와 수정화면에 출력 -> Model에 데이터 담기
			model.addAttribute("vo", service.notice_info(id));
			return "notice/modify";
		}
		
		
		//공지글 삭제처리 요청
		@RequestMapping("/delete")
		public String delete(int id, HttpServletRequest request) {
			//첨부파일을 물리적인 파일을 찾아 삭제할 수 있도록 미리 조회해둔다.
			NoticeVO vo = service.notice_info(id);
			//해당 공지글을 DB에서 삭제한 후 목록화면으로 연결
			if ( service.notice_delete(id)==1 ) {
				common.fileDelete( vo.getFilepath(), request );
				
			}
			return "redirect:list";
		}
		
		
		
		
		
		
		//공지글 첨부파일 다운처리 요청
		@RequestMapping("/download")
		public void download(int id, HttpServletRequest request, HttpServletResponse response) {
			//해당 공지글 정보를 DB에서 조회해와
			//서버의 물리적영역에 있는 파일을 클라이언트컴에 다운로드처리
			NoticeVO vo = service.notice_info(id);
			
			try {
				common.fileDownload(vo.getFilename(), vo.getFilepath(), request, response );
			} catch (Exception e) {
				
			}
			
		}
		
		
		
		//공지글등록처리 요청
		@RequestMapping("/insert")
		public String register(NoticeVO vo, MultipartFile file, HttpServletRequest request) {
			//파일을 첨부한 경우
			if( ! file.isEmpty() ) {
				vo.setFilename( file.getOriginalFilename() );
				vo.setFilepath( common.fileUpload("notice", file, request) );
				
			}
			
			//화면에서 입력한 정보로 DB에 신규저장처리 후 목록화면으로 연결
			service.notice_register(vo);
			return "redirect:list";
			
		}
		
		
		
		//공지글등록화면 요청
		@RequestMapping("/register")
		public String register() {
			return "notice/register";
		}
		
		
		
		
		//공지글정보화면 요청
		@RequestMapping("/info")
		public String info(int id, Model model) {
			service.notice_read(id);
			//선택한 공지글정보 DB에서 조회해와 화면에 출력할 수 있도록 Model객체에 담기
			model.addAttribute("vo", service.notice_info(id));
			model.addAttribute("crlf", "\r\n");
			return "notice/info";
		}
		
	
		//공지글목록화면 요청
		@RequestMapping("/list")
		public String list(Model model, HttpSession session) {
			session.setAttribute("category", "no");
			//DB에서 조회한 정보를 화면에 출력할 수 있도록 Model객체에 담기
			model.addAttribute( "list", service.notice_list() ) ;
			return "notice/list";
			
		}
		
	
}
