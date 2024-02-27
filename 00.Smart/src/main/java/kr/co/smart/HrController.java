package kr.co.smart;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.co.smart.hr.EmployeeVO;
import kr.co.smart.hr.HrService;

@Controller @RequestMapping("/hr")
public class HrController {
	@Autowired private HrService service;
	
	//신규사원등록 삽입저장처리 요청
	@RequestMapping("/insert")
	public String register(EmployeeVO vo) {
		//화면에서 입력한 정보로 DB에 삽입저장 -> 목록화면으로 연결
		service.employee_register(vo);
		return "redirect:list";
	}
	
	
	//신규사원등록 화면 요청
	@RequestMapping("/register")
	public String register(Model model) {
		//부서/업무/매니저를 선택할 수 있도록 DB에서 조회해와 화면에 출력-> Model 객체에 담기
		model.addAttribute("departments", service.hr_department_list());
		model.addAttribute("jobs", service.hr_job_list());
		model.addAttribute("managers", service.hr_manager_list());
		return "hr/register";
	}
	
	//사원정보 변경저장처리 요청
	@RequestMapping("/update")
	public String update(EmployeeVO vo) {
		//화면에서 변경입력한 정보로 DB에 변경저장 -> 정보화면으로 연결
		service.employee_update(vo);
		return "redirect:info?id="+vo.getEmployee_id();
	}
	
	
	//사원정보 수정화면 요청
	@RequestMapping("/modify")
	public String modify(Model model, int id) {
		//해당 사원정보를 DB에서 조회 -> 수정화면에 출력할 수 있도록 Model객체에 담기
		model.addAttribute("vo", service.employee_info(id) );
		
		//부서,업무를 변경할 수 있도록 부서/업무목록을 조회해와 화면에 출력할 수 있도록 Model객체에 담기
		model.addAttribute("departments", service.hr_department_list() );
		model.addAttribute("jobs", service.hr_job_list() );
		return "hr/modify";
	}
	
	
	
	//사원정보 삭제처리 요청
	@RequestMapping("/delete")
	public String delete(int id) {
		//해당 사원정보를 DB에서 삭제하기 -> 목록화면으로 연결
		service.employee_delete(id);
		return "redirect:list";
	}
	
	
	//사원정보조회화면 요청
	@RequestMapping("/info")
	public String info(int id, Model model) {
		//선택한 사원정보를 DB에서 조회 -> 정보화면에 출력할 수 있도록 Model객체에 담기
		model.addAttribute("vo", service.employee_info(id) );
		return "hr/info";
	}
	
	//사원목록화면 요청
	@RequestMapping("/list")
	public String list(HttpSession session, Model model
						, @RequestParam(defaultValue = "-1") int department_id) {
		session.setAttribute("category", "hr");
		
		//특정부서에 속한 사원을 조회할 수 있도록 부서목록을 DB에서 조회 -> Model객체에 담기
		model.addAttribute("departments", service.employee_department_list());
		
		//DB에서 사원목록을 조회해 와 목록화면에 출력 -> Model객체에 담기
		model.addAttribute("list", service.employee_list(department_id));
		
		model.addAttribute("department_id", department_id);
		
		return "hr/list";
	}
	
}
