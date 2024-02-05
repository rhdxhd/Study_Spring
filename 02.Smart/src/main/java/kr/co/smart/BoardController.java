package kr.co.smart;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import kr.co.smart.board.BoardCommentVO;
import kr.co.smart.board.BoardService;
import kr.co.smart.board.BoardVO;
import kr.co.smart.common.CommonUtility;
import kr.co.smart.common.FileVO;
import kr.co.smart.common.PageVO;

@Controller @RequestMapping("/board")
public class BoardController {
	@Autowired private BoardService service;

	//방명록 수정저장처리 요청
	@RequestMapping("/update")
	public String update(BoardVO vo, Model model, PageVO page, String remove  // "1, 2"
						, MultipartFile[] file, HttpServletRequest request) {
		//화면에서 입력한 정보로DB에 변경저장한 후 정보화면으로 연결
		//첨부파일이 있으면 BoardVO 의 fileList에 담기
		vo.setFileList( common.multipleFileUpload("board", file, request) );
		if( service.board_update(vo)==1 ) {
			//삭제된 첨부파일이 있으면 DB에서 삭제+물리적파일도 삭제
			if( ! remove.isEmpty() ) {
				List<FileVO> list = service.board_file_list(remove);
				if( service.board_file_delete(remove) > 0 ) {
					for( FileVO f : list ) {
						common.fileDelete(f.getFilepath(), request);
					}
				}
			}
		}
		
		model.addAttribute("id", vo.getId());
		model.addAttribute("url", "board/info");
		model.addAttribute("page", page);
		
		return "include/redirect";
	}
	
	
	//방명록 수정화면 요청
	@RequestMapping("/modify")
	public String modify(int id, Model model, PageVO page) {
		//해당 글의 정보를 DB에서 조회해와 수정화면에 출력
		model.addAttribute("vo", service.board_info(id));
		model.addAttribute("page", page);
		return "board/modify";
	}
	
	
	//댓글 삭제처리 요청
	@ResponseBody @RequestMapping("/comment/delete")
	public Map<String,Object> comment_delete( int id ) {
//	public Object comment_delete( int id ) {
		Map<String,Object> map = new HashMap<String,Object>();
		if( service.board_comment_delete(id)==1 ) {
			map.put("success", true);
		}else {
			map.put("success", false);
		}
		return map;
	}
	
	//댓글 변경저장처리 요청
	@ResponseBody @RequestMapping("/comment/update")
	public Object comment_update(BoardCommentVO vo) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		if( service.board_comment_update(vo)==1 ) {
			map.put("success", true);
			map.put("message", "성공^^");
			map.put("content", vo.getContent());
		}else{
			map.put("success", false);
			map.put("message", "실패ㅠㅠ");
		}
		return map;
	}
	
	//댓글 목록조회
	@RequestMapping("/comment/list/{board_id}")
	public String comment_list(@PathVariable int board_id, Model model) {
		model.addAttribute("list", service.board_comment_list(board_id) );
		model.addAttribute("crlf", "\r\n");
		model.addAttribute("lf", "\n");
		return "board/comment/comment_list";
	}
	
	//댓글 등록저장처리 요청
	@ResponseBody @RequestMapping("/comment/register")
	public boolean comment_register(BoardCommentVO vo) {
		return service.board_comment_register(vo) == 1 ? true : false;
	}
	
	//방명록 글삭제처리 요청
	@RequestMapping("/delete")
	public String delete(int id, PageVO page, Model model, HttpServletRequest request) {
		//첨부파일이 있다면 물리적인 파일을 삭제할 수 있도록 파일정보를 조회해둔다.
		List<FileVO> list = service.board_file_list(id);
		//해당 방명록 글을 DB에서 삭제하기
		//board를 삭제하면 table설계시 fk에 on delete cascade 에 의해 board_file은 함께 자동 삭제
		if( service.board_delete(id) == 1 ) {
			for( FileVO vo : list ) {
				common.fileDelete(vo.getFilepath(), request);
			}
		}
		
		//삭제후 목록화면으로 연결
		model.addAttribute("page", page);
		model.addAttribute("id", id);
		model.addAttribute("url", "board/list");
		return "include/redirect";
	}
	
	
	//방명록 첨부파일 다운로드 요청
	@RequestMapping("/download")
	public void download(int id, HttpServletRequest request
								, HttpServletResponse response) throws Exception{
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
	public String insert(BoardVO vo, MultipartFile[] file
						, HttpServletRequest request) {
		//첨부된 파일들을 BoardVO 의 fileList에 담기
		vo.setFileList( common.multipleFileUpload("board", file, request));
		
		//화면에서 입력한 정보로 DB에 신규삽입저장처리 -> 화면연결:목록
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
		//DB에서 방명록 글을 한 페이지 정보를 조회해와 화면에 출력할 수 있도록 Model에 담기
		model.addAttribute("page",  service.board_list(page) );
		return "board/list";
	}
	
}