package kr.co.middle.customer;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
	@Autowired private SqlSession sql;
	
	
	
	public int customer_insert(CustomerVO vo) {
		return sql.insert("cu.insert", vo);
	}
	
	
	public int customer_update(CustomerVO vo) {
		return sql.update("cu.update", vo);
	}
	
	public int customer_delete(int id) {
		return sql.delete("cu.delete", id);
	}
	

	public List<CustomerVO> customer_list(String query) {
		// 파라미터 query가 있으면 customer_id로 검색하거나, name으로 검색하도록 쿼리문 작성
		return sql.selectList("cu.list", query);
	}
	
	
	

	public CustomerVO customer_info(int id) {
		//: 파라미터 id는 숫자를 받아야하고 customer_id가 같은 고객정보를 조회하는 쿼리문 작성
		return sql.selectOne("cu.info", id);
	}
}
