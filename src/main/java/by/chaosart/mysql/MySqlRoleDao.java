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

public class MySqlRoleDao {

	private Session session;
	
	public MySqlRoleDao(Session session) throws PersistException {
		this.session = session;
	}

	public void close() throws PersistException {
		try {
			session.close();
		} catch (Exception e) {
			throw new PersistException("Unable to close resourses. ", e);
		}	
	}
	
	public Role create(Role role) throws PersistException {
		Role persistInstance;
		try {
			Serializable generatedId = session.save(role);
			persistInstance = session.get(Role.class, generatedId);
		} catch (Exception e) {
			throw new PersistException("Unable to record new data to DB.", e);
		} 
		return persistInstance;
	}
		
	public Role read(String id) throws PersistException {
		Role persistInstance;
		try {
			persistInstance = session.get(Role.class, id);
		} catch (Exception e) {
			throw new PersistException("Record with PK = " + id
					+ " not found.", e);
		} 	
		return persistInstance;
	}
	
	public Role readByName(String name) throws PersistException {
		Role persistInstance = new Role();
		try {
			Criteria criteria = session.createCriteria(Role.class)
                    .add(Restrictions.eq("name", name));
			if(!criteria.list().isEmpty()){
				persistInstance = (Role) criteria.list().listIterator().next();
			}	
		} catch (Exception e) {
			throw new PersistException("Record with PK = " + name
					+ " not found.", e);
		}
		return persistInstance;
	}

	public void update(Role role) throws PersistException {
		try {
			Transaction trans = session.beginTransaction();
			session.update(role);
			trans.commit();				
		} catch (Exception e) {
			throw new PersistException("Unable to update record.", e);
		}
	}
	

	public void delete(Role role) throws PersistException {
		try {
			Transaction trans = session.beginTransaction();
			session.delete(role);
			trans.commit();		
		} catch (Exception e) {
			throw new PersistException("Unable to delete record.", e);
		}
	}


	public List<Role> getAll() throws PersistException {
		List<Role> list;
		try {
			list = session.createCriteria(Role.class).list();
		} catch (Exception e) {
			throw new PersistException("Unable to read data from DB.", e);
		}
		return list;
	}
}
