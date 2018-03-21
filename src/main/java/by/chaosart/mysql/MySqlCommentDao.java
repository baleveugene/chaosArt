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

public class MySqlCommentDao {

	private Session session;
	
	public MySqlCommentDao(Session session) throws PersistException {
		this.session = session;
	}

	public void close() throws PersistException {
		try {
			session.close();
		} catch (Exception e) {
			throw new PersistException("Unable to close resourses. ", e);
		}	
	}

	public Comment create(Comment comment) throws PersistException {
		Comment persistInstance;
		try {
			Transaction trans = session.beginTransaction();	
			Serializable generatedId = session.save(comment);
			persistInstance = session.get(Comment.class, generatedId);
			trans.commit();
		} catch (Exception e) {
			throw new PersistException("Unable to record new data to DB.", e);
		} 
		return persistInstance;
	}
		
	public Comment read(String id) throws PersistException {
		Comment persistInstance;
		try {
			persistInstance = session.get(Comment.class, id);
		} catch (Exception e) {
			throw new PersistException("Record with PK = " + id
					+ " not found.", e);
		} 	
		return persistInstance;
	}

	public Comment readByText(String text) throws PersistException {
		Comment persistInstance = new Comment();
		try {
			Criteria criteria = session.createCriteria(Comment.class)
                    .add(Restrictions.eq("text", text));
			if(!criteria.list().isEmpty()){
				persistInstance = (Comment) criteria.list().listIterator().next();
			}	
		} catch (Exception e) {
			throw new PersistException("Record with PK = " + text
					+ " not found.", e);
		}
		return persistInstance;
	}
	
	public void update(Comment comment) throws PersistException {
		try {
			Transaction trans = session.beginTransaction();
			session.update(comment);
			trans.commit();
		} catch (Exception e) {
			throw new PersistException("Unable to update record.", e);
		}
	}
	

	public void delete(Comment comment) throws PersistException {
		try {
			Transaction trans = session.beginTransaction();
			session.delete(comment);
			trans.commit();
		} catch (Exception e) {
			throw new PersistException("Unable to delete record.", e);
		}
	}


	public List<Comment> getAll() throws PersistException {
		List<Comment> list;
		try {
			list = session.createCriteria(Comment.class).list();
		} catch (Exception e) {
			throw new PersistException("Unable to read data from DB.", e);
		}
		return list;
	}
	
	public List<Comment> getAll(Art artComment) throws PersistException {
		List<Comment> list;
		try {
			Criteria criteria = session.createCriteria(Comment.class)
                    .add(Restrictions.eq("artComment", artComment));
			list = criteria.list();
		} catch (Exception e) {
			throw new PersistException("Unable to read data from DB.", e);
		}
		return list;
	}
}
