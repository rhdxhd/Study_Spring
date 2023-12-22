package kr.co.smart.customer;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class CustomerDAO implements CustomerService{
	@Autowired @Qualifier("hanul") private SqlSession sql;
	

	@Override
	public int customer_register(CustomerVO vo) {
		return sql.insert("customer.insert", vo);
	}

	@Override
	public List<CustomerVO> customer_list() {
		
		return sql.selectList("customer.list");
	}

	@Override
	public CustomerVO customer_info(int id) {
		
		return sql.selectOne("customer.info", id);
	}

	
	// 조회 : executeQuery
	// 삽인저장/변경저장/삭제: executeUpdate
	
	@Override
	public int customer_update(CustomerVO vo) {
		return sql.update("customer.update", vo);
	}

	@Override
	public int customer_delete(int id) {
		return sql.delete("customer.delete", id);
	}
	
	
	@Override
	public List<CustomerVO> customer_list(String name) {
		return sql.selectList("customer.list", name);
	}
	
	
}
