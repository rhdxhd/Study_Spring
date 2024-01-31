package kr.co.smart;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import kr.co.smart.board.BoardService;
import kr.co.smart.board.BoardVO;
import kr.co.smart.common.CommonUtility;
import kr.co.smart.common.FileVO;
import kr.co.smart.common.PageVO;

@Controller @RequestMapping("/board")
public class BoardController {
	@Autowired private BoardService service;
	
	
	//방명록 수정화면 요청
	@RequestMapping("/modify")
	public String modify(int id, Model model, PageVO page) {
		//해당 글의 정보를 DB에서 조회해와 수정화면에 출력
		model.addAttribute("vo", service.board_info(id));
		model.addAttribute("page", page);
		return "board/modify";
	}
	
	
	
	
	
	//방명록 글삭제처리 요청
	@RequestMapping("/delete")
	public String delete(int id, PageVO page, Model model, HttpServletRequest request) {
		//첨부파일이 있다면 물리적인 파일을 삭제할 수 있도록 파일정보를 조회해둔다.
		List<FileVO> list = service.board_file_list(id);
		//해당 방명록 글을 DB에서 삭제하기
		//board를 삭제하면 table설계시 fk에 on delete cascade 에 의해 board_file은 함꼐 자동 삭제
		if ( service.board_delete(id) == 1 ) {
			for( FileVO vo : list ) {
				common.fileDelete(vo.getFilepath(), request);
			}
			
		}
		//삭제후 목록화면으로 연결
		model.addAttribute("page", page);
		model.addAttribute("id", id);
		model.addAttribute("url", "list");
		return "include/redirect";
	}
	
	
	
	
	//방명록 첨부파일 다운로드 요청
	@RequestMapping("/download")
	public void download(int id, HttpServletRequest request,
								HttpServletResponse response) throws Exception {
		//해당 파일정보를 조회해와 클라이언트에 다운로드하기 
		FileVO vo = service.board_file_info(id);
		common.fileDownload(vo.getFilename(), vo.getFilepath(), request, response);
				
	}
	
	
	
	
	
	//방명록 정보화면 요청
	@RequestMapping("/info")
	public String info(int id, Model model, PageVO page) {
		//해당 id의 정보를 DB에서 조회해와 정보화면에 출력할 수 있도록 Model에 담기
		service.board_read(id); //조회수변경
		model.addAttribute("vo", service.board_info(id));
		model.addAttribute("crlf", "\r\n");
		model.addAttribute("page", page);
		return "board/info";
	}
	
	
	
	
	@Autowired private CommonUtility common;
	
	//방명록 신규등록처리 요청
	@RequestMapping("/insert")
	public String insert(BoardVO vo, MultipartFile[] file, HttpServletRequest request) {
		//첨부된 파일들을 BoardVO 의 fileList에 담기
		vo.setFileList( common.multipleFileUpload("board", file, request));
		
		//화면에서 입력한 정보로 DB에 신규삽입저장처리 ->화면연결:목록
		service.board_register(vo);
		return "redirect:list";
	}
	
	
	
	//방명록 신규등록화면 요청
	@RequestMapping("/register")
	public String register() {
		return "board/register";
	}
	
	
	//방명록 목록화면 요청
	@RequestMapping("/list")
	public String list(PageVO page, Model model, HttpSession session) {
		session.setAttribute("category", "bo");
		//DB에서 방명록 글을 한 페이지 정보를 조회해와 화면에 출력할 수 있또록 Model에 담기
		model.addAttribute("page", service.board_list(page));		
		return "board/list";
	}
	
	
}
