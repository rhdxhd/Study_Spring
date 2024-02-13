package kr.co.smart;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.smart.visual.VisualService;

@RestController   //@Controller  + @ResponseBody
@RequestMapping("/visual")
public class VisualController {
	@Autowired private VisualService service;

	
	//부서원수 조회 요청
	//@ResponseBody 
	@RequestMapping("/department")
	public List<HashMap<String, Object>> department() {
		return service.department();
	}
	
	//상위3개부서의 채용인원수 년도별 조회 요청
	@RequestMapping("/hirement/top3/year")
	public Object hirement_top3_year() {
		List<HashMap<String, Object>> list = service.hirement_top3_year();
		//단위 추출
		Object[] keys = list.get(0).keySet().toArray();
		Arrays.sort(keys);
		keys = Arrays.copyOfRange(keys, 0, keys.length-1); //department_name 제외 : 0~10
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("list", list);
		map.put("unit", keys);
		
		return map;
	}
	@RequestMapping("/hirement/top3/month")
	public Object hirement_top3_month() {
		return service.hirement_top3_month();
	}
	
	
	
	//채용인원수 년도별 조회 요청
	@RequestMapping("/hirement/year")
	public Object hirement_year() {
		return service.hirement_year();
	}
	
	//채용인원수 월별 조회 요청
	@RequestMapping("/hirement/month")
	public Object hirement_month() {
		return service.hirement_month();
	}
	
}