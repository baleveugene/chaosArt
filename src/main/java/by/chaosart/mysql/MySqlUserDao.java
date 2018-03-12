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

public class MySqlUserDao {

	private Session session;
	
	public MySqlUserDao(Session session) throws PersistException {
		this.session = session;
	}

	public void close() throws PersistException {
		try {
			session.close();
		} catch (Exception e) {
			throw new PersistException("Unable to close resourses. ", e);
		}	
	}
	
	public User create(User user) throws PersistException {
		User persistInstance;
		try {
			Serializable generatedId = session.save(user);
			persistInstance = session.get(User.class, generatedId);
		} catch (Exception e) {
			throw new PersistException("Unable to record new data to DB.", e);
		} 
		return persistInstance;
	}
		
	public User read(String id) throws PersistException {
		User persistInstance;
		try {
			persistInstance = session.get(User.class, id);
		} catch (Exception e) {
			throw new PersistException("Record with PK = " + id
					+ " not found.", e);
		} 	
		return persistInstance;
	}
	
	public User readByLogin(String login) throws PersistException {
		User persistInstance = new User();
		try {
			Criteria criteria = session.createCriteria(User.class)
                    .add(Restrictions.eq("login", login));
			if(!criteria.list().isEmpty()){
				persistInstance = (User) criteria.list().listIterator().next();
			}
		} catch (Exception e) {
			throw new PersistException("Record with login = " + login
					+ " not found.", e);
		}
		return persistInstance;
	}

	public void update(User user) throws PersistException {
		try {
			Transaction trans = session.beginTransaction();
			session.update(user);
			trans.commit();
		} catch (Exception e) {
			throw new PersistException("Unable to update record.", e);
		}
	}
	
	public void delete(User user) throws PersistException {
		try {
			Transaction trans = session.beginTransaction();
			session.delete(user);
			trans.commit();
		} catch (Exception e) {
			throw new PersistException("Unable to delete record.", e);
		}
	}


	public List<User> getAll() throws PersistException {
		List<User> list;
		try {
			list = session.createCriteria(User.class).list();
		} catch (Exception e) {
			throw new PersistException("Unable to read data from DB.", e);
		}
		return list;
	}
}
