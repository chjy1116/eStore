package kr.ac.hansung.cse.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import kr.ac.hansung.cse.model.Product;

@Repository // component를 스캔해서 빈등록
@Transactional
public class ProductDao {

	@Autowired
	private SessionFactory sessionFactory;

	public Product getProductById(int id) {
		Session session = sessionFactory.getCurrentSession();
		Product product = session.get(Product.class, id);

		return product;
	}

	@SuppressWarnings("unchecked")
	public List<Product> getProducts() {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("from Product");
		List<Product> productList = query.list();

		return productList;
	}

	public void addProduct(Product product) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(product);//나는 나가보께에 나도 좀만 보껭배터리가 한 2시간정도 남았넹 한 10분볼거얌ㅋㅋ그 끌때 내 놋북꺼주라 오키오키
		session.flush();
	}

	public void deleteProduct(Product product) {
		Session session = sessionFactory.getCurrentSession();
		session.delete(product);
		session.flush();
	}

	public void editProduct(Product product) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(product);
		session.flush();
	}

}
