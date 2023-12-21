package kr.co.smart;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.co.smart.customer.CustomerServiceiImpl;
import kr.co.smart.customer.CustomerVO;

@Controller
public class CustomerController {
	
	@Autowired private CustomerServiceiImpl service;
	
	//고객목록화면 요청
	@RequestMapping("/list.cu")
	public String list(Model model) {
		//DB에서 고객목록을 조회해와 화면에 출력
		List<CustomerVO> list = service.customer_list();
		//조회한 정보를 화면에 출력할 수 있도록 Model객체에 담기
		model.addAttribute("list", list);
		
		
		return "customer/list";
	}

	
	
	
}
