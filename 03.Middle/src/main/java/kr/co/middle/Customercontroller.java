package kr.co.middle;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import kr.co.middle.customer.CustomerService;
import kr.co.middle.customer.CustomerVO;

@RestController // @Controller + @ResponseBody
@RequestMapping(value="/customer", produces = "text/plain; charset=utf-8")
public class Customercontroller {
	@Autowired private CustomerService service;
	
	@RequestMapping("/insert")
	public void insert(String vo) {
		CustomerVO customer = new Gson().fromJson(vo, CustomerVO.class);
		service.customer_insert(customer);
	}
	

	@RequestMapping("/update")
	public void update(String vo) {
		// vo : {customer_id: '44', name: '변경이름', gender: '남', email: 'change@naver.com', phone: '010-1234-5678'}
	CustomerVO customer = new Gson().fromJson(vo, CustomerVO.class);
	service.customer_update(customer);
	}
	
	
	//고객목록조회 요청
	//@ResponseBody
	@RequestMapping("/list")
	public String list(String query) {
		List<CustomerVO> list = service.customer_list(query);
		//System.out.println(new Gson().toJson(list) );
		return new Gson().toJson(list);
		
	}
	
	//- customer/info 요청시
	//: 파라미터 id는 숫자를 받아야 하고 customer_id가 같은 고객정보를 조회하는 쿼리문 작성
	@RequestMapping("/info")
	public String info(int id) {
		CustomerVO vo = service.customer_info(id);
		return new Gson().toJson(vo);
	}
	
	@RequestMapping("/delete")
	public void delete(int id) {
		service.customer_delete(id);
		
	}
	
	
	// - 각 요청에 대한 처리시 조회해온 데이터를 Gson 객체를 사용해 json 형태의 문자열을 만든다.
	// json 문자열을 PrintWriter 의 print 메소드를 사용해 출력을 내보내지 않고
	// System.out.print로 출력해본다.
	
}
