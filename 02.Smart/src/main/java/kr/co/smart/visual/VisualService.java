package kr.co.smart.visual;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class VisualService {
	@Autowired @Qualifier("hr") private SqlSession sql;
	
	//부서별 사원수 조회
	public List<HashMap<String, Object>> department() {
		return sql.selectList("visual.department");
	}
	//채용인원수(년도별/월별) 조회
	public List<HashMap<String, Object>> hirement() {
		return null;
	}
}
