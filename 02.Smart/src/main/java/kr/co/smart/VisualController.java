package kr.co.smart;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import kr.co.smart.visual.VisualService;

//@Controller + @ResponseBody
@RestController
@RequestMapping("/visual")
public class VisualController {
	@Autowired private VisualService service;

	
	
	
	
	//부서원수 조회 요청
	@ResponseBody
	@RequestMapping("/department")
	public List<HashMap<String, Object>> department() {
		return service.department();
	
	}
	
	
	
	
	
}
