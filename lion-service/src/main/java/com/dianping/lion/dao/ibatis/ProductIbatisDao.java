package com.dianping.lion.dao.ibatis;

import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.dianping.lion.dao.ProductDao;
import com.dianping.lion.entity.Product;

public class ProductIbatisDao extends SqlMapClientDaoSupport implements ProductDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<Product> findAll() {
		return getSqlMapClientTemplate().queryForList("Product.findAll");
	}

	@Override
	public void delete(int id) {
		getSqlMapClientTemplate().delete("Product.deleteProduct", id);
	}

	@Override
	public Product findProductByID(int id) {
		return (Product)getSqlMapClientTemplate().queryForObject("Product.findByID", id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Product> findProductByTeamID(int teamId) {
		return getSqlMapClientTemplate().queryForList("Product.findProductByTeamID",teamId);
	}

	@Override
	public int save(Product product) {
		getSqlMapClientTemplate().insert("Product.insertProduct", product);
		return product.getId();
	}

	@Override
	public void update(Product product) {
		getSqlMapClientTemplate().update("Product.updateProduct", product);
	}

}
