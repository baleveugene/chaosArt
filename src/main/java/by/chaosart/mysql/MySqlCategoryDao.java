package by.chaosart.mysql;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import by.chaosart.dao.PersistException;
import by.chaosart.domain.*;

public class MySqlCategoryDao {

	private Session session;
	
	public MySqlCategoryDao(Session session) throws PersistException {
		this.session = session;
	}

	public void close() throws PersistException {
		try {
			session.close();
		} catch (Exception e) {
			throw new PersistException("Unable to close resourses. ", e);
		}	
	}
	
	public Category create(Category category) throws PersistException {
		Category persistInstance;
		try {
			Serializable generatedId = session.save(category);
			persistInstance = session.get(Category.class, generatedId);
		} catch (Exception e) {
			throw new PersistException("Unable to record new data to DB.", e);
		} 
		return persistInstance;
	}
		
	public Category read(String id) throws PersistException {
		Category persistInstance;
		try {
			persistInstance = session.get(Category.class, id);
		} catch (Exception e) {
			throw new PersistException("Record with PK = " + id
					+ " not found.", e);
		} 	
		return persistInstance;
	}
	
	public Category readByName(String name) throws PersistException {
		Category persistInstance = new Category();
		try {
			Criteria criteria = session.createCriteria(Category.class)
                    .add(Restrictions.eq("name", name));
			if(!criteria.list().isEmpty()){
				persistInstance = (Category) criteria.list().listIterator().next();
			}	
		} catch (Exception e) {
			throw new PersistException("Record with PK = " + name
					+ " not found.", e);
		}
		return persistInstance;
	}

	public void update(Category category) throws PersistException {
		try {
			Transaction trans = session.beginTransaction();
			session.update(category);
			trans.commit();
		} catch (Exception e) {
			throw new PersistException("Unable to update record.", e);
		}
	}
	
	public void delete(Category category) throws PersistException {
		try {
			Transaction trans = session.beginTransaction();
			session.delete(category);
			trans.commit();
		} catch (Exception e) {
			throw new PersistException("Unable to delete record.", e);
		}
	}

	public List<Category> getAll() throws PersistException {
		List<Category> list;
		try {
			list = session.createCriteria(Category.class).list();
		} catch (Exception e) {
			throw new PersistException("Unable to read data from DB.", e);
		}
		return list;
	}
}
