package kr.co.smart;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
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
	
	
	private HashMap<String, Object> yearRange(HashMap<String, Object> map) {
		int begin = Integer.parseInt( map.get("begin").toString());
		int end = Integer.parseInt( map.get("end").toString());
		String range= "";
		for ( int year = begin; year <= end; year++ ) {
			range += (range.isEmpty()? "": ", " ) + year + "\"" + year + "년\"" ;
			//2012 "2012년", 2013 "2013년", 2014 "2014년"
		}
		map.put("range", range);
		return map;
		
//		 '2012년' "2012년", '2013년' "2013년", '2014년' "2014년"
//		 , '2015년' "2015년", '2016년' "2016년"
//		 , '2017년' "2017년", '2018년' "2018년", '2019년' "2019년"
//         , '2020년' "2020년", '2021년' "2021년", '2022년' "2022년"
//         , '2023년' "2023년", '2024년' "2024년"	
	}
	
	
	
	//상위3개부서의 채용인원수 년도별 조회 요청
	@RequestMapping("/hirement/top3/year")
	public Object hirement_top3_year(@RequestBody HashMap<String, Object> map) { 
		List<HashMap<String, Object>> list = service.hirement_top3_year( yearRange(map) ); //map을 파라메터로 전달(map으로 데이터 전달)
		
		if(list.size() > 0) {
			//단위 추출
			Object[] keys = list.get(0).keySet().toArray();
			Arrays.sort(keys);
			keys = Arrays.copyOfRange(keys, 0, keys.length-1); //department_name 제외 : 0~10
			//HashMap<String, Object> map = new HashMap<String, Object>();
			//map.put("list", list);
			map.put("unit", keys);
		}
		
		map.put("list", list);
		return map;
	}
	
	@RequestMapping("/hirement/top3/month")
	public Object hirement_top3_month() {
		List<HashMap<String, Object>>  list = service.hirement_top3_month();
		
		HashMap<String, Object>  map = new HashMap<String, Object>();
		map.put("list", list);
		
		Object[] keys = list.get(0).keySet().toArray();
		Arrays.sort(keys);
		keys = Arrays.copyOfRange(keys, 0, keys.length-1); //department_name 제외 : 0~10
		
		map.put("unit", keys);
		return map; 
	}
	
	
	
	//채용인원수 년도별 조회 요청
	@RequestMapping("/hirement/year")
	public Object hirement_year(@RequestBody HashMap<String, Object> map) {  
		return service.hirement_year(map);
	}
	
	//채용인원수 월별 조회 요청
	@RequestMapping("/hirement/month")
	public Object hirement_month() {
		return service.hirement_month();
	}
	
}